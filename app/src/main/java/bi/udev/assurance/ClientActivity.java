package bi.udev.assurance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ClientActivity extends AppCompatActivity {

    String id, nom, prenom, CNI, avatar, email, tel, depuis;
    private SharedPreferences sharedPreferences;
    EditText champ_client_nom, champ_client_prenom, champ_client_CNI,
            champ_client_email, champ_client_tel, champ_client_depuis;
    Button btn_suppr_client, btn_ajout_client, btn_modif_client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        sharedPreferences = ClientActivity.this.getSharedPreferences("user_login", Context.MODE_PRIVATE);
        if(!(sharedPreferences.getBoolean("is_connected", false))) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        String organization = sharedPreferences.getString("organisation", "sans");

        champ_client_nom = (EditText) findViewById(R.id.champ_client_nom);
        champ_client_prenom = (EditText) findViewById(R.id.champ_client_prenom);
        champ_client_CNI = (EditText) findViewById(R.id.champ_client_cni);
        champ_client_email = (EditText) findViewById(R.id.champ_client_email);
        champ_client_tel = (EditText) findViewById(R.id.champ_client_tel);
        champ_client_depuis = (EditText) findViewById(R.id.champ_client_date);
        btn_ajout_client = (Button) findViewById(R.id.btn_ajouter_client);
        btn_suppr_client = (Button) findViewById(R.id.btn_suppr_client);
        btn_modif_client = (Button) findViewById(R.id.btn_modif_client);

        String ajouter = getIntent().getStringExtra("ajouter");

        if(ajouter.equalsIgnoreCase("true")){
            btn_suppr_client.setVisibility(View.INVISIBLE);
            btn_modif_client.setVisibility(View.INVISIBLE);

            champ_client_nom.setFocusable(false);
            champ_client_prenom.setFocusable(false);
            champ_client_CNI.setFocusable(false);
            champ_client_email.setFocusable(false);
            champ_client_tel.setFocusable(false);
            champ_client_depuis.setFocusable(false);

        }else {
            btn_ajout_client.setVisibility(View.INVISIBLE);
            if (!(organization.equalsIgnoreCase("rnp"))){
                btn_suppr_client.setVisibility(View.INVISIBLE);
                btn_modif_client.setVisibility(View.INVISIBLE);
                champ_client_nom.setFocusable(false);
                champ_client_prenom.setFocusable(false);
                champ_client_CNI.setFocusable(false);
                champ_client_email.setFocusable(false);
                champ_client_tel.setFocusable(false);
                champ_client_depuis.setFocusable(false);
            }
            id = getIntent().getStringExtra("id");
            nom = getIntent().getStringExtra("nom");
            prenom = getIntent().getStringExtra("prenom");
            CNI = getIntent().getStringExtra("CNI");
            avatar = getIntent().getStringExtra("avatar");
            email = getIntent().getStringExtra("email");
            tel = getIntent().getStringExtra("tel");
            depuis = getIntent().getStringExtra("depuis");

            champ_client_nom.setText(nom);
            champ_client_prenom.setText(prenom);
            champ_client_CNI.setText(CNI);
            champ_client_email.setText(email);
            champ_client_tel.setText(tel);
            champ_client_depuis.setText(depuis);
        }
    }
}
