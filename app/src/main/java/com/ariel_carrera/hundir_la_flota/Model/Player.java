package com.ariel_carrera.hundir_la_flota.Model;

public class Player {

    private String nombre;
    private int intentos;
    private int tiempo;
    private String _id;
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

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public Player(String id, String nombre, int intentos, int tiempo){
        this._id = id;
        this.nombre = nombre;
        this.intentos = intentos;
        this.tiempo = tiempo;
    }
    public Player(int idImagen, String id, String nombre, int intentos, int tiempo){
        this.idImagen = idImagen;
        this._id = id;
        this.nombre = nombre;
        this.intentos = intentos;
        this.tiempo = tiempo;
    }
}
