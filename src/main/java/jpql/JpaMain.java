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

            //SELECT m FROM Member m -> 엔티티 프로젝션
            List<Member> members = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            for (Member member : members) {
                System.out.println("member.getUsername() = " + member.getUsername());
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
