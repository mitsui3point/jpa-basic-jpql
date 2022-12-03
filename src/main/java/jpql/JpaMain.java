package jpql;

import jpql.entity.Member;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//application loading 시점에 한개만 생성

        EntityManager em = emf.createEntityManager();//실제 transaction 단위당 entityManager 를 생성

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member1 = new Member();
            member1.setUsername("user1");
            member1.setAge(10);
            em.persist(member1);

            //TypeQuery: 반환 타입이 명확할 때 사용
            TypedQuery<Member> typedQuery1 = em.createQuery("select m from Member m", Member.class);
            List<Member> typedQuery1Members = typedQuery1.getResultList();
            for (Member typedQuery1Member : typedQuery1Members) {
                System.out.println("typedQuery1Member = " + typedQuery1Member.getUsername());
            }
            
            TypedQuery<String> typedQuery2 = em.createQuery("select m.username from Member m", String.class);
            List<String> typedQuery2Members = typedQuery2.getResultList();
            for (String typedQuery2Member : typedQuery2Members) {
                System.out.println("typedQuery2Member = " + typedQuery2Member);
            }
            
            //Query: 반환 타입이 명확하지 않을 때 사용
            Query query = em.createQuery("select m.username, m.age from Member m");
            List<Object[]> queryMembers = query.getResultList();
            for (Object[] queryMember : queryMembers) {
                for (Object o : queryMember) {
                    System.out.println("o = " + o);
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
}
