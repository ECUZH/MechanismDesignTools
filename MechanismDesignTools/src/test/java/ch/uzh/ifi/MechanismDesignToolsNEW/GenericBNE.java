package ch.uzh.ifi.MechanismDesignToolsNEW;

//import java.time.LocalTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.*;

import ch.uzh.ifi.MechanismDesignPrimitives.AtomicBid;
import ch.uzh.ifi.MechanismDesignPrimitives.CombinatorialType;
import ch.uzh.ifi.MechanismDesignPrimitives.Type;
import ch.uzh.ifi.Mechanisms.DoubleSidedMarket;
import ch.uzh.ifi.Mechanisms.DoubleSidedMarketFactory;
import ch.uzh.ifi.Mechanisms.GeneralPlannerFactory;
import ch.uzh.ifi.Mechanisms.IMechanismFactory;
import ch.uzh.ifi.Mechanisms.IPlannerFactory;
import ch.uzh.ifi.Mechanisms.JoinPlannerFactory;
import ch.uzh.ifi.Mechanisms.SimplePlannerFactory;
//import mpi.*;


public class GenericBNE 
{
	/*
	 * @param arg[0] - number of buyers
	 * @param arg[1] - number of sellers
	 * @param arg[2] - number of samples
	 * @param arg[3] - number of plans a planer should generate
	 * @param arg[4] - minimum number of sellers per plan (this have an impact on the influence of sellers)
	 * @param arg[5] - maximum number of sellers per plan (this have an impact on the influence of sellers)
	 * @param arg[6] - 1 if the planner should inject estimations errors and 0 otherwise
	 * @param arg[7] - number of threads per process
	 * @param arg[8] - the payment rule to be used (Threshold or VCG)
	 * @param arg[9] - the payment correction rule to be used (Trim, Penalty, VCG or None)
	 * @param arg[10] - the planner type to be used (General or Simple)
	 * @param arg[11] -
	 * @param arg[12] -
	 * @param arg[13] - optimization engine to be used for a global stochastic optimization subproblem ('stochastic' or 'deterministic')
	 * @param arg[14] - 0 if to launch from the beginning, 1 if to launch from the previously saved state
	 */
	public static void main(String[] args)
	{
		//org.apache.logging.log4j.BasicConfigurator.configure();
		Logger logger = LogManager.getLogger(GenericBNE.class);
		logger.info("Number of args: "+args.length);
		
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
		int maxNumberOfSellers = Integer.parseInt(args[6 + offset]);	if( (maxNumberOfSellers > numberOfSellers) || (maxNumberOfSellers > minNumberOfSellers))  throw new RuntimeException("Wrong max number of sellers");
		boolean isInjectable   = Integer.parseInt(args[7 + offset]) == 1 ? true : false;
		int numberOfThreads    = Integer.parseInt(args[8 + offset]);	if( numberOfThreads < 1 )	throw new RuntimeException("The number of threads should be at least 1");
		String paymentRule	   = args[9 + offset];						if( !(paymentRule.equals("VCG") || paymentRule.equals("Threshold")) ) throw new RuntimeException("Wrong payment rule specified: " + paymentRule);
		String paymentCorrectionRule = args[10 + offset];				if( !(paymentCorrectionRule.equals("Trim") || paymentCorrectionRule.equals("Penalty") || paymentCorrectionRule.equals("VCG") || paymentCorrectionRule.equals("None")) ) throw new RuntimeException("Wrong payment Correction Rule");
		String plannerType     = args[11 + offset];						if( !(plannerType.equals("General") || plannerType.equals("Simple") || plannerType.equals("Join")) ) throw new RuntimeException("Wrong planner type specified:" + plannerType );
		String utilityEngine   = args[12 + offset];						if( !(utilityEngine.equals("MT") || utilityEngine.equals("MPI") ) ) throw new RuntimeException("Wrong utility computation engine specified: " + utilityEngine);
		String optimizationEngine=args[13+ offset];						if( !(optimizationEngine.equals("stochastic") || optimizationEngine.equals("deterministic") ) ) throw new RuntimeException("Wrong local optimization engine specified: " + optimizationEngine);
		int loadState 		   = Integer.parseInt(args[14 + offset]);	if( loadState != 0 && loadState != 1 )  throw new RuntimeException("Wrong load state");

		//1.5. Constraints on possible input parameters combinations
		if( numberOfSellers == 3  && numberOfPlans != 2 ) 		throw new RuntimeException("Wrong number of plans for the scenarion with  3 sellers: " + numberOfPlans);
		if( numberOfSellers == 10 && numberOfPlans != 5 ) 		throw new RuntimeException("Wrong number of plans for the scenarion with 10 sellers: " + numberOfPlans);
		if( numberOfSellers == 20 && numberOfPlans != 10) 		throw new RuntimeException("Wrong number of plans for the scenarion with 20 sellers: " + numberOfPlans);
		if( plannerType.equals("Simple") && numberOfSellers !=3)throw new RuntimeException("Wrong number of sellers for this planner");
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
		if( plannerType.equals("General"))
		{
			double errorVarianceFactor = 3.;
			plannerFactory = new GeneralPlannerFactory(numberOfBuyers, numberOfSellers, bids, isInjectable, numberOfPlans, minNumberOfSellers, maxNumberOfSellers, errorVarianceFactor);
		}
		else if (plannerType.equals("Simple"))
		{
			plannerFactory = new SimplePlannerFactory(numberOfBuyers, numberOfSellers, bids, isInjectable);
		}
		else if( plannerType.equals("Join"))
		{
			plannerFactory = new JoinPlannerFactory(numberOfBuyers, numberOfSellers, bids, isInjectable, numberOfPlans, minNumberOfSellers, maxNumberOfSellers);
		}
		else
		{
			logger.error("Wrong planner type specified: " + plannerType);
			return;
		}
		
		DoubleSidedMarket market = new DoubleSidedMarket(numberOfBuyers, numberOfSellers, bids, plannerFactory.producePlanner());
		market.setPaymentRule( paymentRule );
		market.setPaymentCorrectionRule( paymentCorrectionRule );
		
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
			throw new RuntimeException("MPI temporary not supported");
			//MPI.Init(args);
			//utilityComputationEngineFactory = new MPIUtilityComputationEngineFactory();
		}
		else if ( utilityEngine.equals("MT") )
		{
			utilityComputationEngineFactory = new MTUtilityComputationEngineFactory();
		}
		else throw new RuntimeException("Cannot create a utility computation factory.");
		
