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

import com.ariel_carrera.hundir_la_flota.Adapter.MyAdapter;
import com.ariel_carrera.hundir_la_flota.Fragments.DataFragment;
import com.ariel_carrera.hundir_la_flota.Fragments.GameFragment;
import com.ariel_carrera.hundir_la_flota.Model.Player;
import com.ariel_carrera.hundir_la_flota.Servidor.DataBaseListener;
import com.ariel_carrera.hundir_la_flota.Servidor.Service;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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

        btnJugar = (Button) findViewById(R.id.btnJugar);
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

        Service.getInstance().SetContext(getApplicationContext());
        Service.getInstance().isConnected();
        Service.getInstance().getData();

        //DataBaseListener.getInstance();
        DataBaseListener.getInstance().Connect();
        txtInfo.setText(String.valueOf(DataBaseListener.getInstance().numJugadores));



        if (!DataBaseListener.getInstance().isBindedMain()){
            DataBaseListener.getInstance().getChannel().bind("player-save", new SubscriptionEventListener() {

                @Override
                public void onEvent(PusherEvent event) {
                    System.out.println("Received event with data: " + event.toString());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DataBaseListener.getInstance().setBindedMain(true);
                            Service.getInstance().getData();
                            if (Service.getInstance().isConnected()){

                                try{
                                    Toast.makeText(getApplicationContext(), "Ha habido un cambio en el ranking", Toast.LENGTH_SHORT).show();
                                } catch (Exception ex){

                                }
                            }
                        }
                    });
                }
            });
        }

        //DataBaseListener.getInstance().getChannel().unbind();

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

            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }





}
