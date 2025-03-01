package api.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 Η κλάση Cart αντιπροσωπεύει ένα καλάθι αγορών.
 Υποστηρίζει λειτουργίες όπως προσθήκη, διαγραφή, ενημέρωση προϊόντων,
 εμφάνιση περιεχομένων και ολοκλήρωση παραγγελίας.*/

public class Cart implements Serializable {

    private static final long serialVersionUID = 6985686180798802462L;
    // Χάρτης που αποθηκεύει τα προϊόντα στο καλάθι. Κλειδί: Τίτλος προϊόντος, Τιμή: CartItem
    private HashMap<String, CartItem> items;

    /**
     * Κατασκευαστής της κλάσης Cart.
     * Αρχικοποιεί τις δομές δεδομένων για τα αντικείμενα και το ιστορικό παραγγελιών.
     */
    public Cart() {
        this.items = new HashMap<>();
    }

    /**
     * Copy constructor.
     * Δημιουργεί ένα νέο Cart αντικείμενο βασισμένο σε ένα υπάρχον.
     *
     * @param other Το Cart που θα αντιγραφεί.
     */
    public Cart(Cart other) {
        if (other == null) {
            throw new IllegalArgumentException("Δεν μπορεί να αντιγραφεί null αντικείμενο Cart.");
        }
        // Δημιουργούμε νέο HashMap και κάνουμε deep copy των στοιχείων του καλαθιού
        this.items = new HashMap<>();
        for (CartItem cartItem: other.getCart()) {
            this.items.put(cartItem.getProduct().getTitle(), new CartItem(cartItem));
        }
    }

    /**
     * Προσθέτει ένα προϊόν στο καλάθι.
     *
     * @param product Το προϊόν που θα προστεθεί.
     * @param quantity Η ποσότητα του προϊόντος.
     * @throws IllegalArgumentException Αν η ποσότητα είναι μη έγκυρη ή μη διαθέσιμη.
     */
    public void addProduct(Product product, double quantity) {
        if(product!=null) {
            if (quantity <= 0) {
                throw new IllegalArgumentException("Η ποσότητα πρέπει να είναι μεγαλύτερη από το 0.");
            }
            if (!product.isQuantityAvailable(quantity)) {
                throw new IllegalArgumentException("Η ζητούμενη ποσότητα δεν είναι διαθέσιμη.");
            }

            if (items.containsKey(product.getTitle())) {
                // Αν το προϊόν υπάρχει ήδη, ενημερώνεται η ποσότητά του
                CartItem existingItem = items.get(product.getTitle());
                double newQuantity = existingItem.getQuantity() + quantity;

                if (!product.isQuantityAvailable(newQuantity)) {
                    throw new IllegalArgumentException("Η νέα ποσότητα δεν είναι διαθέσιμη.");
                }

                existingItem.setQuantity(newQuantity);
            } else {
                // Αν το προϊόν δεν υπάρχει, προστίθεται στο καλάθι
                items.put(product.getTitle(), new CartItem(product, quantity));
            }
        }
        else
        {
            throw new IllegalArgumentException("Το προϊόν δέν είναι κατάληλο");
        }
    }

    /**
     * Διαγράφει ένα προϊόν από το καλάθι.
     *
     * @param productTitle Ο τίτλος του προϊόντος που θα διαγραφεί.
     * @throws IllegalArgumentException Αν το προϊόν δεν υπάρχει στο καλάθι.
     */
    public void removeProduct(String productTitle) {
        if (!items.containsKey(productTitle)) {
            throw new IllegalArgumentException("Το προϊόν δεν υπάρχει στο καλάθι.");
        }
        items.remove(productTitle);
    }

