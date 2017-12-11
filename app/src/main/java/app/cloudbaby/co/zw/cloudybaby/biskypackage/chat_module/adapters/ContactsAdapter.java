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

import com.squareup.picasso.Picasso;

import java.util.List;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.activities.ChatConversation;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.fragments.ContactsListFragment;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.model.UserModel;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.tools.BadgeView;

/**
 * @author Kudzai Chasinda
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ItemHolder> {
    private List<UserModel> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;

    public ContactsAdapter(List<UserModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.row_user_list, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        UserModel userModel = arrayList.get(position);
        //Bind the view together
        holder.badgeChat.setVisibility(View.GONE);
        Picasso.with(context)
                .load(userModel.getProfileImageUri())
                .resize(MAX_WIDTH, MAX_HEIGHT)
                .centerCrop()
                .into(holder.imgUser);
        holder.tvName.setText(userModel.getFirstName());
        holder.tvStatus.setText(userModel.getStatus());
        holder.badgeChat.setText("" + userModel.getBadge());

        setUpViewHolderOnClickeListener(holder, userModel);
    }

    private void setUpViewHolderOnClickeListener(final ItemHolder holder, final UserModel user) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContactsListFragment.from == 1) { //incase we ever added a group chat I think this is how we'd know
                    Intent intent = new Intent(context, ChatConversation.class);
                    intent.putExtra("reciverUserName", user.getFirstName());
                    intent.putExtra("reciverUid", user.getUserId());
                    intent.putExtra("reciverProfilePic", user.getProfileImageUri());
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    protected class ItemHolder extends RecyclerView.ViewHolder {

        protected ImageView imgUser;
        protected TextView tvName, tvStatus;
        protected BadgeView badgeChat;

        public ItemHolder(View itemView) {
            super(itemView);
            imgUser = (ImageView) itemView.findViewById(R.id.imgUser);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            badgeChat = (BadgeView) itemView.findViewById(R.id.badgeChat);


        }
    }
}
