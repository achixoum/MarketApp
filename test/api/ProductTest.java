package api;

import api.objects.Product;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests για την κλάση Product.
 */
public class ProductTest {

    // Δημιουργία βασικού προϊόντος για δοκιμές
    private Product createSampleProduct() {
        return new Product("Coca Cola 2x1.5lt", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Αναψυκτικά", "Αναψυκτικά σε Μπουκάλια", 3.99, 100, Product.MeasurementUnit.PIECES);
    }

    @Test
    public void testProductCreation() {
        Product product = createSampleProduct();

        assertEquals("Coca Cola 2x1.5lt", product.getTitle());
        assertEquals("Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt", product.getDescription());
        assertEquals("Αναψυκτικά", product.getCategory());
        assertEquals("Αναψυκτικά σε Μπουκάλια", product.getSubcategory());
        assertEquals(3.99, product.getPrice(), 0.01);
        assertEquals(100, product.getQuantityAvailable(), 0.01);
        assertEquals(Product.MeasurementUnit.PIECES, product.getUnit());
    }

    @Test
    public void testCopyConstructor() {
        // Create an original product
        Product originalProduct = new Product(
                "Coca Cola 2x1.5lt",
                "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Αναψυκτικά",
                "Αναψυκτικά σε Μπουκάλια",
                3.99,
                100,
                Product.MeasurementUnit.PIECES
        );

        // Create a copy using the copy constructor
        Product copiedProduct = new Product(originalProduct);

        // Verify that all attributes are equal
        assertEquals(originalProduct.getTitle(), copiedProduct.getTitle());
        assertEquals(originalProduct.getDescription(), copiedProduct.getDescription());
        assertEquals(originalProduct.getCategory(), copiedProduct.getCategory());
        assertEquals(originalProduct.getSubcategory(), copiedProduct.getSubcategory());
        assertEquals(originalProduct.getPrice(), copiedProduct.getPrice(), 0.01);
        assertEquals(originalProduct.getQuantityAvailable(), copiedProduct.getQuantityAvailable(), 0.01);
        assertEquals(originalProduct.getUnit(), copiedProduct.getUnit());

        // Modify the original product and ensure it doesn't affect the copied product
        originalProduct.setTitle("Fanta 1L");
        originalProduct.setPrice(2.49);
        originalProduct.setQuantityAvailable(50);

        assertNotEquals(originalProduct.getTitle(), copiedProduct.getTitle());
        assertNotEquals(originalProduct.getPrice(), copiedProduct.getPrice(), 0.01);
        assertNotEquals(originalProduct.getQuantityAvailable(), copiedProduct.getQuantityAvailable(), 0.01);
    }

    @Test
    public void testSettersAndGetters() {
        Product product = createSampleProduct();

        product.setTitle("Fanta 1L");
        assertEquals("Fanta 1L", product.getTitle());

        product.setDescription("Fanta αναψυκτικό 1 λίτρο");
        assertEquals("Fanta αναψυκτικό 1 λίτρο", product.getDescription());

        product.setCategory("Αναψυκτικά Φρούτων");
        assertEquals("Αναψυκτικά Φρούτων", product.getCategory());

        product.setSubcategory("Αναψυκτικά Φρούτων σε Μπουκάλια");
        assertEquals("Αναψυκτικά Φρούτων σε Μπουκάλια", product.getSubcategory());

        product.setPrice(2.49);
        assertEquals(2.49, product.getPrice(), 0.01);

        product.setQuantityAvailable(50);
        assertEquals(50, product.getQuantityAvailable(), 0.01);

        product.setUnit(Product.MeasurementUnit.KILOS);
        assertEquals(Product.MeasurementUnit.KILOS, product.getUnit());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativePrice() {
        Product product = createSampleProduct();
        product.setPrice(-1.0);  // Θα πρέπει να πετάξει IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeQuantity() {
        Product product = createSampleProduct();
        product.setQuantityAvailable(-5);  // Θα πρέπει να πετάξει IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullUnit() {
        Product product = createSampleProduct();
        product.setUnit(null);  // Θα πρέπει να πετάξει IllegalArgumentException
    }

    @Test
    public void testValidateProduct() {
        Product validProduct = createSampleProduct();
        assertTrue(validProduct.validateProduct());

        try {
            Product invalidProduct = new Product("", "Περιγραφή", "", "Υποκατηγορία", -5.99, -10, Product.MeasurementUnit.PIECES);
            fail("Δεν έπρεπε να δημιουργηθεί το προϊόν με αρνητική τιμή και ποσότητα.");
        } catch (IllegalArgumentException e) {
            assertEquals("Η τιμή δεν μπορεί να είναι αρνητική.", e.getMessage());
        }
    }

    @Test
    public void testGetQuantityLabel() {
        Product productKilos = new Product("Ντομάτες", "Φρέσκες ντομάτες", "Λαχανικά", "Ντομάτες", 1.99, 50, Product.MeasurementUnit.KILOS);
        Product productPieces = createSampleProduct();

        assertEquals("κιλά", productKilos.getQuantityLabel());
        assertEquals("τεμάχια", productPieces.getQuantityLabel());
    }

    @Test
    public void testIsQuantityAvailable() {
        Product product = createSampleProduct();

        assertTrue(product.isQuantityAvailable(50));
        assertFalse(product.isQuantityAvailable(150));
    }

    @Test
    public void testDisplayDetailsAsAdmin() {
        Product product = createSampleProduct();
        product.displayDetails(true);  // Έλεγχος εμφάνισης λεπτομερειών για διαχειριστή (τυπώνει στην κονσόλα)
    }

    @Test
    public void testDisplayDetailsAsCustomer() {
        Product product = createSampleProduct();
        product.displayDetails(false);  // Έλεγχος εμφάνισης λεπτομερειών για πελάτη (τυπώνει στην κονσόλα)
    }

    @Test
    public void testOrdersMadeInitialization() {
        Product product = createSampleProduct();
        assertEquals(0, product.getOrdersmade()); // Αρχική τιμή πρέπει να είναι 0
    }

    @Test
    public void testIncreaseOrdersMade() {
        Product product = createSampleProduct();
        product.increaseOrdersmade(); // Αυξάνει το ordersmade κατά 1
        assertEquals(1, product.getOrdersmade());
        product.increaseOrdersmade();
        assertEquals(2, product.getOrdersmade());
    }

    @Test
    public void testSetAndGetOrdersMade() {
        Product product = createSampleProduct();
        product.setOrdersmade(10); // Ορισμός του ordersmade σε 10
        assertEquals(10, product.getOrdersmade());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeOrdersMade() {
        Product product = createSampleProduct();
        product.setOrdersmade(-1); // Δεν πρέπει να επιτρέπεται αρνητική τιμή
    }
}
