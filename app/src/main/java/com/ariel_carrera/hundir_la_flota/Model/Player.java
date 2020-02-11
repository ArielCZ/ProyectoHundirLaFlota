package com.ariel_carrera.hundir_la_flota.Model;

public class Player {

    private String nombre;
    private int intentos;
    private int tiempo;
    private int id;
    private int idImagen;

    public int getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(int idImagen) {
        this.idImagen = idImagen;
    }



    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Player(int id, String nombre, int intentos, int tiempo){
        this.id = id;
        this.nombre = nombre;
        this.intentos = intentos;
        this.tiempo = tiempo;
    }
    public Player(int idImagen, int id, String nombre, int intentos, int tiempo){
        this.idImagen = idImagen;
        this.id = id;
        this.nombre = nombre;
        this.intentos = intentos;
        this.tiempo = tiempo;
    }
}
