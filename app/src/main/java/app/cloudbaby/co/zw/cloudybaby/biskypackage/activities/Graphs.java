package app.cloudbaby.co.zw.cloudybaby.biskypackage.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.application.MyApplication;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.Config;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.DatesFormarts;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Graphs extends AppCompatActivity {
    String infantId;
    private RequestQueue requestQueue;
    private String URL = Config.BASE_URL;
    private ArrayList<GraphModel> list;
    private ArrayList<GraphModel> listHeight;
    private ArrayList<GraphModel> listHead;
    private GraphView weightGraph, heightGraph, headGraph;

    @BindView(R.id.pgrLoadWeight)
    ProgressBar weightLoad;


    @BindView(R.id.pgrLoadHeight)
    ProgressBar heightLoad;


    @BindView(R.id.pgrLoadHead)
    ProgressBar headLoad;
    private int mNumLabels;

    @BindView(R.id.app_bar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Graphs");

        infantId = getIntent().getExtras().getString("infantId");

        requestQueue = MyApplication.getRequestQueue();
        list = new ArrayList<>();
        listHeight = new ArrayList<>();
        listHead = new ArrayList<>();

        weightGraph = (GraphView) findViewById(R.id.grpWeight);
        heightGraph = (GraphView) findViewById(R.id.grpHeight);
        headGraph = (GraphView) findViewById(R.id.grpHead);

        loadWeightValues(infantId);
        loadHeadValues(infantId);
        loadHeightValues(infantId);
    }


    private void loadWeightValues(String infantId) {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL + "infantWeight.php?infantId=" + infantId, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                // Log.e("data", "data >>> \n" + s);
                weightLoad.setVisibility(View.INVISIBLE);
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject != null) {

                        JSONArray jsonArray = jsonObject.getJSONArray("items");
                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {


                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                                String measurements = jsonObject1.getString("measurement");
                                String date = jsonObject1.getString("date");

                                list.add(new GraphModel(date, measurements));

                            }
                            initGraph(weightGraph, list,"Weight","Weight");

                        } else {

                        }
                    } else {

                    }

                } catch (JSONException e) {
                    weightLoad.setVisibility(View.INVISIBLE);
                    Log.e("jsonerro", e.toString());

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {


            }
        });
        requestQueue.add(stringRequest);


    }

    private void loadHeightValues(String infantId) {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL + "infantHeight.php?infantId=" + infantId, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                // Log.e("data", "data >>> \n" + s);
                heightLoad.setVisibility(View.INVISIBLE);
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject != null) {

                        JSONArray jsonArray = jsonObject.getJSONArray("items");
                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {


                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                                String measurements = jsonObject1.getString("measurement");
                                String date = jsonObject1.getString("date");

                                listHeight.add(new GraphModel(date, measurements));

                            }
                            initGraph(heightGraph, listHeight,"Height","Height");

                        } else {

                        }
                    } else {

                    }

                } catch (JSONException e) {
                    heightLoad.setVisibility(View.INVISIBLE);
                    Log.e("jsonerro", e.toString());

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {


            }
        });
        requestQueue.add(stringRequest);


    }


    private void loadHeadValues(String infantId) {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL + "infantHeadCircumference.php?infantId=" + infantId, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                // Log.e("data", "data >>> \n" + s);
                headLoad.setVisibility(View.INVISIBLE);
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject != null) {

                        JSONArray jsonArray = jsonObject.getJSONArray("items");
                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {


                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                                String measurements = jsonObject1.getString("measurement");
                                String date = jsonObject1.getString("date");

                                listHead.add(new GraphModel(date, measurements));

                            }
                            initGraph(headGraph, listHead,"Head Circumference","Head Measurements");

                        } else {

                        }
                    } else {

                    }

                } catch (JSONException e) {
                    headLoad.setVisibility(View.INVISIBLE);
                    Log.e("jsonerro", e.toString());

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {


            }
        });
        requestQueue.add(stringRequest);


    }

    void initGraph(GraphView graph, ArrayList<GraphModel> list,String title,String xAxis) {

        Date d1 = null, d3 = null;
        mNumLabels = list.size();
        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>();
        for (int i = 0; i < list.size(); i++) {


            series1.appendData(new DataPoint(DatesFormarts.getDateForGraph(list.get(i).getDate()), Float.parseFloat(list.get(i).getWeight())), true, 100);
        }

        series1.setTitle(title);
        series1.setDrawBackground(true);
        series1.setColor(Color.argb(100, 100, 60, 60));
        series1.setBackgroundColor(Color.argb(50, 110, 199, 119));
        series1.setDrawDataPoints(true);




        graph.addSeries(series1);
        // legend
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        graph.getGridLabelRenderer().setVerticalAxisTitle(xAxis);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Dates(Month-Day-Year)");

        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graph.getContext()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(mNumLabels);




    }


    class GraphModel {
        String date, weight;

        public GraphModel(String date, String weight) {
            this.date = date;
            this.weight = weight;
        }

        public String getDate() {
            return date;
        }

        public String getWeight() {
            return weight;
        }
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
