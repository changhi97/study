package naver.naver_api.controller;

import lombok.extern.slf4j.Slf4j;
import naver.naver_api.domain.Member;
import naver.naver_api.service.MemberService;
import naver.naver_api.session.SessionConst;
import naver.naver_api.validation.MemberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@Slf4j
@RequestMapping("/join")
public class joinController {

    private final MemberService memberService;
    private final MemberValidator memberValidator;

    @Autowired
    public joinController(MemberService memberService, MemberValidator memberValidator) {
        this.memberService = memberService;
        this.memberValidator = memberValidator;
    }

    /**WebDataBinder에 검증기를 추가하면 해당 컨트롤러에서 검증기 자동 수행(@Validated 또는 @Valid 를 추가하면 검증기를 실행하라는뜻)
     * ((Bean Validation에서도 동일하게 적용)
     * [그렇다면 검증기가 여러개이면 어떻게 구별하는가?]
     * support가 수행되고 결과가 true이면 memberValidator의 validtor가 수행
     *
     * [글로벌 설정]
     * main문에 선언
     */
    @InitBinder
    public void init(WebDataBinder webDataBinder){
        webDataBinder.addValidators(memberValidator);
    }

    @GetMapping("/oauthMember")
    public String addOauthMember(@ModelAttribute Member member, HttpServletRequest request){
        /**
         *  문제1. 닉네임등록후 다시 닉네임 set 링크에 접근
         *      1. 컨트롤러 호출시 닉네임이 있을경우(닉네임이 존재하는것은 join완료) 예외 발생
         *      2. 닉네임을 등록할때 join이 발생하는데 같은 이메일로 등록을 시도하므로 예외 발생 [O]
         *      3. 인터셉터에 join여부 확인
         *
         *
         */

        /**
         *  문제2. 닉네임등록 안하고 다른 링크로 접속할떼  (닉네임을 set해야 join이 이루어진다)
         *      1. 필터에 닉네임 체크하기(join 체크) [O]
         *      2. 필터는 로그인만 보고 로그인시 닉네임을 체크하여 예외
         */

        //국소적으로 필요하므로 인터럽트 생성X
        HttpSession session = request.getSession();
        if(!temporaryMemberInSession(session)){
            return "redirect:/";
        }
        return "members/addOauthMemberForm";
    }



    @PostMapping("/oauthMember")
    public String saveOauthMemberV3(@Validated @ModelAttribute Member member, BindingResult bindingResult, HttpServletRequest request){
        //단순한 검증은 Bean Validation을 사용하자(@NotBlank, @NotNull....) -> 애로테이션 하나로 검증로직을 매우 편리하게 사용가능
        if(bindingResult.hasErrors()){
            return "members/addOauthMemberForm";
        }

        //닉네임 등록 완료후 join
        HttpSession session = request.getSession();
        if(!temporaryMemberInSession(session)){
            return "redirect:/";
        }
        Member findMember = (Member)session.getAttribute(SessionConst.TEMPORARY_MEMBER);
        Member joinMember = new Member(findMember.getUserName(),findMember.getEmail(),member.getNickName());

        session.removeAttribute(SessionConst.TEMPORARY_MEMBER);

        log.info("join Member before {}",joinMember);
        session.setAttribute(SessionConst.LOGIN_MEMBER, joinMember);
        memberService.join(joinMember);
        Member chk =(Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        log.info("join Member after {}",chk);
        return "redirect:/";
    }

    private static boolean temporaryMemberInSession(HttpSession session) {
        if(session == null || session.getAttribute(SessionConst.TEMPORARY_MEMBER)==null){
            return false;
        }
        return true;
    }


    /*@PostMapping("/oauthMember")
    public String saveOauthMemberV2(@ModelAttribute Member member, BindingResult bindingResult, HttpServletRequest request){

        //Validator 분리
        if(memberValidator.supports(Member.class)){
            memberValidator.validate(member, bindingResult);
        }


        if(bindingResult.hasErrors()){
            return "members/addOauthMemberForm";
        }

        //닉네임 등록 완료후 join
        HttpSession session = request.getSession();
        Member findMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        Member joinMember = new Member(findMember.getUserName(),findMember.getEmail(),member.getNickName());

        session.setAttribute(SessionConst.LOGIN_MEMBER, joinMember);

        memberService.join(joinMember);

        return "redirect:/";
    }

//    BindingResult는 ModelAttribute의 매핑실패에도 대응가능(오류정보를 담아서 컨트롤러 정상호출)
    public String saveOauthMemberV1(@ModelAttribute Member member, BindingResult bindingResult, HttpServletRequest request){
        if(memberService.DuplicateMemberNickName(member)){
//            bindingResult.addError(new FieldError("member","nickName","중복된 별명입니다."));
//            bindingResult.addError(new FieldError("member", "nickName", member.getNickName(), false, new String[]{"overlap.member.nickName"}, null, null));
            bindingResult.rejectValue("nickName","overlap");
        }

        //공백, Whitespace 판별
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "nickName","empty");

        if(member.getNickName().length()<2 || member.getNickName().length()>15){
            //bindingResult.addError(new FieldError("member","nickName","길이가 맞지 않습니다.")); rejectValue와 범위가 없음
            //bindingResult.addError(new FieldError("member","nickName",member.getNickName(),false,new String[]{"member.nickName.length"},new Object[]{2,15},null));
            //bindingResult.addError(new FieldError("member", "nickName", member.getNickName(), false, new String[]{"range.member.nickName"},new Object[]{2,15},null));

            *//**
             * re   jectValue: FieldError, reject: ObjectValue
             * bindingResult는 objectName을 알고있기때문에 rejectValue에서 줄 필요 없음.
             *//*

            bindingResult.rejectValue("nickName","required",new Object[]{2,15},null);

        }

        if(bindingResult.hasErrors()){
            return "members/addOauthMemberForm";
        }

        //닉네임 등록 완료후 join
        HttpSession session = request.getSession();
        Member findMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        Member joinMember = new Member(findMember.getUserName(),findMember.getEmail(),member.getNickName());

        session.setAttribute(SessionConst.LOGIN_MEMBER, joinMember);
        memberService.join(joinMember);
        return "redirect:/";
    }*/
}
