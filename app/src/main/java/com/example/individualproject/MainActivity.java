package com.example.individualproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {
    public static ArrayList ar = new ArrayList();
    ListView lv;
    private DatabaseReference ingdsDB;
    final static String url= "https://www.povarenok.ru/recipes/~";
    public ArrayList<String> links = new ArrayList<String>(); // лист с ссылками на рецепты
    public String name = null; //имя рецепта
    public String imgUrl = null;  //Ссылка на картинку рецепта
    public TreeSet ingDB = new TreeSet(); //Массив (дерево) со всеми ингредиентами, повторений нет
    public TreeSet ing1 = new TreeSet(); //Массив (дерево) для поиска
    public ArrayList step = new ArrayList(); //Массив с шагами по приготовлению
    public ArrayList ingShowUp = new ArrayList(); //Массив для ингредиентов, которые будут показаны в приложениии
    public String descAlt = null; //Альтернативная версия шагов по приготовлению. Для рецептов, в которых есть только описание
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        ingdsDB = FirebaseDatabase.getInstance().getReference("Ingredients");


        fillList();
        listMethod();
        listenerMethod();
    }



    private void listMethod(){ //Устанавливает адаптер и показывает лист в активити
         lv = findViewById(R.id.CatList);
        IngredientsAdapter ica = new IngredientsAdapter(0, getApplicationContext(), ar);
        lv.setAdapter(ica);

    }


    private void fillList(){ //Метод заполняет List элементами. Можно было реализовать и в onCreate, но для красоты кода он находится здесь.




        Ingredient ic1 = new Ingredient(0, "Макароны");
        Ingredient ic2 = new Ingredient(1, "Хлебобулочные изделия");
        Ingredient ic3 = new Ingredient(2, "Овощи");
        Ingredient ic4 = new Ingredient(3, "Фрукты");
        Ingredient ic5 = new Ingredient(4, "Ягоды");
        Ingredient ic6 = new Ingredient(5, "Мясо");
        Ingredient ic7 = new Ingredient(6, "Рыба");
        Ingredient ic8 = new Ingredient(7, "Грибы");

        ar.add(ic1);
        ar.add(ic2);
        ar.add(ic3);
        ar.add(ic4);
        ar.add(ic5);
        ar.add(ic6);
        ar.add(ic7);
        ar.add(ic8);
    }

    private void listenerMethod(){ //Простой метод для распознавания нажатия на элемент
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Ingredient item = (Ingredient) lv.getItemAtPosition(i); //элемент, на который нажал пользователь
                Intent detailedCat = new Intent(getApplicationContext(), activity_ingredients.class); //intent для перехода в другую активность

                //передача обьекта в activity_ingredients с ключом id и значением id выбранного элемента
                detailedCat.putExtra("id", item.getId());
                startActivity(detailedCat);
            }
        });
    }

}