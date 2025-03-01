package api.management;

import api.StringUtil;
import api.objects.Category;
import api.objects.Product;
import api.objects.SubCategory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Αχιλλέας
 * Product Manager class is responsible for managing the products in the supermarket, offers plenty methods for interacting with the products like
 * searching, adding, removing, changing the fields of product, getting information of their current state. Product manager is used by product manager
 * because when interacting with products like adding we need to access the categories where the subcategories and products are stored.
 */
public class ProductManager implements Serializable {
    private static final long serialVersionUID = 1442894525631887615L;
    //the category manager that allows us to get access where the products are located
    private CategoryManager categoryManager;

    /**
     * constructor that initialize the categoryManager
     */
    public ProductManager() {
        categoryManager = new CategoryManager();
    }

    /**
     * method that search a product given some parameters,this method requires the name of the product to search,
     * but the other two are not necessary. Method returns the reference of the actual product if the call happens by an admin, so he can modify the product
     * @param productName the name of the product we are searching
     * @param category    the name of the category of the product
     * @param subCategory the name of the subcategory of the product
     * @param isAdmin     indicates if who is searching is an admin
     * @return the reference of the product if isAdmin is true, a copy of the actual product otherwise
     * @throws IllegalArgumentException if the provided productName String is null because the method requires the name of the product, we must catch the error and inform the user
     */
    public ArrayList<Product> searchProduct(String productName, String category, String subCategory, boolean isAdmin) throws IllegalArgumentException {
        if (productName == null)
            throw new IllegalArgumentException("Cannot search product without the name of it");

        ArrayList<Product> products = new ArrayList<>();
        String normalizedProductName = StringUtil.normalize(productName);
        if (category != null && subCategory == null) {
            Category newCat = categoryManager.getCategory(category);
            if (newCat != null) {
                if (newCat.getAllSubCategories(isAdmin) != null) {
                    for (SubCategory sb : newCat.getAllSubCategories(isAdmin).values()) {
                        for(Product p : sb.getProducts().values()) {
                            String temp = StringUtil.normalize(p.getTitle());
                            if (temp.contains(normalizedProductName)) {
                                if (isAdmin)
                                    products.add(sb.getProductReference(temp));
                                else
                                    products.add(sb.getProduct(temp));
                            }
                        }
                    }
                }
            }
        } else if (category != null && subCategory != null) {
            Category newCat = categoryManager.getCategory(category);
            //System.out.println(category);
            if (newCat != null) {
                SubCategory sub = newCat.getSubCategory(subCategory);
                if (sub != null) {
                    if (isAdmin) {
                        for (Product p : sub.getProducts().values()) {
                            String temp = StringUtil.normalize(p.getTitle());
                            if (temp.contains(normalizedProductName)) {
                                products.add(sub.getProductReference(temp));
                            }
                        }
                    }
                    else {
                        for (Product p : sub.getProducts().values()) {
                            String temp = StringUtil.normalize(p.getTitle());
                            if (temp.contains(normalizedProductName)) {
                                products.add(sub.getProduct(temp));
                            }
                        }
                    }
                }
            }
        }
        else
        {
            for (Category cat : categoryManager.getAllCategories(isAdmin).values()) {
                HashMap<String, SubCategory> subcategories = cat.getAllSubCategories(isAdmin);
                if (!subcategories.isEmpty()) {
                    if (isAdmin) {
                        for (SubCategory sb : subcategories.values()) {
                            for (Product p : sb.getProducts().values()) {
                                String temp = StringUtil.normalize(p.getTitle());
                                if (temp.contains(normalizedProductName)) {
                                    products.add(sb.getProductReference(temp));
                                }
                            }
                        }
                    }
                    else{
                        for (SubCategory sb : subcategories.values()) {
                            for (Product p : sb.getProducts().values()) {
                                String temp = StringUtil.normalize(p.getTitle());
                                if (temp.contains(normalizedProductName)) {
                                    products.add(sb.getProduct(temp));
                                }
                            }
                        }
                    }
                }
            }
        }
        return products;
    }

    /**
     * assistant method that gives all the products from a subcategory
     * @param subCategory the subcategory where we get products
     * @param isAdmin     indicates if the user who does the search is admin
     * @return list with products, if Admin is true the list contains the references otherwise the copies of them
     */
    private ArrayList<Product> getProductsFromSubCategory(SubCategory subCategory, boolean isAdmin) {
        ArrayList<Product> products = new ArrayList<>();
        if (isAdmin) {
            for (Product p : subCategory.getProducts().values()) {
                products.add(subCategory.getProductReference(p.getTitle()));
            }
        }
        else
            products = new ArrayList<>(subCategory.getProducts().values());

        return products;
    }

    /**
     * assistant method that gives all the products from a subcategory
     * @param category the category where we get the products
     * @param isAdmin  indicates if the user who does the search is admin
     * @return list with products, if Admin is true the list contains the references otherwise the copies of them
     */
    private ArrayList<Product> getProductsFromCategory(Category category, boolean isAdmin) {
        ArrayList<Product> products = new ArrayList<>();
        if (isAdmin) {
            for (SubCategory sb : category.getAllSubCategories(true).values()) {
                products.addAll(getProductsFromSubCategory(sb, true));
            }
        } else {
            for (SubCategory sb : category.getAllSubCategories(false).values()) {
                products.addAll(getProductsFromSubCategory(sb, false));
            }
        }
        return products;
    }

