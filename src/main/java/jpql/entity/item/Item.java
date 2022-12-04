package jpql.entity.item;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Item {
    @Id
    @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }


    //== 연관관계 편의 메서드==//
    protected void putName(String name) {
        this.name = name;
    }

    protected void putPrice(int price) {
        this.price = price;
    }

    protected void putStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
