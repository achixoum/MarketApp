package api.objects;

import api.StringUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Αχιλλέας
 * class Category represents a category of products. The category consist of its name and the subcategories that it has
 * where we store our products. So the category is the highest level in the hierarchy, because the supermarket has different
 * categories where each of them has different subcategories, where each subcategory consist of the products.
 * Category class provides the following operations: access to all subcategories and direct access for each one of them for modification,
 * adding subcategories with hashing for faster accessibility, adding products to the subcategories where they are stored
 */
public class Category implements Serializable {

    private static final long serialVersionUID = 5079030810974127034L;
    //name of the category
    private String category;
    //all the subcategories, access with the name of the subcategory
    private HashMap<String, SubCategory> subCategories;
    /**
     * Constructor that initialize the name of category, a hashmap where the subCategories will be stored
     * and a hashmap with the same subcategories again but with normalized title of the subcategory which is the key of the hashmap
     * @param name the name of the category
     */
    public Category(String name) {
        this.category = name;
        this.subCategories = new HashMap<>();
    }

    /**
     * Copy constructor that deep copies the category param if it is not null object
     * @param category the category Object that will be copied
     * @throws IllegalArgumentException if the object is null
     */
    public Category(Category category) {
        if (category == null)
            throw new IllegalArgumentException("Cannot access null category object");

        this.category = category.getCategoryName();
        this.subCategories = new HashMap<>();

        for(Map.Entry<String, SubCategory> sub : category.subCategories.entrySet()) {
            this.subCategories.put(sub.getKey(), new SubCategory(sub.getValue()));
        }
    }

    //getters-setters for the category String
    public void setCategoryName(String name) {
        this.category = name;
    }
    public String getCategoryName() {
        return category;
    }

    /**
     * method for getting a copy of the subcategories hashmap if the user isn't admin
     * if it is then we return the original hashmap
     * @param isAdmin indicates if we will return the original hashmap or a copy
     * @return the original hashmap or a copy of it
     */
    public HashMap<String, SubCategory> getAllSubCategories(boolean isAdmin) {
        if (isAdmin)
            return this.subCategories;
        else {
            HashMap<String, SubCategory> newSubCategories = new HashMap<>();
            if (subCategories.isEmpty())
                return newSubCategories;
            for (SubCategory sub : subCategories.values()) {
                newSubCategories.put(StringUtil.normalize(sub.getTitle()), new SubCategory(sub));
            }
            return newSubCategories;
        }
    }

    /**
     * Gives the reference of a specific subcategory by its name
     * @param title the name of the subcategory we want to get
     * @return the reference of the subcategory object
     * @throws IllegalArgumentException if the title is null
     */
    public SubCategory getSubCategory(String title) {
        if (title == null)
            throw new IllegalArgumentException("Cannot access null String object");
        return subCategories.get(StringUtil.normalize(title));
    }

    /**
     * add a subcategory to the hashmap if it doesn't already exist
     * @param subCategory the subcategory object that will be added
     * @return false if the subcategory already exist in the hashmap, true if the subcategory successfully is added
     * @throws IllegalArgumentException if the subcategory is null
     */
    public boolean addSubCategory(SubCategory subCategory) {
        if (subCategory == null)
            throw new IllegalArgumentException("Cannot access null subcategory object");

        String tempString = StringUtil.normalize(subCategory.getTitle());
        if (!subCategories.containsKey(tempString)) {
            subCategories.put(tempString, subCategory);
            return true;
        }
        return false;
    }

    /**
     * adds a product to a subcategory if the subcategory already exist if it doesn't it creates a new subcategory,
     * add it to the hashmap and then add the product
     * @param product the product object that will be added in the subcategory
     * @throws IllegalArgumentException if the product is null
     */
    public void addProductToSubCategory(Product product) {
        if (product == null)
            throw new IllegalArgumentException("Cannot access null product object");
        if (subCategories.isEmpty() || getSubCategory(product.getSubcategory()) == null) {
            SubCategory sub = new SubCategory(product.getSubcategory());
            addSubCategory(sub);
        }
        subCategories.get(StringUtil.normalize(product.getSubcategory())).addProduct(product);
    }
}
