package gui.Login;

import api.Database;
import api.users.Admin;
import api.users.Customer;
import api.users.User;
import gui.Admin.AdminMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Logging extends JFrame {
    private JTextField username;
    private JLabel usernameLabel;
    private JPasswordField password;
    private JLabel passwordLabel;
    private JButton login;
    private JButton singUp;
    private final int textFieldWidth = 300;
    private final int textFieldHeight = 30;
    private final int labelWidth = 200;
    private final int labelHeight = 30;
    private JLabel warningLabel;
    private ImageIcon warningIcon;
    private Database database;

    public Logging(Database db) {
        database = db;
        makeFrame();
    }

    private void makeFrame() {

        //setting the screen size and centralize the frame to screen
        setTitle("Supermarket");
        setSize(800, 600);
        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension d = t.getScreenSize();
        setLocation((int)(d.getWidth() - getWidth())/2, (int)(d.getHeight() - getHeight())/2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        ImageIcon imageIcon = new ImageIcon("images/SupermarketIcon.png");
        setIconImage(imageIcon.getImage());
        //setting up the utilities of the frame
        SetUpUtilities();
        getContentPane().setBackground(new Color(70, 130, 180));
        add(username);
        add(password);
        add(usernameLabel);
        add(passwordLabel);
        add(login);
        add(singUp);

        setVisible(true);
    }

    void SetUpUtilities() {
        //create the text field and the label for the username
        //place them in the center of the frame
        username = new JTextField();
        username.setBounds((getWidth()-textFieldWidth)/2,(getHeight()-textFieldHeight-100)/2, textFieldWidth, textFieldHeight);
        usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(username.getX()-70, username.getY(), labelWidth, labelHeight);

        JLabel welcomeLabel = new JLabel("Welcome to My Market");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 40));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBounds( usernameLabel.getX(), getHeight()/2 - username.getY(), 500, 200);
        add(welcomeLabel);
        //create the password field and the label for the password
        //place them above the username text field and label
        password = new JPasswordField();
        password.setBounds((getWidth()-textFieldWidth)/2,(getHeight()-textFieldHeight-100)/2 + textFieldHeight+10, textFieldWidth, textFieldHeight);
        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(password.getX()-70, password.getY(), labelWidth, labelHeight);

        //create the warning message label with its icon
        warningIcon = new ImageIcon("images/warning.png");
        Image scaledImage = warningIcon.getImage().getScaledInstance(10,10, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        warningLabel = new JLabel("",scaledIcon, JLabel.LEADING);
        warningLabel.setHorizontalAlignment(JLabel.LEFT);
        warningLabel.setVerticalAlignment(JLabel.TOP);
        warningLabel.setBounds(password.getX(), password.getY() + textFieldHeight,textFieldWidth+10, textFieldHeight/2);
        //create the buttons for sign in and sign up
        SignInButton();
        SingUpButton();
    }

    void SignInButton() {
        login = new JButton("Sign in");
        login.setBounds(password.getX(), password.getY()+ 50 , 100, 20);
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String string1 = username.getText();
                String string2 = new String(password.getPassword());
                warningLabel.setVisible(false);
                if (string1.isEmpty() && string2.isEmpty()) {
                    warningLabel.setVisible(true);
                    warningLabel.setText("Both username and password are required.");
                }
                else if (string2.isEmpty()) {
                    warningLabel.setVisible(true);
                    warningLabel.setText("Password is required. Please enter your password.");
                }
                else if (string1.isEmpty()) {
                    warningLabel.setVisible(true);
                    warningLabel.setText("Username is required. Please enter your username.");
                }
                else {
                    User user = database.getUser(string1);
                    if(user == null) {
                        JOptionPane.showMessageDialog(null, "Wrong username or password. Try again.");
                    }
                    else if(user.getPassword().equals(string2)) {
                        if (user instanceof Admin ) {
                            AdminMain adminFrame = new AdminMain(database,(Admin) user);
                            dispose();
                        }
                        else if (user instanceof Customer) {
                            Customer customer = (Customer) user;
                            gui.Customer.Customer customerFrame = new gui.Customer.Customer(customer, database);
                            dispose();
                        }
                    }
                    else
                        JOptionPane.showMessageDialog(null, "Wrong username or password. Try again.");
                }
                add(warningLabel);
                revalidate();
                repaint();
            }
        });
    }
    void SingUpButton() {
        singUp = new JButton("Sing up");
        singUp.setBounds(password.getX()+ 100, passwordLabel.getY()+50, 100, 20);
        Logging logging = this;
        singUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                warningLabel.setVisible(false);
                SingUp singup = new SingUp(logging, true, database);
            }
        });
    }
}
