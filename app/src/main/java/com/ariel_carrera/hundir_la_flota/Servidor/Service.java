package com.ariel_carrera.hundir_la_flota.Servidor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.ariel_carrera.hundir_la_flota.MainActivity;
import com.ariel_carrera.hundir_la_flota.Model.Player;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Service {
    private Context context;

    static String webPage = "";
    static String serverURL = "http://172.30.0.179:3700/api/get-players";

    public Service(Context context){
        this.context = context;
        if (isConnected()){
            new DownloadWebpageTask().execute(serverURL);
        } else {
            Toast.makeText(this.context, "Error al conectarse al servicio", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        }else {
            return  false;
        }
    }

    public class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls){
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e){
                return "No se puede recuperar la página web. URL puede no ser válida";
            }
        }
    }

    public String downloadUrl(String myurl) throws IOException{
        InputStream is = null;
        try {
            URL url = new URL(serverURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setReadTimeout(10000);
            //conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            //conn.setDoInput(true);
            // Inicia la consulta
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("DT: ", "La respuesta es: " + response);
            is = conn.getInputStream();
            // Para descargar la página web completa
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            webPage = "";
            String data = "";
            while ((data = reader.readLine()) != null){
                webPage += data +"\n";
            }
            return webPage;
        } finally {
            if (is != null){
                is.close();
            }
        }
    }

    public List<Player> leerDatos(){
        if (webPage.contains("nombre")) {
            //Toast.makeText(this, webPage,Toast.LENGTH_LONG).show();
            Gson gson = new Gson();
            //String json = webPage.substring(webPage.indexOf("["),webPage.lastIndexOf("]")+1);
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(webPage);
            JsonElement jsonElement = (JsonElement) jsonObject.get("players");
            Player[] players = gson.fromJson(jsonElement, Player[].class);
            List<Player> playerList = new ArrayList<Player>();
            playerList = Arrays.asList(players);
            return playerList;
        } else {
            return  null;
        }
    }
}

