package org.study.boardProject.repository;

import static org.study.boardProject.entity.QComment.*;
import static org.study.boardProject.entity.QBoard.board;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.study.boardProject.dto.CommentDto;
import org.study.boardProject.entity.Comment;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CustomCommentRepositoryImpl implements CustomCommentRepository {
	private final JPAQueryFactory queryFactory;

	public CustomCommentRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public Page<CommentDto.Response> findComment(long boardId, Pageable pageable) {

		List<CommentDto.Response> result = queryFactory
			.select(Projections.fields(CommentDto.Response.class,
				comment.board.boardId.as("boardId"),
				comment.commentId,
				comment.nickName,
				comment.content,
				comment.createdAt,
				comment.modifiedAt))
			.from(comment)
			.where(comment.board.boardId.eq(boardId))
			.orderBy(comment.commentId.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		return new PageImpl<>(result);
	}
}
