package naver.naver_api.repository;

import naver.naver_api.domain.Board;
import naver.naver_api.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    //인터페이스지만 구현 안해도 된다! 스프링부트가 처리
    List<Board> findByTitle(String title);

    //네임드 쿼리와 마찬가지로 쿼리를 미리 파싱하므로 문법오타가 있을경우 실행시점에 에러발생!
    @Query("select b from Board b where b.title = :title and b.member.userName=:member")
    List<Board> findBoard(@Param("title") String title, @Param("member")String member);

    @Query("select new naver.naver_api.dto.BoardDto(b.id, b.title, m.userName, b.createdDate) from Board b join b.member m")
    List<BoardDto> findBoardDto();

    @Query("select b from Board b where b.member.userName in :names")
    List<Board> findBoardMember(@Param("names") Collection<String> names);

    /**
     * page는 totalCount를 지원 하지만 outer join경우는 성능저하가 발생할수 있으므로 데이터를 가져오는것과 count를 분리할것
     */
//    Page<Board> findByMemberUserName(String name, Pageable pageable);
    @Query(value = "select b from Board b join b.member",
            countQuery = "select count(b) from Board b")
    Page<Board> findByMemberUserName(String name, Pageable pageable);

    Page<Board> findAll(Pageable pageable);

}
