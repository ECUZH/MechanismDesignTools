package ch.uzh.ifi.MechanismDesignToolsNEW;

import java.util.List;

import ch.uzh.ifi.MechanismDesignPrimitives.Type;

public class LLGBinSortingStrategy implements IBinSortingStrategy
{

	/*
	 * A simple constructor
	 */
	public LLGBinSortingStrategy()
	{

	}
	
	/*
	 * (non-Javadoc)
	 * @see Tools.IBinSortingStrategy#sortToBins(java.util.List, java.util.List)
	 */
	@Override
	public List<List<Type>> sortToBins(List<Type> agentsTypes, List<List<Type>> bins) 
	{
		if(agentsTypes.size() != 3) throw new RuntimeException("Wrong number of agents");
		if(bins.size() != 2)		throw new RuntimeException("Wrong number of bins");
		
		for(Type t : agentsTypes)
			if( t.getAgentId() == 1 || t.getAgentId() == 2)
				bins.get(0).add(t);
			else if (t.getAgentId() == 3)
				bins.get(1).add(t);
			else throw new RuntimeException("Incorrect agent id");
			
		for(int i = 0; i < bins.size(); ++i)
		{
			System.out.println("BIN["+i+"]");
			for(Type t : bins.get(i))
				System.out.println("Bin["+i+"]: " + t.toString());
		}
		
		return bins;
	}

}
