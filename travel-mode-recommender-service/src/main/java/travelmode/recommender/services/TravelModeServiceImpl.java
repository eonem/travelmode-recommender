package travelmode.recommender.services;

import com.google.common.base.Strings;
import org.antlr.stringtemplate.StringTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import travelmode.recommender.configuration.TravelModeProperties;
import travelmode.recommender.enums.TravelModeEnum;
import travelmode.recommender.exceptions.ExternalCommunicationsException;
import travelmode.recommender.exceptions.ResponseDeserializationException;
import travelmode.recommender.exceptions.ServiceConfigurationException;
import travelmode.recommender.interfaces.TravelModeService;
import travelmode.recommender.responses.TravelMode;
import travelmode.recommender.responses.google.GoogleApiResponse;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
@EnableConfigurationProperties(TravelModeProperties.class)
public class TravelModeServiceImpl implements TravelModeService {

    @Autowired
    TravelModeProperties travelModeProperties;

    @Autowired
    RestTemplate restTemplate;

    private Logger logger = LoggerFactory.getLogger(TravelModeServiceImpl.class);

    @PostConstruct
    public void init() throws Exception {
        if (Strings.isNullOrEmpty(travelModeProperties.getGoogleApiKey())) {
            throw new ServiceConfigurationException("Google API key has not been set.");
        }
    }

    //@HystrixCommand(fallbackMethod = "tryAgainLater")
    public TravelMode getRecommendedTravelMode(String from, String to, String departureTimestamp)
            throws ExternalCommunicationsException, ResponseDeserializationException {

        String googleApiUriTemplateForDriving = populateGoogleUriTemplate(from, to, departureTimestamp, TravelModeEnum.DRIVING);
        String googleApiUriTemplateForTransit = populateGoogleUriTemplate(from, to, departureTimestamp, TravelModeEnum.TRANSIT);

        int drivingTravelTime = getEstimatedTravelTimeByMode(googleApiUriTemplateForDriving);
        int transitTravelTime = getEstimatedTravelTimeByMode(googleApiUriTemplateForTransit);

        if (logger.isDebugEnabled()) {
            String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(Long.parseLong(departureTimestamp) * 1000));
            logger.debug("Estimated time for DRIVING on date: {} is {} mins.", date, drivingTravelTime / 60);
            logger.debug("Estimated time for TRANSIT on date: {} is {} mins.", date, transitTravelTime / 60);
        }

        if (drivingTravelTime <= transitTravelTime) {
            return new TravelMode(drivingTravelTime, TravelModeEnum.DRIVING);
        } else {
            return new TravelMode(transitTravelTime, TravelModeEnum.TRANSIT);
        }
    }

    private int getEstimatedTravelTimeByMode(String googleApiUriTemplate)
            throws ExternalCommunicationsException, ResponseDeserializationException {

        GoogleApiResponse googleResponse;
        try {
            googleResponse = restTemplate.getForObject(googleApiUriTemplate, GoogleApiResponse.class);
            if (googleResponse.isSuccess()) {
                return extractEstimatedTravelTime(googleResponse);
            } else {
                logger.error("Google Directions API call failure with status: {} error message: {}",
                        googleResponse.getStatus(),
                        googleResponse.getErrorMessage());
            }
        } catch (RestClientException e) {
            logger.error("Network exception occurred while calling Google Directions API: ", e.getCause().getMessage());
            throw new ExternalCommunicationsException(e.getCause());
        }

        throw new ExternalCommunicationsException();
    }

    private int extractEstimatedTravelTime(GoogleApiResponse googleResponse)
            throws ResponseDeserializationException {

        return Optional.ofNullable(googleResponse.getRoutes())
                .map(routes -> routes[0])
                .map(route -> route.getLegs())
                .map(legs -> legs[0])
                .map(leg -> leg.getDurationInTraffic() != null ? leg.getDurationInTraffic() : leg.getDuration())
                .map(duration -> duration.getValue())
                .orElseThrow(ResponseDeserializationException::new);
    }

    private String populateGoogleUriTemplate(String from, String to, String departureTimestamp, TravelModeEnum travelModeEnum) {

        StringTemplate googleApiUriTemplate = new StringTemplate(travelModeProperties.getApiUriTemplate());
        googleApiUriTemplate.setAttribute("from", from);
        googleApiUriTemplate.setAttribute("to", to);
        googleApiUriTemplate.setAttribute("departureTimestamp", departureTimestamp);
        googleApiUriTemplate.setAttribute("trafficModel", travelModeProperties.getTrafficModel());
        googleApiUriTemplate.setAttribute("key", travelModeProperties.getGoogleApiKey());
        googleApiUriTemplate.setAttribute("travelMode", travelModeEnum.toString().toLowerCase());

        String uri = googleApiUriTemplate.toString();
        logger.debug("Making the call to URI: {}", uri);

        return uri;
    }

    private TravelMode tryAgainLater(String from, String to, String departureTimestamp) {
        throw new ServiceConfigurationException("Try again later.");
    }
}
