package jpql;

import jpql.entity.Member;

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
            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setAge(12);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member1");
            member2.setAge(12);
            em.persist(member2);

            //SELECT m.username, m.age FROM Member m -> 스칼라 타입 프로젝션
            //DISTINCT로 중복 제거
            List members = em.createQuery("select distinct m.username, m.age from Member m")
                    .getResultList();

            for (Object m : members) {
                Object[] member = (Object[]) m;
                System.out.println("member[0] = " + member[0]);
                System.out.println("member[1] = " + member[1]);
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
