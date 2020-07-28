package storesections;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ifo.userapp.R;

import activities.BusinessPage;
import adaptersandmore.BusinessDetails;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Foods extends Fragment {

    private DatabaseReference databaseReference;
    private RecyclerView foodRecyclerView;
    private LinearLayoutManager layoutManager;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    public Foods() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_foods, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Business details");
        databaseReference.keepSynced(true);

        foodRecyclerView = rootview.findViewById(R.id.foods_recycler);
        layoutManager = new LinearLayoutManager(getContext());
        foodRecyclerView.setLayoutManager(layoutManager);
        foodRecyclerView.setHasFixedSize(true);
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BusinessDetails, Foods.ListContentHolder>
                (BusinessDetails.class, R.layout.item_list, Foods.ListContentHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(Foods.ListContentHolder viewHolder, BusinessDetails model, int position) {

                viewHolder.setBusinessName(model.getZ_businessName());
                viewHolder.setBusinessDes(model.getZ_businessDes());
                viewHolder.setBusinessLogo(getContext(), model.getZ_businessLogo());
            }

            @Override
            public ListContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ListContentHolder viewHolder =  super.onCreateViewHolder(parent, viewType);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String dataID = firebaseRecyclerAdapter.getRef(foodRecyclerView.getChildLayoutPosition(view)).getKey();
                        Log.d("myTagFoods", dataID);
                        String food = "food";
                        Log.d("myTagFoods", food);

                        Intent intent = new Intent(getContext(), BusinessPage.class);
                        intent.putExtra("dataURL", dataID);
                        intent.putExtra("sType", food);
                        getContext() .startActivity(intent);
                    }
                });

                return viewHolder;
            }
        };
        foodRecyclerView.setAdapter(firebaseRecyclerAdapter);

        return rootview;
    }

    public static class ListContentHolder extends RecyclerView.ViewHolder {
        View view;
        public  ListContentHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
        public void setBusinessName(String z_businessName) {
            TextView post_business_name = view.findViewById(R.id.business_name_list);
            post_business_name.setText(z_businessName);
        }
        public void setBusinessDes(String z_businessDes) {
            TextView post_business_des = view.findViewById(R.id.business_des_list);
            post_business_des.setText(z_businessDes);
        }
        public  void setBusinessLogo(Context context, String z_businessLogo) {
            CircleImageView post_business_logo = view.findViewById(R.id.business_logo_list);
            Glide.with(context).load(z_businessLogo).into(post_business_logo);
        }
    }
}

