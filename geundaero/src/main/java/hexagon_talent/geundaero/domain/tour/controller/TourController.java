package hexagon_talent.geundaero.domain.tour.controller;

import hexagon_talent.geundaero.common.ApiResponse;
import hexagon_talent.geundaero.domain.tour.dto.request.TourOverviewRequestDTO;
import hexagon_talent.geundaero.domain.tour.dto.response.TourOverviewResponseDTO;
import hexagon_talent.geundaero.domain.tour.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import hexagon_talent.geundaero.domain.tour.dto.response.LocationBasedFoodResponseDTO;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/tour")
@RequiredArgsConstructor
public class TourController implements TourControllerDocs{

    private final TourService tourService;

    @PostMapping("/overview")
    public ResponseEntity<ApiResponse<TourOverviewResponseDTO>> getOverview(
            @RequestBody TourOverviewRequestDTO request
    ) {
        TourOverviewResponseDTO dto = tourService.getOverview(request.getLocationId());
        if (dto == null) {
            return ResponseEntity.ok(ApiResponse.fail("NO_CONTENT", "overview not found"));
        }
        return ResponseEntity.ok(ApiResponse.success("tour",dto));
    }

    @GetMapping("/location-based/food")
    public ApiResponse<?> getLocationBasedFood(
            @RequestParam double mapX,
            @RequestParam double mapY,
            @RequestParam(defaultValue = "1000") int radius,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "20") int numOfRows
    ) {
        List<LocationBasedFoodResponseDTO> items = tourService.getNearbyFoods(mapX, mapY, radius, pageNo, numOfRows);

        Map<String, Object> payload = new HashMap<>();
        payload.put("pageNo", pageNo);
        payload.put("numOfRows", numOfRows);
        payload.put("items", items);

        return ApiResponse.success("foods", payload);
    }


}
