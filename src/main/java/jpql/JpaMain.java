package jpql;

import jpql.embeddable.Address;
import jpql.entity.Member;
import jpql.entity.Order;
import jpql.entity.Product;
import jpql.entity.Team;
import jpql.generic.Generic;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//application loading 시점에 한개만 생성

        EntityManager em = emf.createEntityManager();//실제 transaction 단위당 entityManager 를 생성

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        /**
         * 서브 쿼리 지원 함수
         * 전체 상품 각각의 재고보다 주문량이 많은 주문들
         * select o from Order o
         * where o.orderAmount > ALL (select p.stockAmount from Product p)
         */
        try {
            //         * 전체 상품 각각의 재고보다 주문량이 많은 주문들
            for (int i = 0; i < 40; i++) {
                Member member = Member.createMember("name" + i, 10 + i);

                if (i % 4 != 0 && i % 2 != 0) {
                    Product product = Product.createProduct("product" + i, 1000, 80 + i);
                    Order order = Order.createOrder(member, 100, new Address("city" + i, "street " + i, "1000 " + i));
                    order.putProduct(product);

                    em.persist(product);
                    em.persist(order);
                }

                em.persist(member);
            }

            em.flush();
            em.clear();

            String query =
                    "select o, o.product " +
                    "from Order o " +
                    "where o.orderAmount > ALL (select p.stockAmount from o.product p) ";
            List resultList = em.createQuery(query)
                    .getResultList();

            System.out.println("resultList.size() = " + resultList.size());
            for (Object object : resultList) {
                new Generic<>(object);
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }
}
