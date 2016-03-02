package ch.uzh.ifi.MechanismDesignToolsNEW;

public interface IOptimizer 
{
	/*
	 * Optimization trigger.
	 * @return a solution for the optimization problem
	 */
	public OptimizationSolution optimize();
	
	/*
	 * Setup starting point for optimization.
	 * @param startPoint - a starting point for optimization.
	 */
	public void setStartPoint(double[] startPoint);

	/*
	 * Setup the search space, i.e., the box in which to search for the solution.
	 */
	void setSearchSpace(double[] lowerBounds, double[] upperBounds);

}
