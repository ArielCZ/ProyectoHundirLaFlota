package com.ariel_carrera.hundir_la_flota;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ariel_carrera.hundir_la_flota.Servidor.DataBaseListener;
import com.ariel_carrera.hundir_la_flota.Servidor.Global;
import com.ariel_carrera.hundir_la_flota.Servidor.Service;
import com.ariel_carrera.hundir_la_flota.Views.AcercaDeActivity;
import com.ariel_carrera.hundir_la_flota.Views.AyudaActivity;
import com.ariel_carrera.hundir_la_flota.Views.GameActivity;
import com.ariel_carrera.hundir_la_flota.Views.RankingActivity;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnJugar, btnRanking, btnAcercaDe, btnAyuda;
    private GifImageView gifImageView;

    private SubscriptionEventListener subscriptionEventListener;

    // Apartado del servidor

    private Button btnServer;
    private TextView txtInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnJugar = (Button) findViewById(R.id.btnJugar);
        btnJugar.setOnClickListener(this);

        btnAyuda = (Button) findViewById(R.id.btnAyuda);
        btnAyuda.setOnClickListener(this);

        btnAcercaDe = (Button) findViewById(R.id.btnAcercaDe);
        btnAcercaDe.setOnClickListener(this);

        btnRanking = (Button) findViewById(R.id.btnRanking);
        btnRanking.setOnClickListener(this);


        btnServer = (Button) findViewById(R.id.btnServer);
        btnServer.setOnClickListener(this);

        txtInfo = (TextView) findViewById(R.id.txtInfo);

        gifImageView = (GifImageView) findViewById(R.id.gif);
        gifImageView.setVisibility(View.INVISIBLE);


        Service.getInstance().SetContext(getApplicationContext());
        Service.getInstance().isConnected();
        Service.getInstance().getData();

        //DataBaseListener.getInstance();
        DataBaseListener.getInstance().Connect();
        txtInfo.setText(String.valueOf(DataBaseListener.getInstance().numJugadores));



            DataBaseListener.getInstance().getChannel().bind("player-save", subscriptionEventListener = new SubscriptionEventListener() {
                @Override
                public void onEvent(PusherEvent event) {
                    System.out.println("Received event with data: " + event.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DataBaseListener.getInstance().setBindedMain(true);
                            Service.getInstance().getData();
                            if (Service.getInstance().isConnected()){

                                try{
                                    Toast.makeText(getApplicationContext(), "Ha habido un cambio en el ranking", Toast.LENGTH_SHORT).show();
                                    gifImageView.setVisibility(View.VISIBLE);
                                } catch (Exception ex){

                                }
                            }
                        }
                    });
                }
            });

        //DataBaseListener.getInstance().getChannel().unbind();

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                DataBaseListener.getInstance().getChannel().unbind("player-save", subscriptionEventListener);
                Intent Ranking = new Intent(this, RankingActivity.class);
                startActivity(Ranking);

                break;
            case R.id.btnServer:

            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }



    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        if (MotionEvent.ACTION_OUTSIDE == motionEvent.getAction()){
            finish();
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }


}
