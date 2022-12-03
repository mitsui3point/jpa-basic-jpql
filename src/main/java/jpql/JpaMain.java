package jpql;

import jpql.entity.Member;

import javax.persistence.*;
import java.util.*;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//application loading 시점에 한개만 생성

        EntityManager em = emf.createEntityManager();//실제 transaction 단위당 entityManager 를 생성

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member1 = new Member();
            member1.setUsername("user1");
            member1.setAge(10);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("user2");
            member2.setAge(12);
            em.persist(member2);

            Member singleResult = em.createQuery("select m from Member m where m.username = ?1", Member.class)
                    .setParameter(1, "user2")
                    .getSingleResult();
            System.out.println("singleResult = " + singleResult.getUsername());
            System.out.println("singleResult = " + singleResult.getAge());

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
