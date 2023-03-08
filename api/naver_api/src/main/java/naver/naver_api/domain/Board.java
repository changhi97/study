package naver.naver_api.domain;

import lombok.Setter;

import javax.persistence.*;
import java.sql.Clob;

@Entity
@Setter
public class Board extends BaseEntity{

    @Id
    @GeneratedValue
    private  Long id;

    //작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    //제목
    private String title;

    private String writer;

    //글
    private String content;

    //파일


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
