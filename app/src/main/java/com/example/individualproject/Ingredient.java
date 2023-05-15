package com.example.individualproject;

public class Ingredient {
    String name;
    String id;
    int id2;

    public Ingredient(String id, String name){
        this.id = id;
        this.name = name;
    }

    public Ingredient(){

    }

    public Ingredient(Ingredient ingredient){
        this.name = ingredient.getName();
        this.id = ingredient.getId();
    }

    public Ingredient(int id2, String name){
        this.id2 = id2;
        this.name = name;
    }

    public void setId2(int id2) {
        this.id2 = id2;
    }

    public String getId() {
        return id;
    }
    public int getId2() {
        return id2;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + " || " + name;
    }
}
