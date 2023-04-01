package naver.naver_api.repository;

import lombok.extern.slf4j.Slf4j;
import naver.naver_api.domain.Board;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Slf4j
public class BoardJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Board board){
        log.info("board save");
        em.persist(board);
        return board.getId();
    }

    public Board findById(Long id){
        return em.find(Board.class, id);
    }

    public List<Board> findAll(){
        return em.createQuery("select b from Board b",Board.class)
                .getResultList();
    }

    //paging
    public List<Board> findByPage(int offset, int limit){
        return em.createQuery("select b from Board b",Board.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public Long totalCount(){
        return em.createQuery("select count(b) from Board b",Long.class)
                .getSingleResult();
    }

}
