package api.objects;
import java.io.Serializable;
import java.util.Objects;
/**
 * Η κλάση Product αντιπροσωπεύει ένα προϊόν με τα βασικά χαρακτηριστικά του.
 * Παρέχει δυνατότητες για έλεγχο εγκυρότητας δεδομένων, διαθεσιμότητας και εμφάνιση πληροφοριών.
 */
public class Product implements Serializable {

    private static final long serialVersionUID = 5724366834403358866L;
    // Καθορίζει τις μονάδες μέτρησης του προϊόντος (π.χ. κιλά, τεμάχια)
    public enum MeasurementUnit { KILOS, PIECES }

    // Χαρακτηριστικά προϊόντος
    private String title;              // Τίτλος προϊόντος
    private String description;        // Περιγραφή προϊόντος
    private String category;           // Κατηγορία προϊόντος
    private String subcategory;        // Υποκατηγορία προϊόντος
    private double price;              // Τιμή του προϊόντος
    private double quantityAvailable;  // Διαθέσιμη ποσότητα
    private MeasurementUnit unit;      // Μονάδα μέτρησης
    private int ordersmade;            // Αριθμός φορών που έχει αγοραστεί

    /**
     * Προεπιλεγμένος κατασκευαστής.
     * Αρχικοποιεί τα χαρακτηριστικά με προκαθορισμένες τιμές.
     */
    public Product() {
        title = null;
        description = null;
        category = null;
        subcategory = null;
        price = 0;
        quantityAvailable = 0;
        unit = MeasurementUnit.KILOS;
        ordersmade=0;
    }

