package app.cloudbaby.co.zw.cloudybaby.biskypackage.adapters;

/**
 * Created by Bisky on 6/25/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.activities.InfantsOptions;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.fragments.FragmentClientProfile;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.models.ModelInfant;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.Config;

public class InfantsAdapter extends RecyclerView.Adapter<InfantsAdapter.NoticesHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ModelInfant> list;

    public InfantsAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public NoticesHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.row_infant, parent, false);

        return new NoticesHolder(view);
    }

    @Override
    public void onBindViewHolder(NoticesHolder holder, int position) {

        ModelInfant notice = list.get(position);

        holder.names.setText(notice.getNames());
        holder.type.setText(notice.getType());
        Picasso.with(context).load(Config.BASE_URL + notice.getProfilePhoto()).placeholder(R.drawable.placeholder).into(holder.profilePhoto);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NoticesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView names, type;
        ImageView profilePhoto;

        View view;
        RelativeLayout relativeLayout;

        public NoticesHolder(View itemView) {
            super(itemView);
            view = itemView;
            names = (TextView) itemView.findViewById(R.id.txtNames);

            type = (TextView) itemView.findViewById(R.id.txtType);
            profilePhoto = (ImageView) itemView.findViewById(R.id.imgProfilePic);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, InfantsOptions.class);
            intent.putExtra("id", list.get(getAdapterPosition()).getId());

            context.startActivity(intent);

        }
    }

    public void setList(ArrayList<ModelInfant> list) {
        this.list = list;
        notifyDataSetChanged();
    }


}

