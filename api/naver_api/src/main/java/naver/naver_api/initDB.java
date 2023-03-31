package naver.naver_api;

import naver.naver_api.domain.Board;
import naver.naver_api.domain.Member;
import naver.naver_api.service.BoardService;
import naver.naver_api.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class initDB {
    private final BoardService boardService;
    private final MemberService memberService;

    @Autowired
    public initDB(BoardService boardService, MemberService memberService) {
        this.boardService = boardService;
        this.memberService = memberService;
    }

    @PostConstruct
    public void init() {
        DBinit2();
    }

    public void DBinit() {
        Member memberA = new Member("memberA", "memberA@gmail.com");
        memberService.join(memberA);

        Member memberB = new Member("memberB", "memberB@gmail.com");
        memberService.join(memberB);

        Member memberC = new Member("memberC", "memberC@gmail.com");
        memberService.join(memberC);

        Board boardA = new Board(memberA, "test1", "testing", null, null);
        boardService.save(boardA);

        Board boardB = new Board(memberA, "test2", "testing", null, null);
        boardService.save(boardB);

        Board boardC = new Board(memberB, "test3", "testing", null, null);
        boardService.save(boardC);

        Board boardD = new Board(memberC, "test4", "testing", null, null);
        boardService.save(boardD);
    }

    public void DBinit2() {
        Member members[] = {
                new Member("memberA", "memberA@gmail.com"),
                new Member("memberB", "memberB@gmail.com"),
                new Member("memberC", "memberC@gmail.com")
        };
        for (Member member : members) {
            memberService.join(member);
        }

        for(int i = 0; i<99; i++){
            int titleIdx = (int) (Math.random() * 2);
            int memberIdx = (int) (Math.random() * 3);
            boardService.save(new Board(members[memberIdx],"test"+titleIdx,"testing",null,null));
        }
    }


}
