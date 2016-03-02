package ch.uzh.ifi.MechanismDesignToolsNEW;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.*;

import ch.uzh.ifi.MechanismDesignPrimitives.AtomicBid;
import ch.uzh.ifi.MechanismDesignPrimitives.CombinatorialType;
import ch.uzh.ifi.MechanismDesignPrimitives.IDomainGenerator;
import ch.uzh.ifi.MechanismDesignPrimitives.Type;
import ch.uzh.ifi.MechanismDesignPrimitives.AllocationEC;
import ch.uzh.ifi.Mechanisms.AgentTypeException;
import ch.uzh.ifi.Mechanisms.Auction;
import ch.uzh.ifi.Mechanisms.DoubleSidedMarket;
import ch.uzh.ifi.Mechanisms.PaymentException;
import ch.uzh.ifi.Mechanisms.ProbabilisticReverseCA;
import ch.uzh.ifi.Mechanisms.IMechanismFactory;
import ch.uzh.ifi.Mechanisms.ShavingException;
import ch.uzh.ifi.Mechanisms.ZollingerMechanism;

/**
 * The class computes the overall utility of many sampled games with a single fixed agent
 * and his strategy and fixed strategies of other agents.
 * @author Dmitry Moor
 */
public class UtilityEstimator
{

	private static final Logger _logger = LogManager.getLogger(UtilityEstimator.class);
	
	/**
	 * Constructor.
	 * @param localNumberOfSamples number of samples to use for utility estimation.
	 * @param v type of a fixed agent
	 * @param s strategy of the fixed agent
	 * @param mechanismFactory factory producing mechanisms for which BNE should be computed
	 * @param domainGenerator domain generator
	 * @param agentsTypes types of agents
	 * @param shavingStrategies an array (for multidimensional mechanisms) of shaving strategies
	 * @param shaves shaving factors per bin
	 * @param bins bins
	 */
	public UtilityEstimator(int localNumberOfSamples, Type v, double[] s, IMechanismFactory mechanismFactory, IDomainGenerator domainGenerator,
							List<Type> agentsTypes, ShavingStrategy[] shavingStrategies, List<List<Double>> shaves, List<List<Type>> bins)
	{
		init(localNumberOfSamples, v, s, mechanismFactory, domainGenerator, agentsTypes, shavingStrategies, shaves, bins);
		_seed = 0;
	}

	/**
	 * Constructor.
	 * @param localNumberOfSamples number of samples to use for utility estimation.
	 * @param v type of a fixed agent
	 * @param s strategy of the fixed agent
	 * @param mechanismFactory factory producing mechanisms for which BNE should be computed
	 * @param domainGenerator domain generator
	 * @param agentsTypes types of agents
	 * @param shavingStrategies an array (for multidimensional mechanisms) of shaving strategies
	 * @param shaves shaving factors per bin
	 * @param bins bins
	 * @param seed a random seed
	 */
	public UtilityEstimator(int localNumberOfSamples, Type v, double[] s, IMechanismFactory mechanismFactory, IDomainGenerator domainGenerator,
							List<Type> agentsTypes, ShavingStrategy[] shavingStrategies, List<List<Double>> shaves, List<List<Type>> bins, int seed)
	{
		init(localNumberOfSamples, v, s, mechanismFactory, domainGenerator, agentsTypes, shavingStrategies, shaves, bins);
		_seed = seed;
	}
	
