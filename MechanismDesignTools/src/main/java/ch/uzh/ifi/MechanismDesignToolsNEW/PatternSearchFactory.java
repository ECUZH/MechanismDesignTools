package ch.uzh.ifi.MechanismDesignToolsNEW;

public class PatternSearchFactory implements IOptimizerFactory
{

	public PatternSearchFactory()
	{
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see Tools.IOptimizerFactory#produceOptimizer(Tools.Optimizable, int[])
	 */
	@Override
	public IOptimizer produceOptimizer(Optimizable obj, int[] params) 
	{
		return new PatternSearch1D(obj, params);
	}

}
