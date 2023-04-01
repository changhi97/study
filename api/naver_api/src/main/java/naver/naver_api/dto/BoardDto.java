package naver.naver_api.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardDto {
    private Long id;

    private String title;
    private String userName;

    private LocalDateTime createdDate;

    public BoardDto(Long id, String title, String userName, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.userName = userName;
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "BoardDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", userName='" + userName + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
