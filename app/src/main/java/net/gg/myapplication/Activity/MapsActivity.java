package net.gg.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import net.gg.myapplication.R;
import net.gg.myapplication.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap Map;
    private ActivityMapsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Map = googleMap;
        getLocation();
    }

    void getLocation(){
        String latitude_D;
        String longitude_D;


        Intent  io =getIntent();

        latitude_D = io.getStringExtra("latitude_D");
        longitude_D =io.getStringExtra("longitude_D");


        System.out.println(latitude_D+"form 22 page");

        LatLng coordinate = new LatLng(Double.parseDouble(latitude_D), Double.parseDouble(longitude_D));
        System.out.println("form detals page");
        Map.addMarker(new MarkerOptions().position(coordinate).title("Your Location"));
        Map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate,12.0f));
    }
}