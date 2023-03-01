package org.study.boardProject.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.study.boardProject.dto.CommentDto;
import org.study.boardProject.entity.Board;
import org.study.boardProject.entity.Comment;
import org.study.boardProject.global.exception.custom.BusinessLogicException;
import org.study.boardProject.global.exception.custom.ExceptionCode;
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
	public Comment create(Comment comment, long boardId, long parentId){

		/* 존재하는 게시물인지 확인 */
		Board board = boardService.findBoard(boardId);

		if (parentId != 0) {
			comment.setParent(findComment(parentId));
		}

		/* 댓글 등록 */
		comment.setBoard(board);
		commentRepository.save(comment);

		return comment;
	}

	/* 댓글 수정 */
	public Comment update(Comment comment, long commentId, long boardId){
		/* 댓글이 존재하는지 확인 */
		Comment findComment = findVerifiedComment(commentId);

		/* 게시판 정보가 일치하는지 확인 */
		checkBoardId(boardId, findComment.getBoard().getBoardId());

		/* 댓글 수정 */
		Optional.ofNullable(comment.getContent()).ifPresent(findComment::changeContent);
		commentRepository.save(findComment);

		return findComment;
	}

	public Comment findComment(long commetId) {
		return findVerifiedComment(commetId);
	}

	/* 댓글 조회 */
	public Page<CommentDto.Response> getBoardCommentPage(long boardId, int page, int size) {

		return commentRepository.findBoardComments(boardId, PageRequest.of(page, size));
	}

	/* 댓글 삭제 */
	public String delete(long boardId, long commentId){
		/* 댓글 가져오기 */
		Comment comment = findVerifiedComment(commentId);

		/* 게시판 정보가 일치하는지 확인 */
		checkBoardId(boardId, comment.getBoard().getBoardId());

		commentRepository.delete(comment);

		return "댓글이 성공적으로 삭제되었습니다.";
	}

	/* 존재하는 댓글인지 확인 -> 뎃글 정보 리턴 */
	private Comment findVerifiedComment(long commentId) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));

		return comment;
	}

	/* boardId 와 comment.getBoard().getBoardId() 가 같은 지 확인*/
	private void checkBoardId(long boardId, long commentBoardId) {
		if (boardId != commentBoardId) {
			throw new BusinessLogicException(ExceptionCode.NOT_CORRECT_BOARDID);
		}
	}
}
