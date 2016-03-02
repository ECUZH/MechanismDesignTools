package ch.uzh.ifi.MechanismDesignToolsNEW;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import ch.uzh.ifi.MechanismDesignPrimitives.AtomicBid;
import ch.uzh.ifi.MechanismDesignPrimitives.CombinatorialType;
import ch.uzh.ifi.MechanismDesignPrimitives.Type;
import ch.uzh.ifi.Mechanisms.DoubleSidedMarket;
import ch.uzh.ifi.Mechanisms.GeneralPlannerFactory;
import ch.uzh.ifi.Mechanisms.IPlannerFactory;
import ch.uzh.ifi.Mechanisms.Planner;

/*
 * The test performs evaluation of the market efficiency, profits, BB violations etc.
 * by solving many sample games in which agents play according to the precomputed BNE.
 */
public class QueryMarketBenchmark {

	public static void main(String[] args) throws Exception 
	{
		LocalTime time = LocalTime.now();
		System.out.println("Launch the benchmark: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
		
		int numberOfBuyers = 1;
		int numberOfSellers = 5;
		int numberOfPlans = 2;
		int minSellersPerPlan = 2;
		int maxSellersPerPlan = 2;
		boolean isInjectable = true;
		double errorVarianceFactor = 3.;
		
		String paymentRule = "VCG";
		String paymentCorrectionRule = "None";
		double buyersShade = 0.97;
		double sellersShade = 1.02;
		
		int numberOfSamples = 500000;
		double efficiency = 0.;
		double budgetAmount = 0.;
		int budgetCounter = 0;
		double profitsOfBuyer = 0.;
		double profitsOfSeller = 0.;
		Random generator = new Random();
		
		for(int i = 0; i < numberOfSamples; ++i)
		{
			List<Integer> items = new LinkedList<Integer>();
			items.add(0);											//a query
			List<Type> bids = new LinkedList<Type>();
			
			//Construct a buyer's type
			double buyersTrueValue = generator.nextDouble();
			AtomicBid atomB1 = new AtomicBid(1, items, buyersShade*buyersTrueValue);
			atomB1.setTypeComponent("isSeller", 0.0);
			CombinatorialType b1 = new CombinatorialType(); 		//Buyer's type
			b1.addAtomicBid(atomB1);
			bids.add(b1);
			
			//Construct sellers' types
			for(int j = 0; j < numberOfSellers; ++j)
			{
				double sellerTrueValue = generator.nextDouble();
				AtomicBid atomS = new AtomicBid(2+j, items, sellersShade*sellerTrueValue);
				atomS.setTypeComponent("isSeller", 1.0);
				CombinatorialType s = new CombinatorialType(); 		//Seller's type
				s.addAtomicBid(atomS);
				bids.add(s);
			}
			
			IPlannerFactory plannerFactory = new GeneralPlannerFactory(numberOfBuyers, numberOfSellers, bids, isInjectable, numberOfPlans, minSellersPerPlan, maxSellersPerPlan, errorVarianceFactor);
			Planner planner = plannerFactory.producePlanner();
			DoubleSidedMarket market  = new DoubleSidedMarket(numberOfBuyers, numberOfSellers, bids, planner);
			market.setPaymentRule(paymentRule);
			market.setPaymentCorrectionRule(paymentCorrectionRule);
			
			
			market.solveIt();
			efficiency += market.getAllocation().getAllocatedWelfare();
			if(market.getAllocation().getAllocatedWelfare() > 0)
			{
				
				double totalPayment = 0.;
				for(int j = 0; j < market.getPayments().length; ++j)
					totalPayment += market.getPayments()[j];
				
				if(totalPayment < -1e-4)
				{
					budgetCounter+= 1;
					budgetAmount += totalPayment;
				}
			}
			else if (market.getAllocation().getAllocatedWelfare() < 0)
			{
				budgetAmount += market.getAllocation().getAllocatedWelfare();
				budgetCounter+= 1;
			}
			
			double itsValue = 0.;
			double itsCost  = 0.;
			if( market.getAllocation().getNumberOfAllocatedAuctioneers() > 0 && market.getAllocation().getAllocatedWelfare() >0)
			{
				itsValue = market.getAllocation().getAuctioneersAllocatedValue(0) / buyersShade;
				itsCost = market.getAllocation().getTotalAllocatedBiddersValue(0) / sellersShade;
				
				profitsOfBuyer  += itsValue - market.getPayments()[0];	
				//System.out.println("Val="+itsValue + " p="+market.getPayments()[0] + " sw="+market.getAllocation().getWelfare());
				profitsOfSeller += itsCost;
				for(int j = 0; j < market.getPayments().length - 1; ++j)
					profitsOfSeller -= market.getPayments()[1 + j];
			}
		}
		
		System.out.println("Total efficiency is: " + (efficiency/numberOfSamples) + " budget2utility ratio: " + ((double)budgetAmount/efficiency) + 
				           " budgetCounter=" + ((double)budgetCounter/(double)numberOfSamples) + " profitsOfBuyer: " + profitsOfBuyer/numberOfSamples +
				           " profitsOfSellers: " + profitsOfSeller / numberOfSamples);
		time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
	}
}
