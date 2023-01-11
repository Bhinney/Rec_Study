package org.study.boardProject.global.exception.custom;

import lombok.Getter;

public enum ExceptionCode {

	/* 게시판 관련 예외 */
	BOARD_NOT_FOUND(404, "게시판을 찾을 수 없습니다."),

	/* 댓글 관련 예외*/
	COMMENT_NOT_FOUND(404, "댓글을 찾을 수 없습니다.");


	@Getter
	private int status;

	@Getter
	private String message;

	ExceptionCode(int code, String message) {
		this.status = code;
		this.message = message;
	}
}
