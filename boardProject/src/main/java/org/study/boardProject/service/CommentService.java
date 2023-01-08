package org.study.boardProject.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.study.boardProject.entity.Board;
import org.study.boardProject.entity.Comment;
import org.study.boardProject.repository.CommentRepository;

@Service
public class CommentService {

	private final CommentRepository commentRepository;
	private final BoardService boardService;

	public CommentService(CommentRepository commentRepository, BoardService boardService) {
		this.commentRepository = commentRepository;
		this.boardService = boardService;
	}

	/* 댓글 등록 */
	public Comment create(Comment comment){

		/* 존재하는 게시물인지 확인 */
		Board board = boardService.findBoard(comment.getBoard().getBoardId());

		/* 댓글 등록 */
		commentRepository.save(comment);

		return comment;
	}

	/* 댓글 수정 */
	public Comment update(Comment comment){

		/* 댓글이 존재하는지 확인 */
		Comment findComment = findVerifiedComment(comment.getCommentId());

		/* 댓글 수정 */
		Optional.ofNullable(comment.getContent()).ifPresent(content -> findComment.changeContent(content));
		commentRepository.save(findComment);

		return findComment;
	}

	/* 댓글 조회 */
	public Comment getCommentList(long commentId){

		return findVerifiedComment(commentId);
	}

	/* 댓글 삭제 */
	public String delete(long commentId){
		Comment comment = findVerifiedComment(commentId);

		commentRepository.delete(comment);

		return "댓글이 성공적으로 삭제되었습니다.";
	}

	/* 존재하는 댓글인지 확인 */
	private Comment findVerifiedComment(long commentId) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));

		return comment;
	}
}
