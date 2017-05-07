package travelmode.recommender.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import travelmode.recommender.Application;
import travelmode.recommender.configuration.TravelModeProperties;
import travelmode.recommender.enums.TravelModeEnum;
import travelmode.recommender.exceptions.ExternalCommunicationsException;
import travelmode.recommender.exceptions.ResponseDeserializationException;
import travelmode.recommender.responses.TravelMode;
import travelmode.recommender.responses.google.Duration;
import travelmode.recommender.responses.google.GoogleApiResponse;
import travelmode.recommender.responses.google.Leg;
import travelmode.recommender.responses.google.Route;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class TravelModeServiceTest {

    @InjectMocks
    TravelModeServiceImpl travelModeService;

    @Mock
    TravelModeProperties travelModeProperties;

    @Mock
    RestTemplate restTemplate;

    @Mock
    GoogleApiResponse mockGoogleResponse;

    private String from = "test location from";
    private String to = "test location to";
    private String departureTimestamp = "2440913400";

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void testTravelModeServiceReturnsDrivingWhenDrivingDurationIsShorter() throws Exception {

        List<GoogleApiResponse> mockGoogleApiResponseList = prepareMockGoogleResponseList(30, 50);
        when(restTemplate.getForObject(Mockito.anyString(), Matchers.any(Class.class))).thenAnswer(AdditionalAnswers.returnsElementsOf(mockGoogleApiResponseList));

        TravelMode actualTravelMode = travelModeService.getRecommendedTravelMode(from, to, departureTimestamp);
        assertEquals(30, actualTravelMode.getDuration());
        assertEquals(TravelModeEnum.DRIVING, actualTravelMode.getMode());
    }

    @Test
    public void testTravelModeServiceReturnsTransitWhenTransitDurationIsShorter() throws Exception {

        List<GoogleApiResponse> mockGoogleApiResponseList = prepareMockGoogleResponseList(50, 30);
        when(restTemplate.getForObject(Mockito.anyString(), Matchers.any(Class.class))).thenAnswer(AdditionalAnswers.returnsElementsOf(mockGoogleApiResponseList));

        TravelMode actualTravelMode = travelModeService.getRecommendedTravelMode(from, to, departureTimestamp);
        assertEquals(30, actualTravelMode.getDuration());
        assertEquals(TravelModeEnum.TRANSIT, actualTravelMode.getMode());
    }

    @Test
    public void testTravelModeServiceReturnsDrivingWhenDurationsAreEqual() throws Exception {

        List<GoogleApiResponse> mockGoogleApiResponseList = prepareMockGoogleResponseList(30, 30);
        when(restTemplate.getForObject(Mockito.anyString(), Matchers.any(Class.class))).thenAnswer(AdditionalAnswers.returnsElementsOf(mockGoogleApiResponseList));

        TravelMode actualTravelMode = travelModeService.getRecommendedTravelMode(from, to, departureTimestamp);
        assertEquals(30, actualTravelMode.getDuration());
        assertEquals(TravelModeEnum.DRIVING, actualTravelMode.getMode());
    }

    @Test(expected = ExternalCommunicationsException.class)
    public void testTravelModeServiceReturnsCustomExceptionWhenRestClientExceptionOccurs() throws Exception {

        RestClientException mockRestClientException = new RestClientException("mockMessage", new RuntimeException());
        when(restTemplate.getForObject(Mockito.anyString(), Matchers.any(Class.class))).thenThrow(mockRestClientException);

        TravelMode actualTravelMode = travelModeService.getRecommendedTravelMode(from, to, departureTimestamp);
    }

    @Test(expected = ExternalCommunicationsException.class)
    public void testTravelModeServiceReturnsCustomExceptionWhenResponseStatusIsNotSuccess() throws Exception {

        GoogleApiResponse mockGoogleApiResponseWithEmptyRoutes = getMockGoogleApiResponseWithFailureStatus();
        when(restTemplate.getForObject(Mockito.anyString(), Matchers.any(Class.class))).thenReturn(mockGoogleApiResponseWithEmptyRoutes);

        TravelMode actualTravelMode = travelModeService.getRecommendedTravelMode(from, to, departureTimestamp);
    }

    @Test(expected = ResponseDeserializationException.class)
    public void testTravelModeServiceReturnsCustomExceptionWhenThereIsNoRoutesInResponse() throws Exception {

        GoogleApiResponse mockGoogleApiResponseWithEmptyRoutes = getMockGoogleApiResponseWithEmptyRoutes();
        when(restTemplate.getForObject(Mockito.anyString(), Matchers.any(Class.class))).thenReturn(mockGoogleApiResponseWithEmptyRoutes);

        TravelMode actualTravelMode = travelModeService.getRecommendedTravelMode(from, to, departureTimestamp);
    }

    private List<GoogleApiResponse> prepareMockGoogleResponseList(int drivingDuration, int transitDuration) {

        GoogleApiResponse mockGoogleApiResponseForDriving = getMockGoogleApiResponseWithDuration(drivingDuration);
        GoogleApiResponse mockGoogleApiResponseForTransit = getMockGoogleApiResponseWithDuration(transitDuration);
        GoogleApiResponse[] mockGoogleApiResponses = {mockGoogleApiResponseForDriving, mockGoogleApiResponseForTransit};
        return Arrays.asList(mockGoogleApiResponses);
    }

    private GoogleApiResponse getMockGoogleApiResponseWithDuration(int duration) {

        Leg mockLeg = new Leg();
        Duration mockDuration = new Duration();
        mockDuration.setValue(duration);
        mockLeg.setDurationInTraffic(mockDuration);
        Leg[] mockLegs = new Leg[]{mockLeg};

        Route mockRoute = new Route();
        mockRoute.setLegs(mockLegs);
        Route[] mockRoutes = new Route[]{mockRoute};

        GoogleApiResponse mockResponse = new GoogleApiResponse();
        mockResponse.setRoutes(mockRoutes);

        mockResponse.setStatus("OK");

        return mockResponse;
    }

    private GoogleApiResponse getMockGoogleApiResponseWithEmptyRoutes() {

        GoogleApiResponse mockResponse = new GoogleApiResponse();
        mockResponse.setStatus("OK");

        return mockResponse;
    }

    private GoogleApiResponse getMockGoogleApiResponseWithFailureStatus() {

        GoogleApiResponse mockResponse = new GoogleApiResponse();
        mockResponse.setStatus("FAIL");

        return mockResponse;
    }
}
