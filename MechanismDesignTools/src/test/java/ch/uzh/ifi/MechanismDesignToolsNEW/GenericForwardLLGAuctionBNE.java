package ch.uzh.ifi.MechanismDesignToolsNEW;

import ilog.concert.IloException;
import ilog.cplex.IloCplex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.uzh.ifi.DomainGenerators.DomainGeneratorLLG;
import ch.uzh.ifi.MechanismDesignPrimitives.AtomicBid;
import ch.uzh.ifi.MechanismDesignPrimitives.CombinatorialType;
import ch.uzh.ifi.MechanismDesignPrimitives.IDomainGenerator;
import ch.uzh.ifi.MechanismDesignPrimitives.Type;
import ch.uzh.ifi.Mechanisms.CAXOR;
import ch.uzh.ifi.Mechanisms.CAXORFactory;
import ch.uzh.ifi.Mechanisms.IMechanismFactory;

public class GenericForwardLLGAuctionBNE 
{

	private static final Logger _logger = LogManager.getLogger(GenericForwardLLGAuctionBNE.class);
	
	public static void main(String[] args) throws IloException 
	{
		int numberOfArguments = 8;
		int offset = 0;
		if( args.length == numberOfArguments)
			offset = 0;
		else if( args.length == numberOfArguments + 3 )	//offset caused by MPI parameters
			offset = 3;
		else
			throw new RuntimeException("Wrong number of input parameters: " + args.length);
			
		//1. Parse command line arguments		
		int numberOfBins   = Integer.parseInt(args[0 + offset]);		if( numberOfBins    <= 0)	throw new RuntimeException("The number of bins should be posititve");
		int numberOfSamples= Integer.parseInt(args[1 + offset]);		if( numberOfSamples <= 0) 	throw new RuntimeException("The number of samples should be positive: "+ numberOfSamples);
		double costsUpperBound = Double.parseDouble(args[2 + offset]);  if( costsUpperBound < 0 || costsUpperBound > 1) throw new RuntimeException("Wrong costs upper bound specified: " + costsUpperBound );
		int numberOfThreads    = Integer.parseInt(args[3 + offset]);	if( numberOfThreads  < 1 )	throw new RuntimeException("The number of threads should be at least 1");
		String paymentRule	   = args[4 + offset];						if( !(paymentRule.equals("VCG_LLG") || paymentRule.equals("CORE_LLG") || paymentRule.equals("CORE_SH_LLG")) ) throw new RuntimeException("Wrong payment rule specified: " + paymentRule);
		String utilityEngine   = args[5 + offset];						if( !(utilityEngine.equals("MT") || utilityEngine.equals("MPI") ) ) throw new RuntimeException("Wrong utility computation engine specified: " + utilityEngine);
		String optimizationEngine=args[6+ offset];						if( !(optimizationEngine.equals("stochastic") || optimizationEngine.equals("deterministic") ) ) throw new RuntimeException("Wrong local optimization engine specified: " + optimizationEngine);
		int loadState 		   = Integer.parseInt(args[7 + offset]);	if( loadState != 0 && loadState != 1 )  throw new RuntimeException("Wrong load state");
				
		int numberOfBuyers = 3;
		int numberOfItems = 2;

		boolean isMultiplicativeShading = false;
		List<Double> costsRange = new ArrayList<Double>();
		costsRange.add(costsUpperBound);
		costsRange.add(costsUpperBound);
		System.out.println("Costs: " + costsRange.toString());
		
		List<Integer> items = new LinkedList<Integer>();
		for(int i = 0; i < numberOfItems; ++i)
			items.add(i+1);													//Goods in the auction
		
		//3. Generate types of agents
		//First Local bidder
		List<Integer> bundle = Arrays.asList( items.get(0) );
		double marginalValue = 0.1;
		AtomicBid atom11 = new AtomicBid(1, bundle, marginalValue);
		atom11.setTypeComponent( AtomicBid.IsBidder, 1.0);

		CombinatorialType t1 = new CombinatorialType();
		t1.addAtomicBid(atom11);
				
		//Second Local bidder
		bundle = Arrays.asList( items.get(1) );
		marginalValue = 0.2;
		AtomicBid atom21 = new AtomicBid(2, bundle, marginalValue);
		atom21.setTypeComponent( AtomicBid.IsBidder, 1.0);
				
		CombinatorialType t2 = new CombinatorialType();
		t2.addAtomicBid(atom21);
				
		//Global bidder
		bundle = Arrays.asList( items.get(0), items.get(1) );
		marginalValue = 0.15;
		AtomicBid atom31 = new AtomicBid(3, bundle, marginalValue);
		atom31.setTypeComponent( AtomicBid.IsBidder, 1.0);
				
		CombinatorialType t3 = new CombinatorialType();
		t3.addAtomicBid(atom31);
		
		List<Type> bids = Arrays.asList(t1, t2, t3);
				
		//4. Create a market
		CAXOR market = new CAXOR(bids.size(), items.size(), bids, costsRange);
		market.setPaymentRule( paymentRule );
		
		//5. Configure the BNE Solver
		BNESettings settings = new BNESettings();
		settings.setNumberOfBins(numberOfBins);
		settings.setNumberOfGridPoints(20);
		settings.setNumberOfSamples(numberOfSamples);
		settings.setNumberOfThreads(numberOfThreads);
		settings.setEpsilon(1e-4);
		
		if( optimizationEngine.equals("deterministic"))
			settings.setLocalOptimizerFactory(new PatternSearchFactory() );
		else if( optimizationEngine.equals("stochastic"))
			settings.setLocalOptimizerFactory(new StochasticPatternSearchFactory() );
		else throw new RuntimeException("wrong local optimization engine specified: " + optimizationEngine);
		
		ShavingStrategy[] strategies = new ShavingStrategy[1];
		if(isMultiplicativeShading)
			strategies[0] = new MultiplicativeShavingStrategy();
		else
			strategies[0] = new AdditiveShavingStrategy();
		
		IUtilityComputationEngineFactory utilityComputationEngineFactory;
		if( utilityEngine.equals("MPI") )
		{
			throw new RuntimeException("MPI temporarily not supported");
			//MPI.Init(args);
			//utilityComputationEngineFactory = new MPIUtilityComputationEngineFactory();
		}
		else if ( utilityEngine.equals("MT") )
		{
			utilityComputationEngineFactory = new MTUtilityComputationEngineFactory();
		}
		else throw new RuntimeException("Cannot create a utility computation factory.");
		
		IloCplex[] solvers = new IloCplex[numberOfThreads];
		IMechanismFactory[] CAXORFactory = new CAXORFactory[numberOfThreads];
		IDomainGenerator[] llgDomain = new DomainGeneratorLLG[numberOfThreads];
		for(int i = 0; i < numberOfThreads; ++i)
		{
			solvers[i] = new IloCplex();
			llgDomain[i] = new DomainGeneratorLLG();
			CAXORFactory[i] = new CAXORFactory(numberOfBuyers, numberOfItems, paymentRule, costsRange,  solvers[i]);
		}
		
		IBinSortingStrategy binSortingStrategy;
		
		//binSortingStrategy = new ReverseAuctionBinSortingStrategy();
		binSortingStrategy = new LLGBinSortingStrategy();
		
		//logger.info("Random number of tuples, #Buyers= "+ numberOfBuyers);
		//logger.info(paymentRule + " + " + "BNESolver(market, bids, "+settings.getNumberOfBins()+", "+ settings.getNumberOfGridPoints()+
		//		    ", "+numberOfSamples+", strategies, " + numberOfThreads +" ), eps=" + settings.getEpsilon()+ " (use " + utilityEngine + ")");
		//logger.info("Costs upper limits: " + costsRange.toString());
		//logger.info( isMultiplicativeShading ? "Muliplicative shading" : "Additive shading");
		
		BNESolver bne = new BNESolver(market, bids, strategies, utilityComputationEngineFactory, CAXORFactory, llgDomain, binSortingStrategy, settings, solvers);
		bne.setPrecision( settings.getEpsilon() );
		bne.sortToBins();
		
		if(loadState == 1)
		{
			//logger.info("Current state loaded from a file...");
			bne.loadState();
		}
		
		//6. Solve it
		bne.solveIt();
		System.out.print(bne.toString());
		//logger.info(bne.toString());
		for(int i = 0; i < numberOfThreads; ++i)
			solvers[i].end();
		
		//if( utilityEngine.equals("MPI") )
		//	MPI.Finalize();
	}
}
