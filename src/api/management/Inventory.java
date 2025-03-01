package api.management;


import java.io.Serializable;

/**
 * Inventory class connects Categories, Subcategories and products which form the supermarket with the management classes
 * product and category witch they are responsible for the data to be stored and processed correctly. Outer classes use
 * the managers to interact with the data stored
 */
public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;
    private ProductManager productManager;
    private CategoryManager categoryManager;

    /**
     * constructor creates the two object managers
     */
    public Inventory() {
        productManager = new ProductManager();
        categoryManager = new CategoryManager();
    }

    /**
     * With this method other classes such as admin can work on the products of the market
     * @return the product manager
     */
    public ProductManager getProductManager() {
        return productManager;
    }

    public CategoryManager getCategoryManager() {
        return categoryManager;
    }
}

