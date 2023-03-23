package naver.naver_api.controller;

import naver.naver_api.domain.Member;
import naver.naver_api.service.MemberService;
import naver.naver_api.session.SessionConst;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/join")
public class joinController {

    private final MemberService memberService;

    public joinController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/oauthMember")
    public String addOauthMember(@ModelAttribute Member member){
        return "members/addOauthMemberForm";
    }

    @PostMapping("/oauthMember")
    public String saveOauthMember(@Valid @ModelAttribute Member member, BindingResult bindingResult, HttpServletRequest request){
        if(memberService.DuplicateMemberNickName(member)){
            bindingResult.addError(new FieldError("member","nickName","중복된 별명입니다."));
        }

        if(bindingResult.hasErrors()){
            return "members/addOauthMemberForm";
        }

        //여기서 조인 세션저장?...
        HttpSession session = request.getSession();
        Member findMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        Member joinMember = new Member(findMember.getUserName(),findMember.getEmail(),member.getNickName());

        session.setAttribute(SessionConst.LOGIN_MEMBER, joinMember);
        memberService.join(joinMember);
        return "redirect:/";
    }
}
