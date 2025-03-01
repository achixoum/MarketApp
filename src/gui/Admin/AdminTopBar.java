package gui.Admin;

import api.Database;
import api.objects.Category;
import api.objects.SubCategory;
import gui.Login.Logging;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;


public class AdminTopBar extends Component {
    private Database database; // Reference to the Database object
    private JComboBox<String> subCategoryDropdown; // Subcategory dropdown component
    private JComboBox<String> categoryDropdown; // Category dropdown component
    private api.users.Admin admin; // Reference to the Admin object
    private AdminSearch adminSearch; // Reference to the AdminSearch object for searching functionality
    private AdminMenu adminMenu; // Reference to the AdminMenu for managing product views
    private JFrame parentFrame; // Reference to the parent frame (to close on logout)

    // Constructor to initialize the necessary components and references
    public AdminTopBar(Database db, api.users.Admin admin, AdminSearch adminSearch, AdminMenu adminMenu, JFrame parentFrame) {
        this.database = db; // Store the passed Database instance
        this.admin = admin; // Store the Admin object
        this.adminSearch = adminSearch; // Store AdminSearch object
        this.adminMenu = adminMenu; // Store AdminMenu object
        this.parentFrame = parentFrame; // Store the parent JFrame
    }

    // Method to create the top menu panel
    public JPanel createTopMenu() {
        JPanel topMenu = new JPanel();
        topMenu.setLayout(new GridBagLayout());
        topMenu.setPreferredSize(new Dimension(getWidth(), 50)); // Set the height of the top menu
        topMenu.setBackground(new Color(144, 238, 144)); // Light green background for the menu
        topMenu.setBorder(BorderFactory.createLineBorder(new Color(169, 169, 169), 2)); // Add a soft grey border

        // GridBagConstraints for layout management
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Add some space around components

        // Menu Button (Hamburger Icon) with a green background and white icon color
        JButton menuButton = new JButton("\u2630"); // Unicode for hamburger menu icon
        menuButton.setPreferredSize(new Dimension(30, 30)); // Set button size
        menuButton.setBackground(new Color(34, 139, 34)); // Set green background
        menuButton.setForeground(Color.WHITE); // Set white icon color
        menuButton.setFocusPainted(false); // Remove the focus outline
        JPopupMenu menuPopup = new JPopupMenu(); // Popup menu for product management options

        // Create menu items for the popup menu with orange accents
        JMenuItem addProductItem = new JMenuItem("Add Product");
        addProductItem.setBackground(new Color(255, 165, 0)); // Orange background
        JMenuItem unavailableProductsItem = new JMenuItem("Unavailable Products");
        unavailableProductsItem.setBackground(new Color(255, 165, 0)); // Orange background
        JMenuItem mostOrderedProductsItem = new JMenuItem("Most Ordered Products");
        mostOrderedProductsItem.setBackground(new Color(255, 165, 0)); // Orange background
        JMenuItem allProductsItem = new JMenuItem("All Products");
        allProductsItem.setBackground(new Color(255, 165, 0)); // Orange background

        // Add actions for each menu item to display the corresponding product management view
        addProductItem.addActionListener(e -> adminMenu.showAddProductForm());
        unavailableProductsItem.addActionListener(e -> adminMenu.showUnavailableProducts());
        mostOrderedProductsItem.addActionListener(e -> adminMenu.showMostOrderedProducts());
        allProductsItem.addActionListener(e -> adminMenu.showAllProducts());

        // Add the menu items to the popup menu
        menuPopup.add(addProductItem);
        menuPopup.add(unavailableProductsItem);
        menuPopup.add(mostOrderedProductsItem);
        menuPopup.add(allProductsItem);

        // Show the menu on button click
        menuButton.addActionListener(e -> menuPopup.show(menuButton, 0, menuButton.getHeight()));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        topMenu.add(menuButton, gbc);

        // AtomicBoolean flags to control initialization state of the dropdowns
        final AtomicBoolean isCategoryInitialized = new AtomicBoolean(false);
        final AtomicBoolean isSubCategoryInitialized = new AtomicBoolean(false);

        // Subcategory Dropdown
        subCategoryDropdown = new JComboBox<>();
        subCategoryDropdown.addItem("Select Subcategory");
        adminSearch.setSubCategoryDropdown(subCategoryDropdown); // Set the dropdown reference in adminSearch
        subCategoryDropdown.setPreferredSize(new Dimension(100, 30)); // Set the size of the dropdown
        subCategoryDropdown.setBackground(new Color(34, 139, 34)); // Green background
        subCategoryDropdown.setForeground(Color.WHITE); // White text color
        subCategoryDropdown.addActionListener(e -> {
            if (!isSubCategoryInitialized.get()) return; // Prevent handling during initialization

            String selectedSubCategory = (String) subCategoryDropdown.getSelectedItem();
            if (!"Select Subcategory".equals(selectedSubCategory) && !"No Subcategories Available".equals(selectedSubCategory)) {
                // Fetch the selected category and subcategory from the database
                String selectedCategory = (String) categoryDropdown.getSelectedItem();
                Category category = database.getInventory().getCategoryManager().getCategory(selectedCategory);
                if (category != null) {
                    SubCategory subCategory = category.getSubCategory(selectedSubCategory);
                    if (subCategory != null) {
                        adminSearch.showSubcategoryProducts(subCategory, category); // Display the products for the subcategory
                    }
                }
            }
        });

        // Add subcategory dropdown to the menu
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        topMenu.add(subCategoryDropdown, gbc);

        // Category Dropdown
        categoryDropdown = new JComboBox<>();
        categoryDropdown.addItem("Select Category");

        // Fetch and populate categories
        HashMap<String, Category> categories = database.getInventory().getCategoryManager().getAllCategories(true);
        if (categories != null && !categories.isEmpty()) {
            categories.forEach((key, category) -> categoryDropdown.addItem(key)); // Add category names to the dropdown
        } else {
            categoryDropdown.addItem("No Categories Available");
        }

        categoryDropdown.setPreferredSize(new Dimension(100, 30)); // Set size for category dropdown
        categoryDropdown.addActionListener(e -> {
            if (!isCategoryInitialized.get()) return; // Prevent handling during initialization

            String selectedCategory = (String) categoryDropdown.getSelectedItem();
            if (!"Select Category".equals(selectedCategory)) {
                isSubCategoryInitialized.set(false); // Disable subcategory updates during initialization
                adminSearch.updateSubcategoryDropdown(selectedCategory); // Update subcategory dropdown based on category
                isSubCategoryInitialized.set(true); // Re-enable subcategory updates
                adminSearch.showCategoryProducts(selectedCategory); // Show products for the selected category
            }
        });

        categoryDropdown.setBackground(new Color(34, 139, 34)); // Green background
        categoryDropdown.setForeground(Color.WHITE); // White text color
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        topMenu.add(categoryDropdown, gbc);

        // Initialize flags after adding listeners to avoid unnecessary event handling
        isCategoryInitialized.set(true);
        isSubCategoryInitialized.set(true);

        // Search Bar with light grey background and rounded corners
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(400, 30)); // Set larger size for the search field
        searchField.setBackground(new Color(240, 240, 240)); // Light grey background for search field
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 0.5;  // Make search field the largest component in the menu
        topMenu.add(searchField, gbc);

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(34, 139, 34)); // Green background for the search button
        searchButton.setForeground(Color.WHITE); // White text color
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            if (searchText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a search term.");
                return;
            }
            adminSearch.showSearchResults(searchText);  // Show search results based on the entered text
        });
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        topMenu.add(searchButton, gbc);

        // User Menu with orange accents
        JButton userButton = new JButton("\uD83D\uDC64"); // Unicode for person icon
        userButton.setBackground(new Color(255, 165, 0)); // Orange background for user button
        userButton.setForeground(Color.WHITE); // White text color
        JPopupMenu userMenu = new JPopupMenu();
        JMenuItem detailsItem = new JMenuItem("Details");
        JMenuItem logoutItem = new JMenuItem("Logout");

        userMenu.add(detailsItem);
        userMenu.add(logoutItem);

        detailsItem.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
            }
        });
        // Action listeners for user menu items
        logoutItem.addActionListener(e -> {
            parentFrame.dispose(); // Close the parent frame (log out)
            new Logging(database).setVisible(true); // Show the login screen
        });

        // Show the user menu when the user button is clicked
        userButton.addActionListener(e -> userMenu.show(userButton, userButton.getWidth() / 2, userButton.getHeight() / 2));
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        topMenu.add(userButton, gbc);

        return topMenu; // Return the constructed top menu panel
    }
}
