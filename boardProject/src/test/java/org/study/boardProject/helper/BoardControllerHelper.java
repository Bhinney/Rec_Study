package org.study.boardProject.helper;

public interface BoardControllerHelper extends ControllerTestHelper {
	String url = "/api/boards";
	String resource = "/{boardId}";

	default String getBoardURI() {
		return url;
	}


	default String getBoardResourceURI() {
		return url + resource;
	}
}
