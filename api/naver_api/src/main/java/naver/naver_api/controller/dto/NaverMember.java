package naver.naver_api.controller.dto;

import lombok.Data;

@Data
public class NaverMember{
    private String nickName;
    private String token;
    private String email;

    @Override
    public String toString() {
        return "NaverMember{" +
                "nickName='" + nickName + '\'' +
                ", token='" + token + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}