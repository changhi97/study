package naver.naver_api.web;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

/**
 *  [필터 흐름]
 *  HTTP 요청 ->WAS-> 필터 -> 서블릿 -> 컨트롤러
 *
 *  [필터 제한]
 *  HTTP 요청 -> WAS -> 필터(적절하지 않은 요청이라 판단, 서블릿 호출X) //비 로그인 사용자
 *
 *  [필터 체인]
 *  HTTP 요청 ->WAS-> 필터1-> 필터2-> 필터3-> 서블릿 -> 컨트롤러
 */
@Slf4j
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("LogFilter init ");

    }

    @Override
    public void destroy() {
        log.info("LogFilter destroy ");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("LogFilter doFilter ");

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String requestURI = httpRequest.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        try{
            log.info("REQUEST=[{}] [{}]", uuid, requestURI);
            chain.doFilter(request,response);   //다음 필터 실행, 없으면 서블릿 실행
        }catch (Exception e){
        }finally {
            log.info("RESPONSE=[{}] [{}]", uuid, requestURI);
        }

    }
}
