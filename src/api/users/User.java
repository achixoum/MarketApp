package api.users;
import java.io.Serializable;

import api.Database;
import api.management.Inventory;
import api.objects.Product;

import java.util.ArrayList;
import java.util.Objects;

/**
 * User class is the higher in hierarchy for the users type in our market app,
 * represents a user type that will have an account in the app and gets inherited
 * from all the user type that the app will have (admin - customer)
 */
//this class represents a user type that will have an account in our e-shop market
public abstract class User implements Serializable{

    private static final long serialVersionUID = 1L;
    //the username that the user will log in with
    String userName;
    //the password that the user will log in with
    String password;
    //boolean value that checks if the user is admin or not
    boolean isAdmin;

    /**
     * constructor of the user
     * @param userName the username of the user
     * @param password the password of the user
     * @param isAdmin true if the user is admin, false otherwise
     */
    public User(String userName, String password, boolean isAdmin) {
        this.userName = userName;
        this.password = password;
        if((Objects.equals(userName, "admin1") && Objects.equals(password, "password1")) || (Objects.equals(userName, "admin2") && Objects.equals(password, "password2"))) {
            isAdmin = true;
        }
        else {
            isAdmin = false;
        }
    }

    public boolean getIsAdmin() {return isAdmin;}


    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public abstract ArrayList<Product> searchProduct (String title, String category, String subcategory, Inventory inventory);

    public abstract ArrayList<Product> searchProducts (String category, String subCategory, Inventory inventory);

}
