package app.cloudbaby.co.zw.cloudybaby.biskypackage.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import app.cloudbaby.co.zw.cloudybaby.biskypackage.activities.AddInfant;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.adapters.InfantsAdapter;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.application.MyApplication;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.models.ModelInfant;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.Config;
import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentInfantsPrag extends Fragment implements View.OnClickListener {

    @BindView(R.id.imgbtnRefresh)
    Button refresh;

    @BindView(R.id.txtNoInfant)
    TextView noInfant;

    @BindView(R.id.rcvInfants)
    RecyclerView recyclerView;
    @BindView(R.id.pgbRefresh)
    ProgressBar errorProgress;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private RequestQueue requestQueue;
    private String URL = Config.BASE_URL;
    private InfantsAdapter adapter;

    private ArrayList<ModelInfant> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_fragment_infants_prag, container, false);

        ButterKnife.bind(this, view);
        adapter = new InfantsAdapter(getContext());
        requestQueue = MyApplication.getRequestQueue();
        //   setBackgroundImage();
        list = new ArrayList<>();

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                errorProgress.setVisibility(View.VISIBLE);
                refresh.setVisibility(View.INVISIBLE);
                loadItems();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddInfant.class));
            }
        });

        return view;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();
        list.clear();
        adapter.notifyDataSetChanged();
        loadItems();

    }

    private void loadItems() {

        errorProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL + "loadInfants.php?parentId=" + MyApplication.getParentId(), new Response.Listener<String>() {
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

                                Log.e("data", "data loop >>> \n" + i);
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                //     Log.e("Loading", "loading " + i);

                                String names = jsonObject1.getString("firstname") + " " + jsonObject1.getString("lastname");
                                String type = jsonObject1.getString("type");

                                String profilePhoto = jsonObject1.getString("profilephoto");
                                String id = jsonObject1.getString("id");

                                list.add(new ModelInfant(names, type, profilePhoto, id));

                            }

                            adapter.setList(list);
                        } else {
                            errorProgress.setVisibility(View.INVISIBLE);

                            refresh.setVisibility(View.INVISIBLE);
                            // Toast.makeText(Navigation.this, "No stuff found", Toast.LENGTH_SHORT).show();

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
}
