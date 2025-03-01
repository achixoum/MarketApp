package api;

import api.users.*;
import api.objects.*;
import api.management.*;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

/**
 *  Unit tests for the Customer class.
 */
public class CustomerTest {

    private Customer customer;
    private Inventory inventory;
    private Product product1;
    private Product product2;

    @Before
    public void setUp() {
        // Create a customer
        customer = new Customer("john_doe", "password123");

        // Create an inventory and add products
        inventory = new Inventory();
        product1 = new Product("Coca Cola 1.5L", "Soda beverage", "Drinks", "Sodas", 1.50, 100, Product.MeasurementUnit.PIECES);
        product2 = new Product("Apples", "Fresh apples", "Fruits", "Apples", 2.00, 50, Product.MeasurementUnit.KILOS);
        inventory.getProductManager().addProduct(product1);
        inventory.getProductManager().addProduct(product2);
    }

    @Test
    public void testUpdateCart() {
        customer.addToCart(product1, 2); // Initially add 2 products
        customer.updateCart(product1, 5); // Update to 5 products
        assertEquals(5.0, customer.getQuantityAvailableFromCart(product1), 0.001); // Adding delta for floating-point comparison
    }

    @Test
    public void testRemoveFromCart() {
        customer.addToCart(product1, 2);
        customer.removeFromCart(product1);
        assertFalse(customer.getCart().containsProduct(product1));
    }

    @Test
    public void testCompleteOrderSuccess() throws Exception {
        customer.addToCart(product1, 2);
        customer.addToCart(product2, 3);

        Order completedOrder = customer.completeOrder(inventory);

        assertNotNull(completedOrder);
        assertEquals(1, customer.viewOrderHistory().size());
        assertTrue(customer.getOrderHistory().contains(completedOrder));
        assertEquals(2, customer.getOrderHistory().get(0).getItemsOfOrder().size());

        // Modify the assertions to check the stock properly using searchProduct()
        assertEquals(98, inventory.getProductManager().searchProduct(product1.getTitle(), product1.getCategory(), product1.getSubcategory(),false).get(0).getQuantityAvailable(), 0.001); // 100 - 2
        assertEquals(47, inventory.getProductManager().searchProduct(product2.getTitle(), product2.getCategory(), product2.getSubcategory(),false).get(0).getQuantityAvailable(), 0.001); // 50 - 3
    }

    @Test (expected = IllegalArgumentException.class)
    public void testCompleteOrderFailure() throws Exception{
        customer.addToCart(product1, 200); // Exceeds inventory stock

        Order completedOrder = customer.completeOrder(inventory);

        assertNull(completedOrder); // Should return null because the order can't be completed
        assertEquals(0, customer.viewOrderHistory().size()); // No order in history
        assertEquals(100, inventory.getProductManager().searchProduct(product1.getTitle(), product1.getCategory(), product1.getSubcategory(),false).get(0).getQuantityAvailable(), 0.001); // Inventory remains unchanged
    }

    @Test
    public void testViewOrderHistory() throws Exception{
        assertTrue(customer.viewOrderHistory().isEmpty());

        customer.addToCart(product1, 2);
        customer.completeOrder(inventory);

        List<Order> history = customer.viewOrderHistory();
        assertEquals(1, history.size());
    }

    @Test
    public void testViewCart() {
        customer.addToCart(product1, 2);
        customer.addToCart(product2, 3);

        customer.viewCart();
        // Check the console output to verify cart contents
    }

    @Test
    public void testClearCart() {
        customer.addToCart(product1, 2);
        customer.addToCart(product2, 3);

        customer.clearCart();
        assertTrue(customer.getCart().isEmpty());
    }
}