package AdaptersandMore;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ifo.hostapp.R;

import java.util.ArrayList;

public class BusProAdapter extends RecyclerView.Adapter<BusProAdapter.ViewHolder> {

    private ArrayList<String> proRvItems;
    private LayoutInflater inflater;
    private Context context;
    private String type;

    // data is passed into the constructor
    public BusProAdapter(Context context, ArrayList<String> proRvItems, String type) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.proRvItems = proRvItems;
        this.type = type;
    }

    // inflates the row layout from xml when needed
    @Override
    public BusProAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.general_row, parent, false);
        return new BusProAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(BusProAdapter.ViewHolder holder, final int position) {
        final String tempItem = proRvItems.get(position);
        Log.d("myTagrecycle", "" + tempItem + " at " + position);
        holder.myTextView.setText(tempItem);
        if (type.equals("Food")) {
            holder.myTextView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        } else if (type.equals("Medicinal")) {
            holder.myTextView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            holder.myTextView.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return proRvItems.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.spinner_row);
        }
    }
}
