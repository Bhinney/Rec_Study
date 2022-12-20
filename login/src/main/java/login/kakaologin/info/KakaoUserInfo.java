package login.kakaologin.info;

import java.util.Map;

public class KakaoUserInfo extends OAuth2UserInfo{

	public KakaoUserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	/* 소셜 아이디 */
	@Override
	public String getId() {
		return attributes.get("id").toString();
	}

	/* 회원 이름 */
	@Override
	public String getName() {
		Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");

		if (properties == null) {
			return null;
		}

		return (String) properties.get("nickname");
	}

	/* 회원 이메일 */
	@Override
	public String getEmail() {
		return (String) attributes.get("account_email");
	}
}
