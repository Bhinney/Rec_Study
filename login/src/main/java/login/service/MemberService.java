package login.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import login.entity.Client;
import login.entity.Seller;
import login.security.dto.LoginRequestDto;
import login.security.dto.TokenDto;
import login.security.jwt.JwtProvider;
import login.security.util.CustomAuthorityUtils;
import login.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import login.entity.Member;
import login.entity.ProviderType;
import login.repository.MemberRepository;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final CustomAuthorityUtils authorityUtils;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtProvider jwtProvider;
	private final RedisTemplate<String, Object> redisTemplate;

	/* 회원 가입 */
	@Transactional
	public Member createMember(Member member) {

		verifyEmailExist(member.getEmail());
		correctRole(member.getRole());
		List<String> roles = authorityUtils.createRoles(member.getRole());
		if (member.getRole().equalsIgnoreCase("client")) {
			member.setClient(new Client());
		}
		if (member.getRole().equalsIgnoreCase("seller")) {
			member.setSeller(new Seller());
		}

		/* 비밀번호 암호화 + 대문자로 저장 */
		String encryptedPassword = passwordEncoder.encode(member.getPassword());

		member.setCreateMember(
			encryptedPassword,
			member.getRole().toUpperCase(),
			roles,
			ProviderType.LOCAL
		);

		return memberRepository.save(member);
	}

	/* 로그인 */
	public TokenDto tokenLogin(LoginRequestDto loginRequestDto) {

		/* 존재하는 회원인지 확인 */
		Member member = findVerifiedMemberByEmail(loginRequestDto.getEmail());

		/* 비밀번호가 맞는지 확인 */
		verifyPassword(member, loginRequestDto.getPassword());

		/* 프로바이더 확인 */
		checkLocalMember(member);

		/* 로그인 기반으로 "Authentication 토큰" 생성 */
		UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();

		/* AuthenticationToken 으로 인증 정보 가져오기 */
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

		TokenDto tokenDto = jwtProvider.generatedTokenDto(authentication.getName());

		/* Refresh Token 저장 */
		redisTemplate.opsForValue()
			.set("RefreshToken:" + authentication.getName(), tokenDto.getRefreshToken(),
				tokenDto.getRefreshTokenExpiresIn() - new Date().getTime(), TimeUnit.MICROSECONDS);

		return tokenDto;
	}

	/* 로그아웃 */
	public void logoutMember(HttpServletRequest request) {
		String accessToken = request.getHeader("Authorization").replace("Bearer ", "");

		/* Access Token 검증 */
		if (!jwtProvider.validate(accessToken)) {
			log.error("유효하지 않은 access token");
			throw new RuntimeException("유효하지 않은 access token");
		}

		/* 인증 정보 가져오기 */
		Authentication authentication = jwtProvider.getAuthentication(accessToken);

		/* 현재 유저가 맞는지 확인 */
		String email = SecurityUtil.getCurrentEmail();
		if (!authentication.getName().equals(email)) {
			throw new RuntimeException("로그인한 사용자가 일치하지 않습니다.");
		}

		/* Redis 에서 RefreshToken 삭제 */
		redisTemplate.delete("RefreshToken:" + authentication.getName());
	}

	/* 카카오 로그인 */
	public TokenDto kakaoLogin(LoginRequestDto requestDto) {

		TokenDto tokenDto = jwtProvider.generatedTokenDto(requestDto.getEmail());

		/* Refresh Token 저장 */
		redisTemplate.opsForValue()
			.set("RefreshToken:" + requestDto.getEmail(), tokenDto.getRefreshToken(),
				tokenDto.getRefreshTokenExpiresIn() - new Date().getTime(), TimeUnit.MICROSECONDS);

		return tokenDto;
	}

	/* 소셜 로그인 권한 수정 */
	public Member updateSocial(String role, long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
		checkSocialRole(member.getRole());

		if (role.equalsIgnoreCase("CLIENT")) {
			member.setCreateMember(
				"소셜 로그인 사용자, 권한 수정 완료",
				"CLIENT",
				List.of("CLIENT"),
				ProviderType.KAKAO
			);
			member.setClient(new Client());
		} else if (role.equalsIgnoreCase("SELLER")) {
			member.setCreateMember(
				"소셜 로그인 사용자, 권한 수정 완료",
				"SELLER",
				List.of("SELLER"),
				ProviderType.KAKAO
			);
			member.setSeller(new Seller());
		} else {
			throw new RuntimeException("수정할 수 없습니다.");
		}

		return member;
	}

	/* 존재하는 이메일인지 확인 */
	public void verifyEmailExist(String email) {
		Optional<Member> optionalMember = memberRepository.findByEmail(email);
		if (optionalMember.isPresent()) {
			throw new RuntimeException("존재하는 이메일입니다.");
		}
	}

	/* 이메일로 존재하는 회원인지 확인 */
	public Member findVerifiedMemberByEmail(String email) {
		Member findMember = memberRepository.findByEmail(email)
			.orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

		return findMember;
	}

	/* 존재하는 회원인지 확인 */
	private Member findVerifiedMember(long memberId) {
		Optional<Member> optionalMember = memberRepository.findById(memberId);
		Member findMember = optionalMember.orElseThrow(
			() -> new RuntimeException("회원을 찾을 수 없습니다."));

		return findMember;
	}

	/* 역할 확인 */
	private void correctRole(String target) {
		if (! target.equalsIgnoreCase("CLIENT") && ! target.equalsIgnoreCase("SELLER")) {
			throw new RuntimeException("역할이 잘못 입력되었습니다.");
		}
	}

	/* 회원가입 시 비밀번호 확인 */
	public void correctPassword(String password, String passwordCheck) {
		if (!password.equals(passwordCheck)) {
			throw new RuntimeException("비밀번호가 일치하지 않습니다.");
		}
	}

	/* 로그인 시 비밀번호 확인 */
	private void verifyPassword(Member member, String password){

		if (!passwordEncoder.matches(password, member.getPassword())) {
			throw new RuntimeException("올바르지 않은 비밀번호 입니다.");
		}
	}

	/* 로컬 회원인지 확인 */
	private void checkLocalMember(Member member) {
		if (member.getProviderType() != ProviderType.LOCAL) {
			throw new RuntimeException("소셜 회원입니다.");
		}
	}

	/* social 역할 확인 */
	private void checkSocialRole(String role) {
		if (!role.equalsIgnoreCase("SOCIAL")) {
			throw new RuntimeException("소셜 권한 사용자가 아닙니다.");
		}
	}
}
