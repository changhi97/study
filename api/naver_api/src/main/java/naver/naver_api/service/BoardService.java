package naver.naver_api.service;

import naver.naver_api.domain.Board;
import naver.naver_api.domain.UploadFile;
import naver.naver_api.domain.UploadFileEntity;
import naver.naver_api.repository.BoardJpaRepository;
import naver.naver_api.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BoardService {
//    private final BoardRepository boardRepository;
    private final BoardJpaRepository boardRepository;

    @Autowired
    public BoardService(BoardJpaRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Long save(Board board){
        return boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public Board findOne(Long id){
        return boardRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Board> findAll(){
        return boardRepository.findAll();
    }

    public void updateBoard(Long id, String title, String content, UploadFile attachFile, List<UploadFileEntity> imageFile) {
        Board findBoard = boardRepository.findById(id);
        findBoard.updateBoard(title, content, attachFile, imageFile);   //변경감지
    }


}
