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

            /**
             * select t from Team t left outer join fetch t.members m
             *
             * - fetch join 은 나랑 연관된 모든 것들을 다 끌고오겠다는 것.
             *
             * 		물론 중간에 몇개를 걸러서 가져오고 싶을 수 있다.
             * 		하지만 그런경우에서는 fetch join을 사용하면 안된다.
             * 	ex)
             * 		team1 과 연관된 회원이 5명 인데,
             * 		위에 쿼리에 where m.age > 10 의 조건절을 넣었다고 하자.
             * 		그 경우 조회되는 회원이 1명이라고 한다면,
             * 		나머지 4명의 회원이 누락이 된다.
             * 		예를 들어 해당 연관관계에 cascade 같은 옵션이 걸려있을 경우,
             * 		데이터 정합성에 문제가 생기게 되어
             * 		애플리케이션이 기대하지 않은 동작을 수행할 수 있다.
             *
             * 	또한 객체 그래프 탐색의 의도된 목적은 t.members 를 하게 되었을 경우
             * 	team 한건의 엔티티에 속한 members 를 전부 가져오는 것이지,
             * 	team 한건의 엔티티에 속한 members 를 필터링하여 가져오는것이 아님.
             *
             * 	또한 둘 이상의 컬렉션은 페치 조인 할 수 없다.
             *
             * 		일대다 관계도 정합성에 문제가 생기는데,
             * 		일대다 일대다를 나열하듯 fetch join 을 사용하게 될 경우 겉잡을수 없다.
             */
            String query = "select t " +
                    "from Team t " +
                    "left outer join fetch t.members m ";
//                    "where m.username = :username";//fetch join 에서 alias 를 사용한 조건절 제어, fetch join 에서 하면 안됨.
            List<Team> teams = em.createQuery(query, Team.class)
//                    .setParameter("username", "회원3")
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
