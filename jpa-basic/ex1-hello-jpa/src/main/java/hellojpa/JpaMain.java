package hellojpa;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        //엔티티 매니저 팩토리는 하나만 생성됨
        EntityManager em = emf.createEntityManager(); //엔티티 매니저는 쓰레드간 공유x 사용하고 버려야 한다.
        EntityTransaction tx = em.getTransaction();
        tx.begin(); //JPA모든 데이터는 트렌젝션 안에서 동작한다

        //code
        try {
            //Member findMember = em.find(Member.class, 1L);
            List<Member> result = em.createQuery("select m from Member as m", Member.class)
            .getResultList();

            for (Member member : result) {
                System.out.println("member = " + member.getName());
            }
            tx.commit();
        } catch (Exception e){
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
