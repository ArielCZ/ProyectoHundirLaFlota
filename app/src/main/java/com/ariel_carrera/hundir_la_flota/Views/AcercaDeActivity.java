package com.ariel_carrera.hundir_la_flota.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ariel_carrera.hundir_la_flota.MainActivity;
import com.ariel_carrera.hundir_la_flota.R;

public class AcercaDeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnVolver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);

        btnVolver = (Button) findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent inicio = new Intent(this, MainActivity.class);
        startActivity(inicio);
    }

    @Override
    public void onBackPressed(){

    }
}
