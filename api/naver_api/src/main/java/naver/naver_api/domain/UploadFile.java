package naver.naver_api.domain;

import lombok.Data;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.util.Objects;

/**
 * 값타입
 *
 * [비교]
 * 동일성 : 참조값 비교(==)
 * 동등성 : 인스턴스 값을 비교(equals)
 *
 * [주의]
 * 값타입을 수정할때는 set을 사용하지마라 인스턴스 참조때문에 side effect가 발생할수 있음.
 * 생성자를 통해 값을 통째로 변경할것
 * 즉, 삭제후 다시 넣을것!(삭제시 equals를 통해 삭제한다. 반드시 equals, hashcode가 제대로 구현되어 있어야한다.)
 *
 * [값 타입 컬랙션 제약사항]
 * 값 타입은 엔티티와 다르게 식별자 개녕이 없다.
 * 값타입에 변경사항이 발생하면 주인 엔티티와 연관된 모든 데이터를 삭제하고 컬랙션에 있는 값을 다시 저장한다. -> 쓰면 안된다.
 *
 * [값 타입 컬랙션  제안]
 * 값타입 컬랙션을 매핑하는 테이블은 모든 칼럼을 묶어서 기본키를구성해야하한다.
 * 값타입 컬랙션 -> 엔티티의 연관관계로 풀것(N:1)
 *
 * [값 타입 컬랙션 사용]
 * 간단하거나, 추적이 필요하지 않는 값을 다룰때 사용
 * 즉, 실별자가 필요하고 지속해서 값을 추적, 변경해야할경우는 값 타입이 아니라 엔티티이다.
 */

@Getter
@Embeddable
public class UploadFile {
    private String uploadFileName;
    private String storeFileName;

    @Lob
    private String fileText;

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }

    protected UploadFile(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UploadFile that = (UploadFile) o;
        return Objects.equals(uploadFileName, that.uploadFileName) && Objects.equals(storeFileName, that.storeFileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uploadFileName, storeFileName);
    }
}
