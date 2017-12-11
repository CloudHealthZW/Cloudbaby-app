package app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.activities.ChatConversation;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.models.RealmUserModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * @author Kudzai Chasinda
 */

public class RealmContactsAdapter extends RecyclerView.Adapter<RealmContactsAdapter.ItemHolder> {

    private Context context;
    private LayoutInflater inflater;
    private RealmResults<RealmUserModel> results;

    public RealmContactsAdapter(Context context, RealmResults<RealmUserModel> results) {
        this.context = context;
        update(results);
        inflater = LayoutInflater.from(context);

    }

    public void update(RealmResults<RealmUserModel> results) {
        this.results = results;
        notifyDataSetChanged();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.item_single_realm_contact, parent, false);
        return new ItemHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        RealmUserModel user = results.get(position);
        holder.usernameText.setText(user.getFirstName());

        setUpViewHolderClickListener(holder, user);

    }

    private void setUpViewHolderClickListener(final ItemHolder holder, final RealmUserModel user) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatConversation.class);
                intent.putExtra("reciverUserName", user.getFirstName());
                intent.putExtra("reciverUid", user.getUserId());
                intent.putExtra("reciverProfilePic", user.getProfileImageUri());
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (results != null ? results.size() : 0);
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.user_name_text_view)
        TextView usernameText;
        @BindView(R.id.contact_image)
        ImageView contactImageView;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
