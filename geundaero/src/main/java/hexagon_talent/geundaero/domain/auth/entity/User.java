package hexagon_talent.geundaero.domain.auth.entity;


import hexagon_talent.geundaero.domain.auth.dto.request.KakaoProfileDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private String profileImage;
    private Long kakaoId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(KakaoProfileDTO k) {
        this.kakaoId = k.getId();
        this.name = k.getNickname();
        this.email = (k.getKakao_account() != null) ? k.getKakao_account().getEmail() : null;
        this.profileImage = k.getProfileImageUrl();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}