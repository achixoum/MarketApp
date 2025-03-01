package api;

import api.objects.Product;
import api.objects.SubCategory;
import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.*;

/**
 * Unit tests για την κλάση SubCategory.
 */
public class SubCategoryTest {

    private SubCategory subCategory;
    private Product product1;
    private Product product2;
    private Product duplicateProduct;

    // Προετοιμασία για τα τεστ (Setup)
    @org.junit.Before
    public void setUp() {
        // Δημιουργία δείγματος προϊόντων για δοκιμές
        product1 = new Product("Coca Cola 2x1.5L", "Σαπούνι", "Ποτά", "Αναψυκτικά", 1.5, 50, Product.MeasurementUnit.PIECES);
        product2 = new Product("Pepsi 2x1.5L", "Σαπούνι", "Ποτά", "Αναψυκτικά", 1.5, 50, Product.MeasurementUnit.PIECES);
        duplicateProduct = new Product("Coca Cola 2x1.5L", "Σαπούνι", "Ποτά", "Αναψυκτικά", 1.5, 50, Product.MeasurementUnit.PIECES);

        subCategory = new SubCategory("Αναψυκτικά");
    }

    // Test for getting the title of the subcategory
    @Test
    public void testGetTitle() {
        assertEquals("Αναψυκτικά", subCategory.getTitle());
    }

    // Test for getting products in the subcategory
    @Test
    public void testGetProducts() {
        subCategory.addProduct(product1);
        subCategory.addProduct(product2);
        Map<String, Product> products = subCategory.getProducts();

        assertEquals(2, products.size());
        assertTrue(products.containsKey(StringUtil.normalize("Coca Cola 2x1.5L")));
        assertTrue(products.containsKey(StringUtil.normalize("Pepsi 2x1.5L")));
    }

    // Test for adding a valid product
    @Test
    public void testAddProduct() {
        subCategory.addProduct(product1);
        Map<String, Product> products = subCategory.getProducts();

        assertEquals(1, products.size());
        assertTrue(products.containsKey(StringUtil.normalize("Coca Cola 2x1.5L")));
    }

    // Test for adding a duplicate product
    @Test
    public void testAddDuplicateProduct() {
        subCategory.addProduct(product1); // Add product1

        try {
            subCategory.addProduct(duplicateProduct); // Attempt to add the same product again
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Το προϊόν που θέλετε να προσθέσετε υπάρχει ήδη.", e.getMessage());
        }
    }

    // Test for adding a null product
    @Test(expected = IllegalArgumentException.class)
    public void testAddNullProduct() {
        subCategory.addProduct(null); // Should throw an exception
    }

    // Test for removing a product by title
    @Test
    public void testRemoveProduct() {
        subCategory.addProduct(product1);
        subCategory.addProduct(product2);
        subCategory.removeProduct("Coca Cola 2x1.5L");

        Map<String, Product> products = subCategory.getProducts();

        assertEquals(1, products.size());
        assertFalse(products.containsKey(StringUtil.normalize("Coca Cola 2x1.5L")));
        assertTrue(products.containsKey(StringUtil.normalize("Pepsi 2x1.5L")));
    }

    // Test for removing a non-existing product
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonExistingProduct() {
        subCategory.addProduct(product1);
        subCategory.removeProduct("Sprite 2x1.5L"); // Should throw an exception
    }

    // Test for displaying products when the subcategory is not empty
    @Test
    public void testDisplayProducts() {
        subCategory.addProduct(product1);
        subCategory.addProduct(product2);

        subCategory.displayProducts(); // Should print product details to console
    }

    // Test for displaying products when the subcategory is empty
    @Test
    public void testDisplayNoProducts() {
        subCategory.displayProducts(); // Should print "Η υποκατηγορία δεν περιέχει προϊόντα."
    }

    // Test for retrieving an existing product
    @Test
    public void testGetProduct() {
        subCategory.addProduct(product1);
        subCategory.addProduct(product2);

        Product retrievedProduct = subCategory.getProduct("Pepsi 2x1.5L");

        assertNotNull(retrievedProduct);
        assertEquals("Pepsi 2x1.5L", retrievedProduct.getTitle());
    }

    // Test for retrieving a non-existing product
    @Test
    public void testGetProductNonExisting() {
        subCategory.addProduct(product1);
        Product retrievedProduct = subCategory.getProduct("Sprite 2x1.5L");

        assertNull(retrievedProduct);
    }

    // Test for getting the title of an existing product
/*    @Test
    public void testGetProductTitle() {
        subCategory.addProduct(product1);
        String title = subCategory.getProductTitle("Coca Cola 2x1.5L");

        assertEquals("Coca Cola 2x1.5L", title);
    }

    // Test for getting the title of a non-existing product
    @Test
    public void testGetNonExistingProductTitle() {
        subCategory.addProduct(product1);
        String title = subCategory.getProductTitle("Sprite 2x1.5L");

        assertNull(title);
    }

 */
}
