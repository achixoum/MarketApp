package gui.Login;

import api.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SingUp extends JDialog {
    private JTextField firstName;
    private JLabel firstNameLabel;
    private JTextField lastName;
    private JLabel lastNameLabel;
    private JTextField username;
    private JLabel usernameLabel;
    private JPasswordField password;
    private JLabel passwordLabel;
    private JPasswordField confirmPassword;
    private JLabel confirmPasswordLabel;
    private JButton buttonConfirm;
    private JButton buttonCancel;
    private Database database;

    public SingUp(Logging master, boolean modality, Database database) {
        super(master, "Sign Up", modality);
        this.database = database;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 300);
        setLocation((master.getX() + 400)/2, (master.getY() + 300)/2);
        GridLayout gridLayout = new GridLayout(6, 2, 10, 30);
        setLayout(gridLayout);

        SetUp();

        add(firstNameLabel);
        add(firstName);
        add(lastNameLabel);
        add(lastName);
        add(usernameLabel);
        add(username);
        add(passwordLabel);
        add(password);
        add(confirmPasswordLabel);
        add(confirmPassword);
        add(buttonConfirm);
        add(buttonCancel);

        setVisible(true);
    }

    void SetUp() {
        firstName = new JTextField();
        firstNameLabel = new JLabel("Enter your first name:");

        lastName = new JTextField();
        lastNameLabel = new JLabel("Enter your surname:");

        username = new JTextField();
        usernameLabel = new JLabel("Create your username:");

        password = new JPasswordField();
        passwordLabel = new JLabel("Create your password:");

        confirmPassword = new JPasswordField();
        confirmPasswordLabel = new JLabel("Confirm your password:");

        ConfirmButton();
        CancelButton();
    }

    void ConfirmButton() {
        buttonConfirm = new JButton("Create account");
        buttonConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = firstName.getText().trim();
                String surname = lastName.getText().trim();
                String Username = username.getText().trim();
                String Password = new String(password.getPassword());
                String ConfirmPassword = new String(confirmPassword.getPassword());
                if(name.isEmpty() || surname.isEmpty() || Username.isEmpty() || Password.isEmpty() || ConfirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields are necessary. Please fill the missing fields");
                }
                else if (!Password.equals(ConfirmPassword)) {
                    JOptionPane.showMessageDialog(null, "Password doesn't match. Please try again");
                }
                else {
                    api.Logging signup = new api.Logging();
                    try {
                        if(signup.signUp(Username, Password, name, surname, database, "Files/UsersData.dat")) {
                            JOptionPane.showMessageDialog(SingUp.this, "Ο λογαριασμός δημιουργήθηκε με επιτυχία", "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Username already exists.");
                            username.setText("");
                        }
                    } catch (IllegalArgumentException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    void CancelButton() {
        buttonCancel = new JButton("Cancel");
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
