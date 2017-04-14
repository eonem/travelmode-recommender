package travelmode.recommender.interfaces;

import travelmode.recommender.responses.TravelMode;


public interface TravelModeService {

    TravelMode getRecommendedTravelMode(String from, String to, String departureTimestamp);
}
