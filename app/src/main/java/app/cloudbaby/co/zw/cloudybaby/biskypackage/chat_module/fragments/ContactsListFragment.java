package app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.adapters.ContactsAdapter;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.helpers.ContactsHelper;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.model.UserModel;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.tools.progressbars.CustomLoaderDialog;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.PreferencesUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Kudzai Chasinda
 */

public class ContactsListFragment extends Fragment {

    //P.S Read Age of Vectors by Chris Banes
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }


    private static final String TAG = "UserList";
    private FirebaseDatabase database;
    private RecyclerView my_recycler_view;
    private LinearLayoutManager mLayoutManager;
    private CustomLoaderDialog customeLoaderDialog;
    private ContactsAdapter mAdapter;
    private ContactsHelper mHelper;

    public static int from = 0;

    //Start to get the preferences here
    private PreferencesUtils mUtils;

    @BindView(R.id.app_bar)
    Toolbar appBar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            from = bundle.getInt("from");// from = 1 -> ChatConversationList , from = 2 -> GroupChatConversation
            getActivity().setTitle("Select Contact");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_userlist_activity, container, false);
        ButterKnife.bind(this, view);
        setupToolbar(appBar);
        initObjects();

        my_recycler_view = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        my_recycler_view.setLayoutManager(mLayoutManager);
        database = FirebaseDatabase.getInstance();

        getUserList();

        return view;
    }

    private void setupInitialData() {

    }

    private void initObjects() {
        mUtils = PreferencesUtils.getInstance(getActivity());
        mHelper = ContactsHelper.newInstance(getActivity());
    }

    private void getUserList() {

        List<UserModel> usersList = mHelper.displayList(getActivity());
        mAdapter = new ContactsAdapter(usersList, getActivity());


        my_recycler_view.setHasFixedSize(true);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        my_recycler_view.setAdapter(mAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (database != null) {
            final DatabaseReference firebase = database.getReference().child("users").child(mUtils.getUserId()).child("status");
            firebase.setValue("offline");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (database != null) {
            final DatabaseReference firebase = database.getReference().child("users").child(mUtils.getUserId()).child("status");
            firebase.setValue("online");
        }
    }

    private void setupToolbar(Toolbar mAppBar) {
        if (mAppBar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(mAppBar);
            getActivity().setTitle("Contacts");
            final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void setUpFirebaseAdapter() {
        //Sets up our adapter
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

