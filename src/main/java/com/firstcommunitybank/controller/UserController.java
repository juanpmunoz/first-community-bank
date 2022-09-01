package com.firstcommunitybank.controller;

import java.util.HashMap;

import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firstcommunitybank.services.AccountNumberGenerator;
import com.firstcommunitybank.services.Backend;
import com.firstcommunitybank.services.models.Account;
import com.firstcommunitybank.services.models.User;

public class UserController {
	private Scanner sc;
	Logger consoleLogger;
	Logger fileLogger;
	
	public UserController(Scanner sc){
		this.sc = sc;
		consoleLogger = LoggerFactory.getLogger("consoleLogger");
		fileLogger = LoggerFactory.getLogger("fileLogger");
	}
	
	public void createAdmin() {
		//consoleLogger.debug("Create an admin.");
		fileLogger.debug("Create an admin.");
		
		User adminUser = null;
		String username = "";
		String password = "";
		boolean active = true;
		boolean staff = false;
		boolean admin = true;
		boolean authenticated = false;
		
		Scanner sc = this.sc;
	
		
		while(adminUser == null) {
			System.out.println("\n_____ Create Admin _____");
			System.out.print("Username: ");
			username = sc.nextLine();
			System.out.print("Password: ");
			password = sc.nextLine();
			
			if(!username.isEmpty() && !username.isBlank() && !password.isEmpty() && !password.isBlank()) {
				adminUser = User.objects.createUser(username, password, active, staff, admin, authenticated);
			}

			if(username.isEmpty() || username.isBlank() || password.isEmpty() || password.isBlank()) {
				System.out.println("\nUsername and password cannot be empty. Please try again");
			}else if(adminUser != null) {
				System.out.println("\nAdmin succesfully created!");
			}else {
				System.out.println("\nAdmin with username already exists. Please try again.\n");
			}
		}
	}
	
	public void loginOrRegister() {
		//consoleLogger.debug("Login or Register");
		fileLogger.debug("Login or Register");
		
		Scanner sc = this.sc;
		String userInput = "";
		
		while(!userInput.equals("1") && !userInput.equals("2")  && !userInput.equals("3")) {
			
			System.out.println("\nHello, would you like to: ");
			System.out.println("1. Login");
			System.out.println("2. Register");
			System.out.println("3. Admin/Staff Login");
			
			userInput = sc.nextLine();
		}
		
		switch(userInput) {
			case "1":
				login();
				break;
			case "2":
				register();
				login();
				break;
			case "3":
				adminStaffLogin();
				break;
		}
	}
	
	public void register() {
		//consoleLogger.debug("Register users.");
		fileLogger.debug("Register users.");
		
		String username;
		String password;
		User user = null;
		Scanner sc = this.sc;
		
		while(user == null){
			System.out.println("\n_____ Register _____");
			System.out.print("Username: ");
			username = sc.nextLine();
			System.out.print("Password: ");
			password = sc.nextLine();
			
			if(!username.isEmpty() && !username.isBlank() && !password.isEmpty() && !password.isBlank()) {
				user = User.objects.createUser(username, password, true, false, false, false);
			}
			
			if(username.isEmpty() || username.isBlank() || password.isEmpty() || password.isBlank()) {
				System.out.println("\nUsername and password cannot be empty. Please try again");
			}else if(user != null) {
				System.out.println("\nUser succesfully created!");
			}else {
				System.out.println("\nUser with username already exists. Please try again.\n");
			}
		}
	}
	
	public void login() {
		//consoleLogger.debug("User login");
		fileLogger.debug("User login");
		
		String username;
		String password;
		User user = null;
		Scanner sc = this.sc;
		
		while(user == null){
			System.out.println("\n_____ Login _____");
			System.out.print("Username: ");
			username = sc.nextLine();
			System.out.print("Password: ");
			password = sc.nextLine();
			user = Backend.login(username, password);
			
			if(user != null) {
				System.out.println("\nLogin Successful!");
				Request.user = user;
			}else {
				System.out.println("\nYour username or password was wrong.");
				System.out.println("Please try again.");
			}	
		}
	}
	
