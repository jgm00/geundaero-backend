package hexagon_talent.geundaero.domain.tour.entity;

import lombok.Data;

@Data
public class TourLocation {
    private Long id;
    private Long contentId;
    private String title;
    private String description;

    private Double lat;
    private Double lng;
    private String polygonWkt;
}
