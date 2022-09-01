
package com.firstcommunitybank.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionFactory {
	
	public static Connection getConnection() throws SQLException {
		String url = System.getenv("AWS_URL");
		String user = System.getenv("AWS_USER");
		String password = System.getenv("AWS_PASSWORD");
		
		Connection connection = DriverManager.getConnection(url, user, password);
		
		return connection;
		
	}

}
