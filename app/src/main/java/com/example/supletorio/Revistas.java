package com.example.supletorio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Revistas {
    private String volumen;
    private String numero;
    private String anio;
    private String url;
    public Revistas(JSONObject a) throws JSONException {
        volumen = a.getString("volume").toString();
        numero = a.getString("number");
        anio = a.getString("year");
        url = a.getString("portada");
    }

    public String getVolumen() {
        return volumen;
    }

    public String getNumero() {
        return numero;
    }

    public String getAnio() {
        return anio;
    }

    public String getURL(){
        return url;
    }

    public static ArrayList<Revistas> JsonObjectsBuild(JSONArray datos) throws JSONException {
        ArrayList<Revistas> revistas = new ArrayList<>();
        for (int i = 0; i < datos.length(); i++) {
            revistas.add(new Revistas(datos.getJSONObject(i)));
        }
        return revistas;
    }

}
