package gui.Customer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import api.Database;
import api.objects.CartItem;
import api.users.Customer;

/**
 * @author Αχιλλέας
 * The Cart GUI class represents a graphical user interface (GUI) dialog
 * for displaying and managing the shopping cart of a customer.
 */
public class Cart extends JDialog {
    private JPanel cartItemsLabel;
    private JPanel bottomLabel;
    private Customer customer;
    private JFrame owner;
    private JLabel price;
    private Database database;
    private JButton completeOrder;
    private ArrayList<CartItem> items;

    /**
     * Constructor that creates a new Cart dialog.
     * @param customer the customer whose cart is being displayed
     * @param owner the parent frame of this dialog
     * @param database the database for accessing inventory and other data
     */
    public Cart(Customer customer, JFrame owner,Database database) {
        super(owner,"Cart", true);
        this.customer = customer;
        this.owner = owner;
        this.database = database;
        setSize(600,400);
        setLocationRelativeTo(owner);
        makeDialog();
    }

    /**
     * Initializes the dialog layout and components.
     */
    void makeDialog() {
        ImageIcon imageIcon = new ImageIcon("images/Cart.png");
        setIconImage(imageIcon.getImage());
        setLayout(new BorderLayout(0,10));

        JPanel upLabel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton back = new JButton("Back");
        back.setBackground(Color.GRAY);
        back.setFocusable(false);
        upLabel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        add(upLabel, BorderLayout.NORTH);
        makeBottomLabel();
        makeCartItemsLabel();
    }

    /**
     * Updates the list of items in the cart from the customer's cart.
     */
    public void updateItemsInCart() {
        items = (ArrayList<CartItem>) customer.getCart().getCart();
    }


    /**
     * Creates and populates the panel displaying cart items.
     */
    private void makeCartItemsLabel() {
        cartItemsLabel = new JPanel(new GridLayout(0, 2, 10, 10));
        cartItemsLabel.setBackground(new Color(70, 130, 180));
        JScrollPane scrollPane = new JScrollPane(cartItemsLabel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        if (customer.getCart().isEmpty()) {
            JLabel label = new JLabel("Το καλάθι είναι άδειο");
            cartItemsLabel.add(label);
        }
        else {
            updateItemsInCart();
            for (CartItem i : items) {
                Product p = new Product(i.getProduct(), customer, owner, true);
                cartItemsLabel.add(p);
                JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT));
                item.setBackground(new Color(70, 130, 180));
                JButton remove = new JButton("Remove Product");
                remove.setFocusable(false);
                remove.setBackground(new Color(0, 128, 0));
                if (i.getProduct().getUnit() == api.objects.Product.MeasurementUnit.KILOS) {
                    JSpinner kiloSpinner = getSpinner(i);
                    item.add(kiloSpinner);
                    item.add(new JLabel("kilos"));
                } else {
                    JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel((int) i.getQuantity(), 0, i.getProduct().getQuantityAvailable(), 1));
                    JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) quantitySpinner.getEditor();
                    JFormattedTextField textField = editor.getTextField();
                    textField.setEditable(false);
                    textField.setFocusable(false);
                    item.add(quantitySpinner);
                    item.add(new JLabel("Pieces"));
                    quantitySpinner.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            customer.updateCart(i.getProduct(), (double) quantitySpinner.getValue());
                            updatePrice();
                        }
                    });
                }
                item.add(remove);
                cartItemsLabel.add(item);
                remove.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cartItemsLabel.remove(item);
                        cartItemsLabel.remove(p);
                        customer.removeFromCart(i.getProduct());
                        if (customer.getCart().isEmpty())
                            bottomLabel.remove(completeOrder);
                        updatePrice();
                        cartItemsLabel.revalidate();
                        cartItemsLabel.repaint();
                    }
                });
            }
        }
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Creates a spinner for adjusting quantities in kilos.
     * @param i the cart item associated with the spinner
     * @return a JSpinner for adjusting the quantity in kilos
     */
    private JSpinner getSpinner(CartItem i) {
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(i.getQuantity(), 0.0, i.getProduct().getQuantityAvailable(), 0.1); // Min: 0.1, Max: 10.0, Step: 0.1
        JSpinner kiloSpinner = new JSpinner(spinnerModel);
        JSpinner.NumberEditor spinnerEditor = new JSpinner.NumberEditor(kiloSpinner, "0.0");
        kiloSpinner.setEditor(spinnerEditor);
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) kiloSpinner.getEditor();
        JFormattedTextField textField = editor.getTextField();
        textField.setEditable(false);
        textField.setFocusable(false);

        // Add listener to update the cart when the value changes
        kiloSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double quantity = (double) kiloSpinner.getValue();
                customer.updateCart(i.getProduct(), quantity);
                updatePrice();
            }
        });
        return kiloSpinner;
    }

    /**
     * Updates the total price label to reflect the current cart total.
     */
    private void updatePrice() {
        price.setText("Total: " + String.format("%.2f",customer.getCart().getTotalCost()) + " €");
    }

    /**
     * Creates and populates the bottom panel for displaying the total price and order completion button.
     */
    private void makeBottomLabel() {
        bottomLabel = new JPanel(new BorderLayout());
        price = new JLabel("Total: " + String.format("%.2f",customer.getCart().getTotalCost()) + " €");
        price.setFont(new Font("Arial", Font.BOLD, 14));
        if(!customer.getCart().isEmpty()) {
            completeOrder = new JButton("Complete Order");
            completeOrder.setBackground(new Color(0, 128, 0));
            completeOrder.setFocusable(false);
            completeOrder.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        customer.completeOrder(database.getInventory());
                        cartItemsLabel.removeAll();
                        bottomLabel.remove(completeOrder);
                        cartItemsLabel.revalidate();
                        cartItemsLabel.repaint();
                        bottomLabel.revalidate();
                        bottomLabel.repaint();
                        JOptionPane.showMessageDialog(Cart.this, "Η παραγγελία πραγματοποιήθηκε με επιτυχία", "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } catch (Exception ex) {
                        ImageIcon warningIcon = new ImageIcon("images/warning.png");
                        Image image = warningIcon.getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH);
                        ImageIcon scaled = new ImageIcon(image);
                        JOptionPane.showMessageDialog(Cart.this, ex.getMessage(), "Out of stock product/s", JOptionPane.WARNING_MESSAGE, scaled);
                    }
                }
            });
            bottomLabel.add(completeOrder, BorderLayout.LINE_END);
        }
        bottomLabel.add(price, BorderLayout.LINE_START);
        add(bottomLabel, BorderLayout.SOUTH);
    }
}