    /**
     * method that searches for many products by category and subcategory, this method is used when we want to get all the products when we are searching
     * for categories and subcategories. Method returns a list of these products with the references of them in it when the call happened by an admin
     * otherwise list of products with the copies of them
     * @param category    the name of the category we are searching for
     * @param subCategory the name of the subcategory we are searching for
     * @param isAdmin     indicates if who is searching is admin
     * @return list of products within
     */
    public ArrayList<Product> searchProducts(String category, String subCategory, boolean isAdmin) {
        ArrayList<Product> products = new ArrayList<>();
        if (category != null && subCategory == null) {
            Category newCat = categoryManager.getCategory(category);
            if (newCat != null) {
                products.addAll(getProductsFromCategory(newCat, isAdmin));
            }
        } else if (category != null && subCategory != null) {
            Category newCat = categoryManager.getCategory(category);
            if (newCat != null) {
                SubCategory newSub = newCat.getSubCategory(subCategory);
                if (newSub != null) {
                    products.addAll(getProductsFromSubCategory(newSub, isAdmin));
                }
            }
        } else if (subCategory == null){
            for (Category cat : categoryManager.getAllCategories(isAdmin).values()) {
                products.addAll(getProductsFromCategory(cat, isAdmin));
            }
        }
        return products;
    }

    /**
     * this method is responsible for adding both categories-subcategories and products in the supermarket, this means that when we add
     * a new product that is in a new category-subcategory we must add them too and then add the product. In this case the category is
     * being added in this method and the subcategory is being added in the addProductToSubCategory method.
     * @param product the product we want to add
     * @return false if the product we want to add already exist, true if all went good
     * @throws IllegalArgumentException if the product we want to add is null object
     */
    public boolean addProduct(Product product) {
        if (product == null)
            throw new IllegalArgumentException("Cannot acccess null product object");

        if (categoryManager.getAllCategories(false).isEmpty() || categoryManager.getCategory(product.getCategory()) == null) {
            Category newCat = new Category(product.getCategory());
            categoryManager.addCategory(newCat);
            categoryManager.getCategory(product.getCategory()).addProductToSubCategory(product);
        }
        else {
            for(Product p : searchProduct(product.getTitle(), product.getCategory(), product.getSubcategory(), false)) {
                if (p.equals(product)){
                    return false;
                }
            }
            categoryManager.getCategory(product.getCategory()).addProductToSubCategory(product);
        }
        return true;
    }

    /**
     * remove an existing product from the market
     * @param product the product we want to remove
     * @return false if the product doesn't exist, true if the removal was successful
     * @throws IllegalArgumentException if the product we are trying to remove is null object
     */
    public boolean removeProduct(Product product) {
        if (product == null)
            throw new IllegalArgumentException("Cannot access null product object");

        boolean flag = false;
        for(Product p : searchProduct(product.getTitle(), product.getCategory(), product.getSubcategory(), false)) {
            if (p.equals(product))
                flag = true;
        }
        if (flag) {
            Category cat = categoryManager.getCategory(product.getCategory());
            cat.getSubCategory(product.getSubcategory()).removeProduct(product.getTitle());
            return true;
        }
        else
            return false;
    }

    /**
     * Updates the quantity of a product with the newQuantity given if the product exists
     * @param product the product whose quantity we want to update
     * @param newQuantity the quantity that the product will have
     * @throws IllegalArgumentException if the product is null object
     */
    public void updateQuantityOfProduct(Product product, double newQuantity) {
        if (product == null)
            throw new IllegalArgumentException("Cannot access null product object");

        if (!ProductExists(product))
            throw new IllegalArgumentException("The product you are trying to access doesn't exist");

        categoryManager.getCategory(product.getCategory()).getSubCategory(product.getSubcategory()).getProductReference(product.getTitle()).setQuantityAvailable(newQuantity);
    }

    /**
     * checks if a product with a requested quantity is available in the supermarket
     * @param product the product whose availability we want to update
     * @param quantity the quantity requested
     * @return true if it is available otherwise false
     */
    public boolean isProductAvailable(Product product, double quantity) {
        if (product == null)
            throw new IllegalArgumentException("Cannot access null product object");

        if (!ProductExists(product))
            throw new IllegalArgumentException("The product you are trying to access doesn't exist");

        return categoryManager.getCategory(product.getCategory()).getSubCategory(product.getSubcategory()).getProduct(product.getTitle()).isQuantityAvailable(quantity);
    }

    /**
     * returns a list with all the products in the supermarket. This method is referring to Admin users,
     * so they can see important data for the products and modified them
     * @param isAdmin checks if who is accessing the method is Admin
     * @return the list with all the products
     * @throws IllegalArgumentException if isAdmin is false and inform that non Admin users have no access
     */
    public ArrayList<Product> getAllProducts(boolean isAdmin) {
        if (!isAdmin)
            throw new IllegalArgumentException("Access cannot be granted to non Admin user");
        //creates a new list where we are going to store the products
        ArrayList<Product> products = new ArrayList<>();
        //getting all categories and checking if they are zero size
        HashMap<String, Category> categories = categoryManager.getAllCategories(true);
        if (categories.isEmpty())
            return products;
        //adding to products the List that getProductsFromCategory has returned with addAll method
        for (Category cat : categories.values()) {
            products.addAll(getProductsFromCategory(cat, true));
        }
        return products;
    }

    /**
     * checks if a product exists in the supermarket by using the search method
     * @param product the product we are searching for
     * @return true if the given product exists or false otherwise
     */
    private boolean ProductExists(Product product) {
        boolean exists = false;
        for(Product p : searchProduct(product.getTitle(), product.getCategory(), product.getSubcategory(), false)) {
            if (p.equals(product)) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    public Product getProduct(Product product) {
        return categoryManager.getCategory(product.getCategory()).getSubCategory(product.getSubcategory()).getProductReference(product.getTitle());
    }
}
