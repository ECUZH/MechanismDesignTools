package ch.uzh.ifi.MechanismDesignToolsNEW;

/*
 * The factory produces Multi-Threaded utility computation engines.
 */
public class MTUtilityComputationEngineFactory implements IUtilityComputationEngineFactory
{

	public MTUtilityComputationEngineFactory()
	{
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see Tools.IUtilityComputationEngineFactory#produceUtilityComputationEngine(int, Tools.UtilityEstimator)
	 */
	@Override
	public IUtilityComputationEngine produceUtilityComputationEngine( int numberOfThreads, UtilityEstimatorFactory utilityEstimatorFactory, boolean useCorrelatedSamples, int seed) 
	{
		return new MTUtilityComputationEngine(numberOfThreads, utilityEstimatorFactory, useCorrelatedSamples, seed);
	}

}
