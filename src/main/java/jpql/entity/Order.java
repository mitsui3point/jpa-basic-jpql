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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
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

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderAmount=" + orderAmount +
                ", address=" + address +
                '}';
    }

    public void putProduct(Product product) {
        this.product = product;
    }
}