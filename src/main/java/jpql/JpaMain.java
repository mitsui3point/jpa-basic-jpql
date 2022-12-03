package jpql;

import jpql.entity.Member;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//application loading 시점에 한개만 생성

        EntityManager em = emf.createEntityManager();//실제 transaction 단위당 entityManager 를 생성

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member1 = new Member();
            member1.setUsername("user1");
            member1.setAge(11);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("user2");
            member2.setAge(13);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("user2");
            member3.setAge(22);
            em.persist(member3);

            Member member4 = new Member();
            member4.setUsername("user3");
            member4.setAge(29);
            em.persist(member4);

            Member member5 = new Member();
            member5.setUsername("user1");
            member5.setAge(29);
            em.persist(member5);

            List<Object[]> members = em.createQuery(
                    "select m.username," +
                            " count(m) as cnt," +
                            " sum(m.age) as sum," +
                            " avg(m.age) as avg," +
                            " max(m.age) as max," +
                            " min(m.age) as min" +
                            " from Member as m" +
                            " group by m.username" +
                            " having count(m) > 1" +
                            " order by m.username desc")
                    .getResultList();

            for (Object[] member : members) {
                System.out.println("========================");
                System.out.println("member = " + member[0]);
                System.out.println("member = " + member[1]);
                System.out.println("member = " + member[2]);
                System.out.println("member = " + member[3]);
                System.out.println("member = " + member[4]);
                System.out.println("member = " + member[5]);
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
