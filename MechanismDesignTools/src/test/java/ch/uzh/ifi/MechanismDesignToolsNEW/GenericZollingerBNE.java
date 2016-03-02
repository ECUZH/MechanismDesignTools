package ch.uzh.ifi.MechanismDesignToolsNEW;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

//import mpi.MPI;

//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;

import ch.uzh.ifi.MechanismDesignPrimitives.AtomicBid;
import ch.uzh.ifi.MechanismDesignPrimitives.CombinatorialType;
import ch.uzh.ifi.MechanismDesignPrimitives.Type;
import ch.uzh.ifi.Mechanisms.FragmentedProbabilisticPlannerFactory;
import ch.uzh.ifi.Mechanisms.GeneralProbabilisticPlannerFactory;
import ch.uzh.ifi.Mechanisms.IMechanismFactory;
import ch.uzh.ifi.Mechanisms.IPlannerFactory;
import ch.uzh.ifi.Mechanisms.ZollingerMechanism;
import ch.uzh.ifi.Mechanisms.ZollingerMechanismFactory;

public class GenericZollingerBNE {

	public static void main(String[] args) 
	{
		//org.apache.log4j.BasicConfigurator.configure();
		//Logger logger = LogManager.getLogger(GenericZollingerBNE.class);
		//logger.info("Number of args: "+args.length);
		
		int numberOfArguments = 15;
		int offset = 0;
		if( args.length == numberOfArguments)
			offset = 0;
		else if( args.length == numberOfArguments + 3 )	//offset caused by MPI parameters
			offset = 3;
		else
			throw new RuntimeException("Wrong number of input parameters: " + args.length);
			
		//1. Parse command line arguments
		int numberOfBuyers = Integer.parseInt(args[0 + offset]);		if( numberOfBuyers < 1 ) 	throw new RuntimeException("Wrong number of buyers: " + numberOfBuyers);
		int numberOfSellers= Integer.parseInt(args[1 + offset]);		if( numberOfSellers <=0 )	throw new RuntimeException("The number of sellers should be positive: "+ numberOfSellers);
		int numberOfBins   = Integer.parseInt(args[2 + offset]);
		int numberOfSamples= Integer.parseInt(args[3 + offset]);		if( numberOfSamples <=0 ) 	throw new RuntimeException("The number of samples should be positive: "+ numberOfSamples);
		int numberOfPlans  = Integer.parseInt(args[4 + offset]); 		if( numberOfPlans <= 0  )	throw new RuntimeException("Negative number of plans");
		int minNumberOfSellers = Integer.parseInt(args[5 + offset]); 	if( minNumberOfSellers > numberOfSellers) throw new RuntimeException("The number of sellers per plan cannot be larger than the total number of sellers");
		int maxNumberOfSellers = Integer.parseInt(args[6 + offset]);	if( (maxNumberOfSellers > numberOfSellers) || (minNumberOfSellers > maxNumberOfSellers))  throw new RuntimeException("Wrong max number of sellers");
		boolean isInjectable   = Integer.parseInt(args[7 + offset]) == 1 ? true : false;
		int numberOfThreads    = Integer.parseInt(args[8 + offset]);	if( numberOfThreads < 1 )	throw new RuntimeException("The number of threads should be at least 1");
		String allocationRule	   = args[9 + offset];					if( !(allocationRule.equals("BuyerUtility") || allocationRule.equals("SocialWelfare")) ) throw new RuntimeException("Wrong payment rule specified: " + allocationRule);
		String paymentRule	   = args[10 + offset];						if( !(paymentRule.equals("FirstPrice") || paymentRule.equals("SecondPrice")) ) throw new RuntimeException("Wrong payment rule specified: " + paymentRule);
		String plannerType     = args[11 + offset];						if( !(plannerType.equals("GeneralProbabilistic") || plannerType.equals("FragmentedProbabilistic")) ) throw new RuntimeException("Wrong planner type specified:" + plannerType );
		String utilityEngine   = args[12 + offset];						if( !(utilityEngine.equals("MT") || utilityEngine.equals("MPI") ) ) throw new RuntimeException("Wrong utility computation engine specified: " + utilityEngine);
		String optimizationEngine=args[13+ offset];						if( !(optimizationEngine.equals("stochastic") || optimizationEngine.equals("deterministic") ) ) throw new RuntimeException("Wrong local optimization engine specified: " + optimizationEngine);
		int loadState 		   = Integer.parseInt(args[14 + offset]);	if( loadState != 0 && loadState != 1 )  throw new RuntimeException("Wrong load state");

		//1.5. Constraints on possible input parameters combinations
		//if( numberOfSellers == 3  && numberOfPlans != 2 ) 		throw new RuntimeException("Wrong number of plans for the scenarion with  3 sellers: " + numberOfPlans);
		if( numberOfSellers == 10 && numberOfPlans != 5 ) 		throw new RuntimeException("Wrong number of plans for the scenarion with 10 sellers: " + numberOfPlans);
		if( numberOfSellers == 20 && numberOfPlans != 10) 		throw new RuntimeException("Wrong number of plans for the scenarion with 20 sellers: " + numberOfPlans);
		//if( plannerType.equals("Simple") && numberOfSellers !=3)throw new RuntimeException("Wrong number of sellers for this planner");
		if( (args.length == numberOfArguments + 3) && (! utilityEngine.equals("MPI") ) ) throw new RuntimeException("Wrong number of arguments or wrong utility computation engine specified.");
		
		//2. Create a query (a dummy object, not really used by BNE approximation)
		List<Type> bids = new LinkedList<Type>();
		List<Integer> items = new LinkedList<Integer>();
		items.add(0);									//a query
		
		//3. Generate types of agents
		Random generator = new Random();
		generator.setSeed(123456);
		for(int i = 0; i < numberOfBuyers; ++i)
		{
			AtomicBid atomB = new AtomicBid( i+1, items, generator.nextDouble() );
			atomB.setTypeComponent("isSeller", 0.0);
			atomB.setTypeComponent("Distribution", 1.0);
			atomB.setTypeComponent("minValue", 0.0);
			atomB.setTypeComponent("maxValue", 1.0);
			CombinatorialType b = new CombinatorialType(); //Buyer's type
			b.addAtomicBid(atomB);
			bids.add(b);
		}
		
		for(int i = 0; i < numberOfSellers; ++i)
		{
			AtomicBid atomS = new AtomicBid( numberOfBuyers + 1 + i , items, generator.nextDouble() );
			atomS.setTypeComponent("isSeller", 1.0);
			atomS.setTypeComponent("Distribution", 1.0);
			atomS.setTypeComponent("minValue", 0.0);
			atomS.setTypeComponent("maxValue", 1.0);
			CombinatorialType s = new CombinatorialType();//Seller's type
			s.addAtomicBid(atomS);
			bids.add(s);
		}
		
		//4. Create a planner and a market
		IPlannerFactory plannerFactory;
		if( plannerType.equals("FragmentedProbabilistic"))
		{
			plannerFactory = new FragmentedProbabilisticPlannerFactory(numberOfBuyers, numberOfSellers, bids, isInjectable, numberOfPlans, minNumberOfSellers, maxNumberOfSellers);
		}
		else
		{
			//logger.error("Wrong planner type specified: " + plannerType);
			return;
		}
		
		ZollingerMechanism market = new ZollingerMechanism(numberOfBuyers, plannerFactory.producePlanner().generatePlans());
		market.setAllocationRule("SocialWelfare");
		market.setPaymentRule( paymentRule );
		
		//5. Configure the BNE Solver
		BNESettings settings = new BNESettings();
		settings.setNumberOfBins(numberOfBins);
		settings.setNumberOfGridPoints(20);
		settings.setNumberOfSamples(numberOfSamples);
		settings.setNumberOfThreads(numberOfThreads);
		settings.setEpsilon(1e-3);
		
		if( optimizationEngine.equals("deterministic"))
			settings.setLocalOptimizerFactory(new PatternSearchFactory() );
		else if( optimizationEngine.equals("stochastic"))
			settings.setLocalOptimizerFactory(new StochasticPatternSearchFactory() );
		else throw new RuntimeException("wrong local optimization engine specified: " + optimizationEngine);
		
		ShavingStrategy[] strategies = new ShavingStrategy[1];
		strategies[0] = new MultiplicativeShavingStrategy();
		
		IUtilityComputationEngineFactory utilityComputationEngineFactory;
		if( utilityEngine.equals("MPI") )
		{
			throw new RuntimeException("Unsupported");
			//MPI.Init(args);
			//utilityComputationEngineFactory = new MPIUtilityComputationEngineFactory();
		}
		else if ( utilityEngine.equals("MT") )
		{
			utilityComputationEngineFactory = new MTUtilityComputationEngineFactory();
		}
		else throw new RuntimeException("Cannot create a utility computation factory.");
		
		IMechanismFactory[] rcaFactory = {new ZollingerMechanismFactory(numberOfBuyers, numberOfSellers, plannerFactory, paymentRule, allocationRule)};
		
		IBinSortingStrategy binSortingStrategy;
		
		
		binSortingStrategy = new ReverseAuctionBinSortingStrategy();
		
		
		//logger.info("Random number of tuples, random error injection(" + (isInjectable ? "true" : "false") + 
		//			"), #Buyers= "+ numberOfBuyers + " #Sellers=" + numberOfSellers);
		//logger.info(allocationRule + " + " + paymentRule + " + " + "BNESolver(market, bids, "+settings.getNumberOfBins()+", "+
		//		    settings.getNumberOfGridPoints()+", "+numberOfSamples+", strategies, " + plannerType + "Planner, " + 
		//		    numberOfThreads +" ), eps=" + settings.getEpsilon()+ " (use " + utilityEngine + ")");
		
		BNESolver bne = new BNESolver(market, bids, strategies, utilityComputationEngineFactory, rcaFactory, null, binSortingStrategy, settings, null );
		bne.setPrecision( settings.getEpsilon() );
		bne.sortToBins();
		
		if(loadState == 1)
		{
			//logger.info("Current state loaded from a file...");
			bne.loadState();
		}
		
		//6. Solve it
		bne.solveIt();
		//logger.info(bne.toString());
		       
		//if( utilityEngine.equals("MPI") )
		//	MPI.Finalize();
	}

}
