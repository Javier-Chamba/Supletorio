package com.example.supletorio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

class AdaptadorRevistas extends ArrayAdapter<Revistas> {
    public AdaptadorRevistas(Context context, ArrayList<Revistas> datos) {
        super(context, R.layout.ly_revistas, datos);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.ly_revistas, null);

        TextView lblVolumen = (TextView)item.findViewById(R.id.txtVolumen);
        lblVolumen.setText("Volumen: "+getItem(position).getVolumen());

        TextView lblNumero = (TextView)item.findViewById(R.id.txtNumero);
        lblNumero.setText("Número: "+getItem(position).getNumero());

        TextView lblAnio = (TextView)item.findViewById(R.id.txtAnio);
        lblAnio.setText("Año: "+getItem(position).getAnio());

        ImageView imageView = (ImageView)item.findViewById(R.id.imgRevista);
        Glide.with(this.getContext())
                .load(getItem(position).getURL())
//.error(R.drawable.imgnotfound)
                .into(imageView);

        return(item);
    }
}
