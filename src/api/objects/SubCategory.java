package api.objects;

import api.StringUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Η κλάση SubCategory αντιπροσωπεύει μια υποκατηγορία προϊόντων.
 * Περιλαμβάνει λειτουργικότητα για τη διαχείριση προϊόντων, όπως προσθήκη, αφαίρεση και εμφάνιση.
 */
public class SubCategory implements Serializable {

    private static final long serialVersionUID = 3675551128204666738L;

    private String title; // Τίτλος της υποκατηγορίας
    private HashMap<String, Product> products; // HashMap με τον τίτλο του προϊόντος ως κλειδί και το προϊόν ως τιμή
    //private HashMap<String, Product> normalizeProducts; // HashMap with the normalized title of the product as key and the same product as value
    /**
     * Κατασκευαστής που αρχικοποιεί την υποκατηγορία με τίτλο.
     *
     * @param title Ο τίτλος της υποκατηγορίας.
     */
    public SubCategory(String title) {
        this.title = title;
        this.products = new HashMap<>();
        //this.normalizeProducts = new HashMap<>();
    }

    /**
     * Copy constructor.
     * Δημιουργεί ένα νέο αντικείμενο SubCategory με βάση ένα υπάρχον SubCategory.
     *
     * @param other Η υποκατηγορία που θα αντιγραφεί.
     */
    public SubCategory(SubCategory other) {
        if (other == null) {
            throw new IllegalArgumentException("Δεν μπορεί να αντιγραφεί null υποκατηγορία.");
        }
        this.title = other.title; // Αντιγραφή του τίτλου

        // Δημιουργία νέου HashMap με deep copy για τα προϊόντα
        this.products = new HashMap<>();
        //this.normalizeProducts = new HashMap<>();
        for (Map.Entry<String, Product> entry : other.products.entrySet()) {
            Product originalProduct = entry.getValue();
            this.products.put(entry.getKey(), new Product(
                    originalProduct.getTitle(),
                    originalProduct.getDescription(),
                    originalProduct.getCategory(),
                    originalProduct.getSubcategory(),
                    originalProduct.getPrice(),
                    originalProduct.getQuantityAvailable(),
                    originalProduct.getUnit()
            ));
        }
    }

    /**
     * Επιστρέφει τον τίτλο της υποκατηγορίας.
     *
     * @return Ο τίτλος της υποκατηγορίας.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Επιστρέφει τα προϊόντα της υποκατηγορίας.
     *
     * @return Map με τα προϊόντα (κλειδί: τίτλος προϊόντος, τιμή: αντικείμενο προϊόντος).
     */
    public Map<String, Product> getProducts() {
        return new HashMap<>(products);
    }

    /**
     * Προσθέτει ένα προϊόν στην υποκατηγορία.
     *
     * @param product Το προϊόν που θα προστεθεί.
     * @throws IllegalArgumentException Αν το προϊόν είναι null ή υπάρχει ήδη στην υποκατηγορία.
     */
    public void addProduct(Product product) {
        if (product != null) {
            if (products.containsKey(StringUtil.normalize(product.getTitle()))) {
                throw new IllegalArgumentException("Το προϊόν που θέλετε να προσθέσετε υπάρχει ήδη.");
            } else {
                products.put(StringUtil.normalize(product.getTitle()), product);// Χρησιμοποιούμε τον τίτλο του προϊόντος ως κλειδί
                //adds the product in the normalized products map with the normalized title as key
                //normalizeProducts.put(StringUtil.normalize(product.getTitle()), product);
            }
        } else {
            throw new IllegalArgumentException("Το προϊόν που θέλετε να προσθέσετε δεν είναι κατάλληλο.");
        }
    }

    /**
     * Αφαιρεί ένα προϊόν από την υποκατηγορία.
     *
     * @param productTitle Ο τίτλος του προϊόντος που θα αφαιρεθεί.
     * @throws IllegalArgumentException Αν το προϊόν δεν υπάρχει στην υποκατηγορία.
     */
    public void removeProduct(String productTitle) {
        if (products.containsKey(StringUtil.normalize(productTitle))) {
            //products.remove(productTitle);// Αφαίρεση προϊόντος με βάση τον τίτλο
            //remove the product from the normalized map too
            products.remove(StringUtil.normalize(productTitle));
        } else {
            throw new IllegalArgumentException("Το προϊόν που θέλετε να αφαιρέσετε δεν υπάρχει.");
        }
    }

