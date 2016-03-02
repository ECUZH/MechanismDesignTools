package ch.uzh.ifi.MechanismDesignToolsNEW;

import java.time.LocalTime;


/*
 * The class implements a 2-dimensional pattern search method with a symmetrical linear pattern.
 * The parameter space is an interval (0,1)x(0,1).
 * There're no requirements on the existence of the gradient of the objective function.
 */
public class PatternSearch2D implements IOptimizer
{
	public enum Direction {
		NO_DIRECTION, LEFT, RIGHT, TOP, BOTTOM
	}
	
	/*
	 * A constructor.
	 * @param obj - an Optimizable object which should implement the ::objective(...) interface.
	 * @param iParams - integer parameters which should be passed to the Optimizable object for its configuration.
	 * @param nMultistartIterations - the number of multistart iterations for global optimization.
	 */
	public PatternSearch2D( Optimizable obj, int[] iParams/*, int nMultistartIterations*/)
	{
		_obj = obj;
		_integerParams = iParams;
		_step = 0.1;
		_stepFactor = 2.;
		_x = new double[2];
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

		double f0;

		double[] params = new double[ _integerParams.length + 2];
		for(int i = 0; i < _integerParams.length; ++i)
			params[i] = (double)_integerParams[i];

		Direction curDirection = Direction.NO_DIRECTION;

		for(int i = 0; i < _x.length; ++i)
		{
			_x[i] = Math.random();								//Starting point
			params[params.length-2 + i] = _x[i];
		}
		
		//LocalTime time = LocalTime.now();
		//System.out.println( "-> TIME:  " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond() );
		f0 = _obj.objective(params).getMean();
		//time = LocalTime.now();
		//System.out.println( "<- TIME:  " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond() );
		
		//System.out.println( "Init x0 = (" + _x[0] + "," + _x[1] + ").  f(x0)="+ f0 );
		
		while( counter < MAX_LOCAL_ITERATIONS)
		{
			double xL, xR, xT, xB;
			if( _x[0] - _step < 0 )
				xL = 0 + EPS;
			else
				xL = _x[0] - _step;
			
			if( _x[0] + _step > 1 )
				xR = 1 - EPS;
			else
				xR = _x[0] + _step;
			
			if( _x[1] - _step < 0 )
				xB = 0 + EPS;
			else
				xB = _x[1] - _step;
			
			if( _x[1] + _step > 1 )
				xT = 1 - EPS;
			else
				xT = _x[1] + _step;
			
			double fL = 0.; 
			double fR = 0.;
			double fB = 0.; 
			double fT = 0.;
			
			/*if( curDirection == Direction.LEFT) 				//If it goes from right to the left
			{
				params[params.length-2] = xL;
				fL = _obj.objective(params);
				
				if( fL > f0 )
				{
					if( (fL-f0)/f0 < TOL )
						break;
							
					_x[0] = xL;
					f0 = fL;
				}
				else
				{
					curDirection = Direction.NO_DIRECTION;
					_step /= _stepFactor;
				}
				counter++;
				continue;
			}
			else if( curDirection == Direction.RIGHT) 			//If it goes from left to right
			{
				params[params.length-2] = xR;
				fR = _obj.objective(params);
								
				if(fR > f0)
				{
					if( (fR-f0)/f0 < TOL )
						break;
					
					_x[0] = xR;
					f0 = fR;
				}
				else
				{
					curDirection = Direction.NO_DIRECTION;
					_step /= _stepFactor;
				}
				counter++;
				continue;
			}
			else if( curDirection == Direction.BOTTOM) 			//If it goes from top to bottom
			{
				params[params.length-1] = xB;
				fB = _obj.objective(params);
								
				if(fB > f0)
				{
					if( (fB-f0)/f0 < TOL )
						break;
					
					_x[1] = xB;
					f0 = fB;
				}
				else
				{
					curDirection = Direction.NO_DIRECTION;
					_step /= _stepFactor;
				}
				counter++;
				continue;
			}
			else if( curDirection == Direction.TOP) 			//If it goes from bottom to top
			{
				params[params.length-1] = xT;
				fT = _obj.objective(params);
								
				if(fT > f0)
				{
					if( (fT-f0)/f0 < TOL )
						break;
					
					_x[1] = xT;
					f0 = fT;
				}
				else
				{
					curDirection = Direction.NO_DIRECTION;
					_step /= _stepFactor;
				}
				counter++;
				continue;
			}
			else*/ try 
			{
				params[params.length-2] = xL;
				fL = _obj.objective(params).getMean();		//System.out.println("_x[0]=" + _x[0] + " _x[1]=" + _x[1] + " go left xL="+xL+ " fL=" + fL);
				params[params.length-2] = xR;
				fR = _obj.objective(params).getMean();		//System.out.println("_x[0]=" + _x[0] + " _x[1]=" + _x[1] + " go left xR="+xR+ " fR=" + fR);
				
				params[params.length-2] = _x[0];
				
				params[params.length-1] = xB;
				fB = _obj.objective(params).getMean();		//System.out.println("_x[0]=" + _x[0] + " _x[1]=" + _x[1] + " go bottom xB="+xB+ " fB=" + fB);
				params[params.length-1] = xT;
				fT = _obj.objective(params).getMean();		//System.out.println("_x[0]=" + _x[0] + " _x[1]=" + _x[1] + " go top xT="+xT+ " fT=" + fT);
											
				params[params.length-1] = _x[1];
				
				//System.out.println("fL=" + fL + " fR=" + fR + " fT=" + fT + " fB=" + fB);
				
				if( (fL > f0) && (fL > fR) && (fL > fT) && (fL > fB) )
				{			
					//System.out.println("GO LEFT");
					_x[0] = xL;
					if( (fL-f0)/f0 < TOL )
					{
						f0 = fL;
						break;
					}
					f0 = fL;
				}
				else if( (fR > f0) && (fR > fL) && (fR > fT) && (fR > fB) )
				{
					//System.out.println("GO RIGHT");
					_x[0] = xR;
					if( (fR-f0)/f0 < TOL )
					{
						f0 = fR;
						break;
					}
					f0 = fR;
				}
				else if( (fB > f0) && (fB > fL) && (fB > fT) && (fB > fR) )
				{
					//System.out.println("GO BOTTOM");
					_x[1] = xB;
					if( (fB-f0)/f0 < TOL )
					{
						f0 = fB;
						break;
					}
					f0 = fB;
				}
				else if( (fT > f0) && (fT > fL) && (fT > fR) && (fT > fB) )
				{
					//System.out.println("GO TOP");
					_x[1] = xT;
					if( (fT-f0)/f0 < TOL )
					{
						f0 = fT;
						break;
					}
					f0 = fT;
				}
				else
				{
					_step /= _stepFactor;
					//System.out.println("DECREASE STEP");
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			counter++;
		}
		//System.out.println("Number of steps in Pattern Search required : " + counter);
		
		solution = new OptimizationSolution(_x, f0);//TODO:  _x[0] --> _x
		//System.out.println( "Result xn = (" + _x[0] + "," + _x[1] + "). f(xn)="+f0 );
		return solution;
	}
	
	@Override
	public void setStartPoint(double[] startPoint) 
	{/*
		if( startPoint.length > 1)
			throw new RuntimeException(">1-dimensional start points was passed to a 1-dimensional pattern search");
		
		_x = startPoint[0];*/
	}
	
	@Override
	public void setSearchSpace(double[] lowerBounds, double[] upperBounds) 
	{
		// TODO Auto-generated method stub	
	}
	
	private double[] _x;
	private double _step;								//Initial step (pattern radius)
	private double _stepFactor;							//Decrease the step by this factor when converging
	
	private Optimizable _obj;							//An object implementing an objective function
	private int[] _integerParams;						//Configuration parameters
	
	private int MAX_LOCAL_ITERATIONS = 20;
	private double TOL = 1e-4;//1e-3
	private double EPS = 1e-6;
}
