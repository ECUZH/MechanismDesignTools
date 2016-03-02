package ch.uzh.ifi.MechanismDesignToolsNEW;

import java.util.LinkedList;
import java.util.List;

public class StochasticDouble 
{

	/*
	 * Constructor
	 * @param meanValue - the mean (expected) value
	 * @param stdValue - the standard deviation value
	 * @param numberOfSamples - the number of samples was used to ccalculate the mean/std
	 */
	public StochasticDouble(double meanValue, double stdValue, int numberOfSamples)
	{
		_meanValue = meanValue;
		_stdValue  = stdValue;
		_numberOfSamples = numberOfSamples;
		_samples = new LinkedList<Double>();
	}
	
	public StochasticDouble add(StochasticDouble rhs)
	{
		double newMeanValue = _meanValue * _numberOfSamples / (_numberOfSamples + rhs.getNumberOfSamples()) + rhs.getMean() * rhs.getNumberOfSamples() / (_numberOfSamples + rhs.getNumberOfSamples());
		double newStdDev    = Math.sqrt(Math.pow(_stdValue, 2) * _numberOfSamples / (_numberOfSamples + rhs.getNumberOfSamples()) + Math.pow( rhs.getStandardDeviation(), 2) * rhs.getNumberOfSamples() / (_numberOfSamples + rhs.getNumberOfSamples()) );
		int newNSamples		= _numberOfSamples + rhs.getNumberOfSamples();
		
		StochasticDouble res = new StochasticDouble( newMeanValue, newStdDev, newNSamples );
		res.addSamples(this.getSamples());
		res.addSamples( rhs.getSamples());
		return res;
	}
	
	public List<Double> getSamples()
	{
		return _samples;
	}
	
	public void addSample(double sample)
	{
		_samples.add(sample);
	}
	
	public void setSample(int idx, double sample)
	{
		_samples.set(idx, sample);
	}
	
	public void addSamples(double[] samples)
	{
		for(double s : samples)
			_samples.add(s);
	}
	
	public void addSamples(List<Double> samples)
	{
		_samples.addAll(samples);
	}
	
	public double recomputeMean()
	{
		_meanValue = 0.;
		for(Double s : _samples)
			_meanValue += s;
		_meanValue /= _samples.size();
		_numberOfSamples = _samples.size();
		return _meanValue;
	}
	
	public double recomputeStandardDeviation()
	{
		_stdValue = 0.;
		for(Double s : _samples)
			_stdValue += Math.pow(s - _meanValue, 2);
		_stdValue /= _samples.size();
		_stdValue = Math.sqrt(_stdValue);
		_numberOfSamples = _samples.size();
		return _stdValue;
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "mu=" + _meanValue + "/"+_stdValue + "("+ _numberOfSamples + ")";
	}
	
	public double getMean()
	{
		return _meanValue;
	}
	
	public double getStandardDeviation()
	{
		return _stdValue;
	}
	
	public int getNumberOfSamples()
	{
		return _numberOfSamples;
	}
	
	private double _meanValue;
	private double _stdValue;
	private int _numberOfSamples;
	private List<Double> _samples;
}
