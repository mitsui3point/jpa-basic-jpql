package jpql;

import jpql.entity.Member;
import jpql.entity.Product;
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
            Member member = Member.createMember("name", 11);
            em.persist(member);

            Product product = Product.createProduct("name", 10000, 100);
            em.persist(product);

            em.flush();
            em.clear();
            /**
             * [ON vs WHERE]
             * ON : JOIN 을 하기 전 필터링을 한다 (=ON 조건으로 필터링이 된 레코들간 JOIN이 이뤄진다)
             * WHERE : JOIN 을 한 후 필터링을 한다 (=JOIN을 한 결과에서 WHERE 조건절로 필터링이 이뤄진다)
             * https://developyo.tistory.com/121
             */

            String query = "select m, p from Member m left join Product p on m.username = p.name";
            List resultList = em.createQuery(query)
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
                if (object instanceof Product) {
                    new Generic<>().print((Product) object);
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
                System.out.println("result = null");
            }
        }

        public void print(T result) {
            System.out.println("=====================");
            if (result != null) {
                System.out.println(result.getClass().getName() + "\t, result = " + result);
                return;
            }
            System.out.println("result = null");
        }
    }
}
