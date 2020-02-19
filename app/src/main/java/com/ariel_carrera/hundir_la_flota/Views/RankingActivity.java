package com.ariel_carrera.hundir_la_flota.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ariel_carrera.hundir_la_flota.Adapter.MyAdapter;
import com.ariel_carrera.hundir_la_flota.DataBase.DataBase;
import com.ariel_carrera.hundir_la_flota.MainActivity;
import com.ariel_carrera.hundir_la_flota.Model.Player;
import com.ariel_carrera.hundir_la_flota.R;
import com.ariel_carrera.hundir_la_flota.Servidor.Service;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RankingActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnVolver;
    private MyAdapter myAdapter;
    private List<Player> lista_jugadores;
    private ListView listview;

    private DataBase datos;
    private SQLiteDatabase db;
    String pruebita = "Pruebita";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        btnVolver = (Button) findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(this);
        datos = new DataBase(getApplicationContext());
        db = datos.getReadableDatabase();

        lista_jugadores = new ArrayList<Player>();
        listview = (ListView)findViewById(R.id.listViewRanking);
        final Service service = new Service(getApplicationContext());
        if (service.isConnected()){
            try{
                Thread.sleep(300);
            } catch (Exception ex){

            }
            //service.downloadUrl("http://172.30.0.179:3700/api/get-players");
            lista_jugadores = service.leerDatos();
        }
        myAdapter = new MyAdapter(this,R.layout.ranking_item,lista_jugadores);
        listview.setAdapter(myAdapter);
        this.myAdapter.notifyDataSetChanged();





        PusherOptions options = new PusherOptions();
        options.setCluster("eu");
        Pusher pusher = new Pusher("6471bfe60094a6c3c7c1", options);

        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                System.out.println("State changed from " + change.getPreviousState() +
                        " to " + change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                System.out.println("There was a problem connecting! " +
                        "\ncode: " + code +
                        "\nmessage: " + message +
                        "\nException: " + e
                );
            }
        }, ConnectionState.ALL);

        Channel channel = pusher.subscribe("playerschannel");

        channel.bind("player-save", new SubscriptionEventListener() {

            @Override
            public void onEvent(PusherEvent event) {
                System.out.println("Received event with data: " + event.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        service.Conectar();
                        if (service.isConnected()){
                            try{
                                Thread.sleep(300);
                                lista_jugadores = service.leerDatos();
                                myAdapter = new MyAdapter(RankingActivity.this,R.layout.ranking_item,lista_jugadores);
                                listview.setAdapter(myAdapter);
                                myAdapter.notifyDataSetChanged();
                                Toast.makeText(RankingActivity.this, "Se ha agregado una nueva puntuación", Toast.LENGTH_SHORT).show();
                            } catch (Exception ex){

                            }
                        }
                    }
                });
            }
        });


    }


    @Override
    public void onClick(View v) {
        Intent inicio = new Intent(RankingActivity.this, MainActivity.class);
        startActivity(inicio);
    }

    public void onResume(){
        super.onResume();

    }

    public void onPause(){
        super.onPause();
        db.close();
    }

    @Override
    public void onBackPressed(){

    }
}
