package HomePages;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ridenow.R;

import java.util.List;

public class RideAdapter extends ArrayAdapter<Ride> {



    TextView fromLocation,ToLocation,Price,time,formattedDate;
    Button BookButton;

     private Context context;
    private List<Ride> rides;





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

        Ride currentRide = rides.get(position);  // setting the position of ridedata based on index position.

        // Set the ride details
        TextView fromLocation = convertView.findViewById(R.id.fromLocation);
        TextView ToLocation = convertView.findViewById(R.id.ToLocation);
        TextView Price = convertView.findViewById(R.id.Price);
        TextView time = convertView.findViewById(R.id.time);
        TextView formatedDate = convertView.findViewById(R.id.formatedDate);
        Button bookButton = convertView.findViewById(R.id.BookButton);

        // Set the data for the current ride
        fromLocation.setText(String.join(" ","From: " + currentRide.getFromLocation()));
        ToLocation.setText(String.join(" ","To: " + currentRide.getToLocation()));
        Price.setText(String.join("","Price: " + currentRide.getPrice()));
        time.setText(String.join("","Time: " + currentRide.getTime()));
        formatedDate.setText(String.join("","Date: " + currentRide.getFormattedDate()));


        BookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PaymentPage.class);
                context.startActivity(intent);
            }
        });


        return convertView;
    }
}

