package bi.udev.assurance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AssuranceFragment extends Fragment {

    private ArrayList<Assurance> assurances;
    private int page;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    private AdaptateurAssurance adaptateur;
    private SharedPreferences sharedPreferences;

    public AssuranceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_assurance, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerAssurance);

        sharedPreferences = getActivity().getSharedPreferences("user_login", Context.MODE_PRIVATE);
        if(!(sharedPreferences.getBoolean("is_connected", false))){
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }

        assurances = new ArrayList<Assurance>();
        page = 1;
        
        chargerAssurance(page);

        gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        adaptateur = new AdaptateurAssurance(getActivity(), assurances);

        recyclerView.setAdapter(adaptateur);
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                page++;
//                chargerAssurance(page);
//            }
//        });

        return rootView;
    }

    private void chargerAssurance(int page) {
        int no_page = page;
        String token = sharedPreferences.getString("token", "");

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://"+ Host.URL+"/assurances/"/*+no_page*/).newBuilder();
        String url = urlBuilder.build().toString();

        Log.i("============", url);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Token "+token)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                final String mMessage = e.getMessage().toString();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), mMessage, Toast.LENGTH_SHORT).show();
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
                        Assurance produit = new Assurance(
                                jsonObject.getString("id"),
                                jsonObject.getString("no_police"),
                                jsonObject.getString("montant"),
                                jsonObject.getString("debut"),
                                jsonObject.getString("materiel"),
                                jsonObject.getString("client"),
                                jsonObject.getString("fin"),
                                jsonObject.getString("plaque")

                        );
                        Log.i("========", produit.toString());
                        assurances.add(produit);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adaptateur.notifyDataSetChanged();
                            }
                        });
                    }

                } catch (Exception e) {
                    final String message = e.getMessage();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }

}