    /**
     * Κατασκευαστής που δέχεται αρχικές τιμές για όλα τα χαρακτηριστικά.
     *
     * @param title Τίτλος του προϊόντος.
     * @param description Περιγραφή του προϊόντος.
     * @param category Κατηγορία του προϊόντος.
     * @param subcategory Υποκατηγορία του προϊόντος.
     * @param price Τιμή του προϊόντος.
     * @param quantityAvailable Διαθέσιμη ποσότητα του προϊόντος.
     * @param unit Μονάδα μέτρησης του προϊόντος.
     */
    public Product(String title, String description, String category, String subcategory, double price, double quantityAvailable, MeasurementUnit unit) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.subcategory = subcategory;
        setPrice(price); // χρήση setter για έλεγχο εγκυρότητας
        setQuantityAvailable(quantityAvailable); // χρήση setter για έλεγχο εγκυρότητας
        setUnit(unit); // χρήση setter για έλεγχο εγκυρότητας
        ordersmade=0;
    }

    /**
     * Κατασκευαστής αντιγραφής.
     * Δημιουργεί ένα νέο προϊόν, αντιγράφοντας τα χαρακτηριστικά από ένα άλλο προϊόν.
     *
     * @param other Το προϊόν που θα αντιγραφεί.
     * @throws IllegalArgumentException Αν το αντικείμενο που παρέχεται είναι null.
     */
    public Product(Product other) {
        if (other == null) {
            throw new IllegalArgumentException("Το αντικείμενο προς αντιγραφή δεν μπορεί να είναι null.");
        }
        this.title = other.title;
        this.description = other.description;
        this.category = other.category;
        this.subcategory = other.subcategory;
        this.price = other.price;
        this.quantityAvailable = other.quantityAvailable;
        this.unit = other.unit;
        this.ordersmade=other.ordersmade;
    }

    // Getters: Επιστρέφουν τις τιμές των χαρακτηριστικών
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getSubcategory() { return subcategory; }
    public double getPrice() { return price; }
    public double getQuantityAvailable() { return quantityAvailable; }
    public MeasurementUnit getUnit() { return unit; }
    public int getOrdersmade() { return ordersmade; }

    // Setters: Ενημερώνουν τις τιμές των χαρακτηριστικών με έλεγχο εγκυρότητας
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setSubcategory(String subcategory) { this.subcategory = subcategory; }
    public void setOrdersmade(int ordersmade) {
        if (ordersmade < 0) {
            throw new IllegalArgumentException("Η τιμή δεν μπορεί να είναι αρνητική.");
        }
        this.ordersmade = ordersmade;
    }

    /**
     * Ορίζει την τιμή του προϊόντος.
     *
     * @param price Τιμή του προϊόντος.
     * @throws IllegalArgumentException Αν η τιμή είναι αρνητική.
     */
    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Η τιμή δεν μπορεί να είναι αρνητική.");
        }
        this.price = price;
    }

    /**
     * Ορίζει τη διαθέσιμη ποσότητα του προϊόντος.
     *
     * @param quantityAvailable Η διαθέσιμη ποσότητα.
     * @throws IllegalArgumentException Αν η ποσότητα είναι αρνητική.
     */
    public void setQuantityAvailable(double quantityAvailable) {
        if (quantityAvailable < 0) {
            throw new IllegalArgumentException("Η ποσότητα δεν μπορεί να είναι αρνητική.");
        }
        this.quantityAvailable = quantityAvailable;
    }

    /**
     * Ορίζει τη μονάδα μέτρησης του προϊόντος.
     *
     * @param unit Μονάδα μέτρησης.
     * @throws IllegalArgumentException Αν η μονάδα μέτρησης είναι null.
     */
    public void setUnit(MeasurementUnit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Η μονάδα μέτρησης είναι υποχρεωτική.");
        }
        this.unit = unit;
    }

    /**
     * Ελέγχει αν το προϊόν είναι έγκυρο.
     *
     * @return true αν το προϊόν είναι έγκυρο, αλλιώς false.
     */
    public boolean validateProduct() {
        if (title == null || title.isEmpty()) {
            System.out.println("Σφάλμα: Ο τίτλος είναι υποχρεωτικός.");
            return false;
        }
        if (description == null || description.isEmpty()) {
            System.out.println("Σφάλμα: Η περιγραφή είναι υποχρεωτική.");
            return false;
        }
        if (category == null || category.isEmpty()) {
            System.out.println("Σφάλμα: Η κατηγορία είναι υποχρεωτική.");
            return false;
        }
        if (subcategory == null || subcategory.isEmpty()) {
            System.out.println("Σφάλμα: Η υποκατηγορία είναι υποχρεωτική.");
            return false;
        }
        if (price <= 0) {
            System.out.println("Σφάλμα: Η τιμή δεν μπορεί να είναι αρνητική ή μηδενική.");
            return false;
        }
        if (quantityAvailable <= 0) {
            System.out.println("Σφάλμα: Η ποσότητα δεν μπορεί να είναι αρνητική ή μηδενική.");
            return false;
        }
        return true;
    }

    /**
     * Επιστρέφει την περιγραφή της ποσότητας με βάση τη μονάδα μέτρησης.
     *
     * @return "τεμάχια" αν η μονάδα είναι PIECES, "κιλά" αν είναι KILOS.
     */
    public String getQuantityLabel() {
        return (unit == MeasurementUnit.PIECES) ? "τεμάχια" : "κιλά";
    }

    /**
     * Ελέγχει αν η ζητούμενη ποσότητα είναι διαθέσιμη.
     *
     * @param requestedQuantity Η ζητούμενη ποσότητα.
     * @return true αν η ποσότητα είναι διαθέσιμη, αλλιώς false.
     */
    public boolean isQuantityAvailable(double requestedQuantity) {
        return quantityAvailable >= requestedQuantity;
    }

    /**
     * Εμφανίζει τις λεπτομέρειες του προϊόντος.
     *
     * @param isAdmin Αν είναι true, εμφανίζονται επιπλέον πληροφορίες για τον διαχειριστή.
     */
    public void displayDetails(boolean isAdmin) {
        System.out.println("Προϊόν: " + title);
        System.out.println("Περιγραφή: " + description);
        System.out.println("Κατηγορία: " + category);
        System.out.println("Υποκατηγορία: " + subcategory);
        System.out.println("Τιμή: " + price + " €");
        System.out.println("Διαθέσιμη Ποσότητα: " + quantityAvailable + (unit == MeasurementUnit.PIECES ? " τεμάχια" : " κιλά"));
        if (isAdmin) {
            System.out.println("Διαχειριστής: Μπορεί να επεξεργαστεί το προϊόν.");
        } else {
            System.out.println("Πελάτης: Μπορεί να προσθέσει το προϊόν στο καλάθι.");
        }
    }

    @Override
    public boolean equals(Object obj) {
        // Αν το τρέχον αντικείμενο (this) είναι το ίδιο με το obj, επιστρέφουμε true.
        // Δηλαδή, αν δείχνουν στον ίδιο χώρο μνήμης, τότε είναι ίδια αντικείμενα.
        if (this == obj) {
            return true;
        }

        // Αν το obj είναι null ή τα αντικείμενα δεν είναι του ίδιου τύπου (η κλάση δεν είναι η ίδια),
        // τότε επιστρέφουμε false, γιατί δεν μπορούμε να συγκρίνουμε αντικείμενα διαφορετικών τύπων.
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        // Μετατρέπουμε το obj σε Product, αφού έχουμε βεβαιωθεί ότι είναι αντικείμενο της κλάσης Product.
        Product product = (Product) obj;

        // Συγκρίνουμε τις τιμές των πεδίων των δύο αντικειμένων.

        // Συγκρίνουμε την τιμή του προϊόντος (price). Χρησιμοποιούμε την Double.compare για να αποφύγουμε προβλήματα ακρίβειας
        // με αριθμούς κινητής υποδιαστολής.
        return Double.compare(product.price, price) == 0 &&
                Double.compare(product.quantityAvailable, quantityAvailable) == 0 &&
                Objects.equals(title, product.title) &&
                Objects.equals(description, product.description) &&
                Objects.equals(category, product.category) &&
                Objects.equals(subcategory, product.subcategory) &&
                unit == product.unit;
    }

    public void increaseOrdersmade() {
        ordersmade++;
    }
}