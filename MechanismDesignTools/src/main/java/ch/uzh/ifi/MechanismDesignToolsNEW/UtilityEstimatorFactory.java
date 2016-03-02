package ch.uzh.ifi.MechanismDesignToolsNEW;

import ilog.cplex.IloCplex;

import java.util.List;

import ch.uzh.ifi.MechanismDesignPrimitives.IDomainGenerator;
import ch.uzh.ifi.Mechanisms.IMechanismFactory;
import ch.uzh.ifi.MechanismDesignPrimitives.Type;

/**
 * A factory producing utility estimation engines.
 * @author Dmitry
 *
 */
public class UtilityEstimatorFactory 
{
	/*
	 * Constructor.
	 * @param localNumberOfSamples - the number of samples per thread
	 * @param v - the type of a fixed agent at a BNE-iteration
	 * @param s - a point in the search space of strategies for best responses of agent v assuming other agents play their fixed strategies
	 * @param mechanismFactory - a factory producing mechanisms for which BNE is computed
	 * @param agentsTypes - a list of type of all agents participating in the game
	 * @param shavingStrategies - objects implementing shaving strategies for agents (e.g., multiplicative or additive strategies)
	 * @param shaves - fixed shaving factors of different bins
	 * @param bins - a list of bins each includes a set of agents belonging to the bin 
	 */
	public UtilityEstimatorFactory(int localNumberOfSamples, Type v, double[] s, IMechanismFactory[] mechanismFactory, IDomainGenerator[] domainGenerators, 
								   List<Type> agentsTypes, ShavingStrategy[] shavingStrategies, List<List<Double>> shaves, List<List<Type>> bins,
								   IloCplex[] cplexSolvers)
	{
		_numberOfSamples = localNumberOfSamples;
		_v = v;
		_s = s;
		_mechanismFactory = mechanismFactory;
		_domainGenerators  = domainGenerators;
		_agentsTypes = agentsTypes;
		_shavingStrategies = shavingStrategies;
		_shaves = shaves;
		_bins = bins;
		_cplexSolvers = cplexSolvers;
	}
	
	/*
	 * The factory method produces a utility estimation engine.
	 * @return a utility estimation engine
	 */
	//public UtilityEstimator produceUtilityEstimator()
	//{
	//	return new UtilityEstimator( _numberOfSamples, _v, _s, _mechanismFactory, _domainGenerator, _agentsTypes, _shavingStrategies, _shaves, _bins);
	//}

	/*
	 * The factory method produces a utility estimation engine.
	 * @param seed - a seed used by a random numbers generator
	 * @return a utility estimation engine
	 */
	public UtilityEstimator produceUtilityEstimator(int threadIdx)
	{
		return produceUtilityEstimator(0, threadIdx);
		//return new UtilityEstimator( _numberOfSamples, _v, _s, _mechanismFactory, _domainGenerator, _agentsTypes, _shavingStrategies, _shaves, _bins;
	}
	
	/*
	 * The factory method produces a utility estimation engine.
	 * @param seed - a seed used by a random numbers generator
	 * @param threadIdx - an index of a thread in which the mechanism is handled
	 * @return a utility estimation engine
	 */
	public UtilityEstimator produceUtilityEstimator(int seed, int threadIdx)
	{
		return new UtilityEstimator( _numberOfSamples, _v, _s, _mechanismFactory[threadIdx], _domainGenerators[threadIdx], _agentsTypes, _shavingStrategies, _shaves, _bins, seed);
	}
	
	private IMechanismFactory[] _mechanismFactory;		//A factory generating mechanisms for which BNEs are computed
	private IDomainGenerator[] _domainGenerators;
	private List<Type> _agentsTypes;					//True types of agents
	private ShavingStrategy[] _shavingStrategies;		//Shading strategy to be used for shading agent's values
	private List< List<Double> > _shaves;				//A list of current shaving factors per bin. Different shaves within a bin correspond to different dimensions of the strategy space
	private List<List<Type> > _bins;					//Bins allocating agents according to some classification rule
	private IloCplex[] _cplexSolvers;
	
	private int _numberOfSamples;						//A number of samples per thread 
	private Type _v;									//A type of a fixed agent
	private double[] _s;								//Strategies of the fixed agent (a point in the search space of best response strategies)
}
