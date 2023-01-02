package org.study.boardProject.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.study.boardProject.entity.Board;
import org.study.boardProject.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;

	/* 게시판 등록 */
	public Board create(Board board) {
		boardRepository.save(board);

		return board;
	}

	/* 게시판 수정 */
	public Board update(Board board, long boardId) {
		Board findBoard = findVerifiedBoard(boardId);

		Optional.ofNullable(board.getNickName()).ifPresent(findBoard::setNickName);
		Optional.ofNullable(board.getTitle()).ifPresent(findBoard::setTitle);
		Optional.ofNullable(board.getContent()).ifPresent(findBoard::setContent);

		boardRepository.save(findBoard);

		return findBoard;
	}

	/* 특정 게시판 조회 */
	public Board findBoard(long boardId) {
		Board board = findVerifiedBoard(boardId);

		return board;
	}

	/* 전체 게시판 조회 */
	public Page<Board> findBoardList(int page, int size) {
		return boardRepository.findAll(PageRequest.of(page, size, Sort.by("boardId").descending()));
	}

	/* 게시판 삭제 */
	public String delete(long boardId) {
		Board board = findVerifiedBoard(boardId);

		boardRepository.delete(board);

		return "게시판이 성공적으로 삭제되었습니다.";
	}

	/* 존재하는 게시판인지 확인 -> 존재하면 게시판 리턴 */
	private Board findVerifiedBoard(long boardId) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new RuntimeException("게시판이 존재하지 않습니다."));

		return board;
	}
}
