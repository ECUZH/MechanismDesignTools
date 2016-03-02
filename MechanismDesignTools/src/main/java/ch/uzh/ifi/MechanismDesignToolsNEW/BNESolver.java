package ch.uzh.ifi.MechanismDesignToolsNEW;

//import java.time.LocalTime;
import ilog.cplex.IloCplex;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.uzh.ifi.Mechanisms.Auction;
import ch.uzh.ifi.Mechanisms.ProbabilisticCAXOR;
import ch.uzh.ifi.MechanismDesignPrimitives.IDomainGenerator;
import ch.uzh.ifi.Mechanisms.IMechanismFactory;
import ch.uzh.ifi.MechanismDesignPrimitives.Type;

/*
 * The class implements a BNE approximation algorithm.
 */
public class BNESolver implements Optimizable
{
	
	private static final Logger _logger = LogManager.getLogger(BNESolver.class);
	
	/*
	 * Constructor.
	 * @param auction - an instance of an auction 
	 * @param agents - a list of agents types. For every type, the "Value", "Distribution", "minValue", "maxValue", "isSeller" components MUST be specified
	 * @param numBins - number of bins used to group agents into different categories according to the similarity of their values.
	 * @param numGridPoints - number of grid points i.e. the resolution to use for searching for the optimal shading factor.
	 * @param numSamples - number of samples 
	 */
	public BNESolver(Auction auction, List<Type> agents, ShavingStrategy[] strategies, IUtilityComputationEngineFactory utilityFactory, 
					 IMechanismFactory[] mechanismFactory, IDomainGenerator[] domainGenerators, IBinSortingStrategy binSortingStrategy, BNESettings settings, IloCplex[] solvers)
	{
		_settings = settings;
		
		_agentsTypes = agents;
		_utilityComputationEngineFactory = utilityFactory;
		_mechanismFactory = mechanismFactory;
		_domainGenerator  = domainGenerators;
		_binSortingStrategy = binSortingStrategy;
		
		//_filename = "bne_parameters_"+ ((DoubleSidedMarket)mechanismFactory.produceMechanism(agents)).getPaymentRule() + 
		//		    "_" +((DoubleSidedMarket)mechanismFactory.produceMechanism(agents)).getPaymentCorrectionRule() + 
		//		    "_" +_agentsTypes.size() +"_"+ _settings.getNumberOfSamples() +".txt";
		_filename = "bne_parameters_"+ mechanismFactory[0].produceMechanism(agents).getPaymentRule() + 
			    "_" +_agentsTypes.size() +"_"+ _settings.getNumberOfSamples() +".txt";
		
		_oldVal  = new double[ _settings.getNumberOfBins() ];
		_newVal  = new double[ _settings.getNumberOfBins() ];
		
		_shavingStrategies = strategies;
		_rshaves = new LinkedList<List<Double> >();				//A list of multidimensional shaving factors
		_shaves  = new LinkedList<List<Double> >();
		for(int i = 0; i < _settings.getNumberOfBins(); ++i)
		{
			_rshaves.add( new LinkedList<Double>() );
			_shaves.add( new LinkedList<Double>() );
			
			for(int j = 0; j < _shavingStrategies.length; ++j)
			{
				_rshaves.get(i).add( _shavingStrategies[j].getTruthfulShadingFactor() );
				_shaves.get( i).add( _shavingStrategies[j].getInitShave() );
			}
		}
		
		_bins = new LinkedList<List<Type> >();
		for(int i = 0; i < _settings.getNumberOfBins(); ++i)
			_bins.add( new LinkedList<Type>() );	
		
		_cplexSolvers = solvers;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String str = "BNE Calculator. #bins=" + _settings.getNumberOfBins() + "; #gridPoints=" + 
					_settings.getNumberOfGridPoints() + "; #Agents=" + _agentsTypes.size() + "; #Samples" + _settings.getNumberOfSamples() + "\n";
		for(int i = 0; i < _settings.getNumberOfBins(); ++i)
		{
			str += "Bin " + i + " of size " + _bins.get(i).size() + ": ";
			for(int j = 0; j < _bins.get(i).size(); ++j)
				str += " <" + _bins.get(i).get(j).getAgentId()+ "," + _bins.get(i).get(j).toString() + ">, ";
			str += "\n";
		}
		return str;
	}
	
	/*
	 * The method sorts the agent's types into bins according to the 0.9-quantile of their values.
	 */
	public void sortToBins()
	{
		_bins = _binSortingStrategy.sortToBins(_agentsTypes, _bins);
	}
	
