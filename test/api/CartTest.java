package api;

import api.objects.Cart;
import api.objects.CartItem;
import api.objects.Product;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests για την κλάση Cart.
 */
public class CartTest {

    // Δημιουργία δείγματος προϊόντος για δοκιμές
    private Product createSampleProduct(String title, double price, double quantityAvailable) {
        return new Product(
                title,
                "Δείγμα Περιγραφής",
                "Δείγμα Κατηγορίας",
                "Δείγμα Υποκατηγορίας",
                price,
                quantityAvailable,
                Product.MeasurementUnit.PIECES
        );
    }

    @Test
    public void testCopyConstructor() {
        // Δημιουργία ενός αρχικού Cart και προσθήκη προϊόντων
        Cart originalCart = new Cart();
        Product product1 = createSampleProduct("Milk", 1.20, 10);
        Product product2 = createSampleProduct("Bread", 0.80, 20);

        originalCart.addProduct(product1, 2);
        originalCart.addProduct(product2, 5);

        // Δημιουργία αντιγράφου του Cart με τον copy constructor
        Cart copiedCart = new Cart(originalCart);

        // Έλεγχος ότι το αντίγραφο περιέχει τα ίδια προϊόντα
        List<CartItem> originalCartItems = originalCart.getCart();
        List<CartItem> copiedCartItems = copiedCart.getCart();

        assertEquals(originalCartItems.size(), copiedCartItems.size());
        for (int i = 0; i < originalCartItems.size(); i++) {
            CartItem originalItem = originalCartItems.get(i);
            CartItem copiedItem = copiedCartItems.get(i);

            // Έλεγχος για τα ίδια προϊόντα και ποσότητες
            assertEquals(originalItem.getProduct().getTitle(), copiedItem.getProduct().getTitle());
            assertEquals(originalItem.getQuantity(), copiedItem.getQuantity(), 0.01);
            assertEquals(originalItem.getTotalCost(), copiedItem.getTotalCost(), 0.01);
        }

        // Ελέγχουμε ότι τα Cart και CartItem είναι πραγματικά ανεξάρτητα (deep copy)
        assertNotSame(originalCart, copiedCart); // Το Cart πρέπει να είναι διαφορετικά αντικείμενα
        assertNotSame(originalCart.getCart(), copiedCart.getCart()); // Η λίστα προϊόντων πρέπει να είναι διαφορετική

        // Ελέγχουμε ότι τα αντικείμενα CartItem μέσα στις λίστες είναι επίσης διαφορετικά
        for (int i = 0; i < originalCartItems.size(); i++) {
            CartItem originalItem = originalCartItems.get(i);
            CartItem copiedItem = copiedCartItems.get(i);

            assertNotSame(originalItem, copiedItem); // Τα CartItem πρέπει να είναι διαφορετικά αντικείμενα
        }
    }

    @Test
    public void testAddProductToCart() {
        Cart cart = new Cart();
        Product product = createSampleProduct("Milk", 1.20, 10);

        cart.addProduct(product, 2); // Προσθήκη προϊόντος
        CartItem cartItem = cart.getCart().get(0);

        assertEquals("Milk", cartItem.getProduct().getTitle());
        assertEquals(2, cartItem.getQuantity(), 0.01);
        assertEquals(1.20 * 2, cartItem.getTotalCost(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddProductWithInvalidQuantity() {
        Cart cart = new Cart();
        Product product = createSampleProduct("Milk", 1.20, 10);

        cart.addProduct(product, 0); // Μη έγκυρη ποσότητα, πρέπει να πετάξει εξαίρεση
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddProductExceedingAvailableQuantity() {
        Cart cart = new Cart();
        Product product = createSampleProduct("Milk", 1.20, 5);

        cart.addProduct(product, 10); // Υπερβολική ποσότητα, πρέπει να πετάξει εξαίρεση
    }

    @Test
    public void testUpdateProductQuantity() {
        Cart cart = new Cart();
        Product product = createSampleProduct("Bread", 0.80, 20);

        cart.addProduct(product, 5);
        cart.updateQuantity("Bread", 10); // Ενημέρωση ποσότητας

        CartItem cartItem = cart.getCart().get(0);
        assertEquals(10, cartItem.getQuantity(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateProductQuantityToInvalid() {
        Cart cart = new Cart();
        Product product = createSampleProduct("Bread", 0.80, 20);

        cart.addProduct(product, 5);
        cart.updateQuantity("Bread", 25); // Υπερβολική ποσότητα, πρέπει να πετάξει εξαίρεση
    }

    @Test
    public void testRemoveProductFromCart() {
        Cart cart = new Cart();
        Product product = createSampleProduct("Eggs", 2.50, 12);

        cart.addProduct(product, 6);
        cart.removeProduct("Eggs"); // Διαγραφή προϊόντος

        assertTrue(cart.getCart().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonExistingProduct() {
        Cart cart = new Cart();
        cart.removeProduct("NonExistingProduct"); // Προσπάθεια διαγραφής ανύπαρκτου προϊόντος
    }

    @Test
    public void testGetTotalCost() {
        Cart cart = new Cart();
        Product product1 = createSampleProduct("Milk", 1.20, 10);
        Product product2 = createSampleProduct("Bread", 0.80, 20);

        cart.addProduct(product1, 2);
        cart.addProduct(product2, 5);

        assertEquals(1.20 * 2 + 0.80 * 5, cart.getTotalCost(), 0.01);
    }

    @Test
    public void testDisplayCart() {
        Cart cart = new Cart();
        Product product1 = createSampleProduct("Milk", 1.20, 10);
        Product product2 = createSampleProduct("Bread", 0.80, 20);

        cart.addProduct(product1, 2);
        cart.addProduct(product2, 5);

        cart.displayCart(); // Θα εμφανίσει το περιεχόμενο στην κονσόλα (οπτικός έλεγχος)
    }

    @Test
    public void testGetProductFromCart() {
        Cart cart = new Cart();
        Product product = createSampleProduct("Cheese", 3.50, 15);

        cart.addProduct(product, 3);
        Product retrievedProduct = cart.getProductFromCart("Cheese");

        assertEquals("Cheese", retrievedProduct.getTitle());
        assertEquals(3.50, retrievedProduct.getPrice(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNonExistingProductFromCart() {
        Cart cart = new Cart();
        cart.getProductFromCart("NonExistingProduct"); // Πρέπει να πετάξει εξαίρεση
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullProduct() {
        Cart cart = new Cart();
        cart.addProduct(null, 5); // Προσθήκη μη έγκυρου προϊόντος (null)
    }

    @Test
    public void testEmptyCart() {
        Cart cart = new Cart();
        cart.displayCart(); // Θα πρέπει να εκτυπώσει "Το καλάθι είναι άδειο."
    }
}
