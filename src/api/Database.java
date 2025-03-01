package api;

import api.management.Inventory;
import api.users.User;
import api.users.Admin;
import api.users.Customer;
import api.objects.Product;
import api.objects.Category;
import api.objects.SubCategory;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.*;

/**
 * The Database class is responsible for managing the inventory and user data
 * (including both admins and customers). It provides methods to initialize
 * these entities from external data files.
 */
public class Database {
    // The inventory of the system, containing categories, subcategories, and products.
    private Inventory inventory;

    // A collection of users, where the key is the username and the value is the User object.
    public HashMap<String, User> users;

    /**
     * Constructor for the Database class.
     * Initializes an empty inventory and user collection.
     */
    public Database() {
        inventory = new Inventory();
        users = new HashMap<>();
    }

    /**
     * @author Αχιλλέας
     * gives the reference of a user object by giving its username as the key to search in the hashmap
     * if the user doesn't exist in the hashmap it returns null
     * @param username the username of a user that will be the key to search in the hashmap
     * @return User object reference if the user exist, null otherwise
     */
    public User getUser(String username) {
        if (username == null)
            throw new IllegalArgumentException("Cannot access null string object");
        return users.get(username);
    }

    /**
     * Retrieves the inventory of the system.
     *
     * @return The inventory object.
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Saves the current inventory object to a file.
     * The inventory is serialized and written to "Files/InventoryData.dat".
     *
     * @throws IOException if there is an issue during file writing.
     * @throws ClassNotFoundException if the Inventory class is not found during serialization.
     */
    public void WriteInventoryObject(String filename) throws IOException, ClassNotFoundException {
        String filePath = "Files/InventoryData.dat";  // Define the file path to save the inventory data
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(inventory);  // Write the inventory object to the file
            System.out.println("Inventory object saved successfully to " + filename);  // Confirmation message
        }
    }

    /**
     * Reads the inventory object from a file and restores it.
     * The inventory is deserialized from "Files/InventoryData.dat".
     *
     * @throws IOException if there is an issue during file reading.
     * @throws ClassNotFoundException if the Inventory class is not found during deserialization.
     */
    public void ReadInventoryObject(String filename) throws IOException, ClassNotFoundException {
        String filePath = "Files/InventoryData.dat";  // Define the file path to read the inventory data
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            inventory = (Inventory) in.readObject();  // Deserialize the inventory object from the file
        }
    }

    /**
     * Saves the current users object to a file.
     * The users object is serialized and written to "Files/UsersData.dat".
     *
     * @throws IOException if there is an issue during file writing.
     * @throws ClassNotFoundException if the User class is not found during serialization.
     */
    public void WriteUsersObject(String filename) throws IOException, ClassNotFoundException {
        String filePath = "Files/UsersData.dat";  // Define the file path to save the users data
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(users);  // Write the users object to the file
            System.out.println("Users object saved successfully to " + filename);  // Confirmation message
        }
    }

    /**
     * Reads the users object from a file and restores it.
     * The users object is deserialized from "Files/UsersData.dat".
     *
     * @throws IOException if there is an issue during file reading.
     * @throws ClassNotFoundException if the User class is not found during deserialization.
     */
    public void ReadUsersObject(String filename) throws IOException, ClassNotFoundException {
        String filePath = "Files/UsersData.dat";  // Define the file path to read the users data
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            users = (HashMap<String, User>) in.readObject();  // Deserialize the users object from the file
        }
    }

    /**
     * Initializes the users from a specified file. The file should contain lines
     * in the format "username password", where usernames starting with "admin"
     * are treated as admins.
     *
     * @return A HashMap containing the initialized users.
     */
    public HashMap<String, User> InitialiseUsers() {
        String file = "Files/Users.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            // Read each line from the file
            while ((line = br.readLine()) != null) {
                String[] userInfo = line.split(" ");
                String userName = userInfo[0]; // Extract the username
                String password = userInfo[1]; // Extract the password
                // Determine user type based on the username
                if (userName.startsWith("admin")) {
                    users.put(userName, new Admin(userName, password));
                } else {
                    users.put(userName, new Customer(userName, password));
                }
            }
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
        return users;
    }
    /**
     * Initializes the inventory with categories, subcategories, and products
     * using data from specified files.
     * Retrieves the inventory of the system.
     *
     * @return The initialized inventory.
     * @return The inventory object.
     */
    public Inventory InitialiseInventory() {
        String categoryFile = "Files/categories_subcategories.txt";
        String productFile = "Files/products.txt";
        // Load categories and subcategories
        try (BufferedReader br = new BufferedReader(new FileReader(categoryFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line into category and subcategories
                String[] parts = line.split(" \\(");
                if (parts.length == 2) {
                    String categoryName = parts[0].trim(); // Extract the category name
                    String subCategoryNames = parts[1].replace(")", "").trim(); // Extract the subcategories
                    // Create a new Category object
                    Category category = new Category(categoryName);
                    // Split the subcategories and add them to the category
                    String[] subCategories = subCategoryNames.split("@");
                    for (String subCategoryName : subCategories) {
                        SubCategory subCategory = new SubCategory(subCategoryName.trim());
                        category.addSubCategory(subCategory);
                    }
                    // Add the category to the inventory
                    inventory.getCategoryManager().addCategory(category);
                }
            }
        } catch (Exception e) {
            System.out.println("Error occurred while reading categories: " + e.getMessage());
        }
        // Load products
        try (BufferedReader br = new BufferedReader(new FileReader(productFile))) {
            String line;
            String title = null, description = null, category = null, subCategory = null;
            double price = 0.0, quantity = 0.0;
            Product.MeasurementUnit unit = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Τίτλος:")) {
                    title = line.substring("Τίτλος:".length()).trim();
                } else if (line.startsWith("Περιγραφή:")) {
                    description = line.substring("Περιγραφή:".length()).trim();
                } else if (line.startsWith("Κατηγορία:")) {
                    category = line.substring("Κατηγορία:".length()).trim();
                } else if (line.startsWith("Υποκατηγορία:")) {
                    subCategory = line.substring("Υποκατηγορία:".length()).trim();
                } else if (line.startsWith("Τιμή:")) {
                    try {
                        String formattedPrice = line.substring("Τιμή:".length()).trim()
                                .replace(",", ".").replace("€", "");
                        price = Double.parseDouble(formattedPrice);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid price format: " + line);
                        price = 0.0;
                    }
                } else if (line.startsWith("Ποσότητα:")) {
                    try {
                        if(line.contains("kg"))
                            unit = Product.MeasurementUnit.KILOS;
                        else if (line.contains("τεμάχια"))
                            unit = Product.MeasurementUnit.PIECES;
                        String quantityString = line.substring("Ποσότητα:".length()).trim()
                                .replaceAll("[^\\d.]", "");
                        quantity = Double.parseDouble(quantityString);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid quantity format: " + line);
                        quantity = 0.0;
                    }
                    // Create and add the product after reading all details
                    Product product = new Product(title, description, category, subCategory, price, quantity, unit);
                    // Add the product to the appropriate category and subcategory
                    inventory.getProductManager().addProduct(product);

                }
            }
        } catch (Exception e) {
            System.out.println("Error occurred while reading products: " + e.getMessage());
        }
        return inventory;
    }
}
