package api.users;

import api.objects.*;
import api.management.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Αχιλλέας
 * Admin class implements the actions that an admin will have in the app, also serializable because admin will be stored in a binary file as an object.
 * Admin is responsible for modifing the products in real time,so he has direct access to them from the inventory. Also admin is a child of User class and
 * implements the search methods of it therefore he can search for products and have the fields username and password
 */
public class Admin extends User implements AdminActions, Serializable {

    private static final long serialVersionUID = 5456202344210053281L;

    /**
     * constructor of the Admin that uses polymorphism calling super from the User class constructor
     * @param userName the username of the Admin
     * @param password the password of the Admin
     */
    public Admin(String userName, String password) {
        super(userName, password, true);
    }

    /**
     *  adds the product to the inventory using its product manager
     * @param product the product that will be added to the inventory
     * @param inventory the inventory where the product will be added
     * @return true if the product was successfully added
     * @throws IllegalArgumentException if the one of the parameters given is null
     */
    @Override
    public boolean addProduct(Product product, Inventory inventory) {
        if (inventory == null)
            throw new IllegalArgumentException("Cannot access null inventory object");
        if (product == null)
            throw new IllegalArgumentException("Cannot access null product object");
        if (inventory.getProductManager().addProduct(product)) {
            return true;
        }
        return false;
    }

    /**
     * removes a product from the inventory using its product manager
     * @param product the product that will be removed from the inventory
     * @param inventory the inventory where the product was removed from
     * @return true if the product was successfully removed, false otherwise
     * @throws IllegalArgumentException if the one of the parameters given is null
     */
    @Override
    public boolean removeProduct(Product product, Inventory inventory) {
        if (inventory == null)
            throw new IllegalArgumentException("Cannot access null inventory object");
        if (product == null)
            throw new IllegalArgumentException("Cannot access null product object");
        if (inventory.getProductManager().removeProduct(product)) {
            return true;
        }
        return false;
    }

    /**
     * updates the quantity of a product in the inventory using its product manager
     * @param product the product that the quantity will be updated
     * @param inventory the inventory where the product is stored
     * @param quantity the new quantity that the product will have after the update
     * @throws IllegalArgumentException if one of the parameters is null
     */
    @Override
    public void updateQuantityOfProduct(Product product, Inventory inventory, double quantity) throws IllegalArgumentException {
        if (product == null)
            throw new IllegalArgumentException("Cannot access null product object");
        if (inventory == null)
            throw new IllegalArgumentException("Cannot access null inventory object");
        inventory.getProductManager().updateQuantityOfProduct(product, quantity);
    }

    /**
     * Edits an existing product in the inventory given the edited product details.
     * @param productToEdit the original product to edit
     * @param editedProduct  the updated product details
     * @param inventory    the inventory where the product resides
     * @throws IllegalArgumentException if one of the parameters is null
     */
    @Override
    public void EditProduct(Product productToEdit, Product editedProduct, Inventory inventory) {
        if (productToEdit == null || editedProduct == null)
            throw new IllegalArgumentException("Cannot access null product objects");
        if (inventory == null)
            throw new IllegalArgumentException("Cannot access null inventory object");

        //checking if the category or subcategory of the edited product exist so that if one of them or both doesn't we add them to the inventory
        //then we remove the product we want to edit from where it is and then update it with the details provided and then add it again in the correct place
        if (inventory.getCategoryManager().getCategory(editedProduct.getCategory()) == null) {
            inventory.getCategoryManager().addCategory(new Category(editedProduct.getCategory()));
            inventory.getCategoryManager().getCategory(editedProduct.getCategory()).addSubCategory(new SubCategory(editedProduct.getSubcategory()));
        }
        else if (inventory.getCategoryManager().getCategory(editedProduct.getCategory()).getSubCategory(editedProduct.getSubcategory()) == null) {
            inventory.getCategoryManager().getCategory(editedProduct.getCategory()).addSubCategory(new SubCategory(editedProduct.getSubcategory()));
        }
        removeProduct(productToEdit, inventory);
        productToEdit.setQuantityAvailable(editedProduct.getQuantityAvailable());
        productToEdit.setPrice(editedProduct.getPrice());
        productToEdit.setUnit(editedProduct.getUnit());
        productToEdit.setOrdersmade(editedProduct.getOrdersmade());
        productToEdit.setTitle(editedProduct.getTitle());
        productToEdit.setCategory(editedProduct.getCategory());
        productToEdit.setDescription(editedProduct.getDescription());
        productToEdit.setSubcategory(editedProduct.getSubcategory());
        addProduct(productToEdit, inventory);
    }


    /**
     * finds the products that their current quantity is 0 and adds them to a list
     * @param inventory the inventory that the products are stored
     * @return the list withe unavailable products
     * @throws IllegalArgumentException if the inventory is null
     */
    @Override
    public ArrayList<Product> getUnavailableProducts(Inventory inventory) {
        if (inventory == null)
            throw new IllegalArgumentException("Cannot access null inventory object");

        //loop through all products and add the products with zero quantity to the list
        ArrayList<Product> products = new ArrayList<>();
        for (Product p : inventory.getProductManager().getAllProducts(true)) {
            if (p.getQuantityAvailable() == 0)
                products.add(p);
        }
        return products;
    }

    /**
     * gets all the products of the inventory, sorts them by the orders that they have been to
     * and place them to an array list
     * @param inventory the inventory where the products are stored
     * @return the list with all the products
     * @throws IllegalArgumentException if the inventory is null
     */
    @Override
    public ArrayList<Product> getMostOrderProducts(Inventory inventory) {
        if (inventory == null) //null
            throw new IllegalArgumentException("Cannot access null inventory object");

        //getting all the products and sorting them by their orders that they have been in
        ArrayList<Product> tempProducts = inventory.getProductManager().getAllProducts(true);
        tempProducts.sort(Comparator.comparing(Product::getOrdersmade).reversed());
        return tempProducts;
    }

    /**
     * searching for products in the inventory given at least the product title, (category and subcategory are optional)
     * using the product manager. Admin has to take the references of the products so he can modify them, so this method returns
     * the references of the products stored and not a copy of them
     * @param title the title of the product we want to search for
     * @param category the category that the product is in
     * @param subcategory the subcategory that the product is in
     * @param inventory the inventory where the products are stored
     * @return a list of similar products given the parameters we provided
     * @throws IllegalArgumentException if the inventory is null
     */
    @Override
    public ArrayList<Product> searchProduct(String title, String category, String subcategory, Inventory inventory) {
        if (inventory == null)
            throw new IllegalArgumentException("Cannot access null inventory object");
        return inventory.getProductManager().searchProduct(title, category, subcategory, true);
    }

    /**
     * searching for products in the inventory given only the category and subcategory of the products using the product manager of the inventory
     * Admin has to take the references of the products so he can modify them, so this method returns the references of the products stored and not a copy of them
     * @param category the category that products are stored
     * @param subCategory the subcategory that products are stored
     * @param inventory the inventory where the products are store
     * @return a list with the products that are stored in the given category/subcategory
     * @throws IllegalArgumentException if the inventory is null
     */
    @Override
    public ArrayList<Product> searchProducts(String category, String subCategory, Inventory inventory) {
        if (inventory == null)
            throw new IllegalArgumentException("Cannot access null inventory object");
        return inventory.getProductManager().searchProducts(category, subCategory, true);
    }

}
