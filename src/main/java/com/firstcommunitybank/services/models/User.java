package com.firstcommunitybank.services.models;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.firstcommunitybank.repository.ConnectionFactory;
import com.firstcommunitybank.repository.UserManager;

public class User {
	private int id;
	private String username;
	private String password;
	private boolean isActive;
	private boolean isStaff;
	private boolean isAdmin;
	private boolean isAuthenticated;
	
	private Account account;
	public static UserManager objects = new UserManager();
	
	public User(int id, String username, String password, boolean isActive, boolean isStaff, boolean isAdmin, boolean isAuthenticated) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.isActive = isActive;
		this.isStaff = isStaff;
		this.isAdmin = isAdmin;
		this.isAuthenticated = isAuthenticated;
	}
	
	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", isActive=" + isActive + ", isStaff="
				+ isStaff + ", isAdmin=" + isAdmin + ", isAuthenticated=" + isAuthenticated + ", account=" + account
				+ "]";
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isStaff() {
		return isStaff;
	}

	public void setStaff(boolean isStaff) {
		this.isStaff = isStaff;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public void save() {

		final String sql = "UPDATE users " +
							"SET passwords=?, is_active=?, is_staff=?, is_admin=?, is_authenticated=?, account=? " +
							"WHERE id=?;";
				
		
		try (Connection connection = ConnectionFactory.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);)
		{
			preparedStatement.setString(1, this.password);
			preparedStatement.setBoolean(2, this.isActive);
			preparedStatement.setBoolean(3, this.isStaff);
			preparedStatement.setBoolean(4, this.isAdmin);
			preparedStatement.setBoolean(5, this.isAuthenticated);
			if(this.account != null) {
				preparedStatement.setInt(6, this.account.getId());
			}else {
				preparedStatement.setNull(6, 0);
			}
			preparedStatement.setInt(7, this.id);
			preparedStatement.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