	public void logout() {
		//consoleLogger.debug("Logout");
		fileLogger.debug("Logout");
		
		User user = Request.user;
		Backend.logout(user);
		
		Request.user = null;
		Request.adminLogin = false;
		Request.staffLogin = false;
		
		System.out.println("\nLogout Successful!");
		loginOrRegister();
	}
	
	public void options() {
		//consoleLogger.debug("Customer's menu.");
		fileLogger.debug("Customer's menu");
		
		User user = Request.user;
		Scanner sc = this.sc;
		String userInput = "";
		
		while(!userInput.equals("1") && !userInput.equals("2")  && !userInput.equals("3") &&
				!userInput.equals("4") && !userInput.equals("5")  && !userInput.equals("6")) {
			
			System.out.println("\nHello "+ user.getUsername() + "! What would you like to do?");
			System.out.println("1. Apply for a bank account");
			System.out.println("2. Deposit");
			System.out.println("3. Withdraw");
			System.out.println("4. Transfer Funds");
			System.out.println("5. Balance");
			System.out.println("6. Log out");
			
			userInput = sc.nextLine();
		}
		
		switch(userInput) {
			case "1":
				applyForBankAccount();
				break;
			case "2":
				deposit(user);
				break;
			case "3":
				withdraw(user);
				break;
			case "4":
				transferFunds();
				break;
			case "5":
				balance();
				break;
			case "6":
				logout();
				break;
		}
		
	}
	
	public void applyForBankAccount() {
		//consoleLogger.debug("Bank application");
		fileLogger.debug("Bank application");
		
		Scanner sc = this.sc;
		String socialSecurityNumber="";
		String dateOfBirth = "";
		String accountType = "";
		String accountNumber = "";
		User user = Request.user;
		Account account;
		
		
		if (user.getAccount() != null && user.getAccount().isActive() == true) {
			System.out.println("\nYou already have an account.");
			
		}else if (user.getAccount() != null && user.getAccount().isActive() == false) {
			System.out.println("\nYou already applied for an account.");
			System.out.println("Account Status: " + user.getAccount().getStatus());
			
		}else {
			while(socialSecurityNumber.isEmpty() || socialSecurityNumber.isBlank() ||
					dateOfBirth.isEmpty() || dateOfBirth.isBlank() ||
					accountType.isEmpty() || accountType.isBlank()) {
				System.out.println("\n_____ Bank Application _____");
				System.out.println("Fill out this form.");
				System.out.print("Social Security Number (format: AAA-GG-SSSS): ");
				socialSecurityNumber = sc.nextLine();
				System.out.print("Date of Birth (fortmat: YYYY-MM-DD): ");
				dateOfBirth = sc.nextLine();
				System.out.print("Bank Account Type (choices: Checkings, Savings, and Joint): ");
				accountType = sc.nextLine();
				accountNumber = AccountNumberGenerator.getAccountNumber();
				
				if(!socialSecurityNumber.isEmpty() && !socialSecurityNumber.isBlank() &&
						!dateOfBirth.isEmpty() && !dateOfBirth.isBlank() &&
						!accountType.isEmpty() && !accountType.isEmpty()) {
					
					account = Account.objects.createAccount(socialSecurityNumber, dateOfBirth, accountType, accountNumber, 0.0, "Pending", false);
					//user.account = account;
					user.setAccount(account);
					user.save();
					
					System.out.println("\nYour applicatin has been submitted succesfully!");
					System.out.println("It takes 3-5 business days for an account to be approved.");
					System.out.println("You can login at anytime to check yor account status.");
				}else {
					System.out.println("\nSSN, DOB, and Bank Account Type cannot be empty. Please try again");
				}
				
			}
		}
	}
	
	boolean isPositiveDouble(String str) {
		//consoleLogger.debug("check is string is a positive double type");
		fileLogger.debug("Check if string is a positive doble type");
		
		double number;
		try {
			number = Double.parseDouble(str);
			if (number > 0) {
				return true;
			}else {
				return false;
			}
        } catch (NumberFormatException e) {
        	//consoleLogger.error(e.toString());
        	fileLogger.error(e.toString());
            return false;
        }
    }
	
