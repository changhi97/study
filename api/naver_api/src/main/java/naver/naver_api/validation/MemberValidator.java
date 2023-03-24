package naver.naver_api.validation;

import naver.naver_api.domain.Member;
import naver.naver_api.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

//스프링은 검증을 체계적으로 제공하기 위한 인터페이스 제공
@Component  //굳이 스프링이 제공하는것이 아니고 싱글톤이 아닌 상태로 검증할수도있다.
public class MemberValidator implements Validator {

    private final MemberService memberService;

    @Autowired
    public MemberValidator(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        //Member == calzz인지 판별(isAssignableFrom은 Member의 자식 클래스까지 커버가능)
        return Member.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        //errors의 자식이 bindingResult

        Member member = (Member) target;

        if (memberService.DuplicateMemberNickName(member)) {
            errors.rejectValue("nickName", "overlap");
        }

        //공백, Whitespace 판별
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nickName", "empty");

        if (member.getNickName().length() < 2 || member.getNickName().length() > 15) {
            errors.rejectValue("nickName", "required", new Object[]{2, 15}, null);
        }

    }
}
