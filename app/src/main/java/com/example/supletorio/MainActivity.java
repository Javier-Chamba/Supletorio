package com.example.supletorio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import WebServices.Asynchtask;
import WebServices.WebService;

public class MainActivity extends AppCompatActivity implements Asynchtask, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    public Revistas[] revistas=null;
    private String txtVolumen, txtNumero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map<String, String> datos = new HashMap<String, String>();
        WebService ws= new WebService("http://revistas.uteq.edu.ec/ws/getrevistas.php", datos,
                MainActivity.this, this);
        ws.execute("");

        ListView lstOpciones = (ListView) findViewById(R.id.lstLista);
        lstOpciones.setOnItemClickListener(this);

    }

    @Override
    public void processFinish(String result) throws JSONException {

        Log.i("processFinish", result);
        ArrayList<Revistas> revistaList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(result);
        JSONArray revistas = jsonObject.getJSONArray("issues");

        revistaList=Revistas.JsonObjectsBuild(revistas);

        //Adaptador
        AdaptadorRevistas adaptadorRevistas = new AdaptadorRevistas(this, revistaList);

        //AdapterView
        // ListView
        ListView lstOpciones = (ListView)findViewById(R.id.lstLista);
        lstOpciones.setAdapter(adaptadorRevistas);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        txtVolumen=((Revistas)adapterView.getItemAtPosition(position)).getVolumen();
        txtNumero=((Revistas)adapterView.getItemAtPosition(position)).getNumero();

        Intent intent = new Intent(MainActivity.this, ListaArticulos.class);
        Bundle b = new Bundle();
        b.putString("volumen", txtVolumen);
        b.putString("numero", txtNumero);
        intent.putExtras(b);
        startActivity(intent);
    }
}
