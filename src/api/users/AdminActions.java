package api.users;

import api.objects.Product;
import api.management.Inventory;

import java.util.ArrayList;

/**
 * @author Αχιλλέας
 * interface AdminActions has all the actions that a user Admin can do in the app
 * the class that will implement the user type Admin will have to implement this interface
 */
public interface AdminActions {
    boolean addProduct(Product product, Inventory inventory);

    boolean removeProduct(Product product ,Inventory inventory);

    void EditProduct(Product productToEdit, Product editedProduct, Inventory inventory);

    void updateQuantityOfProduct(Product product, Inventory inventory, double Quantity);

    ArrayList<Product> getUnavailableProducts(Inventory inventory);

    ArrayList<Product> getMostOrderProducts(Inventory inventory);
}
