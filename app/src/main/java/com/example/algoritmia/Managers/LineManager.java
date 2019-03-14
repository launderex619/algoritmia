package com.example.algoritmia.Managers;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;

import com.example.algoritmia.Objects.Circle;
import com.example.algoritmia.Objects.Line;

import java.util.ArrayList;

public class LineManager {
    
    private static final int COLOR_WHITE = 0xFFFFFFFF;
    private static final int COLOR_GREEN = 0xFF00FF00;
    private static final int COLOR_BLACK = 0xFF000000;


    private int[][] image;
    private int lineAnchor;
    private ArrayList<Point> line;
    private boolean xLargerThanY;


    public LineManager(int[][] image, int lineAnchor) {
        this.image = image;
        this.lineAnchor = lineAnchor;
    }


    public Line getLine(Circle initialCircle, Circle finalCircle){
        if(line != null)
            line.clear();
        line = new ArrayList<>();
        Point initialPoint = initialCircle.getCenterPoint();
        Point finalPoint = finalCircle.getCenterPoint();

        if(initialPoint.equals(finalPoint.x, finalPoint.y))
            return null;

        //pinto los circulos a blanco para que no interfieran como obstaculos
        paintCircle(initialPoint.x, initialPoint.y, initialCircle.getRadius()+1, COLOR_WHITE);
        paintCircle(finalPoint.x, finalPoint.y, finalCircle.getRadius()+1, COLOR_WHITE);


        int dY = finalPoint.y - initialPoint.y;
        int dX = finalPoint.x - initialPoint.x;
        int x, y, avR, av, avI;

        int incYi, incYr;
        int incXi, incXr;
        if(dY >= 0)
            incYi = 1;
        else {
            dY = -dY;
            incYi = 0;
        }
        if (dX >= 0)
            incXi = 1;
        else{
            dX = -dX;
            incXi = -1;
        }
        if (dX >= dY){
            incYr = 0;
            incXr = incXi;
            xLargerThanY = true;
        }
        else{
            xLargerThanY = false;
            incXr = 0;
            incYr = incYi;
            int temp = dX;
            dX = dY;
            dY = temp;
        }

        x = initialPoint.x;
        y = initialPoint.y;
        avR = 2 * dY;   //avRecto
        av = avR - dX;  //contador
        avI = av - dX;  //avIncrinacion

        do {
            if(!addPoint(x,y, COLOR_GREEN)) { //si no se agrego algun punto es por que hay obstaculo
                paintCircle(initialPoint.x, initialPoint.y, initialCircle.getRadius(), COLOR_BLACK);
                paintCircle(finalPoint.x, finalPoint.y, finalCircle.getRadius(), COLOR_BLACK);
                return null;
            }
            if(av >= 0){
                x += incXi;
                y += incYi;
                av += avI;
            }
            else {
                x += incXr;
                y += incYr;
                av += avR;
            }

        }while(x != finalPoint.x || y != finalPoint.y);

        //regreso los circulos a su color original
        paintCircle(initialPoint.x, initialPoint.y, initialCircle.getRadius(), COLOR_BLACK);
        paintCircle(finalPoint.x, finalPoint.y, finalCircle.getRadius(), COLOR_BLACK);

        return new Line(line, initialPoint, finalPoint, ""+line.size());
    }

    public boolean addPoint(int x,int y, int color){
        try {
            for (int i = -(lineAnchor / 2); i < lineAnchor / 2; i++) {
                if (xLargerThanY) {
                    if (image[y + i][x] != COLOR_WHITE) {
                        if (image[y + i][x] != color)
                            return false;
                    }
                    line.add(new Point(x, y + i));
                } else {
                    if (image[y][x + i] != COLOR_WHITE) {
                        if (image[y][x + i] != color)
                            return false;
                    }
                    line.add(new Point(x + i, y));
                }
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private void paintCircle(int x, int y, int radius, int color) {
        try {
            for (int i = y - radius; i < y + radius; i++)
                for (int j = x - radius; j < x + radius; j++) {
                    if (i >= 0 && j >= 0) {
                        int d = (int) Math.sqrt(((i - y) * (i - y)) + ((j - x) * (j - x)));
                        if (d < radius) {
                            image[i][j] = color;
                        }
                    }
                }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
