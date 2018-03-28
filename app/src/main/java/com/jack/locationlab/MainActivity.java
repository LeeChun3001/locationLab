package com.jack.locationlab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;

import org.w3c.dom.Text;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;

public class MainActivity extends AppCompatActivity implements LocationListener {


    private TextView gps;
    private TextView tv2,tv3;
    private LocationManager locationManager;
    private static final int REQUEST_CONTACTS = 1;
    double lat = 22.965523;
    double lon = 120.168657 ;
    String latt;
    String lonn;
    private String TAG = "TAG";


    private PlaceAutocompleteAdapter adapter;
    private AutoCompleteTextView autocompleteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gps = findViewById(R.id.tv_gps);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);

        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openGpsService();
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });


        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .build();



        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        int permission = ActivityCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //未取得權限，向使用者要求允許權限
            ActivityCompat.requestPermissions( this,
                    new String[]{ACCESS_FINE_LOCATION},
                    REQUEST_CONTACTS );
        }else{
            //已有權限
            getGPS();
        }
    }


    @SuppressLint("MissingPermission")
    private void openGpsService() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //必須允許GPS權限
                    getGPS();
                } else {
                    //使用者拒絕權限，顯示對話框告知
                    new AlertDialog.Builder(this)
                            .setMessage("必須允許GPS權限")
                            .setPositiveButton("OK", null)
                            .show();
                }
                return;
        }
    }


    @SuppressLint("MissingPermission")
    public void getGPS() {

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setBearingRequired(true);//是否要求方向
        criteria.setCostAllowed(true);//是否要求收费

        String rovider  = locationManager.getBestProvider(criteria,true);


        Location x = null ;
        Location y = null ;
        //利用網路輔助定位
        x = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


        String lon = "empty";
        String lat = "empty";


        if (x != null) {

            lon = String.valueOf(x.getLongitude());
            lat = String.valueOf(x.getLatitude());
        }
        tv2.setText(lat + ","+ lon);


        lon = "empty";
        lat = "empty";

        //利用GPS精準定位
        y = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (y != null) {

            lon = String.valueOf(x.getLongitude());
            lat = String.valueOf(x.getLatitude());
        }
        tv3.setText(lat + ","+ lon);


        Log.e(TAG, "getGPS: "+ latt +", " + lonn);


    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat =  location.getLatitude();
        lon =  location.getLongitude();
        latt = String.valueOf(lat);
        lonn = String.valueOf(lon);
        gps.setText(latt + " , " + lonn);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
