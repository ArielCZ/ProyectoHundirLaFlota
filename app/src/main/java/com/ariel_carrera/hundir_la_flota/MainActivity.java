package com.ariel_carrera.hundir_la_flota;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.content.res.Configuration;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ariel_carrera.hundir_la_flota.Servidor.DataBaseListener;
import com.ariel_carrera.hundir_la_flota.Servidor.Global;
import com.ariel_carrera.hundir_la_flota.Servidor.Service;
import com.ariel_carrera.hundir_la_flota.Views.AcercaDeActivity;
import com.ariel_carrera.hundir_la_flota.Views.AyudaActivity;
import com.ariel_carrera.hundir_la_flota.Views.GameActivity;
import com.ariel_carrera.hundir_la_flota.Views.RankingActivity;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnJugar, btnRanking, btnAcercaDe, btnAyuda;
    private GifImageView gifImageView;

    private SubscriptionEventListener subscriptionEventListener;
    private SubscriptionEventListener subscriptionEventListenerOnline;
    private SubscriptionEventListener subscriptionEventListenerOffline;

    private FloatingActionsMenu fab;
    private FloatingActionButton fbSpanish, fbEnglish;

    // Apartado del servidor

    private TextView txtInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = (FloatingActionsMenu)findViewById(R.id.fab);

        fbSpanish = (FloatingActionButton)findViewById(R.id.fbSpanish);
        fbEnglish = (FloatingActionButton)findViewById(R.id.fbEnglish);

        fbSpanish.setOnClickListener(this);
        fbEnglish.setOnClickListener(this);

        btnJugar = (Button) findViewById(R.id.btnJugar);
        btnJugar.setOnClickListener(this);

        btnAyuda = (Button) findViewById(R.id.btnAyuda);
        btnAyuda.setOnClickListener(this);

        btnAcercaDe = (Button) findViewById(R.id.btnAcercaDe);
        btnAcercaDe.setOnClickListener(this);

        btnRanking = (Button) findViewById(R.id.btnRanking);
        btnRanking.setOnClickListener(this);

        txtInfo = (TextView) findViewById(R.id.txtInfo);

        gifImageView = (GifImageView) findViewById(R.id.gif);
        if (Global.isUpdated){
            gifImageView.setVisibility(View.INVISIBLE);
        } else {
            gifImageView.setVisibility(View.VISIBLE);
        }



        DataBaseListener.getInstance().Connect();

        Service.getInstance().SetContext(getApplicationContext());
        Service.getInstance().isConnected();
        try {
            Service.getInstance().getDataPlayers();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DataBaseListener.getInstance().getChannel().bind("player-save", subscriptionEventListener = new SubscriptionEventListener() {
                @Override
                public void onEvent(PusherEvent event) {
                    System.out.println("Received event with data: " + event.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DataBaseListener.getInstance().setBindedMain(true);
                            try {
                                Service.getInstance().getDataPlayers();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (Service.getInstance().isConnected()){

                                try{
                                    gifImageView.setVisibility(View.VISIBLE);
                                    Global.setIsUpdated(false);
                                } catch (Exception ex){

                                }
                            }
                        }
                    });
                }
            });

        DataBaseListener.getInstance().getChannel().bind("connect-player", subscriptionEventListenerOnline = new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                System.out.println("Received event with data: " + event.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{

                            Service.getInstance().getDataOnlinePlayers();
                            String numPlayers = String.valueOf(Service.getInstance().getOnlinePlayers());
                            txtInfo.setText(numPlayers);
                        } catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                });
            }
        });

        DataBaseListener.getInstance().getChannel().bind("disconnect-player", subscriptionEventListenerOffline = new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                System.out.println("Received event with data: " + event.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Service.getInstance().getDataOnlinePlayers();
                            String numPlayers = String.valueOf(Service.getInstance().getOnlinePlayers());
                            txtInfo.setText(numPlayers);

                        } catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                });
            }
        });

    }
    // Desbindea los players
    public void unbindOnlinePlayers(){
        DataBaseListener.getInstance().getChannel().unbind("connect-player", subscriptionEventListenerOnline);
        DataBaseListener.getInstance().getChannel().unbind("disconnect-player", subscriptionEventListenerOffline);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnJugar:
                unbindOnlinePlayers();
                Intent game = new Intent(this, GameActivity.class);
                startActivity(game);
                break;
            case R.id.btnAyuda:
                unbindOnlinePlayers();
                Intent ayuda = new Intent(this, AyudaActivity.class);
                startActivity(ayuda);
                break;
            case R.id.btnAcercaDe:
                unbindOnlinePlayers();
                Intent AcercaDe = new Intent(this, AcercaDeActivity.class);
                startActivity(AcercaDe);
                break;
            case R.id.btnRanking:
                DataBaseListener.getInstance().getChannel().unbind("player-save", subscriptionEventListener);
                unbindOnlinePlayers();
                Global.setIsUpdated(true);
                Intent Ranking = new Intent(this, RankingActivity.class);
                startActivity(Ranking);
                break;
            case R.id.fbSpanish:
                changeLanguage("es");
                fab.collapse();
                recreate();
                break;
            case R.id.fbEnglish:
                changeLanguage("en");
                recreate();
                fab.collapse();
                break;
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


    @Override
    public void onResume(){
        super.onResume();
        try{
            Service.getInstance().getDataOnlinePlayers();

            String numPlayers = String.valueOf(Service.getInstance().getOnlinePlayers());
            txtInfo.setText(numPlayers);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void changeLanguage(String locale){
        if (locale == "es"){
            Configuration config;
            config = new Configuration(getResources().getConfiguration());
            config.locale = new Locale("es");
            getResources().updateConfiguration(config,getResources().getDisplayMetrics());
        }else{
            Configuration config;
            config = new Configuration(getResources().getConfiguration());
            config.locale = (Locale.ENGLISH);
            getResources().updateConfiguration(config,getResources().getDisplayMetrics());
        }

    }
}
