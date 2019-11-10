package bi.udev.assurance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

        String ajouter = getIntent().getStringExtra("ajouter");

        if(ajouter.equalsIgnoreCase("true")){
            btn_auto_suppr.setVisibility(View.INVISIBLE);
            btn_auto_modif.setVisibility(View.INVISIBLE);
            if (!(organization.equalsIgnoreCase("rnp"))) {
                btn_auto_ajouter.setVisibility(View.INVISIBLE);
            }

        }else {
            btn_auto_ajouter.setVisibility(View.INVISIBLE);
            if (!(organization.equalsIgnoreCase("rnp"))) {
                btn_auto_suppr.setVisibility(View.INVISIBLE);
                btn_auto_modif.setVisibility(View.INVISIBLE);
                btn_auto_verifier.setVisibility(View.INVISIBLE);
                champ_auto_CNI.setFocusable(false);
                champ_auto_roues.setFocusable(false);
                champ_auto_chassis.setFocusable(false);
                champ_auto_plaque.setFocusable(false);
                champ_auto_nom.setFocusable(false);
            }
            id = getIntent().getStringExtra("id");
            client = getIntent().getStringExtra("client");
            nom = getIntent().getStringExtra("nom");
            plaque = getIntent().getStringExtra("plaque");
            chassis = getIntent().getStringExtra("chassis");
            roues = getIntent().getStringExtra("roues");
            CNI = getIntent().getStringExtra("CNI");

            champ_auto_CNI.setText(CNI);
            champ_auto_roues.setText(roues);
            champ_auto_chassis.setText(chassis);
            champ_auto_plaque.setText(plaque);
            champ_auto_nom.setText(nom);
            lbl_auto_propr.setText(client);
        }
    }

    public void verifierCni(View view){
        String cni = champ_auto_CNI.getText().toString();
        String token = sharedPreferences.getString("token", "");

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://"+ Host.URL+"/get_client/"+cni).newBuilder();
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Token "+token)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                final String mMessage = e.getMessage().toString();
                AutomobileActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AutomobileActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    for( int i=0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        final String resultat = jsonObject.getString("result");

                        AutomobileActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lbl_auto_propr.setText(resultat);
                            }
                        });
                    }
                } catch (Exception e) {
                    final String message = e.getMessage();
                    AutomobileActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AutomobileActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }

    public void ajouterAutomobile(View view) {
        String token = sharedPreferences.getString("token", "");

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://"+ Host.URL+"/materiels/").newBuilder();

        RequestBody formBody = new FormBody.Builder()
                .add("nom", champ_auto_nom.getText().toString())
                .add("plaque", champ_auto_plaque.getText().toString())
                .add("chassis", champ_auto_chassis.getText().toString())
                .add("roues", champ_auto_roues.getText().toString())
                .add("CNI", champ_auto_CNI.getText().toString())
                .build();

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Token "+token)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                final String mMessage = e.getMessage().toString();
                AutomobileActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AutomobileActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    for( int i=0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        final String resultat = jsonObject.getString("success");

                        AutomobileActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AutomobileActivity.this, resultat, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (Exception e) {
                    final String message = e.getMessage();
                    AutomobileActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AutomobileActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
        finish();
    }

    public void modifierAutomobile(View view) {

        String token = sharedPreferences.getString("token", "");

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://"+ Host.URL+"/materiels/"+id+"/").newBuilder();

        RequestBody formBody = new FormBody.Builder()
                .add("nom", champ_auto_nom.getText().toString())
                .add("plaque", champ_auto_plaque.getText().toString())
                .add("chassis", champ_auto_chassis.getText().toString())
                .add("roues", champ_auto_roues.getText().toString())
                .add("CNI", champ_auto_CNI.getText().toString())
                .build();

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Token "+token)
                .put(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                final String mMessage = e.getMessage().toString();
                AutomobileActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AutomobileActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    for( int i=0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        final String resultat = jsonObject.getString("success");

                        AutomobileActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AutomobileActivity.this, resultat, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (Exception e) {
                    final String message = e.getMessage();
                    AutomobileActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AutomobileActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
        finish();
    }
    public void supprimerAutomobile(View view){

        String token = sharedPreferences.getString("token", "");

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://"+ Host.URL+"/materiels/"+id+"/").newBuilder();

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Token "+token)
                .delete()
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                final String mMessage = e.getMessage().toString();
                AutomobileActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AutomobileActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) {
                AutomobileActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AutomobileActivity.this, "supprimé avec succès", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        finish();
    }
}
