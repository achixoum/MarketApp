package api;

import api.objects.Category;
import api.objects.SubCategory;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;

public class CategoryTest {

    @Test
    public void testGetCategoryName() {
        Category category = new Category("Φρέσκα τρόφιμα");
        assertEquals("The getCategoryName method failed", "Φρέσκα τρόφιμα", category.getCategoryName());
    }

    @Test
    public void testSetCategoryName() {
        Category category = new Category("Κατεψυγμένα τρόφιμα");
        category.setCategoryName("Φρέσκα τρόφιμα");
        assertEquals("The method setCategoryName failed","Φρέσκα τρόφιμα" , category.getCategoryName());
    }

    @Test
    public void testAddSubCategory() {
        Category category = new Category("Φρέσκα τρόφιμα");
        SubCategory subCategory = new SubCategory("Φρούτα");

        assertTrue("Method addSubcategory failed",category.addSubCategory(subCategory));
        assertEquals(1,category.getAllSubCategories(false).size());
    }

    @Test
    public void testAddSubCategoryDuplicate() {
        Category category = new Category("Φρέσκα τρόφιμα");
        SubCategory subCategory = new SubCategory("Φρούτα");

        category.addSubCategory(subCategory);
        assertFalse("Method addSubcategory didnt found duplicate", category.addSubCategory(new SubCategory("Φρούτα")));
    }

    @Test
    public void testGetSubCategorySuccess() {
        Category category = new Category("Φρέσκα τρόφιμα");
        SubCategory subCategory = new SubCategory("Φρούτα");

        category.addSubCategory(subCategory);

        assertNotNull("Method failed to return the subcategory",category.getSubCategory("Φρούτα"));
        assertEquals("The name of the subcategory returned is not correct", "Φρούτα", category.getSubCategory("Φρούτα").getTitle());
    }

    @Test
    public void testGetSubCategoryNotFound() {
        Category category = new Category("Φρέσκα τρόφιμα");
        assertNull("Method GetSubCategory failed to return null when subcategory doesnt exist", category.getSubCategory("Λαχανικά"));
    }

    @Test
    public void testGetAllSubcategoriesWhenIsEmpty() {
        Category category = new Category("Φρέσκα τρόφιμα");
        assertEquals("Method GetAllSubCategories failed to return null when is empty", 0, category.getAllSubCategories(false).size());
    }

    @Test
    public void testGetAllSubCategoriesWhenNotEmpty() {
        Category category = new Category("Φρέσκα τρόφιμα");
        SubCategory subCategory1 = new SubCategory("Φρούτα");
        SubCategory subCategory2 = new SubCategory("Λαχανικά");

        category.addSubCategory(subCategory1);
        category.addSubCategory(subCategory2);

        HashMap<String, SubCategory> result = category.getAllSubCategories(false);

        assertEquals("The size of the subcategories is not correct",2, result.size());
        assertTrue("The subCategory didnt found", category.getAllSubCategories(false).containsKey(StringUtil.normalize("Φρούτα")));
        assertTrue("The subCategory didnt found", category.getAllSubCategories(false).containsKey(StringUtil.normalize("Λαχανικά")));
    }
}
