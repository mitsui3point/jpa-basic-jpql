package jpql;

import jpql.entity.Member;
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
            Member member1 = new Member();
            member1.setUsername("user1");
            member1.setAge(10);

            Team team1 = new Team();
            team1.setName("team1");
            em.persist(team1);

            member1.setTeam(team1);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("user2");
            member2.setAge(12);

            Team team2 = new Team();
            team2.setName("team2");
            em.persist(team2);

            member2.setTeam(team2);
            em.persist(member2);

            //SELECT m.team FROM Member m -> 엔티티 프로젝션
            List<Team> results = em.createQuery("select m.team from Member m", Team.class)
                    .getResultList();

            for (Team result : results) {
                System.out.println("result = " + result.getName());
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
