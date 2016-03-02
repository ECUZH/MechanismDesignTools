package ch.uzh.ifi.MechanismDesignToolsNEW;

import java.util.LinkedList;
import java.util.List;

import ch.uzh.ifi.MechanismDesignPrimitives.Type;

/*
 * The class implements a 2-bins sorting strategy for double auctions. Buyers go to the 
 * 1st bin, sellers go to the second one.
 */
public class DoubleAuctionBinSortingStrategy implements IBinSortingStrategy
{

	/*
	 * A simple constructor
	 */
	public DoubleAuctionBinSortingStrategy()
	{

	}
	
	/*
	 * (non-Javadoc)
	 * @see Tools.IBinSortingStrategy#sortToBins(java.util.List)
	 */
	@Override
	public List< List<Type> > sortToBins(List<Type> agentsTypes, List< List<Type> > bins) 
	{
		for(Type t : agentsTypes)
			if( ! ((Double)t.getTypeComponent(0, "isSeller") == 1.0))
				bins.get(0).add(t);
			else
				bins.get(1).add(t);
		
		for(int i = 0; i < bins.size(); ++i)
		{
			System.out.println("BIN["+i+"]");
			for(Type t : bins.get(i))
				System.out.println("Bin["+i+"]: " + t.toString());
		}
		
		return bins;
	}
}
