package travelmode.recommender.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import travelmode.recommender.Application;
import travelmode.recommender.interfaces.TravelModeService;
import travelmode.recommender.responses.TravelMode;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class TravelModeControllerTest {

    @InjectMocks
    private TravelModeController travelModeController;

    @Mock
    private TravelModeService travelModeService;

    @Mock
    private RestTemplate restTemplate;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(travelModeController).build();
    }

    @Test
    public void shouldGetAccountByName() throws Exception {

        String from = "8+Mask+Avenue,+Donaghmede,+Dublin+5,+D05+E290,+Ireland";
        String to = "The+Academy,+Pearse+Street,+Dublin+2,+Ireland";
        String departureTimestamp = "1491813000";

        String duration = "23 mins";
        String mode = "WALKING";

        when(travelModeService.getRecommendedTravelMode(from, to, departureTimestamp)).thenReturn(new TravelMode(duration, mode));

        mockMvc.perform(get("/travelmode?from=" + from))
                .andExpect(jsonPath("$.duration").value(duration))
                .andExpect(jsonPath("$.mode").value(mode))
                .andExpect(status().isOk());
    }
}
