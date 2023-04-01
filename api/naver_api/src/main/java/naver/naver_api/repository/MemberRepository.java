package naver.naver_api.repository;

import naver.naver_api.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }

    public Member findById(Long id){
        return em.find(Member.class,id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    //엔티티가 없으면 NoResultException 예외발생
    public Member findByEmail(String email){
        return em.createQuery("select m from Member m where m.email=:email",Member.class)
                .setParameter("email",email)
                .getSingleResult();
    }

    public Long findByEmailCount(String email){
        return em.createQuery("select count(m) from Member m where m.email=:email",Long.class)
                .setParameter("email",email)
                .getSingleResult();
    }

    public Long findByNickNameCount(String nickName){
        return em.createQuery("select count(m) from Member m where m.nickName=:nickName",Long.class)
                .setParameter("nickName",nickName)
                .getSingleResult();
    }
}
