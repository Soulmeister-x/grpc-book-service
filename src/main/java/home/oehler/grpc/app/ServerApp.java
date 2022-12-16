package home.oehler.grpc.app;

import home.oehler.grpc.service.BookService;
import home.oehler.grpc.service.OrderService;
import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class ServerApp {
    public static void main(String[] args) {
        System.out.println("Starting gRPC Shop-Server!");
        InetAddress addr = null;
        try {
            addr = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            System.err.println("Error: Unknown Host!");
            System.exit(1);
        }
        int port = 8080;
        SocketAddress sockaddr = new InetSocketAddress(addr, port);
        Server server = NettyServerBuilder.forAddress(sockaddr)
                .addService(new BookService())
                .addService(new OrderService())
                .build();

        try {
            server.start();
        } catch (IOException e) {
            System.out.println("Connection closed.");
        }
        System.out.println("Shop-Server Started at "+server.getPort());
        try {
            server.awaitTermination();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
}
