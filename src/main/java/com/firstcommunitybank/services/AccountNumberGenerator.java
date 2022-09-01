package com.firstcommunitybank.services;

import java.util.Random;

public class AccountNumberGenerator {
	
	public static String getAccountNumber() {

	    Random rnd = new Random();
	    int number = rnd.nextInt(999999999);

	    return String.format("%09d", number);
	}

}
