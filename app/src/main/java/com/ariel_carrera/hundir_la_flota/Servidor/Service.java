package com.ariel_carrera.hundir_la_flota.Servidor;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ariel_carrera.hundir_la_flota.MainActivity;
import com.ariel_carrera.hundir_la_flota.Model.Player;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Service {
    private Context context;

    static String webPage = "";
    static String webPageOnline = "";
    static String getPlayersUrl = "get-players";
    static String getOnlinePlayersUrl = "get-online-players";
    static String serverURL = "http://172.30.0.179:3700/api/";
    static String saveOnlinePlayer = "save-online-player";
    static String deleteOnlinePlayer = "delete-online-player";

    public static final Service SERVICE = new Service();

    public static Service getInstance() {return SERVICE;}

    public void SetContext(Context context){
        this.context = context;
    }


    public void getDataPlayers(){
        if (isConnected()){

            new DownloadWebpageTask().execute(serverURL+getPlayersUrl);
        } else {
            Toast.makeText(this.context, "Error al conectarse al servicio", Toast.LENGTH_SHORT).show();
        }
    }

    public void getDataOnlinePlayers(){
        if (isConnected()){

            new DownloadWebpageTaskOnline().execute(serverURL+getOnlinePlayersUrl);
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

    public class DownloadWebpageTaskOnline extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls){
            try {
                return downloadUrlOnline(urls[0]);
            } catch (IOException e){
                return "No se puede recuperar la página web. URL puede no ser válida";
            }
        }
    }

    public String downloadUrl(String myurl) throws IOException{
        InputStream is = null;
        try {
            URL url = new URL(serverURL+getPlayersUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
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

    public String downloadUrlOnline(String myurl) throws IOException{
        InputStream is = null;
        try {
            URL url = new URL(serverURL+getOnlinePlayersUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Inicia la consulta
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("DT: ", "La respuesta es: " + response);
            is = conn.getInputStream();
            // Para descargar la página web completa
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            webPageOnline = "";
            String data = "";
            while ((data = reader.readLine()) != null){
                webPageOnline += data +"\n";
            }
            return webPageOnline;
        } finally {
            if (is != null){
                is.close();
            }
        }
    }
    public List<Player> leerDatos() {
        if (webPage.contains("nombre")) {
            Gson gson = new Gson();
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(webPage);
            JsonElement jsonElement = (JsonElement) jsonObject.get("players");
            Player[] players = gson.fromJson(jsonElement, Player[].class);
            List<Player> playerList = new ArrayList<Player>();
            playerList = Arrays.asList(players);
            return playerList;
        } else {
            return new ArrayList<Player>();
        }
    }

    public boolean guardar(String nombre,Integer intentos,int tiempo) {

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            String URL = "http://172.30.0.179:3700/api/save-player";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("nombre", nombre);
            jsonBody.put("intentos", intentos);
            jsonBody.put("tiempo", tiempo);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    try {
                        return  requestBody.getBytes("utf-8");
                    } catch (Exception uee) {
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean connectPlayer(){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            String URL = serverURL+saveOnlinePlayer;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    return null;
                }
            };

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public int getOnlinePlayers(){
        if (webPageOnline.contains("data")){
            Gson gson = new Gson();
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(webPageOnline);
            JsonElement jsonElement = (JsonElement) jsonObject.get("data");
            int numero = Integer.parseInt(jsonElement.toString());
            return numero;
        }
        return 0;
    }

    public boolean disconnectPlayer(){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            String URL = serverURL+deleteOnlinePlayer;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    return null;
                }
            };

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }



}

