package main.java.intro;


import main.java.data.Item;
import main.java.data.Person;
import main.java.data.StockRepository;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static main.java.data.PersonnelRepository.getAllPersons;
import static main.java.data.PersonnelRepository.isUserValid;

/**
 * Provides necessary methods to deal through the Warehouse management actions
 *
 * @author riteshp
 */
public class TheWarehouseManager extends StockRepository {
    // =====================================================================================
    // Member Variables
    // =====================================================================================
    private static final Person currentUser = new Person();

    private static final List<String> SESSION_ACTIONS = new ArrayList<>();
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
        this.statusCheck();
        this.greetUser();
        this.authorizations();
    }
public void statusCheck() {
if(this.confirm("Welcome to SWIP Warehouse Management System.\nAre you an employee? ")){
    this.seekUserName();
    this.seekPassword();
}else{
    currentUser.setUserName("Guest");
}
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
                            input + " is not a valid operation. Please enter a number between 1 and " +this.userOptions.length+"!\n" +
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

    private static void listSessionActions() {
        int i = 0;
        if(SESSION_ACTIONS.size() < 1){
            System.out.println("In this session you have not done anything.");
        }else {
            System.out.println("In this session you have:");
            for (String action : SESSION_ACTIONS) {
                i++;
                System.out.println("        "+i +". "+action);
            }
        }
    }

    /**
     * End the application
     */
    public void quit() {
        System.out.printf("\nThank you for your visit, %s!\n", currentUser.getUserName());
        listSessionActions();
        System.exit(0);
    }

    // =====================================================================================
    // Private Methods
    // =====================================================================================

    private boolean verifyUser(){
        return isUserValid(currentUser.getUserName(), currentUser.getPassword());
    }

    private void authorizations() {
        if(this.verifyUser()){
            System.out.println("You are authorized to place an order.");
        }else if(currentUser.getUserName().equals("Guest")){
            System.out.println("You are not authorized to place an order but you can still use the rest of the operations.");
        }else{
            System.out.println("Credentials not recognized. You are not authorized to place an order but you can still use the rest of the operations.");
        }
    }
    /**
     * Get user's name via CLI
     */
    private void seekUserName() {
        System.out.print("What is your user name? ");
        currentUser.setUserName(this.reader.nextLine());
    }

    private void seekPassword() {
        System.out.print("Please enter your password: ");
        currentUser.setPassword(reader.nextLine());
    }

    /**
     * Print a welcome message with the given user's name
     */
    private void greetUser() {
        System.out.printf("\nHello, %s!\n", currentUser.getUserName());
    }

    private void printNumberOfItemsByWarehouse() {
        System.out.println();
        for (int warehouse : getWarehouses()) {
            System.out.printf("Total items in warehouse %d: %d\n", warehouse, StockRepository.getItemsByWarehouse(warehouse).size());
        }
    }

    private void listItemsByWarehouse() {
        Set<Integer> warehouseNumbers = getWarehouses();
        for (int warehouse : warehouseNumbers) {
            System.out.printf("\nItems in warehouse %d:%n",warehouse);
            this.listItems(StockRepository.getItemsByWarehouse(warehouse));

        }
        printNumberOfItemsByWarehouse();
        SESSION_ACTIONS.add("Listed "+getTotalListedItems(StockRepository.getAllItems())+" items.");

    }


    private void listItems(List<Item> warehouse) {
        for (Item item : warehouse) {
            System.out.printf("- %s\n", item.toString());
        }
    }

    private int getTotalListedItems(List<Item> masterList){
        return masterList.size();
    }


    private void searchItemAndPlaceOrder() {
        String itemName = askItemToOrder();
        printAmountAvailable(itemName.toLowerCase());
        printLocations(itemName.toLowerCase());
        if (getAvailableAmount(itemName.toLowerCase()) > 0) {
            printMaximumAvailability(itemName.toLowerCase());
            SESSION_ACTIONS.add("Searched "+getAppropriateIndefiniteArticle(formattedItem(itemName))+ formattedItem(itemName)+".");
            askAmountAndConfirmOrder(getAvailableAmount(itemName.toLowerCase()), itemName);

        }

    }


    private boolean hasVowel(String itemName) {
        switch (itemName.charAt(0)) {
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
            case 'A':
            case 'E':
            case 'I':
            case 'O':
            case 'U':
                return true;
            default:
                return false;
        }
    }

    private String getAppropriateIndefiniteArticle(String itemName){
            return (hasVowel(itemName))?"an " : "a ";
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
        return reader.nextLine().trim();
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
        if(this.verifyUser()){
        boolean toOrder = this.confirm("\nWould you like to order this item? ");
        if (toOrder) {
                int orderAmount = this.getOrderAmount(availableAmount);
                if(orderAmount == 0){
                    return;
                }else{
                    System.out.printf("%d %s%s been ordered\n", orderAmount, formattedItem(item), (orderAmount == 1 ? " has" : checkPluralOrder(item.toLowerCase())));
                    if(orderAmount == 1){
                        SESSION_ACTIONS.add("Ordered "+orderAmount+" "+formattedItem(item)+".");
                    }else {
                        if (checkPluralName(item.toLowerCase())) {
                            SESSION_ACTIONS.add("Ordered " + orderAmount + " " + formattedItem(item) + ".");
                        } else {
                            SESSION_ACTIONS.add("Ordered " + orderAmount + " " + formattedItem(item) + "s.");
                        }
                    }
                }
            }
        }
    }

    private String formattedItem(String itemName) {
        return itemName.toUpperCase().charAt(0) + itemName.substring(1).toLowerCase();
    }

    private boolean checkPluralName(String itemName) {
        return itemName.endsWith("s");
    }

    private String checkPluralOrder(String itemName){
        if(checkPluralName(itemName)){
            return " have";
        }else{
            return "s have";
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

        SESSION_ACTIONS.add("Browsed the category "+menu.get(choice));
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

    private void printCategoryMenu(Map<Integer, String> menu){
        for(var entry : menu.entrySet()) {
            System.out.printf("%d. %s (%d)%n", entry.getKey(), entry.getValue(), amountOfItemsPerCategory(entry.getValue()));
        }
    }



    private int chooseCategory(){
        int result = 0;
        do{
            System.out.print("Type the number of the category to browse: ");
            while(!reader.hasNextInt()) { //repeat until a number is entered.
                String input = reader.nextLine();
                System.out.println("\n**************************************************\n" +
                        input + " is not a valid operation. Please enter a number between 1 and " + getCategories().size()+"!\n" +
                        "**************************************************");
                System.out.print("Type the number of the category to browse: ");
            }
            result = Integer.parseInt(this.reader.nextLine());
            if(result < 1 || result > getCategories().size()){
                System.out.println("Please enter a number between 1 and "+getCategories().size()+"!");
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