package ch.uzh.ifi.MechanismDesignToolsNEW;

//import static org.junit.Assert.*;

import java.awt.Color;
import java.io.IOException;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

//import mpi.MPI;
/*
import org.junit.Test;

import com.xeiam.xchart.BitmapEncoder;
import com.xeiam.xchart.BitmapEncoder.BitmapFormat;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.StyleManager.ChartTheme;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.SwingWrapper;

import Mechanisms.AdditiveShavingStrategy;
import Mechanisms.Allocation;
import Mechanisms.AtomicBid;
import Mechanisms.Auction;
import Mechanisms.CAXOR;
import Mechanisms.CombinatorialType;
import Mechanisms.DoubleSidedMarket;
import Mechanisms.DoubleSidedMarketFactory;
import Mechanisms.FirstPriceAuction;
import Mechanisms.GeneralErrorPlanner;
import Mechanisms.IMechanismFactory;
import Mechanisms.IPlannerFactory;
import Mechanisms.GeneralPlannerFactory;
import Mechanisms.JoinPlannerFactory;
import Mechanisms.MultiplicativeShavingStrategy;
import Mechanisms.Planner;
import Mechanisms.SecondPriceAuction;
import Mechanisms.SemanticWebType;
import Mechanisms.ShavingStrategy;
import Mechanisms.SimpleErrorPlanner;
import Mechanisms.SimplePlannerFactory;
import Mechanisms.SimpleType;
import Mechanisms.ThresholdPayments;
import Mechanisms.Type;
*/
public class testBNESolver {
/*
	@Test
	public void testFirstPriceAuction() 
	{
		int numberOfAgents = 9;
		
		List<Type> lst = new LinkedList<Type>();
		
		for(int i = 0; i < numberOfAgents; ++i)
		{
			List<Integer> items	 = new LinkedList<Integer>();
			items.add(1);
			AtomicBid atom = new AtomicBid(i+1, items, 0.1 * (i+1));
			atom.setTypeComponent("Distribution", 1.0);
			atom.setTypeComponent("minValue", 0.0);
			atom.setTypeComponent("maxValue", 1.0);//((double)(i/3+1))/3.
			Type t = new CombinatorialType();
			t.addAtomicBid(atom);
			lst.add(t);
		}
		
		Auction auction = new FirstPriceAuction(lst);
		BNESolver bne = new BNESolver(auction, lst, 3, 100, 100, new MultiplicativeShavingStrategy() );
		bne.sortToBins();
		bne.solveIt();
		
		assertTrue(bne.getShavingFactor(0) == 1.0);
		assertTrue(Math.abs(bne.getShavingFactor(1) - 1.0) < 1e-2);
		assertTrue(Math.abs(bne.getShavingFactor(2) - ((double)lst.size() - 1)/lst.size()) < 0.1);
	}*/
/*	
	@Test
	public void testSecondPriceAuction() 
	{
		int numberOfAgents = 9;
		
		List<Type> lst = new LinkedList<Type>();
		
		for(int i = 0; i < numberOfAgents; ++i)
		{
			List<Integer> items	 = new LinkedList<Integer>();
			items.add(1);
			AtomicBid atom = new AtomicBid(i+1, items, 0.1 * (i+1));
			atom.setTypeComponent("Distribution", 1.0);
			atom.setTypeComponent("minValue", 0.0);
			atom.setTypeComponent("maxValue", 1.0);
			Type t = new CombinatorialType();
			t.addAtomicBid(atom);
			lst.add(t);
		}
		
		Auction auction = new SecondPriceAuction(lst);
		BNESolver bne = new BNESolver(auction, lst, 3, 10, 10, new MultiplicativeShavingStrategy() );
		bne.sortToBins();
		bne.solveIt();
		
		assertTrue(bne.getShavingFactor(0) == 1.0);
		assertTrue(bne.getShavingFactor(1) == 1.0);
		assertTrue(bne.getShavingFactor(2) == 1.0);
		System.out.println(bne.toString());
	}
*/
/*	@Test
	public void testCombinatorialAuctionLLG() 
	{
		System.out.println("Launch the test");
		LocalTime time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
		
		int numberOfAgents = 3;
		int numberOfItems = 2;
		
		List<Integer> items1 = new LinkedList<Integer>();
		items1.add(1);
		
		List<Integer> items2 = new LinkedList<Integer>();
		items2.add(2);
		
		List<Integer> items3 = new LinkedList<Integer>();
		items3.add(1);
		items3.add(2);
		
		AtomicBid atom1 = new AtomicBid(1, items1, 0.9);
		atom1.setTypeComponent("Distribution", 1.0);
		atom1.setTypeComponent("minValue", 0.0);
		atom1.setTypeComponent("maxValue", 1.0);
		
		AtomicBid atom2 = new AtomicBid(2, items2, 0.8);
		atom2.setTypeComponent("Distribution", 1.0);
		atom2.setTypeComponent("minValue", 0.0);
		atom2.setTypeComponent("maxValue", 1.0);
		
		AtomicBid atom3 = new AtomicBid(3, items3, 0.9);
		atom3.setTypeComponent("Distribution", 1.0);
		atom3.setTypeComponent("minValue", 0.0);
		atom3.setTypeComponent("maxValue", 2.0);
		
		Type t1 = new CombinatorialType();
		t1.addAtomicBid(atom1);
		
		Type t2 = new CombinatorialType();
		t2.addAtomicBid(atom2);
		
		Type t3 = new CombinatorialType();
		t3.addAtomicBid(atom3);
		
		List<Type> lst = new LinkedList<Type>();
		lst.add(t1);
		lst.add(t2);
		lst.add(t3);

		Auction auction = new CAXOR(numberOfAgents, numberOfItems, lst, "VCGNearest2");
		
		ShavingStrategy[] strategies = new ShavingStrategy[1];
		strategies[0] = new AdditiveShavingStrategy();
		
		BNESolver bne = new BNESolver(auction, lst, 2, 10, 100, strategies );
		bne.setPrecision( 1e-3 );
		bne.sortToBins();
		
		bne.solveIt();

		time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());

		//assertTrue( (bne.getShavingFactor(0) < 0.21) && (bne.getShavingFactor(0) > 0.13));	//ideally 0.17
		//assertTrue( Math.abs(bne.getShavingFactor(1) - 0.0) < 1e-3);
		System.out.println(bne.toString());
	}*/
/*	
	@Test
	public void testCombinatorialAuctionLLG_Multivalued()
	{
		System.out.println("Launch the test");
		LocalTime time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
		
		int numberOfAgents = 3;
		int numberOfItems = 2;
		
		List<Integer> items1 = new LinkedList<Integer>();
		items1.add(1);
		
		List<Integer> items2 = new LinkedList<Integer>();
		items2.add(2);
		
		List<Integer> items3 = new LinkedList<Integer>();
		items3.add(1);
		items3.add(2);
		
		AtomicBid atom11 = new AtomicBid(1, items1, 0.9);
		atom11.setTypeComponent("Distribution", 1.0);
		atom11.setTypeComponent("minValue", 0.0);
		atom11.setTypeComponent("maxValue", 1.0);
		
		AtomicBid atom12 = new AtomicBid(1, items2, 0.0);
		atom12.setTypeComponent("Distribution", 1.0);
		atom12.setTypeComponent("minValue", 0.0);
		atom12.setTypeComponent("maxValue", 1.0);
		
		AtomicBid atom21 = new AtomicBid(2, items2, 0.8);
		atom21.setTypeComponent("Distribution", 1.0);
		atom21.setTypeComponent("minValue", 0.0);
		atom21.setTypeComponent("maxValue", 1.0);
		
		AtomicBid atom22 = new AtomicBid(2, items1, 0.0);
		atom22.setTypeComponent("Distribution", 1.0);
		atom22.setTypeComponent("minValue", 0.0);
		atom22.setTypeComponent("maxValue", 1.0);
		
		AtomicBid atom3 = new AtomicBid(3, items3, 0.9);
		atom3.setTypeComponent("Distribution", 1.0);
		atom3.setTypeComponent("minValue", 0.0);
		atom3.setTypeComponent("maxValue", 2.0);
		
		Type t1 = new CombinatorialType();
		t1.addAtomicBid(atom11);
		t1.addAtomicBid(atom12);
		
		Type t2 = new CombinatorialType();
		t2.addAtomicBid(atom21);
		t2.addAtomicBid(atom22);
		
		Type t3 = new CombinatorialType();
		t3.addAtomicBid(atom3);
		
		List<Type> lst = new LinkedList<Type>();
		lst.add(t1);
		lst.add(t2);
		lst.add(t3);

		Auction auction = new CAXOR(numberOfAgents, numberOfItems, lst, "VCGNearest2");
		
		ShavingStrategy[] strategies = new ShavingStrategy[2];
		strategies[0] = new MultiplicativeShavingStrategy();
		strategies[1] = new MultiplicativeShavingStrategy();
		
		BNESolver bne = new BNESolver(auction, lst, 3, 20, 30, strategies );
		bne.setPrecision( 1e-3 );
		bne.sortToBins();
		
		bne.solveIt();

		time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());

		//assertTrue( (bne.getShavingFactor(0) < 0.21) && (bne.getShavingFactor(0) > 0.13));	//ideally 0.17
		//assertTrue( Math.abs(bne.getShavingFactor(1) - 0.0) < 1e-3);
		System.out.println(bne.toString());
	}*/
	
/*	@Test
	public void testDoubleAuction_Threshold()
	{
		System.out.println("Launch the test");
		LocalTime time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
		
		int numberOfBuyers = 1;
		int numberOfSellers = 3;
		int numberOfAgents = numberOfBuyers + numberOfSellers;
		
		List<Integer> items = new LinkedList<Integer>();
		items.add(0);											//a query
		AtomicBid atomB1 = new AtomicBid(1, items, 0.6);
		atomB1.setTypeComponent("isSeller", 0.0);
		atomB1.setTypeComponent("Distribution", 1.0);
		atomB1.setTypeComponent("minValue", 0.0);
		atomB1.setTypeComponent("maxValue", 1.0);
		CombinatorialType b1 = new CombinatorialType(); 		//Buyer's type
		b1.addAtomicBid(atomB1);
		
		AtomicBid atomS1 = new AtomicBid(2, items, 0.1);
		atomS1.setTypeComponent("isSeller", 1.0);
		atomS1.setTypeComponent("Distribution", 1.0);
		atomS1.setTypeComponent("minValue", 0.0);
		atomS1.setTypeComponent("maxValue", 1.0);
		CombinatorialType s1 = new CombinatorialType(); 		//Seller's type
		s1.addAtomicBid(atomS1);
		
		AtomicBid atomS2 = new AtomicBid(3, items, 0.2);
		atomS2.setTypeComponent("isSeller", 1.0);
		atomS2.setTypeComponent("Distribution", 1.0);
		atomS2.setTypeComponent("minValue", 0.0);
		atomS2.setTypeComponent("maxValue", 1.0);
		CombinatorialType s2 = new CombinatorialType(); 		//Seller's type
		s2.addAtomicBid(atomS2);
		
		AtomicBid atomS3 = new AtomicBid(4, items, 0.3);
		atomS3.setTypeComponent("isSeller", 1.0);
		atomS3.setTypeComponent("Distribution", 1.0);
		atomS3.setTypeComponent("minValue", 0.0);
		atomS3.setTypeComponent("maxValue", 1.0);
		CombinatorialType s3 = new CombinatorialType(); 		//Seller's type
		s3.addAtomicBid(atomS3);
		
		List<Type> bids = new LinkedList<Type>();
		bids.add(b1);
		bids.add(s1);
		bids.add(s2);
		bids.add(s3);
		
		Planner planner = new SimplePlanner();
		Auction market = new DoubleSidedMarket(numberOfBuyers, numberOfSellers, bids, planner);
		
		ShavingStrategy[] strategies = new ShavingStrategy[1];
		strategies[0] = new MultiplicativeShavingStrategy();
		
		BNESolver bne = new BNESolver(market, bids, 2, 10, 100, strategies );
		bne.setPrecision( 1e-2 );
		bne.sortToBinsDA();
		
		bne.solveIt();

		time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());

		//assertTrue( (bne.getShavingFactor(0) < 0.21) && (bne.getShavingFactor(0) > 0.13));	//ideally 0.17
		//assertTrue( Math.abs(bne.getShavingFactor(1) - 0.0) < 1e-3);
		System.out.println(bne.toString());
	}*/
	
	
	//****************************************************************************
	
