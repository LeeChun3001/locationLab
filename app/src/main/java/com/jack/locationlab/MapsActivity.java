package com.jack.locationlab;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;

import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final int REQUEST_CONTACTS = 1;
    double lat = 25.126210;
    double lon = 121.500846;
    LatLng current;
    String latt;
    String lonn;
    private String TAG = "TAG";
    String StrCurrent;
    private LatLng search;
    Location currentlocation = new Location("");
    Location destlocation = new Location("");
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        checkPermission();

        Intent gpsi = getIntent();
        StrCurrent = gpsi.getStringExtra("name");
        double gpslat = gpsi.getDoubleExtra("lat", 22.965523);
        double gpslon = gpsi.getDoubleExtra("lon", 120.168657);
        search = new LatLng(gpslat, gpslon);


        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.e(TAG, "Place: " + place.getName());
                StrCurrent = place.getName().toString();
                current = place.getLatLng();
                selectLocation(StrCurrent, current);
            }

            @Override
            public void onError(Status status) {
                Log.e(TAG, "An error occurred: " + status);
            }
        });


    }

    private void selectLocation(String placeName, LatLng latLng) {
//        current = new LatLng(lat, lon);

        CameraPosition currentPosition = CameraPosition.builder()
                .target(latLng)
                .zoom(18)
                .bearing(0)
                .tilt(0)
                .build();
        mMap.addMarker(new MarkerOptions().position(latLng).title(placeName));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(currentPosition), 2000, null);
    }

    private void checkPermission() {
        int permission = ActivityCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //未取得權限，向使用者要求允許權限
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION},
                    REQUEST_CONTACTS);
        } else {
            //已有權限，可進行檔案存取
            getGPS();


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CONTACTS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        selectLocation(StrCurrent, search);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                StrCurrent = latLng.toString();
                current = latLng;
                selectLocation(StrCurrent, current);

                destlocation.setLongitude(latLng.longitude);
                destlocation.setLatitude(latLng.latitude);
                float dis = currentlocation.distanceTo(destlocation);
                Toast.makeText(MapsActivity.this, "distance : " + dis + "m", Toast.LENGTH_LONG).show();

            }
        });
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        checkPermission();


        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setMyLocationEnabled(true);
        mMap.isMyLocationEnabled();


        View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).
                getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 30);


        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                return false;
            }
        });
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @SuppressLint("MissingPermission")
    private void openGpsService() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    @SuppressLint("MissingPermission")
    public void getGPS() {
        openGpsService();

    }
    @Override
    public void onLocationChanged(Location location) {

        lat =  location.getLatitude();
        lon =  location.getLongitude();

        currentlocation.setLatitude(lat);
        currentlocation.setLongitude(lon);

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
