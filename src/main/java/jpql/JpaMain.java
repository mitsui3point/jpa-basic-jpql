package jpql;

import jpql.embeddable.Address;
import jpql.entity.Member;
import jpql.entity.Order;
import jpql.entity.Team;

import javax.persistence.*;
import java.util.*;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//application loading 시점에 한개만 생성

        EntityManager em = emf.createEntityManager();//실제 transaction 단위당 entityManager 를 생성

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("username");
            em.persist(member);

            Order order = new Order();
            order.setMember(member);
            order.setAddress(new Address("city", "street", "10000"));
            em.persist(order);

            //SELECT m.address FROM Member m -> 임베디드 타입 프로젝션
            List<Address> addresses = em.createQuery("select o.address from Order o", Address.class)
                    .getResultList();

            for (Address address : addresses) {
                System.out.println("address.getCity() = " + address.getCity());
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
