package data;

public class Guest extends User{

    public Guest() {
        super();
    }

    @Override
    public boolean checkAuthenticated() {
        return false;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public void setPassword(String password) {}

    @Override
    public void order(String item, int orderAmount) {

    }


}
