package ch.uzh.ifi.MechanismDesignToolsNEW;

/*
 * The class implements a multidimensional gradient descent unconstrained local optimization algorithm.
 */
public class GradientDescent implements IOptimizer
{

	/*
	 * Constructor
	 * @param dimensionality - dimensionality of the optimization problem
	 */
	public GradientDescent(int dimensionality, Optimizable obj)
	{
		_dimensionality = dimensionality;
		_lowerBounds = new double[_dimensionality];
		_upperBounds = new double[_dimensionality];
		_startPoint  = new double[_dimensionality];
		
		for(int i = 0; i < _dimensionality; ++i)
		{
			_lowerBounds[i] = Double.MIN_VALUE;
			_upperBounds[i] = Double.MAX_VALUE;
		}
		_obj = obj;
	}
	
	@Override
	public OptimizationSolution optimize() 
	{
		double finiteDiff = 1e-6;
		double objValue = _obj.objective( _startPoint ).getMean();
		double fun = objValue + 10;
		
		for(int i = 0; (i < MAX_ITER) || (Math.abs(fun-objValue) > TOL ); ++i)
		{
			fun = objValue;
			double[] gradient = new double[_dimensionality];
			
			//1. Compute gradient
			for(int j = 0; j < _dimensionality; ++j)
			{
				double oldCoord = _startPoint[j];
				_startPoint[j] = _startPoint[j] - finiteDiff;
				
				gradient[j] = (fun - _obj.objective(_startPoint).getMean()) / finiteDiff;
				_startPoint[j] = oldCoord;
			}
			
			//2. Move in the anti-gradient direction
			for(int j = 0; j < _dimensionality; ++j)
				_startPoint[j] -= gradient[j] * 0.1;
			
			objValue = _obj.objective( _startPoint ).getMean();
		}
		
		OptimizationSolution sol = new OptimizationSolution(_startPoint, _obj.objective( _startPoint ).getMean());
		return sol;
	}

	/*
	 * (non-Javadoc)
	 * @see Tools.IOptimizer#setStartPoint(double[])
	 */
	@Override
	public void setStartPoint(double[] startPoint) 
	{
		_startPoint = startPoint;
	}

	/*
	 * (non-Javadoc)
	 * @see Tools.IOptimizer#setSearchSpace(double[], double[])
	 */
	@Override
	public void setSearchSpace(double[] lowerBounds, double[] upperBounds) 
	{
		throw new RuntimeException("The gradient descent class implements an unconstrained optimization algorithm.");
	}

	public void setTolerance(double tolerance)
	{
		TOL = tolerance;
	}
	
	private int _dimensionality;
	private double[] _startPoint;
	private double[] _lowerBounds;
	private double[] _upperBounds;
	private Optimizable _obj;
	
	private int MAX_ITER = 20;
	private double TOL = 1e-12;
}
