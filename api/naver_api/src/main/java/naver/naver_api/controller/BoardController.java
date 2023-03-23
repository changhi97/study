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
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/board")
@Slf4j
public class BoardController {

    private final FileStore fileStore;
    private final BoardService boardService;

    @Autowired
    public BoardController(FileStore fileStore, BoardService boardService) {
        this.fileStore = fileStore;
        this.boardService = boardService;
    }

    @GetMapping("/write")
    public String writeBoard() {
        log.info("write Board");
        return "board/board-write";
    }

    @PostMapping("/write")
    public String saveBoard(@ModelAttribute BoardForm boardForm,
                            RedirectAttributes redirectAttributes,
                            HttpServletRequest request) throws IOException {

        UploadFile uploadFile = fileStore.storeFile(boardForm.getAttachFile());
        List<UploadFile> imageFiles = fileStore.storeFiles(boardForm.getImageFiles());

        //save Board 멤
        Member findMember = findSessionMember(request);

        List<UploadFileEntity> imageFileEntity = new ArrayList<>();
        for (UploadFile imageFile : imageFiles) {
            imageFileEntity.add(new UploadFileEntity(imageFile));
        }
        Board board = new Board(findMember, boardForm.getTitle(), boardForm.getContent(), uploadFile, imageFileEntity);
        boardService.save(board);

        redirectAttributes.addAttribute("boardId", board.getId());

        return "redirect:/board/{boardId}";
    }

    @GetMapping("/{boardId}")
    public String showBoard(@PathVariable("boardId") Long id, Model model, HttpServletRequest request) {
        Board findBoard = boardService.findOne(id);
        model.addAttribute("board", findBoard);

        //수정 버튼 활성화 유무
        if(isBoardOwner(findBoard, findSessionMember(request))){
            model.addAttribute("owner", true);
        }else{
            model.addAttribute("owner", false);
        }
        return "board/board-view";
    }

    @GetMapping("/boards")
    public String boardList(Model model) {
        log.info("board/boards");
        List<Board> findBoards = boardService.findAll();

        model.addAttribute("boards", findBoards);
        return "board/boards";
    }

    @GetMapping("/{boardId}/edit")
    public String editBoard(@PathVariable("boardId") Long id, Model model, HttpServletRequest request) {
        //에초에 권한이 없으면 수정버튼을 없애서 호출 금지
        Board findBoard = boardService.findOne(id);

        //작성자만 수정
        if(!isBoardOwner(findBoard, findSessionMember(request))){
            return "redirect:/board/boards";
        }

        model.addAttribute("board", findBoard);
        return "board/board-edit";
    }

    @PostMapping("/{boardId}/edit")
    public String updateBoard(@PathVariable("boardId") Long id, BoardForm boardForm, HttpServletRequest request) throws IOException {
        Board findBoard = boardService.findOne(id);

        //작성자만 수정
        if(!isBoardOwner(findBoard, findSessionMember(request))){
            return "redirect:/board/boards";
        }

        log.info("board update");

        UploadFile uploadFile = fileStore.storeFile(boardForm.getAttachFile());
        List<UploadFileEntity> imageFileEntity = convertUploadFileEntity(boardForm.getImageFiles());

        boardService.updateBoard(id, boardForm.getTitle(), boardForm.getContent(), uploadFile, imageFileEntity);
        return "redirect:/board/{boardId}";
    }


    @ResponseBody
    @GetMapping("/images/{fileName}")
    public Resource downloadImage(@PathVariable("fileName") String fileName) throws MalformedURLException {
        log.info("download Image");
        //보안에 취약하다.. 인증로직을 추가해보자
        return new UrlResource("file:"+fileStore.getFullPath(fileName));
    }

    @GetMapping("/attach/{boardId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable("boardId") Long id) throws MalformedURLException, UnsupportedEncodingException {
        Board findBoard = boardService.findOne(id);

        String storeFileName = findBoard.getAttachFile().getStoreFileName();
        String uploadFileName = findBoard.getAttachFile().getUploadFileName();
        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

        //스프링은 많은 인코딩을 가능하게 하는 UriUtils지원
        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8); //한글, 특수문자등 인코딩
        String contentDisposition = "attachment; filename=\""+encodedUploadFileName+"\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }


    private List<UploadFileEntity> convertUploadFileEntity(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> imageFiles = fileStore.storeFiles(multipartFiles);

        List<UploadFileEntity> imageFileEntity = new ArrayList<>();
        for (UploadFile imageFile : imageFiles) {
            imageFileEntity.add(new UploadFileEntity(imageFile));
        }
        return imageFileEntity;
    }

    private static boolean isBoardOwner(Board findBoard, Member member) {
        log.info("findBoard.getMember().getId() {}", findBoard.getMember().getId());
        log.info("member.getId() {}", member.getId());
        log.info("findBoard.getMember().getEmail() {}",findBoard.getMember().getEmail());
        log.info("member.getEmail() {}",member.getEmail());
        if(findBoard.getMember().getId() == member.getId() && findBoard.getMember().getEmail().equals(member.getEmail())){
            return true;
        }
        return false;
    }

    private static Member findSessionMember(HttpServletRequest request) {
        HttpSession session = request.getSession(); //세션으로 가져올지 세션으로 디비에서 멤버를 가져올지
        Member findMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        return findMember;
    }
}
