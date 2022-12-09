package study.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;

	@Column(length = 45, nullable = false)
	private String name;

	@Column(length = 45, nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(length = 50, nullable = false)
	private String phone;

	@Column(length = 100, nullable = false)
	private String address;

	/* 소셜 로그인 아이디 추가 */
	@Column
	private String socialId;

	/* 소셜 로그인을 추가하면서 해당 판별을 위한 프로바이더 타입 추가 */
	@Enumerated(EnumType.STRING)
	private ProviderType providerType;

	/* security 이용하여 역할 추가 */
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> roles;
}
