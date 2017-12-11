package app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.adapters.UserAdapter;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.helpers.ContactsHelper;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.model.UserModel;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.viewholders.FirebaseUserViewHolder;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.tools.progressbars.CustomLoaderDialog;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.GeneralUtils;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.PreferencesUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Kudzai Chasinda
 *         UserListing activity
 *         TODO rename to something a little more proper
 */

public class UserList extends AppCompatActivity {
    //P.S Read Age of Vectors by Chris Banes
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }


    private static final String TAG = "UserList";

    private FirebaseDatabase database;
    private ArrayList<UserModel> arrayUser;
    private UserAdapter userAdapter;
    private RecyclerView my_recycler_view;
    private LinearLayoutManager mLayoutManager;
    private CustomLoaderDialog customeLoaderDialog;
    private FirebaseRecyclerAdapter<UserModel, FirebaseUserViewHolder> mFirebaseAdapter;
    public static int from = 0;

    private ContactsHelper helper;
    @BindView(R.id.app_bar)
    Toolbar toolbar;

    //Start to get the preferences here
    private PreferencesUtils mUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_userlist_activity);
        ButterKnife.bind(this);


        initObjects();
        setUpinitialData();
       /* getUserList();*/
    }

    private void setUpinitialData() {
        DatabaseReference databaseReference = database.getReference().child("users");
        Query query = databaseReference.limitToFirst(50); // for the first 50 user from users node
        if (customeLoaderDialog == null)
            customeLoaderDialog = new CustomLoaderDialog(this);
            customeLoaderDialog.show(false);
        setUpFirebaseAdapter(query);

    }

    private void initObjects() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUtils = PreferencesUtils.getInstance(this);

        GeneralUtils.USER_ID = mUtils.getUserId();
        my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        my_recycler_view.setLayoutManager(mLayoutManager);
        database = FirebaseDatabase.getInstance();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            from = bundle.getInt("from");// from = 1 -> ChatConversationList , from = 2 -> GroupChatConversation
            setTitle("Select Friend");
        }
    }

    private void getUserList() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (database != null) {
            final DatabaseReference firebase = database.getReference().child("users").child(mUtils.getUserId()).child("status");
            firebase.setValue("online");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (database != null) {
            final DatabaseReference firebase = database.getReference().child("users").child(mUtils.getUserId()).child("status");
            firebase.setValue("offline");
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*getMenuInflater().inflate(R.menu.user_activity, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);*/
        return true;
    }


    private void setUpFirebaseAdapter(Query query) {

        mFirebaseAdapter = new FirebaseRecyclerAdapter<UserModel, FirebaseUserViewHolder>
                (UserModel.class, R.layout.row_user_list, FirebaseUserViewHolder.class, query) {
            @Override
            protected void populateViewHolder(FirebaseUserViewHolder viewHolder, UserModel model, int position) {
                customeLoaderDialog.hide();
                viewHolder.bindUser(model);
            }
        };

        my_recycler_view.setHasFixedSize(true);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        my_recycler_view.setAdapter(mFirebaseAdapter);

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

