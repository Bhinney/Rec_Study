package org.study.boardProject.helper;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

import com.google.gson.Gson;

public interface ControllerTestHelper<T> {

	default RequestBuilder postRequestBuilder(URI uri,
		String content) {
		return MockMvcRequestBuilders
			.post(uri)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(content);
	}

	default RequestBuilder patchRequestBuilder(URI uri,
		String content) {
		return MockMvcRequestBuilders
			.patch(uri)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(content);

	}

	default RequestBuilder getRequestBuilder(String url, long sourceId) {
		return get(url, sourceId)
			.accept(MediaType.APPLICATION_JSON);
	}

	default RequestBuilder getRequestBuilder(String url, MultiValueMap<String, String> queryParams){
		return get(url)
			.params(queryParams)
			.accept(MediaType.APPLICATION_JSON);
	}


	default RequestBuilder deleteRequestBuilder(String uri, long sourceId) {
		return delete(uri, sourceId);
	}

	default URI createURI(String url) {
		return UriComponentsBuilder.newInstance().path(url).build().toUri();
	}

	default URI createURI(String url, long resourceId) {
		return UriComponentsBuilder.newInstance().path(url).buildAndExpand(resourceId).toUri();
	}

	default String toJsonContent(T t) {
		Gson gson = new Gson();
		String content = gson.toJson(t);
		return content;
	}
}
