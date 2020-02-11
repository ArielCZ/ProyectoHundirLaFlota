package com.ariel_carrera.hundir_la_flota.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ariel_carrera.hundir_la_flota.Model.Player;
import com.ariel_carrera.hundir_la_flota.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<String> muertos, heridos, numeros, intentos;
    private List<Player> jugadores;
    private int posicion;

    public MyAdapter(Context context, int layout, ArrayList<String> muertos, ArrayList<String> heridos, ArrayList<String> numeros, ArrayList<String> intentos){
        this.context = context;
        this.layout = layout;
        this.muertos = muertos;
        this.heridos = heridos;
        this.numeros = numeros;
        this.intentos = intentos;
    }

    public MyAdapter(Context context, int layout, List<Player> jugadores){
        this.context = context;
        this.layout = layout;
        this.jugadores = jugadores;
        posicion = 0;
    }
    @Override
    public int getCount() {
        return jugadores.size();
    }

    @Override
    public Object getItem(int position) {
        return this.jugadores.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;


        if (convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(this.context);
            v = layoutInflater.inflate(this.layout,null);
        }
        // Si el layout pasado es el que imprimirá los intentos del usuario
            String nombresText = jugadores.get(position).getNombre();
            String intentosRText = String.valueOf(jugadores.get(position).getIntentos());
            int tiempo = jugadores.get(position).getTiempo();

            TextView txtView4 = (TextView) v.findViewById(R.id.txtNombre);
            TextView txtView5 = (TextView) v.findViewById(R.id.txtIntentos);
            TextView txtView6 = (TextView) v.findViewById(R.id.txtTiempo);
            ImageView image = (ImageView) v.findViewById(R.id.imvJugador);
            if(position == 0){
                image.setImageResource(R.drawable.primero);
            }
            else if(position == 1){
                image.setImageResource(R.drawable.segundo);
            } else if (position == 2) {
                image.setImageResource(R.drawable.tercero);
            } else {
                image.setImageResource(R.drawable.pred);
            }

            // Si el tiempo supera los 60 segundos, imprime los minutos y segundos
            if (tiempo > 60){
                int minutos = tiempo / 60;
                int segundos = tiempo % 60;
                String minutosStr = String.valueOf(minutos);
                String segundosStr = String.valueOf(segundos);
                txtView6.setText(minutosStr+"m "+segundosStr+"s");
                // Sino imprime los segundos que ha tardado
            } else{
                String tiempoText = String.valueOf(tiempo);
                txtView6.setText(tiempoText+"s");
            }
            txtView4.setText(nombresText);
            txtView5.setText(intentosRText);

        return v;
    }
}
