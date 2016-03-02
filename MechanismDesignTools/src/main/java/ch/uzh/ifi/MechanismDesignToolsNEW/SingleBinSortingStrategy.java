package ch.uzh.ifi.MechanismDesignToolsNEW;

import java.util.List;

import ch.uzh.ifi.MechanismDesignPrimitives.Type;

public class SingleBinSortingStrategy implements IBinSortingStrategy
{

	/*
	 * 
	 */
	public SingleBinSortingStrategy()
	{
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see Tools.IBinSortingStrategy#sortToBins(java.util.List, java.util.List)
	 */
	@Override
	public List<List<Type>> sortToBins(List<Type> agentsTypes, List<List<Type>> bins) 
	{
		if(bins.size() != 1)		throw new RuntimeException("Wrong number of bins");
		
		for(Type t : agentsTypes)
			bins.get(0).add(t);
			
		for(int i = 0; i < bins.size(); ++i)
		{
			System.out.println("BIN["+i+"]");
			for(Type t : bins.get(i))
				System.out.println("Bin["+i+"]: " + t.toString());
		}
		
		return bins;
	}

}