	private void deposit(User user) {
		//consoleLogger.debug("User deposit");
		fileLogger.debug("User deposit");
		
		Scanner sc = this.sc;
		double depositAmount;
		Account account = user.getAccount();
		String userInput = "";
		
		if(user.getAccount() != null && user.getAccount().isActive()) {
			while(userInput.isEmpty() || userInput.isBlank() || !isPositiveDouble(userInput)) {
				System.out.println("\n_____ Deposit _____");
				System.out.println("Available Balance: $ " + user.getAccount().getBalance());
				System.out.print("Enter amount to deposit: ");
				
				userInput = sc.nextLine();
				if(!userInput.isEmpty() && !userInput.isBlank() && isPositiveDouble(userInput)) {
					depositAmount = Double.parseDouble(userInput);
					account.setBalance(account.getBalance() + depositAmount);
					account.save();
					System.out.println("Ending Balance: $ " + user.getAccount().getBalance());
				}else {
					System.out.println("\nYou must enter a valid number. Please try again.");
				}
			}
		}else {
			System.out.println("\nYou can't perform this operation because you don't have an account.");
		}
	}
	
	private void withdraw(User user) {
		//consoleLogger.debug("User withdraw");
		fileLogger.debug("User withdraw");
				
		String userInput = "";
		Scanner sc = this.sc;
		double withdrawAmount;
		Account account = user.getAccount();
		
		if(user.getAccount() != null && user.getAccount().isActive()) {
			while(userInput.isEmpty() || userInput.isBlank() || !isPositiveDouble(userInput)) {
				System.out.println("\n_____ Withdraw _____");
				System.out.println("Available Balance: $" + user.getAccount().getBalance());
				System.out.print("Enter amount to withdraw: ");
				userInput = sc.nextLine();
				if(!userInput.isEmpty() && !userInput.isBlank() && isPositiveDouble(userInput)) {
					withdrawAmount = Double.parseDouble(userInput);
					if(withdrawAmount <= account.getBalance()) {
						account.setBalance(account.getBalance() - withdrawAmount);
						account.save();
						System.out.println("Ending Balance: "+ account.getBalance());
					}else {
						System.out.println("\nNot enough funds to withdraw the amount entered.");
					}
				}else {
					System.out.println("\nYou must enter a valid number. Please try again.");
				}
			}
		}else {
			System.out.println("\nYou can't perform this operation because you don't have an account");
		}
	}
	
	private void transferFunds() {
		//consoleLogger.debug("Transfer funds");
		fileLogger.debug("Transfer funds");
				
		User user = Request.user;
		Scanner sc = this.sc;
		double transferAmount;
		String userInput;
		Account account = user.getAccount();
		
		if(user.getAccount() != null && user.getAccount().isActive()) {
			System.out.println("\n_____ Transfer Funds ______");
			System.out.println("Available Balance: $" + user.getAccount().getBalance());
			System.out.println("From: "+ user.getUsername());
			System.out.print("To: ");
			userInput = sc.nextLine();
			
			User toUser = User.objects.getUser(userInput);
			if(toUser != null && toUser.getAccount() != null && toUser.getAccount().isActive()) {
				System.out.print("Enter amount to trasfer: ");
				userInput = sc.nextLine();
				
				if(!userInput.isEmpty() && !userInput.isBlank() && isPositiveDouble(userInput)) {
					transferAmount = Double.parseDouble(userInput);
					if(transferAmount <= account.getBalance()) {
						account.setBalance(account.getBalance() - transferAmount);
						account.save();
						toUser.getAccount().setBalance(toUser.getAccount().getBalance() + transferAmount);
						toUser.getAccount().save();
						System.out.println("Ending Balance: "+ account.getBalance());
					}else {
						System.out.println("\nNot enough funds to tranfer the amount entered.");
					}
				}else {
					System.out.println("\nYou must enter a valid number. Please try again.");
				}
			}else {
				System.out.println("\nUnable to transfer funds!");
				System.out.println("Please type a username with a valid account.");
			}
		}else {
			System.out.println("\nYou can't perform this operation because you don't have an account");
		}
	}
	
	private void balance() {
		//consoleLogger.debug("Account balance");
		fileLogger.debug("Account balance");
				
		User user = Request.user;
		if(user.getAccount() != null && user.getAccount().isActive()) {
			System.out.println("\n_____ Balance _____");
			System.out.println("Balance: "+ user.getAccount().getBalance());
		}else {
			System.out.println("\nYou can't perform this operation because you don't have an account");
		}		
	}
	
