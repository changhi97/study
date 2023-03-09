package naver.naver_api.domain;

import lombok.Data;

import javax.persistence.Embeddable;

/**
 * 값타입
 *
 * [비교]
 * 동일성 : 참조값 비교(==)
 * 동등성 : 인스턴스 값을 비교(equals)
 */

@Data
@Embeddable
public class UploadFile {
    private String uploadFileName;
    private String storeFileName;
}
