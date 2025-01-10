package HomePages;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

import com.example.ridenow.R;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RideAdapter extends ArrayAdapter<Ride> {

    private Context context;
    private List<Ride> rides;


    TextView fromLocation, toLocation, price, time, formatedDate;
    Button bookButton;

    public RideAdapter(Context context, List<Ride> rides) {
        super(context, 0, rides);
        this.context = context;
        this.rides = rides;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.ride_item_layout, parent, false);
        }

        // Get current ride
        Ride currentRide = rides.get(position);

        // Set the ride details
        fromLocation = convertView.findViewById(R.id.fromLocation);
        toLocation = convertView.findViewById(R.id.toLocation);
        price = convertView.findViewById(R.id.price);
        time = convertView.findViewById(R.id.time);
        formatedDate = convertView.findViewById(R.id.formatedDate);
        bookButton = convertView.findViewById(R.id.BookButton);

        // Set the data for the current ride
        if (currentRide != null) {
            fromLocation.setText("From: " + currentRide.getFromLocation());
            toLocation.setText("To: " + currentRide.getToLocation());
            price.setText("Price: " + currentRide.getPrice());
            time.setText("Time: " + currentRide.getTime());
            Timestamp timestamp = currentRide.getTimestampDate();
            if (timestamp != null) {
                Date date = timestamp.toDate();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String formattedDateStr = sdf.format(date);
                formatedDate.setText("Date: " + formattedDateStr);
            } else {
                formatedDate.setText("Date not available");
            }
        }


        // Set up the Book Button
        bookButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, PaymentPage.class);
            context.startActivity(intent);
        });

        return convertView;
    }
}