	private void adminStaffLogin() {
		//consoleLogger.debug("Staff login");
		fileLogger.debug("Staff login");
				
		String username;
		String password;
		User user = null;
		Scanner sc = this.sc;
		
		while(user == null){
			System.out.println("\n_____ Admin/Staff Login _____");
			System.out.print("Username: ");
			username = sc.nextLine();
			System.out.print("Password: ");
			password = sc.nextLine();
			user = Backend.login(username, password);
			
			if(user == null) {
				System.out.println("\nYour username or password was wrong.");
				System.out.println("Please try again.");
			}else if (user != null && user.isAdmin() == false && user.isStaff() == false) {
				System.out.println("\nYou don't have the credentials to login in the Admin/Sfaff portal");
				user = null;
			}
		}
		
		if(user.isAdmin()) {
			Request.adminLogin = true;
			
		}else if(user.isStaff()) {
			Request.staffLogin = true;
		}
		
		Request.user = user;
		System.out.println("\nLogin Successful!");
	}
	
	
	public void staffOptions() {
		//consoleLogger.debug("Staff menu");
		fileLogger.debug("Staff menu");
		User user = Request.user;
		Scanner sc = this.sc;
		String userInput;
		userInput = "";
		
		while(!userInput.equals("1") && !userInput.equals("2")  && !userInput.equals("3")) {
			
			System.out.println("\nHello "+ user.getUsername() + "! What would you like to do?");
			System.out.println("1. View Customers");
			System.out.println("2. View Open Applications");
			System.out.println("3. Log out");
				
			userInput = sc.nextLine();
		}
			
		switch(userInput) {
			case "1":
				viewCustomers();
				break;
			case "2":
				viewOpenApplications();
				break;
			case "3":
				logout();
				break;
		}
	}
	
	private void viewCustomers() {
		//consoleLogger.debug("View customer's information");
		fileLogger.debug("View customer's information");
				
		Map<String, User> users = new HashMap<String, User>();
		users = User.objects.filterByAccountActive();
		Scanner sc = this.sc;
		String userInput = "";
		
		while(!userInput.equals("back")) {
			System.out.println("\n_____ Customers _____");
			for (String username: users.keySet()) {
				System.out.println(username);
			}
			
			System.out.println("\nPlease type a cutomer's username to view their information ");
			System.out.println("or type 'back' to go back to the previous menu.");
			System.out.print("Username: ");
			userInput = sc.nextLine();
			
			User user = users.get(userInput);
			if(user != null) {
				System.out.println("\n_____ Customer Information _____");
				System.out.println("Username: " + user.getUsername());
				System.out.println("Account Type: " + user.getAccount().getAccountType());
				System.out.println("Account Number: " + user.getAccount().getAccountNumber());
				System.out.println("Balance: " + user.getAccount().getBalance());
				System.out.println();
			}else if( user == null && !userInput.equals("back")){
				
				System.out.println("\n_____ Customer Information _____");
				System.out.println("No customer with this username: "+ userInput);
				System.out.println();
				
			}
		}
	}
	
	private void viewOpenApplications() {
		//consoleLogger.debug("View customer applications");
		fileLogger.debug("View customer applications");
				
		Map<String, User> users = new HashMap<String, User>();
		Scanner sc = this.sc;
		String userInput = "";
		
		while(!userInput.equals("back")) {
			users = User.objects.filterByAccountStatus("Pending");
			System.out.println("\n_____ Open Applications _____");
			for (String username: users.keySet()) {
				System.out.println(username);
			}
			
			System.out.println("\nPlease type a cutomer's username to view their information ");
			System.out.println("or type 'back' to go back to the previous menu.");
			System.out.print("Username: ");
			userInput = sc.nextLine();
			
			User user = users.get(userInput);
			if (user != null) {
				System.out.println("\n_____ Customer Information _____");
				System.out.println("Username: " + user.getUsername());
				System.out.println("Account Type: " + user.getAccount().getAccountType());
				System.out.println("Account Number: " + user.getAccount().getAccountNumber());
				System.out.println("Social Security Number: " + user.getAccount().getSocialSecurityNumber());
				System.out.println("Date Of Birth: " + user.getAccount().getDateOfBirth());
				
				System.out.println("\n Would you like to approve or deny this account?");
				System.out.println("1. Approve");
				System.out.println("2. Disapprove");
				System.out.println("3. Go Back");
				
				userInput = sc.nextLine();
				
				while(!userInput.equals("1") && !userInput.equals("2") && !userInput.equals("3")){
					
					System.out.println("\n Would you like to approve or deny this account?");
					System.out.println("1. Approve");
					System.out.println("2. Disapprove");
					System.out.println("3. Go Back");
					
					userInput = sc.nextLine();
					
				}
				if(userInput.equals("1")){
					user.getAccount().setStatus("Approved");
					user.getAccount().setActive(true);
					user.getAccount().save();
				}else if(userInput.equals("2")) {
					user.getAccount().setStatus("Denied");
					user.getAccount().setActive(false);
					user.getAccount().save();
				}
				
				
			}else if( user == null && !userInput.equals("back")){
				System.out.println("\n_____ Customer Information _____");
				System.out.println("No customer with this username: "+ userInput);
				System.out.println();
				
			}
		}
	}
	
