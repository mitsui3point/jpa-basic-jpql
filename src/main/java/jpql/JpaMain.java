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
         * 어떤 팀이든 팀에 소속된 회원
         * select m from Member m
         * where m.team = ANY (select t from Team t)
         */
        try {
            //         어떤 팀이든 팀에 소속된 회원
            for (int i = 0; i < 40; i++) {
                Member member = Member.createMember("name" + i, 10 + i);

                if (i % 4 != 0 && i % 2 != 0) {
                    Team team = Team.createTeam("teamA");
                    em.persist(team);

                    member.changeTeam(team);
                }

                if (i % 4 == 0 || i % 2 == 0) {
                    Team team = Team.createTeam("teamB");
                    em.persist(team);

                    member.changeTeam(team);
                }

                em.persist(member);
            }

            em.flush();
            em.clear();

            String query =
                    "select m " +
                    "from Member m " +
                    "where m.team = ANY (select t from Team t) ";
            List resultList = em.createQuery(query)
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
