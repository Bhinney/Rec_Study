package login.kakaologin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import login.entity.Member;
import login.entity.ProviderType;
import login.kakaologin.dto.KakaoToken;
import login.kakaologin.info.KakaoProfile;
import login.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoService {

	private final MemberRepository memberRepository;

	@Value("${spring.security.oauth2.client.registration.kakao.clientId}")
	private String clientId;

	@Value("${spring.security.oauth2.client.registration.kakao.redirectUri}")
	private String redirectUri;

	@Value("${spring.security.oauth2.client.provider.kakao.tokenUri}")
	private String tokenUri;

	@Value("${spring.security.oauth2.client.provider.kakao.userInfoUri}")
	private String userInfoUri;

	/* code 로 kakao 의 엑세스 토큰 발급 받기 */
	public KakaoToken getAccessToken(String code) {
		/* RequestParam */
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("grant_type", "authorization_code");
		param.add("client_id", clientId);
		param.add("redirect_uri", redirectUri);
		param.add("code", code);

		/* 요청 */
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-type","application/x-www-form-urlencoded;charset=utf-8");
		HttpEntity<MultiValueMap<String,String>> requestEntity = new HttpEntity<>(param, headers);

		RestTemplate template = new RestTemplate();
		KakaoToken kakaoToken = template.postForObject(tokenUri, requestEntity, KakaoToken.class);

		return kakaoToken;
	}

	/* 받은 엑세스 토큰으로 카카오 회원 정보 가져오기 */
	public KakaoProfile findProfile(String token) {
		RestTemplate template = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);

		KakaoProfile kakaoProfile = template.postForObject(userInfoUri, headers, KakaoProfile.class);

		return kakaoProfile;
	}

	/* 카카오 로그인 */
	@Transactional
	public Member saveMember(String accessToken) {

		/* 사용자 정보 받아오기 */
		KakaoProfile profile = findProfile(accessToken);
		Member member = memberRepository.findMemberByEmail(profile.getKakao_account().getEmail());

		/* 첫 이용자 강제 회원가입 */
		if (member == null) {
			String name = profile.getKakao_account().getProfile().getNickname();
			if (name == null || name.equals("")) {
				name = "이름을 입력하세요";
			}

			member = new Member(
				name,
				profile.getKakao_account().getEmail(),
				profile.getId(),
				"010-0000-0000",
				"주소를 입력하세요."
			);

			member.setCreateMember(
				"카카오 로그인 사용자",
				"SOCIAL",
				List.of("SOCIAL"),
				ProviderType.KAKAO
			);

			memberRepository.save(member);
		}

		/* 만일 사용자가 로컬 사용자라면 예외를 던져야 함 */
		if (member.getProviderType() == ProviderType.LOCAL) {
			throw new RuntimeException("소셜 로그인 사용자가 아닙니다.");
		}

		return member;
	}
}
