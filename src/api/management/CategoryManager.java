package api.management;

import api.StringUtil;
import api.objects.Category;
import api.users.Admin;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Αχιλλέας
 * CategoryManager class is responsible for managing categories in the supermarket, like adding categories to the market,
 * getting access to specific category for modyfying and access to all categories for info
 */
public class CategoryManager implements Serializable {

    private static final long serialVersionUID = 7161322679956278337L;
    //a hashmap of the categories with the name as the hash key and the category as the value
    private HashMap<String, Category> categories;

    /**
     * constructor that initialize the Hashmap with categories
     */
    public CategoryManager() {
        categories = new HashMap<>();
    }

    /**
     * method for getting a copy of the categories hashmap if the user isn't admin
     * if it is then we return the original hashmap
     * @param isAdmin indicates if we will return the original hashmap or a copy
     * @return the original hashmap or a copy of it
     */
    public HashMap<String, Category> getAllCategories(boolean isAdmin) {
        if (isAdmin)
          return this.categories;
        else {
            HashMap<String, Category> newCategories = new HashMap<>();
            if (categories.isEmpty())
                return newCategories;
            for (Category cat : categories.values()) {
                newCategories.put(StringUtil.normalize(cat.getCategoryName()), new Category(cat));
            }
            return newCategories;
        }
    }

    /**
     * method for getting access to a specific category in the hashmap
     * @param category the name of the category that we are looking for
     * @return the reference of the category if it exists in the hashmap or null otherwise
     */
    public Category getCategory(String category) {
        if (category == null)
            throw new IllegalArgumentException("Cannot access null category object");
        return categories.get(StringUtil.normalize(category));
    }

    /**
     * Adds a category in the hashmap if it doesn't exist
     * @param category the category we want to add
     * @return false if the category already exists in the hashmap, true if the category is being added successfully
     */
    public boolean addCategory(Category category) {
        if (category == null)
            throw new IllegalArgumentException("Cannot access null category object");
        if (getCategory(category.getCategoryName()) == null) {
            categories.put(StringUtil.normalize(category.getCategoryName()), category);
            return true;
        }
        return false;
    }

}
