package naver.naver_api.repository;

import lombok.extern.slf4j.Slf4j;
import naver.naver_api.domain.Board;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Slf4j
public class BoardRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Board board){
        log.info("board save");
        em.persist(board);
        return board.getId();
    }

}
