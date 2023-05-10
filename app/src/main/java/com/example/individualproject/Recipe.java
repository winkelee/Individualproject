package com.example.individualproject;

import com.google.firebase.database.core.utilities.Tree;

import java.util.ArrayList;
import java.util.TreeSet;

public class Recipe {
    public String id; //Класс рецепта для добавления в базу данных
    public String name = "RECIPE_NAME";
    public String imgUrl = "IMAGE_URL";
    int id2;
    public ArrayList ing = new ArrayList(); //Ингредиенты для поиска
    public ArrayList ingShowUp = new ArrayList(); //Ингредиенты для показа в приложении
    public String descAlt = "ALTERNATIVE_DESCRIPTION"; //Для рецептов без пошагового приготовления
    public ArrayList step = new ArrayList(); //Массив с пунктами приготовления

    public Recipe(String id, String name, String imgUrl, ArrayList ing, ArrayList ingShowUp, ArrayList step){
        this.name = name;
        this.id = id;
        this.imgUrl = imgUrl;
        this.ing = ing;
        this.step = step;
        this.ingShowUp = ingShowUp;
    }

    public Recipe(String id, String name, String imgUrl, ArrayList ing, ArrayList ingShowUp, String descAlt){
        this.name = name;
        this.imgUrl = imgUrl;
        this.ing = ing;
        this.id = id;
        this.descAlt = descAlt;
        this.ingShowUp = ingShowUp;
    }

    public Recipe(int id2, String name, String imgUrl, ArrayList ing, ArrayList ingShowUp, ArrayList step){
        this.name = name;
        this.id2 = id2;
        this.imgUrl = imgUrl;
        this.ing = ing;
        this.step = step;
        this.ingShowUp = ingShowUp;
    }

    public Recipe(int id2, String name, String imgUrl, ArrayList ing, ArrayList ingShowUp, String descAlt){
        this.name = name;
        this.imgUrl = imgUrl;
        this.ing = ing;
        this.id2 = id2;
        this.descAlt = descAlt;
        this.ingShowUp = ingShowUp;
    }

    public Recipe(){

    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public ArrayList getIng() {
        return ing;
    }

    public ArrayList getIngShowUp() {
        return ingShowUp;
    }

    public String getDescAlt() {
        return descAlt;
    }

    public ArrayList getStep() {
        return step;
    }

    public void setId2(int id2) {
        this.id2 = id2;
    }

    @Override
    public String toString() {
        if (descAlt == "ALTERNATIVE_DESCRIPTION"){
            return id + " || " + name + " || " + imgUrl + " || " + ing + " || " + ingShowUp + " || " + step;
        }else{
            return id + " || " + name + " || " + imgUrl + " || " + ing + " || " + ingShowUp + " || " + descAlt;
        }
    }
}
