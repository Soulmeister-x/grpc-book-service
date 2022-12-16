package home.oehler.grpc.res;



public class Book {

    private int bookId;
    private String name;
    private String author;

    

    public Book() {}
    
    public Book(int bookId, String name) {
        this.bookId = bookId;
        this.name = name;
    }
    public Book(int bookId, String name, String author) {
        this.bookId = bookId;
        this.name = name;
        this.author = author;
    }

    public int getBookId() {
        return bookId;
    }
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book [id="+ bookId +", name="+name+", author="+author+"]";
    }



    
}
