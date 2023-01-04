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
	@Builder
	@AllArgsConstructor /* 테스트를 위해 추가 */
	public static class Response {
		private long boardId;
		private String nickName;
		private String title;
		private String content;
		private LocalDate createdAt;
		private LocalDate modifiedAt;
	}
}
