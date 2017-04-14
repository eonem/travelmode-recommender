package travelmode.recommender.services;

import com.google.common.base.Strings;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.antlr.stringtemplate.StringTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import travelmode.recommender.configuration.TravelModeProperties;
import travelmode.recommender.exceptions.ServiceConfigurationException;
import travelmode.recommender.interfaces.TravelModeService;
import travelmode.recommender.responses.TravelMode;
import travelmode.recommender.responses.google.GoogleApiResponse;

import javax.annotation.PostConstruct;

@Service
@EnableConfigurationProperties(TravelModeProperties.class)
public class TravelModeServiceImpl implements TravelModeService {

    private TravelMode ERROR_RESPONSE;

    @Autowired
    TravelModeProperties travelModeProperties;

    @Autowired
    RestTemplate restTemplate;

    @PostConstruct
    public void init() throws Exception {
        if (travelModeProperties == null) {
            throw new ServiceConfigurationException("TravelModeProperties could not be read.");
        }

        if (Strings.isNullOrEmpty(travelModeProperties.getGoogleApiKey())) {
            throw new ServiceConfigurationException("Google API key has not been set.");
        }

        ERROR_RESPONSE = new TravelMode(travelModeProperties.getFallbackResponse());
    }

    @HystrixCommand(fallbackMethod = "tryAgainLater")
    public TravelMode getRecommendedTravelMode(String from, String to, String departureTimestamp) {

        StringTemplate googleApiUriTemplate = new StringTemplate(travelModeProperties.getApiUriTemplate());
        googleApiUriTemplate.setAttribute("from", from);
        googleApiUriTemplate.setAttribute("to", to);
        googleApiUriTemplate.setAttribute("departureTimestamp", departureTimestamp);
        googleApiUriTemplate.setAttribute("trafficModel", travelModeProperties.getTrafficModel());
        googleApiUriTemplate.setAttribute("key", travelModeProperties.getGoogleApiKey());

        GoogleApiResponse googleResponse = restTemplate.getForObject(googleApiUriTemplate.toString(), GoogleApiResponse.class);

        // TODO extract travelMode from response
        return googleResponse.isSuccess() ? new TravelMode(googleResponse.getRoutes()[0].getLegs()[0].getDuration().getText(), "WALKING") : ERROR_RESPONSE;
    }

    private TravelMode tryAgainLater() {
        return ERROR_RESPONSE;
    }
}
