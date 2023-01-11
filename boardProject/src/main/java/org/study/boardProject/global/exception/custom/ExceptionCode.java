package org.study.boardProject.global.exception.custom;

import lombok.Getter;

public enum ExceptionCode {

	/* 게시판 관련 예외 */
	BOARD_NOT_FOUND(404, "게시판이 존재하지 않습니다."),

	/* 댓글 관련 예외*/
	COMMENT_NOT_FOUND(404, "댓글이 존재하지 않습니다."),
	NOT_CORRECT_BOARDID(400, "게시판 정보가 댓글의 게시판 정보와 일치하지 않습니다.");


	@Getter
	private int status;

	@Getter
	private String message;

	ExceptionCode(int code, String message) {
		this.status = code;
		this.message = message;
	}
}
