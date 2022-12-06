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
            em.persist(team1);

            Member member1 = Member.createMember("member1", 11, USER);
            Member member2 = Member.createMember("member2", 11, USER);

            member1.changeTeam(team1);
            member2.changeTeam(team1);

            em.persist(member1);
            em.persist(member2);

            em.flush();
            em.clear();

            String query = "select m " +
                    "from Team t " +
                    "inner join t.members m ";//컬렉션 값 연관 필드; 명시적 조인
            List resultList = em.createQuery(query)
                    .getResultList();

            System.out.println("resultList.size() = " + resultList.size());
            for (Object object : resultList) {
                new ObjectPrinter(object).print();
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
