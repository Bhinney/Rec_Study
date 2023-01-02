package org.study.boardProject.controller;

/* 게시판 Controller */

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

	/* 게시판 등록 */
	public ResponseEntity post() {
		return null;
	}

	/* 게시판 수정 */
	public ResponseEntity patch() {
		return null;
	}

	/* 특정 게시판 조회 */
	public ResponseEntity getBoard() {
		return null;
	}

	/* 전체 게시판 조회 */
	public ResponseEntity getBoardList() {
		return null;
	}

	/* 게시판 삭제 */
	public ResponseEntity delete() {
		return null;
	}
}
