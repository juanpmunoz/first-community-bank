package com.firstcommunitybank.services.models;

import java.sql.Connection;
import java.sql.PreparedStatement;


import com.firstcommunitybank.repository.AccountManager;
import com.firstcommunitybank.repository.ConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Account {
	private int id;
	private String socialSecurityNumber;
	private String dateOfBirth;
	private String accountType;
	private String accountNumber;
	private double balance;
	private String status;
	private boolean active;
	
	Logger consoleLogger;
	Logger fileLogger;
	
	public Account() {
		consoleLogger = LoggerFactory.getLogger("consoleLogger");
		fileLogger = LoggerFactory.getLogger("fileLogger");
	}
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}


	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}


	public String getDateOfBirth() {
		return dateOfBirth;
	}


	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}


	public String getAccountType() {
		return accountType;
	}


	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}


	public String getAccountNumber() {
		return accountNumber;
	}


	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}


	public double getBalance() {
		return balance;
	}


	public void setBalance(double balance) {
		this.balance = balance;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}

	public static AccountManager objects = new AccountManager();
	
	public Account(int id, String socialSecurityNumber, String dateOfBirth, String accountType, 
			String accountNumber, double balance, String status, boolean active) {
		this.id = id;
		this.socialSecurityNumber = socialSecurityNumber;
		this.dateOfBirth = dateOfBirth;
		this.accountType = accountType;
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.status = status;
		this.active = active;
		
	}
	
	
	public void save() {

		final String sql = "UPDATE account " +
							"SET social_security_number=?, date_of_birth=?, account_type=?, account_number=?,"
							+ " balance=?, status=?, active=? " +
							"WHERE id=?;";
				
		
		try (Connection connection = ConnectionFactory.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);)
		{
			preparedStatement.setString(1, this.socialSecurityNumber);
			preparedStatement.setString(2, this.dateOfBirth);
			preparedStatement.setString(3, this.accountType);
			preparedStatement.setString(4, this.accountNumber);
			preparedStatement.setDouble(5, this.balance);
			preparedStatement.setString(6, this.status);
			preparedStatement.setBoolean(7, this.active);
			preparedStatement.setInt(8, this.id);
			
			preparedStatement.executeUpdate();
			
		}catch(Exception e){
			//e.printStackTrace();
			consoleLogger.error(e.toString());
			fileLogger.error(e.toString());
		}
		
	}
	
	public void delete() {
		final String sql = "DELETE FROM account WHERE id=?";
				
		try(Connection connection = ConnectionFactory.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql);)
		{
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
					
		}catch(Exception e){
			//e.printStackTrace();
			consoleLogger.error(e.toString());
			fileLogger.error(e.toString());
	    }
				
	}
}
