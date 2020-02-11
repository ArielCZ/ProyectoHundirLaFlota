package com.ariel_carrera.hundir_la_flota.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ariel_carrera.hundir_la_flota.Adapter.MyAdapter;
import com.ariel_carrera.hundir_la_flota.DataBase.DataBase;
import com.ariel_carrera.hundir_la_flota.MainActivity;
import com.ariel_carrera.hundir_la_flota.Model.Player;
import com.ariel_carrera.hundir_la_flota.R;

import java.util.List;

public class RankingActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnVolver;
    private MyAdapter myAdapter;
    private List<Player> lista_jugadores;
    private ListView listview;

    private DataBase datos;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        btnVolver = (Button) findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(this);
        datos = new DataBase(getApplicationContext());
        db = datos.getReadableDatabase();

        lista_jugadores = datos.LeerJugadores();
        listview = (ListView)findViewById(R.id.listViewRanking);

        myAdapter = new MyAdapter(this,R.layout.ranking_item,lista_jugadores);
        listview.setAdapter(myAdapter);
        this.myAdapter.notifyDataSetChanged();

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
