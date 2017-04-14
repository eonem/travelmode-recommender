package travelmode.recommender.responses;


public class TravelMode {

    private String duration;

    private String mode;

    private String error;

    public TravelMode(String duration, String mode) {
        this.duration = duration;
        this.mode = mode;
    }

    public TravelMode(String error) {
        this.error = error;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String id) {
        this.duration = duration;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String text) {
        this.mode = mode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
