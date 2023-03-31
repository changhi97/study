package naver.naver_api.repository;

import naver.naver_api.domain.Board;
import naver.naver_api.domain.Member;
import naver.naver_api.dto.BoardDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void saveTest(){
        System.out.println(boardRepository.count());


        Member master = new Member("master", "master@gmail.com");
        memberRepository.save(master);

        Board boardA = new Board(master, "test1", "testing", null, null);
        boardRepository.save(boardA);

        System.out.println(boardRepository.count());
    }

    @Test
    void findByTitle(){
        List<Board> result = boardRepository.findByTitle("test1");
        System.out.println(result.size());
    }

    @Test
    void findByTitleAndTitleGreaterThan(){
        List<Board> result = boardRepository.findByTitle("test1");
        System.out.println("findByTitle= " + result.size());

    }

    @Test
    void findBoardTest(){
        List<Board> result = boardRepository.findBoard("test1","memberA");
        System.out.println(result.size());
    }

    @Test
    void findMemberDto(){
        List<BoardDto> boardDto = boardRepository.findBoardDto();
        for (BoardDto dto : boardDto) {
            System.out.println(dto);
        }

    }

    @Test
    void findBoardMember(){
        List<Board> boardMember = boardRepository.findBoardMember(Arrays.asList("memberA","memberC"));
        for (Board board : boardMember) {
            System.out.println(board.getMember().getUserName());
        }
    }

    @Test
    void paging(){

        PageRequest pageRequest = PageRequest.of(6, 3);

        Page<Board> result = boardRepository.findByMemberUserName("memberA", pageRequest);

        for (Board board : result) {
            System.out.println("board.getMember()= "+board.getMember().getUserName());
        }

        //entity -> dto
        Page<BoardDto> map = result.map(b -> new BoardDto(b.getId(), b.getMember().getId(), b.getMember().getUserName()));

    }


}