package com.ariel_carrera.hundir_la_flota.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ariel_carrera.hundir_la_flota.Adapter.MyAdapter;
import com.ariel_carrera.hundir_la_flota.DataBase.DataBase;
import com.ariel_carrera.hundir_la_flota.MainActivity;
import com.ariel_carrera.hundir_la_flota.Model.Player;
import com.ariel_carrera.hundir_la_flota.R;
import com.ariel_carrera.hundir_la_flota.Servidor.DataBaseListener;
import com.ariel_carrera.hundir_la_flota.Servidor.Service;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import java.util.ArrayList;
import java.util.List;


public class RankingActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnVolver;
    private MyAdapter myAdapter;
    private List<Player> lista_jugadores;
    private ListView listview;

    private DataBase datos;
    private SQLiteDatabase db;


    private SubscriptionEventListener subscriptionEventListener;

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
        Service.getInstance().SetContext(getApplication());
        Service.getInstance().getDataPlayers();

        if (Service.getInstance().isConnected()){
            try{
                Thread.sleep(300);
            } catch (Exception ex){

            }
            lista_jugadores = Service.getInstance().leerDatos();

        }

        if (lista_jugadores != null){

        }
        myAdapter = new MyAdapter(this,R.layout.ranking_item,lista_jugadores);
        listview.setAdapter(myAdapter);
        this.myAdapter.notifyDataSetChanged();


        //DataBaseListener.getInstance();

            DataBaseListener.getInstance().getChannel().bind("player-save", subscriptionEventListener = new SubscriptionEventListener() {

                @Override
                public void onEvent(PusherEvent event) {
                    System.out.println("Received event with data: " + event.toString());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DataBaseListener.getInstance().setBindedRanking(true);
                            Service.getInstance().getDataPlayers();
                            if (Service.getInstance().isConnected()){

                                try{
                                    Thread.sleep(300);
                                    lista_jugadores = Service.getInstance().leerDatos();
                                    myAdapter = new MyAdapter(getApplicationContext(),R.layout.ranking_item,lista_jugadores);
                                    listview.setAdapter(myAdapter);
                                    myAdapter.notifyDataSetChanged();
                                    Toast.makeText(getApplicationContext(), "Se ha agregado una nueva puntuación", Toast.LENGTH_SHORT).show();
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
        DataBaseListener.getInstance().getChannel().unbind("player-save", subscriptionEventListener);
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
        DataBaseListener.getInstance().getChannel().unbind("player-save", subscriptionEventListener);
    }
}
