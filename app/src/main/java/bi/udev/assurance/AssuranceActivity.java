package bi.udev.assurance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AssuranceActivity extends AppCompatActivity {

    String id, no_police, montant, debut, CNI, plaque, client, automobile, fin;
    private SharedPreferences sharedPreferences;
    EditText champ_ass_no_police, champ_ass_montant, champ_ass_debut,
            champ_ass_plaque, champ_ass_cni, champ_ass_fin;
    Button btn_ass_plaque, btn_ass_cni, btn_ass_modif, btn_ass_ajouter, btn_ass_suppr;
    TextView lbl_ass_auto, lbl_ass_client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assurance);

        sharedPreferences = AssuranceActivity.this.getSharedPreferences("user_login", Context.MODE_PRIVATE);
        if(!(sharedPreferences.getBoolean("is_connected", false))) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        String organization = sharedPreferences.getString("organisation", "sans");

        champ_ass_cni = (EditText) findViewById(R.id.champ_ass_cni);
        champ_ass_no_police = (EditText) findViewById(R.id.champ_ass_no_police);
        champ_ass_montant = (EditText) findViewById(R.id.champ_ass_montant);
        champ_ass_debut = (EditText) findViewById(R.id.champ_ass_debut);
        champ_ass_plaque = (EditText) findViewById(R.id.champ_ass_plaque);
        champ_ass_fin = (EditText) findViewById(R.id.champ_ass_fin);

        lbl_ass_auto = (TextView) findViewById(R.id.lbl_ass_auto);
        lbl_ass_client = (TextView) findViewById(R.id.lbl_ass_client);

        btn_ass_plaque = (Button) findViewById(R.id.btn_ass_plaque);
        btn_ass_cni = (Button) findViewById(R.id.btn_ass_cni);
        btn_ass_modif = (Button) findViewById(R.id.btn_ass_modif);
        btn_ass_ajouter = (Button) findViewById(R.id.btn_ass_ajouter);
        btn_ass_suppr = (Button) findViewById(R.id.btn_ass_suppr);

        String ajouter = getIntent().getStringExtra("ajouter");

        if(ajouter.equalsIgnoreCase("true")){
            btn_ass_suppr.setVisibility(View.INVISIBLE);
            btn_ass_modif.setVisibility(View.INVISIBLE);
            if (!(organization.equalsIgnoreCase("rnp"))) {
                btn_ass_ajouter.setVisibility(View.INVISIBLE);
                btn_ass_plaque.setVisibility(View.INVISIBLE);
                btn_ass_cni.setVisibility(View.INVISIBLE);
            }

        }else {
            btn_ass_ajouter.setVisibility(View.INVISIBLE);
            if (!(organization.equalsIgnoreCase("rnp"))){
                btn_ass_ajouter.setVisibility(View.INVISIBLE);
                btn_ass_plaque.setVisibility(View.INVISIBLE);
                btn_ass_cni.setVisibility(View.INVISIBLE);
                btn_ass_modif.setVisibility(View.INVISIBLE);
                btn_ass_suppr.setVisibility(View.INVISIBLE);
                champ_ass_cni.setFocusable(false);
                champ_ass_debut.setFocusable(false);
                champ_ass_montant.setFocusable(false);
                champ_ass_no_police.setFocusable(false);
                champ_ass_plaque.setFocusable(false);
                champ_ass_fin.setFocusable(false);
            }

            id = getIntent().getStringExtra("id");
            no_police = getIntent().getStringExtra("no_police");
            montant = getIntent().getStringExtra("montant");
            debut = getIntent().getStringExtra("debut");
            automobile = getIntent().getStringExtra("materiel");
            client = getIntent().getStringExtra("client");
            fin = getIntent().getStringExtra("fin");
            plaque = getIntent().getStringExtra("plaque");
            CNI = getIntent().getStringExtra("CNI");

            champ_ass_cni.setText(CNI);
            champ_ass_debut.setText(debut);
            champ_ass_montant.setText(montant);
            champ_ass_no_police.setText(no_police);
            champ_ass_plaque.setText(plaque);
            champ_ass_fin.setText(fin);
            lbl_ass_auto.setText(automobile);
            lbl_ass_client.setText(client);
        }
    }
    public void verifierPlaque(View view){
        String plaque = champ_ass_plaque.getText().toString();
        String token = sharedPreferences.getString("token", "");

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://"+ Host.URL+"/get_auto/"+plaque).newBuilder();
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Token "+token)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                final String mMessage = e.getMessage().toString();
                AssuranceActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AssuranceActivity.this, mMessage, Toast.LENGTH_SHORT).show();
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

                        AssuranceActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lbl_ass_auto.setText(resultat);
                            }
                        });
                    }
                } catch (Exception e) {
                    final String message = e.getMessage();
                    AssuranceActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AssuranceActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }

    public void verifierCni(View view){
        String cni = champ_ass_cni.getText().toString();
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
                AssuranceActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AssuranceActivity.this, mMessage, Toast.LENGTH_SHORT).show();
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

                        AssuranceActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lbl_ass_auto.setText(resultat);
                            }
                        });
                    }
                } catch (Exception e) {
                    final String message = e.getMessage();
                    AssuranceActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AssuranceActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }

    public void ajouterAssurance(View view) {
        
        String token = sharedPreferences.getString("token", "");

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://"+ Host.URL+"/assurances/").newBuilder();

        RequestBody formBody = new FormBody.Builder()
                .add("no_police", champ_ass_no_police.getText().toString())
                .add("montant", champ_ass_montant.getText().toString())
                .add("debut", champ_ass_debut.getText().toString())
                .add("plaque", champ_ass_plaque.getText().toString())
                .add("CNI", champ_ass_cni.getText().toString())
                .add("fin", champ_ass_fin.getText().toString())
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
                AssuranceActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AssuranceActivity.this, mMessage, Toast.LENGTH_SHORT).show();
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

                        AssuranceActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AssuranceActivity.this, resultat, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (Exception e) {
                    final String message = e.getMessage();
                    AssuranceActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AssuranceActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }

    public void modifierAssurance(View view) {

        String token = sharedPreferences.getString("token", "");

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://"+ Host.URL+"/assurances/"+id+"/").newBuilder();

        RequestBody formBody = new FormBody.Builder()
                .add("no_police", champ_ass_no_police.getText().toString())
                .add("montant", champ_ass_montant.getText().toString())
                .add("debut", champ_ass_debut.getText().toString())
                .add("plaque", champ_ass_plaque.getText().toString())
                .add("CNI", champ_ass_cni.getText().toString())
                .add("fin", champ_ass_fin.getText().toString())
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
                AssuranceActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AssuranceActivity.this, mMessage, Toast.LENGTH_SHORT).show();
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

                        AssuranceActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AssuranceActivity.this, resultat, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (Exception e) {
                    final String message = e.getMessage();
                    AssuranceActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AssuranceActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }
    public void supprimerAssurance(View view){

        String token = sharedPreferences.getString("token", "");

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://"+ Host.URL+"/assurances/"+id+"/").newBuilder();

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
                AssuranceActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AssuranceActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) {
                AssuranceActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AssuranceActivity.this, "supprimé avec succès", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
