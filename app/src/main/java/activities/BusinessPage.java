package activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ifo.userapp.BaseActivity;
import com.ifo.userapp.R;

import adaptersandmore.BusinessDetails;

public class BusinessPage extends BaseActivity {

    private DatabaseReference subReference, latReference, lngReference, telReference, websiteReference, webPageReference;
    private Query limitRef;
    private String dataID, postBusBanner, postNumber, postWebsite, lat, lng, sType;
    private ImageView ctBanner;
    private RecyclerView busRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.business_activity, baseLayout);

        getIncomingData();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Business details");
        databaseReference.keepSynced(true);
        subReference = databaseReference.child(dataID);
        limitRef = subReference.limitToFirst(7);


        setPageContent();
        Toolbar ctb = findViewById(R.id.bp_toolbar);
        setSupportActionBar(ctb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ctBanner = findViewById(R.id.bus_tb_banner);

        busRecyclerView = findViewById(R.id.bus_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        busRecyclerView.setLayoutManager(layoutManager);
        busRecyclerView.setHasFixedSize(true);
        busRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        DatabaseReference latLngRef = subReference.child("z_businessCoordinates");
        latReference = latLngRef.child("latitude");
        lngReference = latLngRef.child("longitude");
        DatabaseReference phoneReference = subReference.child("f_businessNumber");
        telReference = phoneReference.child("value");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getIncomingData() {
        super.onStart();
        if (getIntent().hasExtra("dataURL")){
            dataID = getIntent().getStringExtra("dataURL");
            sType = getIntent().getStringExtra("sType");
            //Log.d("myTagBus", dataID);
            //Log.d("myTagBusType", sType);
        }
    }

    private void  setPageContent() {
        DatabaseReference bannerReference = subReference.child("z_businessBanner");
        getSupportActionBar().setTitle(dataID);

        bannerReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String banner = dataSnapshot.getValue(String.class);
                postBusBanner = banner;
                Glide.with(getApplicationContext()).asBitmap().load(postBusBanner).into(ctBanner);
                //Log.d("myTagName", banner.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<BusinessDetails, BusContentHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BusinessDetails, BusContentHolder>
                (BusinessDetails.class, R.layout.bus_detail_list, BusContentHolder.class, limitRef) {
            @Override
            protected void populateViewHolder(BusContentHolder viewHolder, BusinessDetails model, final int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setValue(model.getValue());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (position) {
                            case 2:
                                latReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        lat = dataSnapshot.getValue(String.class);
                                        Log.d("myTagLat", lat);
                                        //postLat = Double.parseDouble(lat);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                lngReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        lng = dataSnapshot.getValue(String.class);
                                        Log.d("myTagLng", lng);
                                        //postLng = Double.parseDouble(lng);
                                        Intent two = new Intent(getApplicationContext(), Map.class);
                                        Log.d("myTagLat", lat);
                                        Log.d("myTagLng", lng);
                                        two.putExtra("lat", lat);
                                        two.putExtra("lng", lng);
                                        two.putExtra("busName", dataID);
                                        two.putExtra("sType", sType);
                                        startActivity(two);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                break;
                            case 5:
                                telReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String number = dataSnapshot.getValue(String.class);
                                        Log.d("myTagName", number);
                                        postNumber = number;
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                Intent five = new Intent(Intent.ACTION_DIAL, Uri.parse(("tel:" + postNumber)));
                                startActivity(five);
                                break;
                            case 6:
                                websiteReference = subReference.child("g_businessWebsite");
                                webPageReference = websiteReference.child("value");
                                webPageReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String website = dataSnapshot.getValue(String.class);
                                        Log.d("myTagName", website);
                                        postWebsite = website;
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                Intent six = new Intent(Intent.ACTION_VIEW, Uri.parse(postWebsite));
                                startActivity(six);
                        }
                    }
                });

            }

            @Override
            public BusContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                BusContentHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                return viewHolder;
            }
        };
        busRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), StoreDirectory.class);
        getApplicationContext().startActivity(intent);
    }

    public static class BusContentHolder extends RecyclerView.ViewHolder {
        View view;
        public  BusContentHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setTitle(String title) {
            //Log.d("myTagName", title);
            TextView post_title = view.findViewById(R.id.bus_item_title);
            post_title.setText(title);
        }

        public void setValue(String value) {
            //Log.d("myTagName", value);
            TextView post_value = view.findViewById(R.id.bus_item_value);
            post_value.setText(value);
        }

    }
}
