package ch.uzh.ifi.MechanismDesignToolsNEW;

import java.util.List;

import ch.uzh.ifi.MechanismDesignPrimitives.Type;

/*
 * The interface defines different strategies for grouping similar types of agents into same bins.
 */
public interface IBinSortingStrategy 
{
	/*
	 * The interface triggers sorting of specified types of agents into several bins.
	 * @param agentsTypes - types of agents which should be sorted
	 * @param bins - bins to be filled with agents' types
	 * @return the list of bins each containing a list of types grouped according to some binning strategy.
	 */
	List< List<Type> > sortToBins( List<Type> agentsTypes, List< List<Type> > bins);
}
