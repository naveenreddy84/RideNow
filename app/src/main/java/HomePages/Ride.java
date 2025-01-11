package HomePages;

import java.io.Serializable;

public class Ride implements Serializable {

    private String rideId;
    private String fromLocation;
    private String toLocation;
    private String time;
    private String price;
    private long timestampMillis; // Store timestamp as milliseconds

    public Ride(String rideId, String fromLocation, String toLocation, String time, String price, long timestampMillis) {
        this.rideId = rideId;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.time = time;
        this.price = price;
        this.timestampMillis = timestampMillis;
    }

    public String getRideId() {
        return rideId;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public String getTime() {
        return time;
    }

    public String getPrice() {
        return price;
    }

    public long getTimestampMillis() {
        return timestampMillis;
    }

    public void setTimestampMillis(long timestampMillis) {
        this.timestampMillis = timestampMillis;
    }
}
