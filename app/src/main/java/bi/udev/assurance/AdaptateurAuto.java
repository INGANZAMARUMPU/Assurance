package bi.udev.assurance;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import bi.udev.assurance.Automobile;
import bi.udev.assurance.R;

/**
 * Created by KonstrIctor on 01/11/2019.
 */
class AdaptateurAuto extends RecyclerView.Adapter<AdaptateurAuto.ViewHolder> {

    Context context;
    ArrayList<Automobile> automobiles;

    public AdaptateurAuto(Context context, ArrayList<Automobile> automobiles) {
        this.context = context;
        this.automobiles = automobiles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_autos, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.lbl_auto_plaque.setText(automobiles.get(position).plaque);
        holder.lbl_auto_chassis.setText(automobiles.get(position).chassis);
        holder.lbl_auto_nom.setText(automobiles.get(position).nom);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AutomobileActivity.class);
                intent.putExtra("id", automobiles.get(position).id);
                intent.putExtra("client", automobiles.get(position).client);
                intent.putExtra("nom", automobiles.get(position).nom);
                intent.putExtra("plaque", automobiles.get(position).plaque);
                intent.putExtra("chassis", automobiles.get(position).chassis);
                intent.putExtra("roues", automobiles.get(position).roues);
                intent.putExtra("CNI", automobiles.get(position).CNI);
                intent.putExtra("ajouter", "false");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return automobiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView lbl_auto_nom, lbl_auto_plaque, lbl_auto_chassis;
        public View view;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.view = itemView;
            lbl_auto_chassis = (TextView) itemView.findViewById(R.id.lbl_auto_chassis);
            lbl_auto_nom = (TextView) itemView.findViewById(R.id.lbl_auto_nom);
            lbl_auto_plaque = (TextView) itemView.findViewById(R.id.lbl_auto_plaque);
        }
    }
}