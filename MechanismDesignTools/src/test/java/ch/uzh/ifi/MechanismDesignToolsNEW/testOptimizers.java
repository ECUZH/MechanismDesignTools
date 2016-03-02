package ch.uzh.ifi.MechanismDesignToolsNEW;

import static org.junit.Assert.*;

import org.junit.Test;

public class testOptimizers {

	class QuadraticFunction1D implements Optimizable
	{
		/*
		 * 
		 */
		public QuadraticFunction1D(double a, double b, double c)
		{
			_a = a;
			_b = b;
			_c = c;
		}

		/*
		 * (non-Javadoc)
		 * @see Tools.Optimizable#objective(double[])
		 */
		@Override
		public StochasticDouble objective(double... args) 
		{	
			StochasticDouble res = new StochasticDouble(_a * args[0] * args[0] + _b * args[0] + _c, 0, 0);
			return res;
		}
		
		private double _a;
		private double _b;
		private double _c;
	}
	
	class EllipticParaboloid implements Optimizable
	{
		/*
		 * 
		 */
		public EllipticParaboloid(double a, double b, double c, double xShift, double yShift)
		{
			_a = a;
			_b = b;
			_c = c;
			_xShift = xShift;
			_yShift = yShift;
		}

		/*
		 * (non-Javadoc)
		 * @see Tools.Optimizable#objective(double[])
		 */
		@Override
		public StochasticDouble objective(double... args) 
		{	
			StochasticDouble res = new StochasticDouble(_c * ( (args[0]-_xShift) * (args[0]-_xShift) / (_a*_a) + (args[1]-_yShift) * (args[1]-_yShift) / (_b*_b)  ), 0, 0);
			return res;
		}
		
		private double _a;
		private double _b;
		private double _c;
		private double _xShift;
		private double _yShift;
	}	
	
	@Test
	public void testGradientDescentConstructor() 
	{
		QuadraticFunction1D fun = new QuadraticFunction1D(1, 0, 0);
		
		GradientDescent gd = new GradientDescent(1, fun);
		double[] startPoint = {1.0};
		gd.setStartPoint(startPoint);
		gd.setTolerance(1e-15);
		OptimizationSolution sol = gd.optimize();
		
		assertTrue( Math.abs(sol.getArgumentValue()[0] - 0 ) < 1e-6 );
		assertTrue( Math.abs(sol.getFunctionValue() - 0 ) < 1e-6 );
	}
	
	@Test
	public void testGradientDescent1Dim() 
	{
		QuadraticFunction1D fun = new QuadraticFunction1D(1, -2, 4);
		
		GradientDescent gd = new GradientDescent(1, fun);
		double[] startPoint = {1.0};
		gd.setStartPoint(startPoint);
		gd.setTolerance(1e-15);
		OptimizationSolution sol = gd.optimize();
		
		assertTrue( Math.abs(sol.getArgumentValue()[0] - 1 ) < 1e-6 );
		assertTrue( Math.abs(sol.getFunctionValue() - 3 ) < 1e-6 );
	}

	@Test
	public void testGradientDescent2Dim() 
	{
		EllipticParaboloid fun = new EllipticParaboloid(1, 1, 1, 0, 0);
		
		GradientDescent gd = new GradientDescent(2, fun);
		double[] startPoint = {1.0, 1.0};
		gd.setStartPoint(startPoint);
		gd.setTolerance(1e-15);
		OptimizationSolution sol = gd.optimize();
		
		assertTrue( Math.abs(sol.getArgumentValue()[0] - 0 ) < 1e-6 );
		assertTrue( Math.abs(sol.getArgumentValue()[1] - 0 ) < 1e-6 );
		assertTrue( Math.abs(sol.getFunctionValue() - 0 ) < 1e-6 );
	}
	
	@Test
	public void testGradientDescent2Dim1() 
	{
		EllipticParaboloid fun = new EllipticParaboloid(1, 2, 3, 1, 2);
		
		GradientDescent gd = new GradientDescent(2, fun);
		double[] startPoint = {1.0, 1.0};
		gd.setStartPoint(startPoint);
		gd.setTolerance(1e-15);
		OptimizationSolution sol = gd.optimize();
		
		assertTrue( Math.abs(sol.getArgumentValue()[0] - 1 ) < 1e-6 );
		assertTrue( Math.abs(sol.getArgumentValue()[1] - 2 ) < 1e-6 );
		assertTrue( Math.abs(sol.getFunctionValue() - 0 ) < 1e-6 );
	}
}
