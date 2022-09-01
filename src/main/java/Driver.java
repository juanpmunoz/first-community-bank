import java.util.Scanner;

import com.firstcommunitybank.controller.Request;
import com.firstcommunitybank.controller.UserController;
import com.firstcommunitybank.services.models.User;

public class Driver {

	public static void main(String[] args) {
		
		User user;
		boolean isAdmin;
		boolean isStaff;
		
		Scanner sc = new Scanner(System.in);
		UserController userController = new UserController(sc);
		
		
		while(true) {
			user = Request.user;
			isAdmin = Request.adminLogin;
			isStaff = Request.staffLogin;
			
			userController.createAdmin();
			if(user != null && user.isAuthenticated() && isAdmin) {
				userController.adminOptions();
			}else if(user != null && user.isAuthenticated() && isStaff) {
				userController.staffOptions();
			}else if(user != null && user.isAuthenticated()) {
				userController.options();
			}
			else {
				userController.loginOrRegister();
			}
		}
		
		//sc.close();

	}

}
