package app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.activities.ContactsActivity;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.adapters.UserAdapter;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.model.ChatUserModel;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.model.UserModel;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.viewholders.FirebaseChatUserViewHolder;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.tools.progressbars.CustomLoaderDialog;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.Constants;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.GeneralUtils;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.PreferencesUtils;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.Prefs;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Kudzai Chasinda
 */

public class ChatsFragment extends Fragment {
    //P.S Read Age of Vectors by Chris Banes
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.layout_empty_state)
    LinearLayout emptyState;

    @BindView(R.id.emptyStateImage)
    ImageView emptyStateImageView;

    @BindView(R.id.emptyStateText)
    TextView emptyStateTextView;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_chatlist_activity, container, false);
        ButterKnife.bind(this, rootView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ContactsActivity.class);
                intent.putExtra("from", 1);
                startActivity(intent);
            }
        });


        mUtils = PreferencesUtils.getInstance(getActivity());
        mAuth = FirebaseAuth.getInstance();
        my_recycler_view = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        /*cardNoData = (CardView) findViewById(R.id.cardNoData);*/
        my_recycler_view.setLayoutManager(mLayoutManager);
        database = FirebaseDatabase.getInstance();

        Glide.with(this)
                .load(R.drawable.chat_empty)
                .crossFade()
                .into(emptyStateImageView);
        emptyStateTextView.setText("No chats yet, to begin select a contact to chat with from contacts :)");

        GeneralUtils.USER_ID = mUtils.getUserId();
        forOfflineStore();


        setUpFirebaseAdapter();

        return rootView;
    }


    private void forOfflineStore() {
        if (!Prefs.isPersisrence(getActivity())) {
            try {
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                Prefs.setPersistence(getActivity(), true);
            } catch (Exception e) {

            }

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

    @Override
    public void onPause() {
        super.onPause();
        if (database != null) {
            final DatabaseReference firebase = database.getReference().child("users").child(mUtils.getUserId()).child("status");
            firebase.setValue("offline");
        }
    }

    private void setUpFirebaseAdapter() {
        if (customeLoaderDialog == null)
            customeLoaderDialog = new CustomLoaderDialog(getActivity());
        customeLoaderDialog.show(true);
        DatabaseReference databaseReference = database.getReference().child(Constants.CHAT_CONVERSATIONE_BADGE).child(mUtils.getUserId());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    emptyState.setVisibility(View.VISIBLE);
                    customeLoaderDialog.hide();
                } else {
                    emptyState.setVisibility(View.GONE);
                    customeLoaderDialog.hide();//// TODO: 7/2/2017  check
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
                emptyState.setVisibility(View.GONE);
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
        my_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        my_recycler_view.setAdapter(mFirebaseAdapter);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAddChatMember:
                Intent intent = new Intent(getActivity(), ContactsActivity.class);
                intent.putExtra("from", 1);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }


}
