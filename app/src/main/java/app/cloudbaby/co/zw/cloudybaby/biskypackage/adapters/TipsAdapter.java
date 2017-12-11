package app.cloudbaby.co.zw.cloudybaby.biskypackage.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import app.cloudbaby.co.zw.cloudybaby.R;

import app.cloudbaby.co.zw.cloudybaby.biskypackage.models.TipsModel;
import io.realm.RealmResults;

/**
 * Created by bisky on 7/12/2017.
 */

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.NoticesHolder> {
    RealmResults<TipsModel> list;
    private Context context;
    private LayoutInflater inflater;


    public TipsAdapter(Context context, RealmResults<TipsModel> list) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public NoticesHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = inflater.inflate(R.layout.row_tips, parent, false);

        return new NoticesHolder(view);
    }

    @Override
    public void onBindViewHolder(NoticesHolder holder, int position) {

        TipsModel model = list.get(position);
        holder.title.setText(model.getTitle());
        holder.description.setText(model.getMessage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NoticesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, description;


        View view;


        public NoticesHolder(View itemView) {
            super(itemView);
            view = itemView;
            title = (TextView) itemView.findViewById(R.id.txtTitleTips);
            description = (TextView) itemView.findViewById(R.id.txtDescriptionTips);


            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


        }
    }

    public void setList(RealmResults<TipsModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
