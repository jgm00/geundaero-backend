package hexagon_talent.geundaero.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@FeignClient(name = "tourApiClient", url = "${tourapi.base-url}")
public interface TourApiClient {
        @GetMapping("/detailCommon2")
        Map<String,Object> getDetailCommon(
                @RequestParam("MobileOS") String mobileOS,
                @RequestParam("MobileApp") String mobileApp,
                @RequestParam("_type") String type,
                @RequestParam("contentId") String contentId,
                @RequestParam("serviceKey") String serviceKey
        );

        @GetMapping("/locationBasedList2")
        Map<String, Object> getLocationBasedFood(
                @RequestParam("serviceKey") String serviceKey,
                @RequestParam("MobileOS") String mobileOS,
                @RequestParam("MobileApp") String mobileApp,
                @RequestParam("_type") String type,

                @RequestParam("mapX") double mapX,
                @RequestParam("mapY") double mapY,
                @RequestParam("radius") int radius,

                @RequestParam("arrange") String arrange,
                @RequestParam("contentTypeId") String typeFood,

                @RequestParam("pageNo") int pageNo,
                @RequestParam("numOfRows") int numOfRows
        );
}
