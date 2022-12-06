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
            for (int i = 0; i < 15; i++) {
                Product product = Product.createProduct("product" + i, 1000 + (100 * i), i);
                em.persist(product);
            }

            em.flush();
            em.clear();

            String updateQuery = "update Product p set p.price = p.price * 1.1 where p.stockAmount < 10 ";
            int updateCount = em.createQuery(updateQuery)
                    .executeUpdate();
            System.out.println("updateCount = " + updateCount);

            List<Product> products = em.createQuery("select p from Product p ", Product.class).getResultList();
            for (Product product : products) {
                new ObjectPrinter(product).print();
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
