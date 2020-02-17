package com.ariel_carrera.hundir_la_flota.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.ariel_carrera.hundir_la_flota.Model.Player;

import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {
    private static final int VERSION_DB = 1;

    private static final String NAME_DB = "ranking_db.db";

    // Creación de la tabla
    private static final String TABLA_RANKING = "CREATE TABLE Ranking (_id INTEGER PRIMARY KEY, nombre TEXT, intentos INT, tiempo INT)";
    private static final String DROP_TABLA_RANKING = "DROP TABLE IF EXISTS";

    public DataBase(Context context) {
        super(context, NAME_DB, null, VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLA_RANKING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLA_RANKING + DROP_TABLA_RANKING);
        onCreate(db);
    }

    // Función que inserta un nuevo jugador al ranking
    public boolean insertarJugador(String nombre, int intentos, int tiempo) {
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("intentos", intentos);
            values.put("tiempo", tiempo);
            db.insert("Ranking", null, values);
            db.close();
            return true;
        }
        return false;
    }

    // Función que devuelve la lista de los jugadores que han guardado su partida
//    public List<Player> LeerJugadores(){
//        SQLiteDatabase db = getWritableDatabase();
//        List<Player> lista_jugadores = new ArrayList<Player>();
//        String[] result = {"_id","nombre","intentos","tiempo"};
//        try{
//            Cursor c = db.query("Ranking",result,null,null,null,null,"intentos," + "tiempo",null);
//            if (c != null){
//                try{
//                    if (c.moveToNext()){
//                        c.moveToFirst();
//                        do {
//                            Player j = new Player(c.getInt(0),c.getString(1),c.getInt(2),c.getInt(3));
//                            lista_jugadores.add(j);
//                        } while (c.moveToNext());
//                    } else{
//                        // Vacío
//                    }
//                } finally {
//                    c.close();
//                    db.close();
//                }
//            }
//        } catch (SQLiteException e) {
//            e.printStackTrace();
//        }
//        return lista_jugadores;
//    }
}
