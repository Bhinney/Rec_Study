package login.security.hadler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/* 접근에 필요한 권한 없이 접근 -> 403 forbidden 에러 */
@Slf4j
@Component
public class MemberAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException, ServletException {
		log.warn("403 Forbidden error happened: {}", accessDeniedException.getMessage());
		log.error("접근 권한 없음 에러 : {}", accessDeniedException.getMessage());
	}
}