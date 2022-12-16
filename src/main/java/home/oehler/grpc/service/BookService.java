package home.oehler.grpc.service;

import home.oehler.grpc.BookServiceGrpc;
import home.oehler.grpc.Bookshop;
import home.oehler.grpc.res.Book;
import home.oehler.grpc.res.Books;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

public class BookService extends BookServiceGrpc.BookServiceImplBase {
    @Override
    public void getAllBooks(Bookshop.Empty request, StreamObserver<Bookshop.BookResponse> responseObserver) {
        for (Book book : Books.getInstance().getBookList()) {
            responseObserver.onNext(Bookshop.BookResponse.newBuilder()
                    .setId(book.getBookId())
                    .setTitle(book.getName())
                    .setAuthor(book.getAuthor())
                    .build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void getBook(Bookshop.IdRequest request, StreamObserver<Bookshop.BookResponse> responseObserver) {
        int id = request.getId();
        Book book = Books.getInstance().getBookById(id);

        try {
            responseObserver.onNext(Bookshop.BookResponse.newBuilder()
                    .setId(book.getBookId())
                    .setTitle(book.getName())
                    .setAuthor(book.getAuthor())
                    .build());

            responseObserver.onCompleted();
        } catch (NullPointerException errNull ){
            System.err.println("NullPointerException: Book couldn't be fetched");
            responseObserver.onError(new IndexOutOfBoundsException());
        }
    }

    /**/
}


