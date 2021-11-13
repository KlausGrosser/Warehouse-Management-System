package main.java.intro;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Provides necessary methods to deal through the Warehouse management actions
 *
 * @author riteshp
 */
public class TheWarehouseManager extends Repository {
    // =====================================================================================
    // Member Variables
    // =====================================================================================

    // To read inputs from the console/CLI
    private final Scanner reader = new Scanner(System.in);
    private final String[] userOptions = {
            "1. List items by warehouse", "2. Search an item and place an order", "3. Browse items by category", "4. Quit"
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
        int choice = 0;

            System.out.println("What would you like to do? ");
            for (String option : this.userOptions) {
                System.out.println(option);
            }

            System.out.print("Type the number of the operation: ");

            do {
                while(!reader.hasNextInt()) { //repeat until a number is entered.
                   String input = reader.nextLine();
                    System.out.println("\n**************************************************\n" +
                            input + " is not a valid operation. Please enter a number between 1 and 4!\n" +
                            "**************************************************");
                    System.out.print("Type the number of the operation: ");
                }
                choice = Integer.parseInt(this.reader.nextLine());

                if (choice < 1 || choice > this.userOptions.length) {
                    System.out.printf("Error! Please choose a number between 1 and %d: ", this.userOptions.length);
                }
            } while (choice < 1 || choice > this.userOptions.length);
            return choice;
    }

    /**
     * Initiate an action based on given option
     */
    public void performAction(int option) {
        switch (option) {
            case 1:
                this.listItemsByWarehouse();
                break;
            case 2:
                this.searchItemAndPlaceOrder();
                break;
            case 3:
                this.browseByCategory();
                break;
            case 4:
                this.quit();
                break;
            default:
                break;

        }
    }


    /**
     * Confirm an action
     *
     * @return action
     */
    public boolean confirm(String message) {
        String choice;
        do {
            System.out.printf("%s (y/n) ", message);
            choice = this.reader.nextLine();
            if (choice.length() > 0) {
                choice = choice.trim();
            }
            choice = choice.toLowerCase();
        } while (!choice.startsWith("y") && !choice.startsWith("n"));
        return choice.startsWith("y");
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
        this.userName = this.reader.nextLine();
    }

    /**
     * Print a welcome message with the given user's name
     */
    private void greetUser() {
        System.out.printf("\nHello, %s!\n", this.userName);
    }

    private void listItemsByWarehouse() {
        Set<Integer> warehouseNumbers = getWarehouses();
        for (int warehouse : warehouseNumbers) {
            System.out.printf("\nItems in warehouse %d:%n",warehouse);
            this.listItems(Repository.getItemsByWarehouse(warehouse));
        }
        printNumberOfItemsByWarehouse();
    }

    private void printNumberOfItemsByWarehouse() {
        System.out.println();
        for (int warehouse : getWarehouses()) {
            System.out.printf("Total items in warehouse %d: %d\n", warehouse, Repository.getItemsByWarehouse(warehouse).size());
        }
    }

    private void listItems(List<Item> warehouse) {
        for (Item item : warehouse) {
            System.out.printf("- %s\n", item.toString());
        }
    }


    private void searchItemAndPlaceOrder() {
        String itemName = askItemToOrder();
        printAmountAvailable(itemName);
        printLocations(itemName);
        if (getAvailableAmount(itemName) > 0) {
            printMaximumAvailability(itemName);
            askAmountAndConfirmOrder(getAvailableAmount(itemName), itemName);
        }
    }

    private void printMaximumAvailability(String item) {
        int itemsInW1 = find(item, 1);
        int itemsInW2 = find(item, 2);

        if(itemsInW1 < itemsInW2){
            System.out.printf("Maximum availability: %d in Warehouse %s\n", itemsInW2, 2);
        }else if(itemsInW1 > itemsInW2){
            System.out.printf("Maximum availability: %d in Warehouse %s\n", itemsInW1, 1);
        }else{
            System.out.printf("Maximum availability: %d in each Warehouse\n", itemsInW1);
        }
  }
//
    private void printAmountAvailable(String item) {
      System.out.println("Amount available: " + getAvailableAmount(item));
    }

    /**
     * Ask the user to specify an Item to Order
     *
     * @return String itemName
     */
    private String askItemToOrder() {
        System.out.print("\nWhat is the name of the item? : ");
        return reader.nextLine().toLowerCase().trim();
    }

    /**
     * Calculate total availability of the given item
     *
     * @param itemName itemName
     * @return integer availableCount
     */
    private int getAvailableAmount(String itemName) {
        int count = 0;
        for (int x : getWarehouses()) {
            count += find(itemName, x);
        }
        return count;
    }


