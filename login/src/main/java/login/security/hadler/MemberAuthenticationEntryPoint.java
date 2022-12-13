package login.security.hadler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/* 유효하지 않은 인증이거나 인증 정보가 부족 ->  401 Unauthorized 에러 */
@Slf4j
@Component
public class MemberAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {
		log.warn("401 Unauthorized error happened: {}", authException.getMessage());
		log.error("유효하지 않은 인증 혹은 인증 정보 없음 에러 : {}", authException.getMessage());
	}
}
