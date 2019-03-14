package com.example.algoritmia.Managers;

import android.graphics.Point;

import com.example.algoritmia.Objects.Circle;
import com.example.algoritmia.Objects.Line;
import com.example.algoritmia.Objects.Link;
import com.example.algoritmia.Objects.Vertice;

import java.util.ArrayList;

public class GraphManager {

    private static boolean isLoadedGraphData = false;
    private static ArrayList<Vertice> vertices = new ArrayList<>();

    public static Link getSmallestLink() {
        return smallestLink;
    }

    public static void setSmallestLink(Link smallestLink) {
        GraphManager.smallestLink = smallestLink;
    }

    private static Link smallestLink;

    public static void setGraph(ArrayList<Circle> circles, ArrayList<Line> lines) {
        if(vertices != null)
            vertices = new ArrayList<>();
        for (int i = 0; i < circles.size(); i++) {
            Vertice v = new Vertice(circles.get(i), "" + i);
            vertices.add(v);
        }

        for (int i = 0; i < lines.size(); i++) {
            Line actualLine = lines.get(i);
            Link link = new Link(
                    getVertice(actualLine.getOrigin()),
                    getVertice(actualLine.getDestination()),
                    "" + i,
                    actualLine);
            vertices.get(vertices.indexOf(link.getOrigin())).addLink(link);
        }

    }

    private static Vertice getVertice(Point point){
        for (Vertice v: vertices) {
            if(v.getCircle().getCenterPoint().equals(point.x, point.y))
                return v;
        }
        return null;
    }

    public static void addVertice(Vertice vertice){
        vertices.add(vertice);
    }

    public static ArrayList<Vertice> getVertices() {
        return vertices;
    }

    public static void setVertices(ArrayList<Vertice> vertices) {
        GraphManager.vertices = vertices;
    }

    public static boolean isLoadedGraphData() {
        return isLoadedGraphData;
    }

    public static void setLoadedGraphData(boolean loadedGraphData) {
        isLoadedGraphData = loadedGraphData;
    }
}

