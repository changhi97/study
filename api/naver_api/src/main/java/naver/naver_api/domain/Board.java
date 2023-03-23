package naver.naver_api.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter @Getter
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
//    private String writer;

    //글
    @Lob
    private String content;

    //파일
    @Embedded
    private UploadFile attachFile;

//    @ElementCollection  //값타입도 Lazy 로딩
//    @CollectionTable(name = "ImageFile", joinColumns = @JoinColumn(name ="BOARD_ID"))
//    //데이터베이스는 컬랙션을 같은 테이블에 저장할수 없기에 별도의 테이블이 필요함(CollectionTable)
//    //BOARD_ID를 @Colnum으로 지정해줘야하나? 아니면 알아서 해당 클래스의 PK를 BOARD_ID에 넣어주는가
//    private List<UploadFile> imageFile;
    //값타입 컬랙션은 엔티티로 감싼후 1:N으로 풀자


    /**
     * 1. cascade = CascadeType.ALL : Board.imageFile에 인스턴스를 추가하고 Board를 persist하면 imageFile도 persist
     * 즉, cascade 밑에 있는 속성들도 자동으로 persist
     * [주의]
     * child가 하나의 parent에 대해서 관리되는것만 사용, 예를 들어 하나의 첨부파일이 여러개의 게시글에서 사용될때는 사용금지
     * 즉, child의 소유자가 1개일때 사용(완전히 종속적일때)
     * All : persist, remove...
     *
     * 2. orphanRemoval
     * 부모엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제 (수정은 아니다!) (기존의 삭제는 em.find로 엔티티를 찾은후 em.remove)
     * parent.getChildren.remove(0) // 자식 엔티티를 컬랙션에서 제거
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) //게시물이 업로드 파일을 참조한다는 의미(반대는 이상하다)
    @JoinColumn(name = "BOARD_ID")  //연관관계주인
    private List<UploadFileEntity> imageFile = new ArrayList<>();

    protected Board(){

    }

    public Board(Member member, String title, String content, UploadFile attachFile, List<UploadFileEntity> imageFile) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.attachFile = attachFile;
        this.imageFile = imageFile;
    }

    public void updateBoard(String title, String content, UploadFile attachFile, List<UploadFileEntity> imageFile){
        this.title = title;
        this.content = content;
        this.attachFile = attachFile;

        //연관관계에 있는 collection은 clear후 삽입(새로 생성된 컬랙션은 하이버네이트가 관리하지않기 때문)
        this.imageFile.clear();
        this.imageFile.addAll(imageFile);
    }

    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", member=" + member.getUserName() +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", attachFile=" + attachFile +
                ", imageFile=" + imageFile +
                '}';
    }
}
