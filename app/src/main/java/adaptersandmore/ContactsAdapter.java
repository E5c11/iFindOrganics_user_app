package AdaptersandMore;

import android.content.Context;
import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ifo.hostapp.R;

import java.util.ArrayList;

import BusinessDetailsQuestions.BusinessProducts;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private ArrayList<String> contactUs;
    private LayoutInflater inflater;
    private Context context;

    // data is passed into the constructor
    public ContactsAdapter(Context context, ArrayList<String> contactUs) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.contactUs = contactUs;
    }

    // inflates the row layout from xml when needed
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.general_row, parent, false);
        return new ContactsAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ContactsAdapter.ViewHolder holder, final int position) {
        final String tempItem = contactUs.get(position);
        holder.myTextName.setText(tempItem);
        if (position % 2 == 0) {
            //holder.myTextName.setText(tempItem);
            holder.myTextName.setTextColor(context.getResources().getColor(R.color.colorPrimaryGrey));
            holder.myTextName.setEnabled(false);
        } else if (URLUtil.isValidUrl(tempItem)){
           // holder.myTextContent.setText(tempItem);
            holder.myTextName.setMovementMethod(LinkMovementMethod.getInstance());
            holder.myTextName.setTextColor(Color.BLACK);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return contactUs.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextName, myTextContent;

        ViewHolder(View itemView) {
            super(itemView);
            myTextName = itemView.findViewById(R.id.spinner_row);
            //myTextContent = itemView.findViewById(R.id.contact_info);
        }
    }
}
