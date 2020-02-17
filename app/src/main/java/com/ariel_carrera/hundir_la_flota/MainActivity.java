package com.ariel_carrera.hundir_la_flota;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ariel_carrera.hundir_la_flota.Fragments.DataFragment;
import com.ariel_carrera.hundir_la_flota.Fragments.GameFragment;
import com.ariel_carrera.hundir_la_flota.Model.Player;
import com.ariel_carrera.hundir_la_flota.Views.AcercaDeActivity;
import com.ariel_carrera.hundir_la_flota.Views.AyudaActivity;
import com.ariel_carrera.hundir_la_flota.Views.GameActivity;
import com.ariel_carrera.hundir_la_flota.Views.RankingActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.lang.reflect.Type;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnJugar, btnRanking, btnAcercaDe, btnAyuda;

    // Apartado del servidor
    private String webPage = "";
    private String serverURL = "http://172.30.0.179:3700/api/get-players";

    private Button btnServer;
    private TextView txtInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnJugar = (Button)findViewById(R.id.btnJugar);
        btnJugar.setOnClickListener(this);

        btnAyuda = (Button) findViewById(R.id.btnAyuda);
        btnAyuda.setOnClickListener(this);

        btnAcercaDe = (Button) findViewById(R.id.btnAcercaDe);
        btnAcercaDe.setOnClickListener(this);

        btnRanking = (Button) findViewById(R.id.btnRanking);
        btnRanking.setOnClickListener(this);



        btnServer = (Button) findViewById(R.id.btnServer);
        btnServer.setOnClickListener(this);

        txtInfo = (TextView) findViewById(R.id.txtInfo);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnJugar:
                Intent game = new Intent(this, GameActivity.class);
                startActivity(game);
                break;
            case R.id.btnAyuda:
                Intent ayuda = new Intent(this, AyudaActivity.class);
                startActivity(ayuda);
                break;
            case R.id.btnAcercaDe:
                Intent AcercaDe = new Intent(this, AcercaDeActivity.class);
                startActivity(AcercaDe);
                break;
            case R.id.btnRanking:
                Intent Ranking = new Intent(this, RankingActivity.class);
                startActivity(Ranking);
                break;
            case R.id.btnServer:
                boolean conectado = isConnected();

            default:
                break;
        }

    }

    @Override
    public void onBackPressed(){
        finish();
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            Toast.makeText(this,"si", Toast.LENGTH_SHORT).show();
            return true;
        }else {
            Toast.makeText(this, "no", Toast.LENGTH_SHORT).show();
            return  false;
        }
    }

    public class DownloadWebpageTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls){
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e){
                return "No se puede recuperar la página web. URL puede no ser válida";
            }
        }
    }

    private String downloadUrl(String myurl) throws IOException{
        InputStream is = null;
        try {
            URL url = new URL(serverURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setReadTimeout(10000);
            //conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            //conn.setDoInput(true);
            // Inicia la consulta
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("DT: ", "La respuesta es: " + response);
            is = conn.getInputStream();
            // Para descargar la página web completa
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            webPage = "";
            String data = "";
            while ((data = reader.readLine()) != null){
                webPage += data +"\n";
            }
            return webPage;
        } finally {
            if (is != null){
                is.close();
            }
        }
    }




}
