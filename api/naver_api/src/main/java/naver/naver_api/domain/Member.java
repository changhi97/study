package naver.naver_api.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Data
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String userName;

    private String email;

    private String nickName;

//    @OneToMany(mappedBy = "member")
//    private List<Board> boards = new ArrayList<>();

    protected Member(){

    }

    public Member(String userName, String email) {
        this.userName = userName;
        this.nickName = userName;
        this.email = email;
    }

    public Member(String userName, String email,String nickName) {
        this.userName = userName;
        this.nickName = nickName;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.equals(userName, member.userName) && Objects.equals(email, member.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, email);
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
