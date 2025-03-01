package gui.Admin;

import api.Database;
import api.objects.Category;
import api.objects.Product;
import api.objects.SubCategory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AdminSearch extends Component {

    private Database database; // Αναφορά στο αντικείμενο Database για πρόσβαση στην βάση δεδομένων
    private JPanel mainPanel; // Κεντρικό πάνελ περιεχομένου
    private JComboBox<String> subCategoryDropdown; // Dropdown για τις υποκατηγορίες
    private AdminMain adminMain; // Αναφορά στην κεντρική οθόνη του διαχειριστή
    private AdminProduct adminProduct; // Αναφορά στην οθόνη των προϊόντων του διαχειριστή
    private api.users.Admin admin; // Αναφορά στο αντικείμενο του χρήστη διαχειριστή

    /**
     * Κατασκευαστής της κλάσης που αρχικοποιεί το περιβάλλον εργασίας.
     *
     * @param adminMain Αναφορά στην κεντρική οθόνη του διαχειριστή
     * @param adminProduct Αναφορά στην οθόνη των προϊόντων
     * @param db Αναφορά στη βάση δεδομένων
     * @param mainPanel Αναφορά στο βασικό πάνελ
     * @param admin Αναφορά στο αντικείμενο του διαχειριστή
     */
    public AdminSearch(AdminMain adminMain, AdminProduct adminProduct, Database db, JPanel mainPanel , api.users.Admin admin) {
        this.adminMain = adminMain;
        this.adminProduct = adminProduct;
        this.database = db;
        this.mainPanel = mainPanel;
        this.admin = admin;
    };

    // Μέθοδος για την ενημέρωση του dropdown με τις υποκατηγορίες
    public void setSubCategoryDropdown(JComboBox<String> subCategoryDropdown) {
        this.subCategoryDropdown = subCategoryDropdown;
    }

    /**
     * Εμφανίζει τα αποτελέσματα αναζήτησης με βάση το κείμενο αναζήτησης.
     * Κάθε προϊόν εμφανίζεται ως κουμπί, και όταν πατηθεί, ανοίγει τις λεπτομέρειες του προϊόντος.
     *
     * @param searchText Το κείμενο για την αναζήτηση
     */
    public void showSearchResults(String searchText) {
        JLabel titleLabel = new JLabel("SEARCH RESULTS", JLabel.CENTER); // Τίτλος για τα αποτελέσματα αναζήτησης
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel productPanel = new JPanel(new BorderLayout()); // Δημιουργία πάνελ για τα προϊόντα
        productPanel.setBackground(Color.WHITE);
        productPanel.add(titleLabel, BorderLayout.NORTH);

        // Δημιουργία ρολού για την εμφάνιση προϊόντων με FlowLayout
        JPanel flowPanel = new JPanel();
        flowPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Στρατηγική διάταξης για τα προϊόντα
        flowPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(flowPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        productPanel.add(scrollPane, BorderLayout.CENTER);

        // Χρήση SwingWorker για φόρτωση των προϊόντων στο παρασκήνιο
        SwingWorker<Void, JButton> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Αναζήτηση προϊόντων με βάση το κείμενο αναζήτησης
                List<Product> searchResults = database.getInventory().getProductManager().searchProduct(searchText, null, null, true);

                // Δημιουργία κουμπιών για κάθε προϊόν και προσθήκη τους στο flowPanel
                for (Product product : searchResults) {
                    String productTitle = product.getTitle();
                    String description = product.getDescription();
                    String price = String.format("%.2f €", product.getPrice());

                    String buttonText = String.format(
                            "<html><b style='font-size:14px;'>%s</b><br><span style='font-size:12px; color:#999999;'>%s</span><br><span style='color:green; font-size:14px;'>%s</span></html>",
                            productTitle, description, price
                    );

                    JButton productButton = new JButton(buttonText);
                    productButton.setPreferredSize(new Dimension(300, 180)); // Προσαρμογή του μεγέθους του κουμπιού
                    productButton.setHorizontalAlignment(SwingConstants.CENTER);
                    productButton.setVerticalTextPosition(SwingConstants.CENTER);
                    productButton.setFocusPainted(false);

                    // Προσθήκη δράσης για να δείξει τις λεπτομέρειες του προϊόντος όταν πατηθεί
                    productButton.addActionListener(e -> adminProduct.showProductDetails(product));

                    // Δημοσίευση του κουμπιού στο flowPanel
                    publish(productButton);
                }
                return null;
            }

            @Override
            protected void process(List<JButton> buttons) {
                // Προσθήκη των κουμπιών στο flowPanel
                for (JButton button : buttons) {
                    flowPanel.add(button);
                }
                flowPanel.revalidate();
                flowPanel.repaint();
            }
        };

        worker.execute(); // Εκτέλεση της εργασίας στο παρασκήνιο

        // Δημιουργία κουμπιού "Back" για επιστροφή στην κεντρική σελίδα
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {adminMain.showMainPage();});

        buttonPanel.add(backButton);

        productPanel.add(buttonPanel, BorderLayout.SOUTH);

        adminMain.updateCenterPanel(productPanel); // Ενημέρωση του κεντρικού πάνελ με τα αποτελέσματα αναζήτησης
    }

    /**
     * Ενημερώνει το dropdown των υποκατηγοριών βάσει της επιλεγμένης κατηγορίας.
     *
     * @param selectedCategory Η επιλεγμένη κατηγορία
     */
    public void updateSubcategoryDropdown(String selectedCategory) {
        // Ανάκτηση της κατηγορίας από τη βάση δεδομένων
        Category category = database.getInventory().getCategoryManager().getCategory(selectedCategory);
        List<SubCategory> subCategories = category != null ? new ArrayList<>(category.getAllSubCategories(true).values()) : new ArrayList<>();

        // Καθαρισμός υπαρχόντων στοιχείων στο dropdown
        subCategoryDropdown.removeAllItems();

        if (subCategories.isEmpty()) {
            // Αν δεν υπάρχουν υποκατηγορίες, εμφάνιση μηνύματος
            subCategoryDropdown.addItem("No Subcategories Available");
        } else {
            // Αν υπάρχουν υποκατηγορίες, προσθήκη τους στο dropdown
            subCategoryDropdown.addItem("Select Subcategory"); // Προεπιλεγμένη επιλογή
            for (SubCategory subCategory : subCategories) {
                subCategoryDropdown.addItem(subCategory.getTitle());
            }
        }

        // Ενημέρωση του dropdown και επιστροφή στην αρχική επιλογή
        subCategoryDropdown.setSelectedItem("Select Subcategory");

        subCategoryDropdown.revalidate();
        subCategoryDropdown.repaint();
    }

    /**
     * Εμφανίζει τα προϊόντα για μια υποκατηγορία.
     *
     * @param subCategory Η υποκατηγορία που έχει επιλεγεί
     * @param category Η κατηγορία του προϊόντος
     */
    public void showSubcategoryProducts(SubCategory subCategory, Category category) {
        if (subCategory != null) {
            // Ανάκτηση των προϊόντων για την υποκατηγορία
            ArrayList<Product> products = database.getInventory().getProductManager().searchProducts(category.getCategoryName(), subCategory.getTitle(), true);

            JLabel titleLabel = new JLabel(subCategory.getTitle().toUpperCase() + " PRODUCTS", JLabel.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

            JPanel productPanel = new JPanel(new BorderLayout());
            productPanel.setBackground(Color.WHITE);
            productPanel.add(titleLabel, BorderLayout.NORTH);

            JPanel flowPanel = new JPanel();
            flowPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
            flowPanel.setBackground(Color.WHITE);

            JScrollPane scrollPane = new JScrollPane(flowPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            productPanel.add(scrollPane, BorderLayout.CENTER);

            // SwingWorker για τη φόρτωση των προϊόντων στο παρασκήνιο
            SwingWorker<Void, JButton> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    for (Product product : products) {
                        String buttonText = String.format(
                                "<html><b style='font-size:14px;'>%s</b><br><span style='font-size:12px; color:#999;'>%s</span><br><span style='color:green; font-size:14px;'>%.2f €</span></html>",
                                product.getTitle(), product.getDescription(), product.getPrice()
                        );

                        JButton productButton = new JButton(buttonText);
                        productButton.setPreferredSize(new Dimension(200, 180));
                        productButton.setHorizontalAlignment(SwingConstants.CENTER);
                        productButton.setVerticalTextPosition(SwingConstants.CENTER);
                        productButton.setFocusPainted(false);

                        // Προσθήκη δράσης για να δείξει τις λεπτομέρειες του προϊόντος όταν πατηθεί
                        productButton.addActionListener(e -> adminProduct.showProductDetails(product));

                        publish(productButton);
                    }
                    return null;
                }

                @Override
                protected void process(List<JButton> buttons) {
                    for (JButton button : buttons) {
                        flowPanel.add(button);
                    }
                    flowPanel.revalidate();
                    flowPanel.repaint();
                }
            };

            worker.execute();

            // Δημιουργία κουμπιού "Back" για επιστροφή στην κεντρική σελίδα
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(Color.WHITE);
            JButton backButton = new JButton("Back");
            backButton.setFont(new Font("Arial", Font.PLAIN, 14));
            backButton.setBackground(Color.LIGHT_GRAY);
            backButton.setFocusPainted(false);
            backButton.addActionListener(e -> adminMain.showMainPage());
            buttonPanel.add(backButton);

            productPanel.add(buttonPanel, BorderLayout.SOUTH);

            adminMain.updateCenterPanel(productPanel); // Ενημέρωση του κεντρικού πάνελ με τα προϊόντα της υποκατηγορίας
        } else {
            JOptionPane.showMessageDialog(this, "Subcategory not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Εμφανίζει τα προϊόντα για μια κατηγορία.
     *
     * @param categoryName Το όνομα της κατηγορίας
     */
    public void showCategoryProducts(String categoryName) {
        Category category = database.getInventory().getCategoryManager().getCategory(categoryName);

        if (category != null) {
            // Ανάκτηση των προϊόντων για την κατηγορία
            ArrayList<Product> products = database.getInventory().getProductManager().searchProducts(categoryName, null, true);

            JLabel titleLabel = new JLabel(categoryName.toUpperCase() + " PRODUCTS", JLabel.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

            JPanel productPanel = new JPanel(new BorderLayout());
            productPanel.setBackground(Color.WHITE);
            productPanel.add(titleLabel, BorderLayout.NORTH);

            JPanel flowPanel = new JPanel();
            flowPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
            flowPanel.setBackground(Color.WHITE);

            JScrollPane scrollPane = new JScrollPane(flowPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            productPanel.add(scrollPane, BorderLayout.CENTER);

            SwingWorker<Void, JButton> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    for (Product product : products) {
                        String buttonText = String.format(
                                "<html><b style='font-size:14px;'>%s</b><br><span style='font-size:12px; color:#999;'>%s</span><br><span style='color:green; font-size:14px;'>%.2f €</span></html>",
                                product.getTitle(), product.getDescription(), product.getPrice()
                        );

                        JButton productButton = new JButton(buttonText);
                        productButton.setPreferredSize(new Dimension(200, 180));
                        productButton.setHorizontalAlignment(SwingConstants.CENTER);
                        productButton.setVerticalTextPosition(SwingConstants.CENTER);
                        productButton.setFocusPainted(false);

                        productButton.addActionListener(e -> adminProduct.showProductDetails(product));

                        publish(productButton);
                    }
                    return null;
                }

                @Override
                protected void process(List<JButton> buttons) {
                    for (JButton button : buttons) {
                        flowPanel.add(button);
                    }
                    flowPanel.revalidate();
                    flowPanel.repaint();
                }
            };

            worker.execute(); // Εκτέλεση της εργασίας

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(Color.WHITE);
            JButton backButton = new JButton("Back");
            backButton.setFont(new Font("Arial", Font.PLAIN, 14));
            backButton.setBackground(Color.LIGHT_GRAY);
            backButton.setFocusPainted(false);
            backButton.addActionListener(e -> adminMain.showMainPage());
            buttonPanel.add(backButton);

            productPanel.add(buttonPanel, BorderLayout.SOUTH);

            adminMain.updateCenterPanel(productPanel); // Ενημέρωση του κεντρικού πάνελ με τα προϊόντα της κατηγορίας
        } else {
            JOptionPane.showMessageDialog(this, "Category not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
