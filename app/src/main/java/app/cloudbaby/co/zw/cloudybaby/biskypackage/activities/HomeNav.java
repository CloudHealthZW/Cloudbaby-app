package app.cloudbaby.co.zw.cloudybaby.biskypackage.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.application.MyApplication;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.fragments.ChatsFragment;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.fragments.TipsFrament;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.fragments.FragmentInfantsPrag;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.intro.IntroActivity;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.Config;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.Constants;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.PreferencesUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeNav extends RuntimePermissionsActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.app_bar)
    Toolbar toolbar;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    private MainPagerAdapter pagerAdapter;
    private RequestQueue requestQueue;

    private PreferencesUtils mUtils;
    private static final int REQUEST_PERMISSIONS = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_nav);
        ButterKnife.bind(this);

        //Request the permissions
        HomeNav.super.requestAppPermissions(new
                        String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, R.string.runtime_permissions_txt
                , REQUEST_PERMISSIONS);


        setSupportActionBar(toolbar);
        requestQueue = MyApplication.getRequestQueue();
        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);
        setTitle("Cloud Baby");

        mUtils = PreferencesUtils.getInstance(this);

        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        TextView name = (TextView) hView.findViewById(R.id.txtNavName);
        ImageView profilePhoto = (ImageView) hView.findViewById(R.id.imgNavProfile);

        name.setText(MyApplication.getName());

        Picasso.with(this).load(Config.BASE_URL + "clientProfile/" + MyApplication.getParentId() + ".jpg").placeholder(R.drawable.ic_people).into(profilePhoto);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        getToken();
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    private void sendTokenToserver(final String token) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                if (token != null) {
                    //// TODO: 5/6/2017  register token
                    registerFone(token);
                    Log.e("Token ", ">>>>>>>>" + token);
                } else {
                    // Log.e("Token ", token + " <<<<>>>>");
                }


            }
        }).start();

    }

    private void registerFone(final String token) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "updateToken.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.e("debug", s);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {


                Log.e("debug", "" + volleyError);
                // Toast.makeText(getBaseContext(), "Check Internet Connectivity", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("token", token);
                map.put("username", MyApplication.getParentId());


                return map;
            }
        };
        requestQueue.add(stringRequest);

        //Firebase Update with token
        if (!(mUtils.getUserId().equals(""))) {
            mUtils.setPrefsFirebaseToken(token);
            FirebaseDatabase.getInstance()
                    .getReference()
                    .child(Constants.CHAT_USER_BADGE)
                    .child(mUtils.getUserId())
                    .child(Constants.CHAT_FIREBASE_TOKEN)
                    .setValue(token);
        }

    }

    private String getToken() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                String token = FirebaseInstanceId.getInstance().getToken();

                sendTokenToserver(token);

                Log.e("Token", token);
            }
        }).start();
        return null;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(getBaseContext(), Profile.class));
        } else if (id == R.id.nav_signout) {
            MyApplication.clearShared(getBaseContext());
            startActivity(new Intent(getBaseContext(), IntroActivity.class));
            finish();
        } else if (id == R.id.nav_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.cloudbaby_share_message));
            startActivity(Intent.createChooser(shareIntent, "Tell Someone About Cloud Baby"));
        }else if(id == R.id.nav_emergency){
            startActivity(new Intent(getBaseContext(),EmergencyServicesActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menuAddChatMember:
//
//                break;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }
}

class MainPagerAdapter extends FragmentStatePagerAdapter {
    //Titles for our tabs are set here.
    String titles[] = {"Infants", "Chats", "Tips"};

    public MainPagerAdapter(FragmentManager fm) {

        super(fm);
    }

    //A callback for the tab titles.
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new FragmentInfantsPrag();
                break;
            case 1:
                fragment = new ChatsFragment();
                break;
            case 2:
                fragment = new TipsFrament();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

}