    private void printLocations(String item) {
        if (getAvailableAmount(item) > 0) {
            System.out.println("Location:");
            for (Item items : getItemsByWarehouse(1)) {
                if ((items.getState() + " " + items.getCategory()).toLowerCase().equals(item)) {
                    System.out.println("- Warehouse " + items.getWarehouse() + " (in stock for " + daysSinceStocked(items) + " days.)");
                }
            }
            for (Item items : getItemsByWarehouse(2)) {
                if ((items.getState() + " " + items.getCategory()).toLowerCase().equals(item)) {
                    System.out.println("- Warehouse " + items.getWarehouse() + " (in stock for " + daysSinceStocked(items) + " days.)");
                }
            }
        } else {
            System.out.println("Location: Not in stock");
        }
    }

    private long daysSinceStocked(Item item) {
        return TimeUnit.DAYS.convert((new Date().getTime() - item.getDateOfStock().getTime()), TimeUnit.MILLISECONDS);
    }

    /**
     * Find the count of an item in a given warehouse
     *
     * @param item      the item
     * @param warehouse the warehouse
     * @return count
     */
    private int find(String item, int warehouse) {
        int count = 0;
        for (Item itemFound : getItemsByWarehouse(warehouse)) {
            if (item.equals(itemFound.getState().toLowerCase() + " " + itemFound.getCategory().toLowerCase())) {
                count++;
            }
        }
        return count;
    }

    /**
     * Ask order amount and confirm order
     */
    private void askAmountAndConfirmOrder(int availableAmount, String item) {

        boolean toOrder = this.confirm("\nWould you like to order this item? ");
        if (toOrder) {
            int orderAmount = this.getOrderAmount(availableAmount);
            if(orderAmount == 0){
                return;
            } else {
                System.out.printf("%d %s%s been ordered\n", orderAmount, item, (orderAmount == 1 ? " has" : "s have"));
                //System.out.println("Please try again");
            }
        }
    }


    private int getOrderAmount(int availableAmount) {
        int orderAmount = -1;

        System.out.print("How many would you like? ");
        do {
            while(!reader.hasNextInt()) {
                reader.nextLine();
                System.out.print("Please enter a number: ");
            }
            orderAmount = Integer.parseInt(this.reader.nextLine());

            if (orderAmount > availableAmount) {
                System.out.println("**************************************************\n" +
                        "There are not this many available. The maximum amount that can be ordered is: " + availableAmount +
                        "\n**************************************************");

                boolean orderAll = this.confirm("Would you like to order the maximum available?");
                    if (orderAll) {
                        orderAmount = availableAmount;
                        } else {
                            boolean keepOrdering = this.confirm("Would you like to order another amount of this item?");

                            if (keepOrdering) {
                             System.out.print("How many would you like? ");
                             orderAmount = -1;
                             } else {
                                orderAmount = 0;
                                }
                            }
            } else if (orderAmount < 0) {
                System.out.print("Error!! the entered amount must be greater than 0. Please try again: ");
            } else {
                return orderAmount;
            }
        } while (orderAmount < 0 || orderAmount > availableAmount);
        return orderAmount;
    }


    private void browseByCategory() {
        Map<Integer, String> menu = createCategoryMenu();
        System.out.println("\nCategories: ");
        printCategoryMenu(menu);
        int choice = chooseCategory();
        printCategoryItems(choice, menu);
    }


    private int amountOfItemsPerCategory(String category){
        return getItemsByCategory(category).size();
    }

    private Map<Integer, String> createCategoryMenu(){
        Map<Integer, String> result = new LinkedHashMap<>();
        int count = 1;
        for(String category: getCategories()){
            result.put(count, category);
            count++;
        }
        return result;
    }

    private void printCategoryMenu(Map<Integer, String> x){
        for(var entry : x.entrySet()) {
            System.out.printf("%d. %s (%d)%n", entry.getKey(), entry.getValue(), amountOfItemsPerCategory(entry.getValue()));
        }
    }

    private int chooseCategory(){
        int result = 0;
        do{
            System.out.print("Type the number of the category to browse: ");
            result = Integer.parseInt(this.reader.nextLine());
            if(result < 1 || result > getCategories().size()){
                System.out.println("You have typed an incorrect number.");
            }
        }while(result < 1 || result > getCategories().size());
        return result;
    }

    private void printCategoryItems(int choice, Map<Integer, String> x){
        String category = x.get(choice);
        System.out.printf("\nList of %ss available:%n", category);
        for (Item item: getItemsByCategory(category)){
            System.out.printf("%s %s, Warehouse %d%n", item.getState(), category, item.getWarehouse());
        }
    }
}