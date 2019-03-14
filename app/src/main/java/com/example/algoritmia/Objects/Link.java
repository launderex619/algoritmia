package com.example.algoritmia.Objects;

import java.util.ArrayList;

public class Link {
    private Vertice Origin;
    private Vertice Destination;
    private String Name;
    private Line Content;

    public Link(Vertice origin, Vertice destination, String name, Line content) {
        Origin = origin;
        Destination = destination;
        Name = name;
        Content = content;
    }

    public Vertice getOrigin() {
        return Origin;
    }

    public void setOrigin(Vertice origin) {
        Origin = origin;
    }

    public Vertice getDestination() {
        return Destination;
    }

    public void setDestination(Vertice destination) {
        Destination = destination;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Line getContent() {
        return Content;
    }

    public void setContent(Line content) {
        Content = content;
    }
}
