package com.ignis.to_do.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ignis.to_do.dto.BoardDTO;
import com.ignis.to_do.dto.TaskListDTO;
import com.ignis.to_do.exception.board_exception.BoardAlreadyExistsException;
import com.ignis.to_do.exception.board_exception.BoardNotFoundException;
import com.ignis.to_do.model.Board;
import com.ignis.to_do.model.User;
import com.ignis.to_do.repository.BoardRepository;

import jakarta.transaction.Transactional;

@Service    
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserService userService; 
    
    private static final String BOARD_NOT_FOUND = "Board com ID %s nao encontrado";
    private static final String BOARD_ALREADY_EXISTS = "Board com nome %s já existe";

    public BoardService(BoardRepository boardRepository, UserService userService) {
        this.boardRepository = boardRepository;
        this.userService = userService;
    }

    public BoardDTO createBoard(BoardDTO boardDTO) {

        Optional<Board> existingBoard = boardRepository.findByTitleAndOwnerId(boardDTO.getTitle(), boardDTO.getOwnerId());
        if (existingBoard.isPresent()) {
            throw new BoardAlreadyExistsException(BOARD_ALREADY_EXISTS.formatted(boardDTO.getTitle()));
        }

        User user = userService.getUser(boardDTO.getOwnerId());   
        Board board = new Board(boardDTO.getTitle(), user);
        boardRepository.save(board);
        return new BoardDTO(board.getId(), board.getTitle(),
            board.isFavorite(), board.getOwner().getId());
    }

    public BoardDTO getBoardById(Long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(()
         -> new BoardNotFoundException(BOARD_NOT_FOUND.formatted(boardId)));
        return new BoardDTO(board.getId(), board.getTitle(), 
            board.isFavorite(), board.getOwner().getId());
    }

    public void verifyIfBoardExists(Long boardId) {
        boardRepository.findById(boardId).orElseThrow(()
         -> new BoardNotFoundException(BOARD_NOT_FOUND.formatted(boardId)));
    }

    public Iterable<BoardDTO> getAllBoards() {
        
        return boardRepository.findAll().stream().map(board -> new BoardDTO(
            board.getId(), board.getTitle(), board.isFavorite(), board.getOwner().getId())).toList();
    }

    public Iterable<BoardDTO> getMyBoardsByOwnerId(Long ownerId) {

        User user = userService.getUser(ownerId);  
        return user.getBoards().stream().map(board -> new BoardDTO(board.getId(),
             board.getTitle(), board.isFavorite(), board.getOwner().getId())).toList();
    }

    public Iterable<TaskListDTO> myTasksListsByBoardId(Long boardId){

        Board board = boardRepository.findById(boardId).orElseThrow(()
         -> new BoardNotFoundException(BOARD_NOT_FOUND.formatted(boardId)));
        return board.getTaskLists().stream().map(taskList -> new TaskListDTO(
            taskList.getId(), taskList.getTitle(), taskList.getBoard().getId())).toList();
    }

    public boolean isFavorite(Long boardId) {
        return boardRepository.findById(boardId)
                .map(Board::isFavorite)
                .orElseThrow(() -> new BoardNotFoundException(BOARD_NOT_FOUND.formatted(boardId)));
    }

    @Transactional
    public void toggleFavorite(Long boardId) {
        
        verifyIfBoardExists(boardId);
        Board board = boardRepository.findById(boardId).orElseThrow(()
        -> new BoardNotFoundException(BOARD_NOT_FOUND.formatted(boardId)));

        boardRepository.updateFavorite(boardId, !board.isFavorite());
    }
    public void deleteBoardById(Long boardId) {
        verifyIfBoardExists(boardId);
        boardRepository.deleteById(boardId);
    }

    @Transactional
    public BoardDTO updateBoardTitle(BoardDTO boardDTO) {

        verifyIfBoardExists(boardDTO.getId());
        boardRepository.updateTitle(boardDTO.getId(), boardDTO.getTitle());
        Long boardId = boardDTO.getId();
        Board board = boardRepository.findById(boardId).orElseThrow(()
        -> new BoardNotFoundException(BOARD_NOT_FOUND.formatted(boardId)));
        return new BoardDTO(boardId, boardDTO.getTitle(), board.isFavorite(), board.getOwner().getId());
    }

    
    public Board getBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(()
        -> new BoardNotFoundException(BOARD_NOT_FOUND.formatted(boardId)));
    }
}
