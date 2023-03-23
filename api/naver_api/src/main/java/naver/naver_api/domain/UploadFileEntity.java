package naver.naver_api.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class UploadFileEntity {
    @Id
    @GeneratedValue
    private Long id;

    private UploadFile uploadFile;

    protected UploadFileEntity(){

    }

    public UploadFileEntity(UploadFile uploadFile) {
        this.uploadFile = uploadFile;
    }
}
