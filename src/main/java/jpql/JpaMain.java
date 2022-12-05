package jpql;

import jpql.entity.Member;
import jpql.entity.Team;
import jpql.generic.GenericPrint;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

import static jpql.enumulate.MemberType.USER;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//application loading 시점에 한개만 생성

        EntityManager em = emf.createEntityManager();//실제 transaction 단위당 entityManager 를 생성

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team team1 = Team.createTeam("team1");
            Team team2 = Team.createTeam("team2");

            Member member1 = Member.createMember("member1", 11, USER);
            Member member2 = Member.createMember("member2", 11, USER);

            member1.changeTeam(team1);
            member2.changeTeam(team1);

            em.persist(team1);
            em.persist(team2);

            em.persist(member1);
            em.persist(member2);

            em.flush();
            em.clear();

            String query = "select function('group_concat', m.username) from Member m";
            List resultList = em.createQuery(query)
                    .getResultList();

            System.out.println("resultList.size() = " + resultList.size());
            for (Object object : resultList) {
                new GenericPrint<>(object);
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
