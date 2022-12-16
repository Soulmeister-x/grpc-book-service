package home.oehler.grpc.res;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Orders {

    private List<Order> orderList = new ArrayList<>();
    private int newId;

    private Orders() {
        newId = 0;
    }
    
    private static class SingletonHelper {
        private static final Orders INSTANCE = new Orders();
    }

    public static Orders getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public int getNewId() {
        int maxId=0;
        for (Order o : orderList) {
            if (o.getOrderId()>maxId)
                maxId=o.getOrderId();
        }
        return orderList.isEmpty() ? 0 : maxId+1;
    }


    public int addOrder(Order newOrder) {
        orderList.add(newOrder);
        return newId++;
    }

    public Order getOrderById(int id) {
        for (Order o : orderList) {
            if (o.getOrderId() == id)
                return o;
        }
        return null;
    }


    
}
