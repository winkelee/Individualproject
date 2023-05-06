package com.example.individualproject;

import java.util.ArrayList;

public class Recipe { //Класс рецепта для добавления в базу данных
    public String name = "RECIPE_NAME";
    public String imgUrl = "IMAGE_URL";
    public ArrayList ing = new ArrayList(); //Ингредиенты для поиска
    public ArrayList ingShowUp = new ArrayList(); //Ингредиенты для показа в приложении
    public String descAlt = "ALTERNATIVE_DESCRIPTION"; //Для рецептов без пошагового приготовления
    public ArrayList step = new ArrayList(); //Массив с пунктами приготовления

    public Recipe(String name, String imgUrl, ArrayList ing, ArrayList step){
        this.name = name;
        this.imgUrl = imgUrl;
        this.ing = ing;
        this.step = step;
    }

    public Recipe(String name, String imgUrl, ArrayList ing, String descAlt){
        this.name = name;
        this.imgUrl = imgUrl;
        this.ing = ing;
        this.descAlt = descAlt;
    }
}
