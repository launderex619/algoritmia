package com.example.algoritmia.Managers;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.example.algoritmia.Objects.Circle;
import com.example.algoritmia.Objects.Line;

import java.util.ArrayList;

public class CircleManager {

    private static final int LINE_WIDTH = 2;
    private static final int COLOR_BLACK = 0xFF000000;
    private static final int COLOR_GREEN = 0xFF00FF00;
    private static final int COLOR_LIGTH_BLACK = 0xFF010101;
    

    private ArrayList<Circle> Circles;
    private ArrayList<Line> Lines;
    private Bitmap Image;
    private int width;
    private int heigth;
    private boolean isValidImage;

    public CircleManager(Bitmap image) {
        this.Image = image;
        this.width = image.getWidth();
        this.heigth = image.getHeight();
        this.Circles = new ArrayList<>();
        this.Lines = new ArrayList<>();
        this.isValidImage = true;
    }

    /*public void paintClosestLine(Line line){

    }*/

    public int[] analyze(){
        int[] pixels1 = new int[width * heigth];
        Image.getPixels(pixels1, 0, width, 0, 0, width, heigth);

        int[][] pixels = new int[heigth][width];
        for(int i = 0, y= 0; y < heigth;y++){
            for (int x = 0; x < width; x++) {
                pixels[y][x] = pixels1[i];
                i++;
            }
        }

        //verifico que no tenga nada negro en el limite de la imagen
        isValidImage = checkIsValidImage(pixels, heigth, width);

        if(isValidImage) {
            for (int y = 0; y < heigth; y++) {
                for (int x = 0; x < width; x++) {
                    if (pixels[y][x] == COLOR_BLACK) {
                        findCenter(pixels, heigth, width, y, x);
                    }
                }
            }
        }

        //dibujo las lineas de ser posible
        for (int i = 0; i < Circles.size()-1; i++) {
            Circle initialC = Circles.get(i);
            for (int j = i+1; j < Circles.size(); j++) {
                Circle finalC = Circles.get(j);
                Line line = new LineManager(pixels, LINE_WIDTH)
                        .getLine(initialC, finalC);

                if(line != null) {
                    for (Point p : line.getLine()) {
                        pixels[p.y][p.x] = COLOR_GREEN;
                    }
                    Lines.add(line);
                }
            }
        }

        //pinto los circulos sobre las lineas creadas
        for (Circle c: Circles) {
            drawCircle(
                    c.getCenterPoint().x,
                    c.getCenterPoint().y,
                    c.getRadius(),
                    pixels,
                    COLOR_BLACK);
        }

        //convierto el arreglo bidimencional en unidimencional
        for (int i = 0, y = 0; y < heigth; y++) {
            for (int x = 0; x < width; x++) {
                pixels1[i] = pixels[y][x];
                i++;
            }
        }
        if(!isValidImage)
            Circles.clear();

        return pixels1;
    }

    private boolean checkIsValidImage(int[][] pixels, int heigth, int width) {
        for (int i = 0; i < heigth; i++){
            if(pixels[i][0] == COLOR_BLACK && pixels[i][width-1] == COLOR_BLACK)
                return false;
            if(pixels[0][i] == COLOR_BLACK && pixels[heigth-1][i] == COLOR_BLACK)
                return false;
        }
        return true;
    }

    private void findCenter(int[][] pixels, int heigth, int width, int y, int x) {
        int widthMiddle, heigthMiddle;
        int radius;
        //saco el ancho
        int pos = x;
        while(pixels[y][x] == COLOR_BLACK){
            //evito el desbordamiento
            if(x + 1 == width) {
                isValidImage = false;
                break;
            }
            x++;
        }

        widthMiddle = (x+pos)/2;

        //saco el alto
        pos = y;
        while(pixels[y][widthMiddle] == COLOR_BLACK){
            //evito el desbordamiento
            if(y +1 == heigth) {
                isValidImage = false;
                break;
            }
            y++;
        }


        heigthMiddle = (y+pos)/2;
        //saco el radio y verifico que el circulo sea circular >:v
        int heigthTemp;
        pos = 0;
        while (pixels[heigthMiddle+pos][widthMiddle] == COLOR_BLACK)
            pos++;
        heigthTemp = pos;
        pos = 0;
        while (pixels[heigthMiddle][widthMiddle+pos] == COLOR_BLACK)
            pos++;

        if(heigthTemp < pos)
            radius = pos;
        else
            radius = heigthTemp;

        //hago esto por si el ancho de lo que detecto es 30% mayor al alto o viceversa,
        //si esto es valido entonces no estoy comparando un circulo y no se que es.
        if (!((heigthTemp * 1.3) < radius || heigthTemp > (radius*1.3)))
            if(isValidImage) {
            Point center = new Point(widthMiddle, heigthMiddle);
            Circle circle = new Circle(center, ++radius, "" +Circles.size());
            Circles.add(circle);
            drawCircle(widthMiddle, heigthMiddle, radius, pixels, COLOR_LIGTH_BLACK);
            }

    }

    private void drawCircle(int x,int y,int radius, int[][] pixels, int color){
        try {
            for (int i = y - radius; i < y + radius; i++)
                for (int j = x - radius; j < x + radius; j++) {
                    if (i >= 0 && j >= 0) {
                        int d = (int) Math.sqrt(((i - y) * (i - y)) + ((j - x) * (j - x)));
                        if (d < radius) {
                            pixels[i][j] = color;
                        }
                    } else {
                        isValidImage = false;
                        break;
                    }
                }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Circle> getCircles() {
        return Circles;
    }

    public ArrayList<Line> getLines() {
        return Lines;
    }

    public boolean isValidImage() {
        return isValidImage;
    }
}
