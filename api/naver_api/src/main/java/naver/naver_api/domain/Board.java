package naver.naver_api.domain;

import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
public class Board extends BaseEntity{

    @Id
    @GeneratedValue
    private Long id;

    //작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    //제목
    private String title;

    //작성자(로그인 정보 삽입)
    private String writer;

    //글
    private String content;

    //파일
    @Embedded
    private UploadFile attachFile;

    @ElementCollection
    @CollectionTable(name = "ImageFile", joinColumns = @JoinColumn(name ="BOARD_ID"))
    //데이터베이스는 컬랙션을 같은 테이블에 저장할수 없기에 별도의 테이블이 필요함(CollectionTable)
    //BOARD_ID를 @Colnum으로 지정해줘야하나? 아니면 알아서 해당 클래스의 PK를 BOARD_ID에 넣어주는가
    private List<UploadFile> imageFile;

    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", member=" + member.getUserName() +
                ", title='" + title + '\'' +
                ", writer='" + writer + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
