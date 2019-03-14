package com.example.algoritmia.Objects;

import android.graphics.Point;

import java.util.ArrayList;

public class Line {
    private String weight;
    private ArrayList<Point> Line;
    private Point Origin;
    private Point Destination;
    private double distance;

    public Line(ArrayList<Point> line, Point origin, Point destination, String weight) {
        Line = line;
        Origin = origin;
        Destination = destination;
        this.weight = weight;
        distance = Math.sqrt(((destination.x - origin.x ) * (destination.x - origin.x ))
                + ((destination.y - origin.y ) * (destination.y - origin.y )) );
    }

    public ArrayList<Point> getLine() {
        return Line;
    }

    public String getweight() {
        return weight;
    }

    public Point getOrigin() {
        return Origin;
    }

    public Point getDestination() {
        return Destination;
    }

    public double getDistance() { return distance; }
}
