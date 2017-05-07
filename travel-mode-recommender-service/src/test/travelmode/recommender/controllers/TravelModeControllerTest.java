package travelmode.recommender.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import travelmode.recommender.Application;
import travelmode.recommender.enums.TravelModeEnum;
import travelmode.recommender.exceptions.ExternalCommunicationsException;
import travelmode.recommender.exceptions.ResponseDeserializationException;
import travelmode.recommender.exceptions.ServiceConfigurationException;
import travelmode.recommender.interfaces.TravelModeService;
import travelmode.recommender.responses.TravelMode;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class TravelModeControllerTest {

    @InjectMocks
    private TravelModeController travelModeController;

    @Mock
    private TravelModeService travelModeService;

    private MockMvc mockMvc;

    private String from = "test location from";
    private String to = "test location to";
    private String departureTimestamp = "2440913400";

    @Before
    public void setup() {
        initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(travelModeController).build();
    }

    @Test
    public void testTravelModeControllerSuccess() throws Exception {

        int expectedDuration = 1382;
        TravelModeEnum expectedMode = TravelModeEnum.TRANSIT;

        when(travelModeService.getRecommendedTravelMode(from, to, departureTimestamp)).thenReturn(new TravelMode(expectedDuration, expectedMode));

        mockMvc.perform(get("/travelmode?from=" + from + "&to=" + to + "&departure=" + departureTimestamp).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().string("{\"duration\":1382,\"mode\":\"TRANSIT\",\"error\":null}"));
    }

    @Test
    public void testTravelModeControllerThrowsResponseDeserializationException() throws Exception {

        when(travelModeService.getRecommendedTravelMode(from, to, departureTimestamp)).thenThrow(new ResponseDeserializationException());

        mockMvc.perform(get("/travelmode?from=" + from + "&to=" + to + "&departure=" + departureTimestamp))
                .andExpect(status().isBadGateway());
    }

    @Test
    public void testTravelModeControllerThrowsExternalCommunicationsException() throws Exception {

        when(travelModeService.getRecommendedTravelMode(from, to, departureTimestamp)).thenThrow(new ExternalCommunicationsException());

        mockMvc.perform(get("/travelmode?from=" + from + "&to=" + to + "&departure=" + departureTimestamp))
                .andExpect(status().isBadGateway());
    }

    @Test
    public void testTravelModeControllerThrowsServiceConfigurationException() throws Exception {

        when(travelModeService.getRecommendedTravelMode(from, to, departureTimestamp)).thenThrow(new ServiceConfigurationException());

        mockMvc.perform(get("/travelmode?from=" + from + "&to=" + to + "&departure=" + departureTimestamp))
                .andExpect(status().isInternalServerError());
    }
}