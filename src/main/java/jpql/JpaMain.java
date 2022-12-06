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
            Member member1 = Member.createMember("name1", 10, ADMIN);
            em.persist(member1);
            Member member2 = Member.createMember("name2", 10, ADMIN);
            em.persist(member2);

            em.flush();
            em.clear();

            String query = "select m " +
                    "from Member m " +
                    "where m = :member ";
            List members = em.createQuery(query)
                    .setParameter("member", member1)
                    .getResultList();

            for (Object m : members) {
                new ObjectPrinter(m).print();
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
