package gui.Customer;

import api.users.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author Αχιλλέας
 * Represents a GUI panel for displaying a product and its details,
 * allowing the user to view detailed information and add the product to their cart.
 */
public class Product extends JPanel {
    private api.objects.Product product;
    private JButton viewDetails;
    private JButton addToCart;
    private Customer customer;
    private JFrame owner;
    private JPanel quantityPanel;
    private JDialog details;
    private final boolean disableAddToCart;

    /**
     * Constructor tha creates a Product panel
     * @param product the product to be displayed
     * @param customer the customer interacting with this panel
     * @param owner the parent JFrame
     * @param disableAddToCart whether the "Add to Cart" button should be disabled
     */
    public Product(api.objects.Product product, Customer customer, JFrame owner, boolean disableAddToCart) {
        this.product = product;
        this.customer = customer;
        this.owner = owner;
        this.disableAddToCart = disableAddToCart;
        viewProduct();
    }

    /**
     * Initializes the main components of the product panel and sets up the layout.
     */
    private void viewProduct() {
        GroupLayout groupLayout = new GroupLayout(this);
        setLayout(new BorderLayout(10, 10));
        setPreferredSize(new Dimension(200, 150));
        setBackground(new Color(220, 240, 255));
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // Create and add components
        JLabel titleLabel = new JLabel(product.getTitle());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

        JLabel descriptionLabel = new JLabel("<html>" + product.getDescription() + "<html>");
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));


        JLabel priceLabel = new JLabel(String.format("%.2f€", product.getPrice()));
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        priceLabel.setForeground(new Color(0, 128, 0)); // Green color for price


        viewDetails = new JButton("Details");
        viewDetails.setBackground(Color.WHITE);
        viewDetails.setFocusable(false);

        JPanel panel1 = new JPanel(new GridLayout(3, 1, 0, 10));
        panel1.setBackground(new Color(220, 240, 255));
        panel1.add(titleLabel);
        panel1.add(descriptionLabel);
        panel1.add(priceLabel);

        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel2.setBackground(new Color(220, 240, 255));
        panel2.add(viewDetails);

        add(panel1, BorderLayout.CENTER);
        add(panel2, BorderLayout.SOUTH);

        viewDetails();
    }

    /**
     * Sets up the "Details" button to display detailed product information in a dialog.
     */
    private void viewDetails() {
        viewDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //make a JDialog where
                details = new JDialog(owner,"Supermarket");
                details.setBackground(Color.BLACK);
                details.setModal(true);
                ImageIcon icon = new ImageIcon("images/SupermarketIcon.png");
                details.setIconImage(icon.getImage());
                details.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

                JLabel productName;
                JLabel productDescription;
                JLabel productCategory;
                JLabel productSubCategory;
                JLabel priceLabel;

                BorderLayout borderLayout = new BorderLayout(10, 10);
                details.setLayout(borderLayout);
                details.setSize(500, 300);
                details.setLocationRelativeTo(owner);

                // Product details panel
                JPanel detailsPanel = new JPanel(new GridLayout(5, 1, 5, 5));
                detailsPanel.setBackground(new Color(70, 130, 180));
                productName = new JLabel(product.getTitle());
                productName.setFont(new Font("Arial", Font.BOLD, 20));

                productDescription = new JLabel("<html>Description: "+ product.getDescription()+ "<html>");
                productDescription.setFont(new Font("Arial", Font.BOLD, 14));

                productCategory = new JLabel("Category: " + product.getCategory());
                productCategory.setFont(new Font("Arial", Font.BOLD, 14));

                productSubCategory = new JLabel("Subcategory: " + product.getSubcategory());
                productSubCategory.setFont(new Font("Arial", Font.BOLD, 14));

                priceLabel = new JLabel("Price: €" + product.getPrice());
                priceLabel.setFont(new Font("Arial", Font.BOLD, 16));

                detailsPanel.add(productName);
                detailsPanel.add(productDescription);
                detailsPanel.add(productCategory);
                detailsPanel.add(productSubCategory);
                detailsPanel.add(priceLabel);

                JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                addToCart = new JButton("Add to Cart");
                addToCart.setBackground(new Color(0,128,0));
                addToCart.setFocusable(false);
                if (!disableAddToCart) {
                    buttonsPanel.add(addToCart);
                }
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setFocusable(false);
                buttonsPanel.add(cancelButton);

                // Quantity panel
                quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                quantityPanel.add(new JLabel("Select Quantity: "));
                if (!disableAddToCart)
                    details.add(quantityPanel, BorderLayout.NORTH);
                if (product.getUnit() == api.objects.Product.MeasurementUnit.PIECES) {
                    addProductWithPieces();
                }
                else {
                    addProductWithKilos();
                }
                // Add components to dialog
                details.add(detailsPanel, BorderLayout.CENTER);
                details.add(buttonsPanel, BorderLayout.SOUTH);

                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        details.dispose();
                    }
                });
                details.setVisible(true);
            }
        });
    }

    /**
     * Configures the interface to add products measured in kilograms.
     */
    private void addProductWithKilos() {
        JTextField quantityField = new JTextField(5);
        quantityField.setText("0");
        quantityPanel.add(quantityField);
        quantityPanel.add(new JLabel("Kilos"));

        //action listener for the quantity text field to ignore the non digit characters when the user is typing
        quantityField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.' && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
                if (c == '.' && quantityField.getText().contains(".")) {
                    e.consume();
                }
            }
        });

        addToCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double quantity = 0;
                if (!quantityField.getText().isEmpty())
                    quantity = Double.parseDouble(quantityField.getText());

                ImageIcon warningIcon = new ImageIcon("images/warning.png");
                Image scaledImage = warningIcon.getImage().getScaledInstance(10,10, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                JLabel warning = new JLabel("Requested quantity is not available", scaledIcon, JLabel.LEADING);
                if (quantity <= 0) {
                    warning.setText("Quantity must be a positive number");
                    quantityPanel.add(warning);
                }
                else {
                    addToCart(quantity);
                }
                quantityPanel.revalidate();
                quantityPanel.repaint();
            }
        });
    }

    /**
     * Adds a product to the cart with the specified quantity.
     * @param quantity the quantity of the product to add
     */
    private void addToCart(double quantity) {
        ImageIcon warningIcon = new ImageIcon("images/warning.png");
        Image scaledImage = warningIcon.getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel warning = new JLabel("Requested quantity is not available", scaledIcon, JLabel.LEADING);
        if (product.getQuantityAvailable() < quantity) {
            quantityPanel.add(warning);
        } else {
            try{
                customer.addToCart(product, quantity);
                JOptionPane.showMessageDialog(details, "Product added to cart successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                details.dispose();
            }
            catch (IllegalArgumentException e) {
                quantityPanel.add(warning);
            }
        }
    }

    /**
     * Configures the interface to add products measured in pieces.
     */
    private void addProductWithPieces() {
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(0, 0, (int)product.getQuantityAvailable(), 1));
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) quantitySpinner.getEditor();
        JFormattedTextField textField = editor.getTextField();
        textField.setEditable(false);
        textField.setFocusable(false);
        quantityPanel.add(quantitySpinner);
        quantityPanel.add(new JLabel("Pieces"));
        addToCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int quantity = (int) quantitySpinner.getValue();
                addToCart(quantity);
                quantityPanel.revalidate();
                quantityPanel.repaint();
            }
        });
    }
}

