package jpql;

import jpql.entity.Member;
import jpql.entity.Team;
import jpql.entity.item.Book;
import jpql.entity.item.Item;
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
            Book book = Book.create("book1", 1000, 10, "author", "1000-1");
            em.persist(book);

            em.flush();
            em.clear();

            List<Item> resultList = em.createQuery("select i " +
                            "from Item i " +
                            "where type(i) in (Book)", Item.class)
                    .getResultList();

            for (Item item : resultList) {
                new ObjectPrinter(item).print();
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