	public void adminOptions() {
		//consoleLogger.debug("Admin menu");
		fileLogger.debug("Admin meun");
				
		User user = Request.user;
		Scanner sc = this.sc;
		String userInput = "";
		
		while(!userInput.equals("1") && !userInput.equals("2")  && !userInput.equals("3") &&
				!userInput.equals("4") && !userInput.equals("5")  && !userInput.equals("6") &&
				!userInput.equals("7") && !userInput.equals("8")) {
			
			System.out.println("\nHello "+ user.getUsername() + "! What would you like to do?");
			System.out.println("1. View All Accounts");
			System.out.println("2. Approve/Deny Accounts");
			System.out.println("3. Deposit to Accounts");
			System.out.println("4. Withdraw from Accounts");
			System.out.println("5. Transer to/from Accounts");
			System.out.println("6. Canceling Accounts");
			System.out.println("7. Change user permission");
			System.out.println("8. Log out");
				
			userInput = sc.nextLine();
		}
			
		switch(userInput) {
			case "1":
				viewAllAccounts();
				break;
			case "2":
				approveDenyAccounts();
				break;
			case "3":
				depositToAccounts();
				break;
			case "4":
				withdrawFromAccounts();
				break;
			case "5":
				transferToFromAccounts();
				break;
			case "6":
				cancelingAccounts();
				break;
			case "7":
				changeUserPermissions();
				break;
			case "8":
				logout();
				break;
			}
		
	}

	private void viewAllAccounts() {
		//consoleLogger.debug("View all accounts");
		fileLogger.debug("View all accounts");
				
		Map<String, User> users = new HashMap<String, User>();
		users = User.objects.filterByAccountAll();
		User user;
		Scanner sc = this.sc;
		String userInput = "";
		
		while(!userInput.equals("back")) {
			System.out.println("\n_____ Accounts _____");
			for (String username: users.keySet()) {
				user = users.get(username);
				System.out.println(username + " - " + user.getAccount().getStatus());
			}
			
			System.out.println("\nPlease type a user's username to view their information ");
			System.out.println("or type 'back' to go back to the previous menu.");
			System.out.print("Username: ");
			userInput = sc.nextLine();
			
			user = users.get(userInput);
			
			if (user != null) {
				System.out.println("\n_____ Account Information _____");
				
				System.out.println("Username: " + user.getUsername());
				System.out.println("Account Type: " + user.getAccount().getAccountType());
				System.out.println("Account Number: " + user.getAccount().getAccountNumber());
				System.out.println("Account Status: " + user.getAccount().getStatus());
				System.out.println("Social Security Number: " + user.getAccount().getSocialSecurityNumber());
				System.out.println("Date Of Birth: " + user.getAccount().getDateOfBirth());
				System.out.println();
				
				
			}else if( user == null && !userInput.equals("back")){
				
				System.out.println("\n_____ Account Information _____");
				System.out.println("No account with this username: "+ userInput);
				System.out.println();
				
			}
		}
	}
	
