package main.java.data;

import java.util.ArrayList;
import java.util.List;

public class Employee extends User{

    //Fields:
    private String password;
    private ArrayList<Employee> headOf;

    //Constructors:
    public Employee() {}

    public Employee(String userName,String password){
        super.name = userName;
        this.password = password;
    }
    public Employee(String userName, String password, ArrayList<Employee> headOf){
        super.name = userName;
        this.password = password;
        this.headOf = headOf;
    }

    //Getters:
    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    //Public Methods:
    @Override
    public boolean authenticate(String password){
        return (password.equals(this.password));
    }

    public void order(String name, int amount){
        System.out.printf("Ordered %d %s%s", amount, name,(amount == 1 ? "" : checkPluralOrder(name.toLowerCase())));

    }

    private String checkPluralOrder(String itemName){
        if(checkPluralName(itemName)){
            return "";
        }else{
            return "s";
        }
    }

    public boolean checkPluralName(String itemName) {
        return itemName.endsWith("s");
    }

    @Override
    public void greet(){
        if(this.getName().equals("Anonymous")){
            super.greet();
        }else{
            System.out.printf("Hello, %s!\n" +
                    "If you experience a problem with the system,\n" +
                    "please contact technical support.", name);
        }

    }

    public void bye(List actions) {
        super.bye();
        for(int i = 0; i < actions.size(); i++){
            System.out.printf("%d) %s\n", i+1, actions.get(i));
        }
    }

}
