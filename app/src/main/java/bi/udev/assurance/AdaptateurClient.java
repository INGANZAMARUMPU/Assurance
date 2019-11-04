package bi.udev.assurance;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import bi.udev.assurance.Client;
import bi.udev.assurance.R;

/**
 * Created by KonstrIctor on 01/11/2019.
 */
class AdaptateurClient extends RecyclerView.Adapter<AdaptateurClient.ViewHolder> {

    Context context;
    ArrayList<Client> clients;

    public AdaptateurClient(Context context, ArrayList<Client> clients) {
        this.context = context;
        this.clients = clients;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_client, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.lbl_tel_client.setText(clients.get(position).tel);
        holder.lbl_nom_client.setText(clients.get(position).nom+" "+clients.get(position).prenom);
        holder.lbl_email_client.setText(clients.get(position).email);
        holder.lbl_CNI.setText(clients.get(position).CNI);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ClientActivity.class);
                intent.putExtra("id", clients.get(position).id);
                intent.putExtra("nom", clients.get(position).nom);
                intent.putExtra("prenom", clients.get(position).prenom);
                intent.putExtra("CNI", clients.get(position).CNI);
                intent.putExtra("avatar", clients.get(position).avatar);
                intent.putExtra("email", clients.get(position).email);
                intent.putExtra("tel", clients.get(position).tel);
                intent.putExtra("depuis", clients.get(position).depuis);
                intent.putExtra("ajouter", "false");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView lbl_CNI, lbl_nom_client, lbl_email_client, lbl_tel_client;
        public View view;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.view = itemView;
            lbl_CNI = (TextView) itemView.findViewById(R.id.lbl_CNI);
            lbl_email_client = (TextView) itemView.findViewById(R.id.lbl_email_client);
            lbl_nom_client = (TextView) itemView.findViewById(R.id.lbl_nom_client);
            lbl_tel_client = (TextView) itemView.findViewById(R.id.lbl_tel_client);
        }
    }
}