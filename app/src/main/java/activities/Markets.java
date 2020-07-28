package activities;

import android.os.Bundle;

import com.ifo.userapp.BaseActivity;
import com.ifo.userapp.R;

public class Markets extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         *  We will not use setContentView in this activty
         *  Rather than we will use layout inflater to add view in FrameLayout of our base activity layout*/

        /**
         * Adding our layout to parent class frame layout.
         */
        getLayoutInflater().inflate(R.layout.activity_main, baseLayout);

        /**
         * Setting title and itemChecked
         */
        drawerList.setItemChecked(position, true);
        //setTitle("Markets");
        page = 2;

        //(findViewById(R.id.ad_content)).setBackgroundResource(R.drawable.pic5);
    }

}
