package com.example.algoritmia.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.algoritmia.Managers.CircleManager;
import com.example.algoritmia.Managers.GraphManager;
import com.example.algoritmia.Managers.LineManager;
import com.example.algoritmia.Objects.Agente;
import com.example.algoritmia.Objects.Circle;
import com.example.algoritmia.Objects.Vertice;
import com.example.algoritmia.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment implements View.OnClickListener {

    public Bitmap selectedBitmapImage;
    public Bitmap copyBitmapImageWithLines;
    public int [][]originalAnalizedImage;
    private TextView tvName;
    private ImageView ivHolder;
    private Button btnStart;
    private FloatingActionButton fab;
    private SwitchCompat closestPoints;
    private boolean hasImageLoaded;
    private boolean hasGraphLoaded;
    private Button btnAgents;
    private Spinner spnAgents;
    private View view;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        hasImageLoaded = false;
        hasGraphLoaded = false;

        btnAgents = (Button) view.findViewById(R.id.btn_agents);
        spnAgents = (Spinner) view.findViewById(R.id.spnAgents);
        closestPoints = (SwitchCompat) view.findViewById(R.id.puntos_cercanos_switch);
        tvName = (TextView) view.findViewById(R.id.fileName);
        btnStart = (Button) view.findViewById(R.id.start_analysis_button);
        ivHolder = (ImageView) view.findViewById(R.id.image_holder);
        fab = view.findViewById(R.id.fab_icon);

        closestPoints.setEnabled(hasGraphLoaded);
        closestPoints.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnAgents.setOnClickListener(this);
        fab.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {
        if (view == fab) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1);
        }
        else if(view == btnStart){
            if(hasImageLoaded) {
                try {
                    CircleManager circleManager = new CircleManager(selectedBitmapImage);
                    ivHolder.destroyDrawingCache();
                    int imageWidth = selectedBitmapImage.getWidth();
                    int imageHeight = selectedBitmapImage.getHeight();
                    selectedBitmapImage = Bitmap.createBitmap(circleManager.analyze(),
                            imageWidth,
                            imageHeight,
                            Bitmap.Config.ARGB_8888);
                    GraphManager.setGraph(circleManager.getCircles(), circleManager.getLines());
                    selectedBitmapImage = createIdentifiers(circleManager);
                    ivHolder.setImageBitmap(selectedBitmapImage);
                    int imagePixels[] = new int[imageHeight * imageWidth];
                    selectedBitmapImage.getPixels(imagePixels, 0, imageWidth, 0, 0, imageWidth, imageHeight);
                    originalAnalizedImage = new int[imageHeight][imageWidth];
                    int k = 0;
                    for (int i = 0; i < imageHeight; i++) {
                        for (int j = 0; j <imageWidth; j++) {
                            originalAnalizedImage[i][j] = imagePixels[k];
                            k++;
                        }
                    }
                    if(circleManager.isValidImage()) {
                        Toast.makeText(getContext(),
                                " Hay " + circleManager.getCircles().size() + " Circulos",
                                Toast.LENGTH_LONG)
                                .show();
                        hasGraphLoaded = true;
                        closestPoints.setEnabled(hasGraphLoaded);
                        asignaAgentes();
                    }
                    else {
                        Toast.makeText(getContext(),
                                " No es una imagen valida",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(),
                            "Algo salio mal, codigo de error: " + e.toString(),
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
            else{
                Snackbar.make(view, "Debes elegir una imagen primero", Snackbar.LENGTH_LONG)
                        .setAction("Aceptar", HomeFragment.this)
                        .setActionTextColor(getResources().getColor(R.color.colorPrimaryLigth))
                        .show();
            }
        }
        else if(view == closestPoints){
            if(closestPoints.isChecked()){
                if(GraphManager.isLoadedGraphData()) {
                    int[][] copyImage = new int[selectedBitmapImage.getHeight()][selectedBitmapImage.getWidth()];
                    for (int i = 0; i < selectedBitmapImage.getHeight()-1; i++) {
                        for (int j = 0; j < selectedBitmapImage.getWidth()-1; j++) {
                            copyImage[i][j] = originalAnalizedImage[i][j];
                        }
                    }
                    for (Point p : GraphManager.getSmallestLink().getContent().getLine()) {
                        copyImage[p.y][p.x] = 0xFFFF0000;
                    }
                    int newBitmap[] = new int[selectedBitmapImage.getHeight() * selectedBitmapImage.getWidth()];
                    int k = 0;
                    for (int i = 0; i < selectedBitmapImage.getHeight(); i++) {
                        for (int j = 0; j < selectedBitmapImage.getWidth(); j++) {
                            newBitmap[k] = copyImage[i][j];
                            k++;
                        }
                    }
                    Bitmap tempImage = Bitmap.createBitmap(newBitmap,
                            selectedBitmapImage.getWidth(),
                            selectedBitmapImage.getHeight(),
                            Bitmap.Config.ARGB_8888);
                    ivHolder.setImageBitmap(Bitmap.createBitmap(tempImage));
                    copyBitmapImageWithLines = Bitmap.createBitmap(tempImage);
                }
                else {
                    closestPoints.setChecked(false);
                }
            }else{
                ivHolder.setImageBitmap(selectedBitmapImage);
            }
        }
        else if(view == btnAgents){
            if(hasImageLoaded){
                /*
                * Crear el comportamiento de los agentes:
                * */
                String agnts = spnAgents.getSelectedItem().toString();
                int numberOfAgents = Integer.parseInt(agnts);
                ArrayList<Vertice> vertices = new ArrayList<>(GraphManager.getVertices());
                final ArrayList<Vertice> agentsStartLoc = new ArrayList<>();
                final ArrayList<Agente> agentes = new ArrayList<>();

                for (int i = 0; i < numberOfAgents; i++){
                    int pos = (int)(Math.random() * vertices.size());
                    agentsStartLoc.add(vertices.get(pos));
                    vertices.remove(pos);
                }

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        for(int i = 0; i < agentsStartLoc.size(); i++){
                            agentes.add(new Agente(ivHolder, selectedBitmapImage.copy(Bitmap.Config.ARGB_8888, true), agentsStartLoc.get(i)));
                        }
                    }
                };
                r.run();

                for (Agente a : agentes){
                    a.execute(selectedBitmapImage.copy(Bitmap.Config.ARGB_8888, true));
                }
            }
            else
            {
                Snackbar.make(view, "Debes elegir una imagen primero", Snackbar.LENGTH_LONG)
                        .setAction("Aceptar", HomeFragment.this)
                        .setActionTextColor(getResources().getColor(R.color.colorPrimaryLigth))
                        .show();
            }
        }
    }

    private void asignaAgentes() {
        ArrayList<String> nombresVertices = new ArrayList<>();
        for (Vertice v: GraphManager.getVertices()) {
            nombresVertices.add("" + (Integer.parseInt(v.getName()) + 1));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.item_name_agent, nombresVertices);

        spnAgents.setAdapter(adapter);
    }

    private Bitmap createIdentifiers(CircleManager manager) {
        int width = selectedBitmapImage.getWidth();
        int height = selectedBitmapImage.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, selectedBitmapImage.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(selectedBitmapImage, 0, 0, null);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
       // paint.setAlpha(255);
        paint.setTextSize(24f);
        paint.setAntiAlias(true);
        paint.setUnderlineText(true);

        for(Circle circle : manager.getCircles()) {
            Point p = circle.getCenterPoint();
            canvas.drawText( circle.getId(), p.x-10, p.y+10, paint);
        }
        return result;
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                selectedBitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ivHolder.setImageBitmap(selectedBitmapImage);
            tvName.setText(imageUri.toString());
            hasImageLoaded = true;

        }else {
            Toast.makeText(getActivity(), "No seleccionaste ninguna imagen",Toast.LENGTH_LONG).show();
        }
    }

}
