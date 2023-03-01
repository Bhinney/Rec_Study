package org.study.boardProject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.study.boardProject.dto.BoardDto;

import com.querydsl.jpa.impl.JPAQueryFactory;

public interface CustomBoardRepository {
	Page<BoardDto.Response> findBoard(Pageable pageable);
}
