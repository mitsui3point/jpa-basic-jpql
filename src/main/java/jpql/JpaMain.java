package jpql;

import jpql.entity.Member;
import jpql.entity.Team;
import jpql.print.ObjectPrinter;

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

            String query = "select t " +
                    "from Team t " +
                    "left outer join fetch t.members m ";//fetch join 적용
            List<Team> teams = em.createQuery(query, Team.class)
                    .getResultList();

            System.out.println("resultList.size() = " + teams.size());
            for (Team team : teams) {
                new ObjectPrinter(team).print();
                new ObjectPrinter(team.getMembers()).print();//페치 조인으로 팀과 회원을 함께 조회해서 지연 로딩 발생 안함
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
