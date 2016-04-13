package ch.uzh.ifi.MechanismDesignToolsNEW;

import ilog.concert.IloException;
import ilog.cplex.IloCplex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.*;

//import mpi.MPI;
import ch.uzh.ifi.MechanismDesignPrimitives.FocusedBombingStrategy;
import ch.uzh.ifi.MechanismDesignPrimitives.IBombingStrategy;
import ch.uzh.ifi.MechanismDesignPrimitives.Type;
import ch.uzh.ifi.MechanismDesignPrimitives.JointProbabilityMass;
import ch.uzh.ifi.MechanismDesignPrimitives.AtomicBid;
import ch.uzh.ifi.MechanismDesignPrimitives.CombinatorialType;
import ch.uzh.ifi.MechanismDesignPrimitives.IDomainGenerator;
import ch.uzh.ifi.DomainGenerators.GridGenerator;
import ch.uzh.ifi.DomainGenerators.DomainGeneratorSpatial;
import ch.uzh.ifi.DomainGenerators.SpacialDomainGenerationException;
import ch.uzh.ifi.GraphAlgorithms.Graph;
import ch.uzh.ifi.Mechanisms.IMechanismFactory;
import ch.uzh.ifi.Mechanisms.ProbabilisticCAXOR;
import ch.uzh.ifi.Mechanisms.ProbabilisticCAXORFactory;


/**
 * The class provides a generic interface for running BNE approximation for the ProbabilisticCAXOR mechanism. 
 * @author Dmitry Moor
 */
public class GenericProbabilisticForwardCATSAuctionBNE
{

	private static final Logger _logger = LogManager.getLogger(GenericProbabilisticForwardCATSAuctionBNE.class);
	
