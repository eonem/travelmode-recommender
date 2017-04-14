package travelmode.recommender.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@ConfigurationProperties(prefix = "travelmode")
@RefreshScope
public class TravelModeProperties {

    private String googleApiKey;

    private String apiUriTemplate;

    private String fallbackResponse;

    private String trafficModel;

    public String getGoogleApiKey() {
        return googleApiKey;
    }

    public void setGoogleApiKey(String googleApiKey) {
        this.googleApiKey = googleApiKey;
    }

    public String getApiUriTemplate() {
        return apiUriTemplate;
    }

    public void setApiUriTemplate(String apiUriTemplate) {
        this.apiUriTemplate = apiUriTemplate;
    }

    public String getFallbackResponse() {
        return fallbackResponse;
    }

    public void setFallbackResponse(String fallbackResponse) {
        this.fallbackResponse = fallbackResponse;
    }

    public String getTrafficModel() {
        return trafficModel;
    }

    public void setTrafficModel(String trafficModel) {
        this.trafficModel = trafficModel;
    }

}
