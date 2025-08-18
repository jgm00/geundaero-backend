package hexagon_talent.geundaero.domain.auth.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
public class KakaoProfileDTO {
    private Long id;
    private Map<String, Object> properties;
    private KakaoAccount kakao_account;

    public String getNickname() {
        if (kakao_account != null && kakao_account.getProfile() != null) {
            String n = kakao_account.getProfile().getNickname();
            if (n != null && !n.isBlank()) return n;
        }
        return properties == null ? null : toStr(properties.get("nickname"));
    }
    public String getProfileImageLegacy() {
        return properties == null ? null : toStr(properties.get("profile_image"));
    }
    public String getProfileImageUrl() {
        if (kakao_account != null && kakao_account.getProfile() != null) {
            String url = kakao_account.getProfile().getProfile_image_url();
            if (url != null && !url.isBlank()) return url;
        }
        String legacy = getProfileImageLegacy();
        return (legacy != null && !legacy.isBlank()) ? legacy : null;
    }
    private String toStr(Object v){ return v == null ? null : String.valueOf(v); }

    @Data
    public static class KakaoAccount{
        private String email;
        private String birthday;
        private String gender;
        private String age_range;
        private Profile profile;

        @Data
        public static class Profile{
            private String nickname;
            private String profile_image_url;
            private String thumbnail_image_url;
        }
    }
}