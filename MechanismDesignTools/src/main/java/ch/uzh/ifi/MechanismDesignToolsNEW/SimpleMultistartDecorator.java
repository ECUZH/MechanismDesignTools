package ch.uzh.ifi.MechanismDesignToolsNEW;

import java.util.LinkedList;
import java.util.List;

/*
 * The class implements a simple multistart global optimization strategy with uniformly distributed 
 * starting points.
 */
public class SimpleMultistartDecorator extends MultistartDecorator
{

	/*
	 * Constructor.
	 * @param obj - an optimizable object providing an interface to the objective function
	 * @param component - an object performing the search of a local minimizer
	 * @param nMultistartIterations - the number of iteration required by the engine
	 */
	public SimpleMultistartDecorator(int dimensionality, Optimizable obj, IOptimizer component, int nMultistartIterations) 
	{
		super(component);
		_dimensionality = dimensionality;
		_obj = obj;
		_multistartIterations = nMultistartIterations;
		
		_lowerBounds = new double[_dimensionality];
		_upperBounds = new double[_dimensionality];
		for(int i = 0; i < _dimensionality; ++i)
		{
			_lowerBounds[i] = Double.MIN_VALUE;
			_upperBounds[i] = Double.MAX_VALUE;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see Tools.MultistartDecorator#optimize()
	 */
	@Override
	public OptimizationSolution optimize()
	{
		OptimizationSolution[] solutions = new OptimizationSolution[_multistartIterations];

		List< List<Double> > x0 = new LinkedList< List<Double> >();
		
		for(int i = 0; i < _multistartIterations; ++i)
		{
			x0.add( new LinkedList<Double>() );
			for(int j = 0; j < _dimensionality; ++j)
				x0.get(i).add( _lowerBounds[j] + (_upperBounds[j] - _lowerBounds[j]) * Math.random() );
		}
		
		for(int j = 0; j < _multistartIterations; ++j)
		{
			double[] startPoint = new double[_dimensionality];
			for(int k = 0; k < _dimensionality; ++k)
				startPoint[k] = x0.get(j).get(k);
			
			this._component.setStartPoint(startPoint);
			try
			{
				solutions[j] = this._component.optimize();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		double maxVal = 0.;
		int optIdx = 0;
		
		for(int i = 0; i < _multistartIterations; ++i)
		{
			//System.out.println("Local Optimization Solution: f(" + solutions[i].getArgumentValue()[0]+")="+solutions[i].getFunctionValue());
				
			if( solutions[i].getFunctionValue() > maxVal )
			{
				maxVal = solutions[i].getFunctionValue();
				optIdx = i;
			}
		}
		//System.out.println("Res: f(" + solutions[optIdx].getArgumentValue()[0]+")="+solutions[optIdx].getFunctionValue() );
		return solutions[optIdx];
	}
	
	/*
	 * (non-Javadoc)
	 * @see Tools.IOptimizer#setStartPoint(double[])
	 */
	@Override
	public void setStartPoint(double[] startPoint) 
	{
		//do nothing as no starting points are required for a simple multistart
	}

	@Override
	public void setSearchSpace(double[] lowerBounds, double[] upperBounds) 
	{
		_lowerBounds = lowerBounds;
		_upperBounds = upperBounds;
		_component.setSearchSpace(_lowerBounds, _upperBounds);
	}
	
	private int _dimensionality;
	private double[] _lowerBounds;
	private double[] _upperBounds;
	private double EPS = 1e-10;//TODO: this EPS MUST be the same as in PatternSearch1D
}
