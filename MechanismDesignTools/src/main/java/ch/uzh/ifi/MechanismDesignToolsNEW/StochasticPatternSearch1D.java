package ch.uzh.ifi.MechanismDesignToolsNEW;

import java.util.Random;

import org.apache.commons.math3.stat.inference.TestUtils;

/*
 * The class implements a 1-dimensional pattern search method with a symmetrical linear pattern.
 * The parameter space is an interval (0,1).
 * There're no requirements on the existence of the gradient of the objective function.
 */
public class StochasticPatternSearch1D implements IOptimizer
{
	public enum Direction {
		NO_DIRECTION, LEFT, RIGHT
	}
	/*
	 * A constructor.
	 * @param obj - an Optimizable object which should implement the ::objective(...) interface.
	 * @param iParams - integer parameters which should be passed to the Optimizable object for its configuration.
	 * @param nMultistartIterations - the number of multistart iterations for global optimization.
	 */
	public StochasticPatternSearch1D( Optimizable obj, int[] iParams/*, int nMultistartIterations*/)
	{
		_obj = obj;
		_integerParams = iParams;
		_step = 0.1;
		_stepFactor = 2.;
		_lowerBound = 0.;
		_upperBound = 1.;
	}
	
	/*
	 * The method performs optimization and returns the solution.
	 * @return the solution for the optimization problem.
	 */
	public OptimizationSolution optimize()
	{
		_step = 0.1;
		int counter = 0;
		OptimizationSolution solution;

		_params = new double[ _integerParams.length + 1];
		for(int i = 0; i < _integerParams.length; ++i)
			_params[i] = (double)_integerParams[i];

		Direction curDirection = Direction.NO_DIRECTION;

		Random generator = new Random();
		generator.setSeed(System.nanoTime());
		_x = _lowerBound + (_upperBound - _lowerBound) * generator.nextDouble();

		_params[_params.length-1] = _x;
		StochasticDouble objValue0 = _obj.objective(_params);

		while( counter < MAX_LOCAL_ITERATIONS)
		{
			_params[_params.length-2] = 0;
			_params[_params.length-1] = _x;
			objValue0 = _obj.objective(_params);
			
			if( _x - _step < _lowerBound )
				_xL = _lowerBound + EPS;
			else
				_xL = _x - _step;
			
			if( _x + _step > _upperBound )
				_xR = _upperBound - EPS;
			else
				_xR = _x + _step;
			
			if( curDirection == Direction.LEFT) 								//If it goes from right to the left
			{
				_params[_params.length-2] = 0;
				_params[_params.length-1] = _xL;
				StochasticDouble objValueL = _obj.objective(_params);
				
				if( isSignificantlyLarger(objValueL, objValue0, _xL, _x) )		//If there's a significant evidence that fL > f0
				{
					System.out.println("Go left");
					if( (objValueL.getMean()-objValue0.getMean())/objValue0.getMean() < TOL )
						break;
						
					_x = _xL;
					objValue0 = objValueL;
				}
				else
				{
					System.out.println("Decrease step");
					curDirection = Direction.NO_DIRECTION;
					_step /= _stepFactor;
				}
				
				counter++;
				continue;	
			}
			else if( curDirection == Direction.RIGHT) 							//If it goes from left to right
			{
				_params[_params.length-2] = 0;
				_params[_params.length-1] = _xR;
				StochasticDouble objValueR = _obj.objective(_params);
				
				if( isSignificantlyLarger(objValueR, objValue0, _xR, _x) )		//If there's a significant evidence that fR > f0
				{
					System.out.println("Go right");
					if( (objValueR.getMean()-objValue0.getMean())/objValue0.getMean() < TOL )
						break;
					
					_x = _xR;
					objValue0 = objValueR;
				}
				else
				{
					System.out.println("Decrease step");
					curDirection = Direction.NO_DIRECTION;
					_step /= _stepFactor;
				}
				counter++;
				continue;
			} 
			else try 
			{
				_params[_params.length-2] = 0;
				_params[_params.length-1] = _xL;
				StochasticDouble objValueL = _obj.objective(_params);
				_params[_params.length-1] = _xR;
				StochasticDouble objValueR = _obj.objective(_params);
				
				System.out.println("f0=" + objValue0.toString() + ", fL="+objValueL.toString()+", fR="+objValueR.toString());
				
				if( isSignificantlyLarger(objValueL, objValue0, _xL, _x) )
				{
						System.out.println("Go left");
						if( (objValueL.getMean()-objValue0.getMean())/objValue0.getMean() < TOL )
							break;

						_x = _xL;
						objValue0 = objValueL;
						curDirection = Direction.LEFT;
				}
				if( isSignificantlyLarger(objValueR, objValue0, _xR, _x) )
				{
					System.out.println("Go right");
					if( (objValueR.getMean()-objValue0.getMean())/objValue0.getMean() < TOL )
						break;

					_x = _xR;
					objValue0 = objValueR;
					curDirection = Direction.RIGHT;
				}
				else if( isSignificantlyLarger(objValue0, objValueL, _x, _xL) && isSignificantlyLarger(objValue0, objValueR, _x, _xR) )
				{
					System.out.println("Decrease step");
					_step /= _stepFactor;
				}
				else
				{
					System.out.println("Decrease step as well");
					_step /= _stepFactor;
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			counter++;
		}
		double[] x = {_x};
		solution = new OptimizationSolution( x, objValue0.getMean());
		return solution;
	}
	
	/*
	 * (non-Javadoc)
	 * @see Tools.IOptimizer#setStartPoint(double[])
	 */
	@Override
	public void setStartPoint(double[] startPoint) 
	{
		if( startPoint.length > 1)
			throw new RuntimeException(">1-dimensional start points was passed to a 1-dimensional pattern search");
		
		_x = startPoint[0];
	}
	
	/*
	 * (non-Javadoc)
	 * @see Tools.IOptimizer#setSearchSpace(double[], double[])
	 */
	@Override
	public void setSearchSpace(double[] lowerBounds, double[] upperBounds)
	{
		_lowerBound = lowerBounds[0];
		_upperBound = upperBounds[0];
	}
	
	/*
	 * 
	 */
	private boolean isSignificantlyLarger(StochasticDouble val1, StochasticDouble val2, double x1, double x2)
	{
		double TCritical = 3.29;
		
		if(_isPairedTest)
		{
			boolean isMaxNumSamplesReached = false;
			int counter = 0;
			
			while( ! isMaxNumSamplesReached )
			{
				StochasticDouble diff = new StochasticDouble(0,0,0);
				diff.addSamples(val1.getSamples());
				for(int i = 0; i < val1.getNumberOfSamples(); ++i)
					diff.setSample(i, diff.getSamples().get(i) - val2.getSamples().get(i));
			
				diff.recomputeMean();
				diff.recomputeStandardDeviation();
			
				double T = Math.abs(  diff.getMean() * (double)Math.sqrt(diff.getNumberOfSamples()) /  diff.getStandardDeviation() );
			
				if( diff.getMean() > 0 && T > TCritical )
					return true;
				else if( diff.getMean() < 0 && T > TCritical )
					return false;
				else if( T <= TCritical )
				{
					//sample more
					counter += 1;
					_params[_params.length-2] = counter;
					_params[_params.length-1] = x1;
					val1 = val1.add(_obj.objective(_params));
					_params[_params.length-1] = x2;
					val2 = val2.add(_obj.objective(_params));
					isMaxNumSamplesReached = (val1.getNumberOfSamples() > MAX_NUMBER_OF_SAMPLES) ? true : false;
					System.out.println("Repeat: T=" + T + " Mean(diff)="+diff.getMean() + " S(diff)="+diff.getStandardDeviation() + 
									   " #Samples="+diff.getNumberOfSamples() + ". Comparing Mean(v1)="+val1.getMean() + 
									   " S(v1)="+val1.getStandardDeviation() + " and Mean(v2)="+val2.getMean() + 
									   " S(v2)="+val2.getStandardDeviation() );
				}
			}
		}
		else
		{
			
		}
		return false;
	}
	
	private double _x;
	private double _xL; 
	private double _xR;
	private double _step;
	private double _stepFactor;
	
	private Optimizable _obj;
	private int[] _integerParams;
	
	private double _lowerBound;
	private double _upperBound;
	
	private int MAX_NUMBER_OF_SAMPLES = 1000000;
	private int MAX_LOCAL_ITERATIONS = 20;
	private double TOL = 1e-3;
	private double EPS = 1e-10;
	
	private boolean _isPairedTest = true;
	private double[] _params;
}
