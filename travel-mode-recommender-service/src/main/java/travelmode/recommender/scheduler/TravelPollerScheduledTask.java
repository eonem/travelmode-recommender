package travelmode.recommender.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import travelmode.recommender.configuration.TravelModeProperties;
import travelmode.recommender.domain.Travel;
import travelmode.recommender.interfaces.TravelModeService;
import travelmode.recommender.repositories.TravelRepository;
import travelmode.recommender.responses.TravelMode;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class TravelPollerScheduledTask {

    private static final Logger logger = LoggerFactory
            .getLogger(TravelPollerScheduledTask.class);

    @Autowired
    TravelModeProperties travelModeProperties;

    @Autowired
    TravelRepository travelRepository;

    @Autowired
    TravelModeService travelModeService;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "dd-MM-yyyyTHH:mm:ss");

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void pollTravels() {

        long departureDateUpperBoundTimestamp = System.currentTimeMillis() + Long.parseLong(travelModeProperties.getTravelPollDateRangeInMins()) * 60 * 1000;
        String departureDateUpperBound = dateFormat.format(departureDateUpperBoundTimestamp);

        List<Travel> travels = travelRepository.findByDeparture(departureDateUpperBound);
        travels.stream().forEach(travel -> {
            TravelMode recommendation = travelModeService.getRecommendedTravelMode(travel.getOrigin(),
                    travel.getDestination(),
                    travel.getDeparture());
            
            logger.debug("Recommendation for userId: {} departure: {} is: {} with duration: {}",
                    travel.getUserId(),
                    travel.getDeparture(),
                    recommendation.getMode(),
                    recommendation.getDuration());
        });
    }
}

