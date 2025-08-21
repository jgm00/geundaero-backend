package hexagon_talent.geundaero.domain.tour.dto.response.record;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TourApiBody(
        Integer pageNo,
        Integer numOfRows,
        Integer totalCount,
        TourApiItems items
) {
}
