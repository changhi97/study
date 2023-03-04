package naver.naver_api.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManger {
    public static final String SESSION_COOKIE_NANE = "mySessionId";
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    /**
     * 세션 생성
     * * sessionId 생성(임의의 추정 불가능한 랜덤값)
     * * 세션 저장소에 sessionId와 보관할 값 저장
     * * sessionId로 응답 쿠키를 생성해서 클라이언트에게 전달
     */
    public void createSession(Object value, HttpServletResponse response) {
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        Cookie cookie = new Cookie(SESSION_COOKIE_NANE, sessionId);
        response.addCookie(cookie);
    }

    /**
     * 세션 조회
     */
    public Object getSession(HttpServletRequest request){
        Cookie findCookie = findCookie(request, SESSION_COOKIE_NANE);
        return sessionStore.get(findCookie.getValue());
    }

    private static Cookie findCookie(HttpServletRequest request, String cookieName) {
        if(request.getCookies()==null){
            return null;
        }
        /*Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);*/
        for (Cookie cookie : request.getCookies()) {
            if(cookie.getName().equals(cookieName)){
                return cookie;
            }
        }
        return null;
    }

    /**
     * 세션 만료
     */
    public void expire(HttpServletRequest request){
        Cookie findCookie = findCookie(request, SESSION_COOKIE_NANE);
        sessionStore.remove(findCookie.getValue());
    }
}
