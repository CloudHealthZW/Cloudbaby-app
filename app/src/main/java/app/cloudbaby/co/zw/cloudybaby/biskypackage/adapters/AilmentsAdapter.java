package app.cloudbaby.co.zw.cloudybaby.biskypackage.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.models.ModelAilments;

/**
 * Created by bisky on 6/25/2017.
 */

public class AilmentsAdapter extends RecyclerView.Adapter<AilmentsAdapter.NoticesHolder> {


    private ArrayList<ModelAilments> list;
    private Context context;
    private LayoutInflater inflater;


    public AilmentsAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public NoticesHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = inflater.inflate(R.layout.row_ailment, parent, false);

        return new NoticesHolder(view);
    }

    @Override
    public void onBindViewHolder(NoticesHolder holder, int position) {
        ModelAilments notice = list.get(position);

        holder.ailment.setText(notice.getAilments());
        holder.description.setText(notice.getDescription());
        holder.type.setText(notice.getType());
        holder.severity.setText(notice.getSeverity());
        holder.date.setText(notice.getDate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NoticesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView ailment, description, type, severity, date;


        View view;


        public NoticesHolder(View itemView) {
            super(itemView);
            view = itemView;
            ailment = (TextView) itemView.findViewById(R.id.txtAilment);
            description = (TextView) itemView.findViewById(R.id.txtDescription);
            type = (TextView) itemView.findViewById(R.id.txtType);
            severity = (TextView) itemView.findViewById(R.id.txtseverity);
            date = (TextView) itemView.findViewById(R.id.txtDDate);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


        }
    }

    public void setList(ArrayList<ModelAilments> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}

