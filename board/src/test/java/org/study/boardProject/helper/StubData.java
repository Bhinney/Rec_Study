package org.study.boardProject.helper;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.study.boardProject.dto.BoardDto;
import org.study.boardProject.entity.Board;

public class StubData {
	private static Map<HttpMethod, Object> stubRequestBody;
	static {
		stubRequestBody = new HashMap<>();
		stubRequestBody.put(HttpMethod.POST, new BoardDto.Post("김이름", "제목입니다.", "내용입니다."));
		stubRequestBody.put(HttpMethod.PATCH, new BoardDto.Patch("수정된 이름", "수정된 제목입니다.", "수정된 내용입니다."));
	}

	public static class MockBoard {
		public static Object getRequestBody(HttpMethod method) {
			return stubRequestBody.get(method);
		}

		public static BoardDto.Response getBoardResponse() {
			BoardDto.Response board = BoardDto.Response.builder()
				.boardId(1L)
				.nickName("김이름")
				.title("제목입니다.")
				.content("내용입니다.")
				.createdAt(LocalDate.now())
				.modifiedAt(LocalDate.now())
				.build();

			return board;
		}

		public static Page<Board> getBoardListStubData() {
			Board board1 = Board.builder()
					.nickName("김이름1")
					.title("첫 번째 제목입니다.")
					.content("첫 번째 내용입니다.")
					.build();

			Board board2 = Board.builder()
					.nickName("김이름2")
					.title("두 번째 제목입니다.")
					.content("두 번째 내용입니다.")
					.build();

			Board board3 = Board.builder()
					.nickName("김이름3")
					.title("세 번째 제목입니다.")
					.content("세 번째 내용입니다.")
					.build();

			return new PageImpl<>(List.of(board3, board2, board1),
				PageRequest.of(0, 10, Sort.by("memberId").descending()),3);
		}

		public static List<BoardDto.Response> getBoardListResponse() {
			BoardDto.Response board1 = BoardDto.Response.builder()
				.boardId(1L)
				.nickName("김이름1")
				.title("첫 번째 제목입니다.")
				.content("첫 번째 내용입니다.")
				.createdAt(LocalDate.now())
				.modifiedAt(LocalDate.now())
				.build();

			BoardDto.Response board2 = BoardDto.Response.builder()
				.boardId(2L)
				.nickName("김이름2")
				.title("두 번째 제목입니다.")
				.content("두 번째 내용입니다.")
				.createdAt(LocalDate.now())
				.modifiedAt(LocalDate.now())
				.build();

			BoardDto.Response board3 = BoardDto.Response.builder()
				.boardId(3L)
				.nickName("김이름3")
				.title("세 번째 제목입니다.")
				.content("세 번째 내용입니다.")
				.createdAt(LocalDate.now())
				.modifiedAt(LocalDate.now())
				.build();

			return List.of(
				board3, board2, board1
			);
		}

		public static Board getBoardStubData(long boardId) {
			Board board = Board.builder()
					.nickName("김이름")
					.title("제목입니다.")
					.content("내용입니다.")
					.build();

			return board;
		}
	}
}
