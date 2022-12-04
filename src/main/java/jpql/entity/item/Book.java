package jpql.entity.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "B")
public class Book extends Item {
    private String author;
    private String isbn;

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public static Book create(String name, int price, int stockQuantity, String author, String isbn) {
        Book item = new Book();
        item.putName(name);
        item.putPrice(price);
        item.putStockQuantity(stockQuantity);
        item.author = author;
        item.isbn = isbn;
        return item;
    }

    @Override
    public String toString() {
        return "Book{" +
                "author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                '}';
    }
}
