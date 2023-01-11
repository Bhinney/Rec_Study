package org.study.boardProject.repository;

import static org.study.boardProject.entity.QBoard.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.study.boardProject.dto.BoardDto;
import org.study.boardProject.entity.Board;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CustomBoardRepositoryImpl implements CustomBoardRepository{
	private final JPAQueryFactory queryFactory;

	public CustomBoardRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public Page<BoardDto.Response> findBoard(Pageable pageable) {
		List<BoardDto.Response> result = queryFactory
			.select(Projections.fields(BoardDto.Response.class,
				board.boardId,
				board.nickName,
				board.title,
				board.content,
				board.createdAt,
				board.modifiedAt))
			.from(board)
			.orderBy(board.boardId.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Board> total = queryFactory.selectFrom(board);

		return PageableExecutionUtils.getPage(result, pageable, () -> total.fetch().size());
	}
}
