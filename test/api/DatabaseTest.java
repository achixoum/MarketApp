package api;

import api.management.Inventory;
import api.objects.Category;
import api.objects.Product;
import api.objects.SubCategory;
import api.users.User;
import api.users.Admin;
import api.users.Customer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;

public class DatabaseTest {

    private Database database;

    @Before
    public void setUp() {
        // Create a new Database instance before each test
        database = new Database();
    }

    @Test
    public void testInitialiseUsers() {
        // Assume "Files/Users.txt" contains sample data for users
        HashMap<String, User> users = database.InitialiseUsers();

        // Check if the users are correctly initialized
        assertNotNull("Users should not be null", users);
        assertTrue("There should be at least one user", users.size() > 0);
        assertNotNull("Admin user should be found", users.get("admin1"));
        assertTrue("Admin user should be of type Admin", users.get("admin1") instanceof Admin);
    }

    @Test
    public void testInitialiseInventory() {
        // Assume "Files/categories_subcategories.txt" and "Files/products.txt" contain sample data
        Inventory inventory = database.InitialiseInventory();

        // Check if the inventory is correctly initialized
        assertNotNull("Inventory should not be null", inventory);
        assertTrue("Inventory should contain categories", inventory.getCategoryManager().getAllCategories(false).size() > 0);
        assertTrue("There should be at least one product", inventory.getProductManager().getAllProducts(true).size() > 0);
    }

    @Test
    public void testWriteAndReadInventoryObject() throws IOException, ClassNotFoundException {
        // Initialize the inventory
        Inventory inventory = database.InitialiseInventory();

        // Write the inventory object to a file
        database.WriteInventoryObject("Files/InventoryDataTest.dat");

        // Read the inventory object from the file
        database.ReadInventoryObject("Files/InventoryDataTest.dat");

        // Check if the inventory object was correctly restored
        assertNotNull("Inventory should be restored from the file", database.getInventory());
    }

    @Test
    public void testWriteAndReadUsersObject() throws IOException, ClassNotFoundException {
        // Initialize the users
        database.InitialiseUsers();

        // Write the users object to a file
        database.WriteUsersObject("Files/UsersDataTest.dat");

        // Read the users object from the file
        database.ReadUsersObject("Files/UsersDataTest.dat");

        // Check if the users object was correctly restored
        assertNotNull("Users should be restored from the file", database.getUser("admin1"));
        assertTrue("User should be an instance of Admin", database.getUser("admin1") instanceof Admin);
    }

    @Test
    public void testGetUser() {
        // Initialize users
        database.InitialiseUsers();

        // Test retrieving an existing user
        User user = database.getUser("admin1");
        assertNotNull("Admin user should exist", user);
        assertTrue("User should be an instance of Admin", user instanceof Admin);

        // Test retrieving a non-existing user
        User nonExistentUser = database.getUser("nonexistentUser");
        assertNull("Non-existent user should return null", nonExistentUser);
    }

    @Test
    public void testGetInventory() {
        // Initialize the inventory
        database.InitialiseInventory();

        // Check if the inventory is correctly retrieved
        Inventory inventory = database.getInventory();
        assertNotNull("Inventory should not be null", inventory);
    }

    @Test
    public void testWriteAndReadUsersObjectWithExceptionHandling() {
        try {
            // Try writing and reading the users object with invalid data
            database.WriteUsersObject("Files/UsersDataTest.dat");
            database.ReadUsersObject("Files/UsersDataTest.dat");
        } catch (IOException | ClassNotFoundException e) {
            fail("IOException or ClassNotFoundException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testWriteAndReadInventoryObjectWithExceptionHandling() {
        try {
            // Try writing and reading the inventory object with invalid data
            database.WriteInventoryObject("Files/InventoryDataTest.dat");
            database.ReadInventoryObject("Files/InventoryDataTest.dat");
        } catch (IOException | ClassNotFoundException e) {
            fail("IOException or ClassNotFoundException occurred: " + e.getMessage());
        }
    }

}
