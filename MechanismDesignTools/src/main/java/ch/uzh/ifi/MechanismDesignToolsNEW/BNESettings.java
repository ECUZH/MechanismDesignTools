package ch.uzh.ifi.MechanismDesignToolsNEW;

import ch.uzh.ifi.Mechanisms.IPlannerFactory;


/*
 * The class encapsulates all settings required for BNESolver.
 */
public class BNESettings 
{
	/*
	 * A simple (default) constructor
	 */
	public BNESettings()
	{
		_numberOfBins = 0;
		_numberOfGridPoints = 0;
		_numberOfSamples = 0;
		_optimizerFactory = null;
		_numberOfThreads = 0;
	}
	
	/*
	 * The method sets up the number of bins for BNESolver.
	 * @param numberOfBins - the number of bins for the BNE solver
	 */
	public void setNumberOfBins( int numberOfBins )
	{
		if( numberOfBins <= 0) 			throw new RuntimeException("The number of bins should be positive: " + numberOfBins);
		_numberOfBins = numberOfBins;
	}
	
	/*
	 * The method sets up the number of grid points used by the optimization phase of BNESolver.
	 * @param numberOfGridPoints - the number of grid points
	 */
	public void setNumberOfGridPoints( int numberOfGridPoints )
	{
		if( numberOfGridPoints <= 0 )	throw new RuntimeException("The number of grid points should be positive: " + numberOfGridPoints);
		_numberOfGridPoints = numberOfGridPoints;
	}
	
	/*
	 * The method sets up the number of Samples.
	 * @param numberOfSamples - the number of samples
	 */
	public void setNumberOfSamples( int numberOfSamples )
	{
		if( numberOfSamples <= 0 )		throw new RuntimeException("The number of samples should be positive: " + numberOfSamples);
		_numberOfSamples = numberOfSamples;
	}
	
	/*
	 * The method sets up the epsilon parameter of BNESolver
	 * @param espilon
	 */
	public void setEpsilon( double epsilon )
	{
		if( epsilon < 0. ) 				throw new RuntimeException("Epsilon should be non-negative: " + epsilon);
		_epsilon = epsilon;
	}
	
	/*
	 * The method sets up the number of threads to be used by BNESolver
	 * @param numberOfThreads - the number of threads to be used by BNESolver
	 */
	public void setNumberOfThreads( int numberOfThreads )
	{
		if( numberOfThreads < 1)		throw new RuntimeException("The number of threads should be at least 1: " + numberOfThreads);
		_numberOfThreads = numberOfThreads;
	}
	
	/*
	 * The method sets up the precision of the approximation algorithm (the epsilon parameter)
	 * @param precision - the epsilon parameter of the algorithm
	 */
	public void setPrecision( double precision )
	{
		if( precision <= 0 )			throw new RuntimeException("The precision should be strictly positive: " + precision);
		PRECISION = precision;
	}
	
	/*
	 * THe method sets up the optimizer factory, i.e. the factory which produces different local optimization
	 * engines.
	 */
	public void setLocalOptimizerFactory(IOptimizerFactory optimizerFactory)
	{
		_optimizerFactory = optimizerFactory;
	}
	
	/*
	 * The method returns the number of bins.
	 * @return the number of bins
	 */
	public int getNumberOfBins()
	{
		return _numberOfBins;
	}
	
	/*
	 * The method returns the number of grid points.
	 * @return the number of grid points
	 */
	public int getNumberOfGridPoints()
	{
		return _numberOfGridPoints;
	}
	
	/*
	 * The method returns the number of samples
	 * @return the number of samples
	 */
	public int getNumberOfSamples()
	{
		return _numberOfSamples;
	}
	
	/*
	 * The method returns the epsilon parameter of BNESolver.
	 * @return epsilon
	 */
	public double getEpsilon()
	{
		return _epsilon;
	}
	
	/*
	 * The method returns the number of threads used by BNESolver
	 * @return the number of threads
	 */
	public int getNumberOfThreads()
	{
		return _numberOfThreads;
	}

	/*
	 * The method returns the maximum number of iterations after which the approximation process should be aborted.
	 * @return the max number of iterations.
	 */
	public int getMaxIterations()
	{
		return MAX_ITERATIONS;
	}
	
	/*
	 * The method returns the precision of the approximation algorithm.
	 * @return the precision.
	 */
	public double getPrecision()
	{
		return PRECISION;
	}
	
	/*
	 * The method returns the sort quantile for the bin sorting procedure.
	 * @return the bin sorting quantile
	 */
	public double getSortQuantile()
	{
		return SORT_QUANTILE;
	}
	
	/*
	 * THe method returns the optimizer factory, i.e. the factory which produces different local optimization
	 * engines.
	 */
	public IOptimizerFactory getLocalOptimizerFactory()
	{
		if(_optimizerFactory == null) throw new RuntimeException("WARNING: no optimizer factory was set up.");
		return _optimizerFactory;
	}
	
	private int _numberOfBins;						//Number of bins used to classify agents
	private int _numberOfGridPoints;				//Number of grid points used by the global optimization phase of BNESolver
	private int _numberOfSamples;					//Number of samples
	private IOptimizerFactory _optimizerFactory;
	private int _numberOfThreads;					//The number of threads to be used by BNESolver
	private double _epsilon;						//The epsilon parameter in epsilon-BNE
	
	private int MAX_ITERATIONS = 1000;				//Maximum number of iterations after which approximation of BNE should be aborted
	private double PRECISION = 1e-2;				//The search finishes if the delta in utilities is no more than PRECISION*100% (1e-2 -> 1%)

	private double SORT_QUANTILE = 0.9;					//The p-quantile for sorting multivalued types into bins
}
