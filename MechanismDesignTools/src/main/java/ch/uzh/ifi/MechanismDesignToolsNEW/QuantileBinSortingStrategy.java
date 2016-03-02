package ch.uzh.ifi.MechanismDesignToolsNEW;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import ch.uzh.ifi.MechanismDesignPrimitives.Type;

public class QuantileBinSortingStrategy implements IBinSortingStrategy 
{

	public QuantileBinSortingStrategy()
	{
		
	}
	
	@Override
	public List<List<Type>> sortToBins(List<Type> agentsTypes, List<List<Type>> bins) 
	{
 
		double[] sortValues = new double[ agentsTypes.size()];
		
		for(int i = 0; i <  agentsTypes.size(); ++i)
		{
			int numberOfAtoms =  agentsTypes.get(i).getNumberOfAtoms();
			List<Double> itsValues = new LinkedList<Double>();
			for(int j = 0; j < numberOfAtoms; ++j)
				itsValues.add(  agentsTypes.get(i).getAtom(j).getValue() );
			
			itsValues.sort(null);
			sortValues[i] = itsValues.get( (int) Math.floor((double)numberOfAtoms * 0.9/*_settings.getSortQuantile()*/ ) );
			
			for(int j = 0; j < numberOfAtoms; ++j)
				 agentsTypes.get(i).getAtom(j).setTypeComponent("sortValue", sortValues[i]);
		}
		
		List<Type> sortedTypes = new LinkedList<Type>();
		for(int i = 0; i <  agentsTypes.size(); ++i)
			sortedTypes.add( agentsTypes.get(i));
		
		//sortedTypes.sort(new TypeComparator());
		sortedTypes.sort( (arg0, arg1) -> Double.compare((Double)arg0.getAtom(0).getTypeComponent("sortValue"), (Double)arg1.getAtom(0).getTypeComponent("sortValue")) );
		
		System.out.println("Sorted : " + sortedTypes.toString());
		
		for(int i = 0; i < sortedTypes.size(); ++i)
			bins.get( i * bins.size() /  agentsTypes.size() ).add( sortedTypes.get(i) );	
		
		for(int i = 0; i < bins.size(); ++i)
		{
			System.out.println("BIN["+i+"]");
			for(Type t : bins.get(i))
				System.out.println("Bin["+i+"]: " + t.toString());
		}
		
		return null;
	}

	
	/*
	 * The class implements a comparator for agents types w.r.t. the sortValue, i.e.,
	 * the 90% quantile of agent's values over different bundles.
	 */
	private static class TypeComparator implements Comparator<Type> 
	{
		/*
		 * (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Type arg0, Type arg1) 
		{
			if( (Double)arg0.getAtom(0).getTypeComponent("sortValue") > (Double)arg1.getAtom(0).getTypeComponent("sortValue") )
				return 1;
			else if( (Double)arg0.getAtom(0).getTypeComponent("sortValue") < (Double)arg1.getAtom(0).getTypeComponent("sortValue") )
				return -1;
			else
				return 0;
		}
	}
}
