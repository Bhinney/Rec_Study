package org.study.boardProject.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

public class BoardDto {

	@Getter
	@Builder
	public static class Post {
		private String nickName;
		private String title;
		private String content;
	}

	@Getter
	@Builder
	public static class Patch {
		private String nickName;
		private String title;
		private String content;
	}

	@Getter
	@Builder
	public static class Response {
		private long boardId;
		private String nickName;
		private String title;
		private String content;
		private LocalDate createdAt;
		private LocalDate modifiedAt;
	}
}
