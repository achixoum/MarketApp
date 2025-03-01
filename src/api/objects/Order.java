package api.objects;

import api.management.Inventory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @author Αχιλλέας
 * Represents an Order made by a customer, containing a cart of items, the date the order was made, and the total cost of the order.
 * This class implements Serializable for persistence.
 */
public class Order implements Serializable {
    //the date we made the order
    private static final long serialVersionUID = 7L;
    private LocalDateTime date;
    private Cart cart;
    private double totalCost;

    /**
     * Constructor that create a new Order based on the provided cart.
     * The order date is set to the current date and time.
     * @param cart the cart to associate with this order. A deep copy is made.
     */
    public Order(Cart cart) {
        date = LocalDateTime.now();
        if (cart != null) {
            this.cart = new Cart(cart);
            totalCost = cart.getTotalCost();
        }
    }

    /**
     * copy constructor of the order that deep copies the given order
     * @param order the order to copy
     */
    public Order(Order order) {
        this.date = order.getDate();
        this.cart = new Cart(order.getCart());
        this.totalCost = order.getTotalCost();
    }


    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Cart getCart() {
        return cart;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public List<CartItem> getItemsOfOrder() {
        return cart.getCart();
    }

    /**
     * Completes the order by checking product availability and updating inventory.
     * @param inventory the inventory to update with the order.
     * @return true if the order is completed successfully, false otherwise.
     * @throws IllegalArgumentException if the inventory is null.
     */
    public boolean completeOrder(Inventory inventory) {
        if (inventory == null) {
            throw new IllegalArgumentException("Cannot access null invetory object");
        }
        //checks for every product in the cart for its availability if is not available method returns false and can't complete order
        //if it is updates the quantity of the product in the inventory
        //if (getItemsOfOrder().isEmpty())
          //  return false;
        for (CartItem item : getItemsOfOrder()) {
            Product product = item.getProduct();
            double quantity = item.getQuantity();
            if (!inventory.getProductManager().isProductAvailable(product,quantity))
                return false;
            inventory.getProductManager().updateQuantityOfProduct(item.getProduct(),item.getProduct().getQuantityAvailable() - item.getQuantity());
            inventory.getProductManager().getProduct(item.getProduct()).increaseOrdersmade();
        }
        return true;
    }
}
