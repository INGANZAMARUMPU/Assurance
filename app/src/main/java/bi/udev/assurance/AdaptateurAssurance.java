package bi.udev.assurance;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by KonstrIctor on 01/11/2019.
 */
class AdaptateurAssurance extends RecyclerView.Adapter<AdaptateurAssurance.ViewHolder> {

    Context context;
    ArrayList<Assurance> assurances;

    public AdaptateurAssurance(Context context, ArrayList<Assurance> assurances) {
        this.context = context;
        this.assurances = assurances;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_assurance, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.lbl_ass_plaque.setText(assurances.get(position).plaque);
        holder.lbl_ass_date.setText(assurances.get(position).fin);
        holder.lbl_ass_auto.setText(assurances.get(position).materiel);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AssuranceActivity.class);
                intent.putExtra("id", assurances.get(position).id);
                intent.putExtra("no_police", assurances.get(position).no_police);
                intent.putExtra("montant", assurances.get(position).montant);
                intent.putExtra("debut", assurances.get(position).debut);
                intent.putExtra("materiel", assurances.get(position).materiel);
                intent.putExtra("client", assurances.get(position).client);
                intent.putExtra("fin", assurances.get(position).fin);
                intent.putExtra("plaque", assurances.get(position).plaque);
                intent.putExtra("CNI", assurances.get(position).CNI);
                intent.putExtra("ajouter", "false");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return assurances.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView lbl_ass_plaque, lbl_ass_date, lbl_ass_auto;
        public View view;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.view = itemView;
            lbl_ass_auto = (TextView) itemView.findViewById(R.id.lbl_ass_auto);
            lbl_ass_date = (TextView) itemView.findViewById(R.id.lbl_ass_date);
            lbl_ass_plaque = (TextView) itemView.findViewById(R.id.lbl_ass_plaque);
        }
    }
}