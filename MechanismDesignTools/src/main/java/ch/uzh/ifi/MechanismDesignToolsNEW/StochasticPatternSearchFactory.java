package ch.uzh.ifi.MechanismDesignToolsNEW;

public class StochasticPatternSearchFactory implements IOptimizerFactory
{

	@Override
	public IOptimizer produceOptimizer(Optimizable obj, int[] params) 
	{
		return new StochasticPatternSearch1D(obj, params);
	}

}
