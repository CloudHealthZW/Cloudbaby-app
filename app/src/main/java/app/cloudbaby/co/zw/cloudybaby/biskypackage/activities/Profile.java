package app.cloudbaby.co.zw.cloudybaby.biskypackage.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.fragments.FragmentClientProfile;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.fragments.FragmentSpouseProfile;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class Profile extends AppCompatActivity implements MaterialTabListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //working on tabs

        tabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

// Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
            }
        });
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {

            tabHost.addTab(tabHost.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));


        }
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        Fragment fragment = null;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            FragmentClientProfile infantsPrag = new FragmentClientProfile();
            FragmentSpouseProfile fragmentChat = new FragmentSpouseProfile();

            if (position == 0) {
                fragment = infantsPrag;

            } else if (position == 1) {
                fragment = fragmentChat;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Personal Information";
                case 1:
                    return "Spouse Information";

            }
            return null;
        }
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private MaterialTabHost tabHost;
    private ViewPager mViewPager;
}
