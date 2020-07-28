package activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.ifo.userapp.BaseActivity;
import com.ifo.userapp.R;

import adaptersandmore.StorePagerAdapter;
import storesections.Cosmetics;
import storesections.Foods;
import storesections.Medicinal;

public class StoreDirectory extends BaseActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private StorePagerAdapter storePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_directory, baseLayout);

        drawerList.setItemChecked(position, true);
        //setTitle("StoreDirectory");
        page = 1;

        toolbar = findViewById(R.id.direct_toolbar);
        setSupportActionBar(toolbar);
        tabLayout = findViewById(R.id.store_tab);
        viewPager = findViewById(R.id.store_pager);
        storePagerAdapter = new StorePagerAdapter(getSupportFragmentManager());
        storePagerAdapter.addFragments(new Foods(), "Foods");
        storePagerAdapter.addFragments(new Cosmetics(), "Cosmetics");
        storePagerAdapter.addFragments(new Medicinal(), "Medicinal");
        viewPager.setAdapter(storePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}
