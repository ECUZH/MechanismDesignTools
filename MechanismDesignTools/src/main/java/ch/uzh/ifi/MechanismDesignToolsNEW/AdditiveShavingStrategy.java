package ch.uzh.ifi.MechanismDesignToolsNEW;

import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;

import ch.uzh.ifi.Mechanisms.ShavingException;

public class AdditiveShavingStrategy implements ShavingStrategy 
{
	/*
	 * (non-Javadoc)
	 * @see Mechanisms.ShavingStrategy#updateShaves(double[], double[], java.util.List, java.util.List, int)
	 */
	@Override
	public List<List<Double> > updateShaves( double[] oldValues, double[] newValues, List< List<Double> > shaves, List< List<Double> > rshaves, int component) 
	{
		/*for(int i = 0; i < shaves.size(); ++i)	//for every Bin
		{
			double minConst = 1e-2;
			double w = Math.abs(Math.atan( (newValues[i] - oldValues[i]) / newValues[i] )) * 2*(1-minConst) / Math.PI + minConst;
			
			shaves.get(i).set(component, (1-w)*rshaves.get(i).get(component) + w * shaves.get(i).get(component) );
			
			if( shaves.get(i).get(component) > 1 ) throw new RuntimeException("_shaves["+i+"]["+component+"] > 1");
		}
		return shaves;*/
		double w = 0;
		for(int i = 0 ; i < shaves.size(); ++i)
		{
			double minConst = 1e-1;
			w = Math.abs(Math.atan( shaves.size() * (newValues[i] - oldValues[i]) / newValues[i] )) * 2*(1-minConst) / Math.PI + minConst;

			shaves.get(i).set(component, rshaves.get(i).get(component) * w + shaves.get(i).get(component) * (1-w));
		}
		return shaves;
	}

	/*
	 * (non-Javadoc)
	 * @see Mechanisms.ShavingStrategy#initShaves(int, double[])
	 */
	@Override
	public double[] initShaves(int numberOfBins, double[] shaves) 
	{
		for(int i = 0; i < numberOfBins; ++i)
			shaves[i] = 0.0;
		return shaves;
	}

	/*
	 * (non-Javadoc)
	 * @see Mechanisms.ShavingStrategy#getInitShave()
	 */
	@Override
	public double getInitShave() 
	{
		return 0.0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see Mechanisms.ShavingStrategy#isShadingRequired(org.apache.commons.math3.linear.RealMatrix, int, double)
	 */
	@Override
	public boolean isShadingRequired(RealMatrix utilities, int bin, double maxUtilityGain) 
	{
		if( Math.abs( utilities.getEntry(bin, 0) - maxUtilityGain ) < 1e-6 )
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see Mechanisms.ShavingStrategy#getTruthfulShadingFactor()
	 */
	@Override
	public double getTruthfulShadingFactor() 
	{
		return 0.0;
	}

	/*
	 * (non-Javadoc)
	 * @see Mechanisms.ShavingStrategy#isShadingValid(double, double)
	 */
	@Override
	public boolean isShadingValid(double value, double shadingFactor) 
	{
		if( value - shadingFactor > 0 )
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
		//if( ! isShadingValid(value, shadingFactor) )
		//	//return 0.;
		//	throw new ShavingException("The value " + value + " cannot be shaded by " + shadingFactor, value, shadingFactor);
		if( isBuyer )
			return Math.max(value - shadingFactor, 1e-6);
		else
			return value + shadingFactor;
	}
	
	/*
	 * (non-Javadoc)
	 * @see Mechanisms.ShavingStrategy#getUnShadedValue(double, double, boolean)
	 */
	@Override
	public double getUnShadedValue(double reportedValue, double shadingFactor, boolean isBuyer) 
	{
		if( isBuyer )
			return reportedValue + shadingFactor;
		else
			return reportedValue - shadingFactor;
	}
	
	/*
	 * (non-Javadoc)
	 * @see Mechanisms.ShavingStrategy#isExceedCritical(double, double)
	 */
	@Override
	public boolean isExceedCritical(double criticalShadingFactor, double shadingFactor)
	{
		if( shadingFactor >= criticalShadingFactor )
			return true;
		return false;
	}
}
