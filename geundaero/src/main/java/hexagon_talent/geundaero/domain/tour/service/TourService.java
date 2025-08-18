package hexagon_talent.geundaero.domain.tour.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexagon_talent.geundaero.util.TextSanitizer;
import hexagon_talent.geundaero.domain.tour.dao.TourDAO;
import hexagon_talent.geundaero.domain.tour.dto.response.TourOverviewResponseDTO;
import hexagon_talent.geundaero.domain.tour.entity.TourLocation;
import hexagon_talent.geundaero.external.TourApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

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

}