	private void approveDenyAccounts() {
		//consoleLogger.debug("Approve or deny accounts");
		fileLogger.debug("Approve or deny accounts");
				
		Map<String, User> users = new HashMap<String, User>();
		Scanner sc = this.sc;
		String userInput = "";
		User user;
		
		while(!userInput.equals("back")) {
			users = User.objects.filterByAccountAll();
			
			System.out.println("\n_____ Accounts _____");
			for (String username: users.keySet()) {
				user = users.get(username);
				System.out.println(username + " - " + user.getAccount().getStatus());
			}
			
			System.out.println("\nPlease type a user's username to view their information ");
			System.out.println("or type 'back' to go back to the previous menu.");
			System.out.print("Username: ");
			userInput = sc.nextLine();
			
			user = users.get(userInput);
			if (user != null) {
				System.out.println("\n_____ Account Information _____");
				System.out.println("Username: " + user.getUsername());
				System.out.println("Account Type: " + user.getAccount().getAccountType());
				System.out.println("Account Number: " + user.getAccount().getAccountNumber());
				System.out.println("Account Status: " + user.getAccount().getStatus());
				System.out.println("Social Security Number: " + user.getAccount().getSocialSecurityNumber());
				System.out.println("Date Of Birth: " + user.getAccount().getDateOfBirth());
				
				System.out.println("\n Would you like to approve or deny this account?");
				System.out.println("1. Approve");
				System.out.println("2. Deny");
				System.out.println("3. Go Back");
				
				userInput = sc.nextLine();
				
				while(!userInput.equals("1") && !userInput.equals("2") && !userInput.equals("3")){
					
					System.out.println("\n Would you like to approve or deny this account?");
					System.out.println("1. Approve");
					System.out.println("2. Disapprove");
					System.out.println("3. Go Back");
					
					userInput = sc.nextLine();
					
				}
				if(userInput.equals("1")){
					user.getAccount().setStatus("Approved");
					user.getAccount().setActive(true);
					user.getAccount().save();
				}else if(userInput.equals("2")) {
					user.getAccount().setStatus("Denied");
					user.getAccount().setActive(false);
					user.getAccount().save();
				}
				
			}else if( user == null && !userInput.equals("back")){
				
				System.out.println("\n_____ Account Information _____");
				System.out.println("No customer with this username: "+ userInput);
				System.out.println();
				
			}
		}
	}
	
	private void depositToAccounts() {
		//consoleLogger.debug("Deposit to all accounts");
		fileLogger.debug("Deposit to all accounts");
				
		Map<String, User> users = new HashMap<String, User>();
		users = User.objects.filterByAccountActive();
		User user;
		Scanner sc = this.sc;
		String userInput = "";
		
		while(!userInput.equals("back")) {
			System.out.println("\n_____ Accounts _____");
			for (String username: users.keySet()) {
				user = users.get(username);
				System.out.println(username);
			}
			
			System.out.println("\nPlease type an account's username to deposit");
			System.out.println("or type 'back' to go back to the previous menu.");
			System.out.print("Username: ");
			userInput = sc.nextLine();
			
			user = users.get(userInput);
			
			if (user != null) {
				deposit(user);
			}else if( user == null && !userInput.equals("back")){
				System.out.println("\nNo account with this username: "+ userInput);	
			}
			
		}
	}
	
	private void withdrawFromAccounts() {
		//consoleLogger.debug("Admin withdraw from all customers");
		fileLogger.debug("Admin withdraw from all customers");
				
		Map<String, User> users = new HashMap<String, User>();
		users = User.objects.filterByAccountActive();
		User user;
		Scanner sc = this.sc;
		String userInput = "";
		
		while(!userInput.equals("back")) {
			System.out.println("\n_____ Accounts _____");
			for (String username: users.keySet()) {
				user = users.get(username);
				System.out.println(username);
			}
			
			System.out.println("\nPlease type an account's username to withdraw");
			System.out.println("or type 'back' to go back to the previous menu.");
			System.out.print("Username: ");
			userInput = sc.nextLine();
			
			user = users.get(userInput);
			
			if (user != null) {
				withdraw(user);
			}else if( user == null && !userInput.equals("back")){
				System.out.println("\nNo account with this username: "+ userInput);	
			}
			
		}
	}
	
