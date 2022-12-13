package login.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import login.dto.MemberDto;
import login.entity.Client;
import login.entity.Seller;
import login.security.util.CustomAuthorityUtils;
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

	/* 회원 가입 */
	public Member createMember(MemberDto.Post requestBody) {
		verifyEmailExist(requestBody.getEmail());
		correctRole(requestBody.getRole());
		List<String> roles = authorityUtils.createRoles(requestBody.getRole());
		String encryptedPassword = passwordEncoder.encode(requestBody.getPassword());

		/* 비밀번호 암호화 + 대문자로 저장 */
		Member member = Member.builder()
			.password(encryptedPassword)
			.providerType(ProviderType.LOCAL)
			.role(requestBody.getRole().toUpperCase())
			.roles(roles)
			.build();
		if (requestBody.getRole().equalsIgnoreCase("client")) {
			member.setClient(new Client());
		}
		if (requestBody.getRole().equalsIgnoreCase("seller")) {
			member.setSeller(new Seller());
		}

		return memberRepository.save(member);
	}

	/* 존재하는 이메일인지 확인 */
	public void verifyEmailExist(String email) {
		Optional<Member> optionalMember = memberRepository.findByEmail(email);
		if (optionalMember.isPresent()) {
			throw new RuntimeException("존재하는 이메일입니다.");
		}
	}

	/* 존재하는 회원인지 확인 */
	public Member findVerifiedMember(long memberId) {
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

	/* 비밀번호 확인 */
	public void correctPassword(String password, String passwordCheck) {
		if (!password.equals(passwordCheck)) {
			throw new RuntimeException("비밀번호가 일치하지 않습니다.");
		}
	}
}
