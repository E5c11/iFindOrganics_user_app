package activities;

import android.os.Bundle;

import com.ifo.userapp.BaseActivity;
import com.ifo.userapp.R;


public class About extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_about, baseLayout);



    }

}
