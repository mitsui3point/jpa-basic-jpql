package jpql;

import jpql.dto.MemberDTO;
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

            List<MemberDTO> resultList = em.createQuery("select distinct new jpql.dto.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();

            for (MemberDTO memberDTO : resultList) {
                System.out.println("memberDTO.getUsername() = " + memberDTO.getUsername());
                System.out.println("memberDTO.getAge() = " + memberDTO.getAge());
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
