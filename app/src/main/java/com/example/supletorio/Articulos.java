package com.example.supletorio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Articulos {
    private String titulo;
    private String fecha;
    private String pdf;
    private String url;
    public Articulos(JSONObject a) throws JSONException {
        titulo = a.getString("title").toString();
        fecha = a.getString("date_published");
        pdf = a.getString("pdf");
        //url = a.getString("portada");
    }

    public String getTitulo() {
        return titulo;
    }

    public String getFecha() {
        return fecha;
    }

    public String getPdf() {
        return pdf;
    }

    public String getUrl() {
        return url;
    }

    public static ArrayList<Articulos> JsonObjectsBuild(JSONArray datos) throws JSONException {
        ArrayList<Articulos> articulos = new ArrayList<>();
        for (int i = 0; i < datos.length(); i++) {
            articulos.add(new Articulos(datos.getJSONObject(i)));
        }
        return articulos;
    }

}
