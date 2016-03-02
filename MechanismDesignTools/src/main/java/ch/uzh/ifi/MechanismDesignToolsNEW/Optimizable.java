package ch.uzh.ifi.MechanismDesignToolsNEW;

/*
 * The interface for classes which provide an objective function to be optimized.
 * Such classes can be passed to different mathematical optimization solvers (e.g. to PatternSearch1D.java)
 * for further optimization of the objective.
 */
public interface Optimizable 
{

	/*
	 * The objective function to be optimized.
	 * @param args - an array of parameters. Read the description of the appropriate solvers
	 * for further details.
	 * @return an optimal value.
	 */
	public StochasticDouble objective(double...args);
}
