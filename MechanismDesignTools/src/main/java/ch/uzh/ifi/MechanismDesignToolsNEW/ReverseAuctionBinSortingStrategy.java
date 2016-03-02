package ch.uzh.ifi.MechanismDesignToolsNEW;

import java.util.List;

import ch.uzh.ifi.MechanismDesignPrimitives.AtomicBid;
import ch.uzh.ifi.MechanismDesignPrimitives.Type;

public class ReverseAuctionBinSortingStrategy implements IBinSortingStrategy
{

	/*
	 * A simple constructor
	 */
	public ReverseAuctionBinSortingStrategy()
	{

	}
	
	@Override
	public List<List<Type>> sortToBins(List<Type> agentsTypes, List<List<Type>> bins) 
	{
		for(Type t : agentsTypes)
			if(  ((Double)t.getTypeComponent(0, AtomicBid.IsBidder) == 1.0))
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
