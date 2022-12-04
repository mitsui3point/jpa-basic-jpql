package jpql.entity;

import jpql.embeddable.Address;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    private int orderAmount;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @Embedded
    private Address address;

    protected Order() {
    }

    public Long getId() {
        return id;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public Member getMember() {
        return member;
    }

    public Product getItem() {
        return product;
    }

    public Address getAddress() {
        return address;
    }

    //==연관관계 편의 메서드==//

    public static Order createOrder(Member member, int orderAmount, Address address) {
        Order order = new Order();
        order.member = member;
        order.orderAmount = orderAmount;
        order.address = address;
        return order;
    }

    public void putProduct(Product product) {
        this.product = product;
    }
}