package com.firstcommunitybank.services;

import com.firstcommunitybank.services.models.User;
// test
public class Backend {
	
	public static User authenticate(String username, String password) {
		
		User user = null;
		user = User.objects.getUser(username);
		
		if (user!= null && user.getPassword().equals(password)) {
			return user;
		}
		return null;
	}
	
	public static User login(String username, String password) {
		User user = authenticate(username, password);
		
		if(user != null) {
			user.setAuthenticated(true);
			
		}
		return user;
		
	}
	
	public static void logout(User user) {
		user.setAuthenticated(false);
	}
}