	/*
	@Test
	public void testDoubleAuction_Trim()
	{
		System.out.println("Launch the test");
		LocalTime time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
		
		int numberOfBuyers = 1;
		int numberOfSellers = 3;
		int numberOfAgents = numberOfBuyers + numberOfSellers;
		
		List<Integer> items = new LinkedList<Integer>();
		items.add(0);											//a query
		AtomicBid atomB1 = new AtomicBid(1, items, 0.6);
		atomB1.setTypeComponent("isSeller", 0.0);
		atomB1.setTypeComponent("Distribution", 1.0);
		atomB1.setTypeComponent("minValue", 0.0);
		atomB1.setTypeComponent("maxValue", 1.0);
		CombinatorialType b1 = new CombinatorialType(); 		//Buyer's type
		b1.addAtomicBid(atomB1);
		
		AtomicBid atomS1 = new AtomicBid(2, items, 0.1);
		atomS1.setTypeComponent("isSeller", 1.0);
		atomS1.setTypeComponent("Distribution", 1.0);
		atomS1.setTypeComponent("minValue", 0.0);
		atomS1.setTypeComponent("maxValue", 1.0);
		CombinatorialType s1 = new CombinatorialType(); 		//Seller's type
		s1.addAtomicBid(atomS1);
		
		AtomicBid atomS2 = new AtomicBid(3, items, 0.2);
		atomS2.setTypeComponent("isSeller", 1.0);
		atomS2.setTypeComponent("Distribution", 1.0);
		atomS2.setTypeComponent("minValue", 0.0);
		atomS2.setTypeComponent("maxValue", 1.0);
		CombinatorialType s2 = new CombinatorialType(); 		//Seller's type
		s2.addAtomicBid(atomS2);
		
		AtomicBid atomS3 = new AtomicBid(4, items, 0.3);
		atomS3.setTypeComponent("isSeller", 1.0);
		atomS3.setTypeComponent("Distribution", 1.0);
		atomS3.setTypeComponent("minValue", 0.0);
		atomS3.setTypeComponent("maxValue", 1.0);
		CombinatorialType s3 = new CombinatorialType(); 		//Seller's type
		s3.addAtomicBid(atomS3);
		
		List<Type> bids = new LinkedList<Type>();
		bids.add(b1);
		bids.add(s1);
		bids.add(s2);
		bids.add(s3);
		
		String paymentRule = "Threshold";
		String paymentCorrectionRule = "Penalty";
		
		IPlannerFactory plannerFactory = new SimplePlannerFactory(numberOfBuyers, numberOfSellers, bids, true);
		
		DoubleSidedMarket market = new DoubleSidedMarket(numberOfBuyers, numberOfSellers, bids, plannerFactory.producePlanner());
		market.setPaymentRule(paymentRule);
		market.setPaymentCorrectionRule(paymentCorrectionRule);
		
		BNESettings settings = new BNESettings();
		settings.setEpsilon(1e-2);
		settings.setNumberOfBins(2);
		settings.setNumberOfGridPoints(10);
		settings.setNumberOfSamples(500000);
		settings.setNumberOfThreads(10);
		
		ShavingStrategy[] strategies = new ShavingStrategy[1];
		strategies[0] = new MultiplicativeShavingStrategy();
		
		IUtilityComputationEngineFactory utilityComputationEngineFactory;
		utilityComputationEngineFactory = new MTUtilityComputationEngineFactory();
		
		IMechanismFactory dsmFactory = new DoubleSidedMarketFactory(numberOfBuyers, numberOfSellers, plannerFactory, paymentRule, paymentCorrectionRule);
		
		IBinSortingStrategy binSortingStrategy = new DoubleAuctionBinSortingStrategy();//AllDifferentBinSortingStrategy();
		
		System.out.println("Random number of tuples 0...10, random error injection, random tuples numbers");
		System.out.println("TRIM + " + "BNESolver(market, bids, "+settings.getNumberOfBins()+", "+settings.getNumberOfGridPoints()+", "+settings.getNumberOfSamples()+", strategies ), w=0.01");
		
		BNESolver bne = new BNESolver(market, bids, strategies, utilityComputationEngineFactory, dsmFactory, binSortingStrategy, settings);
		bne.setPrecision( 1e-2 );
		bne.sortToBins();
		bne.solveIt();

		time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
		System.out.println(bne.toString());
	}*/
	
