package api;

import api.objects.*;
import api.management.*;
import api.users.Admin;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;


public class AdminTest {

    @Test
    public void testAddProduct() {
        Inventory inventory = new Inventory();
        Admin admin = new Admin("admin1", "password123");

        Product product = new Product("Coca Cola 2x1.5L", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.99, 100, Product.MeasurementUnit.PIECES);

        assertTrue(admin.addProduct(product, inventory));
        assertNotNull(inventory.getProductManager().searchProduct("Coca Cola 2x1.5L", "Anapsiktika", "Anapsuktika 2 mpoukalia", true));
    }

    @Test
    public void testRemoveProduct() {
        Inventory inventory = new Inventory();
        Admin admin = new Admin("admin2", "password123");

        Product product = new Product("Coca Cola 2x1.5L", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.99, 100, Product.MeasurementUnit.PIECES);
        admin.addProduct(product, inventory);

        assertTrue(admin.removeProduct(product, inventory));
        assertEquals(0,inventory.getProductManager().searchProduct("coca cola 2x1.5L", "anapsiktika", "anapsuktika 2 mpoukalia", true).size());
    }

    @Test
    public void testUpdateQuantityOfProduct() {
        Inventory inventory = new Inventory();
        Admin admin = new Admin("admin3", "password123");

        Product product = new Product("Coca Cola 2x1.5L", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.99, 100, Product.MeasurementUnit.PIECES);
        admin.addProduct(product, inventory);

        admin.updateQuantityOfProduct(product, inventory, 150);
        ArrayList<Product> updatedProduct = inventory.getProductManager().searchProduct("coca cola 2x1.5L", "anapsiktika", "anapsuktika 2 mpoukalia", true);
        assertNotNull(updatedProduct);
        assertEquals(150, updatedProduct.get(0).getQuantityAvailable(),0.001);
    }

    @Test
    public void testEditProduct() {
        Inventory inventory = new Inventory();
        Admin admin = new Admin("admin4", "password123");

        Product originalProduct = new Product("Coca Cola 2x1.5L", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.99, 100, Product.MeasurementUnit.PIECES);
        admin.addProduct(originalProduct, inventory);
        for(Category category : inventory.getCategoryManager().getAllCategories(true).values()) {
            System.out.println(category.getCategoryName());
        }
        Product editedProduct = new Product("Tomato", "Ripe red tomatoes", "Vegetables", "Fresh Vegetables", 2.5, 150, Product.MeasurementUnit.KILOS);

        admin.EditProduct(originalProduct, editedProduct, inventory);

        ArrayList<Product> updatedProduct = inventory.getProductManager().searchProduct("tomato", "vegetables", "fresh vegetables", true);
        assertNotNull(updatedProduct);
        assertEquals(1, updatedProduct.size());
        assertEquals(150, updatedProduct.getFirst().getQuantityAvailable(),0.001);
        assertEquals(2.5, updatedProduct.get(0).getPrice(), 0.001);
        assertEquals("Ripe red tomatoes", updatedProduct.get(0).getDescription());
    }

    @Test
    public void testGetUnavailableProducts() {
        Inventory inventory = new Inventory();
        Admin admin = new Admin("admin5", "password123");

        Product availableProduct = new Product("Coca Cola 2x1.5L", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.99, 100, Product.MeasurementUnit.PIECES);
        Product unavailableProduct =new Product("Pepsi 2x1.5L", "Pepsi αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.89, 0, Product.MeasurementUnit.PIECES);
        admin.addProduct(availableProduct, inventory);
        admin.addProduct(unavailableProduct, inventory);

        ArrayList<Product> unavailableProducts = admin.getUnavailableProducts(inventory);
        assertEquals(1, unavailableProducts.size());
        assertEquals("Pepsi 2x1.5L", unavailableProducts.get(0).getTitle());
    }

    @Test
    public void testGetMostOrderProducts() {
        Inventory inventory = new Inventory();
        Admin admin = new Admin("admin6", "password123");

        Product product1 = new Product("Coca Cola 2x1.5L", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.99, 100, Product.MeasurementUnit.PIECES);
        Product product2 = new Product("Pepsi 2x1.5L", "Pepsi αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.89, 100, Product.MeasurementUnit.PIECES);
        admin.addProduct(product1, inventory);
        admin.addProduct(product2, inventory);

        ArrayList<Product> mostOrderedProducts = admin.getMostOrderProducts(inventory);
        assertEquals(2, mostOrderedProducts.size());
        assertEquals("Pepsi 2x1.5L", mostOrderedProducts.get(0).getTitle());
        assertEquals("Coca Cola 2x1.5L", mostOrderedProducts.get(1).getTitle());
    }

    @Test
    public void testSearchProduct() {
        Inventory inventory = new Inventory();
        Admin admin = new Admin("admin7", "password123");

        Product product = new Product("Κρασί Ερυθρό Ξηρό 750ml", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.99, 100, Product.MeasurementUnit.PIECES);
        admin.addProduct(product, inventory);

        ArrayList<Product> foundProduct = admin.searchProduct("κρασι", "anapsiktika", "Anapsuktika 2 mpoukalia", inventory);
        assertEquals(1, foundProduct.size());
        assertEquals("Κρασί Ερυθρό Ξηρό 750ml", foundProduct.get(0).getTitle());
    }

    @Test
    public void testSearchProducts() {
        Inventory inventory = new Inventory();
        Admin admin = new Admin("admin8", "password123");

        Product product1 = new Product("Coca Cola 2x1.5L", "Coca Cola αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.99, 100, Product.MeasurementUnit.PIECES);
        Product product2 = new Product("Pepsi 2x1.5L", "Pepsi αναψυκτικό σε πακέτο 2 μπουκάλια 1.5lt",
                "Anapsiktika", "Anapsuktika 2 mpoukalia", 3.89, 100, Product.MeasurementUnit.PIECES);

        admin.addProduct(product1, inventory);
        admin.addProduct(product2, inventory);

        ArrayList<Product> bakeryProducts = admin.searchProducts("anapsiktika", "anapsuktika 2 mpoukalia ", inventory);
        assertEquals(2, bakeryProducts.size());
    }
}
