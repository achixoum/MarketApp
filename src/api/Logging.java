package api;

import java.io.*;
import api.users.Customer;
import api.users.User;
import api.Database;

public class Logging {

    // Constructor - Initializes the Logging class
    public Logging() {
    }

    /**
     * Sign-in method that checks if a user exists in the UsersData.dat file with the provided credentials.
     *
     * @param username The username of the user attempting to sign in.
     * @param password The password of the user attempting to sign in.
     * @param database The database object that manages user data.
     * @return true if the username exists and the password matches, false otherwise.
     */
    public boolean signIn(String username, String password, Database database, String filename) throws IllegalArgumentException {
        // Check if the users HashMap is empty (i.e., data has not been loaded yet)
        if (database.users.isEmpty()) {
            try {
                // Load users from the UsersData.dat file if they have not been loaded already
                database.ReadUsersObject(filename);
            } catch (IOException | ClassNotFoundException e) {
                // Handle errors related to reading users from file
                System.err.println("Error loading users from file: " + e.getMessage());
                return false;  // Return false if there is an issue loading users
            }
        }

        // Retrieve the user object from the database using the provided username
        User user = database.users.get(username);

        // If the user exists and the provided password matches the stored password, return true
        if (user != null && user.getPassword().equals(password)) {
            return true;
        }

        // Return false if the user does not exist or the password does not match
        return false;
    }

    /**
     * Sign-up method to create a new customer and add them to the users HashMap.
     *
     * @param username  The desired username of the new user.
     * @param password  The password of the new user.
     * @param firstName The first name of the new user.
     * @param lastName  The last name of the new user.
     * @param database  The database object that manages user data.
     * @return true if the sign-up is successful, false if the username already exists or an error occurs.
     */
    public boolean signUp(String username, String password, String firstName, String lastName, Database database, String filename) throws IllegalArgumentException {
        // Check if the users HashMap is empty (i.e., data has not been loaded yet)
        if (database.users.isEmpty()) {
            try {
                // Load users if not already loaded
                database.ReadUsersObject(filename);
            } catch (IOException | ClassNotFoundException e) {
                // Handle errors related to reading users from file
                System.out.println("Error loading users from file: " + e.getMessage());
                return false;  // Return false if there is an issue loading users
            }
        }

        // Check if the username already exists in the database
        if (database.getUser(username) != null) {
            // Inform the user if the username is already taken
            return false;  // Return false if the username is already taken
        }

        // Create a new Customer object with the provided details
        Customer newCustomer = new Customer(username, password);
        newCustomer.setFirstName(firstName);
        newCustomer.setLastName(lastName);

        // Add the new customer to the users HashMap
        database.users.put(username, newCustomer);

        // Attempt to save the updated users data to the file
        try {
            database.WriteUsersObject(filename);
            return true;  // Return true to indicate success
        } catch (IOException | ClassNotFoundException e) {
            // Handle errors related to saving users data to file
            System.out.println("Error saving users to file: " + e.getMessage());
            return false;  // Return false if there is an error saving user data
        }
    }

}
