package naver.naver_api.controller.dto;

import lombok.Data;

@Data
public class OauthMember {
    private String userName;
    private String token;
    private String email;

    @Override
    public String toString() {
        return "OauthMember{" +
                "nickName='" + userName + '\'' +
                ", token='" + token + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}