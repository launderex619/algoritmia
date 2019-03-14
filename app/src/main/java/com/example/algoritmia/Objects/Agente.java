package com.example.algoritmia.Objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Agente extends AsyncTask<Bitmap, Bitmap, Bitmap> {

    private ImageView holder;
    private Bitmap originalBitmap;
    private Vertice startVertice;
    private ArrayList<Vertice> verticesRecorridos = new ArrayList<>();


    public Agente(ImageView holder, Bitmap originalBmp, Vertice vertice){
        super();
        this.holder = holder;
        this.originalBitmap = originalBmp;
        this.startVertice = vertice;
    }

    private Bitmap createCircle(Bitmap bmp, float x, float y, float r){
        Canvas canvas = new Canvas(bmp);
        Paint black = new Paint();
        black.setColor(Color.GREEN);
        canvas.drawCircle(x,y,r, black);
        return bmp;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //dibujar los circulitos en el bitmap

    }

    @Override
    protected Bitmap doInBackground(Bitmap... bitmaps) {
        //bitmaps[0] = Bitmap.createBitmap(originalBitmap);
        Bitmap bmp = bitmaps[0];
        ArrayList<Vertice> verticesRecorridos = new ArrayList<>();
        boolean canRun = true;
        synchronized (this) {
            try {
                if (startVertice.getLinks().size() > 0) {
                    Vertice actualVer = startVertice;
                    do {
                        verticesRecorridos.add(actualVer);
                        int numeroArista = (int)(Math.random() * actualVer.getLinks().size()-1);

                        for (int i = 0; i < actualVer.getLinks().get(numeroArista).getContent().getLine().size(); i++) {
                            bmp = bitmaps[0].copy(Bitmap.Config.ARGB_8888, true);
                            Point actual = actualVer.getLinks().get(0).getContent().getLine().get(i);
                            createCircle(bmp, actual.x, actual.y, 15);
                            publishProgress(bmp);
                            //wait(1);
                        }
                        int rec = actualVer.getLinks().size();
                        boolean continuar = false;
                        for (int i = 0; i < rec; i++){
                            if(verticesRecorridos.indexOf(actualVer.getLinks().get(i).getDestination()) < 0){
                                actualVer = actualVer.getLinks().get(i).getDestination();
                                continuar = true;
                                break;
                            }
                        }
                        canRun = continuar;
                    } while (canRun);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bmp;
    }

    @Override
    protected void onProgressUpdate(Bitmap... values) {
        super.onProgressUpdate(values);
        holder.setImageBitmap(values[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
    }
}
