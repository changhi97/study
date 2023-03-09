package naver.naver_api.controller;

import lombok.extern.slf4j.Slf4j;
import naver.naver_api.domain.Board;
import naver.naver_api.domain.Member;
import naver.naver_api.session.SessionConst;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class BoardController {

    @GetMapping("/write")
    public String writeBoard(){
        log.info("write Board");
        return "board/boardWrite";
    }

    @PostMapping("/write")
    @ResponseBody
    public String saveBoard(@ModelAttribute Board board,HttpServletRequest request){
//        String title = request.getParameter("title");
//        String writer = request.getParameter("writer");
//        String content = request.getParameter("content");
//        StringBuilder result = new StringBuilder();
//        result.append(title).append("/n").append(writer).append("/n").append(content);

        HttpSession session = request.getSession(); //세션으로 가져올지 세션으로 디비에서 멤버를 가져올지
        Member findMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        board.setMember(findMember);

        return board.toString();
    }
}
