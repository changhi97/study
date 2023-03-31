package naver.naver_api.dto;

public class BoardDto {
    private Long id;
    private Long memberId;
    private String memberName;

    public BoardDto(Long id, Long memberId, String memberName) {
        this.id = id;
        this.memberId = memberId;
        this.memberName = memberName;
    }

    @Override
    public String toString() {
        return "BoardDto{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                '}';
    }
}
