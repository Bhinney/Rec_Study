package org.study.boardProject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boards")
public class CommentController {

	/* 댓글 등록 */
	public ResponseEntity postComment(){
		return null;
	}

	/* 댓글 수정 */
	public ResponseEntity patchComment(){
		return null;
	}

	/* 댓글 조회 */
	public ResponseEntity getCommentList(){
		return null;
	}

	/* 댓글 삭제 */
	public ResponseEntity deleteComment(){
		return null;
	}

}
