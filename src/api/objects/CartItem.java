package api.objects;

import java.io.Serializable;

/**
 * Η κλάση CartItem αντιπροσωπεύει ένα αντικείμενο στο καλάθι αγορών.
 * Κάθε CartItem περιέχει ένα προϊόν και την ποσότητά του.
 */
public class CartItem implements Serializable {

    private static final long serialVersionUID = 5L;
    // Το προϊόν που περιέχεται στο καλάθι
    private Product product;

    // Η ποσότητα του προϊόντος
    private double quantity;

    /**
     * Κατασκευαστής της κλάσης CartItem.
     *
     * @param product Το προϊόν που προστίθεται στο καλάθι.
     * @param quantity Η ποσότητα του προϊόντος.
     */
    public CartItem(Product product, double quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * Copy constructor.
     * Δημιουργεί ένα νέο αντικείμενο CartItem βασισμένο σε ένα υπάρχον CartItem.
     *
     * @param other Το CartItem που θα αντιγραφεί.
     */
    public CartItem(CartItem other) {
        if (other == null) {
            throw new IllegalArgumentException("Δεν μπορεί να αντιγραφεί null αντικείμενο CartItem.");
        }
        // Δημιουργούμε νέο αντίγραφο του Product (deep copy) και αντιγράφουμε την ποσότητα
        this.product = new Product(
                other.product.getTitle(),
                other.product.getDescription(),
                other.product.getCategory(),
                other.product.getSubcategory(),
                other.product.getPrice(),
                other.product.getQuantityAvailable(),
                other.product.getUnit()
        );
        this.quantity = other.quantity;
    }

    /**
     * Επιστρέφει το προϊόν που περιέχεται στο CartItem.
     *
     * @return Το προϊόν.
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Επιστρέφει την ποσότητα του προϊόντος στο CartItem.
     *
     * @return Η ποσότητα του προϊόντος.
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Ενημερώνει την ποσότητα του προϊόντος στο CartItem.
     *
     * @param quantity Η νέα ποσότητα του προϊόντος.
     */
    public void setQuantity(double quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Η ποσότητα δεν μπορεί να είναι αρνητική.");
        }
        this.quantity = quantity;
    }

    /**
     * Υπολογίζει το συνολικό κόστος για το CartItem,
     * πολλαπλασιάζοντας την τιμή του προϊόντος με την ποσότητα.
     *
     * @return Το συνολικό κόστος.
     */
    public double getTotalCost() {
        return product.getPrice() * quantity;
    }
}