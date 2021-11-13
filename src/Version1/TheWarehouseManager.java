package Version1;

import java.util.Objects;
import java.util.Scanner;

/**
 * Provides necessary methods to deal through the Warehouse management actions
 *
 * @author riteshp
 */
public class TheWarehouseManager implements Repository {
    // =====================================================================================
    // Member Variables
    // =====================================================================================

    // To read inputs from the console/CLI
    private final Scanner reader = new Scanner(System.in);
    private final String[] userOptions = {
            "1. List items by warehouse", "2. Search an item and place an order", "3. Quit"
    };
    // To refer the user provided name.
    private String userName;

    // =====================================================================================
    // Public Member Methods
    // =====================================================================================

    /**
     * Welcome User
     */
    public void welcomeUser() {
        this.seekUserName();
        this.greetUser();
    }

    /**
     * Ask for user's choice of action
     */
    public int getUsersChoice() {
        System.out.println("What would you like to do?");
        for (String s : userOptions) {
            System.out.println(s);
        }
        System.out.print("Type the number of the operation: ");
        return reader.nextInt();
    }

    /**
     * Initiate an action based on given option
     */
    public void performAction(int option) {
        switch (option) {
            case 1:
                listItemsByWarehouse();
                break;
            case 2:
                String itemName = askItemToOrder();
                if (itemName.equals("")) {
                    System.out.println("Please enter an item name");
                }
                else {
                    searchItemAndPlaceOrder(itemName);
                }
                break;
            case 3:
                quit();
            default:
                System.out.println("\n**************************************************\n" +
                        option+" is not a valid operation.\n" +
                        "**************************************************");

        }
    }

    /**
     * Confirm an action
     *
     * @return action
     */
    public boolean confirm(String message) {
        boolean action;
        System.out.print(message);
        String answer = reader.next();
        action = answer.toLowerCase().charAt(0) == 'y';
        return action;
    }

    /**
     * End the application
     */
    public void quit() {
        System.out.printf("\nThank you for your visit, %s!\n", this.userName);
        System.exit(0);
    }

    // =====================================================================================
    // Private Methods
    // =====================================================================================

    /**
     * Get user's name via CLI
     */
    private void seekUserName() {
        System.out.print("What is your user name? ");
        userName = reader.nextLine();
    }

    /**
     * Print a welcome message with the given user's name
     */
    private void greetUser() {
        System.out.printf("\nHello, %s!\n", this.userName);
    }

    private void listItemsByWarehouse() {
        System.out.println("Items in warehouse 1:");
        listItems(WAREHOUSE1);
        System.out.println("\nItems in warehouse 2:");
        listItems(WAREHOUSE2);
        quit();
    }

    private void listItems(String[] warehouse) {
        for (String s : warehouse) {
            System.out.println("- " + s);
        }
    }


    private void searchItemAndPlaceOrder(String itemName) {
        System.out.println("Amount available: " + getAvailableAmount(itemName));
        getOrderLocation(itemName);
        askAmountAndConfirmOrder(getAvailableAmount(itemName), itemName);
    }

    /**
     * Ask the user to specify an Item to Order
     *
     * @return String itemName
     */
    private String askItemToOrder() {
        System.out.print("\nWhat is the name of the item? ");
        return reader.nextLine()+reader.nextLine();
    }

    /**
     * Calculate total availability of the given item
     *
     * @param itemName itemName
     * @return integer availableCount
     */
    private int getAvailableAmount(String itemName) {
        return find(itemName,WAREHOUSE1)+find(itemName,WAREHOUSE2);
    }

    /**
     * Find the count of an item in a given warehouse
     *
     * @param item the item
     * @param warehouse the warehouse
     * @return count
     */
    private int find(String item, String[] warehouse) {
        int count = 0;
        for (String s : warehouse) {
            if (Objects.equals(s, item)) {
                count++;
            }
        }
        return count;
    }

    /** Ask order amount and confirm order */
    private void askAmountAndConfirmOrder(int availableAmount, String item) {
        if(confirm("\nWould you like to order this item?(y/n) ")) {
            System.out.print("How many would you like? ");
            int inputChoice = reader.nextInt();
            if (inputChoice <= availableAmount) {
                System.out.println(inputChoice + " " + item + " have been ordered");
            } else {
                System.out.println("**************************************************\n" +
                        "There are not this many available. The maximum amount that can be ordered is: " + availableAmount +
                        "\n**************************************************");

                if (confirm("Would you like to order the maximum available?(y/n) ")) {
                    System.out.println(availableAmount + " " + item + " have been ordered");
                } else {
                    quit();
                }
            }
        }
        else {
            quit();
        }
    }


    private void getOrderLocation(String itemName) {

        int amountInW1 = find(itemName,WAREHOUSE1);
        int amountInW2 = find(itemName,WAREHOUSE2);
        boolean insideOfW1 = amountInW1 != 0;
        boolean insideOfW2 = amountInW2 != 0;

        if (insideOfW1 && insideOfW2) {
            System.out.println("Location: Both warehouses");
            if (amountInW1 == amountInW2) {
                System.out.println("Maximum availability: " + amountInW1 + " in both warehouses");
            }
            else if (amountInW1 > amountInW2) {
                System.out.println("Maximum availability: " + amountInW1 + " in Warehouse 1");
            }
            else {
                System.out.println("Maximum availability: " + amountInW2 + " in Warehouse 2");

            }
        }
        else if (insideOfW1){
            System.out.println("Location: Warehouse 1");
        }
        else if (insideOfW2){
            System.out.println("Location: Warehouse 2");
        }
        else {
            System.out.println("Location: Not in stock");
            quit();
        }
    }
}