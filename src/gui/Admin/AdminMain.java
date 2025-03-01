package gui.Admin;

import api.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class AdminMain extends JFrame {

    private Database database; // Αναφορά στη βάση δεδομένων
    private JPanel mainPanel; // Κύριο panel περιεχομένων
    private JPanel topMenu;  // Panel για το πάνω μενού
    private JPanel ovalPanel; // Panel με στρογγυλεμένες γωνίες για το μήνυμα "Welcome"
    private api.users.Admin admin; // Χρήστης Admin που είναι συνδεδεμένος

    /**
     * Κατασκευαστής της κλάσης AdminMain.
     *
     * @param db Η βάση δεδομένων της εφαρμογής.
     * @param admin Ο χρήστης Admin.
     */
    public AdminMain(Database db, api.users.Admin admin) {
        this.database = db;
        this.admin = admin; // Αποθηκεύει τον χρήστη admin
        makeFrame(); // Δημιουργεί το παράθυρο του admin
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Δημιουργεί και ρυθμίζει το κύριο παράθυρο της εφαρμογής.
     */
    void makeFrame() {
        setTitle("Supermarket"); // Ο τίτλος του παραθύρου
        setSize(1280, 720); // Μέγεθος παραθύρου
        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension d = t.getScreenSize();
        setLocation((int) (d.getWidth() - getWidth()) / 2, (int) (d.getHeight() - getHeight()) / 2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon("images/SupermarketIcon.png");
        setIconImage(icon.getImage());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    database.WriteInventoryObject("Files/InventoryData.dat");
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        // Ρύθμιση Layout
        setLayout(new BorderLayout());

        // Δημιουργία του πάνω μενού
        AdminProduct adminProduct = new AdminProduct(this, database, admin);
        AdminSearch adminSearch = new AdminSearch(this, adminProduct, database, mainPanel, admin);
        AdminMenu adminMenu = new AdminMenu(this, adminProduct, database, admin);
        AdminTopBar a = new AdminTopBar(database, admin, adminSearch, adminMenu, this);
        topMenu = a.createTopMenu();

        // Προσθήκη του πάνω μενού στο παράθυρο
        add(topMenu, BorderLayout.NORTH);

        // Δημιουργία κύριου panel περιεχομένων με gradient φόντο
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Δημιουργία gradient εφέ από πράσινο σε λευκό
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(144, 238, 144), 0, getHeight(), Color.WHITE);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());

        // Panel με στρογγυλεμένες γωνίες γύρω από το μήνυμα καλωσορίσματος
        ovalPanel = new JPanel();
        ovalPanel.setBackground(new Color(34, 139, 34)); // Φωτεινό πράσινο φόντο για το οβάλ panel
        ovalPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Κεντράρισμα του κειμένου
        ovalPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // Περιθώριο γύρω από το οβάλ

        // Ετικέτα καλωσορίσματος
        JLabel welcomeLabel = new JLabel("Welcome to My Market");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28)); // Ρύθμιση γραμματοσειράς
        welcomeLabel.setForeground(Color.WHITE); // Άσπρο χρώμα κειμένου για αντίθεση

        ovalPanel.add(welcomeLabel);
        mainPanel.add(ovalPanel);

        // Προσθήκη του mainPanel στο κεντρικό μέρος της εφαρμογής
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true); // Καθορισμός του παραθύρου ως ορατό
    }

    /**
     * Εμφανίζει την κύρια σελίδα με το μενού.
     */
    public void showMainPage() {
        mainPanel.removeAll(); // Καθαρίζει το προηγούμενο περιεχόμενο του panel

        // Δημιουργία νέου panel για το καλωσόρισμα με στρογγυλεμένες γωνίες
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(new Color(34, 139, 34)); // Φωτεινό πράσινο φόντο
        welcomePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel welcomeLabel = new JLabel("Welcome to My Market");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE); // Άσπρο κείμενο για αντίθεση

        // Προσθήκη της ετικέτας στο οβάλ panel
        welcomePanel.add(welcomeLabel);

        // Προσθήκη του οβάλ panel στο κύριο panel
        mainPanel.add(welcomePanel);

        // Ενημέρωση του κεντρικού panel με το νέο περιεχόμενο
        updateCenterPanel(mainPanel);
    }

    /**
     * Ενημερώνει το κεντρικό panel της εφαρμογής.
     *
     * @param newPanel Το νέο panel που θα προστεθεί στο κέντρο.
     */
    public void updateCenterPanel(JPanel newPanel) {
        getContentPane().removeAll();
        add(topMenu, BorderLayout.NORTH); // Προσθήκη του πάνω μενού

        // Δημιουργία νέου panel με gradient φόντο
        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(144, 238, 144), 0, getHeight(), Color.WHITE);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        gradientPanel.setLayout(new BorderLayout());
        gradientPanel.add(newPanel, BorderLayout.CENTER); // Προσθήκη του νέου panel στο κέντρο του gradientPanel

        add(gradientPanel, BorderLayout.CENTER);

        // Επαναφορά και ανανέωση του UI
        revalidate();
        repaint();
    }
}