	/**
	 * Initialization.
	 * @param localNumberOfSamples number of samples to use for utility estimation.
	 * @param v type of a fixed agent
	 * @param s strategy of the fixed agent
	 * @param mechanismFactory factory producing mechanisms for which BNE should be computed
	 * @param domainGenerator domain generator
	 * @param agentsTypes types of agents
	 * @param shavingStrategies an array (for multidimensional mechanisms) of shaving strategies
	 * @param shaves shaving factors per bin
	 * @param bins bins
	 */
	private void init(int localNumberOfSamples, Type v, double[] s, IMechanismFactory mechanismFactory, IDomainGenerator domainGenerator,
							List<Type> agentsTypes, ShavingStrategy[] shavingStrategies, List<List<Double>> shaves, List<List<Type>> bins)
	{
		_numberOfSamples = localNumberOfSamples;
		_v = v;
		_s = s;
		_utility = 0;
		_mechanismFactory = mechanismFactory;
		_domainGenerator  = domainGenerator;
		_agentsTypes = agentsTypes;
		_shavingStrategies = shavingStrategies;
		_shaves = shaves;
		_bins = bins;
	}
	
	/**
	 * The method implements the utility() functionality in multiple threads.
	 * @return a stochastic double object which includes the utility mean, stddev and the number of samples.
	 */
	public StochasticDouble computeUtility()
	{
		List<Type> types;
		double[] sampleUtilities = new double[_numberOfSamples];
		int numberOfEmptyCoreCases = 0;
		int numberOfVCGInCoreCases = 0;
		
		for(int i = 0; i < _numberOfSamples; ++i)
		{
			types = constructSampleTypes( _v, _s, i );							//Generate i-th sample
			for(int j = 0; j < types.size(); ++j)
				if( types.get(j).getAgentId() == _v.getAgentId())
					_v = types.get(j);

			_logger.debug("Types generated: " + types.toString());
			
			/*int itsBinIdx = 0;
			boolean isFound = false;
			for(List<Type> b : _bins)
			{
				for(Type t : b)
					if( t.getAgentId() == _v.getAgentId() )
					{
						isFound = true;
						break;
					}
				if(isFound)
					break;
				itsBinIdx += 1;
			}*/
			
			Auction G;															//Create a new market with new random plans and newly generated sample types
			if( _seed > 0)
				G = _mechanismFactory.produceMechanism(types, _seed*i);
			else
				G = _mechanismFactory.produceMechanism(types);
				
			try
			{
				G.solveIt();
			}
			catch(PaymentException e1)
			{
				if( e1.getMessage().equals("Empty Core") )
				{
					numberOfEmptyCoreCases += 1;
					_logger.warn("#emptyCoreCases="+numberOfEmptyCoreCases+ " out of " + _numberOfSamples);
				}
				else if(e1.getMessage().equals("VCG is in the Core"))
				{	
					numberOfVCGInCoreCases += 1;
					_logger.warn("#VCGinCoreCases="+numberOfVCGInCoreCases+ " out of " + _numberOfSamples);
				}
				else
					_logger.error("Wrong error msg: " + e1.toString());
			}
			catch (Exception e) 
			{
				_logger.error("ERROR when solving the auction " + e.toString());
			}

			//TODO: rework this crazy shit !!
			if( ((Double)_v.getTypeComponent(0, AtomicBid.IsBidder) == 0.0) && G.getAllocation().isAllocated(_v.getAgentId())  )	//if the agent is an auctioneer (not a bidder) and allocated
			{
				if( (G.getAllocation().getAllocatedWelfare() >= 0) || (G.getPaymentRule() == "VCG"))
				{
					try
					{
						assert( G.getAllocation().getNumberOfAllocatedAuctioneers() == G.getPayments().length );
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
					double val = 0.;
					if( G.isReverse())
						val = G.getAllocation().getAuctioneersAllocatedValue(0) / _s[0];						//buyer's true value
					else
						val = -1* G.getAllocation().getAuctioneersAllocatedValue(0) * _s[0];					//seller's true cost //TODO: never tested this case
					
					double payment = 0.;
					
					try 
					{
						payment = G.getPayments()[0];
					}
					catch (Exception e) 
					{
						System.out.println("Exception when accessing buyer's payment " + e.toString());
					}
						
					sampleUtilities[i] = val - payment;
					if( (val - payment < 0) && (G.getPaymentRule() != "VCG") )
						System.out.println("Negative utility "+(val - payment)+" val="+val+" payment="+payment+" for plans "+((DoubleSidedMarket)G).getPlans().toString() + 
								     " types " + types.toString() + " while alloc " + G.getAllocation().getAllocatedWelfare());
				}
				else
				{
					double val = G.getAllocation().getAuctioneersAllocatedValue(0);
					sampleUtilities[i] = val / _s[0] - val;
					assert(_utility >= 0);
				}
			}
			else if( ((Double)_v.getTypeComponent(0, AtomicBid.IsBidder) == 1.0) && (G.getAllocation().getNumberOfAllocatedAuctioneers() > 0) )
			{
				if( G.isBudgetBalanced() || (G.getAllocation().getAllocatedWelfare() >= 0) || (G.getPaymentRule() == "VCG"))//TODO: simplify this
				{
					for(int k = 0; k < G.getAllocation().getNumberOfAllocatedAuctioneers(); ++k)
						if( G.getAllocation().getBiddersInvolved(k).contains( _v.getAgentId() ) )							//If the bidder participates in this trade
						{
							int bidderIdx = G.getAllocation().getBiddersInvolved(k).indexOf( _v.getAgentId() );
							double trueAllocatedValue = 0.;
							
							if( G.isReverse() )
								trueAllocatedValue = _shavingStrategies[0].getUnShadedValue(-1 * G.getAllocation().getBiddersAllocatedValue(0, bidderIdx), _s[0], false);	//true allocated cost
							else
							{
								int itsAllocatedBundle = G.getAllocation().getAllocatedBundlesOfTrade(k).get(bidderIdx);
								double reportedValue = _v.getAtom(itsAllocatedBundle).getValue();
								trueAllocatedValue = _shavingStrategies[0].getUnShadedValue(reportedValue, _s[0], true);
								
								double realizedRV = ((AllocationEC)(G.getAllocation())).getRealizedRV(k, bidderIdx);
								trueAllocatedValue *= realizedRV;
							}
							double payment = 0.;
							try
							{
								if( _mechanismFactory.getMehcanismName().equals( DoubleSidedMarket.class.getSimpleName()) )
									payment = G.getPayments()[ G.getAllocation().getNumberOfAllocatedAuctioneers() + bidderIdx ];
								else
									payment = G.getPayments()[ bidderIdx ];
							}
							catch (Exception e) 
							{
								e.printStackTrace();
							}
							
							//System.out.println(G.toString() + "\n_s[0]=" + _s[0] + " AllocatedValue=" + G.getAllocation().getBiddersAllocatedValue(k, bidderIdx));
							
							if( ( ! G.isReverse() ) || _mechanismFactory.getMehcanismName().equals(DoubleSidedMarket.class.getSimpleName()) )
								sampleUtilities[i] = trueAllocatedValue - payment;
							else
								sampleUtilities[i] = trueAllocatedValue + payment;
														
							if( (sampleUtilities[i] < 0) && ( G.isExPostIR()) && (!G.getPaymentRule().equals("VCG") )  )
							{
								System.err.println("Negative utility for the bidder" + sampleUtilities[i] + " val=" + G.getAllocation().getBiddersAllocatedValue(0, bidderIdx) +", true val=" + trueAllocatedValue + " payment="+payment + /*" for plans " + G.getPlans().toString() +*/ " types " + types.toString() + " while alloc " + G.getAllocation().getAllocatedWelfare());
								
								try
								{
									for(int q = 0; q < G.getPayments().length; ++q)
										System.err.println("p["+q+"]=" + G.getPayments()[q]);
								}
								catch (Exception e) 
								{
									e.printStackTrace();
								}
									
								throw new RuntimeException("Negative utility for the seller");
							}
						}
						else
							sampleUtilities[i] = 0;						//else the bidder is not allocated => no utility gain
				}
				else
				{
					for(int k = 0; k < G.getAllocation().getNumberOfAllocatedAuctioneers(); ++k)
						if( G.getAllocation().getBiddersInvolved(0).contains( _v.getAgentId() ) )	//If the seller participates in this deal
						{
							int sellerIdx = G.getAllocation().getBiddersInvolved(0).indexOf( _v.getAgentId() );
							double reportedCost = G.getAllocation().getBiddersAllocatedValue(0, sellerIdx);
							double trueCost = reportedCost *_s[0];
								
							sampleUtilities[i] = -1* trueCost + reportedCost;
							assert(_utility >= 0);
								
							double budgetNeeded = (G.getAllocation().getTotalAllocatedBiddersValue(0) - G.getAllocation().getAuctioneersAllocatedValue(0));									
							if(budgetNeeded < 0)
								_logger.error("Budget is negative: " + G.getAllocation().getTotalAllocatedBiddersValue(0) + 
									     " - " + G.getAllocation().getAuctioneersAllocatedValue(0) + " = " + budgetNeeded);
						}
				}
			}
		}

		_logger.info("numberOfEmptyCoreCases = " + numberOfEmptyCoreCases + " numberOfVCGInCoreCases = " + numberOfVCGInCoreCases + 
				           " in total = " + (numberOfEmptyCoreCases + numberOfVCGInCoreCases) + " out of " + _numberOfSamples);
		_logger.info( numberOfEmptyCoreCases + " " + numberOfVCGInCoreCases + " ; ");

		
		_utilityStd  = 0.;
		
		_utility = 0.;
		for(int i = 0; i < _numberOfSamples; ++i)
			_utility += sampleUtilities[i];

		_utility /= _numberOfSamples;
		
		for(int i = 0; i < _numberOfSamples; ++i)
			_utilityStd += Math.pow(sampleUtilities[i] - _utility, 2);
		
		_utilityStd = Math.sqrt( _utilityStd / _numberOfSamples);
		
		StochasticDouble res = new StochasticDouble(_utility, _utilityStd, _numberOfSamples);
		res.addSamples(sampleUtilities);
		return res;
	}
	
	/*
	 * 
	 */
	public List< List<Double> > getShaves()
	{
		return _shaves;
	}
	
	/*
	 * 
	 */
	public void setShaves(List< List<Double> > shaves)
	{
		_shaves = shaves;
	}
	
	/**
	 * The method returns the number of bins used.
	 * @return the number of bins used
	 */
	public int getNumberOfBins()
	{
		return _bins.size();
	}
	
	/**
	 * The method returns a standard deviation of the utility computed in sampled games.
	 * @return a standard deviation in utility
	 */
	public double getUtilityStd()
	{
		return _utilityStd;
	}
	
	/**
	 * The method generates sample types.
	 * @param v - the type of an agent for which a new shading factor should be checked
	 * @param s - the new shading factor which should be applied to an agent v (an array in multidimensional mechanisms)
	 * @param iterNumber - the number of BNE iteration (is used if we want to exploit correlated samples)
	 * @return a list of a new sample types
	 */
	private List<Type> constructSampleTypes( Type v, double[] s, int iterNumber)
	{
		List<Type> types = new ArrayList<Type>();
		
		//Generate types and setup strategies
		for(int j = 0; j < _agentsTypes.size(); ++j)
		{			
			Type ct = new CombinatorialType();
			int itsBinIdx = -1;
			
			try
			{
				itsBinIdx = getBinIndex(_agentsTypes.get(j));
			} 
			catch (AgentTypeException e1) 
			{
				if( ! (_mechanismFactory.getMehcanismName().equals( ProbabilisticReverseCA.class.getSimpleName() ) || 
					   _mechanismFactory.getMehcanismName().equals( ZollingerMechanism.class.getSimpleName() ) ))
				{
					e1.printStackTrace();
				}
				else
				{
					//If agent's type is present but the agent is not strategic (=> not in any of bins), then use its type and do not 
					//change its strategy. E.g., in a one-sided reverse CA when buyer's type is needed only in order to compute the SW of plans  
					ct = _domainGenerator.generateBid( (iterNumber+1) * (j+1) * 2015, _agentsTypes.get(j) );
					types.add(ct);
					continue;
				}
			}
			
			//2. Generate a new type
			ct = _domainGenerator.generateBid( (iterNumber+1) * (j+1) * 2015, _agentsTypes.get(j) );
			
			if(ct.getNumberOfAtoms() == 0)
				_logger.warn("Empty bid");
			else
				_logger.debug("ct: " + ct.toString());
			
			//3. Setup strategies for all agents
			List<Double> shadingFactor = new ArrayList<Double>();
			Type t = null;
			
			if( _agentsTypes.get(j).getAgentId() != v.getAgentId() ) 
			{
				shadingFactor =  _shaves.get(itsBinIdx);
				t = ct;
			}
			else
			{
				for(double i : s)
					shadingFactor.add(i);
				t = ct;
			}
			
			boolean isBuyer = false;
			boolean isAuctioneer = (Double)_agentsTypes.get(j).getTypeComponent(0, AtomicBid.IsBidder) == 0.0 ? true : false;
			
			if( (!isAuctioneer && !_mechanismFactory.isReverse())  || ( isAuctioneer && _mechanismFactory.isReverse()) )
				isBuyer = true;
			else
				isBuyer = false;
			
			try																							//Shade the value
			{
				for(int atomIdx = 0; atomIdx < ct.getNumberOfAtoms(); ++atomIdx)
					//for(int i = 0; i < _shavingStrategies.length; ++i)								//For multidimensional mechanisms
					{
						String dimensionName = AtomicBid.Value;
						ct.setTypeComponent(atomIdx, dimensionName, _shavingStrategies[0/*atomIdx*//*i*/].getShadedValue( t.getAtom(atomIdx).getValue(), shadingFactor.get(0/*atomIdx*/), isBuyer) );
					}
			}
			catch (ShavingException e) 																	//Large shading exception
			{
				_logger.error(e.toString() + ". Shading type " + t.toString() + " by " + shadingFactor.toString() + ". ");
				for(int atomIdx = 0; atomIdx < _agentsTypes.get(j).getNumberOfAtoms(); ++atomIdx)	
					ct.setTypeComponent(atomIdx, AtomicBid.Value, 1e-6);
			}
			
			types.add( ct );
		}
		return types;
	}
	
	/**
	 * Given the type, the method returns the bin which contains the type.
	 * @param agentType - the type of an agent of interest
	 * @return an index of the bin corresponding to the type
	 * @throws AgentTypeException if no bin can be found for the specified type
	 */
	private int getBinIndex(Type agentType) throws AgentTypeException
	{
		for(List<Type> b : _bins)
			if( b.contains(agentType) )
				return _bins.indexOf(b);
		throw new AgentTypeException("There's no such type " + agentType.toString() + " in bins: " + _bins.toString());
	}
	
	
	private IMechanismFactory _mechanismFactory;		//Factory producing sample mechanisms
	private IDomainGenerator _domainGenerator;			//Domain generator
	private List<Type> _agentsTypes;					//True types of agents
	private ShavingStrategy[] _shavingStrategies;		//Shading strategy to be used for shading agent's values
	private List< List<Double> > _shaves;				//A list of current shaving factors per bin. Different shaves within a bin correspond to different dimensions of the strategy space
	private List<List<Type> > _bins;					//Bins allocating agents according to some classification rule

	private int _numberOfSamples;						//The number of samples to be used for utility estimation
	private Type _v;									//The type of a fixed agent
	private double[] _s;								//The (multidimensional) strategy of the fixed agent
	private double _utility;							//Mean utility
	private double _utilityStd;							//Standard deviation of the utility
	
	private long _seed;									//A seed to be used for sampling
}
