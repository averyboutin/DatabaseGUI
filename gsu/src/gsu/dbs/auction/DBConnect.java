package gsu.dbs.auction;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnect {

	private static String SERVER 	= "jdbc:mysql://45.79.216.182";
	private static String DATABASE 	= "AuctionDB";
	private static String USER 		= "root";
	private static String PASSWORD 	= "Orange!9739";
	private static Connection c;
	
	public static Connection connect() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}catch (InstantiationException ie) {
				System.err.println("Whoops: " + ie.getMessage());
		}catch (ClassNotFoundException cnf) {
			System.err.println("Whoops: " + cnf.getMessage());
		}catch (IllegalAccessException ia) {
			System.err.println("Whoops: " + ia.getMessage());
		}
		
		c = DriverManager.getConnection(SERVER + "/" + DATABASE, USER, PASSWORD);
		return c;
	}

	public static Connection getConnection() throws SQLException, ClassNotFoundException{
		if(!c.isClosed() && c != null) {
			return c;
		}
		connect();
		return c;
	}
}