package org.study.boardProject.controller;

/* 게시판 Controller */

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.study.boardProject.dto.BoardDto;
import org.study.boardProject.entity.Board;
import org.study.boardProject.global.response.MultiResponseDto;
import org.study.boardProject.mapper.BoardMapper;
import org.study.boardProject.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

	private final BoardService boardService;
	private final BoardMapper mapper;

	public BoardController(BoardService boardService, BoardMapper mapper) {
		this.boardService = boardService;
		this.mapper = mapper;
	}

	/* 게시판 등록 */
	@PostMapping
	public ResponseEntity<BoardDto.Response> post(@RequestBody BoardDto.Post requestBody) {
		Board board = boardService.create(mapper.boardPostDtoToBoard(requestBody));

		return new ResponseEntity<>(mapper.boardToBoardResponseDto(board), HttpStatus.CREATED);
	}

	/* 게시판 수정 */
	@PatchMapping("/{boardId}")
	public ResponseEntity<BoardDto.Response> patch(@PathVariable long boardId, @RequestBody BoardDto.Patch requestBody) {
		Board board = boardService.update(mapper.boardPatchDtoToBoard(requestBody), boardId);

		return ResponseEntity.ok(mapper.boardToBoardResponseDto(board));
	}

	/* 특정 게시판 조회 */
	@GetMapping("/{boardId}")
	public ResponseEntity<BoardDto.Response> getBoard(@PathVariable long boardId) {
		Board board = boardService.findBoard(boardId);

		return ResponseEntity.ok(mapper.boardToBoardResponseDto(board));
	}

	/* 전체 게시판 조회 */
	@GetMapping
	public ResponseEntity<MultiResponseDto<BoardDto.Response>> getBoardList(@RequestParam int page, @RequestParam int size) {
		// Page<Board> boardPage = boardService.findBoardList(page - 1, size);
		// List<Board> boardList = boardPage.getContent();
		// List<BoardDto.Response> responses = mapper.boardToBoardResponseDto(boardList);

		Page<BoardDto.Response> boardPage = boardService.getBoardPage(page - 1, size);
		List<BoardDto.Response> boardList = boardPage.getContent();

		return ResponseEntity.ok(new MultiResponseDto<>(boardList, boardPage));
	}

	/* 게시판 삭제 */
	@DeleteMapping("/{boardId}")
	public ResponseEntity<String> delete(@PathVariable long boardId) {
		String message = boardService.delete(boardId);

		return ResponseEntity.ok(message);
	}
}
