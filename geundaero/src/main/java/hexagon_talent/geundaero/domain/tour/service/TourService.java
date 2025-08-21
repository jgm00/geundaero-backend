package hexagon_talent.geundaero.domain.tour.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexagon_talent.geundaero.domain.tour.dto.response.LocationBasedFoodResponseDTO;
import hexagon_talent.geundaero.util.TextSanitizer;
import hexagon_talent.geundaero.domain.tour.dao.TourDAO;
import hexagon_talent.geundaero.domain.tour.dto.response.TourOverviewResponseDTO;
import hexagon_talent.geundaero.domain.tour.entity.TourLocation;
import hexagon_talent.geundaero.external.TourApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TourService {

    private final TourDAO tourDAO;
    private final TourApiClient tourApiClient;
    private final ObjectMapper om = new ObjectMapper();

    @Value("${tourapi.app-name}")   private String appName;
    @Value("${tourapi.service-key}") private String serviceKey;

    public TourOverviewResponseDTO getOverview(Long locationId) {
        TourLocation loc = tourDAO.findById(locationId);
        if (loc == null) throw new IllegalArgumentException("invalid locationId");

        Map<String, Object> res = tourApiClient.getDetailCommon(
                "ETC", appName, "json", String.valueOf(loc.getContentId()), serviceKey
        );


        JsonNode root  = om.convertValue(res, JsonNode.class);
        JsonNode itemN = root.path("response").path("body").path("items").path("item");
        if (itemN.isMissingNode() || itemN.isNull()) return null;

        JsonNode first = itemN.isArray() ? itemN.get(0) : itemN;

        String titleApi    = first.path("title").asText(null);
        String overviewRaw = first.path("overview").asText(null);
        if (overviewRaw == null || overviewRaw.isBlank()) return null;

        String title = (titleApi != null && !titleApi.isBlank()) ? titleApi : loc.getTitle();

        return TourOverviewResponseDTO.builder()
                .title(TextSanitizer.clean(title))
                .overview(TextSanitizer.clean(overviewRaw))
                .build();
    }
    public List<LocationBasedFoodResponseDTO> getNearbyFoods(double mapX, double mapY, Integer radius, Integer pageNo, Integer numOfRows) {
        int r  = Math.min(Optional.ofNullable(radius).orElse(1000), 20000);
        int pn = Optional.ofNullable(pageNo).orElse(1);
        int nr = Optional.ofNullable(numOfRows).orElse(20);

        Map<String, Object> res = tourApiClient.getLocationBasedFood(
                serviceKey,
                "ETC",
                appName,
                "json",
                mapX, mapY, r,
                "S",
                "39",
                pn, nr
        );

        JsonNode root  = om.convertValue(res, JsonNode.class);
        JsonNode items = root.path("response").path("body").path("items").path("item");
        if (items.isMissingNode() || items.isNull()) return List.of();

        List<LocationBasedFoodResponseDTO> list = new ArrayList<>();
        if (items.isArray()) {
            for (JsonNode n : items) list.add(toFoodDTO(n));
        } else {
            list.add(toFoodDTO(items));
        }
        return list;
    }

    private LocationBasedFoodResponseDTO toFoodDTO(JsonNode n) {
        return new LocationBasedFoodResponseDTO(
                n.path("title").asText(null),
                n.path("addr1").asText(null),
                n.path("addr2").asText(null),
                n.path("tel").asText(null),
                n.path("mapx").asText(null),
                n.path("mapy").asText(null),
                n.path("dist").asText(null)
        );
    }
}