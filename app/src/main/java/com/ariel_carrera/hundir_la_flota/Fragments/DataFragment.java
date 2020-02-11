package com.ariel_carrera.hundir_la_flota.Fragments;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ariel_carrera.hundir_la_flota.DataBase.DataBase;
import com.ariel_carrera.hundir_la_flota.MainActivity;
import com.ariel_carrera.hundir_la_flota.R;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataFragment extends Fragment implements View.OnClickListener {

    private TextView barco2, barco3, barco4, txtEstadoBarco, txtIntentos;
    private ImageView imvState;

    private Button btnGuardar;
    private Button btnSalir;
    private EditText etNombre;

    private DataBase datos;
    private SQLiteDatabase db;

    private int tiempo;
    private Chronometer cronometro;


    public DataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data, container, false);
        barco2 = (TextView) view.findViewById(R.id.txtBarco2);
        barco3 = (TextView) view.findViewById(R.id.txtBarco3);
        barco4 = (TextView) view.findViewById(R.id.txtBarco4);
        txtEstadoBarco = (TextView) view.findViewById(R.id.txtEstadoBarco);
        txtIntentos = (TextView) view.findViewById(R.id.txtIntentos);
        barco2.setText("3");
        barco3.setText("2");
        barco4.setText("1");

        btnGuardar = (Button) view.findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(this);

        etNombre = (EditText) view.findViewById(R.id.etNombre);

        cronometro = (Chronometer) view.findViewById(R.id.cronometro);

        imvState = (ImageView) view.findViewById(R.id.imvState);

        btnSalir = (Button)view.findViewById(R.id.btnSalir);
        btnSalir.setOnClickListener(this);



        return view;
    }

    public void writeData(String estadoBarco, int barco, int numBarco) {
        switch (barco) {
            case 4:
                barco4.setText(String.valueOf(numBarco));
                break;
            case 3:
                barco3.setText(String.valueOf(numBarco));
                break;
            case 2:
                barco2.setText(String.valueOf(numBarco));
                break;
            default:
                break;
        }
        imvState.setImageDrawable(getResources().getDrawable(R.drawable.hundido));

        txtEstadoBarco.setText(estadoBarco);
        checkGame();
    }

    public void writeState(String estadoBarco, int numIntentos){
        txtEstadoBarco.setText(estadoBarco);
        txtIntentos.setText(String.valueOf(numIntentos));
        checkGame();

        switch (estadoBarco){
            case "Agua":
                imvState.setImageDrawable(getResources().getDrawable(R.drawable.agua));
                break;
            case "Tocado":
                imvState.setImageDrawable(getResources().getDrawable(R.drawable.tocado));
                break;
                default:
                    break;
        }

    }


    public void onResume(){
        super.onResume();
        datos = new DataBase(getContext());
        db = datos.getWritableDatabase();
    }

    public void onPause(){
        super.onPause();
        db.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGuardar:
                String nombre = etNombre.getText().toString();
                if (nombre.equals("")){
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Aviso");
                    alert.setMessage("Debes introducir un nombre para poder guardar");
                    alert.setCancelable(false);

                    alert.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alert.setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent inicio = new Intent(getActivity(), MainActivity.class);
                            startActivity(inicio);
                        }
                    });
                    AlertDialog myAlert = alert.create();
                    myAlert.show();
                } else {
                    boolean anadido;
                    anadido = datos.insertarJugador(nombre,Integer.parseInt(txtIntentos.getText().toString()),tiempo);
                    Intent inicio = new Intent(getActivity(),MainActivity.class);
                    startActivity(inicio);

                }
                break;
            case R.id.btnSalir:
                Intent inicio = new Intent(getActivity(),MainActivity.class);
                startActivity(inicio);
                break;
        }


    }

    public void checkGame(){
        if (barco2.getText().toString().equals("0")&& barco3.getText().toString().equals("0") && barco4.getText().toString().equals("0")){
            btnGuardar.setVisibility(View.VISIBLE);
            etNombre.setVisibility(View.VISIBLE);
            txtEstadoBarco.setVisibility(View.INVISIBLE);
            String time = cronometro.getText().toString();
            // Se recoge el tiempo transcurrio en milisegundos
            int elapsed = (int) (SystemClock.elapsedRealtime() - cronometro.getBase());
            // Se transforma a segundos para luego almacenar en la base de datos
            int seconds = (int) (elapsed / 1000);
            tiempo = seconds;
        }

    }
}
