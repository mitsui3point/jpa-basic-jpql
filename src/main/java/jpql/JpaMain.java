package jpql;

import jpql.entity.Member;
import jpql.entity.Team;
import jpql.print.ObjectPrinter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

import static jpql.enumulate.MemberType.ADMIN;
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
            Team team3 = Team.createTeam("team3");

            em.persist(team1);
            em.persist(team2);
            em.persist(team3);

            Member member1 = Member.createMember("회원1", 11, USER);
            Member member2 = Member.createMember("회원2", 12, USER);
            Member member3 = Member.createMember("회원3", 13, USER);
            Member member4 = Member.createMember(null, 14, USER);

            member1.changeTeam(team1);
            member2.changeTeam(team1);
            member3.changeTeam(team2);

            em.persist(member1);
            em.persist(member2);
            em.persist(member3);
            em.persist(member4);

            em.flush();
            em.clear();

            //하이버네이트는 경고 로그를 남기고 메모리에서 페이징(매우 위험)
            //WARN: HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
            //해결; LAZY Loading, team.getMembers() 를 객체 그래프 탐색할 때 마다 쿼리가 나가게 됨, 성능이 좋지 않음.
            String query = "select t " +
                    "from Team t ";
            List<Team> teams = em.createQuery(query, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(3)
                    .getResultList();

            System.out.println("resultList.size() = " + teams.size());
            for (Team team : teams) {
                new ObjectPrinter(team).print();
                new ObjectPrinter(team.getMembers()).print();
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
