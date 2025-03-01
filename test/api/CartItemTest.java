package api;

import api.objects.CartItem;
import api.objects.Product;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests για την κλάση CartItem.
 */
public class CartItemTest {

    // Δημιουργία ενός δείγματος προϊόντος για χρήση στις δοκιμές
    private Product createSampleProduct() {
        return new Product("Coca Cola 2x1.5lt", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Αναψυκτικά", "Αναψυκτικά σε Μπουκάλια", 3.99, 100, Product.MeasurementUnit.PIECES);
    }

    // Δημιουργία ενός δείγματος CartItem για χρήση στις δοκιμές
    private CartItem createSampleCartItem() {
        return new CartItem(createSampleProduct(), 2);
    }

    @Test
    public void testCopyConstructor() {
        // Δημιουργία ενός αρχικού CartItem
        Product product = createSampleProduct();
        CartItem originalCartItem = new CartItem(product, 2);

        // Δημιουργία αντιγράφου με τον copy constructor
        CartItem copiedCartItem = new CartItem(originalCartItem);

        // Έλεγχος ότι τα δύο CartItem έχουν τα ίδια χαρακτηριστικά
        assertEquals(originalCartItem.getProduct(), copiedCartItem.getProduct());  // Έλεγχος αν τα προϊόντα είναι τα ίδια
        assertEquals(originalCartItem.getQuantity(), copiedCartItem.getQuantity(), 0.01);  // Έλεγχος ποσότητας

        // Ελέγχουμε ότι τα αντικείμενα είναι διαφορετικά (deep copy)
        assertNotSame(originalCartItem, copiedCartItem);  // Βεβαιωνόμαστε ότι δεν είναι το ίδιο αντικείμενο

        // Ελέγχουμε ότι το αντίγραφο έχει το ίδιο προϊόν (deep copy του προϊόντος)
        assertNotSame(originalCartItem.getProduct(), copiedCartItem.getProduct());
    }

    @Test
    public void testCartItemCreation() {
        Product product = createSampleProduct();
        CartItem cartItem = new CartItem(product, 2);

        assertEquals(product, cartItem.getProduct());
        assertEquals(2, cartItem.getQuantity(), 0.01);
    }

    @Test
    public void testSetQuantity() {
        CartItem cartItem = createSampleCartItem();
        cartItem.setQuantity(5);

        assertEquals(5, cartItem.getQuantity(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeQuantity() {
        CartItem cartItem = createSampleCartItem();
        cartItem.setQuantity(-3);  // Θα πρέπει να πετάξει IllegalArgumentException
    }

    @Test
    public void testGetTotalCost() {
        CartItem cartItem = createSampleCartItem();
        assertEquals(7.98, cartItem.getTotalCost(), 0.01);

        cartItem.setQuantity(3);
        assertEquals(11.97, cartItem.getTotalCost(), 0.01);
    }

    @Test
    public void testUpdateQuantityAndTotalCost() {
        CartItem cartItem = createSampleCartItem();

        cartItem.setQuantity(4);
        assertEquals(4, cartItem.getQuantity(), 0.01);
        assertEquals(15.96, cartItem.getTotalCost(), 0.01);
    }
}