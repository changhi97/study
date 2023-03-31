package naver.naver_api.dto;

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