	/*
	 * The method returns a shaving factor of a specified bin.
	 * @param idxBin - an index of the bin
	 * @param idxDim - an index of the dimension of the strategy space
	 * @return a shaving factor of the bin
	 */
	public double getShavingFactor(int idxBin, int idxDim)
	{
		return _shaves.get(idxBin).get(idxDim);
	}
	
	/*
	 * The method specifies the precision of the approximation procedure.
	 * @param precision - an approximation precision (the default value is 1e-6)
	 */
	public void setPrecision(double precision)
	{
		_settings.setPrecision(precision);
	}
	
	/*
	 * The method solves the BNE approximation problem.
	 */
	public void solveIt()
	{
		boolean isFirstIteration = true;
		int counter = 0;
		int strategyOffset = 2;

		while( counter < _settings.getMaxIterations() )
		{
			//TODO: this optimization might not work for multidimensional strategy spaces
			//for(int i = 0; i < _numberOfAgents; ++i)																			//Setup critical shading, i.e., the shading factor ...
			//	_agentsTypes.get(i).setTypeComponent(0, "CriticalShading", 1.0 - _shavingStrategy.getTruthfulShadingFactor());	//... decreasing which will not lead to an agent i been allocated.

			if( ! isFirstIteration )														//Don't shave on the 1st iteration
				for(int i = 0; i < _shavingStrategies.length; ++i)
					_shavingStrategies[i].updateShaves( _oldVal, _newVal, _shaves, _rshaves, i);
			
			for(int i = 0; i < _settings.getNumberOfBins(); ++i)
			{
				int[] optParams = {i, 0};													//optimization configuration parameters: <binIdx, iteration of optimization process>
				MultistartDecorator optimizer = new SimpleMultistartDecorator(1, this, _settings.getLocalOptimizerFactory().produceOptimizer(this, optParams), _settings.getNumberOfGridPoints());
				
				double[] lowerBounds = new double[1];										//Search region lower bounds
				double[] upperBounds = new double[1];										//Search region upper bounds
				lowerBounds[0] = 0.;	
				upperBounds[0] = 1.;
				optimizer.setSearchSpace(lowerBounds, upperBounds);
				
				OptimizationSolution sol = optimizer.optimize();

				if( (sol.getFunctionValue() == 0.0) ) 										//No utility gain => no sense in shading
					for(int j = 0; j < _rshaves.get(i).size(); ++j)
						_rshaves.get(i).set(j, _shavingStrategies[j].getTruthfulShadingFactor());//TODO: why truthful but not current shade???
				else
					for(int j = 0; j < _rshaves.get(i).size(); ++j)
						_rshaves.get(i).set(j, sol.getArgumentValue()[j]);
				
				_newVal[i] = sol.getFunctionValue();
				double[] params = new double[ _shavingStrategies.length + strategyOffset ];	//Find the expected utility given the old shaving factor
				for(int j = strategyOffset; j < _shavingStrategies.length + strategyOffset; ++j)
					params[j] = _shaves.get(i).get(j - strategyOffset);
				params[0] = optParams[0];													//Bin index
				params[1] = optParams[1];													//Optimization iteration. (for stochastic pattern search)

				_oldVal[i] = objective( params ).getMean();
				
				assert( _newVal[i] >= _oldVal[i] );											//Best response MUST always be better than any other strategy!

				System.out.println("Iteration= "+ counter +" shaves["+i+"]=(" + _shaves.get(i).get(0) + "," + ") New/Old val: " + _newVal[i] + "/" + _oldVal[i] + 
								   " _rshaves[" + i + "]=(" + _rshaves.get(i).get(0)+"," + ") counter=" + counter + " delta=" + (Math.abs(_oldVal[i] - _newVal[i]) / _oldVal[i]) );
			}
			
			boolean isLastIteration = true;
			for(int i = 0; i < _settings.getNumberOfBins(); ++i)
				if( Math.abs(_oldVal[i] - _newVal[i]) / _oldVal[i] > _settings.getPrecision() )
					isLastIteration = false;
			
			if( isFirstIteration )
				isLastIteration = false;
			isFirstIteration = false;
			
			if( isLastIteration )
				break;
			counter += 1;

			saveState();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see Tools.Optimizable#objective(double[])
	 */
	public StochasticDouble objective(double...args)
	{
		int argumentOffset = 2;												//First n=argumentOffset parameters are configuration parameters
		int binIdx = (int)(args[0]);										//An index of a bin
		int optIter= (int)(args[1]);										//optimization Iteration
		
		double[] x = new double[args.length - argumentOffset];				//A shaving factor TODO: several shaves
		for(int i = 0; i < args.length - argumentOffset; ++i)
			x[i] = args[i+argumentOffset];
		
		StochasticDouble utility = new StochasticDouble(0., 0., 0);
		List<Type> binK = _bins.get(binIdx);
		
		//for(int k = 0; k < binK.size(); ++k)
		int k = 0;//(int)Math.floor(Math.random() * binK.size());
			//if( ! _shavingStrategy.isExceedCritical((Double) binK.get(k).getTypeComponent(0, "CriticalShading"), x))	// This is an optimization meaning that if the current shading factor ...
				try																										// ... is smaller than the critical shading factor, then this shade ...
				{																										// ... wouldn't lead to allocation anyway.
					UtilityEstimatorFactory utilityEstimatorFactory = new UtilityEstimatorFactory( _settings.getNumberOfSamples()/_settings.getNumberOfThreads(), 
																			  binK.get(k), x, _mechanismFactory, _domainGenerator, _agentsTypes, 
																			  _shavingStrategies, _shaves, _bins, _cplexSolvers);
					IUtilityComputationEngine utilityComputationEngine = _utilityComputationEngineFactory.produceUtilityComputationEngine(_settings.getNumberOfThreads(), utilityEstimatorFactory, true, optIter);
					//long t1 = System.currentTimeMillis();
					utility = utility.add( utilityComputationEngine.computeUtility(x, binK.get(k)) );
					//long t2 = System.currentTimeMillis();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
		return utility;
	}
		
	/*
	 * The method saves to a file all data required to reproduce the current iteration
	 */
	private void saveState()
	{
		PrintWriter writer;
		try 
		{
			writer = new PrintWriter(_filename, "UTF-8");
			String shavesStr = "";
			for(List<Double> sh : _shaves)
				shavesStr += sh.get(0) + " ";
			writer.println( shavesStr );

			String rshavesStr = "";
			for(List<Double> sh : _rshaves)
				rshavesStr += sh.get(0) + " ";
			writer.println( rshavesStr );
			
			String newValStr = "";
			for(Double  val: _newVal)
				newValStr += val + " ";
			writer.println( newValStr );
			
			String oldValStr = "";
			for(Double  val: _oldVal)
				oldValStr += val + " ";
			writer.println( oldValStr );
			
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * The method loads all data required to start a new iteration from a file
	 */
	public void loadState()
	{
		BufferedReader br;
		try 
		{
			br = new BufferedReader(new FileReader(_filename));
			try 
			{
		        String line = br.readLine();
		        String[] shavesStr = line.split(" ");
		        for(int i = 0; i < _shaves.size(); ++i)
		        	_shaves.get(i).set(0, Double.valueOf(shavesStr[i]));
		        
		        line = br.readLine();
		        String[] rshavesStr = line.split(" ");
		        for(int i = 0; i < _rshaves.size(); ++i)
		        	_rshaves.get(i).set(0, Double.valueOf(rshavesStr[i]));
		        
		        line = br.readLine();
		        String[] newValStr = line.split(" ");
		        for(int i = 0; i < _newVal.length; ++i)
		        	_newVal[i] = Double.valueOf(newValStr[i]);
		        
		        line = br.readLine();
		        String[] oldValStr = line.split(" ");
		        for(int i = 0; i < _oldVal.length; ++i)
		        	_oldVal[i] = Double.valueOf(oldValStr[i]);
		        
		        line = br.readLine();
		    }
			finally 
		    {
				br.close();
		    }
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
	private IUtilityComputationEngineFactory _utilityComputationEngineFactory;//A factory producing different (MPI, Multi-Threaded etc.) utility computation factories
	private IMechanismFactory[] _mechanismFactory;		//A m-m factory	
	private IDomainGenerator[]  _domainGenerator;
	private IBinSortingStrategy _binSortingStrategy;	//A strategy to be used to group agents' types into different bins
	private List<Type> _agentsTypes;					//True types of agents
	private List<List<Type> > _bins;					//Bins allocating agents according to some classification rule
	
	private List< List<Double> > _shaves;				//A list of current shaving factors per bin. Different shaves within a bin correspond to different dimensions of the strategy space
	private List< List<Double> > _rshaves;				//A list of provisional shaving factors per bin. Different shaves within a bin correspond to different dimensions of the strategy space
	private double[] _oldVal;							//Utilities values of all groups of agents if they shade with _shaves[]
	private double[] _newVal;							//Utilities values of all groups of agents if they shade with _rshaves[]
	
	private ShavingStrategy[] _shavingStrategies;		//Shading strategy to be used for shading agent's values
	private BNESettings _settings;						//Some settings for the BNE approximation engine
	private String _filename;							//The name of a file in which intermediate results are stored
	
	private IloCplex[] _cplexSolvers;
}
