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
            Team team2 = Team.createTeam("team2");
            Team team3 = Team.createTeam("team3");
            em.persist(team1);
            em.persist(team2);
            em.persist(team3);

            Member member1 = Member.createMember("member1", 13);
            Member member2 = Member.createMember("member2", 20);
            Member member3 = Member.createMember("member3", 4);
            Member member4 = Member.createMember("member4", 55);

            member1.changeTeam(team1);
            member2.changeTeam(team1);
            member3.changeTeam(team2);
            em.persist(member1);
            em.persist(member2);
            em.persist(member3);
            em.persist(member4);

            em.flush();
            em.clear();
            /**
             * [ON vs WHERE]
             * ON : JOIN 을 하기 전 필터링을 한다 (=ON 조건으로 필터링이 된 레코들간 JOIN이 이뤄진다)
             * WHERE : JOIN 을 한 후 필터링을 한다 (=JOIN을 한 결과에서 WHERE 조건절로 필터링이 이뤄진다)
             * https://developyo.tistory.com/121
             */

//            String query = "select t from Team t left join t.members m on t.name = :name";
//            String query = "select t from Team t left join t.members m on t.username = :username";
//            String query = "select t, m from Team t left join t.members m on t.name = :name";
//            String query = "select t, m from Team t left join t.members m on m.username = :username";
//            String query = "select m from Member m left join m.team t on m.name = :name";
//            String query = "select m from Member m left join m.team t on m.username = :username";
            String query = "select m, t from Member m left join m.team t on t.name = :name";
//            String query = "select m, t from Member m left join m.team t on m.username = :username";
            List resultList = em.createQuery(query)
                    .setParameter("name", "team1")
//                    .setParameter("username", "member1")
                    .getResultList();

            System.out.println("resultList.size() = " + resultList.size());
            for (Object object : resultList) {
                if (object instanceof Object[]) {
                    new Generic<>().print((Object[]) object);
                }
                if (object instanceof Member) {
                    new Generic<>().print((Member) object);
                }
                if (object instanceof Team) {
                    new Generic<>().print((Team) object);
                }
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

    static class Generic<T> {
        public void print(T[] results) {
            System.out.println("=====================");
            for (T result : results) {
                if (result != null) {
                    System.out.println(result.getClass().getName() + "\t: result = " + result);
                    continue;
                }
                System.out.println("result = " + result);
            }
        }

        public void print(T result) {
            System.out.println("=====================");
            if (result != null) {
                System.out.println(result.getClass().getName() + "\t, result = " + result);
                return;
            }
            System.out.println("result = " + result);
        }
    }
}
