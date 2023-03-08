package naver.naver_api.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String userName;
    private String email;

//    @OneToMany(mappedBy = "member")
//    private List<Board> boards = new ArrayList<>();

    protected Member(){

    }

    public Member(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
