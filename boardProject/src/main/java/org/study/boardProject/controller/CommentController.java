package org.study.boardProject.controller;

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
import org.study.boardProject.dto.CommentDto;
import org.study.boardProject.entity.Comment;
import org.study.boardProject.mapper.CommentMapper;
import org.study.boardProject.service.CommentService;

@RestController
@RequestMapping("/api/boards/{boardId}/comments")
public class CommentController {

	private final CommentService commentService;
	private final CommentMapper mapper;

	public CommentController(CommentService commentService, CommentMapper mapper) {
		this.commentService = commentService;
		this.mapper = mapper;
	}

	/* 댓글 등록 */
	@PostMapping
	public ResponseEntity<CommentDto.Response> postComment(@PathVariable long boardId, @RequestBody CommentDto.Post requestBody){
		Comment comment = commentService.create(mapper.postCommentDtoToComment(requestBody), boardId);

		return new ResponseEntity<>(mapper.commentToCommentResponseDto(comment, boardId), HttpStatus.CREATED);
	}

	/* 댓글 수정 */
	@PatchMapping("/{commentId}")
	public ResponseEntity<CommentDto.Response> patchComment(@PathVariable long boardId, @PathVariable long commentId,
		@RequestBody CommentDto.Patch requestBody){
		Comment comment = commentService.update(mapper.patchCommentDtoToComment(requestBody), commentId, boardId);

		return ResponseEntity.ok(mapper.commentToCommentResponseDto(comment, boardId));
	}

	/* 댓글 조회 */
	@GetMapping
	public ResponseEntity<List<CommentDto.Response>> getCommentList(@PathVariable long boardId, @RequestParam int page, @RequestParam int size){
		Page<Comment> commentPage = commentService.getCommentList(boardId, page - 1, size);
		List<Comment> commentList = commentPage.getContent();

		return ResponseEntity.ok(mapper.commentToCommentResponseDto(commentList, boardId));
	}

	/* 댓글 삭제 */
	@DeleteMapping("/{commentId}")
	public ResponseEntity deleteComment(@PathVariable long boardId, @PathVariable long commentId){
		String message = commentService.delete(boardId, commentId);

		return ResponseEntity.ok(message);
	}

}
