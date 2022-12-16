package home.oehler.grpc.res;

import java.util.List;

public class Order {

    private int orderId;
    private List<OrderItem> orders;

    
    /*** Constructors ***/
    public Order() {
    }
    public Order(int orderId, List<OrderItem> orders) {
        this.orderId = orderId;
        this.orders = orders;
    }

    /*** Functions ***/

    @Override
    public String toString() {
        String s =
                "Order{" +
                "orderID=" + orderId;
        for (OrderItem i : orders) {
            s += ",{" +
                    "bookId=" + i.getBookId() +
                    ",amount=" + i.getAmount() +
                    "}";
        }
        return s+"}";
    }

    /*** GETTERS & SETTERS ***/
    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public List<OrderItem> getOrders() {return orders;}
    public void setOrders(List<OrderItem> orders) {this.orders = orders;}
    /*** ***/

    
}
