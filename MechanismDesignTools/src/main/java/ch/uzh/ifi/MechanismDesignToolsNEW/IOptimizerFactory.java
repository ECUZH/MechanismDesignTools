package ch.uzh.ifi.MechanismDesignToolsNEW;

/*
 * Factory interface for producing different optimization engines
 */
public interface IOptimizerFactory 
{

	/*
	 * @param obj - an objective function to be optimized
	 * @param params - an array of configuration parameters for the optimization algorithm
	 */
	IOptimizer produceOptimizer(Optimizable obj, int[] params);
}
