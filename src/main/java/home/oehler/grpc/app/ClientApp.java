package home.oehler.grpc.app;

import home.oehler.grpc.BookServiceGrpc;
import home.oehler.grpc.Bookshop;
import home.oehler.grpc.OrderServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ClientApp {
    public static void main(String[] args) {
        System.out.println("Running gRPC Shop client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080).usePlaintext().build();

        BookServiceGrpc.BookServiceBlockingStub itemServiceStub = BookServiceGrpc.newBlockingStub(channel);
        OrderServiceGrpc.OrderServiceBlockingStub orderServiceBlockingStub = OrderServiceGrpc.newBlockingStub(channel);
        Scanner scanner = null;

        try {
            scanner = new Scanner(System.in);
            boolean finish = false;
            String stars = "*************************************";

            while (!finish) {
                String tmp="";

                System.out.println(stars+"\nEnter operation code \n"+
                        "1: ask for specific book;\n" +
                        "2: fetch all books; \n"+
                        "3: order books;\n" +
                        "4: fetch all orders;\n" +
                        "5: get order details;\n"+
                        "0: exit");

                Integer opr = scanner.nextInt();
                System.out.println(stars);
                switch (opr) {
                    case 1:
                        // GET single book
                        // FUNNY: if you change print to println, the error message will be printed differently
                        System.out.print("Enter book id:");



                        int bookId = scanner.nextInt();
                        Bookshop.BookResponse book = null;
                        try {
                            book = itemServiceStub.getBook(Bookshop.IdRequest.newBuilder()
                                    .setId(bookId)
                                    .build());
                        } catch (StatusRuntimeException error) {
                            System.err.printf("Error while calling BookService: Book #%d not found\n", bookId);
                            continue;
                        }
                        System.out.println("Book #"+book.getId()
                                + "\ntitle:  " + book.getTitle()
                                + "\nauthor: " + book.getAuthor());
                        break;
                    case 2:
                        // GET all books
                        Iterator<Bookshop.BookResponse> bookResponse = itemServiceStub.getAllBooks(Bookshop.Empty.newBuilder().build());
                        // print all books
                        System.out.println("All Books:");
                        bookResponse.forEachRemaining( o -> {
                            String s =  "\nBook   #"+o.getId()
                                    + "\ntitle:  " + o.getTitle()
                                    + "\nauthor: " + o.getAuthor();
                            System.out.println(s);
                        });
                        break;
                    case 3:
                        // POST new order
                        Bookshop.OrderRequest.Builder b = Bookshop.OrderRequest.newBuilder();
                        System.out.println("Order books:");
                        do {
                            System.out.print("Enter book id:\t");
                            int id = scanner.nextInt();
                            System.out.print("Enter amount:\t");
                            int amount = scanner.nextInt();

                            // add new order to list
                            b.addOrders(Bookshop.OrderItem.newBuilder()
                                            .setBookId(id)
                                            .setAmount(amount)
                                    .build());

                            System.out.println("Order another book? [y|n]");
                        } while (scanner.next().equals("y"));

                        Bookshop.IdResponse response = orderServiceBlockingStub.createOrder(b.build());
                        System.out.println(stars+"\nOrder created: #" + response.getId());
                        break;
                    case 4:
                        // GET all orders
                        System.out.println("Your Orders:");
                        Iterator<Bookshop.OrderResponse> orderResponse = orderServiceBlockingStub.getAllOrders(Bookshop.Empty.newBuilder().build());
                        orderResponse.forEachRemaining(o -> {
                            System.out.println("Order  #"+ o.getOrderId());

                            for (Bookshop.OrderItem x : o.getOrdersList()) {
                                System.out.println("\n  BookId:  #"+x.getBookId());
                                System.out.println("  Amount:  "+x.getAmount());
                            };
                        });
                        System.out.println(stars);
                        break;

                    case 5:
                        // GET single order by id
                        System.out.print("Enter order ID: ");
                        int orderId = scanner.nextInt();

                        Bookshop.IdRequest request =  Bookshop.IdRequest.newBuilder()
                                .setId(orderId)
                        .build();
                        Bookshop.OrderResponse responseOrder = null;
                        try {
                            responseOrder = orderServiceBlockingStub.getOrder(request);
                        } catch (StatusRuntimeException error) {
                            System.err.printf("Error while calling OrderService: Order #%d not found\n", orderId);
                            continue;
                        }
                        System.out.printf("\nOrder  #"+ responseOrder.getOrderId());
                        for (Bookshop.OrderItem x : responseOrder.getOrdersList()) {
                            System.out.println("  BookId: #"+x.getBookId());
                            System.out.println("  Amount:  "+x.getAmount());
                        };
                        break;

                    default:
                        finish = true;
                        break;
                }

            }
        } finally {
            if (scanner != null)
                scanner.close();
        }

        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