	/*@Test
	public void testDoubleAuction_Trim10()
	{
		System.out.println("Launch the test");
		LocalTime time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
		
		int numberOfBuyers = 1;
		int numberOfSellers = 10;
		int numberOfAgents = numberOfBuyers + numberOfSellers;
		
		List<Integer> items = new LinkedList<Integer>();
		items.add(0);											//a query
		AtomicBid atomB1 = new AtomicBid(1, items, 0.6);
		atomB1.setTypeComponent("isSeller", 0.0);
		atomB1.setTypeComponent("Distribution", 1.0);
		atomB1.setTypeComponent("minValue", 0.0);
		atomB1.setTypeComponent("maxValue", 1.0);
		CombinatorialType b1 = new CombinatorialType(); 		//Buyer's type
		b1.addAtomicBid(atomB1);
		
		AtomicBid atomS1 = new AtomicBid(2, items, 0.1);
		atomS1.setTypeComponent("isSeller", 1.0);
		atomS1.setTypeComponent("Distribution", 1.0);
		atomS1.setTypeComponent("minValue", 0.0);
		atomS1.setTypeComponent("maxValue", 1.0);
		CombinatorialType s1 = new CombinatorialType(); 		//Seller's type
		s1.addAtomicBid(atomS1);
		
		AtomicBid atomS2 = new AtomicBid(3, items, 0.2);
		atomS2.setTypeComponent("isSeller", 1.0);
		atomS2.setTypeComponent("Distribution", 1.0);
		atomS2.setTypeComponent("minValue", 0.0);
		atomS2.setTypeComponent("maxValue", 1.0);
		CombinatorialType s2 = new CombinatorialType(); 		//Seller's type
		s2.addAtomicBid(atomS2);
		
		AtomicBid atomS3 = new AtomicBid(4, items, 0.3);
		atomS3.setTypeComponent("isSeller", 1.0);
		atomS3.setTypeComponent("Distribution", 1.0);
		atomS3.setTypeComponent("minValue", 0.0);
		atomS3.setTypeComponent("maxValue", 1.0);
		CombinatorialType s3 = new CombinatorialType(); 		//Seller's type
		s3.addAtomicBid(atomS3);
		
		AtomicBid atomS4 = new AtomicBid(5, items, 0.4);
		atomS4.setTypeComponent("isSeller", 1.0);
		atomS4.setTypeComponent("Distribution", 1.0);
		atomS4.setTypeComponent("minValue", 0.0);
		atomS4.setTypeComponent("maxValue", 1.0);
		CombinatorialType s4 = new CombinatorialType(); 		//Seller's type
		s4.addAtomicBid(atomS4);
		
		AtomicBid atomS5 = new AtomicBid(6, items, 0.5);
		atomS5.setTypeComponent("isSeller", 1.0);
		atomS5.setTypeComponent("Distribution", 1.0);
		atomS5.setTypeComponent("minValue", 0.0);
		atomS5.setTypeComponent("maxValue", 1.0);
		CombinatorialType s5 = new CombinatorialType(); 		//Seller's type
		s5.addAtomicBid(atomS5);
		
		AtomicBid atomS6 = new AtomicBid(7, items, 0.6);
		atomS6.setTypeComponent("isSeller", 1.0);
		atomS6.setTypeComponent("Distribution", 1.0);
		atomS6.setTypeComponent("minValue", 0.0);
		atomS6.setTypeComponent("maxValue", 1.0);
		CombinatorialType s6 = new CombinatorialType(); 		//Seller's type
		s6.addAtomicBid(atomS6);
		
		AtomicBid atomS7 = new AtomicBid(8, items, 0.7);
		atomS7.setTypeComponent("isSeller", 1.0);
		atomS7.setTypeComponent("Distribution", 1.0);
		atomS7.setTypeComponent("minValue", 0.0);
		atomS7.setTypeComponent("maxValue", 1.0);
		CombinatorialType s7 = new CombinatorialType(); 		//Seller's type
		s7.addAtomicBid(atomS7);
		
		AtomicBid atomS8 = new AtomicBid(9, items, 0.8);
		atomS8.setTypeComponent("isSeller", 1.0);
		atomS8.setTypeComponent("Distribution", 1.0);
		atomS8.setTypeComponent("minValue", 0.0);
		atomS8.setTypeComponent("maxValue", 1.0);
		CombinatorialType s8 = new CombinatorialType(); 		//Seller's type
		s8.addAtomicBid(atomS8);
		
		AtomicBid atomS9 = new AtomicBid(10, items, 0.9);
		atomS9.setTypeComponent("isSeller", 1.0);
		atomS9.setTypeComponent("Distribution", 1.0);
		atomS9.setTypeComponent("minValue", 0.0);
		atomS9.setTypeComponent("maxValue", 1.0);
		CombinatorialType s9 = new CombinatorialType(); 		//Seller's type
		s9.addAtomicBid(atomS9);
		
		AtomicBid atomS10 = new AtomicBid(11, items, 0.45);
		atomS10.setTypeComponent("isSeller", 1.0);
		atomS10.setTypeComponent("Distribution", 1.0);
		atomS10.setTypeComponent("minValue", 0.0);
		atomS10.setTypeComponent("maxValue", 1.0);
		CombinatorialType s10 = new CombinatorialType(); 		//Seller's type
		s10.addAtomicBid(atomS10);
		
		List<Type> bids = new LinkedList<Type>();
		bids.add(b1);
		bids.add(s1);
		bids.add(s2);
		bids.add(s3);
		bids.add(s4);
		bids.add(s5);
		bids.add(s6);
		bids.add(s7);
		bids.add(s8);
		bids.add(s9);
		bids.add(s10);
		
		Planner planner = new GeneralErrorPlanner(numberOfBuyers, numberOfSellers, bids);
		DoubleSidedMarket market = new DoubleSidedMarket(numberOfBuyers, numberOfSellers, bids, planner);
		market.setPaymentRule("Threshold");
		market.setPaymentCorrectionRule("Trim");
		
		ShavingStrategy[] strategies = new ShavingStrategy[1];
		strategies[0] = new MultiplicativeShavingStrategy();
		
		int numberOfBins = 2;
		int numberOfGridPoints = 7;
		int numberOfSamples = 500;
		System.out.println("Random number of tuples 0...10, random error injection, random tuples numbers, #Sellers=" + numberOfSellers);
		System.out.println("TRIM + " + "BNESolver(market, bids, "+numberOfBins+", "+numberOfGridPoints+", "+numberOfSamples+", strategies ), w=0.01");
		BNESolver bne = new BNESolver(market, bids, numberOfBins, numberOfGridPoints, numberOfSamples, strategies );
		bne.setPrecision( 1e-2 );
		if( numberOfBins == 2 )
			bne.sortToBinsDA();
		else
			bne.sortToBins4();
		
		bne.solveIt();

		time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
		System.out.println(bne.toString());
	}*/

/*	@Test
	public void testDoubleAuction_Trim10()
	{
		System.out.println("Launch the test");
		LocalTime time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
		
		int numberOfBuyers = 1;
		int numberOfSellers = 10;
		
		List<Integer> items = new LinkedList<Integer>();
		items.add(0);											//a query
		AtomicBid atomB1 = new AtomicBid(1, items, 0.6);
		atomB1.setTypeComponent("isSeller", 0.0);
		atomB1.setTypeComponent("Distribution", 1.0);
		atomB1.setTypeComponent("minValue", 0.0);
		atomB1.setTypeComponent("maxValue", 1.0);
		CombinatorialType b1 = new CombinatorialType(); 		//Buyer's type
		b1.addAtomicBid(atomB1);
		
		AtomicBid atomS1 = new AtomicBid(2, items, 0.1);
		atomS1.setTypeComponent("isSeller", 1.0);
		atomS1.setTypeComponent("Distribution", 1.0);
		atomS1.setTypeComponent("minValue", 0.0);
		atomS1.setTypeComponent("maxValue", 1.0);
		CombinatorialType s1 = new CombinatorialType(); 		//Seller's type
		s1.addAtomicBid(atomS1);
		
		AtomicBid atomS2 = new AtomicBid(3, items, 0.2);
		atomS2.setTypeComponent("isSeller", 1.0);
		atomS2.setTypeComponent("Distribution", 1.0);
		atomS2.setTypeComponent("minValue", 0.0);
		atomS2.setTypeComponent("maxValue", 1.0);
		CombinatorialType s2 = new CombinatorialType(); 		//Seller's type
		s2.addAtomicBid(atomS2);
		
		AtomicBid atomS3 = new AtomicBid(4, items, 0.3);
		atomS3.setTypeComponent("isSeller", 1.0);
		atomS3.setTypeComponent("Distribution", 1.0);
		atomS3.setTypeComponent("minValue", 0.0);
		atomS3.setTypeComponent("maxValue", 1.0);
		CombinatorialType s3 = new CombinatorialType(); 		//Seller's type
		s3.addAtomicBid(atomS3);
		
		AtomicBid atomS4 = new AtomicBid(5, items, 0.4);
		atomS4.setTypeComponent("isSeller", 1.0);
		atomS4.setTypeComponent("Distribution", 1.0);
		atomS4.setTypeComponent("minValue", 0.0);
		atomS4.setTypeComponent("maxValue", 1.0);
		CombinatorialType s4 = new CombinatorialType(); 		//Seller's type
		s4.addAtomicBid(atomS4);
		
		AtomicBid atomS5 = new AtomicBid(6, items, 0.5);
		atomS5.setTypeComponent("isSeller", 1.0);
		atomS5.setTypeComponent("Distribution", 1.0);
		atomS5.setTypeComponent("minValue", 0.0);
		atomS5.setTypeComponent("maxValue", 1.0);
		CombinatorialType s5 = new CombinatorialType(); 		//Seller's type
		s5.addAtomicBid(atomS5);
		
		AtomicBid atomS6 = new AtomicBid(7, items, 0.6);
		atomS6.setTypeComponent("isSeller", 1.0);
		atomS6.setTypeComponent("Distribution", 1.0);
		atomS6.setTypeComponent("minValue", 0.0);
		atomS6.setTypeComponent("maxValue", 1.0);
		CombinatorialType s6 = new CombinatorialType(); 		//Seller's type
		s6.addAtomicBid(atomS6);
		
		AtomicBid atomS7 = new AtomicBid(8, items, 0.7);
		atomS7.setTypeComponent("isSeller", 1.0);
		atomS7.setTypeComponent("Distribution", 1.0);
		atomS7.setTypeComponent("minValue", 0.0);
		atomS7.setTypeComponent("maxValue", 1.0);
		CombinatorialType s7 = new CombinatorialType(); 		//Seller's type
		s7.addAtomicBid(atomS7);
		
		AtomicBid atomS8 = new AtomicBid(9, items, 0.8);
		atomS8.setTypeComponent("isSeller", 1.0);
		atomS8.setTypeComponent("Distribution", 1.0);
		atomS8.setTypeComponent("minValue", 0.0);
		atomS8.setTypeComponent("maxValue", 1.0);
		CombinatorialType s8 = new CombinatorialType(); 		//Seller's type
		s8.addAtomicBid(atomS8);
		
		AtomicBid atomS9 = new AtomicBid(10, items, 0.9);
		atomS9.setTypeComponent("isSeller", 1.0);
		atomS9.setTypeComponent("Distribution", 1.0);
		atomS9.setTypeComponent("minValue", 0.0);
		atomS9.setTypeComponent("maxValue", 1.0);
		CombinatorialType s9 = new CombinatorialType(); 		//Seller's type
		s9.addAtomicBid(atomS9);
		
		AtomicBid atomS10 = new AtomicBid(11, items, 0.45);
		atomS10.setTypeComponent("isSeller", 1.0);
		atomS10.setTypeComponent("Distribution", 1.0);
		atomS10.setTypeComponent("minValue", 0.0);
		atomS10.setTypeComponent("maxValue", 1.0);
		CombinatorialType s10 = new CombinatorialType(); 		//Seller's type
		s10.addAtomicBid(atomS10);
		
		List<Type> bids = new LinkedList<Type>();
		bids.add(b1);
		bids.add(s1);
		bids.add(s2);
		bids.add(s3);
		bids.add(s4);
		bids.add(s5);
		bids.add(s6);
		bids.add(s7);
		bids.add(s8);
		bids.add(s9);
		bids.add(s10);
		
		int numberOfPlans = 5;
		int minNumberOfSellers = 2;
		int maxNumberOfSellers = 2;
		IPlannerFactory plannerFactory = new GeneralPlannerFactory(numberOfBuyers, numberOfSellers, bids, true, numberOfPlans, minNumberOfSellers, maxNumberOfSellers);
		
		DoubleSidedMarket market = new DoubleSidedMarket(numberOfBuyers, numberOfSellers, bids, plannerFactory.producePlanner());
		market.setPaymentRule("Threshold");
		market.setPaymentCorrectionRule("Trim");
		
		ShavingStrategy[] strategies = new ShavingStrategy[1];
		strategies[0] = new MultiplicativeShavingStrategy();
		
		BNESettings settings = new BNESettings();
		settings.setNumberOfBins(2);
		settings.setNumberOfGridPoints(7);
		settings.setNumberOfSamples(500);
		settings.setNumberOfThreads(10);
		int numberOfBins = 4;
		int numberOfGridPoints = 7;
		int numberOfSamples = 1000;
		System.out.println("Random number of tuples 0...10, random error injection, #Sellers=" + numberOfSellers);
		System.out.println("TRIM + " + "BNESolver(market, bids, "+numberOfBins+", "+numberOfGridPoints+", "+numberOfSamples+", strategies ), w=0.01");
		BNESolver bne = new BNESolver(market, bids, strategies, plannerFactory, settings);
		bne.setPrecision( 1e-2 );
		if( numberOfBins == 2 )
			bne.sortToBinsDA();
		else
			bne.sortToBins4();
		
		bne.solveIt();

		time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
		System.out.println(bne.toString());
	}
	*/
	/*@Test
	public void testDoubleAuction_VCG10()
	{
		System.out.println("Launch the test");
		LocalTime time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
		
		int numberOfBuyers = 1;
		int numberOfSellers = 10;
		int numberOfAgents = numberOfBuyers + numberOfSellers;
		
		List<Integer> items = new LinkedList<Integer>();
		items.add(0);											//a query
		AtomicBid atomB1 = new AtomicBid(1, items, 0.6);
		atomB1.setTypeComponent("isSeller", 0.0);
		atomB1.setTypeComponent("Distribution", 1.0);
		atomB1.setTypeComponent("minValue", 0.0);
		atomB1.setTypeComponent("maxValue", 1.0);
		CombinatorialType b1 = new CombinatorialType(); 		//Buyer's type
		b1.addAtomicBid(atomB1);
		
		AtomicBid atomS1 = new AtomicBid(2, items, 0.1);
		atomS1.setTypeComponent("isSeller", 1.0);
		atomS1.setTypeComponent("Distribution", 1.0);
		atomS1.setTypeComponent("minValue", 0.0);
		atomS1.setTypeComponent("maxValue", 1.0);
		CombinatorialType s1 = new CombinatorialType(); 		//Seller's type
		s1.addAtomicBid(atomS1);
		
		AtomicBid atomS2 = new AtomicBid(3, items, 0.2);
		atomS2.setTypeComponent("isSeller", 1.0);
		atomS2.setTypeComponent("Distribution", 1.0);
		atomS2.setTypeComponent("minValue", 0.0);
		atomS2.setTypeComponent("maxValue", 1.0);
		CombinatorialType s2 = new CombinatorialType(); 		//Seller's type
		s2.addAtomicBid(atomS2);
		
		AtomicBid atomS3 = new AtomicBid(4, items, 0.3);
		atomS3.setTypeComponent("isSeller", 1.0);
		atomS3.setTypeComponent("Distribution", 1.0);
		atomS3.setTypeComponent("minValue", 0.0);
		atomS3.setTypeComponent("maxValue", 1.0);
		CombinatorialType s3 = new CombinatorialType(); 		//Seller's type
		s3.addAtomicBid(atomS3);
		
		AtomicBid atomS4 = new AtomicBid(5, items, 0.4);
		atomS4.setTypeComponent("isSeller", 1.0);
		atomS4.setTypeComponent("Distribution", 1.0);
		atomS4.setTypeComponent("minValue", 0.0);
		atomS4.setTypeComponent("maxValue", 1.0);
		CombinatorialType s4 = new CombinatorialType(); 		//Seller's type
		s4.addAtomicBid(atomS4);
		
		AtomicBid atomS5 = new AtomicBid(6, items, 0.5);
		atomS5.setTypeComponent("isSeller", 1.0);
		atomS5.setTypeComponent("Distribution", 1.0);
		atomS5.setTypeComponent("minValue", 0.0);
		atomS5.setTypeComponent("maxValue", 1.0);
		CombinatorialType s5 = new CombinatorialType(); 		//Seller's type
		s5.addAtomicBid(atomS5);
		
		AtomicBid atomS6 = new AtomicBid(7, items, 0.6);
		atomS6.setTypeComponent("isSeller", 1.0);
		atomS6.setTypeComponent("Distribution", 1.0);
		atomS6.setTypeComponent("minValue", 0.0);
		atomS6.setTypeComponent("maxValue", 1.0);
		CombinatorialType s6 = new CombinatorialType(); 		//Seller's type
		s6.addAtomicBid(atomS6);
		
		AtomicBid atomS7 = new AtomicBid(8, items, 0.7);
		atomS7.setTypeComponent("isSeller", 1.0);
		atomS7.setTypeComponent("Distribution", 1.0);
		atomS7.setTypeComponent("minValue", 0.0);
		atomS7.setTypeComponent("maxValue", 1.0);
		CombinatorialType s7 = new CombinatorialType(); 		//Seller's type
		s7.addAtomicBid(atomS7);
		
		AtomicBid atomS8 = new AtomicBid(9, items, 0.8);
		atomS8.setTypeComponent("isSeller", 1.0);
		atomS8.setTypeComponent("Distribution", 1.0);
		atomS8.setTypeComponent("minValue", 0.0);
		atomS8.setTypeComponent("maxValue", 1.0);
		CombinatorialType s8 = new CombinatorialType(); 		//Seller's type
		s8.addAtomicBid(atomS8);
		
		AtomicBid atomS9 = new AtomicBid(10, items, 0.9);
		atomS9.setTypeComponent("isSeller", 1.0);
		atomS9.setTypeComponent("Distribution", 1.0);
		atomS9.setTypeComponent("minValue", 0.0);
		atomS9.setTypeComponent("maxValue", 1.0);
		CombinatorialType s9 = new CombinatorialType(); 		//Seller's type
		s9.addAtomicBid(atomS9);
		
		AtomicBid atomS10 = new AtomicBid(11, items, 0.45);
		atomS10.setTypeComponent("isSeller", 1.0);
		atomS10.setTypeComponent("Distribution", 1.0);
		atomS10.setTypeComponent("minValue", 0.0);
		atomS10.setTypeComponent("maxValue", 1.0);
		CombinatorialType s10 = new CombinatorialType(); 		//Seller's type
		s10.addAtomicBid(atomS10);
		
		List<Type> bids = new LinkedList<Type>();
		bids.add(b1);
		bids.add(s1);
		bids.add(s2);
		bids.add(s3);
		bids.add(s4);
		bids.add(s5);
		bids.add(s6);
		bids.add(s7);
		bids.add(s8);
		bids.add(s9);
		bids.add(s10);
		
		GeneralErrorPlanner planner = new GeneralErrorPlanner(numberOfBuyers, numberOfSellers, bids);
		Auction market = new DoubleSidedMarket(numberOfBuyers, numberOfSellers, bids, planner);
		
		ShavingStrategy[] strategies = new ShavingStrategy[1];
		strategies[0] = new MultiplicativeShavingStrategy();
		
		int numberOfBins = 2;
		int numberOfGridPoints = 7;
		int numberOfSamples = 500;
		System.out.println("Random number of tuples 0...10, random error injection, random tuples numbers. #Sellers= " + numberOfSellers);
		System.out.println("VCG + " + "BNESolver(market, bids, "+numberOfBins+", "+numberOfGridPoints+", "+numberOfSamples+", strategies ), w=0.01");
		BNESolver bne = new BNESolver(market, bids, numberOfBins, numberOfGridPoints, numberOfSamples, strategies );
		bne.setPrecision( 1e-2 );
		if( numberOfBins == 2 )
			bne.sortToBinsDA();
		else
			bne.sortToBins4();
		
		bne.solveIt();

		time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
		System.out.println(bne.toString());
	}
	*//*
	@Test
	public void testDoubleAuction_Penalty10()
	{
		System.out.println("Launch the test");
		LocalTime time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
		
		int numberOfBuyers = 1;
		int numberOfSellers = 10;
		int numberOfAgents = numberOfBuyers + numberOfSellers;
		
		List<Integer> items = new LinkedList<Integer>();
		items.add(0);											//a query
		AtomicBid atomB1 = new AtomicBid(1, items, 0.6);
		atomB1.setTypeComponent("isSeller", 0.0);
		atomB1.setTypeComponent("Distribution", 1.0);
		atomB1.setTypeComponent("minValue", 0.0);
		atomB1.setTypeComponent("maxValue", 1.0);
		CombinatorialType b1 = new CombinatorialType(); 		//Buyer's type
		b1.addAtomicBid(atomB1);
		
		AtomicBid atomS1 = new AtomicBid(2, items, 0.1);
		atomS1.setTypeComponent("isSeller", 1.0);
		atomS1.setTypeComponent("Distribution", 1.0);
		atomS1.setTypeComponent("minValue", 0.0);
		atomS1.setTypeComponent("maxValue", 1.0);
		CombinatorialType s1 = new CombinatorialType(); 		//Seller's type
		s1.addAtomicBid(atomS1);
		
		AtomicBid atomS2 = new AtomicBid(3, items, 0.2);
		atomS2.setTypeComponent("isSeller", 1.0);
		atomS2.setTypeComponent("Distribution", 1.0);
		atomS2.setTypeComponent("minValue", 0.0);
		atomS2.setTypeComponent("maxValue", 1.0);
		CombinatorialType s2 = new CombinatorialType(); 		//Seller's type
		s2.addAtomicBid(atomS2);
		
		AtomicBid atomS3 = new AtomicBid(4, items, 0.3);
		atomS3.setTypeComponent("isSeller", 1.0);
		atomS3.setTypeComponent("Distribution", 1.0);
		atomS3.setTypeComponent("minValue", 0.0);
		atomS3.setTypeComponent("maxValue", 1.0);
		CombinatorialType s3 = new CombinatorialType(); 		//Seller's type
		s3.addAtomicBid(atomS3);
		
		AtomicBid atomS4 = new AtomicBid(5, items, 0.4);
		atomS4.setTypeComponent("isSeller", 1.0);
		atomS4.setTypeComponent("Distribution", 1.0);
		atomS4.setTypeComponent("minValue", 0.0);
		atomS4.setTypeComponent("maxValue", 1.0);
		CombinatorialType s4 = new CombinatorialType(); 		//Seller's type
		s4.addAtomicBid(atomS4);
		
		AtomicBid atomS5 = new AtomicBid(6, items, 0.5);
		atomS5.setTypeComponent("isSeller", 1.0);
		atomS5.setTypeComponent("Distribution", 1.0);
		atomS5.setTypeComponent("minValue", 0.0);
		atomS5.setTypeComponent("maxValue", 1.0);
		CombinatorialType s5 = new CombinatorialType(); 		//Seller's type
		s5.addAtomicBid(atomS5);
		
		AtomicBid atomS6 = new AtomicBid(7, items, 0.6);
		atomS6.setTypeComponent("isSeller", 1.0);
		atomS6.setTypeComponent("Distribution", 1.0);
		atomS6.setTypeComponent("minValue", 0.0);
		atomS6.setTypeComponent("maxValue", 1.0);
		CombinatorialType s6 = new CombinatorialType(); 		//Seller's type
		s6.addAtomicBid(atomS6);
		
		AtomicBid atomS7 = new AtomicBid(8, items, 0.7);
		atomS7.setTypeComponent("isSeller", 1.0);
		atomS7.setTypeComponent("Distribution", 1.0);
		atomS7.setTypeComponent("minValue", 0.0);
		atomS7.setTypeComponent("maxValue", 1.0);
		CombinatorialType s7 = new CombinatorialType(); 		//Seller's type
		s7.addAtomicBid(atomS7);
		
		AtomicBid atomS8 = new AtomicBid(9, items, 0.8);
		atomS8.setTypeComponent("isSeller", 1.0);
		atomS8.setTypeComponent("Distribution", 1.0);
		atomS8.setTypeComponent("minValue", 0.0);
		atomS8.setTypeComponent("maxValue", 1.0);
		CombinatorialType s8 = new CombinatorialType(); 		//Seller's type
		s8.addAtomicBid(atomS8);
		
		AtomicBid atomS9 = new AtomicBid(10, items, 0.9);
		atomS9.setTypeComponent("isSeller", 1.0);
		atomS9.setTypeComponent("Distribution", 1.0);
		atomS9.setTypeComponent("minValue", 0.0);
		atomS9.setTypeComponent("maxValue", 1.0);
		CombinatorialType s9 = new CombinatorialType(); 		//Seller's type
		s9.addAtomicBid(atomS9);
		
		AtomicBid atomS10 = new AtomicBid(11, items, 0.45);
		atomS10.setTypeComponent("isSeller", 1.0);
		atomS10.setTypeComponent("Distribution", 1.0);
		atomS10.setTypeComponent("minValue", 0.0);
		atomS10.setTypeComponent("maxValue", 1.0);
		CombinatorialType s10 = new CombinatorialType(); 		//Seller's type
		s10.addAtomicBid(atomS10);
		
		List<Type> bids = new LinkedList<Type>();
		bids.add(b1);
		bids.add(s1);
		bids.add(s2);
		bids.add(s3);
		bids.add(s4);
		bids.add(s5);
		bids.add(s6);
		bids.add(s7);
		bids.add(s8);
		bids.add(s9);
		bids.add(s10);
		
		GeneralErrorPlanner planner = new GeneralErrorPlanner(numberOfBuyers, numberOfSellers, bids);
		Auction market = new DoubleSidedMarket(numberOfBuyers, numberOfSellers, bids, planner);
		
		ShavingStrategy[] strategies = new ShavingStrategy[1];
		strategies[0] = new MultiplicativeShavingStrategy();
		
		int numberOfBins = 2;
		int numberOfGridPoints = 7;
		int numberOfSamples = 500;
		System.out.println("Random number of tuples 0...10, random error injection, random tuples numbers. #Sellers= " + numberOfSellers);
		System.out.println("Penalty + " + "BNESolver(market, bids, "+numberOfBins+", "+numberOfGridPoints+", "+numberOfSamples+", strategies ), w=0.01");
		BNESolver bne = new BNESolver(market, bids, numberOfBins, numberOfGridPoints, numberOfSamples, strategies );
		bne.setPrecision( 1e-2 );
		if( numberOfBins == 2 )
			bne.sortToBinsDA();
		else
			bne.sortToBins4();
		
		bne.solveIt();

		time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
		System.out.println(bne.toString());
	}
	*/
	/*
	@Test
	public void testDoubleAuction_VCG_IC10()
	{
		System.out.println("Launch the test");
		LocalTime time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
		
		int numberOfBuyers = 1;
		int numberOfSellers = 10;
		int numberOfAgents = numberOfBuyers + numberOfSellers;
		
		List<Integer> items = new LinkedList<Integer>();
		items.add(0);											//a query
		AtomicBid atomB1 = new AtomicBid(1, items, 0.6);
		atomB1.setTypeComponent("isSeller", 0.0);
		atomB1.setTypeComponent("Distribution", 1.0);
		atomB1.setTypeComponent("minValue", 0.0);
		atomB1.setTypeComponent("maxValue", 1.0);
		CombinatorialType b1 = new CombinatorialType(); 		//Buyer's type
		b1.addAtomicBid(atomB1);
		
		AtomicBid atomS1 = new AtomicBid(2, items, 0.1);
		atomS1.setTypeComponent("isSeller", 1.0);
		atomS1.setTypeComponent("Distribution", 1.0);
		atomS1.setTypeComponent("minValue", 0.0);
		atomS1.setTypeComponent("maxValue", 1.0);
		CombinatorialType s1 = new CombinatorialType(); 		//Seller's type
		s1.addAtomicBid(atomS1);
		
		AtomicBid atomS2 = new AtomicBid(3, items, 0.2);
		atomS2.setTypeComponent("isSeller", 1.0);
		atomS2.setTypeComponent("Distribution", 1.0);
		atomS2.setTypeComponent("minValue", 0.0);
		atomS2.setTypeComponent("maxValue", 1.0);
		CombinatorialType s2 = new CombinatorialType(); 		//Seller's type
		s2.addAtomicBid(atomS2);
		
		AtomicBid atomS3 = new AtomicBid(4, items, 0.3);
		atomS3.setTypeComponent("isSeller", 1.0);
		atomS3.setTypeComponent("Distribution", 1.0);
		atomS3.setTypeComponent("minValue", 0.0);
		atomS3.setTypeComponent("maxValue", 1.0);
		CombinatorialType s3 = new CombinatorialType(); 		//Seller's type
		s3.addAtomicBid(atomS3);
		
		AtomicBid atomS4 = new AtomicBid(5, items, 0.4);
		atomS4.setTypeComponent("isSeller", 1.0);
		atomS4.setTypeComponent("Distribution", 1.0);
		atomS4.setTypeComponent("minValue", 0.0);
		atomS4.setTypeComponent("maxValue", 1.0);
		CombinatorialType s4 = new CombinatorialType(); 		//Seller's type
		s4.addAtomicBid(atomS4);
		
		AtomicBid atomS5 = new AtomicBid(6, items, 0.5);
		atomS5.setTypeComponent("isSeller", 1.0);
		atomS5.setTypeComponent("Distribution", 1.0);
		atomS5.setTypeComponent("minValue", 0.0);
		atomS5.setTypeComponent("maxValue", 1.0);
		CombinatorialType s5 = new CombinatorialType(); 		//Seller's type
		s5.addAtomicBid(atomS5);
		
		AtomicBid atomS6 = new AtomicBid(7, items, 0.6);
		atomS6.setTypeComponent("isSeller", 1.0);
		atomS6.setTypeComponent("Distribution", 1.0);
		atomS6.setTypeComponent("minValue", 0.0);
		atomS6.setTypeComponent("maxValue", 1.0);
		CombinatorialType s6 = new CombinatorialType(); 		//Seller's type
		s6.addAtomicBid(atomS6);
		
		AtomicBid atomS7 = new AtomicBid(8, items, 0.7);
		atomS7.setTypeComponent("isSeller", 1.0);
		atomS7.setTypeComponent("Distribution", 1.0);
		atomS7.setTypeComponent("minValue", 0.0);
		atomS7.setTypeComponent("maxValue", 1.0);
		CombinatorialType s7 = new CombinatorialType(); 		//Seller's type
		s7.addAtomicBid(atomS7);
		
		AtomicBid atomS8 = new AtomicBid(9, items, 0.8);
		atomS8.setTypeComponent("isSeller", 1.0);
		atomS8.setTypeComponent("Distribution", 1.0);
		atomS8.setTypeComponent("minValue", 0.0);
		atomS8.setTypeComponent("maxValue", 1.0);
		CombinatorialType s8 = new CombinatorialType(); 		//Seller's type
		s8.addAtomicBid(atomS8);
		
		AtomicBid atomS9 = new AtomicBid(10, items, 0.9);
		atomS9.setTypeComponent("isSeller", 1.0);
		atomS9.setTypeComponent("Distribution", 1.0);
		atomS9.setTypeComponent("minValue", 0.0);
		atomS9.setTypeComponent("maxValue", 1.0);
		CombinatorialType s9 = new CombinatorialType(); 		//Seller's type
		s9.addAtomicBid(atomS9);
		
		AtomicBid atomS10 = new AtomicBid(11, items, 0.45);
		atomS10.setTypeComponent("isSeller", 1.0);
		atomS10.setTypeComponent("Distribution", 1.0);
		atomS10.setTypeComponent("minValue", 0.0);
		atomS10.setTypeComponent("maxValue", 1.0);
		CombinatorialType s10 = new CombinatorialType(); 		//Seller's type
		s10.addAtomicBid(atomS10);
		
		List<Type> bids = new LinkedList<Type>();
		bids.add(b1);
		bids.add(s1);
		bids.add(s2);
		bids.add(s3);
		bids.add(s4);
		bids.add(s5);
		bids.add(s6);
		bids.add(s7);
		bids.add(s8);
		bids.add(s9);
		bids.add(s10);
		
		Planner planner = new GeneralErrorPlanner(numberOfBuyers, numberOfSellers, bids);
		Auction market = new DoubleSidedMarket(numberOfBuyers, numberOfSellers, bids, planner);
		
		ShavingStrategy[] strategies = new ShavingStrategy[1];
		strategies[0] = new MultiplicativeShavingStrategy();
		
		int numberOfBins = 2;
		int numberOfGridPoints = 7;
		int numberOfSamples = 500;
		System.out.println("Random number of tuples 0...10, random error injection, random tuples numbers, #Sellers=" + numberOfSellers);
		System.out.println("VCG_IC + " + "BNESolver(market, bids, "+numberOfBins+", "+numberOfGridPoints+", "+numberOfSamples+", strategies ), w=0.01");
		BNESolver bne = new BNESolver(market, bids, numberOfBins, numberOfGridPoints, numberOfSamples, strategies );
		bne.setPrecision( 1e-2 );
		if( numberOfBins == 2 )
			bne.sortToBinsDA();
		else
			bne.sortToBins4();
		
		bne.solveIt();

		time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
		System.out.println(bne.toString());
	}*/
	/*
	
	@Test
	public void testDoubleAuction_DA10()
	{
		System.out.println("Launch the test");
		LocalTime time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
		
		int numberOfBuyers = 1;
		int numberOfSellers = 10;
		int numberOfAgents = numberOfBuyers + numberOfSellers;
		
		List<Integer> items = new LinkedList<Integer>();
		items.add(0);											//a query
		AtomicBid atomB1 = new AtomicBid(1, items, 0.6);
		atomB1.setTypeComponent("isSeller", 0.0);
		atomB1.setTypeComponent("Distribution", 1.0);
		atomB1.setTypeComponent("minValue", 0.0);
		atomB1.setTypeComponent("maxValue", 1.0);
		CombinatorialType b1 = new CombinatorialType(); 		//Buyer's type
		b1.addAtomicBid(atomB1);
		
		AtomicBid atomS1 = new AtomicBid(2, items, 0.1);
		atomS1.setTypeComponent("isSeller", 1.0);
		atomS1.setTypeComponent("Distribution", 1.0);
		atomS1.setTypeComponent("minValue", 0.0);
		atomS1.setTypeComponent("maxValue", 1.0);
		CombinatorialType s1 = new CombinatorialType(); 		//Seller's type
		s1.addAtomicBid(atomS1);
		
		AtomicBid atomS2 = new AtomicBid(3, items, 0.2);
		atomS2.setTypeComponent("isSeller", 1.0);
		atomS2.setTypeComponent("Distribution", 1.0);
		atomS2.setTypeComponent("minValue", 0.0);
		atomS2.setTypeComponent("maxValue", 1.0);
		CombinatorialType s2 = new CombinatorialType(); 		//Seller's type
		s2.addAtomicBid(atomS2);
		
		AtomicBid atomS3 = new AtomicBid(4, items, 0.3);
		atomS3.setTypeComponent("isSeller", 1.0);
		atomS3.setTypeComponent("Distribution", 1.0);
		atomS3.setTypeComponent("minValue", 0.0);
		atomS3.setTypeComponent("maxValue", 1.0);
		CombinatorialType s3 = new CombinatorialType(); 		//Seller's type
		s3.addAtomicBid(atomS3);
		
		AtomicBid atomS4 = new AtomicBid(5, items, 0.4);
		atomS4.setTypeComponent("isSeller", 1.0);
		atomS4.setTypeComponent("Distribution", 1.0);
		atomS4.setTypeComponent("minValue", 0.0);
		atomS4.setTypeComponent("maxValue", 1.0);
		CombinatorialType s4 = new CombinatorialType(); 		//Seller's type
		s4.addAtomicBid(atomS4);
		
		AtomicBid atomS5 = new AtomicBid(6, items, 0.5);
		atomS5.setTypeComponent("isSeller", 1.0);
		atomS5.setTypeComponent("Distribution", 1.0);
		atomS5.setTypeComponent("minValue", 0.0);
		atomS5.setTypeComponent("maxValue", 1.0);
		CombinatorialType s5 = new CombinatorialType(); 		//Seller's type
		s5.addAtomicBid(atomS5);
		
		AtomicBid atomS6 = new AtomicBid(7, items, 0.6);
		atomS6.setTypeComponent("isSeller", 1.0);
		atomS6.setTypeComponent("Distribution", 1.0);
		atomS6.setTypeComponent("minValue", 0.0);
		atomS6.setTypeComponent("maxValue", 1.0);
		CombinatorialType s6 = new CombinatorialType(); 		//Seller's type
		s6.addAtomicBid(atomS6);
		
		AtomicBid atomS7 = new AtomicBid(8, items, 0.7);
		atomS7.setTypeComponent("isSeller", 1.0);
		atomS7.setTypeComponent("Distribution", 1.0);
		atomS7.setTypeComponent("minValue", 0.0);
		atomS7.setTypeComponent("maxValue", 1.0);
		CombinatorialType s7 = new CombinatorialType(); 		//Seller's type
		s7.addAtomicBid(atomS7);
		
		AtomicBid atomS8 = new AtomicBid(9, items, 0.8);
		atomS8.setTypeComponent("isSeller", 1.0);
		atomS8.setTypeComponent("Distribution", 1.0);
		atomS8.setTypeComponent("minValue", 0.0);
		atomS8.setTypeComponent("maxValue", 1.0);
		CombinatorialType s8 = new CombinatorialType(); 		//Seller's type
		s8.addAtomicBid(atomS8);
		
		AtomicBid atomS9 = new AtomicBid(10, items, 0.9);
		atomS9.setTypeComponent("isSeller", 1.0);
		atomS9.setTypeComponent("Distribution", 1.0);
		atomS9.setTypeComponent("minValue", 0.0);
		atomS9.setTypeComponent("maxValue", 1.0);
		CombinatorialType s9 = new CombinatorialType(); 		//Seller's type
		s9.addAtomicBid(atomS9);
		
		AtomicBid atomS10 = new AtomicBid(11, items, 0.45);
		atomS10.setTypeComponent("isSeller", 1.0);
		atomS10.setTypeComponent("Distribution", 1.0);
		atomS10.setTypeComponent("minValue", 0.0);
		atomS10.setTypeComponent("maxValue", 1.0);
		CombinatorialType s10 = new CombinatorialType(); 		//Seller's type
		s10.addAtomicBid(atomS10);
		
		List<Type> bids = new LinkedList<Type>();
		bids.add(b1);
		bids.add(s1);
		bids.add(s2);
		bids.add(s3);
		bids.add(s4);
		bids.add(s5);
		bids.add(s6);
		bids.add(s7);
		bids.add(s8);
		bids.add(s9);
		bids.add(s10);
		
		Planner planner = new GeneralPlanner(numberOfBuyers, numberOfSellers, bids);
		Auction market = new DoubleSidedMarket(numberOfBuyers, numberOfSellers, bids, planner);
		
		ShavingStrategy[] strategies = new ShavingStrategy[1];
		strategies[0] = new MultiplicativeShavingStrategy();
		
		int numberOfBins = 2;
		int numberOfGridPoints = 7;
		int numberOfSamples = 1000;
		System.out.println("Random number of tuples 0...10, random error injection, random tuples numbers, #Sellers=" + numberOfSellers);
		System.out.println("DA + " + "BNESolver(market, bids, "+numberOfBins+", "+numberOfGridPoints+", "+numberOfSamples+", strategies ), w=0.01");
		BNESolver bne = new BNESolver(market, bids, numberOfBins, numberOfGridPoints, numberOfSamples, strategies );
		bne.setPrecision( 1e-2 );
		if( numberOfBins == 2 )
			bne.sortToBinsDA();
		else
			bne.sortToBins4();
		
		bne.solveIt();

		time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
		System.out.println(bne.toString());
	}
	*/
	
/*	@Test
	public void testDoubleAuction_VCG()
	{
		System.out.println("Launch the test");
		LocalTime time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
		
		int numberOfBuyers = 1;
		int numberOfSellers = 3;
		int numberOfAgents = numberOfBuyers + numberOfSellers;
		
		List<Integer> items = new LinkedList<Integer>();
		items.add(0);											//a query
		AtomicBid atomB1 = new AtomicBid(1, items, 0.6);
		atomB1.setTypeComponent("isSeller", 0.0);
		atomB1.setTypeComponent("Distribution", 1.0);
		atomB1.setTypeComponent("minValue", 0.0);
		atomB1.setTypeComponent("maxValue", 1.0);
		CombinatorialType b1 = new CombinatorialType(); 		//Buyer's type
		b1.addAtomicBid(atomB1);
		
		AtomicBid atomS1 = new AtomicBid(2, items, 0.1);
		atomS1.setTypeComponent("isSeller", 1.0);
		atomS1.setTypeComponent("Distribution", 1.0);
		atomS1.setTypeComponent("minValue", 0.0);
		atomS1.setTypeComponent("maxValue", 1.0);
		CombinatorialType s1 = new CombinatorialType(); 		//Seller's type
		s1.addAtomicBid(atomS1);
		
		AtomicBid atomS2 = new AtomicBid(3, items, 0.2);
		atomS2.setTypeComponent("isSeller", 1.0);
		atomS2.setTypeComponent("Distribution", 1.0);
		atomS2.setTypeComponent("minValue", 0.0);
		atomS2.setTypeComponent("maxValue", 1.0);
		CombinatorialType s2 = new CombinatorialType(); 		//Seller's type
		s2.addAtomicBid(atomS2);
		
		AtomicBid atomS3 = new AtomicBid(4, items, 0.3);
		atomS3.setTypeComponent("isSeller", 1.0);
		atomS3.setTypeComponent("Distribution", 1.0);
		atomS3.setTypeComponent("minValue", 0.0);
		atomS3.setTypeComponent("maxValue", 1.0);
		CombinatorialType s3 = new CombinatorialType(); 		//Seller's type
		s3.addAtomicBid(atomS3);
		
		List<Type> bids = new LinkedList<Type>();
		bids.add(b1);
		bids.add(s1);
		bids.add(s2);
		bids.add(s3);
		
		Planner planner = new SimpleErrorPlanner();
		Auction market = new DoubleSidedMarket(numberOfBuyers, numberOfSellers, bids, planner);
		
		ShavingStrategy[] strategies = new ShavingStrategy[1];
		strategies[0] = new MultiplicativeShavingStrategy();
		
		int numberOfBins = 2;
		int numberOfGridPoints = 10;
		int numberOfSamples = 1000;
		System.out.println("Random number of tuples 0...10, random error injection, random tuples numbers");
		System.out.println("PC_VCG + " + "BNESolver(market, bids, "+numberOfBins+", "+numberOfGridPoints+", "+numberOfSamples+", strategies ), w=0.01");
		BNESolver bne = new BNESolver(market, bids, numberOfBins, numberOfGridPoints, numberOfSamples, strategies );
		bne.setPrecision( 1e-2 );
		if( numberOfBins == 2 )
			bne.sortToBinsDA();
		else
			bne.sortToBins4();
		
		bne.solveIt();

		time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());

		//assertTrue( (bne.getShavingFactor(0) < 0.21) && (bne.getShavingFactor(0) > 0.13));	//ideally 0.17
		//assertTrue( Math.abs(bne.getShavingFactor(1) - 0.0) < 1e-3);
		System.out.println(bne.toString());
	}*/
	
/*	@Test
	public void testDoubleAuction_Penalty()
	{
		System.out.println("Launch the test");
		LocalTime time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
		
		int numberOfBuyers = 1;
		int numberOfSellers = 3;
		int numberOfAgents = numberOfBuyers + numberOfSellers;
		
		List<Integer> items = new LinkedList<Integer>();
		items.add(0);											//a query
		AtomicBid atomB1 = new AtomicBid(1, items, 0.6);
		atomB1.setTypeComponent("isSeller", 0.0);
		atomB1.setTypeComponent("Distribution", 1.0);
		atomB1.setTypeComponent("minValue", 0.0);
		atomB1.setTypeComponent("maxValue", 1.0);
		CombinatorialType b1 = new CombinatorialType(); 		//Buyer's type
		b1.addAtomicBid(atomB1);
		
		AtomicBid atomS1 = new AtomicBid(2, items, 0.1);
		atomS1.setTypeComponent("isSeller", 1.0);
		atomS1.setTypeComponent("Distribution", 1.0);
		atomS1.setTypeComponent("minValue", 0.0);
		atomS1.setTypeComponent("maxValue", 1.0);
		CombinatorialType s1 = new CombinatorialType(); 		//Seller's type
		s1.addAtomicBid(atomS1);
		
		AtomicBid atomS2 = new AtomicBid(3, items, 0.2);
		atomS2.setTypeComponent("isSeller", 1.0);
		atomS2.setTypeComponent("Distribution", 1.0);
		atomS2.setTypeComponent("minValue", 0.0);
		atomS2.setTypeComponent("maxValue", 1.0);
		CombinatorialType s2 = new CombinatorialType(); 		//Seller's type
		s2.addAtomicBid(atomS2);
		
		AtomicBid atomS3 = new AtomicBid(4, items, 0.3);
		atomS3.setTypeComponent("isSeller", 1.0);
		atomS3.setTypeComponent("Distribution", 1.0);
		atomS3.setTypeComponent("minValue", 0.0);
		atomS3.setTypeComponent("maxValue", 1.0);
		CombinatorialType s3 = new CombinatorialType(); 		//Seller's type
		s3.addAtomicBid(atomS3);
		
		List<Type> bids = new LinkedList<Type>();
		bids.add(b1);
		bids.add(s1);
		bids.add(s2);
		bids.add(s3);
		
		Planner planner = new SimpleErrorPlanner();
		Auction market = new DoubleSidedMarket(numberOfBuyers, numberOfSellers, bids, planner);
		
		ShavingStrategy[] strategies = new ShavingStrategy[1];
		strategies[0] = new MultiplicativeShavingStrategy();
		
		int numberOfBins = 2;
		int numberOfGridPoints = 7;
		int numberOfSamples = 10000;
		System.out.println("Random number of tuples 0...10, random error injection, random tuples numbers");
		System.out.println("Penalty + " + "BNESolver(market, bids, "+numberOfBins+", "+numberOfGridPoints+", "+numberOfSamples+", strategies ), w=0.01");
		BNESolver bne = new BNESolver(market, bids, numberOfBins, numberOfGridPoints, numberOfSamples, strategies );
		bne.setPrecision( 1e-2 );
		
		if( numberOfBins == 2)
			bne.sortToBinsDA();
		else
			bne.sortToBins4();
		
		bne.solveIt();

		time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());

		//assertTrue( (bne.getShavingFactor(0) < 0.21) && (bne.getShavingFactor(0) > 0.13));	//ideally 0.17
		//assertTrue( Math.abs(bne.getShavingFactor(1) - 0.0) < 1e-3);
		System.out.println(bne.toString());
	}*/
	
/*	@Test
	public void testDoubleAuction_VCG_NO_CORRECTION()
	{
		System.out.println("Launch the test");
		LocalTime time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
		
		int numberOfBuyers = 1;
		int numberOfSellers = 3;
		int numberOfAgents = numberOfBuyers + numberOfSellers;
		
		List<Integer> items = new LinkedList<Integer>();
		items.add(0);											//a query
		AtomicBid atomB1 = new AtomicBid(1, items, 0.6);
		atomB1.setTypeComponent("isSeller", 0.0);
		atomB1.setTypeComponent("Distribution", 1.0);
		atomB1.setTypeComponent("minValue", 0.0);
		atomB1.setTypeComponent("maxValue", 1.0);
		CombinatorialType b1 = new CombinatorialType(); 		//Buyer's type
		b1.addAtomicBid(atomB1);
		
		AtomicBid atomS1 = new AtomicBid(2, items, 0.1);
		atomS1.setTypeComponent("isSeller", 1.0);
		atomS1.setTypeComponent("Distribution", 1.0);
		atomS1.setTypeComponent("minValue", 0.0);
		atomS1.setTypeComponent("maxValue", 1.0);
		CombinatorialType s1 = new CombinatorialType(); 		//Seller's type
		s1.addAtomicBid(atomS1);
		
		AtomicBid atomS2 = new AtomicBid(3, items, 0.2);
		atomS2.setTypeComponent("isSeller", 1.0);
		atomS2.setTypeComponent("Distribution", 1.0);
		atomS2.setTypeComponent("minValue", 0.0);
		atomS2.setTypeComponent("maxValue", 1.0);
		CombinatorialType s2 = new CombinatorialType(); 		//Seller's type
		s2.addAtomicBid(atomS2);
		
		AtomicBid atomS3 = new AtomicBid(4, items, 0.3);
		atomS3.setTypeComponent("isSeller", 1.0);
		atomS3.setTypeComponent("Distribution", 1.0);
		atomS3.setTypeComponent("minValue", 0.0);
		atomS3.setTypeComponent("maxValue", 1.0);
		CombinatorialType s3 = new CombinatorialType(); 		//Seller's type
		s3.addAtomicBid(atomS3);
		
		List<Type> bids = new LinkedList<Type>();
		bids.add(b1);
		bids.add(s1);
		bids.add(s2);
		bids.add(s3);
		
		Planner planner = new SimpleErrorPlanner();
		DoubleSidedMarket market = new DoubleSidedMarket(numberOfBuyers, numberOfSellers, bids, planner);
		market.setPaymentRule("VCG");
		
		ShavingStrategy[] strategies = new ShavingStrategy[1];
		strategies[0] = new MultiplicativeShavingStrategy();
		
		int numberOfBins = 2;
		int numberOfGridPoints = 7;
		int numberOfSamples = 5000;
		System.out.println("Random number of tuples 0...5, random error injection, random tuples numbers");
		System.out.println("VCG_NO_CORRECTION + " + "BNESolver(market, bids, "+numberOfBins+", "+numberOfGridPoints+", "+numberOfSamples+", strategies ), w=0.01");
		BNESolver bne = new BNESolver(market, bids, numberOfBins, numberOfGridPoints, numberOfSamples, strategies );
		bne.setPrecision( 1e-2 );
		
		if( numberOfBins == 2)
			bne.sortToBinsDA();
		else
			bne.sortToBins4();
		
		bne.solveIt();

		time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());

		//assertTrue( (bne.getShavingFactor(0) < 0.21) && (bne.getShavingFactor(0) > 0.13));	//ideally 0.17
		//assertTrue( Math.abs(bne.getShavingFactor(1) - 0.0) < 1e-3);
		System.out.println(bne.toString());
	}*/
	
