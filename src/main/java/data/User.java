package main.java.data;

import java.util.List;

public class User {

    //Fields:
    protected String name = "Anonymous";
    private boolean isAuthenticated = false;

    //Constructors:
    public User() {}
    public User(String userName){
        this.name = userName;
    }
    //Getters:
    public String getName() {
        return this.name = name;
    }

    public void setName(String name){
        this.name = name;
    }

    public boolean getAuthenticated() {
        return this.isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.isAuthenticated = authenticated;
    }

    //public methods:
    public boolean authenticate(String password) {
        return false;
    }

    public boolean isNamed(String name) {
        return (name.equals(this.name));
    }

    public void greet(){
        System.out.printf("Hello, %s!\n" +
                "Welcome to our Warehouse Database.\n" +
                "If you don't find what you are looking for,\n" +
                "please ask one of our staff members to assist you.",this.name);
    }

    public void bye(){
        System.out.printf("\nThank you for your visit %s!\n", this.name);

    }
}
