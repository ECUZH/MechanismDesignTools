package ch.uzh.ifi.MechanismDesignToolsNEW;

/*
 * The abstract class specifies an interface for the multistart optimization engine.
 * Different implementations of the multistart engine can be combine with different
 * local search methods following the Decorator pattern.
 */
public abstract class MultistartDecorator implements IOptimizer
{
	
	/*
	 * Constructor
	 */
	public MultistartDecorator(IOptimizer component)
	{
		_component = component;
	}
	
	/*
	 * (non-Javadoc)
	 * @see Tools.IOptimizer#optimize()
	 */
	@Override
	public OptimizationSolution optimize()
	{
		return _component.optimize();
	}

	protected IOptimizer _component;					//TODO: can be an array of IOptimizers
	protected Optimizable _obj;							//An optimizable object which provides an objective to be optimized
	protected int _multistartIterations;				//The number of starting points for the multistart engine
}
