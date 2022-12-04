package jpql;

import jpql.embeddable.Address;
import jpql.entity.Member;
import jpql.entity.Order;
import jpql.entity.Product;
import jpql.entity.Team;
import jpql.generic.Generic;

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

        /**
         * 서브 쿼리 지원 함수
         * 팀A 소속인 회원
         * select m from Member m
         * where exists (select t from m.team t where t.name = ‘팀A')
         */
        try {
            for (int i = 0; i < 40; i++) {
                Member member = Member.createMember("name" + i, 10 + i);

                Team team = Team.createTeam("teamA");
                if (i % 4 != 0 && i % 2 != 0) {
                    team = Team.createTeam("teamB");
                }
                em.persist(team);

                member.changeTeam(team);
                em.persist(member);
            }

            em.flush();
            em.clear();

            String query =
                    "select m, m.team " +
                    "from Member m " +
                    "where exists (select t from m.team t where t.name = :name) ";
            List resultList = em.createQuery(query)
                    .setParameter("name", "teamA")
                    .getResultList();

            System.out.println("resultList.size() = " + resultList.size());
            for (Object object : resultList) {
                new Generic<>(object);
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
