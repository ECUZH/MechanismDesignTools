package ch.uzh.ifi.MechanismDesignToolsNEW;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RealQueriesBenchmark 
{

	 public RealQueriesBenchmark(List<String> queryIds, Connection dbConnection) 
	 {
		 this.queryIds = queryIds;
		 this.dbConnection = dbConnection;
	 }
	 
	public static void main(String[] args) throws Exception 
	{
		String inputFile = defaultInputFile;
        String outputFile = defaultOutputFile;
        
        BufferedReader in = new BufferedReader(new FileReader(inputFile));
        
        queryIdList = new ArrayList<String>();
        String line = in.readLine(); 							//skip header line
        while ((line = in.readLine()) != null) {
            // use comma as separator
            queryIdList.add(line);
        }

        Connection dbConnection = null;
        
        try 
        {
            Class.forName("org.postgresql.Driver");
            Properties props = new Properties();
            props.setProperty("user", USER);
            props.setProperty("password", PASSWORD);
            props.setProperty("ssl", "true");
            props.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
            
            dbConnection = DriverManager.getConnection("jdbc:postgresql://" + DBHOST + "/" + DB, props);
        } 
        catch (ClassNotFoundException e) 
        {
            e.printStackTrace();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        
        RealQueriesBenchmark priceCalculation = new RealQueriesBenchmark(queryIdList, dbConnection);
        try 
        {
            priceCalculation.calculatePrices(outputFile);
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            try 
            {
                dbConnection.close();
            } 
            catch (SQLException e) 
            {
            }
        }
        System.exit(0);   
	}

	public void calculatePrices(String outputFile) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException, SQLException 
	{
        PrintWriter writer = new PrintWriter(outputFile, "UTF-8");

        for (String queryId : queryIds) 
        {
            System.out.println("->  " + queryId);
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT value, planId FROM buyerActualValue WHERE queryId='" + queryId + "'");
            while (resultSet.next())
            {
                int value = resultSet.getInt("value");
                int planId = resultSet.getInt("planId");
                System.out.println("v = " + value );
                System.out.println("pi= " + planId );
            }
            
            Statement statement1 = dbConnection.createStatement();
            ResultSet resultSet1 = statement1.executeQuery("SELECT planId, host, cost FROM planSellerCost WHERE queryId='" + queryId + "'");
            while (resultSet1.next())
            {
            	int planId = resultSet1.getInt("planId");
                String host = resultSet1.getString("host");
                int cost = resultSet1.getInt("cost");
                System.out.println("host = " + host );
                System.out.println("cost = " + cost );
                System.out.println("pi= " + planId );
            }
            //Pricing pricing = new Pricing(new VCG(), dbConnection, queryId);
            //pricing.calculatePrices();
        }
        writer.close();
    }
	
	public static final String defaultInputFile = "C:\\Users\\Dmitry\\workspace\\Algorithms32\\MechanismDesignTools\\test\\Tools\\queries.csv";
    public static final String defaultOutputFile = "C:\\Users\\Dmitry\\workspace\\Algorithms32\\MechanismDesignTools\\test\\Tools\\output.csv";

    private static final String DBHOST = "pg.ifi.uzh.ch";
    private static final String DB = "s0691318";
    private static final String USER = "s0691318";
    private static final String PASSWORD = "jiP3p";

    private static List<String> queryIdList;
    private Connection dbConnection;
    private List<String> queryIds;
}
