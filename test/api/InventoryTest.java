package api;

import api.management.Inventory;
import api.objects.Category;
import api.objects.Product;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

public class InventoryTest {
    @Test
    public void testAddCategory() {
        Inventory inventory = new Inventory();
        Category category = new Category("Φρούτα");

        assertTrue("Category should be added successfully", inventory.getCategoryManager().addCategory(category));
        assertFalse("Duplicate category should not be added", inventory.getCategoryManager().addCategory(category));
    }

    @Test
    public void testGetAllCategoriesWhenEmpty() {
        Inventory inventory = new Inventory();

        assertEquals("getAllCategories should return null when inventory is empty",0, inventory.getCategoryManager().getAllCategories(false).size());
    }

    @Test
    public void testGetAllCategoriesWhenNotEmpty() {
        Inventory inventory = new Inventory();
        inventory.getCategoryManager().addCategory(new Category("Φρούτα"));

        HashMap<String, Category> categories = inventory.getCategoryManager().getAllCategories(false);
        assertNotNull("getAllCategories should not return null when inventory has categories", categories);
        assertEquals("Category count should be 1", 1, categories.size());
    }

    @Test
    public void testAddProductToInventory() {
        Inventory inventory = new Inventory();
        Product product = new Product("Coca Cola 2x1.5L", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.99, 100, Product.MeasurementUnit.PIECES);

        assertTrue("Product should be added successfully", inventory.getProductManager().addProduct(product));
        assertFalse("Duplicate product should not be added", inventory.getProductManager().addProduct(product));
    }

    @Test
    public void testSearchProductByNameOnly() {
        Inventory inventory = new Inventory();
        Product product = new Product("Coca Cola 2x1.5L", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.99, 100, Product.MeasurementUnit.PIECES);
        inventory.getProductManager().addProduct(product);

        ArrayList<Product> products = inventory.getProductManager().searchProduct("Coca Cola 2x1.5L", null, null, false);
        assertNotNull("searchProduct should find the product by name", products);
        assertEquals("Product title does not match", "Coca Cola 2x1.5L", products.get(0).getTitle());
    }

    @Test
    public void testSearchProductByCategoryAndSubCategory() {
        Inventory inventory = new Inventory();
        Product product = new Product("Coca Cola 2x1.5L", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.99, 100, Product.MeasurementUnit.PIECES);
        inventory.getProductManager().addProduct(product);

        ArrayList<Product> products = inventory.getProductManager().searchProduct("Coca Cola 2x1.5L", "Anapsiktika", "Anapsuktika 2 mpoukalia", false);
        assertNotNull("searchProduct should find the product by category and subcategory", products);
        assertEquals("Product title does not match", "Coca Cola 2x1.5L",  products.get(0).getTitle());
    }

    @Test
    public void testRemoveProductFromInventory() {
        Inventory inventory = new Inventory();
        Product product = new Product("Coca Cola 2x1.5L", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.99, 100, Product.MeasurementUnit.PIECES);
        inventory.getProductManager().addProduct(product);

        assertTrue("removeProductFromInventory should return true for existing product", inventory.getProductManager().removeProduct(product));
        assertFalse("removeProductFromInventory should return false for non-existing product", inventory.getProductManager().removeProduct(product));
    }

