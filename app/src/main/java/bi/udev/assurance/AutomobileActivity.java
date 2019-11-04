package bi.udev.assurance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AutomobileActivity extends AppCompatActivity {

    String id, client, nom, plaque, chassis, roues, CNI;
    EditText champ_auto_nom, champ_auto_plaque, champ_auto_chassis
            , champ_auto_roues, champ_auto_CNI;
    TextView lbl_auto_propr;
    Button btn_auto_verifier, btn_auto_modif, btn_auto_suppr, btn_auto_ajouter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automobile);

        sharedPreferences = AutomobileActivity.this.getSharedPreferences("user_login", Context.MODE_PRIVATE);
        if(!(sharedPreferences.getBoolean("is_connected", false))) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        String organization = sharedPreferences.getString("organisation", "sans");

        champ_auto_nom = (EditText) findViewById(R.id.champ_auto_nom);
        champ_auto_plaque = (EditText) findViewById(R.id.champ_auto_plaque);
        champ_auto_chassis = (EditText) findViewById(R.id.champ_auto_chassis);
        champ_auto_roues = (EditText) findViewById(R.id.champ_auto_roues);
        champ_auto_CNI = (EditText) findViewById(R.id.champ_auto_CNI);
        lbl_auto_propr = (TextView) findViewById(R.id.lbl_auto_propr);
        btn_auto_verifier = (Button) findViewById(R.id.btn_auto_verifier);
        btn_auto_modif = (Button) findViewById(R.id.btn_auto_modif);
        btn_auto_ajouter = (Button) findViewById(R.id.btn_auto_ajouter);
        btn_auto_suppr = (Button) findViewById(R.id.btn_auto_suppr);

        id = getIntent().getStringExtra("id");
        client = getIntent().getStringExtra("client");
        nom = getIntent().getStringExtra("nom");
        plaque = getIntent().getStringExtra("plaque");
        chassis = getIntent().getStringExtra("chassis");
        roues = getIntent().getStringExtra("roues");
        CNI = getIntent().getStringExtra("CNI");
    }
}