		IMechanismFactory[] dsmFactory = {new DoubleSidedMarketFactory(numberOfBuyers, numberOfSellers, plannerFactory, paymentRule, paymentCorrectionRule)};
		
		IBinSortingStrategy binSortingStrategy;
		
		if( settings.getNumberOfBins() == 2 )
			binSortingStrategy = new DoubleAuctionBinSortingStrategy();
		else if (settings.getNumberOfBins() == numberOfBuyers + numberOfSellers)
			binSortingStrategy = new AllDifferentBinSortingStrategy();
		else
			throw new RuntimeException("Unsupported bin sorting strategy specified.");
		
		logger.info("Random number of tuples, random error injection(" + (isInjectable ? "true" : "false") + 
					"), #Buyers= "+ numberOfBuyers + " #Sellers=" + numberOfSellers);
		logger.info(paymentRule + " " + paymentCorrectionRule + " + " + "BNESolver(market, bids, "+settings.getNumberOfBins()+", "+
				    settings.getNumberOfGridPoints()+", "+numberOfSamples+", strategies, " + plannerType + "Planner, " + 
				    numberOfThreads +" ), eps=" + settings.getEpsilon()+ " (use " + utilityEngine + ")");
		
		BNESolver bne = new BNESolver(market, bids, strategies, utilityComputationEngineFactory, dsmFactory, null, binSortingStrategy, settings, null );
		bne.setPrecision( settings.getEpsilon() );
		bne.sortToBins();
		
		if(loadState == 1)
		{
			logger.info("Current state loaded from a file...");
			bne.loadState();
		}
		
		//6. Solve it
		bne.solveIt();
		logger.info(bne.toString());
		       
		//if( utilityEngine.equals("MPI") )
		//	MPI.Finalize();
	}
}
