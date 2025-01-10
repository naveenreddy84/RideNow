package HomePages;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Ride implements Serializable {

    private String rideId;
    private String fromLocation;
    private String toLocation;
    private String time;
    private String price;
    private Timestamp timestampDate;


    public Ride(String rideId, String fromLocation, String toLocation, String time, String price, Timestamp timestampDate) {
        this.rideId = rideId;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.time = time;
        this.price = price;
        this.timestampDate = timestampDate;
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

    public Timestamp getTimestampDate() {
        return timestampDate;
    }
}


