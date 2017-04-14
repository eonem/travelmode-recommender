package travelmode.recommender.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import travelmode.recommender.interfaces.TravelModeService;
import travelmode.recommender.responses.TravelMode;

@RestController
public class TravelModeController {

    @Autowired
    TravelModeService travelModeService;

    @RequestMapping(path = "/travelmode", method = RequestMethod.GET)
    public TravelMode getRecommendedTravelMode(@RequestParam String from,
                                               @RequestParam String to,
                                               @RequestParam String departure) {
        return travelModeService.getRecommendedTravelMode(to, from, departure);
    }
}
