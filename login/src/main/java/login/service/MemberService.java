package login.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	/* 회원 가입 */
	public Member createMember(Member member) {
		verifyEmailExist(member.getEmail());

		/* 비밀번호 암호화 */
		String encryptedPassword = passwordEncoder.encode(member.getPassword());
		member = Member.builder()
			.password(encryptedPassword)
			.providerType(ProviderType.LOCAL)
			.build();

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
}
