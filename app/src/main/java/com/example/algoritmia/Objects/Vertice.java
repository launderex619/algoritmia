package com.example.algoritmia.Objects;

import java.util.ArrayList;

public class Vertice {
    Circle Circle;
    String Name;
    ArrayList<Link> links;

    public Vertice(com.example.algoritmia.Objects.Circle circle, String name) {
        Circle = circle;
        Name = name;
        this.links = new ArrayList<>();
    }

    public com.example.algoritmia.Objects.Circle getCircle() {
        return Circle;
    }

    public void setCircle(com.example.algoritmia.Objects.Circle circle) {
        Circle = circle;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ArrayList<Link> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Link> links) {
        this.links = links;
    }

    public void addLink(Link link){
        links.add(link);
    }
}
