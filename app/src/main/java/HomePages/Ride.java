package HomePages;

import java.io.Serializable;

public class Ride implements Serializable {
    private String fromLocation;
    private String ToLocation;
    private String time;
    private String Price;
    private String formatedDate;

    public Ride(String fromLocation, String toLocation, String time, String price, String formattedDate) {
        this.fromLocation = fromLocation;
        this.ToLocation = toLocation;
        this.time = time;
        this.Price = price;
        this.formatedDate = formattedDate;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public String getToLocation() {
        return ToLocation;
    }

    public String getTime() {
        return time;
    }

    public String getPrice() {
        return Price;
    }

    public String getFormattedDate() {
        return formatedDate;
    }
}

