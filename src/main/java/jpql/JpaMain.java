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

        try {
            Member member1 = Member.createMember("name", 11);
            em.persist(member1);
            Member member2 = Member.createMember("name2", 11);
            em.persist(member2);

            Product product1 = Product.createProduct("name", 10000, 100);
            em.persist(product1);
            Product product2 = Product.createProduct("name2", 10000, 100);
            em.persist(product2);

            em.flush();
            em.clear();
            /**
             * [ON vs WHERE]
             * ON : JOIN 을 하기 전 필터링을 한다 (=ON 조건으로 필터링이 된 레코들간 JOIN이 이뤄진다)
             * WHERE : JOIN 을 한 후 필터링을 한다 (=JOIN을 한 결과에서 WHERE 조건절로 필터링이 이뤄진다)
             * https://developyo.tistory.com/121
             */

            String query = "select p, m from Member m left join Product p on m.username = p.name";
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
