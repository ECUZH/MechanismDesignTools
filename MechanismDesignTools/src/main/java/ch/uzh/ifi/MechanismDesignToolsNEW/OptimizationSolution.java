package ch.uzh.ifi.MechanismDesignToolsNEW;

public class OptimizationSolution 
{
	
	public OptimizationSolution(double[] x, double y)
	{
		_x = new double[x.length];
		for(int i = 0; i < x.length; ++i)
			_x[i] = x[i];
		_y = y;
	}
	
	@Override
	public String toString()
	{
		String str = "(";
		for(int i = 0; i < _x.length; ++i)
			str += _x[i] + ", ";
		str += ";  " + _y + ")";
		return str;
	}
	
	public double[] getArgumentValue()
	{
		return _x;
	}
	
	public double getFunctionValue()
	{
		return _y;
	}
	
	private double[] _x;
	private double _y;
}
