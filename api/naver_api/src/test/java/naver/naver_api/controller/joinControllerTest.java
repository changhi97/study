package naver.naver_api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.FieldError;
import org.springframework.validation.MessageCodesResolver;

import static org.assertj.core.api.Assertions.assertThat;

class MessageCodesResolverTest {

    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test
    void ResolverObject(){
        String[] result = codesResolver.resolveMessageCodes("required", "member");
        for (String s : result) {
            System.out.println(s);
        }

        assertThat(result).containsExactly("required.member","required");
    }

    @Test
    void ResolverField(){
        String[] result = codesResolver.resolveMessageCodes("required", "member", "nickName", String.class);
        for (String s : result) {
            System.out.println(s);
        }
        /** rejectValue(), reject()는 내부에서 MessageCodesResolver를 사용하여 오류 메시지생성
         * MessageCodesResolver는 검증 오류 코드로 메시지를 생성한다.(구체적인것을 먼저 만들고 덜 구체적인것을 나중에만든다.)
         * [메시지 생성규칙 - 객체 오류]
         * 1. errorCode + objectName
         * 2. errorCode
         *
         * [메시지 생성규칙 - 필드 오류]
         *  1. errorCode + objectName + field
         *  2. errorCode + field
         *  3. errorCode + type
         *  4. errorCode
         * new FieldError("member", "nickName", null, false, messageCodes, null, null);
         */
    }

}