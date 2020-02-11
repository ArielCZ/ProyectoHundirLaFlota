package com.ariel_carrera.hundir_la_flota;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;

import com.ariel_carrera.hundir_la_flota.Fragments.DataFragment;
import com.ariel_carrera.hundir_la_flota.Fragments.GameFragment;
import com.ariel_carrera.hundir_la_flota.Views.AcercaDeActivity;
import com.ariel_carrera.hundir_la_flota.Views.AyudaActivity;
import com.ariel_carrera.hundir_la_flota.Views.GameActivity;
import com.ariel_carrera.hundir_la_flota.Views.RankingActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnJugar, btnRanking, btnAcercaDe, btnAyuda;
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
        }

    }

    @Override
    public void onBackPressed(){
        finish();
    }


}
