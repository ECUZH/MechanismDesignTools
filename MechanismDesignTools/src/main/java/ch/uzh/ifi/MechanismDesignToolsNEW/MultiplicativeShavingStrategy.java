package ch.uzh.ifi.MechanismDesignToolsNEW;

import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;

import ch.uzh.ifi.Mechanisms.ShavingException;

/*
 * The class implements a multiplicative shaving strategy, i.e., a true value of an agent
 * is multiplied by a shaving factor:
 * - for a buyer sh*v
 * - for a seller (2-sh)*v
 */
public class MultiplicativeShavingStrategy implements ShavingStrategy 
{
	/*
	 * (non-Javadoc)
	 * @see Mechanisms.ShavingStrategy#initShaves(int, double[])
	 */
	@Override
	public double[] initShaves(int numberOfBins, double[] shaves) 
	{
		for(int i = 0; i < numberOfBins; ++i)
			shaves[i] = 1.0;
		return shaves;
	}

	/*
	 * (non-Javadoc)
	 * @see Mechanisms.ShavingStrategy#getInitShave()
	 */
	@Override
	public double getInitShave() 
	{
		return 1.0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see Mechanisms.ShavingStrategy#updateShaves(int, double[], double[], double[], double[])
	 */
	@Override
	public List<List<Double> > updateShaves(double[] oldValues, double[] newValues, List<List<Double> > shaves, List<List<Double> > rshaves, int component) 
	{
		double w = 0;
		for(int i = 0 ; i < shaves.size(); ++i)
		{
			double minConst = 1e-2;
			w = Math.abs(Math.atan( shaves.size() * (newValues[i] - oldValues[i]) / newValues[i] )) * 2*(1-minConst) / Math.PI + minConst;

			shaves.get(i).set(component, rshaves.get(i).get(component) * w + shaves.get(i).get(component) * (1-w));
		}
		return shaves;
	}

	/*
	 * (non-Javadoc)
	 * @see Mechanisms.ShavingStrategy#isShadingRequired(org.apache.commons.math3.linear.RealMatrix, int, double)
	 */
	@Override
	public boolean isShadingRequired(RealMatrix utilities, int bin, double maxUtilityGain) 
	{
		if( Math.abs( utilities.getEntry(bin, utilities.getColumnDimension()-1) - maxUtilityGain ) < 1e-6 )
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see Mechanisms.ShavingStrategy#isShadingValid(double, double)
	 */
	@Override
	public boolean isShadingValid(double value, double shadingFactor) 
	{
		if( (value * shadingFactor >= 0) && (value * shadingFactor <= value/*1.0*/) )
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see Mechanisms.ShavingStrategy#getShadedValue(double, double)
	 */
	@Override
	public double getShadedValue(double value, double shadingFactor, boolean isBuyer) throws ShavingException 
	{
		if( ! isShadingValid(value, shadingFactor) )
			throw new ShavingException("The value " + value + " cannot be shaded by " + shadingFactor, value, shadingFactor);//Exception("The value " + value + " cannot be shaded by " + shadingFactor);
		if( isBuyer )
			return value * shadingFactor;
		else
			return value * ( 1/shadingFactor );//(2-shadingFactor);
	}
	
	/*
	 * (non-Javadoc)
	 * @see Mechanisms.ShavingStrategy#getUnShadedValue(double, double, boolean)
	 */
	@Override
	public double getUnShadedValue(double reportedValue, double shadingFactor, boolean isBuyer) 
	{
		if( isBuyer )
			return reportedValue / shadingFactor;
		else
			return reportedValue * shadingFactor ;
	}

	/*
	 * (non-Javadoc)
	 * @see Mechanisms.ShavingStrategy#getTruthfulShadingFactor()
	 */
	@Override
	public double getTruthfulShadingFactor() 
	{
		return 1.0;
	}

	/*
	 * (non-Javadoc)
	 * @see Mechanisms.ShavingStrategy#isExceedCritical(double, double)
	 */
	@Override
	public boolean isExceedCritical(double criticalShadingFactor, double shadingFactor)
	{
		if( shadingFactor <= criticalShadingFactor )
			return true;
		return false;
	}
}