	public static void main(String[] args) throws SpacialDomainGenerationException 
	{
		int numberOfArguments = 10;
		int offset = 0;
		if( args.length == numberOfArguments)
			offset = 0;
		else if( args.length == numberOfArguments + 3 )	//offset caused by MPI parameters
			offset = 3;
		else
			throw new RuntimeException("Wrong number of input parameters: " + args.length);
		
		//1. Parse command line arguments		
		int numberOfBins   = Integer.parseInt(args[0 + offset]);		if( numberOfBins    <= 0)	throw new RuntimeException("The number of bins should be posititve");
		String problemSize = args[1 + offset]; 							if( ! (problemSize.equals("big") || problemSize.equals("small")) )	throw new RuntimeException("Wrong problem size");
		String uncertaintyLevel = args[2 + offset];						if( ! (uncertaintyLevel.equals("low") || uncertaintyLevel.equals("high") || uncertaintyLevel.equals("no")) )  throw new RuntimeException("Wrong uncertianty level specified");
		int costsLimit = Integer.parseInt(args[3 + offset]);			if( costsLimit < 0 ) 		throw new RuntimeException("Negative costs");
		int numberOfSamples= Integer.parseInt(args[4 + offset]);		if( numberOfSamples <= 0) 	throw new RuntimeException("The number of samples should be positive: "+ numberOfSamples);
		int numberOfThreads    = Integer.parseInt(args[5 + offset]);	if( numberOfThreads  < 1 )	throw new RuntimeException("The number of threads should be at least 1");
		String paymentRule	   = args[6 + offset];						if( !(paymentRule.equals("EC-VCG_LLG") || paymentRule.equals("EC-VCG") || 
																			  paymentRule.equals("EC-CORE")    || paymentRule.equals("EC-CORE_LLG") ||
																			  paymentRule.equals("Exp-CORE")   || paymentRule.equals("ECC-CORE")   || paymentRule.equals("ECR-CORE") ) ) throw new RuntimeException("Wrong payment rule specified: " + paymentRule);
		String utilityEngine   = args[7 + offset];						if( !(utilityEngine.equals("MT") || utilityEngine.equals("MPI") ) ) throw new RuntimeException("Wrong utility computation engine specified: " + utilityEngine);
		String optimizationEngine=args[8+ offset];						if( !(optimizationEngine.equals("stochastic") || optimizationEngine.equals("deterministic") ) ) throw new RuntimeException("Wrong local optimization engine specified: " + optimizationEngine);
		int loadState 		   = Integer.parseInt(args[9 + offset]);	if( loadState != 0 && loadState != 1 )  throw new RuntimeException("Wrong load state");
				
		int numberOfBuyers = problemSize.equals("small") ? 5 : 8;
		int numberOfRows = problemSize.equals("small") ? 3 : 4;
		int numberOfColumns = problemSize.equals("small") ? 3 : 4;
		int numberOfItems = numberOfRows * numberOfColumns;
		int numberOfJpmfSamples = 1000;
		double costsUpperLimit = (double)costsLimit;
		boolean isMultiplicativeShading = true;
		
		List<Double> costsRange = new ArrayList<Double>();
		for(int i = 0 ; i < numberOfItems; ++i)
			costsRange.add(costsUpperLimit);
		
		List<Integer> items = new ArrayList<Integer>();
		for(int i = 0; i < numberOfItems; ++i)
			items.add(i+1);													//Goods in the auction
		
		//3. Generate types of agents
		List<Type> bids = new ArrayList<Type>();
		for(int i = 0; i < numberOfBuyers; ++i)
		{
			List<Integer> bundle = new ArrayList<Integer>();
			bundle.add( items.get(0) );
			double marginalValue = Math.random();
			AtomicBid atom = new AtomicBid(i+1, bundle, marginalValue);
			atom.setTypeComponent( AtomicBid.IsBidder, 1.0);
			CombinatorialType t = new CombinatorialType();
			t.addAtomicBid(atom);
			bids.add(t);
		}
		
		GridGenerator generator = new GridGenerator(numberOfRows, numberOfColumns);
		generator.setSeed(0);
		generator.buildProximityGraph();
		Graph grid = generator.getGrid();
		
		double primaryReductionCoef = 0;
		double secondaryReductionCoef = 0;
		if( uncertaintyLevel.equals("low") )
		{
			primaryReductionCoef = 0.3;
			secondaryReductionCoef = 0.2;
		}
		else if( uncertaintyLevel.equals("high") )
		{
			primaryReductionCoef = 0.6;
			secondaryReductionCoef = 0.1;
		}
		else if( uncertaintyLevel.equals("no") )
		{
			primaryReductionCoef = 0;
			secondaryReductionCoef = 0;
		}
		
		JointProbabilityMass jpmf = new JointProbabilityMass( grid );
		jpmf.setNumberOfSamples( numberOfJpmfSamples );
		jpmf.setNumberOfBombsToThrow( uncertaintyLevel.equals("low") ? 5 : 7 );
		
		IBombingStrategy b = new FocusedBombingStrategy(grid, 1., primaryReductionCoef, secondaryReductionCoef);
		List<IBombingStrategy> bombs = new ArrayList<IBombingStrategy>();
		bombs.add(b);
		
		List<Double> pd = new ArrayList<Double>();
		pd.add(1.);
		
		jpmf.setBombs(bombs, pd);
		jpmf.update();
		
		//4. Create a market
		ProbabilisticCAXOR market = new ProbabilisticCAXOR(bids.size(), items.size(), bids, costsRange, jpmf);
		market.setPaymentRule( paymentRule );
		
		//5. Configure the BNE Solver
		BNESettings settings = new BNESettings();
		settings.setNumberOfBins(numberOfBins);
		settings.setNumberOfGridPoints(1);
		settings.setNumberOfSamples(numberOfSamples);
		settings.setNumberOfThreads(numberOfThreads);
		settings.setEpsilon(1e-3);
		
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
		
		try
		{
			IMechanismFactory[] probabilisticCAXORFactories = new ProbabilisticCAXORFactory[numberOfThreads];
			IDomainGenerator[] catsDomainGenerators = new DomainGeneratorSpatial[numberOfThreads];
			IloCplex[] solvers = new IloCplex[numberOfThreads];
			JointProbabilityMass[] jpmfs = new JointProbabilityMass[numberOfThreads];
			
			for(int i = 0; i < numberOfThreads; ++i)
			{
				solvers[i] = new IloCplex();
				solvers[i].setParam(IloCplex.Param.RootAlgorithm.AuxRootThreads, -1);
				solvers[i].setParam(IloCplex.Param.Threads, 1);
				jpmfs[i] = jpmf.copyIt();
				probabilisticCAXORFactories[i] = new ProbabilisticCAXORFactory(numberOfBuyers, numberOfItems, paymentRule, costsRange, grid, numberOfJpmfSamples, jpmfs[i], solvers[i]);
				catsDomainGenerators[i] = new DomainGeneratorSpatial( numberOfItems, problemSize.equals("small") ? 0.78 : 0.85 );
			}
			
			IBinSortingStrategy binSortingStrategy;
			binSortingStrategy = new SingleBinSortingStrategy();
		
			_logger.info("Random number of tuples, #Buyers= "+ numberOfBuyers);
			_logger.info(paymentRule + " + " + "BNESolver(market, bids, "+settings.getNumberOfBins()+", "+ settings.getNumberOfGridPoints()+
					    ", "+numberOfSamples+", strategies, " + numberOfThreads +" ), eps=" + settings.getEpsilon()+ " (use " + utilityEngine + ")");
			_logger.info("Costs upper limits: " + costsRange.toString());
			_logger.info( isMultiplicativeShading ? "Muliplicative shading" : "Additive shading");
		
			BNESolver bne = new BNESolver(market, bids, strategies, utilityComputationEngineFactory, probabilisticCAXORFactories, catsDomainGenerators, binSortingStrategy, settings, solvers );
			bne.setPrecision( settings.getEpsilon() );
			bne.sortToBins();
		
			if(loadState == 1)
			{
				_logger.info("Current state loaded from a file...");
				bne.loadState();
			}
		
			//6. Solve it
			bne.solveIt();
			_logger.info(bne.toString());
			
			for(int i = 0; i < numberOfThreads; ++i)
				solvers[i].end();
		} 
		catch (IloException e) 
		{
			System.err.println("Cannot access the CPLEX solver. The job will be cancelled.");
			e.printStackTrace();
		}
		
		//if( utilityEngine.equals("MPI") )
		//	MPI.Finalize();
	}
}
