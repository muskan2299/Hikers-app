package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                //Log.i("Location",location.toString());
                updateLocation(location);
            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastknownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    public void startListening(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    public void updateLocation(Location location){
        //Log.i("Location",location.toString());
        TextView latText = findViewById(R.id.latText);
        TextView longText = findViewById(R.id.longText);
        TextView altText = findViewById(R.id.altText);
        TextView accText = findViewById(R.id.accText);
        TextView addText = findViewById(R.id.addText);

        latText.setText("Latitude:" + Double.toString(location.getLatitude()));
        longText.setText("Longitude:" + Double.toString(location.getLongitude()));
        accText.setText("Accuracy:" + Double.toString(location.getAccuracy()));
        altText.setText("Altitude:" + Double.toString(location.getAltitude()));

        String address = "Could not find address !!";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if(addressList != null && addressList.size() > 0){
                //Log.i("PlaceInfo",addressList.get(0).toString());
                address = "Address:\n";

                if (addressList.get(0).getThoroughfare() != null){
                    address += addressList.get(0).getThoroughfare() + " \n";
                }

                if (addressList.get(0).getLocality() != null){
                    address += addressList.get(0).getLocality() + " \n";
                }


                if (addressList.get(0).getPostalCode() != null){
                    address += addressList.get(0).getPostalCode() + " \n";
                }

                if (addressList.get(0).getAdminArea() != null){
                    address += addressList.get(0).getAdminArea();
                }

            }

        } catch (Exception e){
            e.printStackTrace();
        }

        addText.setText(address);

    }
}