    /**
     * Επιστρέφει ένα προϊόν με βάση τον τίτλο του.
     *
     * @param title Ο τίτλος του προϊόντος.
     * @return Αντικείμενο Product ή null αν δεν βρεθεί.
     */
    public Product getProduct(String title) {
        String newS = StringUtil.normalize(title);
        if (products.containsKey(newS)) {
            Product product = products.get(newS);
            return new Product(product.getTitle(), product.getDescription(), product.getCategory(), product.getSubcategory(), product.getPrice(), product.getQuantityAvailable(), product.getUnit());
        }
        return null;
    }

    /**
     * @param title the title of the product we want
     * @return the value of the key title, null if the product doesn't exist
     * @author Αχιλλέας
     * Extra method where we want the reference of the product for modifying
     */
    public Product getProductReference(String title) {
        if (title == null)
            throw new IllegalArgumentException("Cannot access null string object");
        return products.get(StringUtil.normalize(title));
    }

    /**
     * Εμφανίζει όλα τα προϊόντα της υποκατηγορίας.
     * Αν δεν υπάρχουν προϊόντα, εμφανίζει κατάλληλο μήνυμα.
     */
    public void displayProducts() {
        if (products.isEmpty()) {
            System.out.println("Η υποκατηγορία δεν περιέχει προϊόντα.");
        } else {
            System.out.println("Προϊόντα στην υποκατηγορία: " + title);
            for (Map.Entry<String, Product> entry : products.entrySet()) {
                entry.getValue().displayDetails(true); // Εμφάνιση προϊόντων με λεπτομέρειες για διαχειριστή
                System.out.println();
            }
        }
    }
    /*
    /**
     * Επιστρέφει τον τίτλο ενός προϊόντος αν υπάρχει στην υποκατηγορία.
     *
     * @param title Ο τίτλος του προϊόντος που αναζητείται.
     * @return Ο τίτλος του προϊόντος ή null αν δεν υπάρχει.
     */
    public String getProductTitle(String title) {
        if (products.containsKey(title)) {
            return products.get(title).getTitle();
        } else {
            return null;
        }
    }

    /**
     * Επιστρέφει όλα τα προϊόντα της υποκατηγορίας.
     * Αν ο χρήστης είναι διαχειριστής, επιστρέφει όλα τα προϊόντα.
     * Αν ο χρήστης δεν είναι διαχειριστής, επιστρέφει μόνο τα προϊόντα που είναι ορατά για τους κανονικούς χρήστες.
     *
     * @param isAdmin Αν ο χρήστης είναι διαχειριστής ή όχι. Αν είναι διαχειριστής, επιστρέφει όλα τα προϊόντα.
     * @return Χάρτης με τα προϊόντα (κλειδί: τίτλος προϊόντος, τιμή: αντικείμενο προϊόντος).
     */
    public Map<String, Product> getAllProducts(boolean isAdmin) {
        // Αν ο χρήστης είναι διαχειριστής, επιστρέφει όλα τα προϊόντα
        if (isAdmin) {
            // Επιστρέφει ολόκληρο τον χάρτη προϊόντων για τους διαχειριστές
            // Δημιουργούμε νέο HashMap για να αποφύγουμε τη άμεση τροποποίηση του αρχικού χάρτη
            return new HashMap<>(products); // Επιστρέφει έναν αντίγραφο του χάρτη προϊόντων
        } else {
            // Αν ο χρήστης δεν είναι διαχειριστής, φιλτράρει τα προϊόντα για να περιλαμβάνει μόνο εκείνα που είναι ορατά
            Map<String, Product> filteredProducts = new HashMap<>(); // Δημιουργεί νέο χάρτη για τα φιλτραρισμένα προϊόντα

            // Διατρέχει όλα τα προϊόντα στον αρχικό χάρτη
            for (Map.Entry<String, Product> entry : products.entrySet()) {
                Product product = entry.getValue(); // Λαμβάνει το αντικείμενο προϊόντος

            }

            // Επιστρέφει τον φιλτραρισμένο χάρτη προϊόντων
            return filteredProducts;
        }
    }
}