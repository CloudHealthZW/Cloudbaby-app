package app.cloudbaby.co.zw.cloudybaby.biskypackage.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.adapters.VaccinationAdapter;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.application.MyApplication;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.models.ModelVaccination;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.Config;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Vaccination extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.imgbtnRefresh)
    ImageButton refresh;

    @BindView(R.id.txtNoInfant)
    TextView noInfant;

    @BindView(R.id.rcvInfants)
    RecyclerView recyclerView;
    @BindView(R.id.pgbRefresh)
    ProgressBar errorProgress;

    @BindView(R.id.app_bar)
    Toolbar toolbar;


    private RequestQueue requestQueue;
    private String URL = Config.BASE_URL;
    private VaccinationAdapter adapter;
    private ArrayList<ModelVaccination> list;
    private String infantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination);

        ButterKnife.bind(this);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Vaccination");


        infantId = getIntent().getExtras().getString("infantId");

        requestQueue = MyApplication.getRequestQueue();
        list = new ArrayList<>();

        adapter = new VaccinationAdapter(this);
        requestQueue = MyApplication.getRequestQueue();
        //   setBackgroundImage();
        list = new ArrayList<>();

        refresh.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        loadItems(infantId);
    }


    private void loadItems(String infantId) {

        errorProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL + "vaccination.php?infantId=" + infantId, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("data", "data >>> \n" + s);
                refresh.setVisibility(View.INVISIBLE);
                errorProgress.setVisibility(View.INVISIBLE);
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject != null) {

                        JSONArray jsonArray = jsonObject.getJSONArray("items");
                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {


                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                //     Log.e("Loading", "loading " + i);

                                String _id = jsonObject1.getString("_id");
                                String vaccinationAgainst = jsonObject1.getString("vaccinations");
                                String drugAdministered = jsonObject1.getString("drugadministered");
                                String amount = jsonObject1.getString("amount");
                                String details = jsonObject1.getString("description");
                                String date1 = jsonObject1.getString("date");
                                String centreId = jsonObject1.getString("centreid");

                                list.add(new ModelVaccination(_id, vaccinationAgainst, drugAdministered, amount + "oc", details, date1, centreId));

                            }

                            adapter.setList(list);
                        } else {
                            errorProgress.setVisibility(View.INVISIBLE);

                            refresh.setVisibility(View.INVISIBLE);
                            // Toast.makeText(Navigation.this, "No stuff found", Toast.LENGTH_SHORT).show();
                            noInfant.setText("No record found");
                            noInfant.setVisibility(View.VISIBLE);
                        }
                    } else {
                        errorProgress.setVisibility(View.INVISIBLE);

                        //  Log.e("NOTHING FOUND", "EMPTY");
                        //     Toast.makeText(Navigation.this, "No stuff found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {

                    Log.e("jsonerro", e.toString());

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {


                refresh.setVisibility(View.VISIBLE);
                errorProgress.setVisibility(View.INVISIBLE);


            }
        });
        requestQueue.add(stringRequest);

    }

    @Override
    public void onClick(View v) {
        errorProgress.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.INVISIBLE);
        loadItems(infantId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
