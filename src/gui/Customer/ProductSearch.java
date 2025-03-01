package gui.Customer;

import api.Database;
import api.objects.Category;
import api.objects.Product;
import api.objects.SubCategory;
import api.users.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author Αχιλλέας
 * The ProductSearch GUI class represents a JPanel for searching and displaying products in a GUI.
 * It provides functionality to search products by title, category, or subcategory, and display the results dynamically in a scrollable panel.
 */
public class ProductSearch extends JPanel {
    private Customer customer;
    private Database database;
    private JTextField searchText;
    private ImageIcon searchIcon;
    private JButton searchButton;
    private JPanel productsPanel;
    private JScrollPane scrollPane;
    private JPanel titlePanel;
    private JLabel titleLabel;
    private final int productWidth = 250;
    private final int productHeight = 150;
    private GroupLayout productLayout;
    private JFrame owner;
    private GroupLayout.SequentialGroup verticalGroup;
    private GroupLayout.ParallelGroup horizontalGroup;
    private ArrayList<api.objects.Product> products;
    private JComboBox<String> categoryDropDown;
    private JComboBox<String> subcategoryDropDown;

    /**
     * Constructor that creates a new  ProductSearch panel.
     * @param customer the customer performing the product search.
     * @param database the database containing product and category data.
     * @param owner the parent JFrame.
     */
    public ProductSearch(Customer customer, Database database, JFrame owner) {
        this.database = database;
        this.customer = customer;
        this.owner = owner;
        setUp();
    }

    /**
     * Initializes the layout and components of the ProductSearch panel.
     */
    private void setUp() {
        //create a flow layout to put the text field and the button together
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT,0,0);
        setBackground(new Color(70, 130, 180));
        setLayout(layout);

