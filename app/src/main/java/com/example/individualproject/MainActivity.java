package com.example.individualproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<Ingredient> ar = new ArrayList<>();
    ListView lv;
    private DatabaseReference ingdsDB;
    private IngredientsAdapter ia;
    private SearchView searchBar;
    final static String url= "https://www.povarenok.ru/recipes/~";
    private ArrayList<String> links = new ArrayList<String>(); // лист с ссылками на рецепты
    private String name = null; //имя рецепта
    int l =0;
    private String imgUrl = null;  //Ссылка на картинку рецепта
    private TreeSet ingDB = new TreeSet(); //Массив (дерево) со всеми ингредиентами, повторений нет
    private TreeSet ing1 = new TreeSet(); //Массив (дерево) для поиска
    private ArrayList step = new ArrayList(); //Массив с шагами по приготовлению
    private ArrayList ingShowUp = new ArrayList(); //Массив для ингредиентов, которые будут показаны в приложениии
    private String descAlt = null; //Альтернативная версия шагов по приготовлению. Для рецептов, в которых есть только описание
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        ingdsDB = FirebaseDatabase.getInstance().getReference("Ingredients");
        searchBar = findViewById(R.id.searchBar); //Работа с поиском ингредиентов
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) { //Вызывается при применении поиска пользователем
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) { //Вызывается каждый раз когда пользователь меняет текст
                ArrayList<Ingredient> ingList = new ArrayList<>();
                for (Ingredient ing : ar){
                    if (ing.getName().toLowerCase().contains(s.toLowerCase())){
                        ingList.add(ing);
                    }
                }
                IngredientsAdapter ia = new IngredientsAdapter(0, getApplicationContext(), ingList);
                lv.setAdapter(ia);
                return false;
            }
        });

        fillList();
        listMethod();
        listenerMethod();
    }



    private void listMethod(){ //Устанавливает адаптер и показывает лист в активити
         lv = findViewById(R.id.CatList);
        ia = new IngredientsAdapter(0, getApplicationContext(), ar);
        lv.setAdapter(ia);

    }


    private void fillList(){
        ValueEventListener  valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!ar.isEmpty()){
                    ar.clear();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    l++;
                    Ingredient ingredient = dataSnapshot.getValue(Ingredient.class);
                    if (ingredient != null){
                        ingredient.setId2(l);
                        ar.add(ingredient);
                    }

                }
                ia.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ingdsDB.addValueEventListener(valueEventListener);

    }

    private void listenerMethod(){ //Простой метод для распознавания нажатия на элемент
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Ingredient item = (Ingredient) lv.getItemAtPosition(i); //элемент, на который нажал пользователь
                Intent detailedCat = new Intent(getApplicationContext(), activity_ingredients.class); //intent для перехода в другую активность

                //передача обьекта в activity_ingredients с ключом id и значением id выбранного элемента
                detailedCat.putExtra("id", item.getId2());
                startActivity(detailedCat);
            }
        });
    }



}