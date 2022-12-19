package login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import login.dto.MemberDto;
import login.entity.Member;
import login.mapper.MemberMapper;
import login.security.dto.LoginRequestDto;
import login.security.dto.TokenDto;
import login.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@Transactional
public class MemberController {
	private final MemberService memberService;
	private final MemberMapper mapper;

	/* 회원 가입 */
	@PostMapping("/members/signup")
	public ResponseEntity postMember(@Valid @RequestBody MemberDto.Post requestBody) throws Exception {
		Member createMember = memberService.createMember(mapper.memberPostDtoToMember(requestBody));


		/* 로그인 시도를 위한 LoginRequestDto 생성 */
		LoginRequestDto loginRequestDto = new LoginRequestDto(createMember.getEmail(), requestBody.getPassword());

		/* 자동 로그인 */
		return loginMember(loginRequestDto);
	}

	/* 로그인 */
	@PostMapping("/members/login")
	public ResponseEntity loginMember(@RequestBody LoginRequestDto requestBody) {

		/* 로그인 정보로 토큰 생성 */
		TokenDto tokenDto = memberService.tokenLogin(requestBody);

		/* 엑세스 토큰 헤더에 담아주기 */
		HttpHeaders httpHeaders = setHeader(tokenDto.getAccessToken());

		Member member = memberService.findVerifiedMemberByEmail(requestBody.getEmail());

		/* 역할에 따라 응답 바디가 다르므로, 나누어 주었다.*/
		if(member.getRole().equals("SELLER")) {
			return new ResponseEntity<>(mapper.memberToSellerResponseDto(member), httpHeaders, HttpStatus.OK);
		} else if (member.getRole().equals("CLIENT")) {
			return new ResponseEntity<>(mapper.memberToClientResponseDto(member), httpHeaders, HttpStatus.OK);
		}

		/* 로그인이 실패할 경우, 문제가 존재하는 것 */
		throw new RuntimeException("로그인에 실패하였습니다.");
	}

	/* 로그아웃 */
	@GetMapping("/members/logout")
	public ResponseEntity logoutMember(HttpServletRequest request) {
		memberService.logoutMember(request);

		return ResponseEntity.ok("로그아웃 되었습니다.");
	}

	/* 로그인 헤더 설정 */
	private HttpHeaders setHeader(String token) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Authorization", token);

		return httpHeaders;
	}
}
