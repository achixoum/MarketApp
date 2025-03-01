package api;

import api.management.Inventory;
import api.objects.Cart;
import api.objects.CartItem;
import api.objects.Order;
import api.objects.Product;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;


public class OrderTest {

    @Test
    public void testGetDate() {
        Cart cart = new Cart();
        Order order = new Order(cart);
        LocalDateTime date =  LocalDateTime.now();
        order.setDate(date);
        assertEquals("Method GetDate failed", date, order.getDate());
    }
    @Test
    public void testSetDate() {
        Cart cart = new Cart();
        Order order = new Order(cart);
        LocalDateTime date = LocalDateTime.now();
        order.setDate(date);

        assertEquals("The SetDate method failed", date , order.getDate());
    }
    @Test
    public void testGetTotalCostWithZeroItems() {
        Cart cart = new Cart();
        Order order = new Order(cart);

        assertEquals(0, order.getTotalCost(), 0.001);
    }
    @Test
    public void testGetTotalCostWithSomeItems() {
        Cart cart = new Cart();
        Product p1 = new Product("Coca Cola 2x1.5L", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Αναψυκτικά", "Αναψυκτικά 2 Μπουκάλια", 3.99, 100, Product.MeasurementUnit.PIECES);
        Product p2= new Product("Pepsi 2x1.5L", "Pepsi αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Αναψυκτικά", "Αναψυκτικά 2 Μπουκάλια", 3.89, 100, Product.MeasurementUnit.PIECES);
        cart.addProduct(p1, 2);
        cart.addProduct(p2, 1);
        double totalCot = 1*p2.getPrice() + 2*p1.getPrice();
        Order order = new Order(cart);

        assertEquals(totalCot, order.getTotalCost(), 0.001);
    }
    @Test
    public void testGetItemsOfOrder() {
        Cart cart = new Cart();
        Product p1 = new Product("Coca Cola 2x1.5L", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Αναψυκτικά", "Αναψυκτικά 2 Μπουκάλια", 3.99, 100, Product.MeasurementUnit.PIECES);
        Product p2 = new Product("Pepsi 2x1.5L", "Pepsi αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Αναψυκτικά", "Αναψυκτικά 2 Μπουκάλια", 3.89, 100, Product.MeasurementUnit.PIECES);
        cart.addProduct(p2, 1);
        cart.addProduct(p1,3);

        Order order = new Order(cart);

        List<CartItem> items = cart.getCart();
        assertEquals(items.size(), order.getItemsOfOrder().size());
    }
    @Test
    public void testCompleteOrderSuccess() {
        Cart cart = new Cart();
        Inventory inventory = new Inventory();
        Product p1 = new Product("Coca Cola 2x1.5L", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.99, 100, Product.MeasurementUnit.PIECES);
        Product p2 = new Product("Pepsi 2x1.5L", "Pepsi αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.89, 100, Product.MeasurementUnit.PIECES);
        inventory.getProductManager().addProduct(p1);
        inventory.getProductManager().addProduct(p2);

        //inventory.addProductToInventory(p1);
        cart.addProduct(p1,3);
        cart.addProduct(p2,2);
        cart.displayCart();
        Order order = new Order(cart);
        assertTrue("The method Complete failed with valid input", order.completeOrder(inventory));
    }
}
