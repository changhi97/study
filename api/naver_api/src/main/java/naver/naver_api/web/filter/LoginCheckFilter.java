package naver.naver_api.web.filter;

import lombok.extern.slf4j.Slf4j;
import naver.naver_api.session.SessionConst;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class LoginCheckFilter implements Filter {

//    String[] whiteList= {"/", "/naver/login", "/naver/callback", "/userInfo", "/loginJS/*"};
    String[] whiteList= {"/", "/naver/login", "/naver/callback", "/loginJS/*"};
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();

        try{
            log.info("인증 체트 필터 시작 = {}",requestURI);

            if(isLoginCheckPath(requestURI)){
                log.info("로그인 인증 체크 로직 실행 {}",requestURI);
                HttpSession session = httpRequest.getSession(false);
                if(session==null || session.getAttribute(SessionConst.LOGIN_MEMBER) ==null){
                    log.info("미인증 사용자 요청");
                    //로그인후 현재 페이지로 보내기위해 requestURI정보 까지 전송
                    //컨트롤러에서 @RequestParam으로 받으면 되지만 네아로의 콜백 url때문에 사용 불가능
                    httpResponse.sendRedirect("/naver/login?redirectURL="+requestURI);
                    return;
                }
            }
            chain.doFilter(request,response);

        }catch (Exception e){
            throw e;
        }finally {
            log.info("로그인 인증 체크 필터 종료");
        }
    }

    //whiteList일 경우 인증 체크 X
    private boolean isLoginCheckPath(String requestURI){
        return !PatternMatchUtils.simpleMatch(whiteList,requestURI);

    }
}
