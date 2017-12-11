package app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.adapters.RealmContactsAdapter;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.helpers.ContactsHelper;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.models.RealmUserModel;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.tools.progressbars.CustomLoaderDialog;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.GeneralUtils;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.PreferencesUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * @author Kudzai Chasinda
 */

public class ContactsActivity extends AppCompatActivity {

    //P.S Read Age of Vectors by Chris Banes
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static final String TAG = "CONTACTSACTIVITY";

    @BindView(R.id.my_recycler_view)
    RecyclerView contactsRecyclerView;
    @BindView(R.id.app_bar)
    Toolbar appBar;
    private LinearLayoutManager mLayoutManager;
    private CustomLoaderDialog customeLoaderDialog;

    private static final String FRAGMENT_TAG = "ContactsFragment";

    //Start to get the preferences here
    private PreferencesUtils mUtils;
    private ContactsHelper mContactsHelper;

    private RealmContactsAdapter mAdapter;
    private Realm realm;
    private RealmResults<RealmUserModel> mResults;

    //TODO empty state here
    @BindView(R.id.emptyStateLayout)
    LinearLayout emptyStateLayout;
    @BindView(R.id.emptyStateText)
    TextView emptyStateTextView;
    @BindView(R.id.emptyStateImage)
    ImageView emptyStateImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);

        mLayoutManager = new LinearLayoutManager(this);
        contactsRecyclerView.setLayoutManager(mLayoutManager);
        setupToolbar(appBar);

        mUtils = PreferencesUtils.getInstance(this);

        GeneralUtils.USER_ID = mUtils.getUserId();

        realm = Realm.getDefaultInstance();
        mResults = realm.where(RealmUserModel.class).findAllAsync();
        mResults = mResults.sort("firstName", Sort.ASCENDING);

        if (mResults.isEmpty()) {
            emptyStateImageView.setVisibility(View.VISIBLE);
            emptyStateTextView.setText("Looks like you don't have any contacts on Cloud Baby yet.\nTry clicking Refresh.");
            //TODO Glide.with(this).load(<Img>).into(ImageView);
            contactsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyStateLayout.setVisibility(View.GONE);
            contactsRecyclerView.setVisibility(View.VISIBLE);
        }

        mAdapter = new RealmContactsAdapter(this, mResults);
        contactsRecyclerView.setAdapter(mAdapter);
    }

    private void refresh() {
        final ProgressDialog pd = new ProgressDialog(ContactsActivity.this);
        pd.setTitle("Refreshing");
        pd.setCancelable(false);
        pd.setMessage("Refreshing Contacts List");
        pd.show();
        new AsyncTask<Void, Void, Void>() {


            @Override
            protected Void doInBackground(Void... params) {

                mContactsHelper = ContactsHelper.newInstance(ContactsActivity.this);
                mContactsHelper.displayList(ContactsActivity.this);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                pd.dismiss();

            }
        }.execute();
    }

    private RealmChangeListener mChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            mAdapter.update(mResults);
        }
    };

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.refresh_contacts:
                refresh();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Add our change listener for changes in the db
        mResults.addChangeListener(mChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Remove our listener
        mResults.removeChangeListener(mChangeListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contacts, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void setupToolbar(Toolbar mAppBar) {
        if (mAppBar != null) {
            setSupportActionBar(mAppBar);
            setTitle("Select Contact");
            final ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
