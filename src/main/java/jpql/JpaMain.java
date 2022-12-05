package jpql;

import jpql.entity.Member;
import jpql.entity.Team;
import jpql.enumulate.MemberType;
import jpql.generic.GenericPrint;

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

            Member member1 = Member.createMember("member1", 11, USER);

            em.persist(member1);

            em.flush();
            em.clear();

            String query = "select m, " +
                    "concat('a', 'b') as concatEx, " +
                    //hibernate 구현체 지원문법. jpa 표준 interface 에서는 지원하지 않는 문법(jpa 사용시 90% 는 hibernate 구현체를 사용)
                    //"'a' || 'b' as doublePipeEx, "
                    "substring('jpabook', 4, 3) as substringEx, " +
                    "trim('         jpa book') as trimEx, " +
                    "lower('JPABOOK') as lowerEx, " +
                    "upper('jpabook') as upperEx, " +
                    "length('jpabook') as lengthEx, " +
                    "locate('boo', 'jpabook') as locateEx, " +
                    "abs(-33L) as absEx, " +//정수
                    "sqrt(33L) as sqrtEx, " +//제곱근
                    "mod(33L, 5L) as modEx " +//나머지
                    "from Member m ";
            List resultList = em.createQuery(query)
                    .getResultList();

            System.out.println("resultList.size() = " + resultList.size());
            for (Object object : resultList) {
                new GenericPrint<>(object);
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
