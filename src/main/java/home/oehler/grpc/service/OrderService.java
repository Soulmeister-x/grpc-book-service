package home.oehler.grpc.service;

import home.oehler.grpc.BookServiceGrpc;
import home.oehler.grpc.Bookshop;
import home.oehler.grpc.OrderServiceGrpc;
import home.oehler.grpc.res.Order;
import home.oehler.grpc.res.OrderItem;
import home.oehler.grpc.res.Orders;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

public class OrderService extends OrderServiceGrpc.OrderServiceImplBase {
    @Override
    public void getAllOrders(Bookshop.Empty request, StreamObserver<Bookshop.OrderResponse> responseObserver) {
        Orders.getInstance().getOrderList().forEach(o -> {
            responseObserver.onNext(Bookshop.OrderResponse.newBuilder()
                    .setOrderId(o.getOrderId())
                    .build());
        });

        responseObserver.onCompleted();
    }

    @Override
    public void getOrder(Bookshop.IdRequest request, StreamObserver<Bookshop.OrderResponse> responseObserver) {

        int id = request.getId();
        Order o = Orders.getInstance().getOrderById(id);

        try {

            Bookshop.OrderResponse.Builder b = Bookshop.OrderResponse.newBuilder()
                    .setOrderId(o.getOrderId());
            int index = 0;
            for (OrderItem i : o.getOrders()) {
                b.addOrders(Bookshop.OrderItem.newBuilder()
                        .setBookId(i.getBookId())
                        .setAmount(i.getAmount())
                        .build());
            }

            responseObserver.onNext(b.build());
            responseObserver.onCompleted();
        } catch (NullPointerException errNull ){
            System.err.println("NullPointerException: Order #"+id+" couldn't be fetched");
            responseObserver.onError(new IndexOutOfBoundsException());
        }

    }

    @Override
    public void createOrder(Bookshop.OrderRequest request, StreamObserver<Bookshop.IdResponse> responseObserver) {

        List<OrderItem> items = new ArrayList<>();
        request.getOrdersList().forEach(o -> {
            int bookId = o.getBookId();
            int amount = o.getAmount();
            items.add(new OrderItem(bookId, amount));
        });

        int newOrderId = Orders.getInstance().addOrder(new Order(Orders.getInstance().getNewId(), items));
        responseObserver.onNext(Bookshop.IdResponse.newBuilder()
                .setId(newOrderId)
                .build());
        responseObserver.onCompleted();
    }
    /**/

}
