package jpql;

import jpql.entity.Member;
import jpql.generic.Generic;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

import static jpql.enumulate.MemberType.*;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//application loading 시점에 한개만 생성

        EntityManager em = emf.createEntityManager();//실제 transaction 단위당 entityManager 를 생성

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = Member.createMember("member1", 33, USER);
            em.persist(member);

            em.flush();
            em.clear();

            String query ="select m, 'STRING', 'SHE''S', 10, 10L, 10.01D, 10.01F, TRUE " +
                    "from Member m " +
//                    "where m.type = jpql.enumulate.MemberType.USER";
                    "where m.type = :memberType";
            List resultList = em.createQuery(query)
                    .setParameter("memberType", USER)
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
