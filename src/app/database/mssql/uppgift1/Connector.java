package app.database.mssql.uppgift1;

import java.sql.*;

public class Connector {

	private static Connector instance = new Connector();
	private static final String DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static String URL = "jdbc:sqlserver://localhost:1433;database=uppgift11;";
	private static String USER = "app";
	private static String PASS = "app";
	
	private Connector() {
		try {
			Class.forName(DRIVER_CLASS);	
		} catch (ClassNotFoundException ce) {
			ce.printStackTrace();
		}
	}
	
	private Connection createConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASS);
	}
	
	public static Connection getConnection() throws SQLException {
		return instance.createConnection();
	}
	
}
