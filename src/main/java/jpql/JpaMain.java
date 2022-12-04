package jpql;

import jpql.entity.Member;
import jpql.entity.Team;
import jpql.generic.GenericPrint;

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
            Member member1 = Member.createMember("dd", 70, ADMIN);
            Member member2 = Member.createMember("cc", 9, ADMIN);
            Member member3 = Member.createMember(null, 20, USER);
            Member member4 = Member.createMember("관리자", 20, USER);

            Team teamA = Team.createTeam("teamA");
            Team teamB = Team.createTeam("teamB");
            Team teamC = Team.createTeam("teamC");

            member1.changeTeam(teamA);
            member2.changeTeam(teamB);
            member3.changeTeam(teamA);
            member4.changeTeam(teamC);

            em.persist(teamA);
            em.persist(teamB);
            em.persist(teamC);

            em.persist(member1);
            em.persist(member2);
            em.persist(member3);
            em.persist(member4);

            em.flush();
            em.clear();

            String query = "select m, " +
                    //기본 case
                    "case when m.age >= 60 then '경로요금' " +
                    "     when m.age <= 10 then '학생요금' " +
                    "                      else '일반요금' end, " +
                    //단순 case
                    "case m.team.name when 'teamA' then '인센110' " +
                    "                 when 'teamB' then '인센120' " +
                    "                 else '인센105' end, " +
                    //COALESCE: 하나씩 조회해서 null이 아니면 반환
                    "coalesce(m.username, '이름없는회원'), " +
                    //NULLIF: 두 값이 같으면 null 반환, 다르면 첫번째 값 반환
                    "nullif(m.username, '관리자') " +
                    "from Member m ";
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
