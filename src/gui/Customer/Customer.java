package gui.Customer;

import api.Database;
import gui.Login.Logging;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * @author Αχιλλέας
 * Represents the graphical user interface (GUI) for a customer in the supermarket system. This frame provides options for accessing the customer's cart, viewing the product menu,
 * managing their profile, and searching for products.
 */
public class Customer extends JFrame {
    private api.users.Customer customer;
    private final Database database;
    private JButton cart;
    private JButton menu;
    private JButton profileButton;

    /**
     * Constructor that makes the customer frame
     * @param customer the customer that has logged in
     * @param database the database of the app give for modification
     */
    public Customer(api.users.Customer customer, Database database) {
        this.database = database;
        this.customer = customer;
        makeFrame();
    }

    /**
     * Sets up the main frame and its components
     */
    public void makeFrame() {
        setTitle("Supermarket");
        setSize(1280, 720);
        setResizable(true);
        //align the frame to the center of the screen
        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension d = t.getScreenSize();
        setLocation((int)(d.getWidth() - getWidth())/2, (int)(d.getHeight() - getHeight())/2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    database.WriteInventoryObject("Files/InventoryData.dat");
                    database.WriteUsersObject("Files/UsersData.dat");
                }
                catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        ImageIcon imageIcon = new ImageIcon("images/SupermarketIcon.png");
        setIconImage(imageIcon.getImage());
        //create the main layout that will be used in the frame

        JPanel toolbarPanel = new JPanel();
        toolbarPanel.setBackground(new Color(70, 130, 180));
        toolbarPanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,new Color(70, 130, 180)));
        GroupLayout layout = new GroupLayout(toolbarPanel);
        toolbarPanel.setLayout(layout);

        //menu icon with button
        ImageIcon menuIcon = new ImageIcon("images/Menu.png");
        Image img1 = menuIcon.getImage().getScaledInstance(30,40, Image.SCALE_SMOOTH);
        menuIcon = new ImageIcon(img1);
        menu = new JButton(menuIcon);
        setIcon(menu, "images/MenuHover.png", menuIcon, 30, 40);

        //cart icon with button
        ImageIcon cartIcon = new ImageIcon("images/Cart.png");
        Image img2 = cartIcon.getImage().getScaledInstance(30,40, Image.SCALE_SMOOTH);
        cartIcon = new ImageIcon(img2);
        cart = new JButton(cartIcon);
        setIcon(cart, "images/CartHover.png", cartIcon, 30, 40);


        ImageIcon profileIcon = new ImageIcon("images/Profile.png");
        Image img3 = profileIcon.getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
        profileIcon = new ImageIcon(img3);
        profileButton = new JButton(profileIcon);
        setIcon(profileButton, "images/ProfileHover.png", profileIcon, 30, 30);


        ProductSearch search = new ProductSearch(customer, database, this);

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(Color.BLACK);
        separator.setBackground(Color.BLACK);


        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(menu,0,30,30)
                        .addGap(0,0,Short.MAX_VALUE).addGap(10).addComponent(search,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)
                        .addGap(0,0,Short.MAX_VALUE).addComponent(cart, 0,30,40)
                        .addGap(10).addComponent(profileButton,0,30,30))
                .addGroup(layout.createSequentialGroup()
                    .addGap(10).addComponent(search.titlePanel(), GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(separator)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                        .addGap(10).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(menu,0,30,30)
                                .addComponent(search,0,30,40)
                                .addComponent(cart,0,30,40)
                                .addComponent(profileButton,0,30,30))
                .addGap(10)
                .addComponent(search.titlePanel(), GroupLayout.PREFERRED_SIZE,30, GroupLayout.PREFERRED_SIZE).addGap(10)
                .addGap(10).addComponent(separator)
        );

        cart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search.resetSearchResults();
                Cart cart = new Cart(customer, (JFrame) getOwner(), database);
                cart.setVisible(true);
            }
        });

        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu choices = new JPopupMenu();
                JMenuItem item1 = new JMenuItem("Όλα τα προϊόντα");
                choices.add(item1);
                item1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        search.searchAllProducts();
                    }
                });
                JMenuItem item2 = new JMenuItem("Ιστορικό παραγγελιών");
                choices.add(item2);
                item2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        OrderHistory orderHistory = new OrderHistory(customer, (JFrame) Customer.this);
                    }
                });
                choices.show(menu, 0, menu.getHeight());
            }
        });

        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu profileMenu = new JPopupMenu();

                // Add "Log Out" option
                JMenuItem logOutItem = new JMenuItem("Log Out");
                logOutItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                        Logging logging = new Logging(database);
                    }
                });

                // Add "View Profile" option
                JMenuItem viewProfileItem = new JMenuItem("View Profile");
                viewProfileItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Show a small tab (popup dialog) with user details
                        showProfileDialog();
                    }
                });
                profileMenu.add(logOutItem);
                profileMenu.add(viewProfileItem);
                profileMenu.show(profileButton, 0, profileButton.getHeight());
            }
        });

        JPanel container = new JPanel(new BorderLayout());
        container.add(toolbarPanel, BorderLayout.NORTH);
        container.add(search.getProductsScrollPane(), BorderLayout.CENTER);

        setContentPane(container);

        setVisible(true);
    }

    /**
     * Creates a button with hover effects for icon changes.
     * @param button the button where we are applying changes.
     * @param filename file path to the hover icon.
     * @param width icon width.
     * @param height icon height.
     */
    private void setIcon(JButton button, String filename, ImageIcon Icon1, int width, int height) {
        ImageIcon Icon2 = new ImageIcon(filename);
        Image img2 = Icon2.getImage().getScaledInstance(width,height, Image.SCALE_SMOOTH);
        Icon2 = new ImageIcon(img2);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusable(false);
        ImageIcon finalIcon2 = Icon2;
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setIcon(finalIcon2); // Change icon on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setIcon(Icon1); // Revert to default icon
            }
        });
    }

    /**
     * Displays a dialog with the customer's profile information.
     */
    private void showProfileDialog() {
        //create a small dialog showing the user's name and surname
        JDialog profileDialog = new JDialog(this, "User Profile", true);
        profileDialog.setSize(250, 150);
        profileDialog.setLayout(new GridLayout(3, 1));

        JLabel nameLabel = new JLabel("Name: " + customer.getFirstName());
        JLabel surnameLabel = new JLabel("Surname: " + customer.getLastName());
        JButton closeButton = new JButton("Close");
        closeButton.setFocusable(false);

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                profileDialog.dispose();
            }
        });

        profileDialog.add(nameLabel);
        profileDialog.add(surnameLabel);
        profileDialog.add(closeButton);

        profileDialog.setLocationRelativeTo(this);
        profileDialog.setVisible(true);
    }
}
