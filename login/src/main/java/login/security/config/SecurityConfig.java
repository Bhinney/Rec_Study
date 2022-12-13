package login.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import login.security.jwt.JwtProvider;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtProvider jwtProvider;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.headers().frameOptions().sameOrigin()
			.and()
			.csrf().disable()
			.cors()
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.formLogin().disable()
			.httpBasic().disable()
			.apply(new JwtSecurityConfig(jwtProvider))
			.and()
			.authorizeHttpRequests(authorize -> authorize

				/* 메인 페이지는 모두 접근이 가능해야한다. */
				.antMatchers(HttpMethod.GET, "/").permitAll() /* 메인 페이지 */

				/* 회원 관련 접근 제한 */
				.antMatchers(HttpMethod.POST, "/members/signup").permitAll() /* 자체 회원가입 */
				.antMatchers(HttpMethod.POST, "/login").permitAll() /* 자체 로그인 */
				.antMatchers(HttpMethod.GET, "/login/**").permitAll() /* 소셜 로그인을 위해 */
				.antMatchers(HttpMethod.POST, "/login/**").permitAll() /* 소셜 로그인을 위해 */

				/* 접근 제한 확인을 위해 나머지는 접근할 수 없는 권한으로 설정 */
				.anyRequest().authenticated()
			);
			// .exceptionHandling()
			// .authenticationEntryPoint(new MemberAuthenticationEntryPoint())
			// .accessDeniedHandler(new MemberAccessDeniedHandler());

		return http.build();
	}
}

