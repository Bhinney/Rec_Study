package org.study.boardProject.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class BoardDto {

	@Getter
	@Builder
	@AllArgsConstructor /* 테스트를 위해 추가 */
	public static class Post {
		private String nickName;
		private String title;
		private String content;
	}

	@Getter
	@Builder
	@AllArgsConstructor /* 테스트를 위해 추가 */
	public static class Patch {
		private String nickName;
		private String title;
		private String content;
	}

	@Getter
	public static class Response {
		private long boardId;
		private String nickName;
		private String title;
		private String content;
		private LocalDate createdAt;
		private LocalDate modifiedAt;

		public Response() {}

		@Builder
		public Response(long boardId, String nickName, String title, String content, LocalDate createdAt,
			LocalDate modifiedAt) {
			this.boardId = boardId;
			this.nickName = nickName;
			this.title = title;
			this.content = content;
			this.createdAt = createdAt;
			this.modifiedAt = modifiedAt;
		}
	}
}
