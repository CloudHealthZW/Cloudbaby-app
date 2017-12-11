package app.cloudbaby.co.zw.cloudybaby.biskypackage.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.activities.Ailments;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.activities.Graphs;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.activities.InfantUpdate;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.activities.Vaccination;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.models.ModelOptions;

/**
 * Created by bisky on 6/25/2017.
 */

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.NoticesHolder> {


    private ArrayList<ModelOptions> list;
    private Context context;
    private LayoutInflater inflater;


    public OptionsAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public NoticesHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = inflater.inflate(R.layout.row_options, parent, false);

        return new NoticesHolder(view);
    }

    @Override
    public void onBindViewHolder(NoticesHolder holder, int position) {
        ModelOptions notice = list.get(position);

        holder.titile.setText(notice.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NoticesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titile;


        View view;


        public NoticesHolder(View itemView) {
            super(itemView);
            view = itemView;
            titile = (TextView) itemView.findViewById(R.id.txtTitle);


            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            String id = list.get(getAdapterPosition()).getId();
            String infantId = list.get(getAdapterPosition()).getInfantId();
            // Toast.makeText(context, "id " + id + "\t infant id " + infantId, Toast.LENGTH_SHORT).show();
            if (id.equalsIgnoreCase("0")) {
                Intent intent = new Intent(context, Graphs.class);
                intent.putExtra("infantId", infantId);

                context.startActivity(intent);

            } else if (id.equalsIgnoreCase("1")) {

                Intent intent = new Intent(context, Ailments.class);
                intent.putExtra("infantId", infantId);

                context.startActivity(intent);

            } else if (id.equalsIgnoreCase("2")) {
                //TODO baby SHop this
                Intent intent = new Intent(context, Vaccination.class);
                intent.putExtra("infantId", infantId);

                context.startActivity(intent);
            } else if (id.equalsIgnoreCase("3")) {
                Intent intent = new Intent(context, InfantUpdate.class);
                intent.putExtra("infantId", infantId);

                context.startActivity(intent);
            } else if (id.equalsIgnoreCase("4")) {
                Toast.makeText(context, "Baby Store Coming Soon", Toast.LENGTH_LONG).show();
            }

        }
    }

    public void setList(ArrayList<ModelOptions> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
