package ch.uzh.ifi.MechanismDesignToolsNEW;

import java.util.LinkedList;
import java.util.List;

import ch.uzh.ifi.MechanismDesignPrimitives.Type;

/*
 * The class implements a Multi-Threaded (MT) approach to utility computation.
 */
public class MTUtilityComputationEngine implements IUtilityComputationEngine
{
	/*
	 * Constructor
	 * @param numberOfThreads - the number of threads to be used for utility computation.
	 */
	public MTUtilityComputationEngine(int numberOfThreads, UtilityEstimatorFactory utilityEstimatorFactory, boolean useCorrelatedSamples, int seed)
	{
		_numberOfThreads = numberOfThreads;
		_utilityEstimatorFactory = utilityEstimatorFactory;
		_useCorrelatedSamples = useCorrelatedSamples;
		_seed = seed;
	}

	/*
	 * (non-Javadoc)
	 * @see Tools.IUtilityComputationEngine#computeUtility(double[], Mechanisms.Type)
	 */
	@Override
	public StochasticDouble computeUtility(double[] s, Type v) 
	{
		_uStdTotal = 0.;
		StochasticDouble u = new StochasticDouble(0., 0., 0);
		_u = new StochasticDouble[ _numberOfThreads ];
		
		try
		{
			List<Thread> threads = new LinkedList<Thread>();
			for(int i = 0; i < _numberOfThreads; ++i)
			{
				Thread thread = new Thread(new SamplingWorker("Thread", i, _useCorrelatedSamples ? _utilityEstimatorFactory.produceUtilityEstimator(20 * _seed + (i+1), i) : _utilityEstimatorFactory.produceUtilityEstimator(i)) );
				threads.add(thread);
			}
			
			for(int i = 0; i < _numberOfThreads; ++i)
				threads.get(i).start();
			
			for(int i = 0; i < _numberOfThreads; ++i)
				threads.get(i).join(0);		
		}
		catch(InterruptedException e)
		{
		    e.printStackTrace();
		}

		for(int i = 0; i < _numberOfThreads; ++i)
			u = u.add(_u[i]);
		
		return u;
	}
	
	public double getUtilityStd()
	{
		return _uStdTotal;
	}
	
	/*
	 * A class implementing a utility computation worker.
	 */
	private class SamplingWorker implements Runnable
	{
		private Thread _thread;										//A thread object
		private String _threadName;									//The thread's name
		private int _threadId;										//A thread id
		private UtilityEstimator _utilityEstimator;					//Utility estimator object
		
		/*
		 * @param name - a name for the thread
		 * @param threadId - an id of the thread
		 * @param utilityEstimator - utility estimation algorithm
		 */
		public SamplingWorker(String name, int threadId, UtilityEstimator utilityEstimator)
		{
			_threadName = name + threadId;
			_threadId = threadId;
			_utilityEstimator = utilityEstimator;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run()
		{
			_u[_threadId] = _utilityEstimator.computeUtility();
		}
		
		/*
		 * 
		 */
		public void start()
		{
			if(_thread == null)
			{
				_thread = new Thread(this, _threadName);
				_thread.start();
			}
		}
	}

	
	private int _numberOfThreads;					//The number of threads to be used
	private StochasticDouble[] _u;					//An array in which different threads put their locally computed utilities
	private UtilityEstimatorFactory _utilityEstimatorFactory;//Utility estimator
	
	private double _uStdTotal;
	private boolean _useCorrelatedSamples;
	private int _seed;
}
