package com.jack.locationlab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import org.w3c.dom.Text;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements LocationListener {


    private TextView gps;
    private LocationManager locationManager;
    private LocationListener locationListeners;
    private static final int REQUEST_CONTACTS = 1;
    double lat = 22.965523;
    double lon = 120.168657 ;
    String latt;
    String lonn;
    private String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gps = findViewById(R.id.tv_gps);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        int permission = ActivityCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //未取得權限，向使用者要求允許權限
            ActivityCompat.requestPermissions( this,
                    new String[]{ACCESS_FINE_LOCATION ,ACCESS_COARSE_LOCATION},
                    REQUEST_CONTACTS );
        }else{
            //已有權限，可進行檔案存取
            getGPS();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //取得聯絡人權限，進行存取
                    getGPS();
                } else {
                    //使用者拒絕權限，顯示對話框告知
                    new AlertDialog.Builder(this)
                            .setMessage("必須允許聯絡人權限才能顯示資料")
                            .setPositiveButton("OK", null)
                            .show();
                }
                return;
        }
    }


    @SuppressLint("MissingPermission")
    public void getGPS() {

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        Log.e(TAG, "getGPS: "+ latt +", " + lonn);


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
