package naver.naver_api.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
public class BaseEntity {

    @Column(updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    @PrePersist
    public void prePersist(){
        createdDate = LocalDateTime.now();
        lastModifiedDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        lastModifiedDate = LocalDateTime.now();
    }

}
