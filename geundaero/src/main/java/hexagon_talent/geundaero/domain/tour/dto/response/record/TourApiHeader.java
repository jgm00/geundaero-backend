package hexagon_talent.geundaero.domain.tour.dto.response.record;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TourApiHeader(String resultCode, String resultMsg) {
}
