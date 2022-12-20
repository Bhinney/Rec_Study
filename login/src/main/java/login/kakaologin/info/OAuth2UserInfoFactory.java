package login.kakaologin.info;

import java.util.Map;

import login.entity.ProviderType;

public class OAuth2UserInfoFactory {
	public static OAuth2UserInfo getOAuth2UserInfo(ProviderType providerType, Map<String, Object> attributes) {
		switch (providerType) {
			case KAKAO: return new KakaoUserInfo(attributes);
			default: throw new IllegalArgumentException("Invalid Provider Type.");
		}
	}
}
