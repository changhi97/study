package naver.naver_api.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class BoardForm {
    private String title;
    private String content;
    private MultipartFile attachFile;
    private List<MultipartFile> imageFiles;
}
