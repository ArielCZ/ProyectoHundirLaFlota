package com.ariel_carrera.hundir_la_flota.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ariel_carrera.hundir_la_flota.MainActivity;
import com.ariel_carrera.hundir_la_flota.Model.Boat;
import com.ariel_carrera.hundir_la_flota.R;
import com.ariel_carrera.hundir_la_flota.Views.GameActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {

    private static int maxN = 8;
    private Context context;
    private int counter = 1;

    private DataListener callback;


    // Declaración de las celdas
    private ImageView[][] ivCell = new ImageView[maxN][maxN];
    private Drawable[] drawCell = new Drawable[4];


    // Array de coordenadas
    private ArrayList<Point> coordenadas = new ArrayList<Point>();
    private ArrayList<Point> pulsados = new ArrayList<Point>();
    private ArrayList<Point> ocupados = new ArrayList<Point>();

    // Lista de barcos
    private ArrayList<Boat> barcos = new ArrayList<Boat>();

    private Point actual = new Point();

    // Array bidimensional
    private int[][] board = new int[maxN][maxN];

    // Comprobación de casillas
    public ArrayList<String> opciones = new ArrayList<>();

    // Variables para el control de las posiciones
    private String orientacion;
    private boolean ocupadoDerecha;
    private boolean ocupadoIzquierda;
    private boolean ocupadoArriba;
    private boolean ocupadoAbajo;

    // Número de barcos
    private int barco4 = 1;
    private int barco3 = 2;
    private int barco2 = 3;

    // Sonidos
    MediaPlayer mp;

    // Control del estado del barco y el número de intentos
    private String estadoBarco;
    private int agua = 0;
    private int numIntentos = 0;

    private boolean debugMode = false;

    private Chronometer cronometro;


    public GameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game, container, false);
        context = getActivity();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        // Juego
        loadResources();
        designBoardGame();
        setBoats(4);
        setBoats(3);
        setBoats(3);
        setBoats(2);
        setBoats(2);
        setBoats(2);
        cronometro = (Chronometer) getActivity().findViewById(R.id.cronometro);
        play();

    }

    // Carga el xml de las celdas, y las imagenes de los barcos
    private void loadResources() {
        drawCell[3] = context.getResources().getDrawable(R.drawable.cell_bg);
        drawCell[0] = null;
        drawCell[1] = context.getResources().getDrawable(R.drawable.imagenapp);
        drawCell[2] = context.getResources().getDrawable(R.drawable.acorazado);
    }

    //Creación del tablero
    private void designBoardGame() {

        // Se divide el tamaño de la pantalla entre el número de celdas que hay
        int sizeofCell = Math.round(ScreenWidth() / maxN);
        LinearLayout.LayoutParams lpRow = new LinearLayout.LayoutParams(sizeofCell * maxN, sizeofCell);
        LinearLayout.LayoutParams lpCell = new LinearLayout.LayoutParams(sizeofCell, sizeofCell);

        LinearLayout linBoardGame = (LinearLayout) getView().findViewById(R.id.linBoardGame);


        for (int i = 0; i < maxN; i++) {
            LinearLayout linRow = new LinearLayout(context);


            for (int j = 0; j < maxN; j++) {
                ivCell[i][j] = new ImageView(context);
                ivCell[i][j].setBackground(drawCell[3]);
                ivCell[i][j].setTag(1);
                final int x = i;
                final int y = j;

                linRow.addView(ivCell[i][j], lpCell);
            }
            linBoardGame.addView(linRow, lpRow);

        }
    }


    // Método que coloca un barco de una determinada longitud pasado por parámetro
    public void setBoats(int longitud) {
        // Estado inicial de las variables para cada barco que se va a crear
        opciones.clear();
        ocupadoDerecha = false;
        ocupadoIzquierda = false;
        ocupadoArriba = false;
        ocupadoAbajo = false;
        Boat boat = new Boat(longitud);
        int x = 0;
        int y = 0;


        do {
            x = (int) (Math.random() * 7);
            y = (int) (Math.random() * 7);
            actual.x = x;
            actual.y = y;
        } while (ocupados.contains(actual));


        // comprueba las casillas de alrededor y añade una opción de colocación a la lista de opciones posibles

        if (y + boat.getLongitud() - 1 < maxN) {
            for (int i = x - 1; i < x + 2; i++) {
                if (!(i < 0 || i > (maxN - 1))) {
                    for (int j = y; j < y + boat.getLongitud() + 1; j++) {
                        if (!(j < 0 || j > (maxN - 1))) {
                            if (board[i][j] == 2) {
                                ocupadoDerecha = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (!ocupadoDerecha) {
                opciones.add("Derecha");
            }
        }

        if ((y - (boat.getLongitud() - 1)) >= 0) {
            for (int i = x - 1; i < (x + 2); i++) {
                if (!(i < 0 || i > (maxN - 1))) {
                    for (int j = (y - boat.getLongitud()); j <= y; j++) {
                        if (!(j < 0 || j > (maxN - 1))) {
                            if (board[i][j] == 2) {
                                ocupadoIzquierda = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (!ocupadoIzquierda) {
                opciones.add("Izquierda");
            }

        }

        if ((x + boat.getLongitud() - 1) < maxN) {
            for (int i = x; i < (x + boat.getLongitud() + 1); i++) {
                if (!(i < 0 || i > (maxN - 1))) {
                    for (int j = y - 1; j < (y + 2); j++) {
                        if (!(j < 0 || j > (maxN - 1))) {
                            if (board[i][j] == 2) {
                                ocupadoAbajo = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (!ocupadoAbajo) {
                opciones.add("Abajo");
            }

        }

        if ((x - (boat.getLongitud() - 1)) >= 0) {
            for (int i = (x - boat.getLongitud()); i < (x + 1); i++) {
                if (!(i < 0 || i > (maxN - 1))) {
                    for (int j = y - 1; j < (y + 2); j++) {
                        if (!(j < 0 || j > (maxN - 1))) {
                            if (board[i][j] == 2) {
                                ocupadoArriba = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (!ocupadoArriba) {
                opciones.add("Arriba");
            }

        }

        if (opciones.size() == 0) {
            setBoats(longitud);
        } else {


            // De las opciones posibles genera una aleatoria
            orientacion = opciones.get((int) (Math.random() * (opciones.size() - 0)) + 0);

            // Dependiendo de la orientación
            switch (orientacion) {
                case "Derecha":
                    setCells(boat, x, y, "y", 1);
                    checkCells("Derecha", boat, x, y);
                    break;
                case "Izquierda":
                    setCells(boat, x, y, "y", 0);
                    checkCells("Izquierda", boat, x, y);
                    break;
                case "Abajo":
                    setCells(boat, x, y, "x", 1);
                    checkCells("Abajo", boat, x, y);
                    break;
                case "Arriba":
                    setCells(boat, x, y, "x", 0);
                    checkCells("Arriba", boat, x, y);
                    break;
                default:
                    break;
            }
        }
    }


    // Método que inicializa a 2 los puntos en los que se colocará un barco
    private void setCells(Boat boat, int x, int y, String modifier, int operator) {
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < boat.getLongitud(); j++) {
                board[x][y] = 2;
                ocupados.add(new Point(x, y));
                Point p1 = new Point(x, y);
                coordenadas.add(p1);
                boat.coordenadas.add(p1);
                if (operator == 0) {
                    if (modifier == "x") {
                        x--;
                    } else {
                        y--;
                    }
                } else {
                    if (modifier == "x") {
                        x++;
                    } else {
                        y++;
                    }
                }


            }
        }

        barcos.add(boat);

        // Dibuja en el tablero el barco
        for (int i = 0; i < boat.coordenadas.size(); i++) {
            if (!debugMode){
                ivCell[boat.coordenadas.get(i).x][boat.coordenadas.get(i).y].setImageDrawable(getResources().getDrawable(R.drawable.boat));
                ivCell[boat.coordenadas.get(i).x][boat.coordenadas.get(i).y].setTag(R.drawable.boat);
            } else {
                ivCell[boat.coordenadas.get(i).x][boat.coordenadas.get(i).y].setImageDrawable(getResources().getDrawable(R.drawable.debugboat));
                ivCell[boat.coordenadas.get(i).x][boat.coordenadas.get(i).y].setTag(R.drawable.boat);
            }

        }
    }


    // Método que inicializa a 1 los puntos alrededor de un barco ya colocado
    private void checkCells(String orientacion, Boat boat, int x, int y) {
        switch (orientacion) {
            case "Derecha":
                for (int i = x - 1; i < x + 2; i++) {
                    if (!(i < 0 || i > (maxN - 1))) {
                        for (int j = y - 1; j < y + boat.getLongitud() + 1; j++) {
                            if (!(j < 0 || j > (maxN - 1))) {
                                if (board[i][j] != 2) {
                                    board[i][j] = 1;
                                    ocupados.add(new Point(i, j));
                                }
                            }
                        }
                    }
                }
                break;
            case "Izquierda":
                for (int i = x - 1; i < (x + 2); i++) {
                    if (!(i < 0 || i > (maxN - 1))) {
                        for (int j = (y - boat.getLongitud()); j <= y + 1; j++) {
                            if (!(j < 0 || j > (maxN - 1))) {
                                if (board[i][j] != 2) {
                                    board[i][j] = 1;
                                    ocupados.add(new Point(i, j));
                                }
                            }
                        }
                    }
                }
                break;
            case "Abajo":
                for (int i = (x - 1); i < (x + boat.getLongitud() + 1); i++) {
                    if (!(i < 0 || i > (maxN - 1))) {
                        for (int j = y - 1; j < (y + 2); j++) {
                            if (!(j < 0 || j > (maxN - 1))) {
                                if (board[i][j] != 2) {
                                    board[i][j] = 1;
                                    ocupados.add(new Point(i, j));
                                }
                            }
                        }
                    }
                }
                break;
            case "Arriba":
                for (int i = (x - boat.getLongitud()); i < (x + 2); i++) {
                    if (!(i < 0 || i > (maxN - 1))) {
                        for (int j = y - 1; j < (y + 2); j++) {
                            if (!(j < 0 || j > (maxN - 1))) {
                                if (board[i][j] != 2) {
                                    board[i][j] = 1;
                                    ocupados.add(new Point(i, j));
                                }
                            }
                        }
                    }
                }
                break;
            default:
                break;

        }
    }


    // Devuelve el tamaño de la pantalla
    private float ScreenWidth() {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }


    // Método que controla el modo de juego
    private void play() {


        for (int i = 0; i < maxN; i++) {
            for (int j = 0; j < maxN; j++) {
                final int x = i;
                final int y = j;
                final int count = counter;
                ivCell[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        estadoBarco = "";
                        agua = 0;


                        if (ivCell[x][y].getTag().equals(R.drawable.boat)){
                            mp = MediaPlayer.create(getContext(),R.raw.tocado);
                            mp.start();
                            ivCell[x][y].setImageDrawable(getResources().getDrawable(R.drawable.touched));
                        } else {
                            mp = MediaPlayer.create(getContext(),R.raw.agua);
                            mp.start();
                            ivCell[x][y].setImageDrawable(getResources().getDrawable(R.drawable.water));
                        }
                        if (counter == 1){
                            cronometro.setBase(SystemClock.elapsedRealtime());
                            cronometro.start();
                            numIntentos++;
                        }
                        // Si ya ha sido pulsado una casilla se añade a la lista de ya pulsados
                        if (!pulsados.contains(new Point(x, y))) {
                            pulsados.add(new Point(x, y));
                            for (int k = 0; k < barcos.size(); k++) {
                                if (barcos.get(k).coordenadas.contains(new Point(x, y))) {
                                    barcos.get(k).tocado++;
                                    estadoBarco = "Tocado";
                                } else {
                                    agua++;

                                }


                                if (agua < barcos.size()) {
                                    numIntentos = numIntentos;
                                    if (counter == 1){
                                        if (ivCell[x][y].getTag().equals(R.drawable.boat)){
                                            callback.sendState(estadoBarco,numIntentos);
                                        } else {
                                            callback.sendState("Agua",numIntentos);
                                        }

                                    } else {
                                        callback.sendState(estadoBarco,numIntentos);
                                    }

                                } else {
                                    if (counter != 1){
                                        numIntentos++;
                                        callback.sendState("Agua",numIntentos);
                                    }
                                }



                                if (barcos.get(k).tocado == barcos.get(k).getLongitud()) {
                                    barcos.get(k).setHundido(true);
                                    estadoBarco = "Hundido barco de " + barcos.get(k).getLongitud();
                                }

                                // Si el barco es hundido
                                if (barcos.get(k).isHundido()) {
                                    switch (barcos.get(k).getLongitud()) {
                                        case 4:
                                            barco4--;
                                            callback.sendStats(estadoBarco, 4, barco4);
                                            break;
                                        case 3:
                                            barco3--;
                                            callback.sendStats(estadoBarco, 3, barco3);
                                            break;
                                        case 2:
                                            barco2--;
                                            callback.sendStats(estadoBarco, 2, barco2);
                                            break;
                                        default:
                                            break;
                                    }
                                    mp = MediaPlayer.create(getContext(),R.raw.hundido);
                                    mp.start();
                                    barcos.remove(k);
                                }
                                if (numIntentos > 39){
                                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                    alert.setTitle("Aviso");
                                    alert.setMessage("No se permiten más intentos ¿Quieres volver a intentarlo?");
                                    alert.setCancelable(false);

                                    alert.setNegativeButton("Sí", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent juego = new Intent(getActivity(), GameActivity.class);
                                            startActivity(juego);
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
                                }

                                if (barco4 == 0 && barco3 == 0 && barco2 == 0){
                                    cronometro.stop();

                                    for (int i = 0; i < maxN;i++){
                                        for (int j = 0; j< maxN;j++){
                                                ivCell[i][j].setImageDrawable(getResources().getDrawable(R.drawable.boat));
                                            ivCell[i][j].setEnabled(false);
                                        }
                                    }
                                }
                            }
                            counter++;
                            ivCell[x][y].setEnabled(false);
                        }
                    }

                });

            }

        }




    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (DataListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() + "DataListener mal implementado");
        }
    }

    public interface DataListener {
        void sendState(String estadoBarco, int numIntentos);

        void sendStats(String estadoBarco, int barco, int numBarco);


    }

}

