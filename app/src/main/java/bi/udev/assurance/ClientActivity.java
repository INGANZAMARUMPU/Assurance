package bi.udev.assurance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ClientActivity extends AppCompatActivity {

    String id, nom, prenom, CNI, avatar, email, tel, depuis;
    private SharedPreferences sharedPreferences;
    EditText champ_client_nom, champ_client_prenom, champ_client_CNI,
            champ_client_email, champ_client_tel, champ_client_depuis;
    Button btn_suppr_client, btn_ajout_client, btn_modif_client, btn_client_image;

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
        btn_client_image = (Button) findViewById(R.id.btn_client_image);

        String ajouter = getIntent().getStringExtra("ajouter");

        if(ajouter.equalsIgnoreCase("true")){
            btn_suppr_client.setVisibility(View.INVISIBLE);
            btn_modif_client.setVisibility(View.INVISIBLE);
            if (!(organization.equalsIgnoreCase("rnp"))) {
                btn_ajout_client.setVisibility(View.INVISIBLE);
            }

        }else {
            btn_ajout_client.setVisibility(View.INVISIBLE);
            if (!(organization.equalsIgnoreCase("rnp"))){
                btn_suppr_client.setVisibility(View.INVISIBLE);
                btn_modif_client.setVisibility(View.INVISIBLE);
                btn_client_image.setVisibility(View.INVISIBLE);
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

    public void ajouterClient(View view) {

        String token = sharedPreferences.getString("token", "");

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://"+ Host.URL+"/clients/").newBuilder();

        RequestBody formBody = new FormBody.Builder()
                .add("nom", champ_client_nom.getText().toString())
                .add("prenom", champ_client_prenom.getText().toString())
                .add("CNI", champ_client_CNI.getText().toString())
                .add("email", champ_client_email.getText().toString())
                .add("tel", champ_client_tel.getText().toString())
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
                ClientActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ClientActivity.this, mMessage, Toast.LENGTH_SHORT).show();
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

                        ClientActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ClientActivity.this, resultat, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (Exception e) {
                    final String message = e.getMessage();
                    ClientActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ClientActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
        finish();
    }

    public void modifierClient(View view) {

        String token = sharedPreferences.getString("token", "");

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://"+ Host.URL+"/clients/"+id+"/").newBuilder();

        RequestBody formBody = new FormBody.Builder()
                .add("nom", champ_client_nom.getText().toString())
                .add("prenom", champ_client_prenom.getText().toString())
                .add("CNI", champ_client_CNI.getText().toString())
                .add("email", champ_client_email.getText().toString())
                .add("tel", champ_client_tel.getText().toString())
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
                ClientActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ClientActivity.this, mMessage, Toast.LENGTH_SHORT).show();
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

                        ClientActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ClientActivity.this, resultat, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (Exception e) {
                    final String message = e.getMessage();
                    ClientActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ClientActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
        finish();
    }
    public void supprimerClient(View view){
        String token = sharedPreferences.getString("token", "");

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://"+ Host.URL+"/clients/"+id+"/").newBuilder();

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
                ClientActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ClientActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) {
                ClientActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ClientActivity.this, "supprimé avec succès", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        finish();
    }
}
