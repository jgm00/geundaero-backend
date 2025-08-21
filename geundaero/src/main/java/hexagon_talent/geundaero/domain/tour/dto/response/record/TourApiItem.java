package hexagon_talent.geundaero.domain.tour.dto.response.record;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TourApiItem(
        String title,
        String addr1,
        String addr2,
        String tel,
        String mapx,
        String mapy,
        String dist
) {
}