	private void transferToFromAccounts() {
		//consoleLogger.debug("Admin trasfer from to all accounts");
		fileLogger.debug("Admin transfer from to all accounts");
				
		Map<String, User> users = new HashMap<String, User>();
		users = User.objects.filterByAccountActive();
				User fromUser, toUser;
		Scanner sc = this.sc;
		String userInput = "";
		double transferAmount;
		
		while(!userInput.equals("back")) {
			System.out.println("\n_____ Accounts _____");
			for (String username: users.keySet()) {
				System.out.println(username);
			}
			
			System.out.println("\nPlease type From and To account's username to transfer funds");
			System.out.println("or type 'back' to go back to the previous menu.");

			
			System.out.print("\nFrom Account(Username): ");
			userInput = sc.nextLine();
			fromUser = users.get(userInput);
			
			if(fromUser != null) {
				System.out.print("To Account(Username): ");
				userInput = sc.nextLine();
				toUser = users.get(userInput);
				
				if(toUser != null) {
					System.out.print("Enter amount to trasfer: ");
					
					userInput = sc.nextLine();
					if(isPositiveDouble(userInput)) {
						transferAmount = Double.parseDouble(userInput);
						if(transferAmount <= fromUser.getAccount().getBalance()) {
							fromUser.getAccount().setBalance(fromUser.getAccount().getBalance() - transferAmount);
							fromUser.getAccount().save();
							toUser.getAccount().setBalance(toUser.getAccount().getBalance() + transferAmount);
							toUser.getAccount().save();
						}else {
							System.out.println("\nNot enough funds to tranfer the amount entered.");
						}
					}else {
						System.out.println("\nYou must enter a valid number. Please try again.");
					}
				}else {
					System.out.println("\nNo account with this username: "+ userInput);
				}
			}else {
				if(!userInput.equals("back")) {
					System.out.println("\nNo account with this username: "+ userInput);
				}
			}
		}
	}
	
	private void cancelingAccounts() {
		//consoleLogger.debug("Deleting accounts");
		fileLogger.debug("Deleting accounts");
				
		Map<String, User> users = new HashMap<String, User>();
		User user;
		Scanner sc = this.sc;
		String userInput = "";
		
		while(!userInput.equals("back")) {
			users = User.objects.filterByAccountActive();
			System.out.println("\n_____ Accounts _____");
			for (String username: users.keySet()) {
				user = users.get(username);
				System.out.println(username);
			}
			
			System.out.println("\nPlease type an account's username to cancel");
			System.out.println("or type 'back' to go back to the previous menu.");
			System.out.print("Username: ");
			userInput = sc.nextLine();
			
			user = users.get(userInput);
			
			if (user != null) {
				user.getAccount().delete();
				user.setAccount(null);
			}else if( user == null && !userInput.equals("back")){
				System.out.println("\nNo account with this username: "+ userInput);	
			}
			
		}
	}
	
	private void changeUserPermissions() {
		//consoleLogger.debug("Change user's permission");
		fileLogger.debug("Change user's permission");
				
		Map<String, User> users = new HashMap<String, User>();
		String userInput = ""; 
		User user;
		
		while(!userInput.equals("back")) {
			users = User.objects.all();
			System.out.println("\n_____ Accounts _____");
			for (String username: users.keySet()) {
				System.out.println(username);
			}
			
			System.out.println("\nPlease type the user's username to change permissions");
			System.out.println("or type 'back' to go back to the previous menu.");
			System.out.print("Username: ");
			userInput = sc.nextLine();
			
			user = users.get(userInput);
			
			if(user != null) {
				while(!userInput.equals("1") && !userInput.equals("2") && !userInput.equals("3")
						&& !userInput.equals("4") && !userInput.equals("5")){
					
					System.out.println("\n Add or Remove Permissions");
					System.out.println("1. Add staff permission");
					System.out.println("2. Add admin permission");
					System.out.println("3. Remove staff permission");
					System.out.println("4. Remove admin permission");
					System.out.println("5. Go Back");
					
					userInput = sc.nextLine();	
					
					if(userInput.equals("1")){
						user.setStaff(true);
						user.save();
					}else if(userInput.equals("2")) {
						user.setAdmin(true);
						user.save();
					}else if(userInput.equals("3")) {
						user.setStaff(false);
						user.save();
					}else if(userInput.equals("4")) {
						user.setAdmin(false);
						user.save();
					}
					
				}
			}else {
				System.out.println("\nPlease type a valid username");
			}
			
		}
		
	}
	
}

