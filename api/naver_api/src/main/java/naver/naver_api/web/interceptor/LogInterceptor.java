package naver.naver_api.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * [스프링 인터셉터 흐름]
 * HTTP 요청 ->WAS-> 필터 -> 서블릿 -> 스프링 인터셉터 -> 컨트롤러
 * <p>
 * ( MVC 흐름 : HTTP 요청 시 Dispatcher Servlet이 핸들러 어뎁터를 찾은후 컨트롤러(핸들러)실행, 결과로 ModelAndview를 반환하면 Dispatcher Servlet이 받고 사용자에서 응답)
 * [호출 흐름] (true : 컨트롤러 호출)
 * preHandle : 핸들러 어댑터 호출 전에 호출
 * postHandle :    핸들러 어댑터 호출 후에 호출
 * afterCompletion :   뷰가 렌더링 된 이후에 호출
 */
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        request.setAttribute("loginId", uuid);

        /*
        //@RequestMapping : HandlerMethod
        if(handler instanceof HandlerMethod){
            HandlerMethod hm = (HandlerMethod) handler;
            //model등 꺼내서 사용가능
            hm.getBean();
            hm.getMethod();
        }*/
        log.info("REQUEST [{}] [{}] [{}]", uuid, requestURI, handler);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle [{}]", modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute("loginId");
        log.info("REQUEST [{}] [{}]", logId, requestURI);

        if(ex != null){
            log.error("afterCompletion error!", ex);
        }
    }
}
