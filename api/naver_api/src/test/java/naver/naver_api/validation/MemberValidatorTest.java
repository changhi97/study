package naver.naver_api.validation;

import naver.naver_api.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MemberValidatorTest {

    @Test
    void beanValidation(){
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        Member member = new Member("test","test@naver.com");
        member.setNickName(" ");
        member.setNickName(null);

        Set<ConstraintViolation<Member>> validate = validator.validate(member);

        for (ConstraintViolation<Member> v : validate) {
            System.out.println("validate = " + v);
            System.out.println("message= "+v.getMessage());

        }

    }

}