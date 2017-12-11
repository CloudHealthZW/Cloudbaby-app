package app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.viewholders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.activities.ChatConversation;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.activities.UserList;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.model.UserModel;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.tools.BadgeView;

/**
 * @author Kudzai Chasinda
 */

public class FirebaseUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;
    View mView;
    Context mContext;
    UserModel userModel;

    public FirebaseUserViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindUser(UserModel userModel) {
        this.userModel = userModel;
        ImageView imgUser = (ImageView) mView.findViewById(R.id.imgUser);
        TextView tvName = (TextView) mView.findViewById(R.id.tvName);
        TextView tvStatus = (TextView) mView.findViewById(R.id.tvStatus);
        BadgeView badgeChat = (BadgeView) mView.findViewById(R.id.badgeChat);
        badgeChat.setVisibility(View.GONE);
//        Picasso.with(mContext)
//                .load(userModel.getProfileImageUri())
//                .resize(MAX_WIDTH, MAX_HEIGHT)
//                .centerCrop()
//                .into(imgUser);
        tvName.setText(userModel.getFirstName());
        tvStatus.setText(userModel.getStatus());
        badgeChat.setText("" + userModel.getBadge());
    }

    @Override
    public void onClick(View view) {
        if (UserList.from == 1) {
            Intent intent = new Intent(mContext, ChatConversation.class);
            intent.putExtra("reciverUserName", userModel.getFirstName());
            intent.putExtra("reciverUid", userModel.getUserId());
            intent.putExtra("reciverProfilePic", userModel.getProfileImageUri());
            intent.putExtra("receiverToken", userModel.getFirebaseToken());
            mContext.startActivity(intent);
            ((Activity) mContext).finish();
        }

    }

}

