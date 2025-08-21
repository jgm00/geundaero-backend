package hexagon_talent.geundaero.domain.tour.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationBasedFoodResponseDTO {
    private String title;
    private String addr1;
    private String addr2;
    private String tel;
    private String mapx;
    private String mapy;
    private String dist;
}
