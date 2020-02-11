package com.ariel_carrera.hundir_la_flota.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;

import android.os.Bundle;


import com.ariel_carrera.hundir_la_flota.Fragments.DataFragment;
import com.ariel_carrera.hundir_la_flota.Fragments.GameFragment;
import com.ariel_carrera.hundir_la_flota.R;


public class GameActivity extends FragmentActivity implements GameFragment.DataListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);




    }

    @Override
    public void sendState(String estadoBarco, int numIntentos) {
        DataFragment dataFragment = (DataFragment) getSupportFragmentManager().findFragmentById(R.id.dataFragment);
        dataFragment.writeState(estadoBarco, numIntentos);
    }

    @Override
    public void sendStats(String estadoBarco, int barco, int numBarco) {
        DataFragment dataFragment = (DataFragment) getSupportFragmentManager().findFragmentById(R.id.dataFragment);
        dataFragment.writeData(estadoBarco, barco, numBarco);
    }

    @Override
    public void onBackPressed(){

    }

}