        ArrayList<String> names = new ArrayList<>();
        for(Category cat : database.getInventory().getCategoryManager().getAllCategories(false).values()) {
            names.add(cat.getCategoryName());
        }
        names.add("Επίλεξε κατηγορία");
        String[] namesArray = names.toArray(new String[0]);
        categoryDropDown = new JComboBox<>(namesArray);
        categoryDropDown.setBackground(Color.WHITE);
        categoryDropDown.setForeground(new Color(50, 50, 50));
        categoryDropDown.setSelectedItem("Επίλεξε κατηγορία");
        subcategoryDropDown = new JComboBox<>();
        subcategoryDropDown.setBackground(Color.WHITE);
        subcategoryDropDown.addItem("Επίλεξε υποκατηγορία");
        subcategoryDropDown.setForeground(new Color(50, 50, 50));
        subcategoryDropDown.setSelectedItem("Επίλεξε υποκατηγορία");
        //create the button and the text field
        searchText = new JTextField();
        searchText.setPreferredSize(new Dimension(300,30));
        searchText.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));

        //scale the image so that can fit in the button
        searchIcon = new ImageIcon("images/Search.png");
        Image img = searchIcon.getImage().getScaledInstance(20,20, Image.SCALE_SMOOTH);
        searchIcon.setImage(img);

        //create the button with the icon centered
        searchButton = new JButton(searchIcon);
        searchButton.setHorizontalAlignment(SwingConstants.CENTER);
        searchButton.setVerticalAlignment(SwingConstants.CENTER);
        searchButton.setContentAreaFilled(true);
        //searchButton.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));

        titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(70, 130, 180));
        titleLabel = new JLabel("Search results will appear here.");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);

        //create the area where the products will be displayed
        productsPanel = new JPanel();
        productsPanel.setBackground(new Color(70, 130, 180));
        productLayout = new GroupLayout(productsPanel);
        productsPanel.setLayout(productLayout);
        scrollPane = new JScrollPane(productsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //add them to the panel
        add(categoryDropDown);
        add(subcategoryDropDown);
        add(searchText);
        add(searchButton);
        SearchResults();
    }

    /**
     * Add listeners for the text field and the search button to display products and the listeners
     * for the subcategory and category dropdowns to display the products also.
     */
    private void SearchResults() {
        searchText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayProductsWithTitle();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayProductsWithTitle();
            }
        });
        searchWithCategorySubCategory();
    }

    /**
     * update the {@code products} list by searching with method of the user {@code searchProduct} providing only the title
     * @param text the given text that will provide to search method
     */
    private void searchWithTitle(String text) {
        subcategoryDropDown.setEnabled(false);
        subcategoryDropDown.setSelectedItem("Επίλεξε υποκατηγορία");
        categoryDropDown.setSelectedItem("Επίλεξε κατηγορία");
        products = customer.searchProduct(text, null, null, database.getInventory());
        titleLabel.setText(text + " | " +products.size() + " Products found");
    }

    private void searchWithCategorySubCategory() {
        subcategoryDropDown.setEnabled(false);
        categoryDropDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!(categoryDropDown.getSelectedItem() == "Επίλεξε κατηγορία")) {
                    subcategoryDropDown.removeAllItems();
                    subcategoryDropDown.setEnabled(false);
                    subcategoryDropDown.addItem("Επίλεξε υποκατηγορία");
                    String category = (String) categoryDropDown.getSelectedItem();
                    products = customer.searchProducts(category, null, database.getInventory());
                    if (products != null)
                        titleLabel.setText(category + " | " + products.size() + " Products found");
                    setGroupLayout();
                    for (SubCategory sub : database.getInventory().getCategoryManager().getCategory(category).getAllSubCategories(false).values()) {
                        subcategoryDropDown.addItem(sub.getTitle());
                    }
                    subcategoryDropDown.setEnabled(true);
                    subcategoryDropDown.setSelectedItem("Επίλεξε υποκατηγορία");
                }
            }
        });
        subcategoryDropDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (subcategoryDropDown.isEnabled() && subcategoryDropDown.getItemCount() > 0 && !(subcategoryDropDown.getSelectedItem() == "Επίλεξε υποκατηγορία")) {
                    String subCategory = (String) subcategoryDropDown.getSelectedItem();
                    products = customer.searchProducts((String)categoryDropDown.getSelectedItem(), subCategory, database.getInventory());
                    if (products != null)
                        titleLabel.setText(subCategory + " | " +products.size() + " Products found");
                    setGroupLayout();
                }
            }
        });
    }

    /**
     * Configures the group layout for displaying products in the ui.
     * Clears existing components in the layout, resets the layout groups, and invokes the product display logic.
     */
    private void setGroupLayout() {
        productsPanel.removeAll();
        horizontalGroup = productLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addGap(0,0,Short.MAX_VALUE);
        verticalGroup = productLayout.createSequentialGroup();
        displayProducts();
        productLayout.setHorizontalGroup(horizontalGroup);
        productLayout.setVerticalGroup(verticalGroup);
        productsPanel.revalidate();
        productsPanel.repaint();
    }


    /**
     * Displays products in the UI based on the text entered in the search field. If the search field is empty, it shows an appropriate message,
     * otherwise, it searches for products by title and updates the display.
     */
    private void displayProductsWithTitle() {
        productsPanel.removeAll();

        horizontalGroup = productLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addGap(0,0,Short.MAX_VALUE);
        verticalGroup = productLayout.createSequentialGroup();

        //represents the layout of each row
        String searchedText = searchText.getText();

        if (searchedText.isEmpty()) {
            titleLabel.setText("No products found for: \"" + searchedText + "\"");
        }
        else {
            searchWithTitle(searchedText);
            if (products.isEmpty()) {
                titleLabel.setText("No products found for: \"" + searchedText + "\"");
            }
            displayProducts();
        }
        productLayout.setHorizontalGroup(horizontalGroup);
        productLayout.setVerticalGroup(verticalGroup);
        productsPanel.revalidate();
        productsPanel.repaint();
    }

    /**
     * Displays products in the product panel using a grid layout. Configures the number of products displayed per row and adds them to the layout.
     */
    private void displayProducts() {
        int ProductsInRow = 4;
        GroupLayout.SequentialGroup sequentialRowGroup = productLayout.createSequentialGroup().addGap(10);
        GroupLayout.ParallelGroup parallelRowGroup = productLayout.createParallelGroup(GroupLayout.Alignment.BASELINE);

        //dynamically adding groups to the main group layouts based on the products that are stored in products list
        int count = 0;
        for (Product product : products) {
            gui.Customer.Product p = new gui.Customer.Product(product, customer, owner, false);
            sequentialRowGroup.addComponent(p, productWidth, productWidth, productWidth).addGap(10);
            parallelRowGroup.addComponent(p, productHeight, productHeight, productHeight);
            count++;
            if (count == ProductsInRow) {
                horizontalGroup.addGroup(sequentialRowGroup).addGap(0, 0, Short.MAX_VALUE);
                verticalGroup.addGroup(parallelRowGroup).addGap(10);
                sequentialRowGroup = productLayout.createSequentialGroup().addGap(10);
                parallelRowGroup = productLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
                count = 0;
            }
        }
        if (count > 0) {
            horizontalGroup.addGroup(sequentialRowGroup);
            verticalGroup.addGroup(parallelRowGroup);
        }
    }

    /**
     * this method is for other classes to take the title panel made and place it where they want
     * @return the title panel
     */
    public JPanel titlePanel() {
        return titlePanel;
    }

    /**
     * this method is for other classes to take the scrollPane with the products and place it where they want
     * @return the scroll pane with the products
     */
    public JScrollPane getProductsScrollPane() {
        return scrollPane;
    }

    /**
     * Resets the search results and clears the product display.
     * This method also resets the dropdown menus to their default state and updates the title label.
     */
    public void resetSearchResults() {
        if (products != null){
            products.clear();
            setGroupLayout();
            setDropdowns();
            titleLabel.setText("Search results will appear here.");
        }
    }

    /**
     * Searches and displays all available products in the inventory.
     * This method resets dropdown menus, retrieves all products, and updates the product display.
     */
    public void searchAllProducts() {
        setDropdowns();
        products = customer.searchProducts(null, null, database.getInventory());
        titleLabel.setText(products.size() + " Products found");
        setGroupLayout();
    }

    /**
     * Resets the dropdown menus to their default states.
     * This includes disabling the subcategory dropdown and resetting selected items.
     */
    private void setDropdowns() {
        subcategoryDropDown.setEnabled(false);
        subcategoryDropDown.setSelectedItem("Επίλεξε υποκατηγορία");
        categoryDropDown.setSelectedItem("Επίλεξε κατηγορία");
    }
}
