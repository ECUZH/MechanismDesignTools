package ch.uzh.ifi.MechanismDesignToolsNEW;

import ch.uzh.ifi.MechanismDesignPrimitives.Type;

/*
 * An interface for different utility computation meathods (e.g., multi-threaded, MPI etc)
 */
public interface IUtilityComputationEngine 
{
	/*
	 * The interface triggers utility computation for a given agent and his strategy.
	 * @param s - the strategy of a fixed agent
	 * @param v - the type of the fixed agent
	 * @return StochasticDouble - mean, stddev and the number of samples of the utility
	 */
	StochasticDouble computeUtility( double[] s, Type v );
	
	double getUtilityStd();
}