    /**
     * Ενημερώνει την ποσότητα ενός προϊόντος στο καλάθι.
     *
     * @param productTitle Ο τίτλος του προϊόντος που θα ενημερωθεί.
     * @param newQuantity Η νέα ποσότητα του προϊόντος.
     * @throws IllegalArgumentException Αν το προϊόν δεν υπάρχει ή η νέα ποσότητα δεν είναι έγκυρη.
     */
    public void updateQuantity(String productTitle, double newQuantity) {
        if (!items.containsKey(productTitle)) {
            throw new IllegalArgumentException("Το προϊόν δεν υπάρχει στο καλάθι.");
        }

        CartItem item = items.get(productTitle);
        if (newQuantity <= 0) {
            removeProduct(productTitle); // Αν η ποσότητα είναι 0 ή μικρότερη, διαγράφεται το προϊόν
        } else if (!item.getProduct().isQuantityAvailable(newQuantity)) {
            throw new IllegalArgumentException("Η νέα ποσότητα δεν είναι διαθέσιμη.");
        } else {
            item.setQuantity(newQuantity);
        }
    }

    /**
     * Εμφανίζει τα περιεχόμενα του καλαθιού.
     * Υπολογίζει και εκτυπώνει το συνολικό κόστος.
     */
    public void displayCart() {
        if (items.isEmpty()) {
            System.out.println("Το καλάθι είναι άδειο.");
            return;
        }

        System.out.println("Περιεχόμενα καλαθιού:");
        for (CartItem item : items.values()) {
            System.out.printf("- %s (%s): %.2f x %.2f€ = %.2f€\n",
                    item.getProduct().getTitle(),
                    item.getProduct().getQuantityLabel(),
                    item.getQuantity(),
                    item.getProduct().getPrice(),
                    item.getTotalCost()
            );
        }
        System.out.printf("Συνολικό κόστος: %.2f€\n", getTotalCost());
    }

    /**
     * Υπολογίζει το συνολικό κόστος όλων των προϊόντων στο καλάθι.
     *
     * @return Το συνολικό κόστος.
     */
    public double getTotalCost() {
        double total = 0;
        for (CartItem item : items.values()) {
            total += item.getTotalCost();
        }
        return total;
    }

    /**
     * Επιστρέφει ένα συγκεκριμένο προϊόν από το καλάθι με βάση τον τίτλο του.
     *
     * @param productTitle Ο τίτλος του προϊόντος που θα επιστραφεί.
     * @return Το αντικείμενο Product που αντιστοιχεί στο προϊόν.
     * @throws IllegalArgumentException Αν το προϊόν δεν υπάρχει στο καλάθι.
     */
    public Product getProductFromCart(String productTitle) {
        if (!items.containsKey(productTitle)) {
            throw new IllegalArgumentException("Το προϊόν δεν υπάρχει στο καλάθι.");
        }
        return items.get(productTitle).getProduct();
    }

    /**
     * Επιστρέφει όλα τα προϊόντα που υπάρχουν στο καλάθι ως λίστα CartItem.
     *
     * @return Μια λίστα με όλα τα αντικείμενα CartItem στο καλάθι.
     */
    public List<CartItem> getCart() {
        return new ArrayList<>(items.values());
    }

    /**
     * Εκκαθαρίζει όλα τα προϊόντα από το καλάθι.
     */
    public void clearCart() {
        items.clear();
    }

    /**
     * Checks if the cart is empty.
     *
     * @return True if the cart contains no products, false otherwise.
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Checks if a specific product is in the cart.
     *
     * @param product The product to check.
     * @return True if the product is in the cart, false otherwise.
     */
    public boolean containsProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        return items.containsKey(product.getTitle());
    }

    /**
     * Επιστρέφει το CartItem για ένα συγκεκριμένο προϊόν από το καλάθι.
     *
     * @param productTitle Ο τίτλος του προϊόντος που θα επιστραφεί.
     * @return Το αντικείμενο CartItem που αντιστοιχεί στο προϊόν.
     * @throws IllegalArgumentException Αν το προϊόν δεν υπάρχει στο καλάθι.
     */
    public CartItem getCartItem(String productTitle) {
        if (!items.containsKey(productTitle)) {
            throw new IllegalArgumentException("Το προϊόν δεν υπάρχει στο καλάθι.");
        }
        return items.get(productTitle);
    }
}