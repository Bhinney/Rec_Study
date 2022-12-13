package login.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import login.dto.MemberDto;
import login.entity.Member;
import login.security.dto.LoginRequestDto;
import login.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;

	/* 회원 가입 */
	@PostMapping("/members/signup")
	public ResponseEntity postMember(@Valid @RequestBody MemberDto.Post requestBody) throws Exception {
		Member createMember = memberService.createMember(requestBody);


		/* 로그인 시도를 위한 LoginRequestDto 생성 */
		LoginRequestDto loginRequestDto = new LoginRequestDto(createMember.getEmail(), requestBody.getPassword());

		/* 로그인 */
		return null;
	}
}
