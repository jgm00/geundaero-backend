package hexagon_talent.geundaero.domain.tour.dao;

import hexagon_talent.geundaero.domain.tour.entity.TourLocation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TourDAO {
    TourLocation findById(@Param("id") Long id);
}