	/*
	@Test
	public void testDoubleAuction_Benchmarks() throws Exception
	{
		System.out.println("Launch the test");
		LocalTime time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
		
		int numberOfBuyers = 1;
		int numberOfSellers = 10;

		int numberOfSamples = 100000;
		double buyersShade = 1.0;
		double sellersShade = 1.0;
		
		double utility = 0.;
		double budgetAmount = 0.;
		int budgetCounter = 0;
		double profitsOfBuyer = 0.;
		double profitsOfSeller = 0.;
		Random generator = new Random();
		
		for(int i = 0; i < numberOfSamples; ++i)
		{
			List<Integer> items = new LinkedList<Integer>();
			items.add(0);											//a query
			
			double buyersTrueValue = generator.nextDouble();
			AtomicBid atomB1 = new AtomicBid(1, items, buyersShade*buyersTrueValue);
			atomB1.setTypeComponent("isSeller", 0.0);
			CombinatorialType b1 = new CombinatorialType(); 		//Buyer's type
			b1.addAtomicBid(atomB1);
			
			double sellerATrueValue = generator.nextDouble();
			AtomicBid atomS1 = new AtomicBid(2, items, sellersShade*sellerATrueValue);
			atomS1.setTypeComponent("isSeller", 1.0);
			CombinatorialType s1 = new CombinatorialType(); 		//Seller's type
			s1.addAtomicBid(atomS1);
			
			double sellerBTrueValue = generator.nextDouble();
			AtomicBid atomS2 = new AtomicBid(3, items, sellersShade*sellerBTrueValue);
			atomS2.setTypeComponent("isSeller", 1.0);
			CombinatorialType s2 = new CombinatorialType(); 		//Seller's type
			s2.addAtomicBid(atomS2);
			
			double sellerCTrueValue = generator.nextDouble();
			AtomicBid atomS3 = new AtomicBid(4, items, sellersShade*sellerCTrueValue);
			atomS3.setTypeComponent("isSeller", 1.0);
			CombinatorialType s3 = new CombinatorialType(); 		//Seller's type
			s3.addAtomicBid(atomS3);
			
			double seller4TrueValue = generator.nextDouble();
			AtomicBid atomS4 = new AtomicBid(5, items, sellersShade*seller4TrueValue);
			atomS4.setTypeComponent("isSeller", 1.0);
			CombinatorialType s4 = new CombinatorialType(); 		//Seller's type
			s4.addAtomicBid(atomS4);
			
			double seller5TrueValue = generator.nextDouble();
			AtomicBid atomS5 = new AtomicBid(6, items, sellersShade*seller5TrueValue);
			atomS5.setTypeComponent("isSeller", 1.0);
			CombinatorialType s5 = new CombinatorialType(); 		//Seller's type
			s5.addAtomicBid(atomS5);
			
			double seller6TrueValue = generator.nextDouble();
			AtomicBid atomS6 = new AtomicBid(7, items, sellersShade*seller6TrueValue);
			atomS6.setTypeComponent("isSeller", 1.0);
			CombinatorialType s6 = new CombinatorialType(); 		//Seller's type
			s6.addAtomicBid(atomS6);
			
			double seller7TrueValue = generator.nextDouble();
			AtomicBid atomS7 = new AtomicBid(8, items, sellersShade*seller7TrueValue);
			atomS7.setTypeComponent("isSeller", 1.0);
			CombinatorialType s7 = new CombinatorialType(); 		//Seller's type
			s7.addAtomicBid(atomS7);
			
			double seller8TrueValue = generator.nextDouble();
			AtomicBid atomS8 = new AtomicBid(9, items, sellersShade*seller8TrueValue);
			atomS8.setTypeComponent("isSeller", 1.0);
			CombinatorialType s8 = new CombinatorialType(); 		//Seller's type
			s8.addAtomicBid(atomS8);
			
			double seller9TrueValue = generator.nextDouble();
			AtomicBid atomS9 = new AtomicBid(10, items, sellersShade*seller9TrueValue);
			atomS9.setTypeComponent("isSeller", 1.0);
			CombinatorialType s9 = new CombinatorialType(); 		//Seller's type
			s9.addAtomicBid(atomS9);
			
			double seller10TrueValue = generator.nextDouble();
			AtomicBid atomS10 = new AtomicBid(11, items, sellersShade*seller10TrueValue);
			atomS10.setTypeComponent("isSeller", 1.0);
			CombinatorialType s10 = new CombinatorialType(); 		//Seller's type
			s10.addAtomicBid(atomS10);
			
			List<Type> bids = new LinkedList<Type>();
			bids.add(b1);
			bids.add(s1);
			bids.add(s2);
			bids.add(s3);
			bids.add(s4);
			bids.add(s5);
			bids.add(s6);
			bids.add(s7);
			bids.add(s8);
			bids.add(s9);
			bids.add(s10);
			
			Planner planner = new GeneralErrorPlanner(numberOfBuyers, numberOfSellers, bids);
			planner.setNumberOfPlans(5);
			planner.setMinSellersPerPlan(2);
			planner.setMaxSellersPerPlan(2);
			
			DoubleSidedMarket market  = new DoubleSidedMarket(numberOfBuyers, numberOfSellers, bids, planner);
			market.setPaymentRule("VCG");
			market.setPaymentCorrectionRule("None");
			
			boolean isBB = true;
			if( isBB )
			{ 
				//VCG_IC BB analysis
				market.solveIt();
				if(market.getAllocation().getWelfare() > 0)
				{
					double totalPayment = 0.;
					for(int j = 0; j < market.getPayments().length; ++j)
						totalPayment += market.getPayments()[j];
					
					if(totalPayment < -1e-6)
					{
						budgetCounter+= 1;
						budgetAmount += totalPayment;
					}
				}
				//else
				//{
				//	budgetCounter+= 1;
				//	budgetAmount += market.getAllocation().getWelfare();
				//}
				utility += market.getAllocation().getWelfare();
			}
			else
			{
				market.computeWinnerDetermination();
				
				utility += market.getAllocation().getWelfare();
				if( market.getAllocation().getWelfare() < 0)
				{
					budgetAmount += market.getAllocation().getWelfare();
					budgetCounter+= 1;
				}
				
				boolean isAllocated = false;
				int allocatedPlan = 0;		
				double itsValue = 0.;
				double itsCost  = 0.;
				if( market.getAllocation().getNumberOfAllocatedBuyers() > 0 )
				{
					isAllocated = true;
					allocatedPlan = market.getAllocation().getAllocatedBundlesByIndex(0).get(0);
					itsValue = planner.getPlans().get(0).getAtom(allocatedPlan).getValue() / buyersShade;
					itsCost  = (Double)planner.getPlans().get(0).getAtom(allocatedPlan).getTypeComponent("Cost") / sellersShade;
				}
				planner.withdrawError();
				planner.reset(bids);
				
				market.solveIt();
				
				if( isAllocated && (market.getAllocation().getWelfare()>0))
				{
					profitsOfBuyer  += itsValue - market.getPayments()[0];
					double sellerPayments = 0.;
					for(int j = 1; j < market.getPayments().length; ++j)
						sellerPayments += market.getPayments()[j];
					profitsOfSeller += itsCost - sellerPayments;
				}
			}
		}
		
		System.out.println("Total utility is: " + utility + " budget2utility ratio: " + ((double)budgetAmount/utility) + 
				           " budgetCounter=" + ((double)budgetCounter/(double)numberOfSamples) + " profitsOfBuyer: " + profitsOfBuyer/numberOfSamples +
				           " profitsOfSellers: " + profitsOfSeller / numberOfSamples);
		time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
	}*/
	/*
	@Test
	public void testDoubleAuction_Benchmarks() throws Exception
	{
		System.out.println("Launch the test");
		LocalTime time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
		
		int numberOfBuyers = 1;
		int numberOfSellers = 15;

		int numberOfSamples = 100000;
		double buyersShade = 0.99;
		double sellersShade = 1.09;
		
		double utility = 0.;
		double budgetAmount = 0.;
		int budgetCounter = 0;
		double profitsOfBuyer = 0.;
		double profitsOfSeller = 0.;
		Random generator = new Random();
		
		for(int i = 0; i < numberOfSamples; ++i)
		{
			List<Integer> items = new LinkedList<Integer>();
			items.add(0);											//a query
			
			double buyersTrueValue = generator.nextDouble();
			AtomicBid atomB1 = new AtomicBid(1, items, buyersShade*buyersTrueValue);
			atomB1.setTypeComponent("isSeller", 0.0);
			CombinatorialType b1 = new CombinatorialType(); 		//Buyer's type
			b1.addAtomicBid(atomB1);
			
			double sellerATrueValue = generator.nextDouble();
			AtomicBid atomS1 = new AtomicBid(2, items, sellersShade*sellerATrueValue);
			atomS1.setTypeComponent("isSeller", 1.0);
			CombinatorialType s1 = new CombinatorialType(); 		//Seller's type
			s1.addAtomicBid(atomS1);
			
			double sellerBTrueValue = generator.nextDouble();
			AtomicBid atomS2 = new AtomicBid(3, items, sellersShade*sellerBTrueValue);
			atomS2.setTypeComponent("isSeller", 1.0);
			CombinatorialType s2 = new CombinatorialType(); 		//Seller's type
			s2.addAtomicBid(atomS2);
			
			double sellerCTrueValue = generator.nextDouble();
			AtomicBid atomS3 = new AtomicBid(4, items, sellersShade*sellerCTrueValue);
			atomS3.setTypeComponent("isSeller", 1.0);
			CombinatorialType s3 = new CombinatorialType(); 		//Seller's type
			s3.addAtomicBid(atomS3);
			
			double seller4TrueValue = generator.nextDouble();
			AtomicBid atomS4 = new AtomicBid(5, items, sellersShade*seller4TrueValue);
			atomS4.setTypeComponent("isSeller", 1.0);
			CombinatorialType s4 = new CombinatorialType(); 		//Seller's type
			s4.addAtomicBid(atomS4);
			
			double seller5TrueValue = generator.nextDouble();
			AtomicBid atomS5 = new AtomicBid(6, items, sellersShade*seller5TrueValue);
			atomS5.setTypeComponent("isSeller", 1.0);
			CombinatorialType s5 = new CombinatorialType(); 		//Seller's type
			s5.addAtomicBid(atomS5);
			
			double seller6TrueValue = generator.nextDouble();
			AtomicBid atomS6 = new AtomicBid(7, items, sellersShade*seller6TrueValue);
			atomS6.setTypeComponent("isSeller", 1.0);
			CombinatorialType s6 = new CombinatorialType(); 		//Seller's type
			s6.addAtomicBid(atomS6);
			
			double seller7TrueValue = generator.nextDouble();
			AtomicBid atomS7 = new AtomicBid(8, items, sellersShade*seller7TrueValue);
			atomS7.setTypeComponent("isSeller", 1.0);
			CombinatorialType s7 = new CombinatorialType(); 		//Seller's type
			s7.addAtomicBid(atomS7);
			
			double seller8TrueValue = generator.nextDouble();
			AtomicBid atomS8 = new AtomicBid(9, items, sellersShade*seller8TrueValue);
			atomS8.setTypeComponent("isSeller", 1.0);
			CombinatorialType s8 = new CombinatorialType(); 		//Seller's type
			s8.addAtomicBid(atomS8);
			
			double seller9TrueValue = generator.nextDouble();
			AtomicBid atomS9 = new AtomicBid(10, items, sellersShade*seller9TrueValue);
			atomS9.setTypeComponent("isSeller", 1.0);
			CombinatorialType s9 = new CombinatorialType(); 		//Seller's type
			s9.addAtomicBid(atomS9);
			
			double seller10TrueValue = generator.nextDouble();
			AtomicBid atomS10 = new AtomicBid(11, items, sellersShade*seller10TrueValue);
			atomS10.setTypeComponent("isSeller", 1.0);
			CombinatorialType s10 = new CombinatorialType(); 		//Seller's type
			s10.addAtomicBid(atomS10);
			
			double seller11TrueValue = generator.nextDouble();
			AtomicBid atomS11 = new AtomicBid(12, items, sellersShade*seller11TrueValue);
			atomS11.setTypeComponent("isSeller", 1.0);
			CombinatorialType s11 = new CombinatorialType(); 		//Seller's type
			s11.addAtomicBid(atomS11);
			
			double seller12TrueValue = generator.nextDouble();
			AtomicBid atomS12 = new AtomicBid(13, items, sellersShade*seller12TrueValue);
			atomS12.setTypeComponent("isSeller", 1.0);
			CombinatorialType s12 = new CombinatorialType(); 		//Seller's type
			s12.addAtomicBid(atomS12);
			
			double seller13TrueValue = generator.nextDouble();
			AtomicBid atomS13 = new AtomicBid(14, items, sellersShade*seller13TrueValue);
			atomS13.setTypeComponent("isSeller", 1.0);
			CombinatorialType s13 = new CombinatorialType(); 		//Seller's type
			s13.addAtomicBid(atomS13);
			
			double seller14TrueValue = generator.nextDouble();
			AtomicBid atomS14 = new AtomicBid(15, items, sellersShade*seller14TrueValue);
			atomS14.setTypeComponent("isSeller", 1.0);
			CombinatorialType s14 = new CombinatorialType(); 		//Seller's type
			s14.addAtomicBid(atomS14);
			
			double seller15TrueValue = generator.nextDouble();
			AtomicBid atomS15 = new AtomicBid(16, items, sellersShade*seller15TrueValue);
			atomS15.setTypeComponent("isSeller", 1.0);
			CombinatorialType s15 = new CombinatorialType(); 		//Seller's type
			s15.addAtomicBid(atomS15);
			
			List<Type> bids = new LinkedList<Type>();
			bids.add(b1);
			bids.add(s1);
			bids.add(s2);
			bids.add(s3);
			bids.add(s4);
			bids.add(s5);
			bids.add(s6);
			bids.add(s7);
			bids.add(s8);
			bids.add(s9);
			bids.add(s10);
			bids.add(s11);
			bids.add(s12);
			bids.add(s13);
			bids.add(s14);
			bids.add(s15);
			
			Planner planner = new GeneralPlanner(numberOfBuyers, numberOfSellers, bids);
			DoubleSidedMarket market  = new DoubleSidedMarket(numberOfBuyers, numberOfSellers, bids, planner);
			market.setPaymentRule("Threshold");
			market.setPaymentCorrectionRule("None");
			
			//VCG_IC BB analysis
			boolean isBB = true;
			if(isBB)
			{
				market.solveIt();
				if(market.getAllocation().getWelfare() > 0)
				{
					double totalPayment = 0.;
					for(int j = 0; j < market.getPayments().length; ++j)
						totalPayment += market.getPayments()[j];
					
					if(totalPayment < -1e-6)
					{
						budgetCounter+= 1;
						budgetAmount += totalPayment;
					}
				}
				utility += market.getAllocation().getWelfare();
			}
			else
			{
				market.computeWinnerDetermination();
				
				utility += market.getAllocation().getWelfare();
				if( market.getAllocation().getWelfare() < 0)
				{
					budgetAmount += market.getAllocation().getWelfare();
					budgetCounter+= 1;
				}
				
				boolean isAllocated = false;
				int allocatedPlan = 0;		
				double itsValue = 0.;
				double itsCost  = 0.;
				if( market.getAllocation().getNumberOfAllocatedBuyers() > 0 )
				{
					isAllocated = true;
					allocatedPlan = market.getAllocation().getAllocatedBundlesByIndex(0).get(0);
					itsValue = planner.getPlans().get(0).getAtom(allocatedPlan).getValue() / buyersShade;
					itsCost  = (Double)planner.getPlans().get(0).getAtom(allocatedPlan).getTypeComponent("Cost") / sellersShade;
				}
				planner.withdrawError();
				planner.reset(numberOfBuyers, numberOfSellers, bids);
				
				market.solveIt();
				
				if( isAllocated && (market.getAllocation().getWelfare()>0))
				{
					profitsOfBuyer  += itsValue - market.getPayments()[0];
					double sellerPayments = 0.;
					for(int j = 1; j < market.getPayments().length; ++j)
						sellerPayments += market.getPayments()[j];
					profitsOfSeller += itsCost - sellerPayments;
				}
			}
		}
		
		System.out.println("Total utility is: " + utility + " budget2utility ratio: " + ((double)budgetAmount/utility) + 
				           " budgetCounter=" + ((double)budgetCounter/(double)numberOfSamples) + " profitsOfBuyer: " + profitsOfBuyer/numberOfSamples +
				           " profitsOfSellers: " + profitsOfSeller / numberOfSamples);
		time = LocalTime.now();
		System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
	}
*/
	
/*	@Test
	public void testDoubleSidedMarket() throws Exception 
	{
		int numberOfBuyers = 2;
		int numberOfSellers = 3;
		
		List<Integer> serversUsed1 = new LinkedList<Integer>();							//Sellers
		serversUsed1.add(1);
		serversUsed1.add(2);
		List<Double> costsAssociated1 = new LinkedList<Double>();						//Costs of sellers
		costsAssociated1.add(0.05);
		costsAssociated1.add(0.2);
		AtomicBid plan1 = new SemanticWebType(1, serversUsed1, 0.3, costsAssociated1);
		
		List<Integer> serversUsed2 = new LinkedList<Integer>();
		serversUsed2.add(2);
		serversUsed2.add(3);
		List<Double> costsAssociated2 = new LinkedList<Double>();
		costsAssociated2.add(0.1);
		costsAssociated2.add(0.4);		
		AtomicBid plan2 = new SemanticWebType(1, serversUsed2, 0.6, costsAssociated2);
		
		CombinatorialType t1 = new CombinatorialType();
		t1.addAtomicBid(plan1);
		t1.addAtomicBid(plan2);
		
		
		List<Integer> serversUsed3 = new LinkedList<Integer>();							//Sellers
		serversUsed3.add(1);
		serversUsed3.add(2);
		List<Double> costsAssociated3 = new LinkedList<Double>();						//Costs of sellers
		costsAssociated3.add(0.1);
		costsAssociated3.add(0.2);
		AtomicBid plan3 = new SemanticWebType(2, serversUsed1, 0.6, costsAssociated3);
		
		CombinatorialType t2 = new CombinatorialType();
		t2.addAtomicBid(plan3);
		
		List<Type> bids = new LinkedList<Type>();
		bids.add(t1);
		bids.add(t2);
		
		Auction market = new DoubleSidedMarket(numberOfBuyers, numberOfSellers, bids);
		
		
		ShavingStrategy[] strategies = new ShavingStrategy[1];
		strategies[0] = new AdditiveShavingStrategy();
		
		BNESolver bne = new BNESolver(market, bids, 2, 10, 100, strategies );
		bne.setPrecision( 1e-3 );
		bne.sortToBins();
		
		bne.solveIt();

		System.out.println(bne.toString());
	}*/
	/*
	@Test
	public void testCombinatorialAuctionLarge() 
	{
		System.out.println("Launch the test");
		
		int numberOfAgents = 9;
		int numberOfItems = 2;
		
		// 1st agent (medium values):
		List<Integer> items11 = new LinkedList<Integer>();
		items11.add(1);
		AtomicBid atom11 = new AtomicBid(1, items11, 0.25);
		atom11.setTypeComponent("Distribution", 1.0);
		atom11.setTypeComponent("minValue", 0.0);
		atom11.setTypeComponent("maxValue", 1.0);
		
		List<Integer> items12 = new LinkedList<Integer>();
		items12.add(2);
		AtomicBid atom12 = new AtomicBid(1, items12, 0.15);
		atom12.setTypeComponent("Distribution", 1.0);
		atom12.setTypeComponent("minValue", 0.0);
		atom12.setTypeComponent("maxValue", 1.0);
		
		List<Integer> items13 = new LinkedList<Integer>();
		items13.add(1);
		items13.add(2);
		AtomicBid atom13 = new AtomicBid(1, items13, 0.3);
		atom13.setTypeComponent("Distribution", 1.0);
		atom13.setTypeComponent("minValue", 0.0);
		atom13.setTypeComponent("maxValue", 1.0);
		
		Type t1 = new CombinatorialType();
		t1.addAtomicBid(atom11);
		t1.addAtomicBid(atom12);
		t1.addAtomicBid(atom13);
		
		
		// 2nd agent (medium values):
		List<Integer> items21 = new LinkedList<Integer>();
		items21.add(1);
		AtomicBid atom21 = new AtomicBid(2, items21, 0.3);
		atom21.setTypeComponent("Distribution", 1.0);
		atom21.setTypeComponent("minValue", 0.0);
		atom21.setTypeComponent("maxValue", 1.0);
		
		List<Integer> items22 = new LinkedList<Integer>();
		items22.add(2);
		AtomicBid atom22 = new AtomicBid(2, items22, 0.3);
		atom22.setTypeComponent("Distribution", 1.0);
		atom22.setTypeComponent("minValue", 0.0);
		atom22.setTypeComponent("maxValue", 1.0);
		
		List<Integer> items23 = new LinkedList<Integer>();
		items23.add(1);
		items23.add(2);
		AtomicBid atom23 = new AtomicBid(2, items23, 0.3);
		atom23.setTypeComponent("Distribution", 1.0);
		atom23.setTypeComponent("minValue", 0.0);
		atom23.setTypeComponent("maxValue", 1.0);
		
		Type t2 = new CombinatorialType();
		t2.addAtomicBid(atom21);
		t2.addAtomicBid(atom22);
		t2.addAtomicBid(atom23);		
		
		
		// 3rd agent (medium values):
		List<Integer> items31 = new LinkedList<Integer>();
		items31.add(1);
		AtomicBid atom31 = new AtomicBid(3, items31, 0.2);
		atom31.setTypeComponent("Distribution", 1.0);
		atom31.setTypeComponent("minValue", 0.0);
		atom31.setTypeComponent("maxValue", 1.0);
		
		List<Integer> items32 = new LinkedList<Integer>();
		items32.add(2);
		AtomicBid atom32 = new AtomicBid(3, items32, 0.2);
		atom32.setTypeComponent("Distribution", 1.0);
		atom32.setTypeComponent("minValue", 0.0);
		atom32.setTypeComponent("maxValue", 1.0);
		
		List<Integer> items33 = new LinkedList<Integer>();
		items33.add(1);
		items33.add(2);
		AtomicBid atom33 = new AtomicBid(3, items33, 0.45);
		atom33.setTypeComponent("Distribution", 1.0);
		atom33.setTypeComponent("minValue", 0.0);
		atom33.setTypeComponent("maxValue", 1.0);
		
		Type t3 = new CombinatorialType();
		t3.addAtomicBid(atom31);
		t3.addAtomicBid(atom32);
		t3.addAtomicBid(atom33);
		
		// 4th agent (small values):
		List<Integer> items41 = new LinkedList<Integer>();
		items41.add(1);
		AtomicBid atom41 = new AtomicBid(4, items41, 0.1);
		atom41.setTypeComponent("Distribution", 1.0);
		atom41.setTypeComponent("minValue", 0.0);
		atom41.setTypeComponent("maxValue", 1.0);
		
		List<Integer> items42 = new LinkedList<Integer>();
		items42.add(2);
		AtomicBid atom42 = new AtomicBid(4, items42, 0.05);
		atom42.setTypeComponent("Distribution", 1.0);
		atom42.setTypeComponent("minValue", 0.0);
		atom42.setTypeComponent("maxValue", 1.0);
		
		List<Integer> items43 = new LinkedList<Integer>();
		items43.add(1);
		items43.add(2);
		AtomicBid atom43 = new AtomicBid(4, items43, 0.1);
		atom43.setTypeComponent("Distribution", 1.0);
		atom43.setTypeComponent("minValue", 0.0);
		atom43.setTypeComponent("maxValue", 1.0);
		
		Type t4 = new CombinatorialType();
		t4.addAtomicBid(atom41);
		t4.addAtomicBid(atom42);
		t4.addAtomicBid(atom43);
		
		// 5th agent (small values):
		List<Integer> items51 = new LinkedList<Integer>();
		items51.add(1);
		AtomicBid atom51 = new AtomicBid(5, items51, 0.04);
		atom51.setTypeComponent("Distribution", 1.0);
		atom51.setTypeComponent("minValue", 0.0);
		atom51.setTypeComponent("maxValue", 1.0);
				
		List<Integer> items52 = new LinkedList<Integer>();
		items52.add(2);
		AtomicBid atom52 = new AtomicBid(5, items52, 0.01);
		atom52.setTypeComponent("Distribution", 1.0);
		atom52.setTypeComponent("minValue", 0.0);
		atom52.setTypeComponent("maxValue", 1.0);
				
		List<Integer> items53 = new LinkedList<Integer>();
		items53.add(1);
		items53.add(2);
		AtomicBid atom53 = new AtomicBid(5, items53, 0.3);
		atom53.setTypeComponent("Distribution", 1.0);
		atom53.setTypeComponent("minValue", 0.0);
		atom53.setTypeComponent("maxValue", 1.0);
				
		Type t5 = new CombinatorialType();
		t5.addAtomicBid(atom51);
		t5.addAtomicBid(atom52);
		t5.addAtomicBid(atom53);
		
		// 6th agent (small values):
		List<Integer> items61 = new LinkedList<Integer>();
		items61.add(1);
		AtomicBid atom61 = new AtomicBid(6, items61, 0.01);
		atom61.setTypeComponent("Distribution", 1.0);
		atom61.setTypeComponent("minValue", 0.0);
		atom61.setTypeComponent("maxValue", 1.0);
				
		List<Integer> items62 = new LinkedList<Integer>();
		items62.add(2);
		AtomicBid atom62 = new AtomicBid(6, items62, 0.15);
		atom62.setTypeComponent("Distribution", 1.0);
		atom62.setTypeComponent("minValue", 0.0);
		atom62.setTypeComponent("maxValue", 1.0);
				
		List<Integer> items63 = new LinkedList<Integer>();
		items63.add(1);
		items63.add(2);
		AtomicBid atom63 = new AtomicBid(6, items63, 0.32);
		atom63.setTypeComponent("Distribution", 1.0);
		atom63.setTypeComponent("minValue", 0.0);
		atom63.setTypeComponent("maxValue", 1.0);
				
		Type t6 = new CombinatorialType();
		t6.addAtomicBid(atom61);
		t6.addAtomicBid(atom62);
		t6.addAtomicBid(atom63);
		
		
		// 7th agent (large values):
		List<Integer> items71 = new LinkedList<Integer>();
		items71.add(1);
		AtomicBid atom71 = new AtomicBid(7, items61, 0.3);
		atom71.setTypeComponent("Distribution", 1.0);
		atom71.setTypeComponent("minValue", 0.0);
		atom71.setTypeComponent("maxValue", 1.0);
						
		List<Integer> items72 = new LinkedList<Integer>();
		items72.add(2);
		AtomicBid atom72 = new AtomicBid(7, items72, 0.35);
		atom72.setTypeComponent("Distribution", 1.0);
		atom72.setTypeComponent("minValue", 0.0);
		atom72.setTypeComponent("maxValue", 1.0);
						
		List<Integer> items73 = new LinkedList<Integer>();
		items73.add(1);
		items73.add(2);
		AtomicBid atom73 = new AtomicBid(7, items73, 0.99);
		atom73.setTypeComponent("Distribution", 1.0);
		atom73.setTypeComponent("minValue", 0.0);
		atom73.setTypeComponent("maxValue", 1.0);
						
		Type t7 = new CombinatorialType();
		t7.addAtomicBid(atom71);
		t7.addAtomicBid(atom72);
		t7.addAtomicBid(atom73);
				
		
		// 8th agent (large values):
		List<Integer> items81 = new LinkedList<Integer>();
		items81.add(1);
		AtomicBid atom81 = new AtomicBid(8, items81, 0.3);
		atom81.setTypeComponent("Distribution", 1.0);
		atom81.setTypeComponent("minValue", 0.0);
		atom81.setTypeComponent("maxValue", 1.0);
						
		List<Integer> items82 = new LinkedList<Integer>();
		items82.add(2);
		AtomicBid atom82 = new AtomicBid(8, items82, 0.4);
		atom82.setTypeComponent("Distribution", 1.0);
		atom82.setTypeComponent("minValue", 0.0);
		atom82.setTypeComponent("maxValue", 1.0);
						
		List<Integer> items83 = new LinkedList<Integer>();
		items83.add(1);
		items83.add(2);
		AtomicBid atom83 = new AtomicBid(8, items83, 0.95);
		atom83.setTypeComponent("Distribution", 1.0);
		atom83.setTypeComponent("minValue", 0.0);
		atom83.setTypeComponent("maxValue", 1.0);
						
		Type t8 = new CombinatorialType();
		t8.addAtomicBid(atom81);
		t8.addAtomicBid(atom82);
		t8.addAtomicBid(atom83);
		
		
		// 9th agent (large values):
		List<Integer> items91 = new LinkedList<Integer>();
		items91.add(1);
		AtomicBid atom91 = new AtomicBid(9, items61, 0.4);
		atom91.setTypeComponent("Distribution", 1.0);
		atom91.setTypeComponent("minValue", 0.0);
		atom91.setTypeComponent("maxValue", 1.0);
						
		List<Integer> items92 = new LinkedList<Integer>();
		items92.add(2);
		AtomicBid atom92 = new AtomicBid(9, items92, 0.45);
		atom92.setTypeComponent("Distribution", 1.0);
		atom92.setTypeComponent("minValue", 0.0);
		atom92.setTypeComponent("maxValue", 1.0);

		List<Integer> items93 = new LinkedList<Integer>();
		items93.add(1);
		items93.add(2);
		AtomicBid atom93 = new AtomicBid(9, items93, 0.8);
		atom93.setTypeComponent("Distribution", 1.0);
		atom93.setTypeComponent("minValue", 0.0);
		atom93.setTypeComponent("maxValue", 1.0);
						
		Type t9 = new CombinatorialType();
		t9.addAtomicBid(atom91);
		t9.addAtomicBid(atom92);
		t9.addAtomicBid(atom93);
		
		
		List<Type> lst = new LinkedList<Type>();
		lst.add(t1);
		lst.add(t2);
		lst.add(t3);
		lst.add(t4);
		lst.add(t5);
		lst.add(t6);
		lst.add(t7);
		lst.add(t8);
		lst.add(t9);
		
		Auction auction = new CAXOR(numberOfAgents, numberOfItems, lst);
		BNESolver bne = new BNESolver(auction, lst, 3, 100, 50, new MultiplicativeShavingStrategy());
		bne.sortToBins();
		
		bne.solveIt();
		
		//assertTrue(bne.getShavingFactor(0) == 1.0);
		assertTrue(bne.getShavingFactor(1) == 0.0);
		//assertTrue(bne.getShavingFactor(2) == 1.0);
		System.out.println(bne.toString());
	}*/
	/*
	@Test
	public void testCombinatorialAuctionMedium() 
	{
		System.out.println("Launch the test");
		
		int numberOfAgents = 6;
		int numberOfItems = 2;
		
		// 1st agent (small values):
		List<Integer> items11 = new LinkedList<Integer>();
		items11.add(1);
		AtomicBid atom11 = new AtomicBid(1, items11, 0.01);
		atom11.setTypeComponent("Distribution", 1.0);
		atom11.setTypeComponent("minValue", 0.0);
		atom11.setTypeComponent("maxValue", 1.0);
		
		List<Integer> items12 = new LinkedList<Integer>();
		items12.add(2);
		AtomicBid atom12 = new AtomicBid(1, items12, 0.02);
		atom12.setTypeComponent("Distribution", 1.0);
		atom12.setTypeComponent("minValue", 0.0);
		atom12.setTypeComponent("maxValue", 1.0);
		
		List<Integer> items13 = new LinkedList<Integer>();
		items13.add(1);
		items13.add(2);
		AtomicBid atom13 = new AtomicBid(1, items13, 0.10);
		atom13.setTypeComponent("Distribution", 1.0);
		atom13.setTypeComponent("minValue", 0.0);
		atom13.setTypeComponent("maxValue", 1.0);
		
		Type t1 = new CombinatorialType();
		t1.addAtomicBid(atom11);
		t1.addAtomicBid(atom12);
		t1.addAtomicBid(atom13);
		
		
		// 2nd agent (small values):
		List<Integer> items21 = new LinkedList<Integer>();
		items21.add(1);
		AtomicBid atom21 = new AtomicBid(2, items21, 0.04);
		atom21.setTypeComponent("Distribution", 1.0);
		atom21.setTypeComponent("minValue", 0.0);
		atom21.setTypeComponent("maxValue", 1.0);
		
		List<Integer> items22 = new LinkedList<Integer>();
		items22.add(2);
		AtomicBid atom22 = new AtomicBid(2, items22, 0.1);
		atom22.setTypeComponent("Distribution", 1.0);
		atom22.setTypeComponent("minValue", 0.0);
		atom22.setTypeComponent("maxValue", 1.0);
		
		List<Integer> items23 = new LinkedList<Integer>();
		items23.add(1);
		items23.add(2);
		AtomicBid atom23 = new AtomicBid(2, items23, 0.1);
		atom23.setTypeComponent("Distribution", 1.0);
		atom23.setTypeComponent("minValue", 0.0);
		atom23.setTypeComponent("maxValue", 1.0);
		
		Type t2 = new CombinatorialType();
		t2.addAtomicBid(atom21);
		t2.addAtomicBid(atom22);
		t2.addAtomicBid(atom23);		
		
		
		// 3rd agent (medium values):
		List<Integer> items31 = new LinkedList<Integer>();
		items31.add(1);
		AtomicBid atom31 = new AtomicBid(3, items31, 0.1);
		atom31.setTypeComponent("Distribution", 1.0);
		atom31.setTypeComponent("minValue", 0.0);
		atom31.setTypeComponent("maxValue", 1.0);
		
		List<Integer> items32 = new LinkedList<Integer>();
		items32.add(2);
		AtomicBid atom32 = new AtomicBid(3, items32, 0.2);
		atom32.setTypeComponent("Distribution", 1.0);
		atom32.setTypeComponent("minValue", 0.0);
		atom32.setTypeComponent("maxValue", 1.0);
		
		List<Integer> items33 = new LinkedList<Integer>();
		items33.add(1);
		items33.add(2);
		AtomicBid atom33 = new AtomicBid(3, items33, 0.4);
		atom33.setTypeComponent("Distribution", 1.0);
		atom33.setTypeComponent("minValue", 0.0);
		atom33.setTypeComponent("maxValue", 1.0);
		
		Type t3 = new CombinatorialType();
		t3.addAtomicBid(atom31);
		t3.addAtomicBid(atom32);
		t3.addAtomicBid(atom33);
		
		// 4th agent (small values):
		List<Integer> items41 = new LinkedList<Integer>();
		items41.add(1);
		AtomicBid atom41 = new AtomicBid(4, items41, 0.1);
		atom41.setTypeComponent("Distribution", 1.0);
		atom41.setTypeComponent("minValue", 0.0);
		atom41.setTypeComponent("maxValue", 1.0);
		
		List<Integer> items42 = new LinkedList<Integer>();
		items42.add(2);
		AtomicBid atom42 = new AtomicBid(4, items42, 0.15);
		atom42.setTypeComponent("Distribution", 1.0);
		atom42.setTypeComponent("minValue", 0.0);
		atom42.setTypeComponent("maxValue", 1.0);
		
		List<Integer> items43 = new LinkedList<Integer>();
		items43.add(1);
		items43.add(2);
		AtomicBid atom43 = new AtomicBid(4, items43, 0.3);
		atom43.setTypeComponent("Distribution", 1.0);
		atom43.setTypeComponent("minValue", 0.0);
		atom43.setTypeComponent("maxValue", 1.0);
		
		Type t4 = new CombinatorialType();
		t4.addAtomicBid(atom41);
		t4.addAtomicBid(atom42);
		t4.addAtomicBid(atom43);
		
		// 5th agent (small values):
		List<Integer> items51 = new LinkedList<Integer>();
		items51.add(1);
		AtomicBid atom51 = new AtomicBid(5, items51, 0.3);
		atom51.setTypeComponent("Distribution", 1.0);
		atom51.setTypeComponent("minValue", 0.0);
		atom51.setTypeComponent("maxValue", 1.0);
				
		List<Integer> items52 = new LinkedList<Integer>();
		items52.add(2);
		AtomicBid atom52 = new AtomicBid(5, items52, 0.4);
		atom52.setTypeComponent("Distribution", 1.0);
		atom52.setTypeComponent("minValue", 0.0);
		atom52.setTypeComponent("maxValue", 1.0);
				
		List<Integer> items53 = new LinkedList<Integer>();
		items53.add(1);
		items53.add(2);
		AtomicBid atom53 = new AtomicBid(5, items53, 0.9);
		atom53.setTypeComponent("Distribution", 1.0);
		atom53.setTypeComponent("minValue", 0.0);
		atom53.setTypeComponent("maxValue", 1.0);
				
		Type t5 = new CombinatorialType();
		t5.addAtomicBid(atom51);
		t5.addAtomicBid(atom52);
		t5.addAtomicBid(atom53);
		
		// 6th agent (small values):
		List<Integer> items61 = new LinkedList<Integer>();
		items61.add(1);
		AtomicBid atom61 = new AtomicBid(6, items61, 0.35);
		atom61.setTypeComponent("Distribution", 1.0);
		atom61.setTypeComponent("minValue", 0.0);
		atom61.setTypeComponent("maxValue", 1.0);
				
		List<Integer> items62 = new LinkedList<Integer>();
		items62.add(2);
		AtomicBid atom62 = new AtomicBid(6, items62, 0.5);
		atom62.setTypeComponent("Distribution", 1.0);
		atom62.setTypeComponent("minValue", 0.0);
		atom62.setTypeComponent("maxValue", 1.0);
				
		List<Integer> items63 = new LinkedList<Integer>();
		items63.add(1);
		items63.add(2);
		AtomicBid atom63 = new AtomicBid(6, items63, 0.8);
		atom63.setTypeComponent("Distribution", 1.0);
		atom63.setTypeComponent("minValue", 0.0);
		atom63.setTypeComponent("maxValue", 1.0);
				
		Type t6 = new CombinatorialType();
		t6.addAtomicBid(atom61);
		t6.addAtomicBid(atom62);
		t6.addAtomicBid(atom63);
		
		List<Type> lst = new LinkedList<Type>();
		lst.add(t1);
		lst.add(t2);
		lst.add(t3);
		lst.add(t4);
		lst.add(t5);
		lst.add(t6);
		
		Auction auction = new CAXOR(numberOfAgents, numberOfItems, lst, "Core");
		BNESolver bne = new BNESolver(auction, lst, 3, 10, 50, new MultiplicativeShavingStrategy());
		bne.sortToBins();
		
		bne.solveIt();
		
		//assertTrue(bne.getShavingFactor(0) == 1.0);
		assertTrue(bne.getShavingFactor(1) == 0.0);
		//assertTrue(bne.getShavingFactor(2) == 1.0);
		System.out.println(bne.toString());
	}
	*/
	/*@Test
	public void testXChartTest()
	{
		Chart chart = new ChartBuilder().width(800).height(600).theme(ChartTheme.Matlab).title("Matlab Theme").xAxisTitle("X").yAxisTitle("Y").build();
		chart.getStyleManager().setXAxisTickMarkSpacingHint(100);
		List<Integer> x = new LinkedList<Integer>();
		List<Double>  y = new LinkedList<Double>();
		
		x.add(1);
		x.add(2);
		y.add(10.);
		y.add(20.);
		
		chart.addSeries("price", x, y);
		
		x.add(3);
		y.add(10.);
		chart.getSeriesMap().get("price").replaceXData(x);
		chart.getSeriesMap().get("price").replaceYData(y);
		
	    // Show it
	    //new SwingWrapper(chart).displayChart();
	    try {
			BitmapEncoder.saveBitmap(chart, "./Sample_Chart", BitmapFormat.JPG);
		} catch (IOException e) 
	    {
			e.printStackTrace();
		}
	    
	    x.add(3);
		y.add(10.);
		chart.getSeriesMap().get("price").replaceXData(x);
		chart.getSeriesMap().get("price").replaceYData(y);
		
	    while(true);
	}*/
/*
	@Test
	public void testOnlineFirstPriceAuction1() 
	{		
		int numberOfAgents = 2;
		int numberOfItems = 1;
		
		List<Integer> items = new LinkedList<Integer>();
		items.add(1);
		
		AtomicBid atom1 = new AtomicBid(1, items, 0.9);
		atom1.setTypeComponent("Time", 2);
		atom1.setTypeComponent("Distribution", 1.0);
		atom1.setTypeComponent("minValue", 0.0);
		atom1.setTypeComponent("maxValue", 1.0);
		atom1.setTypeComponent("minTime", 0.0);
		atom1.setTypeComponent("maxTime", 2.0);
		
		OnlineType ot1 = new OnlineType();
		ot1.addAtomicBid(atom1);
		
		atom1 = new AtomicBid(2, items, 1.0);
		atom1.setTypeComponent("Time", 1);
		atom1.setTypeComponent("Distribution", 1.0);
		atom1.setTypeComponent("minValue", 0.0);
		atom1.setTypeComponent("maxValue", 1.0);
		atom1.setTypeComponent("minTime", 0.0);
		atom1.setTypeComponent("maxTime", 2.0);
		
		OnlineType ot2 = new OnlineType();
		ot2.addAtomicBid(atom1);
		
		List<Type> types = new LinkedList<Type>();
		types.add(ot1);
		types.add(ot2);
		
		OnlineFirstPriceAuction auction = new OnlineFirstPriceAuction(types);
		//auction.solveIt();
		
		//Allocation allocation = auction.getAllocation();

		BNESolver bne = new BNESolver(auction, types, 2, 10, 5, new AdditiveShavingStrategy());
		bne.setPrecision( 1e-2 );
		bne.sortToBins();
		
		bne.solveIt();

		//time = LocalTime.now();
		//System.out.println("Start: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
	}*/
}
