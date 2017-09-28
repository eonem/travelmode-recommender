package travelmode.recommender.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import travelmode.recommender.domain.Travel;
import travelmode.recommender.repositories.TravelRepository;

@RestController
public class TravelController {

    @Autowired
    TravelRepository travelRepository;

    @RequestMapping(path = "/travel/{userId}", method = RequestMethod.POST)
    public @ResponseBody
    Travel postTravel(@PathVariable("userId") String userId,
                      @RequestParam String origin,
                      @RequestParam String destination,
                      @RequestParam String departure) {

        return travelRepository.save(new Travel(userId, origin, destination, departure));
    }
}
