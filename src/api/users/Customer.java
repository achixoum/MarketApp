package api.users;
import java.io.Serializable;
import api.objects.*;
import api.management.*;
import api.Database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

/**
 * The Customer class represents a user type that can interact with the e-shop market.
 * Customers can manage their cart, place orders, and view order history.
 */
public class Customer extends User implements CustomerActions,Serializable {

    private static final long serialVersionUID = 6258068009032576790L;
    private Cart cart; // The customer's shopping cart
    private List<Order> orderHistory; // List of completed orders
    private String FirstName;
    private String LastName;

    /**
     * Constructor to initialize a new Customer with a username and password.
     *
     * @param userName The customer's username.
     * @param password The customer's password.
     */
    public Customer(String userName, String password) {
        super(userName, password, false);
        this.cart = new Cart();
        this.orderHistory = new ArrayList<>();
    }

    public void setFirstName(String firstName) {FirstName = firstName;}
    public void setLastName(String lastName) {LastName = lastName;}
    public String getFirstName() {return FirstName;}
    public String getLastName() {return LastName;}

    public double getQuantityAvailableFromCart(Product product) {

        return cart.getCartItem(product.getTitle()).getQuantity();

    }

    /**
     * Adds a product to the cart.
     *
     * @param product  The product to add.
     * @param quantity The quantity of the product to add.
     */
    @Override
    public void addToCart(Product product, double quantity) throws IllegalArgumentException {
            cart.addProduct(product, quantity);
    }

    /**
     * Updates the quantity of a product in the cart.
     *
     * @param product  The product to update.
     * @param quantity The new quantity of the product.
     */
    @Override
    public void updateCart(Product product, double quantity) throws IllegalArgumentException {
            cart.updateQuantity(product.getTitle(), quantity);
    }

    /**
     * Removes a product from the cart.
     *
     * @param product The product to remove.
     */
    @Override
    public void removeFromCart(Product product) throws IllegalArgumentException {
            cart.removeProduct(product.getTitle());
    }

    /**
     * Views the order history of the customer.
     *
     * @return A list of copies of the customer's completed orders to ensure immutability.
     */
    @Override
    public List<Order> viewOrderHistory() {
        if (orderHistory.isEmpty()) {
            System.out.println("No orders have been placed yet.");
        }
        // Create a new list of copies of the orders
        List<Order> orderCopies = new ArrayList<>();
        for (Order order : orderHistory) {
            orderCopies.add(new Order(order)); // Assuming a copy constructor in Order class
        }
        return orderCopies;
    }

    /**
     * Completes the current cart as an order and updates the order history.
     *
     * @return The completed order.
     */
    @Override
    public Order completeOrder(Inventory inventory) throws Exception {
        if (inventory == null) {
            throw new Exception("Inventory access failed. Cannot complete the order.");
        }

        Order order = new Order(cart);

        // Check product availability and update inventory
        if (order.completeOrder(inventory)) {
            order.setDate(LocalDateTime.now()); // Set the order date to now
            orderHistory.add(order); // Add to the customer's order history
            cart.clearCart(); // Clear the cart after order completion
            return order;
        } else {
            throw new Exception("Order could not be completed. Insufficient stock for one or more products.");
        }
    }

    /**
     * Displays the current cart contents.
     */
    public void viewCart() {
        cart.displayCart();
    }

    /**
     * Clears the cart entirely.
     */
    public void clearCart() {
        cart.clearCart();
    }

    /**
     * Gets the customer's shopping cart.
     *
     * @return The customer's cart.
     */
    public Cart getCart() {
        return cart;
    }

    /**
     * Gets the customer's order history.
     *
     * @return The list of completed orders.
     */
    public List<Order> getOrderHistory() {
        return orderHistory;
    }

    // Implement the searchProduct method from the User class to search for a single product
    @Override
    public ArrayList<Product> searchProduct(String title, String category, String subcategory, Inventory inventory) {
        if (inventory ==  null)
            throw new IllegalArgumentException("Cannot access null inventory object");
        return inventory.getProductManager().searchProduct(title, category, subcategory, false);
    }

    // Implement the searchProducts method from the User class to search for multiple products
    @Override
    public ArrayList<Product> searchProducts(String category, String subCategory, Inventory inventory) {
        if (inventory ==  null)
            throw new IllegalArgumentException("Cannot access null inventory object");
        return inventory.getProductManager().searchProducts(category, subCategory, false); // Assuming Inventory has a searchProducts method
    }

}
