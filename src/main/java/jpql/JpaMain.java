package jpql;

import jpql.entity.Member;
import jpql.entity.Team;

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

        try {
            Team team1 = Team.createTeam("team1");
            em.persist(team1);
            Team team2 = Team.createTeam("team2");
            em.persist(team2);

            Member member1 = Member.createMember("member1", 13);
            Member member2 = Member.createMember("member2", 20);
            Member member3 = Member.createMember("member3", 4);

            member1.changeTeam(team1);
            member2.changeTeam(team1);
            em.persist(member1);
            em.persist(member2);
            em.persist(member3);

            em.flush();
            em.clear();

            String query = "select m, t from Member m inner join m.team t";
            List<Object[]> resultList = em.createQuery(query, Object[].class)
                    .getResultList();
            for (Object[] objects : resultList) {
                for (Object object : objects) {
                    System.out.println("object = " + object);
                }
            }

//            String query = "select t from Member m left outer join m.team t";
//            List<Team> teams = em.createQuery(query, Team.class)
//                    .getResultList();
//            for (Team t : teams) {
//                System.out.println("t = " + t);
//            }

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
