package app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.adapters.UserAdapter;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.model.ChatUserModel;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.model.UserModel;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.viewholders.FirebaseChatUserViewHolder;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.tools.progressbars.CustomLoaderDialog;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.Constants;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.GeneralUtils;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.PreferencesUtils;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.Prefs;

/**
 * @author Kudzai Chasinda
 *         Essentially the messages activity
 *         TODO rename to something a little bit more proper
 */

public class ChatConversationList extends AppCompatActivity implements View.OnClickListener {
    //P.S Read Age of Vectors by Chris Banes
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }


    private static final String TAG = "UserList";
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private ArrayList<UserModel> arrayUser;
    private UserAdapter userAdapter;
    private RecyclerView my_recycler_view;
    private LinearLayoutManager mLayoutManager;
    private CustomLoaderDialog customeLoaderDialog;
    private FirebaseRecyclerAdapter<ChatUserModel, FirebaseChatUserViewHolder> mFirebaseAdapter;


    private PreferencesUtils mUtils;

   // private FirebaseUser user;
/*    private CardView cardNoData;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chatlist_activity);

        initObjects();
        setUpFirebaseAdapter();
       /* getUserList();*/
    }

    private void initObjects() {
        mUtils = PreferencesUtils.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        /*cardNoData = (CardView) findViewById(R.id.cardNoData);*/
        my_recycler_view.setLayoutManager(mLayoutManager);
        database = FirebaseDatabase.getInstance();

        getSupportActionBar().setTitle("Chat List");
        GeneralUtils.USER_ID = mUtils.getUserId();
        forOfflineStore();

    }

    private void forOfflineStore() {
        if (!Prefs.isPersisrence(ChatConversationList.this)) {
            try {
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                Prefs.setPersistence(ChatConversationList.this, true);
            } catch (Exception e) {

            }

        }
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

    private void setUpFirebaseAdapter() {
        if (customeLoaderDialog == null)
            customeLoaderDialog = new CustomLoaderDialog(this);
        customeLoaderDialog.show(true);
        DatabaseReference databaseReference = database.getReference().child(Constants.CHAT_CONVERSATIONE_BADGE).child(mUtils.getUserId());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    customeLoaderDialog.hide();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mFirebaseAdapter = new FirebaseRecyclerAdapter<ChatUserModel, FirebaseChatUserViewHolder>
                (ChatUserModel.class, R.layout.row_user_list, FirebaseChatUserViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(FirebaseChatUserViewHolder viewHolder, ChatUserModel model, int position) {
                customeLoaderDialog.hide();
                viewHolder.bindUser(model);

            }

            @Override
            public boolean onFailedToRecycleView(FirebaseChatUserViewHolder holder) {
                customeLoaderDialog.hide();

                return super.onFailedToRecycleView(holder);
            }

        };
        my_recycler_view.setHasFixedSize(true);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        my_recycler_view.setAdapter(mFirebaseAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.menuAddChatMember:
//                Intent intent = new Intent(ChatConversationList.this, UserList.class);
//                intent.putExtra("from", 1);
//                startActivity(intent);
//                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
