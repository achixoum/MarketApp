package api.users;

import api.objects.*;
import api.management.*;
import java.util.List;

public interface CustomerActions {
    void addToCart(Product product, double quantity);
    void updateCart(Product product, double quantity);
    void removeFromCart(Product product);
    List<Order> viewOrderHistory();
    Order completeOrder(Inventory inventory) throws Exception;
}
