package hexagon_talent.geundaero.domain.tour.controller;

import hexagon_talent.geundaero.common.ApiResponse;
import hexagon_talent.geundaero.domain.tour.dto.request.TourOverviewRequestDTO;
import hexagon_talent.geundaero.domain.tour.dto.response.TourOverviewResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

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

    @Operation(
            summary = "근처 음식점(거리순) 조회",
            description =
                    "GPS 좌표를 기준으로 반경 내 **음식점(contentTypeId=39)** 을 **거리순(arrange=S)** 으로 조회합니다.\n\n" +
                            "1️⃣ **동작**\n" +
                            "- 외부: `locationBasedList2?MobileOS=ETC&MobileApp=...&_type=json&mapX=...&mapY=...&radius=...&arrange=S&contentTypeId=39` 호출\n" +
                            "- 응답: `title, addr1, addr2, tel, mapx, mapy, dist` 필드만 정제해 반환\n\n" +
                            "2️⃣ **요청 파라미터**\n" +
                            "- `mapX`(경도, 필수), `mapY`(위도, 필수), `radius`(기본 1000, 최대 20000), `pageNo`(기본 1), `numOfRows`(기본 20)\n\n" +
                            "3️⃣ **예시 요청**\n" +
                            "```bash\n" +
                            "curl -G \"http://localhost:8080/api/tour/location-based/food\" \\\n" +
                            "  --data-urlencode \"mapX=127.0276\" \\\n" +
                            "  --data-urlencode \"mapY=37.4979\" \\\n" +
                            "  --data-urlencode \"radius=1500\" \\\n" +
                            "  --data-urlencode \"pageNo=1\" \\\n" +
                            "  --data-urlencode \"numOfRows=20\"\n" +
                            "```\n\n" +
                            "4️⃣ **성공 응답 예시**\n" +
                            "```json\n" +
                            "{\n" +
                            "  \"code\": \"0\",\n" +
                            "  \"message\": \"정상 처리 되었습니다.\",\n" +
                            "  \"data\": {\n" +
                            "    \"foods\": {\n" +
                            "      \"pageNo\": 1,\n" +
                            "      \"numOfRows\": 20,\n" +
                            "      \"items\": [\n" +
                            "        {\n" +
                            "          \"title\": \"홍길동식당\",\n" +
                            "          \"addr1\": \"서울 강남구 테헤란로 123\",\n" +
                            "          \"addr2\": \"지하 1층\",\n" +
                            "          \"tel\": \"02-123-4567\",\n" +
                            "          \"mapx\": \"127.0276\",\n" +
                            "          \"mapy\": \"37.4979\",\n" +
                            "          \"dist\": \"145\"\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  }\n" +
                            "}\n" +
                            "```\n\n" +
                            "✅ **고정값**: MobileOS=ETC, arrange=S(거리순), contentTypeId=39(음식점)"
    )
    ApiResponse<?> getLocationBasedFood(
            @RequestParam double mapX,
            @RequestParam double mapY,
            @RequestParam(defaultValue = "1000") int radius,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "20") int numOfRows
    ) ;
}

