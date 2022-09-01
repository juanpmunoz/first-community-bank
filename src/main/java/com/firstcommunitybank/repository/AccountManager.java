package com.firstcommunitybank.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firstcommunitybank.services.models.Account;

public class AccountManager {
	Logger consoleLogger;
	Logger fileLogger;
	
	public AccountManager(){
		consoleLogger = LoggerFactory.getLogger("consoleLogger");
		fileLogger = LoggerFactory.getLogger("fileLogger");
	}
	
	public Account createAccount(String socialSecurityNumber, String dateOfBirth, String accountType, String accountNumber,
			double balance, String status, boolean active) {
		Account account = null;
		final String sql = "INSERT INTO account(social_security_number, date_of_birth, account_type, account_number,"
				+ "balance, status, active)"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?);";
		
		try (Connection connection = ConnectionFactory.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);)
		{
			preparedStatement.setString(1, socialSecurityNumber);
			preparedStatement.setString(2, dateOfBirth);
			preparedStatement.setString(3, accountType);
			preparedStatement.setString(4, accountNumber);
			preparedStatement.setDouble(5, balance);
			preparedStatement.setString(6, status);
			preparedStatement.setBoolean(7, active);
			preparedStatement.executeUpdate();
			ResultSet set = preparedStatement.getGeneratedKeys();
            if(set.next())
            {
                int account_id = set.getInt("id");
                account = Account.objects.getAccount(account_id);
            }
			
		}catch(Exception e){
			consoleLogger.error(e.toString());
			fileLogger.error(e.toString());
			
			e.printStackTrace();
		}
		
				return account;
	}
	
	public Account getAccount(int id) {
		Account account = null;
		final String sql = "SELECT * FROM account WHERE id = '" + id + "';";
		
		try (Connection connection = ConnectionFactory.getConnection();
				Statement statement = connection.createStatement();
				ResultSet set = statement.executeQuery(sql);)
		{
			
			if(set.next()) {
				account = new Account(set.getInt("id"), set.getString("social_security_number"), set.getString("date_of_birth"),
						set.getString("account_type"), set.getString("account_number"), set.getDouble("balance"),
						set.getString("status"), set.getBoolean("active"));
			}
		
		}catch(Exception e){
			consoleLogger.error(e.toString());
			fileLogger.error(e.toString());
			e.printStackTrace();
		}
		return account;
	}
	
	
}


