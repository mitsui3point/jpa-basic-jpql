package jpql;

import jpql.entity.Member;
import jpql.entity.item.Book;
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
            Book book = Book.create("book", 10000, 100, "author1", "isbn1");
            em.persist(book);

            em.flush();
            em.clear();

            String query ="select i " +
                    "from Item i " +
                    "where type(i) = Book ";
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
