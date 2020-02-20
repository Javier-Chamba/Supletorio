package com.example.supletorio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.TextAnnotation;
import com.google.api.services.vision.v1.model.WebDetection;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import WebServices.Asynchtask;
import WebServices.WebService;

public class ListaArticulos extends AppCompatActivity implements Asynchtask, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{

    String volumen, numero;
    private ImageView mPhotoImageView;

    public Vision vision;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_articulos);
        Bundle bundle = this.getIntent().getExtras();
        //Construimos el mensaje a mostrar
        volumen=bundle.getString("volumen");
        numero=bundle.getString("numero");

        Map<String, String> datos = new HashMap<String, String>();
        WebService ws= new WebService("http://revistas.uteq.edu.ec/ws/getarticles.php?volumen="+volumen+"&num="+numero, datos,
                ListaArticulos.this, this);
        ws.execute("");

        ListView lstOpciones = (ListView) findViewById(R.id.lstListado);
        lstOpciones.setOnItemClickListener(this);

        getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        getPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

        mPhotoImageView = findViewById(R.id.imgArticulo);


        Vision.Builder visionBuilder = new Vision.Builder(new NetHttpTransport(),
                new AndroidJsonFactory(),  null);
        visionBuilder.setVisionRequestInitializer(new VisionRequestInitializer(""));
        vision = visionBuilder.build();

    }

    @Override
    public void processFinish(String result) throws JSONException {
        Log.i("processFinish", result);
        ArrayList<Articulos> articuloList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(result);
        JSONArray articulos = jsonObject.getJSONArray("articles");

        articuloList=Articulos.JsonObjectsBuild(articulos);

        TextoImagen();

        //Adaptador
        AdaptadorArticulos adaptadorArticulos = new AdaptadorArticulos(this, articuloList);

        //AdapterView
        // ListView
        ListView lstOpciones = (ListView)findViewById(R.id.lstListado);
        lstOpciones.setAdapter(adaptadorArticulos);
    }

    public void getPermission(String permission){

        if (Build.VERSION.SDK_INT >= 23) {
            if (!(checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED))
                ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            //Toast.makeText(this.getApplicationContext(),"OK", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(((Articulos)adapterView.getItemAtPosition(position)).getPdf()));
        request.setDescription("PDF Paper");
        request.setTitle(((Articulos)adapterView.getItemAtPosition(position)).getTitulo());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "filedownload.pdf");
        DownloadManager manager = (DownloadManager) this.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        try {
            manager.enqueue(request);        }
        catch (Exception e) {
            Toast.makeText(this.getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void TextoImagen(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                /*
                ImageView imagen=(ImageView)findViewById(R.id.imgArticulo);
                BitmapDrawable drawable = (BitmapDrawable) imagen.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                bitmap = scaleBitmapDown(bitmap, 1200);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                byte[] imageInByte = stream.toByteArray();

                //1.Paso
                Image inputImage = new Image();
                inputImage.encodeContent(imageInByte);

                 */

                //2.Feature
                Feature desiredFeature = new Feature();
                desiredFeature.setType("WEB_ENTITIES");

                //3.Arma la solicitud(es)
                AnnotateImageRequest request = new AnnotateImageRequest();
                String casa="casa";
                request.set("casa",casa);
                request.setFeatures(Arrays.asList(desiredFeature));
                BatchAnnotateImagesRequest batchRequest =  new BatchAnnotateImagesRequest();
                batchRequest.setRequests(Arrays.asList(request));


                //4. Asignamos al control VisionBuilder la solicitud
                try {
                    Vision.Images.Annotate annotateRequest  = vision.images().annotate(batchRequest);

                    //5. Enviamos la solicitud y obtenemos la respuesta
                    annotateRequest.setDisableGZipContent(true);
                    BatchAnnotateImagesResponse batchResponse  = annotateRequest.execute();


                    //6. Obtener la respuesta
                    TextAnnotation text = batchResponse.getResponses().get(0).getFullTextAnnotation();
                    String message="";
                    if(text!=null){
                        message=text.getText();
                    }else{
                        message="No hay texto";
                    }
                    final String result = message;

                    //Asignar la respuesta a la UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //TextView imageDetail = (TextView)findViewById(R.id.textView2);
                            //imageDetail.setText(result);
                        }
                    });

                }catch(IOException e) {
                    e.getStackTrace();
                }





            }
        });

    }

    /*

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;
        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

     */


}
