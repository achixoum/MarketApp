package gui.Admin;

import api.Database;
import api.objects.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import java.util.Map;
import java.util.stream.Collectors;

// AdminMenu class handles various administrative tasks related to products
public class AdminMenu {
    private AdminMain main; // Main UI container
    private Database database; // Database reference for data access
    private api.users.Admin admin; // Admin reference for product management tasks
    private AdminProduct adminProduct; // Reference to another GUI component for handling product details

    // Constructor: Initializes the admin menu with the provided main, adminProduct, database, and admin
    public AdminMenu(AdminMain main, AdminProduct adminProduct ,Database db, api.users.Admin admin ) {
        this.main = main;
        this.adminProduct = adminProduct;
        this.database = db;
        this.admin = admin;
    };

    // Show form for adding a new product to the system
    public void showAddProductForm() {
        JPanel formPanel = new JPanel(new BorderLayout());

        // Title label for the add product form
        JLabel titleLabel = new JLabel("ADD PRODUCT", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(34, 139, 34)); // Green text
        formPanel.add(titleLabel, BorderLayout.NORTH);

        // Input fields for product details
        JPanel inputPanel = new JPanel(new GridLayout(9, 2, 10, 10)); // Grid layout with labels and text fields
        inputPanel.setBackground(Color.WHITE);

        // Text fields for user input
        JTextField titleField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField subcategoryField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField priceField = new JTextField();
        JComboBox<String> unitComboBox = new JComboBox<>(new String[]{"KILOS", "PIECES"});

        // Buttons for submitting or going back
        JButton submitButton = new JButton("Add Product");
        JButton backButton = new JButton("Back");

        // Style the buttons
        submitButton.setBackground(new Color(144, 238, 144)); // Light green for submit
        submitButton.setForeground(Color.WHITE); // White text
        backButton.setBackground(new Color(34, 139, 34)); // Dark green for back button
        backButton.setForeground(Color.WHITE);

        // Add fields and labels to input panel
        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);
        inputPanel.add(new JLabel("Subcategory:"));
        inputPanel.add(subcategoryField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Unit:"));
        inputPanel.add(unitComboBox);
        inputPanel.add(new JLabel());
        inputPanel.add(submitButton);
        inputPanel.add(new JLabel());
        inputPanel.add(backButton);

        formPanel.add(inputPanel, BorderLayout.CENTER);

        // Submit button action listener
        submitButton.addActionListener(e -> {
            try {
                String title = titleField.getText().trim();
                String description = descriptionField.getText().trim();
                String category = categoryField.getText().trim();
                String subcategory = subcategoryField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                double quantity = Double.parseDouble(quantityField.getText().trim());
                String unitInput = (String) unitComboBox.getSelectedItem();
                Product.MeasurementUnit unit = Product.MeasurementUnit.valueOf(unitInput.toUpperCase());

                // Create a new Product object
                Product product = new Product(title, description, category, subcategory, price, quantity, unit);

                // Validate the product before adding
                if (!product.validateProduct()) {
                    JOptionPane.showMessageDialog(null, "Invalid product details. Please review the fields.");
                    return;
                }

                // Add the product to the database and show success/failure message
                boolean success = database.getInventory().getProductManager().addProduct(product);
                JOptionPane.showMessageDialog(null, success ? "Product added successfully!" : "Failed to add product.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });

        // Back button action listener
        backButton.addActionListener(e -> main.showMainPage());

        // Update the center panel of the main UI with the form
        main.updateCenterPanel(formPanel);
    }

    // Show a list of unavailable products
    public void showUnavailableProducts() {
        JLabel titleLabel = new JLabel("UNAVAILABLE PRODUCTS", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setBackground(Color.WHITE);
        productPanel.add(titleLabel, BorderLayout.NORTH);

        // Fetch unavailable products from the admin
        List<Product> unavailableProducts = admin.getUnavailableProducts(database.getInventory());

        // Set up the column names for the table
        String[] columns = {"ΠΡΟΙΟΝ", "ΠΟΣΟΤΗΤΑ"};

        // Prepare the data for the table
        Object[][] data = new Object[unavailableProducts.size()][2];
        for (int i = 0; i < unavailableProducts.size(); i++) {
            data[i][0] = unavailableProducts.get(i).getTitle();
            data[i][1] = 0; // Quantity is 0 for unavailable products
        }

        // Create a table with the data and column names
        DefaultTableModel model = new DefaultTableModel(data, columns);
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);

        // Customize table appearance
        table.setBackground(Color.WHITE);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(30);

        // Add table to scroll pane and display
        JScrollPane scrollPane = new JScrollPane(table);
        productPanel.add(scrollPane, BorderLayout.CENTER);

        // Back button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> main.showMainPage());
        buttonPanel.add(backButton);

        productPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Update center panel of the main UI with product details
        main.updateCenterPanel(productPanel);
    }

    // Show a list of most ordered products
    public void showMostOrderedProducts() {
        JLabel titleLabel = new JLabel("MOST ORDERED PRODUCTS", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setBackground(Color.WHITE);
        productPanel.add(titleLabel, BorderLayout.NORTH);

        // Fetch and sort most ordered products
        List<Product> mostOrderedProducts = admin.getMostOrderProducts(database.getInventory());
        mostOrderedProducts.sort((p1, p2) -> Integer.compare(p2.getOrdersmade(), p1.getOrdersmade()));

        // Set up table columns and data
        String[] columns = {"ΠΡΟΙΟΝ", "ΠΑΡΑΓΓΕΛΙΕΣ"};
        Object[][] data = new Object[mostOrderedProducts.size()][2];
        for (int i = 0; i < mostOrderedProducts.size(); i++) {
            data[i][0] = mostOrderedProducts.get(i).getTitle();
            data[i][1] = mostOrderedProducts.get(i).getOrdersmade();
        }

        // Create table with data
        DefaultTableModel model = new DefaultTableModel(data, columns);
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);

        // Customize table appearance
        table.setBackground(Color.WHITE);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(30);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        productPanel.add(scrollPane, BorderLayout.CENTER);

        // Back button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> main.showMainPage());
        buttonPanel.add(backButton);

        productPanel.add(buttonPanel, BorderLayout.SOUTH);

        main.updateCenterPanel(productPanel);
    }

    // Show all products in a grid layout with buttons for each product
    public void showAllProducts() {
        JLabel titleLabel = new JLabel("ALL PRODUCTS", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setBackground(Color.WHITE);
        productPanel.add(titleLabel, BorderLayout.NORTH);

        // Scrollable grid panel for product buttons
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(0, 3, 10, 10)); // 3 columns, unlimited rows
        gridPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        productPanel.add(scrollPane, BorderLayout.CENTER);

        // Add buttons for each product using SwingWorker (background task)
        SwingWorker<Void, JButton> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                List<Product> allProducts = database.getInventory().getProductManager().getAllProducts(true);

                for (Product product : allProducts) {
                    String productTitle = product.getTitle();
                    String fullName = product.getDescription();
                    String price = String.format("%.2f €", product.getPrice());

                    // Create a button for each product with formatted HTML content
                    String buttonText = String.format(
                            "<html><b style='font-size:14px;'>%s</b><br><span style='font-size:12px; color:#999999;'>%s</span><br><span style='color:green;'>%s</span></html>",
                            productTitle, fullName, price
                    );

                    JButton productButton = new JButton(buttonText);
                    productButton.setPreferredSize(new Dimension(200, 150));
                    productButton.setHorizontalAlignment(SwingConstants.CENTER);
                    productButton.setFocusPainted(false);

                    // Add action listener for each button to show product details
                    productButton.addActionListener(e -> adminProduct.showProductDetails(product));

                    // Publish the button to add to UI
                    publish(productButton);
                }
                return null;
            }

            @Override
            protected void process(List<JButton> buttons) {
                for (JButton button : buttons) {
                    gridPanel.add(button); // Add button to grid
                }
                gridPanel.revalidate();
                gridPanel.repaint(); // Refresh grid after adding buttons
            }
        };

        worker.execute(); // Execute worker in background

        // Back button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> main.showMainPage());
        buttonPanel.add(backButton);

        productPanel.add(buttonPanel, BorderLayout.SOUTH);

        main.updateCenterPanel(productPanel);
    }
}
