package intro;

import java.util.ArrayList;
import java.util.List;

import data.*;

public class TheWarehouseApp {

    //Fields:
    /**
     * Execute the <i>TheWarehouseApp</i>
     *
     * @param args
     */
    public static List<String> SESSION_ACTIONS = new ArrayList<>();
    public static boolean IS_EMPLOYEE = false;
    public static User SESSION_USER = new Guest();

    //Main method:
    public static void main(String[] args) {

        TheWarehouseManager theManager = new TheWarehouseManager();

        // Welcome User
        theManager.welcomeUser();
        SESSION_USER.setPassword("");
        String password = SESSION_USER.getPassword();
        // Get the user's choice of action and perform action
        do {
            if(!UserRepository.isUserAdmin(SESSION_USER.getName())) {
                if(UserRepository.isUserEmployee(SESSION_USER.getName())){
                    IS_EMPLOYEE = true;
                    if(SESSION_USER.getPassword().isEmpty()){
                        password = theManager.seekPassword();
                    }
                    if(UserRepository.isUserValid(SESSION_USER.getName(),password)){
                        SESSION_USER.setAuthenticated(true);
                    }

                }
                int choice = theManager.getUsersChoice();
                theManager.performAction(choice);

            } else { //If user is admin

                // prompt for password and allow further actions if authenticated:

                AdminServiceImpl adminService = new AdminServiceImpl();

                do {
                    if (!SESSION_USER.checkAuthenticated()) {
                        adminService.authenticateAdmin();
                    }
                    if(!SESSION_USER.checkAuthenticated()){
                    if (!theManager.confirm("\nWrong password!\nDo you want to try again?")) {
                        theManager.quit();
                    }
                    }
                }while (!SESSION_USER.checkAuthenticated());

                int choice = adminService.getAdminsChoice();

                adminService.performAction(choice);

            }
            // confirm to do more
            if (!theManager.confirm("\nDo you want to perform another action?")) {
                theManager.quit();
            }

        } while (true);
    }
}