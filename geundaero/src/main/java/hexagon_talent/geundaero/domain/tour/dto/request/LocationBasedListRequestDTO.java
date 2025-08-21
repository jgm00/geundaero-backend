package hexagon_talent.geundaero.domain.tour.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record LocationBasedListRequestDTO(
        @NotNull Double mapX,
        @NotNull Double mapY,
        @Min(1) @Max(20000) Integer radius,
        Integer pageNo,
        Integer numOfRows
) {}

