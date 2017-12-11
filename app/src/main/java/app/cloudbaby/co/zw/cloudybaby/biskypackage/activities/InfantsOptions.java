package app.cloudbaby.co.zw.cloudybaby.biskypackage.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.adapters.OptionsAdapter;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.models.ModelOptions;
import butterknife.BindView;
import butterknife.ButterKnife;

public class InfantsOptions extends AppCompatActivity {
    @BindView(R.id.rcvOptions)
    RecyclerView recyclerView;

    String id;
    private OptionsAdapter adapter;

    private ArrayList<ModelOptions> list;

    @BindView(R.id.app_bar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infants_options);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Options");

        list = new ArrayList<>();
        id = getIntent().getExtras().getString("id");
        adapter = new OptionsAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        loadRecyclerView(id);

    }

    private void loadRecyclerView(String id) {
        String[] titles = {"Graphs", "Ailments", "Vaccinations", "Baby Profile", "Baby Shop"};
        for (int i = 0; i < titles.length; i++) {
            list.add(new ModelOptions(String.valueOf(i), titles[i], id));
        }
        adapter.setList(list);

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
