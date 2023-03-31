package naver.naver_api.repository;

import naver.naver_api.domain.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardJpaRepositoryTest {

    @Autowired
    BoardJpaRepository boardJpaRepository;

    @Test
    void paging(){
        int offset = 0;
        int limit = 10;

        List<Board> byPage = boardJpaRepository.findByPage(offset, limit);
        Long totalCount = boardJpaRepository.totalCount();
        System.out.println(totalCount);
        System.out.println(byPage.size());
    }

}