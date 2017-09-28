package travelmode.recommender.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import travelmode.recommender.domain.Travel;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface TravelRepository extends CrudRepository<Travel, Integer> {

    @Query("select t from Travel t where t.departure <= :departure")
    List<Travel> findByDeparture(@Param("departure") String departure);
}
