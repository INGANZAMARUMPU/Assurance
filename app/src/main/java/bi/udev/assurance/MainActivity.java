package bi.udev.assurance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    EditText champ_username, champ_password;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        champ_username = (EditText) findViewById(R.id.champ_username);
        champ_password = (EditText) findViewById(R.id.champ_password);

        sharedPreferences = getSharedPreferences("user_login", Context.MODE_PRIVATE);

        if(sharedPreferences.getBoolean("is_connected", false)){
            Intent intent = new Intent(MainActivity.this, DrawerLayoutActivity.class);
            startActivity(intent);
        }
    }


    public void connexion(View view){
        final String login = champ_username.getText().toString();
        final String passwd = champ_password.getText().toString();
        Toast.makeText(MainActivity.this, "Verification des logins", Toast.LENGTH_SHORT).show();

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://"+ Host.URL+"/login").newBuilder();
        urlBuilder.addQueryParameter("username",login);
        urlBuilder.addQueryParameter("password", passwd);
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final String mMessage = e.getMessage().toString();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    Utilisateur user = new Utilisateur(
                            jsonObject.getString("firstname"),
                            jsonObject.getString("lastname"),
                            jsonObject.getString("username"),
                            jsonObject.getString("email"),
                            jsonObject.getString("Token"),
                            jsonObject.getString("organisation"),
                            "http://"+ Host.URL+jsonObject.getString("avatar")

                    );

                    if(!(user.username.trim().equals(""))){
                        sharedPreferences = getSharedPreferences("user_login", Context.MODE_PRIVATE);

                        SharedPreferences.Editor session = sharedPreferences.edit();
                        session.putString("nom", user.nom);
                        session.putString("prenom", user.prenom);
                        session.putString("username", user.username);
                        session.putString("email", user.email);
                        session.putString("token", user.token);
                        session.putString("organisation", user.organisation);
                        session.putString("avatar", user.avatar);
                        session.putBoolean("is_connected", true);
                        session.commit();

                        Intent intent = new Intent(MainActivity.this, DrawerLayoutActivity.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    final String message = e.getMessage();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
