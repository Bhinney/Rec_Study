package org.study.boardProject.boardTest;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.study.boardProject.util.ApiDocumentUtils.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.study.boardProject.controller.BoardController;
import org.study.boardProject.dto.BoardDto;
import org.study.boardProject.entity.Board;
import org.study.boardProject.helper.BoardControllerHelper;
import org.study.boardProject.helper.StubData;
import org.study.boardProject.mapper.BoardMapper;
import org.study.boardProject.service.BoardService;

import com.google.gson.Gson;

@WebMvcTest(BoardController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class BoardControllerTest implements BoardControllerHelper {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BoardService boardService;

	@MockBean
	private BoardMapper mapper;

	@Autowired
	private Gson gson;

	@Test
	@DisplayName("게시글 등록 테스트")
	public void postBoardTest() throws Exception {

		/* given */
		BoardDto.Post post = new BoardDto.Post("김이름", "제목입니다.", "내용입니다.");
		String content = gson.toJson(post);

		BoardDto.Response response =
			new BoardDto.Response(1L, "김이름", "제목입니다.", "내용입니다.", LocalDate.now(), LocalDate.now());

		given(mapper.boardPostDtoToBoard(Mockito.any(BoardDto.Post.class))).willReturn(new Board());
		given(boardService.create(Mockito.any(Board.class))).willReturn(new Board());
		given(mapper.boardToBoardResponseDto(Mockito.any(Board.class))).willReturn(response);

		/* when */
		ResultActions actions
			= mockMvc.perform(
			post("/api/boards")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(content)
		);

		/* then */
		actions
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.nickName").value(post.getNickName()))
			.andExpect(jsonPath("$.title").value(post.getTitle()))
			.andExpect(jsonPath("$.content").value(post.getContent()))
			.andDo(document("post-board",
				getRequestPreProcessor(),
				getResponsePreProcessor(),
				requestFields(
					List.of(
						fieldWithPath("nickName").type(JsonFieldType.STRING).description("작성자"),
						fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
						fieldWithPath("content").type(JsonFieldType.STRING).description("내용")
					)
				),
				responseFields(
					List.of(
						fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시판 식별자"),
						fieldWithPath("nickName").type(JsonFieldType.STRING).description("작성자"),
						fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
						fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
						fieldWithPath("createdAt").type(JsonFieldType.STRING).description("최초 작성 일자"),
						fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("수정 일자")
					)
				)
			));
	}

	@Test
	@DisplayName("게시글 수정 테스트")
	public void patchBoardTest() throws Exception {

		/* given */
		long boardId = 1L;
		BoardDto.Patch patch = new BoardDto.Patch("수정된 이름", "수정된 제목입니다.", "수정된 내용입니다.");
		String content = gson.toJson(patch);

		BoardDto.Response response =
			new BoardDto.Response(1L,"수정된 이름", "수정된 제목입니다.", "수정된 내용입니다.", LocalDate.now(), LocalDate.now());

		given(mapper.boardPatchDtoToBoard(Mockito.any(BoardDto.Patch.class))).willReturn(new Board());
		given(boardService.update(Mockito.any(Board.class), Mockito.anyLong())).willReturn(new Board());
		given(mapper.boardToBoardResponseDto(Mockito.any(Board.class))).willReturn(response);

		/* when */
		ResultActions actions
			= mockMvc.perform(
			patch("/api/boards/{boardId}", boardId)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(content)
		);

		/* then */
		actions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nickName").value(patch.getNickName()))
			.andExpect(jsonPath("$.title").value(patch.getTitle()))
			.andExpect(jsonPath("$.content").value(patch.getContent()))
			.andDo(document("patch-board",
				getRequestPreProcessor(),
				getResponsePreProcessor(),
				requestFields(
					List.of(
						fieldWithPath("nickName").type(JsonFieldType.STRING).description("작성자"),
						fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
						fieldWithPath("content").type(JsonFieldType.STRING).description("내용")
					)
				),
				responseFields(
					List.of(
						fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시판 식별자"),
						fieldWithPath("nickName").type(JsonFieldType.STRING).description("작성자"),
						fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
						fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
						fieldWithPath("createdAt").type(JsonFieldType.STRING).description("최초 작성 일자"),
						fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("수정 일자")
					)
				)
			));
	}

	@Test
	@DisplayName("특정 게시물 조회 테스트")
	public void getBoardTest() throws Exception {
		/* given */
		long boardId = 1L;
		BoardDto.Response response = StubData.MockBoard.getBoardResponse();

		String url = getBoardResourceURI();

		given(boardService.findBoard(Mockito.anyLong())).willReturn(new Board());
		given(mapper.boardToBoardResponseDto(Mockito.any(Board.class))).willReturn(response);

		/* when */
		ResultActions actions = mockMvc.perform(getRequestBuilder(url, boardId));

		/* then */
		actions.andExpect(status().isOk())
			.andDo(document("get-board",
				getResponsePreProcessor(),
				pathParameters(parameterWithName("boardId").description("게시판 식별자")),
				responseFields(
					List.of(
						fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시판 식별자"),
						fieldWithPath("nickName").type(JsonFieldType.STRING).description("작성자"),
						fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
						fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
						fieldWithPath("createdAt").type(JsonFieldType.STRING).description("최초 작성 일자"),
						fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("수정 일자")
					)
				)
			));
	}

	@Test
	@DisplayName("게시물 리스트 조회")
	public void getBoardListTest() throws Exception {
		Page<Board> boardList = StubData.MockBoard.getBoardListStubData();
		List<BoardDto.Response> responseList = StubData.MockBoard.getBoardListResponse();

		String url = getBoardURI();

		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
		queryParams.add("page", "1");
		queryParams.add("size", "3");

		given(boardService.findBoardList(Mockito.anyInt(), Mockito.anyInt())).willReturn(boardList);
		given(mapper.boardToBoardResponseDto(Mockito.anyList())).willReturn(responseList);

		/* when */
		ResultActions actions = mockMvc.perform(getRequestBuilder(url,queryParams));

		/* then */
		actions.andExpect(status().isOk())
			.andDo(document("get-boardList",
				getRequestPreProcessor(),
				getResponsePreProcessor(),
				requestParameters(
					List.of(
						parameterWithName("page").description("페이지 번호"),
						parameterWithName("size").description("페이지 사이즈")
					)
				),
				responseFields(
					List.of(
						fieldWithPath("[].boardId").type(JsonFieldType.NUMBER).description("게시판 식별자"),
						fieldWithPath("[].nickName").type(JsonFieldType.STRING).description("작성자"),
						fieldWithPath("[].title").type(JsonFieldType.STRING).description("제목"),
						fieldWithPath("[].content").type(JsonFieldType.STRING).description("내용"),
						fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("최초 작성 일자"),
						fieldWithPath("[].modifiedAt").type(JsonFieldType.STRING).description("수정 일자")
					)
				)
			));
	}

	@Test
	@DisplayName("게시물 삭제 테스트")
	public void deleteBoardTest() throws Exception {
		long boardId = 1L;
		String message = "게시판이 성공적으로 삭제되었습니다.";
		given(boardService.delete(Mockito.anyLong())).willReturn(message);

		String url = getBoardResourceURI();

		/* when */
		ResultActions actions = mockMvc.perform(deleteRequestBuilder(url, boardId));

		actions.andExpect(status().isOk())
			.andDo(document("delete-board",
					pathParameters(parameterWithName("boardId").description("게시판 식별자"))
				)
			);
	}
}
