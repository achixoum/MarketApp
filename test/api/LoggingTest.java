package api;

import api.users.Customer;
import api.users.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoggingTest {

    private Logging logging;
    private Database mockDatabase;

    @Before
    public void setUp() {
        // Create a mock Database instance before each test
        mockDatabase = new Database();
        logging = new Logging();
    }

    @Test
    public void testSignUpSuccess() {
        // Try to sign up a new user
        String username = "testuser";
        String password = "password123";
        String firstName = "John";
        String lastName = "Doe";

        // Call the sign-up method
        boolean result = logging.signUp(username, password, firstName, lastName, mockDatabase, "Files/UsersDataTest.dat");

        // Check if the sign-up was successful
        assertTrue("Sign up should be successful", result);

        // Check if the user is added to the database
        User user = mockDatabase.getUser(username);
        assertNotNull("User should be added to the database", user);
        assertEquals("Username should match", username, user.getUserName());
        assertTrue("User should be an instance of Customer", user instanceof Customer);
    }

    @Test
    public void testSignUpUsernameTaken() {
        // Add an existing user manually to the database for testing
        String existingUsername = "existingUser";
        String existingPassword = "password123";
        Customer existingCustomer = new Customer(existingUsername, existingPassword);
        mockDatabase.users.put(existingUsername, existingCustomer);

        // Try to sign up a user with the same username
        String username = "existingUser";  // Same as existing username
        String password = "newpassword";
        String firstName = "Jane";
        String lastName = "Doe";

        // Call the sign-up method
        boolean result = logging.signUp(username, password, firstName, lastName, mockDatabase, "Files/UsersDataTest.dat");

        // Check if the sign-up failed due to the username being taken
        assertFalse("Sign up should fail due to username being taken", result);
    }

    @Test
    public void testSignInSuccess() {
        // Sign up a new user first
        String username = "testuser";
        String password = "password123";
        String firstName = "John";
        String lastName = "Doe";
        logging.signUp(username, password, firstName, lastName, mockDatabase, "Files/UsersDataTest.dat");

        // Now, try to sign in with the correct username and password
        boolean result = logging.signIn(username, password, mockDatabase, "Files/UsersDataTest.dat");

        // Check if the sign-in was successful
        assertTrue("Sign-in should be successful", result);
    }

    @Test
    public void testSignInFailureIncorrectPassword() {
        // Sign up a new user first
        String username = "testuser";
        String password = "password123";
        String firstName = "John";
        String lastName = "Doe";
        logging.signUp(username, password, firstName, lastName, mockDatabase, "Files/UsersDataTest.dat");

        // Now, try to sign in with the correct username but an incorrect password
        boolean result = logging.signIn(username, "wrongpassword", mockDatabase, "");

        // Check if the sign-in failed due to incorrect password
        assertFalse("Sign-in should fail due to incorrect password", result);
    }

    @Test
    public void testSignInFailureNonExistentUser() {
        // Try to sign in with a non-existent user
        boolean result = logging.signIn("nonexistentuser", "password123", mockDatabase, "Files/UsersDataTest.dat");

        // Check if the sign-in failed due to non-existent user
        assertFalse("Sign-in should fail due to non-existent user", result);
    }

}
