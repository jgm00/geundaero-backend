package hexagon_talent.geundaero.domain.tour.controller;

import hexagon_talent.geundaero.common.ApiResponse;
import hexagon_talent.geundaero.domain.tour.dto.request.TourOverviewRequestDTO;
import hexagon_talent.geundaero.domain.tour.dto.response.TourOverviewResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Tour", description = "근대골목 투어 API")
public interface TourControllerDocs {

    @Operation(
            summary = "개요(overview) 조회",
            description =
                    "내부 locationId를 받아 DB에서 contentId를 찾고, **Tour API(detailCommon2)** 에 실시간 요청하여 " +
                            "**title, overview**를 정제해 반환합니다.\n\n" +
                            "1️⃣ **동작**\n" +
                            "- DB: `tour_location.id → content_id` 조회\n" +
                            "- 외부: `detailCommon2?MobileOS=ETC&MobileApp=...&_type=json&contentId=...` 호출\n" +
                            "- 응답: HTML/엔티티 정리된 `title`, `overview` 반환\n\n" +
                            "2️⃣ **요청 바디 예시**\n" +
                            "```json\n" +
                            "{ \"locationId\": 1 }\n" +
                            "```\n\n" +
                            "3️⃣ **성공 응답 예시**\n" +
                            "```json\n" +
                            "{\n" +
                            "  \"code\": \"0\",\n" +
                            "  \"message\": \"정상 처리 되었습니다.\",\n" +
                            "  \"data\": {\n" +
                            "    \"tour\": {\n" +
                            "      \"title\": \"대구 계산동성당\",\n" +
                            "      \"overview\": \"계산성당은 중구 계산오거리에 위치하며 범어대성당과 함께 천주교 대구 대교구의 공동 주교좌성당이다.\"\n" +
                            "    }\n" +
                            "  }\n" +
                            "}\n" +
                            "```\n\n"
    )
    ResponseEntity<ApiResponse<TourOverviewResponseDTO>> getOverview(@RequestBody TourOverviewRequestDTO request);
}

