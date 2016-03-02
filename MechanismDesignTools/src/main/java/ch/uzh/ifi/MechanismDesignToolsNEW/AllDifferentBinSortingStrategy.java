package ch.uzh.ifi.MechanismDesignToolsNEW;

import java.util.List;

import ch.uzh.ifi.MechanismDesignPrimitives.Type;

public class AllDifferentBinSortingStrategy implements IBinSortingStrategy
{
	/*
	 * 
	 */
	public AllDifferentBinSortingStrategy()
	{
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see Tools.IBinSortingStrategy#sortToBins(java.util.List, java.util.List)
	 */
	@Override
	public List<List<Type>> sortToBins(List<Type> agentsTypes, List<List<Type>> bins) 
	{
		for(int i = 0; i < agentsTypes.size(); ++i)
			bins.get(i).add(agentsTypes.get(i));
		return bins;
	}

}
