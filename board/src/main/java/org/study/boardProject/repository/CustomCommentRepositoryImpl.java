package org.study.boardProject.repository;

import static org.study.boardProject.entity.QComment.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.study.boardProject.dto.CommentDto;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CustomCommentRepositoryImpl implements CustomCommentRepository {
	private final JPAQueryFactory queryFactory;

	public CustomCommentRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public Page<CommentDto.Response> findBoardComments(long boardId, Pageable pageable) {

		/* 게시판 댓글 정보 가져오기 */
		List<CommentDto.Response> result = queryFactory
			.select(Projections.fields(CommentDto.Response.class,
				comment.board.boardId,
				comment.commentId,
				comment.parent.commentId,
				comment.nickName,
				comment.content,
				comment.ref,
				comment.createdAt,
				comment.modifiedAt
			))
			.from(comment)
			.where(comment.board.boardId.eq(boardId).and(comment.parent.isNull()))
			.orderBy(comment.commentId.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		return PageableExecutionUtils.getPage(result, pageable, result::size);
	}
}
