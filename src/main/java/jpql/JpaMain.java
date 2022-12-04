package jpql;

import jpql.entity.Member;
import jpql.entity.Product;
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
         * 서브 쿼리
         * • 나이가 평균보다 많은 회원
         * select m from Member m
         * where m.age > (select avg(m2.age) from Member m2)
         */
        try {
            for (int i = 0; i < 40; i++) {
                Member member = Member.createMember("name"+i, 10 + i);
                em.persist(member);
            }

            em.flush();
            em.clear();

            String query =
                    "select m " +
                    "from Member m " +
                    "where m.age > (select avg(m2.age) from Member m2)";
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
