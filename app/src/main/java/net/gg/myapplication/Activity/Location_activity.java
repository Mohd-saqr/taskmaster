package net.gg.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.amplifyframework.datastore.generated.model.LocationTask;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import net.gg.myapplication.R;

public class Location_activity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap googleMap;

    private double latitude;
    private double longitude;
    GoogleMapOptions options ;
    Location lastKnownLocation ;
    private FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);








//
//        options.mapType(GoogleMap.MAP_TYPE_HYBRID)
//                .compassEnabled(false)
//                .rotateGesturesEnabled(false)
//                .tiltGesturesEnabled(false);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap=googleMap;
        this.googleMap.setBuildingsEnabled(true);

        this.googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        getDeviceLocation();




    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */


               fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                   @Override
                   public void onComplete(@NonNull Task<Location> task) {
                       Location location = task.getResult();
                       if (location == null) {

                       } else {
                           latitude = location.getLatitude();
                           longitude = location.getLongitude();

                           LatLng coordinate = new LatLng(latitude, longitude);

                           googleMap.addMarker(new MarkerOptions()
                                   .position(coordinate)
                                   .title("Marker"));

//                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
                           googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 12.0f));
                       }
                   }
               });

        }


}