package jpql;

import jpql.embeddable.Address;
import jpql.entity.Member;
import jpql.entity.Order;
import jpql.entity.Product;
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
         * 서브 쿼리
         * 한 건이라도 주문한 고객
         * select m from Member m
         * where (select count(o) from Order o where m = o.member) > 0
         */
        try {
            for (int i = 0; i < 40; i++) {
                Member member = Member.createMember("name"+i, 10 + i);
                em.persist(member);
                if (i % 4 == 0) {
                    Order order = Order.createOrder(member, 3 + i, new Address("city" + i, "street" + i, "1000" + i));
                    em.persist(order);
                }
                if (i % 2 == 0) {
                    Order order = Order.createOrder(member, 3 + i, new Address("city" + i, "street" + i, "1000" + i));
                    em.persist(order);
                }
            }

            em.flush();
            em.clear();

            String query =
                    "select m " +
                    "from Member m " +
                    "where (select count(o) from Order o where m = o.member) > 1 ";
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
