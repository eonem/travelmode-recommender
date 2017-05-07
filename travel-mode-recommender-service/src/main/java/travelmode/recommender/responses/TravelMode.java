package travelmode.recommender.responses;


import travelmode.recommender.enums.TravelModeEnum;

public class TravelMode {

    private int duration;

    private TravelModeEnum mode;

    private String error;

    public TravelMode(int duration, TravelModeEnum mode) {
        this.duration = duration;
        this.mode = mode;
    }

    public TravelMode(String error) {
        this.error = error;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public TravelModeEnum getMode() {
        return mode;
    }

    public void setMode(TravelModeEnum mode) {
        this.mode = mode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
