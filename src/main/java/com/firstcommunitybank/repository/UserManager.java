package com.firstcommunitybank.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firstcommunitybank.services.models.Account;
import com.firstcommunitybank.services.models.User;

public class UserManager {
	Logger consoleLogger = LoggerFactory.getLogger("consoleLogger");
	Logger fileLogger = LoggerFactory.getLogger("fileLogger");
	
	public Map<String, User> all(){
		Map<String, User> users;
		users = new HashMap<String, User>();
		User user;
		Account account;

		final String sql = "SELECT * FROM users;";

		try(Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
				ResultSet set = statement.executeQuery(sql);)
		{
			
			while(set.next()) {
				user = new User(set.getInt("id"), set.getString("username"), set.getString("passwords"), set.getBoolean("is_active"),
				set.getBoolean("is_staff"), set.getBoolean("is_admin"), set.getBoolean("is_authenticated"));
				int account_id = set.getInt("account");
				if(account_id != 0) {
					account = Account.objects.getAccount(account_id);
					user.setAccount(account);
				}
				users.put(user.getUsername(), user);
			}
			
		}catch(Exception e){
			//e.printStackTrace();
			consoleLogger.error(e.toString());
			fileLogger.error(e.toString());
		}
		
		return users;
	}
	
	public User createUser(String username, String password, boolean active, boolean staff, boolean admin, boolean authenticated){
		//consoleLogger.debug("Create a user.");
		fileLogger.debug("Create a user.");
		
		User user = null;
		final String sql = "INSERT INTO users(username, passwords, is_active, is_staff, is_admin, is_authenticated)"
				+ "VALUES(?, ?, ?, ?, ?, ?);";
		
		try (Connection connection = ConnectionFactory.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);)
		{
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			preparedStatement.setBoolean(3, active);
			preparedStatement.setBoolean(4, staff);
			preparedStatement.setBoolean(5, admin);
			preparedStatement.setBoolean(6, authenticated);
			preparedStatement.executeUpdate();

			user = User.objects.getUser(username);
			
		}catch(SQLException e){
			consoleLogger.error(e.getMessage());
			fileLogger.error(e.toString());
		}
		return user;

	}

	public User getUser(String username) {
		User user = null;
		Account account;
		
		final String sql = "SELECT * FROM users WHERE username = '" + username + "';";
		
		try(Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
				ResultSet set = statement.executeQuery(sql);)
		{
			
			if(set.next()) {
				user = new User(set.getInt("id"), set.getString("username"), set.getString("passwords"), set.getBoolean("is_active"),
						set.getBoolean("is_staff"), set.getBoolean("is_admin"), set.getBoolean("is_authenticated"));
				
				int account_id = set.getInt("account");
				if(account_id != 0) {
					account = Account.objects.getAccount(account_id);
					user.setAccount(account);
				}
			}
		
		}catch(Exception e){
			//e.printStackTrace();
			consoleLogger.error(e.toString());
			fileLogger.error(e.toString());
		}
		return user;
	}
	
	
	public Map<String, User> filterByAccountStatus(String status){
		Map<String, User> filteredHashMap = new HashMap<String, User>();
		Map<String, User> users;
		users = User.objects.all();
		
		User user;
		
		for (String username: users.keySet() ) {
			user = users.get(username);
		
			if (user.isActive()) {
				if(user.getAccount() != null && user.getAccount().getStatus().equals(status)) {
					filteredHashMap.put(username, user);
				}
			}
			
			
		}
		return filteredHashMap;
	
	}
	
	public Map<String, User> filterByAccountActive(){
		Map<String, User> filteredHashMap = new HashMap<String, User>();
		Map<String, User> users;
		users = User.objects.all();
		
		User user;
		
		for (String username: users.keySet() ) {
			user = users.get(username);
			if (user.isActive()) {
				if(user.getAccount() != null && user.getAccount().isActive() == true) {
					filteredHashMap.put(username, user);
				}
			}
			
			
		}
		return filteredHashMap;
	}
	
	public Map<String, User> filterByAccountAll(){
		Map<String, User> filteredHashMap = new HashMap<String, User>();
		Map<String, User> users;
		users = User.objects.all();
		
		User user;
		
		for (String username: users.keySet() ) {
			user = users.get(username);
			if (user.isActive()) {
				if(user.getAccount() != null) {
					filteredHashMap.put(username, user);
				}
			}
		}
		return filteredHashMap;
	}

}
