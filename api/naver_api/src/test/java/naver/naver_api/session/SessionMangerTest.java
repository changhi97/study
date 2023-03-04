package naver.naver_api.session;

import naver.naver_api.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SessionMangerTest {

    @Autowired
    SessionManger sessionManger;

    @Test
    public void sessionTest(){
        MockHttpServletResponse response = new MockHttpServletResponse();
        Member member = new Member("water", "water@email.com");
        sessionManger.createSession(member, response);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        Object result = sessionManger.getSession(request);
        assertThat(result).isEqualTo(member);

        sessionManger.expire(request);
        Object result2 = sessionManger.getSession(request);
        assertThat(result2).isNull();
    }
}