    @Test
    public void testUpdateQuantityOfProductInInventory() {
        Inventory inventory = new Inventory();
        Product product = new Product("Coca Cola 2x1.5L", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.99, 100, Product.MeasurementUnit.PIECES);
        inventory.getProductManager().addProduct(product);

        inventory.getProductManager().updateQuantityOfProduct(product, 5);
        assertEquals("Product quantity should be updated", 5, product.getQuantityAvailable(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateQuantityOfNonExistingProduct() {
        Inventory inventory = new Inventory();
        Product product = new Product("Coca Cola 2x1.5L", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.99, 100, Product.MeasurementUnit.PIECES);

        inventory.getProductManager().updateQuantityOfProduct(product, 5);
    }

    @Test
    public void testIsProductAvailable() {
        Inventory inventory = new Inventory();
        Product product = new Product("Coca Cola 2x1.5L", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.99, 100, Product.MeasurementUnit.PIECES);
        inventory.getProductManager().addProduct(product);

        assertTrue("Product should be available for quantity 5", inventory.getProductManager().isProductAvailable(product, 5));
        assertFalse("Product should not be available for quantity 15", inventory.getProductManager().isProductAvailable(product, 101));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsProductAvailableForNonExistingProduct() {
        Inventory inventory = new Inventory();
        Product product = new Product("Coca Cola 2x1.5L", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.99, 100, Product.MeasurementUnit.PIECES);

        inventory.getProductManager().isProductAvailable(product, 5);
    }

    @Test
    public void testSearchProductByAdmin() {
        Inventory inventory = new Inventory();
        Product product = new Product("Coca Cola 2x1.5L", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.99, 100, Product.MeasurementUnit.PIECES);
        inventory.getProductManager().addProduct(product);

        ArrayList<Product> products = inventory.getProductManager().searchProduct("Coca Cola 2x1.5L", null, null, true);
        boolean flag = false;
        for (Product p : products) {
            if (p == product)
                flag = true;
        }
        assertTrue(flag);
    }

    @Test
    public void testSearchProductByNotAdmin() {
        Inventory inventory = new Inventory();
        Product product = new Product("Coca Cola 2x1.5L", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.99, 100, Product.MeasurementUnit.PIECES);
        Product product1 = new Product("Ούζο Πλωμαρίου 200ml",
                "Παραδοσιακό ούζο από τη Λέσβο με γλυκάνισο.", "Αλκοολούχα ποτά", "Ούζο", 5.00,150, Product.MeasurementUnit.PIECES);
        Product product2 = new Product("Pepsi 2x1.5L", "Pepsi αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Mpampouinos", "Anapsuktika 3 mpoukalia", 3.89, 100, Product.MeasurementUnit.PIECES);

        inventory.getProductManager().addProduct(product);
        inventory.getProductManager().addProduct(product2);
        inventory.getProductManager().addProduct(product1);

        inventory.getProductManager().searchProduct("Ούζο Πλωμαρίου 200ml", null, null, false);

        assertNotEquals(10, product.getQuantityAvailable(),0.001);
    }

    @Test
    public void testSearchProductsByCategoryByAdmin() {
        Inventory inventory = new Inventory();
        Product product1 = new Product("Coca Cola 2x1.5L", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.99, 100, Product.MeasurementUnit.PIECES);
        Product product2 = new Product("Pepsi 2x1.5L", "Pepsi αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.89, 100, Product.MeasurementUnit.PIECES);
        inventory.getProductManager().addProduct(product1);
        inventory.getProductManager().addProduct(product2);

        ArrayList<Product> products = inventory.getProductManager().searchProducts("Anapsiktika", null, true);

        assertSame(product1, products.get(1));
        assertSame(product2, products.get(0));
    }

    @Test
    public void testSearchProductsByCategoryByNotAdmin() {
        Inventory inventory = new Inventory();
        Product product1 = new Product("Coca Cola 2x1.5L", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.99, 100, Product.MeasurementUnit.PIECES);
        Product product2 = new Product("Pepsi 2x1.5L", "Pepsi αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.89, 100, Product.MeasurementUnit.PIECES);
        inventory.getProductManager().addProduct(product1);
        inventory.getProductManager().addProduct(product2);

        ArrayList<Product> products = inventory.getProductManager().searchProducts("Anapsiktika", null, false);
        assertNotSame(product1, products.get(0));
        assertNotSame(product2, products.get(1));
    }
    @Test
    public void testSearchProductsByCategory() {
        Inventory inventory = new Inventory();
        Product product1 = new Product("Coca Cola 2x1.5L", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.99, 100, Product.MeasurementUnit.PIECES);
        Product product2 = new Product("Pepsi 2x1.5L", "Pepsi αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.89, 100, Product.MeasurementUnit.PIECES);
        inventory.getProductManager().addProduct(product1);
        inventory.getProductManager().addProduct(product2);

        ArrayList<Product> products = inventory.getProductManager().searchProducts("Anapsiktika", null, false);
        assertEquals("Search by category should return 2 products", 2, products.size());
    }

    @Test
    public void testSearchProductsByCategoryAndSubCategory() {
        Inventory inventory = new Inventory();
        Product product = new Product("Pepsi 2x1.5L", "Pepsi αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.89, 100, Product.MeasurementUnit.PIECES);
        inventory.getProductManager().addProduct(product);

        ArrayList<Product> products = inventory.getProductManager().searchProducts("Anapsiktika", "Anapsuktika 2 mpoukalia", false);
        assertEquals("Search by category and subcategory should return 1 product", 1, products.size());
        assertEquals("Product title does not match", "Pepsi 2x1.5L", products.get(0).getTitle());
    }

    @Test
    public void testGetAllProducts() {
        Inventory inventory = new Inventory();

        Product product = new Product("Pepsi 2x1.5L", "Pepsi αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.89, 100, Product.MeasurementUnit.PIECES);
        inventory.getProductManager().addProduct(product);

        ArrayList<Product> products = inventory.getProductManager().getAllProducts(true);

        assertEquals("Search by category and subcategory should return 1 product", 1, products.size());
        assertEquals("Product title does not match", product, products.get(0));
    }

    @Test
    public void testGetProduct() {
        Inventory inventory = new Inventory();
        Product product = new Product("Pepsi 2x1.5L", "Pepsi αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.89, 100, Product.MeasurementUnit.PIECES);
        inventory.getProductManager().addProduct(product);

        assertEquals(product, inventory.getProductManager().getProduct(product));
    }
}


