package naver.naver_api.controller;

import naver.naver_api.domain.Member;
import naver.naver_api.session.SessionConst;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class userInfoController {

    @GetMapping("/userInfo")
    public String userInfo(HttpServletRequest request, Model model) {
//        HttpSession session = request.getSession(false);//true: 세션이 없으면 생성, false: 세션이 없으면 null
//        if(session == null){
//            return "index";
//        }
//
//        NaverMember member = (NaverMember)session.getAttribute(SessionConst.LOGIN_MEMBER);
//
//        if(member == null){
//            return "index";
//        }
//        model.addAttribute("member",member);

        //컨트롤러마다 세션을 체크하는 로직을 짜는것보다 필터, 인터럽트 사용

        HttpSession session = request.getSession();
        Member findMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        model.addAttribute("member", findMember);
        return "userInfo";
    }
}
