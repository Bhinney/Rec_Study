package org.study.boardProject.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.study.boardProject.dto.BoardDto;
import org.study.boardProject.entity.Board;

@Mapper(componentModel = "spring")
public interface BoardMapper {
	Board boardPostDtoToBoard (BoardDto.Post post);
	Board boardPatchDtoToBoard (BoardDto.Patch patch);
	BoardDto.Response boardToBoardResponseDto (Board board);
	List<BoardDto.Response> boardToBoardResponseDto (List<Board> boards);
}
