package jpql;

import jpql.entity.Product;
import jpql.print.ObjectPrinter;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//application loading 시점에 한개만 생성

        EntityManager em = emf.createEntityManager();//실제 transaction 단위당 entityManager 를 생성

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Product product1 = Product.createProduct("product1", 1000 + (100 * 1), 1);
            em.persist(product1);
            Product product2 = Product.createProduct("product2", 1000 + (100 * 2), 2);
            em.persist(product2);
            Product product3 = Product.createProduct("product3", 1000 + (100 * 3), 3);
            em.persist(product3);

            //FLUSH 자동 호출: commit or query 호출
            String updateQuery = "update Product p set p.price = p.price * 1.1 where p.stockAmount < 10 ";
            int updateCount = em.createQuery(updateQuery)
                    .executeUpdate();
            System.out.println("updateCount = " + updateCount);
            System.out.println("product2 = " + product2);

            /**
             * updateQuery 가 DB에만 반영되어 있기 때문에,
             * em.find(), em.createQuery().getResultList() 로 재조회해도 기존 값이 남아 있다.
             * 영속성 컨텍스트를 초기화(clear) 해 주어야 조회시 벌크연산이 완료된 DB 값이 조회되고,? 1차캐시에 재등록된다.
             *
             * 추후 spring data jpa 에서는
             * 이런 방식으로 제공한다. https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.modifying-queries
             */
            em.clear();

            List<Product> products = em.createQuery("select p from Product p ", Product.class).getResultList();
            for (Product p : products) {
                new ObjectPrinter(p).print();
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
