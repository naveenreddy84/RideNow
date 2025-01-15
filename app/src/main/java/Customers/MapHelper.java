package Customers;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

public class MapHelper {

    // A method to handle setting up the map with locations, markers, and directions
    public static void setupMap(GoogleMap googleMap, String fromLocation, String toLocation, Context context) {
        // Retrieve the coordinates of the selected locations
        LatLng startLocation = getCoordinates(fromLocation);
        LatLng endLocation = getCoordinates(toLocation);

        // Check if both locations are valid
        if (startLocation != null && endLocation != null) {
            // Add markers for both start and end locations
            googleMap.addMarker(new MarkerOptions().position(startLocation).title("Start: " + fromLocation));
            googleMap.addMarker(new MarkerOptions().position(endLocation).title("Destination: " + toLocation));

            // Set the camera to view both markers within the bounds
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(startLocation);
            builder.include(endLocation);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));

            // Request directions between the two locations
            fetchDirections(googleMap, startLocation, endLocation, context);
        } else {
            Toast.makeText(context, "Invalid locations selected.", Toast.LENGTH_SHORT).show();
        }
    }

    // A method to retrieve the coordinates for known locations
    private static LatLng getCoordinates(String location) {
        switch (location) {
            case "Montreal":
                return new LatLng(45.5017, -73.5673);
            case "Toronto":
                return new LatLng(43.6532, -79.3832);
            case "Ottawa":
                return new LatLng(45.4215, -75.6992);
            default:
                return null;
        }
    }

    // A method to fetch directions and draw a polyline on the map
    private static void fetchDirections(GoogleMap googleMap, LatLng origin, LatLng destination, Context context) {
        // Initialize the GeoApiContext with your Maps API key
        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .apiKey("AIzaSyAIBuH45lw-2q4GnINKq9dA_upx9sVmfso") // Replace with your actual API key
                .build();

        // Create a Directions API request to get directions from the origin to the destination
        DirectionsApiRequest request = DirectionsApi.newRequest(geoApiContext)
                .origin(new com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                .destination(new com.google.maps.model.LatLng(destination.latitude, destination.longitude))
                .departureTimeNow()
                .avoid(DirectionsApi.RouteRestriction.TOLLS);

        // Set up the callback for the directions API request
        request.setCallback(new com.google.maps.PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                // If a route is found, add the polyline to the map
                if (result.routes.length > 0) {
                    DirectionsRoute route = result.routes[0];
                    PolylineOptions polylineOptions = new PolylineOptions();

                    // Decode the polyline from the directions result and add it to the map
                    for (com.google.maps.model.LatLng step : route.overviewPolyline.decodePath()) {
                        polylineOptions.add(new LatLng(step.lat, step.lng));
                    }
                    googleMap.addPolyline(polylineOptions);
                } else {
                    Toast.makeText(context, "No route found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable e) {
                // Handle failure case (e.g., API request failure)
                Toast.makeText(context, "Error fetching directions: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
