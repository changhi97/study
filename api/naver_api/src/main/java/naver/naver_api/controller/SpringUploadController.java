package naver.naver_api.controller;

import lombok.extern.slf4j.Slf4j;
import naver.naver_api.dto.BoardForm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
public class SpringUploadController {

    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/upload")
    public String newFile() {
        return "board/upload-form";
    }

    @PostMapping("/upload")
    public String saveFile(@RequestParam String itemName,
                           @RequestParam MultipartFile file, HttpServletRequest request) throws IOException {
        if (!file.isEmpty()) {
            String fullPath = fileDir + file.getOriginalFilename();
            log.info("파일저장 {}", fullPath);
            file.transferTo(new File(fullPath));
        }
        return "board/upload-form";
    }

    @GetMapping("/uploadTest")
    public String writeFileTest() {

        return "board/fileCustom";
    }

    @PostMapping("/uploadTest")
    public String saveFileTest(@RequestParam("myFiles") List<MultipartFile> myFiles) {
        log.info("========================uploadTest========================");
        for (MultipartFile myFile : myFiles) {
            log.info("name {}", myFile.getOriginalFilename());
        }
        BoardForm board = new BoardForm();
        return "redirect:/";
    }
}