package com.ariel_carrera.hundir_la_flota.Servidor;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

public class DataBaseListener {

    public static final DataBaseListener dbListener = new DataBaseListener();

    public static DataBaseListener getInstance() {return dbListener;}

    public static Channel channel;

    public boolean bindedRanking ;

    public boolean connected;

    public int numJugadores;

    public boolean isBindedRanking() {
        return bindedRanking;
    }

    public void setBindedRanking(boolean bindedRanking) {
        this.bindedRanking = bindedRanking;
    }

    public boolean isBindedMain() {
        return bindedMain;
    }

    public void setBindedMain(boolean bindedMain) {
        this.bindedMain = bindedMain;
    }

    public boolean bindedMain;

    protected DataBaseListener(){
        this.bindedMain = false;
        this.bindedRanking = false;
        this.connected = false;
        this.numJugadores = 0;
    }



    public void Connect(){

        if (!connected){

            PusherOptions options = new PusherOptions();
            options.setCluster("eu");
            Pusher pusher = new Pusher("6471bfe60094a6c3c7c1", options);
            numJugadores++;
            pusher.connect(new ConnectionEventListener() {
                @Override
                public void onConnectionStateChange(ConnectionStateChange change) {
                    System.out.println("State changed from " + change.getPreviousState() +
                            " to " + change.getCurrentState());
                }

                @Override
                public void onError(String message, String code, Exception e) {
                    System.out.println("There was a problem connecting! " +
                            "\ncode: " + code +
                            "\nmessage: " + message +
                            "\nException: " + e
                    );
                }
            }, ConnectionState.ALL);

            channel = pusher.subscribe("playerschannel");
        }
        this.connected = true;
    }

    public Channel getChannel(){
        return channel;
    }



}

