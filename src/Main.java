import api.Database;

import java.io.IOException;

import gui.Login.Logging;

/**
 * Το πρόγραμμά σας πρέπει να έχει μόνο μία main, η οποία πρέπει να είναι η παρακάτω.
 * <p>
 * <p>
 * ************* ΜΗ ΣΒΗΣΕΤΕ ΑΥΤΗ ΤΗΝ ΚΛΑΣΗ ************
 */
public class Main {
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        Database db = new Database();
        db.ReadUsersObject("Files/UsersData.dat");
        db.ReadInventoryObject("Files/InventoryData.dat");
        Logging logging = new Logging(db);
    }
}

