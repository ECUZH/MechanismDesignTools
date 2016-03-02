package ch.uzh.ifi.MechanismDesignToolsNEW;

import java.util.Random;

import org.apache.logging.log4j.*;

/*
 * The class implements a 1-dimensional pattern search method with a symmetrical linear pattern.
 * The parameter space is an interval (0,1).
 * There're no requirements on the existence of the gradient of the objective function.
 */
public class PatternSearch1D implements IOptimizer
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
	public PatternSearch1D( Optimizable obj, int[] iParams/*, int nMultistartIterations*/)
	{
		//Logger.getRootLogger().getLoggerRepository().resetConfiguration();		
		//_logger = LogManager.getLogger(this.getClass());
		
		//ConsoleAppender console = new ConsoleAppender();
		//String PATTERN = "%d [%p|%c|%C{1}] %m%n";
		//console.setLayout(new PatternLayout(PATTERN)); 
		//console.setThreshold(Level.DEBUG);
		//console.activateOptions();
		
		//_logger.getRootLogger().addAppender(console);
		//_logLevel = Level.OFF;
		//_logger.setLevel(_logLevel);						//set Level.DEBUG for debugging and Level.ERROR for release
		_binToTrace = 0;
		
		_obj = obj;
		_integerParams = iParams;
		_step = 0.2;
		_stepFactor = 1.5;
		_lowerBound = 0.;
		_upperBound = 1.;
		MAX_LOCAL_ITERATIONS = (int)(2*(_upperBound - _lowerBound) / _step);
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

		double[] params = new double[ _integerParams.length + 1];
		for(int i = 0; i < _integerParams.length; ++i)
			params[i] = (double)_integerParams[i];

		Direction curDirection = Direction.NO_DIRECTION;

		Random generator = new Random();
		generator.setSeed(System.nanoTime());
		
		_x = _lowerBound + (_upperBound - _lowerBound) * generator.nextDouble();

		params[params.length-1] = _x;
		
		f0 = _obj.objective(params).getMean();
		//if(_integerParams[0] == _binToTrace)	{_logger.setLevel(_logLevel); _logger.info("\n x0="+_x + " f0="+f0);}
		
		while( counter < MAX_LOCAL_ITERATIONS)
		{
			double xL, xR;
			if( _x - _step < _lowerBound )
				xL = _lowerBound + EPS;
			else
				xL = _x - _step;
			
			if( _x + _step > _upperBound )
				xR = _upperBound - EPS;
			else
				xR = _x + _step;
			
			double fL = 0.; 
			double fR = 0.;
			
			if( curDirection == Direction.LEFT) 				//If it goes from right to the left
			{
				if(_x == xL)									//lower bound reached => finish local optimization
					break;
				
				params[params.length-1] = xL;
				fL = _obj.objective(params).getMean();
				//System.out.println("\n x0="+ xL + " f0="+fL);
				
				if( fL > f0 )
				{
					if( (fL-f0)/f0 < TOL )
						break;
							
					_x = xL;
					f0 = fL;
					
					//if(_integerParams[0] == _binToTrace)	{_logger.setLevel(_logLevel); _logger.debug("xL="+ xL + " fL="+fL + " keep going left ");}
				}
				else
				{
					curDirection = Direction.NO_DIRECTION;
					_step /= _stepFactor;
					
					//if(_integerParams[0] == _binToTrace) 	{_logger.setLevel(_logLevel); _logger.debug("LEFT: decrease step ");}
				}
				counter++;
				continue;
			}
			else if( curDirection == Direction.RIGHT) 			//If it goes from left to right
			{
				if(_x == xR)									//upper bound reached => finish local optimization
					break;
				
				params[params.length-1] = xR;
				fR = _obj.objective(params).getMean();
				//System.out.println("\n x0="+ xR + " f0="+fR);
								
				if(fR > f0)
				{
					if( (fR-f0)/f0 < TOL )
						break;
					
					_x = xR;
					f0 = fR;
					
					//if(_integerParams[0] == _binToTrace)	{_logger.setLevel(_logLevel); _logger.debug("xR="+ xR + " fR="+fR + " keep going right ");}
				}
				else
				{
					curDirection = Direction.NO_DIRECTION;
					_step /= _stepFactor;
					
					//if(_integerParams[0] == _binToTrace) 	{_logger.setLevel(_logLevel); _logger.debug("RIGHT: decrease step ");}
				}
				counter++;
				continue;
			}
			else try 
			{	
				params[params.length-1] = xL;
				fL = _obj.objective(params).getMean();
				//System.out.println("\n x0="+xL + " f0="+fL);
				
				params[params.length-1] = xR;
				fR = _obj.objective(params).getMean();
				//System.out.println("\n x0="+ xR + " f0="+fR);

				
				//if(_integerParams[0] == _binToTrace)		{_logger.setLevel(_logLevel); _logger.debug("xL="+ xL + " fL="+fL + " xR="+xR+" fR="+fR);}
				
				if( fL > f0 )
				{
					if( (fL-f0)/f0 < TOL )
						break;

					_x = xL;
					f0 = fL;
					curDirection = Direction.LEFT;
					
					//if(_integerParams[0] == _binToTrace)	{_logger.setLevel(_logLevel); _logger.debug("Go left");}
				}
				else if(fR > f0)
				{
					if( (fR-f0)/f0 < TOL )
						break;
								
					_x = xR;
					f0 = fR;
					curDirection = Direction.RIGHT;
					
					//if(_integerParams[0] == _binToTrace)	{_logger.setLevel(_logLevel); _logger.debug("Go right");}
				}
				else
				{
					_step /= _stepFactor;
					
					//if(_integerParams[0] == _binToTrace)	{_logger.setLevel(_logLevel); _logger.debug("Decrease step");}
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			counter++;
		}
		
		//if( counter == MAX_LOCAL_ITERATIONS) 				{System.out.println("MAX_LOCAL_ITERATIONS exceeded.");}
		double[] x = {_x};
		solution = new OptimizationSolution( x, f0);
		//System.out.println("OPT: f(" + x[0] +")="+f0 );
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
	
	private double _x;
	private double _step;
	private double _stepFactor;
	
	private Optimizable _obj;
	private int[] _integerParams;
	
	private double _lowerBound;
	private double _upperBound;
	
	private int MAX_LOCAL_ITERATIONS = 15;
	private double TOL = 1e-9;//3;
	private double EPS = 1e-10;
	
	//private Logger _logger;
	//private Level _logLevel;
	private int _binToTrace = 0;
}