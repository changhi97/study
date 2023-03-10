package naver.naver_api.controller;

import lombok.extern.slf4j.Slf4j;
import naver.naver_api.controller.dto.BoardForm;
import naver.naver_api.domain.Board;
import naver.naver_api.domain.Member;
import naver.naver_api.domain.UploadFile;
import naver.naver_api.domain.UploadFileEntity;
import naver.naver_api.file.FileStore;
import naver.naver_api.service.BoardService;
import naver.naver_api.session.SessionConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class BoardController {

    private final FileStore fileStore;
    private final BoardService boardService;

    @Autowired
    public BoardController(FileStore fileStore,BoardService boardService) {
        this.fileStore = fileStore;
        this.boardService= boardService;
    }
    @GetMapping("/write")
    public String writeBoard(){
        log.info("write Board");
        return "board/boardWrite";
    }

    @PostMapping("/write")
    @ResponseBody
    public String saveBoard(@ModelAttribute BoardForm boardForm, HttpServletRequest request) throws IOException {
        UploadFile uploadFile = fileStore.storeFile(boardForm.getAttachFile());
        List<UploadFile> imageFiles = fileStore.storeFiles(boardForm.getImageFiles());

        //save Board
        HttpSession session = request.getSession(); //세션으로 가져올지 세션으로 디비에서 멤버를 가져올지
        Member findMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);

        List<UploadFileEntity> imageFileEntity = new ArrayList<>();
        for (UploadFile imageFile : imageFiles) {
            imageFileEntity.add(new UploadFileEntity(imageFile));
        }
//        Board board = new Board(findMember, boardForm.getTitle(), boardForm.getContent(), uploadFile, imageFileEntity);
//        log.info("board {}", board.toString());
        boardService.save(new Board(findMember, boardForm.getTitle(), boardForm.getContent(), uploadFile, imageFileEntity));

        return "ok";
    }
}
