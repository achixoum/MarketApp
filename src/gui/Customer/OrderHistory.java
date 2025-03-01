package gui.Customer;

import api.objects.CartItem;
import api.objects.Order;
import api.users.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;

/**
 * @author Αχιλλέας
 * The {@code OrderHistory} class represents a graphical user interface (GUI) dialog
 * that displays the order history of a customer. It shows a table of past orders and allows users to view details of a selected order.
 */
public class OrderHistory extends JDialog {
    private Customer customer;
    private JFrame owner;

    /**
     * Constructor of the OrderHistory dialog that creates the dialog
     * @param customer the customer whose order history is displayed
     * @param owner the parent JFrame that owns this dialog
     */
    public OrderHistory(Customer customer, JFrame owner) {
        super(owner, true);
        this.customer = customer;
        this.owner = owner;
        setTitle("Ιστορικό Παραγγελιών");
        ImageIcon icon = new ImageIcon("images/SupermarketIcon.png");
        setIconImage(icon.getImage());
        setSize(800, 400);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //panel to hold the table
        JPanel mainPanel = new JPanel(new BorderLayout());

        //Table model
        String[] columnNames = {"Παραγγελίες", "Ημερομηνία Παραγγελίας"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (int i=0;i<customer.getOrderHistory().size();i++) {
            Order order = customer.getOrderHistory().get(i);
            tableModel.addRow(new Object[]{i+1,order.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            });
        }

        JTable table = new JTable(tableModel);
        table.setBackground(new Color(70, 130, 180));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(table);

        //add table to the panel
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        //Panel to display selected order's details
        JPanel orderDetails = new JPanel(new GridLayout(0, 2, 10, 10));
        orderDetails.setBackground(new Color(70, 130, 180));
        JScrollPane detailsScrollPane = new JScrollPane(orderDetails);
        detailsScrollPane.setPreferredSize(new Dimension(800, 300));

        mainPanel.add(detailsScrollPane, BorderLayout.SOUTH);

        //Add click listener to the table
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                orderDetails.removeAll();
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    Order selectedOrder = customer.viewOrderHistory().get(selectedRow);
                    for (CartItem item : selectedOrder.getItemsOfOrder()) {
                        orderDetails.add(new Product(item.getProduct(), customer, (JFrame) getOwner(), false));
                        if (item.getProduct().getUnit() == api.objects.Product.MeasurementUnit.PIECES)
                            orderDetails.add(new JLabel((int)item.getQuantity() + " " + item.getProduct().getUnit()));
                        else
                            orderDetails.add(new JLabel(String.format("%.2f", item.getQuantity()) + " " + item.getProduct().getUnit()));
                    }
                }
                orderDetails.revalidate();
                orderDetails.repaint();
            }
        });

        add(mainPanel);
        setVisible(true);
    }
}

