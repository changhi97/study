package naver.naver_api.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class BoardForm {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private MultipartFile attachFile;

    private List<MultipartFile> imageFiles;

    public BoardForm(String title, String content) {
        this.title = title;
        this.content = content;
    }


    public BoardForm() {
    }

    @Override
    public String toString() {
        return "BoardForm{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", attachFile=" + attachFile.getOriginalFilename() +
                ", imageFiles=" + imageFiles.get(0).getOriginalFilename() +
                '}';
    }
}
