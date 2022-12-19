package login.mapper;

import org.mapstruct.Mapper;

import login.dto.MemberDto;
import login.entity.Member;

@Mapper(componentModel = "spring")
public interface MemberMapper {

	default Member memberPostDtoToMember(MemberDto.Post post) {
		if (post == null) {
			return null;
		}

		Member.MemberBuilder member = Member.builder();

		member.name( post.getName() );
		member.email( post.getEmail() );
		member.password( post.getPassword() );
		member.phone( post.getPhone() );
		member.address( post.getAddress() );
		member.role( post.getRole() );

		return member.build();
	}

	default MemberDto.ClientResponseDto memberToClientResponseDto(Member member) {
		if (member == null) {
			return null;
		}

		MemberDto.ClientResponseDto response =
			MemberDto.ClientResponseDto.builder()
				.memberId(member.getMemberId())
				.clientId(member.getClient().getClientId())
				.email(member.getEmail())
				.name(member.getName())
				.phone(member.getPhone())
				.address(member.getAddress())
				.role(member.getRole())
				.build();

		return response;
	}

	default MemberDto.SellerResponseDto memberToSellerResponseDto(Member member) {
		if (member == null) {
			return null;
		}

		MemberDto.SellerResponseDto response =
			MemberDto.SellerResponseDto.builder()
				.memberId(member.getMemberId())
				.sellerId(member.getSeller().getSellerId())
				.email(member.getEmail())
				.name(member.getName())
				.phone(member.getPhone())
				.address(member.getAddress())
				.role(member.getRole())
				.introduce(member.getSeller().getIntroduce())
				.imageUrl(member.getSeller().getImageUrl())
				.build();

		return response;
	}
}
