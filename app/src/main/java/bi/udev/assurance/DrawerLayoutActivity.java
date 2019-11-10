package bi.udev.assurance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DrawerLayoutActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences;
    TextView lbl_drawer_username, lbl_drawer_email;
    ImageView imgAvatar;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = DrawerLayoutActivity.this.getSharedPreferences("user_login", Context.MODE_PRIVATE);
        if(!(sharedPreferences.getBoolean("is_connected", false))){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        String email = sharedPreferences.getString("email", "email non spécifié");
        String defaut = sharedPreferences.getString("username", "");
        String nom = sharedPreferences.getString("nom", "");
        String prenom = sharedPreferences.getString("prenom", defaut);
        String username = prenom+" "+nom;
        String url_image = sharedPreferences.getString("avatar","");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        imgAvatar = (ImageView) headerView.findViewById(R.id.imgAvatar);
        lbl_drawer_email = (TextView) headerView.findViewById(R.id.lbl_drawer_email);
        lbl_drawer_username = (TextView) headerView.findViewById(R.id.lbl_drawer_username);

        lbl_drawer_username.setText(username);
        lbl_drawer_email.setText(email);
        Glide.with(DrawerLayoutActivity.this).load(url_image).into(imgAvatar);

        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_principale, new ClientsFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btnLogout) {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String organization = sharedPreferences.getString("organisation", "sans");

        if (organization.equalsIgnoreCase("rnp")) {
            fab.setVisibility(View.VISIBLE);
        }

        if (id == R.id.btnLogout) {
            logout();
        } else if (id == R.id.btnAssurances) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DrawerLayoutActivity.this, AssuranceActivity.class);
                    intent.putExtra("ajouter", "true");
                    startActivity(intent);
                }
            });
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_principale, new AssuranceFragment())
                    .commit();

        } else if (id == R.id.btnAutos) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DrawerLayoutActivity.this, AutomobileActivity.class);
                    intent.putExtra("ajouter", "true");
                    startActivity(intent);
                }
            });
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_principale, new AutosFragment())
                    .commit();

        } else if (id == R.id.btnClients) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DrawerLayoutActivity.this, ClientActivity.class);
                    intent.putExtra("ajouter", "true");
                    startActivity(intent);
                }
            });
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_principale, new ClientsFragment())
                    .commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout() {
        String token = sharedPreferences.getString("token", "");
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://"+ Host.URL+"/logout").newBuilder();
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder().url(url).header("Authorization", "Token "+token).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final String mMessage = e.getMessage().toString();
                DrawerLayoutActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DrawerLayoutActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String message = jsonObject.getString("success");
                    sharedPreferences.edit().clear().commit();
                    Intent intent = new Intent(DrawerLayoutActivity.this, MainActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    final String message = e.getMessage();
                    DrawerLayoutActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DrawerLayoutActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
