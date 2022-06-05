package net.gg.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
    GoogleMap Map;

    GoogleMapOptions options ;
    Location lastKnownLocation ;
    private FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);

        mapFragment.getMapAsync(this);








//
//        options.mapType(GoogleMap.MAP_TYPE_HYBRID)
//                .compassEnabled(false)
//                .rotateGesturesEnabled(false)
//                .tiltGesturesEnabled(false);

    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Map=googleMap;

        Map.setBuildingsEnabled(true);

        Map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
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


//               fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//                   @Override
//                   public void onComplete(@NonNull Task<Location> task) {
//                       Location location = task.getResult();
//                       if (location == null) {
//
//                       } else {
//
//                       }
//                   }
//               });



        Intent i =getIntent();
        double latitude;
        double longitude;
        if (i.hasExtra("details_location")){
            latitude = i.getDoubleExtra("latitude_D",-31.954301);
            longitude =i.getDoubleExtra("longitude_D",35.935330);

            LatLng coordinate = new LatLng(latitude, longitude);
            System.out.println("form detals page");
            Map.addMarker(new MarkerOptions().position(coordinate).title("Your Location"));
            Map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate,12.0f));

        }else if (i.hasExtra("latitude")){
            latitude = i.getDoubleExtra("latitude",-31.954301);
            longitude =i.getDoubleExtra("longitude",35.935330);

            LatLng coordinate = new LatLng(latitude, longitude);

            Map.addMarker(new MarkerOptions().position(coordinate).title("Your Location"));
            Map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate,12.0f));

        }else {
            latitude =-31.954301;
            longitude = 35.935330;

            System.out.println("000000000002515");

            LatLng coordinate = new LatLng(latitude, longitude);

            Map.addMarker(new MarkerOptions().position(coordinate).title("Your Location"));
            Map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 12.0f));
        }
        }


}