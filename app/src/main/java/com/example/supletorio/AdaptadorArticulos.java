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

class AdaptadorArticulos extends ArrayAdapter<Articulos> {
    public AdaptadorArticulos(Context context, ArrayList<Articulos> datos) {
        super(context, R.layout.ly_articulos, datos);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.ly_articulos, null);

        TextView lblTitulo = (TextView)item.findViewById(R.id.txtTitulo);
        lblTitulo.setText("Titulo: "+getItem(position).getTitulo());

        TextView lblFcha = (TextView)item.findViewById(R.id.txtFecha);
        lblFcha.setText("Fecha: "+getItem(position).getFecha());

        TextView lblPdf = (TextView)item.findViewById(R.id.txtPdf);
        lblPdf.setText("URL PDF: "+getItem(position).getPdf());

        /*
        ImageView imageView = (ImageView)item.findViewById(R.id.imgArticulo);
        Glide.with(this.getContext())
                .load(getItem(position).getUrl())
//.error(R.drawable.ic_launcher_background)
                .into(imageView);

         */

        return(item);
    }
}
