package com.example.algoritmia.Objects;

import android.graphics.Paint;
import android.graphics.Point;

public class Circle {
    private Point CenterPoint;
    private String Id;
    private int Radius;


    public Circle(Point centerPoint, int radius, String id) {
        CenterPoint = centerPoint;
        Radius = radius;
        Id = id;
    }

    public Point getCenterPoint() {
        return CenterPoint;
    }

    public int getRadius() {
        return Radius;
    }

    public String getId() {
        return Id;
    }
}
