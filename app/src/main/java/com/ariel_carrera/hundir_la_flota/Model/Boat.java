package com.ariel_carrera.hundir_la_flota.Model;

import android.graphics.Point;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Boat {

    // Nuevo código
    private int longitud;
    public ArrayList<Point> coordenadas = new ArrayList<Point>();
    private boolean hundido;
    public int tocado = 0;


    public int getLongitud() {
        return longitud;
    }


    public Boat(int longitud) {
        this.longitud = longitud;

    }


    public boolean isHundido() {
        return hundido;
    }

    public void setHundido(boolean hundido) {
        this.hundido = hundido;
    }
}

