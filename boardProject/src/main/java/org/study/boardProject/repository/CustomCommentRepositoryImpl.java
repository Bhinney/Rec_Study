package org.study.boardProject.repository;

import static org.study.boardProject.entity.QComment.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.study.boardProject.dto.CommentDto;
import org.study.boardProject.dto.QCommentDto_Response;

import com.querydsl.jpa.impl.JPAQueryFactory;

public class CustomCommentRepositoryImpl implements CustomCommentRepository {
	private final JPAQueryFactory queryFactory;

	public CustomCommentRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public Page<CommentDto.Response> findComment(long boardId, Pageable pageable) {

		List<CommentDto.Response> result = queryFactory
			.select(new QCommentDto_Response(
				comment.board.boardId,
				comment.commentId,
				comment.nickName,
				comment.content,
				comment.createdAt,
				comment.modifiedAt
			))
			.from(comment)
			.where(comment.board.boardId.eq(boardId))
			.orderBy(comment.commentId.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = queryFactory.selectFrom(comment)
			.where(comment.board.boardId.eq(boardId))
			.fetch().size();

		return PageableExecutionUtils.getPage(result, pageable, () -> total);
	}
}
