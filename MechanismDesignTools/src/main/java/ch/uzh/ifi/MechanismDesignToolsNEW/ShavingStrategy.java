package ch.uzh.ifi.MechanismDesignToolsNEW;

import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;

import ch.uzh.ifi.Mechanisms.ShavingException;

/*
 * Describes interface for different shaving strategies. 
 */
public interface ShavingStrategy 
{
	/*
	 * The method initializes the shaves array. The init value depends on the type of shaving used.
	 * @param numberOfBins - the number of bins in BNE approximation algorithm
	 * @param shaves - an array of shaves to be initialized
	 * @return a new array of initialized shaves
	 */
	public double[] initShaves(int numberOfBins, double[] shaves);
	
	/*
	 * 
	 */
	public double getInitShave();
	
	/*
	 * The interface defines how the shaves should be updates on each iteration.
	 * @param numberOfBins - the number of bins of BNE approximation algorithm
	 * @param oldValues - utility values using shading factor of the previous iteration
	 * @param newValues - utility values using shading factor of the current iteration
	 * @param shaves - a reference to the shaves of bidders in different bins
	 * @param rshaves - a reference to "relative" shaves computed on current iteration.
	 * @return new shaves array 
	 */
	//public double[] updateShaves(int numberOfBins, double[] oldValues, double[] newValues, double[] shaves, double[] rshaves);
	public List< List<Double> > updateShaves(double[] oldValues, double[] newValues, List<List<Double> > shaves, List<List<Double> > rshaves, int component);
	
	/*
	 * The method checks if any shading really required for the specified utility gain in the specified bin.
	 * @param utilities  - a matrix n x m where n is the number of bins, m is the number of grid points in BNE approximation algorithm.
	 * @param bin - the number of a particular bin of interest
	 * @param maxUtilityGain - a value of utility gain for which it is needed to check if any shading really required
	 * @return false if truthful bidding results in the same utility gain
	 */
	public boolean isShadingRequired(RealMatrix utilities, int bin, double maxUtilityGain);
	
	/*
	 * The method checks if the specified shading factor is valid for the given value.
	 * @param value - a value to be checked
	 * @param shadingFactor - a shading factor to be checked
	 * @return true if the shading is valid and false otherwise
	 */
	public boolean isShadingValid(double value, double shadingFactor);
	
	/*
	 * The method returns the value shaded by a specified shading factor.
	 * @param value - a value to be shaved
	 * @param shadingFactor - a shading factor to be applied
	 * @param isBuyer - true if an agent is a buyer, false  in case of a seller
	 * @return a new (shaded) value
	 */
	public double getShadedValue(double value, double shadingFactor, boolean isBuyer) throws ShavingException;
	
	/*
	 * The method returns the real value based on reported value and the specified shading factor.
	 * @param reportedValue - a reported value
	 * @param shadingFactor - a shading factor to be applied
	 * @param isBuyer - true if an agent is a buyer, false  in case of a seller
	 * @return a new (shaded) value
	 */
	public double getUnShadedValue(double reportedValue, double shadingFactor, boolean isBuyer);
	
	/*
	 * The method returns the shading factor which is truthful for a particular shading strategy.
	 * @return truthful shading factor
	 */
	public double getTruthfulShadingFactor();
	
	/*
	 * The method returns true if the specified shading factor exceeds the critical one, i.e., the factor for which nothing is allocated.
	 * @param criticalShadingFactor - the value of shading factor which signals that if to 'exceed' it, no allocation is feasible. 
	 * @param shadingFactor - shading factor to be compared with the critical one
	 */
	public boolean isExceedCritical(double criticalShadingFactor, double shadingFactor);
}
