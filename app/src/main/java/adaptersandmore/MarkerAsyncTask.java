package adaptersandmore;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class MarkerAsyncTask extends AsyncTask<Void, Void, String> {

    private DatabaseReference databaseReference, subReference, keyReference, latLngRef, latReference, lngReference;
    private String sType, storeType, getLat, getLng;
    private double lat, lng;
    private LatLng latLng;
    private GoogleMap googleMap;
    private MarkerOptions marker;

    public MarkerAsyncTask (DatabaseReference databaseReference, String sType, GoogleMap googleMap, MarkerOptions marker) {

        this.databaseReference = databaseReference;
        this.sType = sType;
        this.googleMap = googleMap;
        this.marker = marker;
    }

    @Override
    protected String doInBackground(Void... params) {

        databaseReference.addValueEventListener(new ValueEventListener() { //attach listener

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { //something changed!
                for (DataSnapshot businessSnapshot : dataSnapshot.getChildren()) {
                    final String busName = businessSnapshot.getKey();
                    Log.d("myTagBusSnap", "Business name is: " + busName);
                    subReference = databaseReference.child(busName);
                    keyReference = subReference.child("z_businessType");
                    keyReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            storeType = dataSnapshot.getValue().toString();
                            Log.d("myTagBusKey", storeType);
                            if (storeType.contains(sType)) {
                                latLngRef = subReference.child("z_businessCoodinates");
                                latReference = latLngRef.child("latitude");
                                lngReference = latLngRef.child("longitude");
                                latReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        getLat = dataSnapshot.getValue(String.class);
                                        Log.d("myTagLat", getLat);
                                        //postLat = Double.parseDouble(lat);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                lngReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        getLng = dataSnapshot.getValue(String.class);
                                        Log.d("myTagLng", getLng);
                                        lat = Double.parseDouble(getLat);
                                        lng = Double.parseDouble(getLng);
                                        latLng = new LatLng(lat, lng);
                                        googleMap.addMarker(marker.position(latLng).title(busName)).showInfoWindow();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            } else {

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { //update UI here if error occurred.

            }
        });


        return "Download complete";
    }

}
