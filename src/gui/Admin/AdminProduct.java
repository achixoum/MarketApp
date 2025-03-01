package gui.Admin;

import api.Database;
import api.objects.Product;

import javax.swing.*;
import java.awt.*;

public class AdminProduct extends Component {

    private AdminMain main;
    private Database database; // Reference to the existing Database object
    private api.users.Admin admin;

    // Constructor to initialize the main component, database and admin user
    public AdminProduct(AdminMain main, Database db, api.users.Admin admin) {
        this.main = main;
        this.database = db;
        this.admin = admin;
    }

    // Method to show the details of a product
    public void showProductDetails(Product product) {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BorderLayout());
        detailsPanel.setBackground(Color.WHITE);

        // Title for the product details
        JLabel titleLabel = new JLabel("Product Details: " + product.getTitle(), JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        detailsPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel to display the product's attributes
        JPanel productDetailsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        productDetailsPanel.setBackground(Color.WHITE);

        // Add labels and product data
        productDetailsPanel.add(new JLabel("Title:"));
        productDetailsPanel.add(new JLabel(product.getTitle()));
        productDetailsPanel.add(new JLabel("Description:"));
        productDetailsPanel.add(new JLabel(product.getDescription()));
        productDetailsPanel.add(new JLabel("Category:"));
        productDetailsPanel.add(new JLabel(product.getCategory()));
        productDetailsPanel.add(new JLabel("Subcategory:"));
        productDetailsPanel.add(new JLabel(product.getSubcategory()));
        productDetailsPanel.add(new JLabel("Price:"));
        productDetailsPanel.add(new JLabel(String.format("%.2f €", product.getPrice())));
        productDetailsPanel.add(new JLabel("Available Quantity:"));
        productDetailsPanel.add(new JLabel(String.valueOf(product.getQuantityAvailable())));

        detailsPanel.add(productDetailsPanel, BorderLayout.CENTER);

        // Panel for action buttons (Edit, Remove, Update Quantity)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        JButton editButton = new JButton("Edit Product");
        JButton removeButton = new JButton("Remove Product");
        JButton updateQuantityButton = new JButton("Update Quantity");

        // Style buttons
        for (JButton button : new JButton[]{editButton, removeButton, updateQuantityButton}) {
            button.setFont(new Font("Arial", Font.PLAIN, 14));
            button.setBackground(Color.LIGHT_GRAY);
            button.setFocusPainted(false);
        }

        // Action listener for each button
        editButton.addActionListener(e -> showEditProductForm(product)); // Open edit form
        removeButton.addActionListener(e -> removeProduct(product)); // Remove product from inventory
        updateQuantityButton.addActionListener(e -> {updateProductQuantity(product);}); // Update the product's quantity

        // Add buttons to the button panel
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(updateQuantityButton);

        detailsPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Back button to return to the main menu page
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setBackground(Color.WHITE);
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> main.showMainPage()); // Return to main page
        backButtonPanel.add(backButton);
        detailsPanel.add(backButtonPanel, BorderLayout.NORTH);

        // Update the main panel to show the product details
        main.updateCenterPanel(detailsPanel);
    }

    // Method to remove the product from inventory
    private void removeProduct(Product product) {
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this product?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            boolean success = admin.removeProduct(product, database.getInventory());
            JOptionPane.showMessageDialog(this, success ? "Product removed successfully!" : "Failed to remove product.");
            main.showMainPage(); // Return to main page
        }
    }

    // Method to update the product's quantity in the inventory
    private void updateProductQuantity(Product product) {
        // Use main (the JFrame) as the parent component
        String quantityStr = JOptionPane.showInputDialog(main, "Enter new quantity:");

        if (quantityStr != null) {
            try {
                double newQuantity = Double.parseDouble(quantityStr);
                admin.updateQuantityOfProduct(product, database.getInventory(), newQuantity); // Update the quantity
                JOptionPane.showMessageDialog(main, "Quantity updated successfully!");
                showProductDetails(product); // Refresh details
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(main, "Invalid quantity input.");
            }
        }
    }

    // Method to show the form to edit the product details
    public void showEditProductForm(Product product) {
        // Create the form panel with a grid layout
        JPanel editFormPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        editFormPanel.setBackground(Color.WHITE);

        // Create text fields for each product detail
        JTextField titleField = new JTextField(product.getTitle());
        JTextField descriptionField = new JTextField(product.getDescription());
        JTextField categoryField = new JTextField(product.getCategory());
        JTextField subcategoryField = new JTextField(product.getSubcategory());
        JTextField priceField = new JTextField(String.valueOf(product.getPrice()));
        JTextField quantityField = new JTextField(String.valueOf(product.getQuantityAvailable()));

        // ComboBox for selecting the measurement unit
        JComboBox<Product.MeasurementUnit> unitComboBox = new JComboBox<>(Product.MeasurementUnit.values());
        unitComboBox.setSelectedItem(product.getUnit());

        // Add labels and text fields to the form
        editFormPanel.add(new JLabel("Title:"));
        editFormPanel.add(titleField);
        editFormPanel.add(new JLabel("Description:"));
        editFormPanel.add(descriptionField);
        editFormPanel.add(new JLabel("Category:"));
        editFormPanel.add(categoryField);
        editFormPanel.add(new JLabel("Subcategory:"));
        editFormPanel.add(subcategoryField);
        editFormPanel.add(new JLabel("Price (€):"));
        editFormPanel.add(priceField);
        editFormPanel.add(new JLabel("Quantity:"));
        editFormPanel.add(quantityField);
        editFormPanel.add(new JLabel("Unit:"));
        editFormPanel.add(unitComboBox);

        // Panel for buttons (Save and Cancel)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton saveButton = new JButton("Save Changes");
        JButton cancelButton = new JButton("Cancel");

        // Style the buttons
        for (JButton button : new JButton[]{saveButton, cancelButton}) {
            button.setFont(new Font("Arial", Font.PLAIN, 14));
            button.setBackground(Color.LIGHT_GRAY);
            button.setFocusPainted(false);
        }

        // Save button action to update product details
        saveButton.addActionListener(e -> {
            try {
                // Validate and create a new product object with updated fields
                double price = Double.parseDouble(priceField.getText().trim());
                double quantity = Double.parseDouble(quantityField.getText().trim());

                if (price < 0 || quantity < 0) {
                    System.out.println("cringe");
                    throw new NumberFormatException("Negative values are not allowed.");
                }

                // Create a new product with the updated values
                Product editedProduct = new Product(
                        titleField.getText().trim(),
                        descriptionField.getText().trim(),
                        categoryField.getText().trim(),
                        subcategoryField.getText().trim(),
                        price,
                        quantity,
                        (Product.MeasurementUnit) unitComboBox.getSelectedItem()  // Get the selected unit
                );

                // Update the product in the inventory
                admin.EditProduct(product, editedProduct, database.getInventory());

                // Show success message
                JOptionPane.showMessageDialog(this, "Product updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Refresh the product details
                showProductDetails(product);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values for price and quantity.\n" +
                        "Ensure no negative values are entered.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "An error occurred while updating the product.\n" +
                        "Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Cancel button action to return to product details
        cancelButton.addActionListener(e -> showProductDetails(product));

        // Add buttons to the button panel
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Wrap the form in a main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(new JLabel("Edit Product Details", JLabel.CENTER), BorderLayout.NORTH);
        mainPanel.add(editFormPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Display the edit form
        main.updateCenterPanel(mainPanel);
    }
}
