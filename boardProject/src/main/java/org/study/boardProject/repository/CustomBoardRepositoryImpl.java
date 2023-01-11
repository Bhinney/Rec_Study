package org.study.boardProject.repository;

import static org.study.boardProject.entity.QBoard.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.study.boardProject.dto.BoardDto;
import org.study.boardProject.dto.QBoardDto_Response;

import com.querydsl.jpa.impl.JPAQueryFactory;

public class CustomBoardRepositoryImpl implements CustomBoardRepository{
	private final JPAQueryFactory queryFactory;

	public CustomBoardRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public Page<BoardDto.Response> findBoard(Pageable pageable) {
		List<BoardDto.Response> result = queryFactory
			.select(new QBoardDto_Response(
				board.boardId,
				board.nickName,
				board.title,
				board.content,
				board.createdAt,
				board.modifiedAt
			))
			.from(board)
			.orderBy(board.boardId.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = queryFactory.selectFrom(board).fetch().size();

		return PageableExecutionUtils.getPage(result, pageable, () -> total);
	}
}
