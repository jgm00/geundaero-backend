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

@RestController
@RequestMapping("/api/tour")
@RequiredArgsConstructor

public class TourController implements TourControllerDocs{

    private final TourService tourOverviewService;

    @PostMapping("/overview")
    public ResponseEntity<ApiResponse<TourOverviewResponseDTO>> getOverview(
            @RequestBody TourOverviewRequestDTO request
    ) {
        TourOverviewResponseDTO dto = tourOverviewService.getOverview(request.getLocationId());
        if (dto == null) {
            return ResponseEntity.ok(ApiResponse.fail("NO_CONTENT", "overview not found"));
        }
        return ResponseEntity.ok(ApiResponse.success("tour",dto));
    }
}
