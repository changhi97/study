package naver.naver_api.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import naver.naver_api.session.SessionConst;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        
//        log.info("Login Check Interceptor [{}]", requestURI);

        HttpSession session = request.getSession();
        if(session==null||session.getAttribute(SessionConst.LOGIN_MEMBER)==null){
            response.sendRedirect("/naver/login?requestURI="+requestURI);
            return false;
        }
        return true;
    }
}
