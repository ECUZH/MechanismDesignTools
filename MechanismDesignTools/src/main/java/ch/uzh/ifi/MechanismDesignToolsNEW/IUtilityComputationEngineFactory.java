package ch.uzh.ifi.MechanismDesignToolsNEW;

/*
 * A factory producing different impolementations of utility computation engines (Multi-threaded, MPI based etc...)
 */
public interface IUtilityComputationEngineFactory 
{
	/*
	 * The interface used for producing a utility computation factory served by a given number of tasks and using
	 * a particular utility estimation strategy.
	 * @param numberOfThreads - the maximum number of tasks which should be used by the produced engine.
	 * @param utilityEstimator - the utility estimation strategy. 
	 */
	public IUtilityComputationEngine produceUtilityComputationEngine(int numberOfThreads, UtilityEstimatorFactory utilityEstimatorFactory, boolean useCorrelatedSamples, int seed);
}
