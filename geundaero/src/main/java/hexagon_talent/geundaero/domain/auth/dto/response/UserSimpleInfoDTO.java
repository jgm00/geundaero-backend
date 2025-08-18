package hexagon_talent.geundaero.domain.auth.dto.response;

import hexagon_talent.geundaero.domain.auth.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSimpleInfoDTO {
    private long id;
    private String name;
    private String email;
    private String profileImage;
    private long kakaoId;

    public UserSimpleInfoDTO(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.profileImage = user.getProfileImage();
        this.kakaoId = user.getKakaoId();
    }
}