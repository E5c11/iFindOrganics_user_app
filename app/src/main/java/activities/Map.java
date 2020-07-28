package activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ifo.userapp.BaseActivity;
import com.ifo.userapp.R;

import adaptersandmore.MarkerAsyncTask;
import adaptersandmore.NullMarkerAsyncTask;

public class Map extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleMap mGoogleMap;
    private MapView mapView;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location loc;
    private LatLng getLatLng, currentLatLng;
    private double lat, lng;

    private static final int Request_User_Location_Code = 99;
    private String title, getLat, getLng, sType;
    private int height = 100;
    private int width = 100;

    private DatabaseReference databaseReference;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawerList.setItemChecked(position, true);
        page = 4;
        getLayoutInflater().inflate(R.layout.map_fragment, baseLayout);
        mapInstance(savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Business details");
        databaseReference.keepSynced(true);
    }

    private void mapInstance(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.mipmap.marker_logo);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        MarkerOptions marker = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

        UiSettings uiSettings = mGoogleMap.getUiSettings();
        uiSettings.setCompassEnabled(true);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            buildApiClient();
            googleMap.setMyLocationEnabled(true);

            Intent i = getIntent();
            if (i.hasExtra("lat")) {
                title = i.getStringExtra("busName");
                sType = i.getStringExtra("sType");
                getLat = i.getStringExtra("lat");
                getLng = i.getStringExtra("lng");
                Log.d("myTagMapName", title);
                Log.d("myTagMapLat", getLat);
                Log.d("myTagMapLng", getLng);
                lat = Double.parseDouble(getLat);
                lng = Double.parseDouble(getLng);
                getLatLng = new LatLng(lat, lng);

                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getLatLng, 13));
                //googleMap.addMarker(marker.position(getLatLng).title(title)).showInfoWindow();
                MarkerAsyncTask markerAsyncTask = new MarkerAsyncTask(databaseReference, sType, googleMap, marker);
                markerAsyncTask.execute();
            } else {
                if (currentLatLng == null) {
                    NullMarkerAsyncTask nullMarkerAsyncTask = new NullMarkerAsyncTask(databaseReference, googleMap, marker);
                    nullMarkerAsyncTask.execute();
                } else {
                Log.d("myTagHello", "From button");
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13));
                NullMarkerAsyncTask nullMarkerAsyncTask = new NullMarkerAsyncTask(databaseReference, googleMap, marker);
                nullMarkerAsyncTask.execute();
                }
            }
            /*googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    marker.getTitle();
                }
            });*/
        }
    }

    protected synchronized void buildApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        loc = location;
        currentLatLng = new LatLng(location.getLatitude(),location.getLongitude());

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13));

        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Request_User_Location_Code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            buildApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "Please enable your GPS", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
