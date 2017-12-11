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
import app.cloudbaby.co.zw.cloudybaby.biskypackage.models.ModelVaccination;

/**
 * Created by bisky on 6/26/2017.
 */

public class VaccinationAdapter extends RecyclerView.Adapter<VaccinationAdapter.NoticesHolder> {


    private ArrayList<ModelVaccination> list;
    private Context context;
    private LayoutInflater inflater;


    public VaccinationAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public NoticesHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = inflater.inflate(R.layout.row_vaccination, parent, false);

        return new NoticesHolder(view);
    }

    @Override
    public void onBindViewHolder(NoticesHolder holder, int position) {
        ModelVaccination notice = list.get(position);

        holder.drug.setText(notice.getDrugAdministered());
        holder.centreId.setText(notice.getCentreId());
        holder.details.setText(notice.getDescription());
        holder.against.setText(notice.getVaccination());
        holder.date.setText(notice.getDate());
        holder.dosage.setText(notice.getAmount());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NoticesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView drug, centreId, details, against, date, dosage;


        View view;


        public NoticesHolder(View itemView) {
            super(itemView);
            view = itemView;
            drug = (TextView) itemView.findViewById(R.id.txtDrug);
            centreId = (TextView) itemView.findViewById(R.id.txtCentreId);
            details = (TextView) itemView.findViewById(R.id.txtDetails);
            against = (TextView) itemView.findViewById(R.id.txtVaccineAgainst);
            date = (TextView) itemView.findViewById(R.id.txtPrescribedDate);
            dosage = (TextView) itemView.findViewById(R.id.txtDosage);


            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


        }
    }

    public void setList(ArrayList<ModelVaccination> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}

