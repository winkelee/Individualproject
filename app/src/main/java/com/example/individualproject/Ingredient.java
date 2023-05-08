package com.example.individualproject;

public class Ingredient {
    String name;
    String id;
    int id2;

    public Ingredient(String id, String name){
        this.id = id;
        this.name = name;
    }

    public Ingredient(int id2, String name){
        this.id2 = id2;
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + " || " + name;
    }
}