package naver.naver_api.controller;

import lombok.extern.slf4j.Slf4j;
import naver.naver_api.domain.Member;
import naver.naver_api.dto.OauthMember;
import naver.naver_api.service.MemberService;
import naver.naver_api.session.SessionConst;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class LoginController {

    private final MemberService memberService;

    @Autowired
    private LoginController(MemberService memberService) {
        this.memberService = memberService;
    }


    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) throws IOException {

        //세션이 있으면 logout 주기
        HttpSession session = request.getSession(false);
        if(session==null){
            return "index";
        }

        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(loginMember==null){
            return "index";
        }

        model.addAttribute("member",loginMember);
        return "loginIndex";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if(session!=null){
            session.invalidate();
        }
        return "redirect:/";
    }

    @RequestMapping("/naver/login")
    public String naverLoginForm() {
        log.info("naverLoginForm");
        return "login";
    }

    @GetMapping("/naver/callback")
    public String naverLoginV1() {
        log.info("naverLoginV1");
        return "loginCallback";
    }

    //로그인과 회원가입 분리
    @PostMapping("/naver/callback")
    public String setToken(@ModelAttribute OauthMember oauthMember, HttpServletRequest request) throws ParseException {
        log.info("setToken");

        //가져온 토큰으로 한번더 인증
        String info = getInfo(oauthMember);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(info);
//        log.info("jsonObject={}", jsonObject);
        String memberCode = String.valueOf(jsonObject.get("resultcode"));

        //토큰이 유효하다면 로그인 세션 생성
        if (memberCode.equals("00")) {
            HttpSession session = request.getSession();

            //id가 set되어 있지 않는 엔티티
            Member loginMember = new Member(oauthMember.getUserName(), oauthMember.getEmail());

            //로그인 전 임시 세션 사용

            //email로 가입여부확인,
            if(!memberService.DuplicateMemberEmail(loginMember)){
                session.setAttribute(SessionConst.TEMPORARY_MEMBER, loginMember);
                return "redirect:/join/oauthMember";
            }

            //이미 가입된 엔티티반환(id가 set되어 있는 엔티티)    세션은 redis에 저장
            loginMember = memberService.findByEmail(oauthMember.getEmail());
            session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
            log.info("loginMember={}",loginMember);

        }
        return "redirect:/";
    }

    public String getInfo(OauthMember member) {
        String token = member.getToken();
        String header = "Bearer " + token; // Bearer 다음에 공백 추가

        String apiURL = "https://openapi.naver.com/v1/nid/me";

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);
        String responseBody = get(apiURL, requestHeaders);

//        System.out.println(responseBody);
        return responseBody;
    }

    private static String get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }


            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }


    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();


            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }


            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

}