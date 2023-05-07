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
    private DatabaseReference recipeDB;
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
        recipeDB = FirebaseDatabase.getInstance().getReference("Recipes");
        Thread thread = new Thread(new Runnable() { // новый поток для обхода NetworkFromMainThreadException
            int debug = 0;

            @Override
            public void run() {
                try {

                    for (int i = 1; i< 100; i++){ //Программа проходит по первым 100 страницам с рецептами и собирает на них ссылки
                        Document parse = Jsoup.connect(url + i).get(); //Работает по такому принципу: добавляет к ссылке в конце число i и переходит по ней, вынимая данные
                        Log.i("Logger", "run: adding to links");
                        for (Element row : parse.select("article.item-bl>h2>a[href]")){
                            String link = row.getAllElements().attr("href");
                            links.add(link);
                            Log.i("Logger", "run: link added " + link +" " + i);
                        }
                    }

                    Log.i("Logger", "run: links is completed");

                    for (int i = 833; i< links.size(); i++){ // Для каждой ссылки отрабатывают следующие циклы
                        Document parse = Jsoup.connect(links.get(i)).get();
                        Log.d("Logger", "run: The url is " + links.get(i));

                        for (Element row : parse.select("h1")){ //Получаем имя рецепта
                            name = row.getAllElements().text();
                            debug = i;
                        }
                        for (Element row : parse.select("div.m-img>img").subList(0, Math.min(1, parse.select("div.m-img>img").size()))){ //Получаем картинку
                            imgUrl = row.getAllElements().attr("src"); //Цикл берёт первую картинку в блоке рецепта и получает ссылку, потом мы её расшифруем (не здесь)
                            debug = i;
                        }

                        for (Element row : parse.select("div.ingredients-bl>ul>li>a:first-of-type>span")){ //Получение всех ингредиентов со 100 страниц и запись в дерево без повторений
                            String ing = row.getAllElements().text();
                            ingDB.add(ing);
                            ing1.add(ing);

                        }

                        for (Element row : parse.select("div.ingredients-bl>ul>li")){ //Получение ингредиентов для показа в рецепте
                            Elements val = row.select("span");
                            String ing;
                            ing = val.text();
                            ingShowUp.add(ing);
                        }

                        for (Element row : parse.select("ul>li.cooking-bl>div>p")){ //Получение шагов по рецепту
                            String stepElement = row.getAllElements().text();
                            step.add(stepElement);
                        }

                        for (Element row : parse.select("article.item-bl.item-about>div>div:not([class])")){ //Получение описания
                            descAlt = row.getAllElements().text();
                        }

                        if (step.isEmpty()){
                            String id = recipeDB.getKey();
                            ArrayList ing11 = new ArrayList(ing1);
                            Recipe recipe = new Recipe(id, name, imgUrl, ing11, ingShowUp, descAlt);
                            Log.d("Logger", "run: Recipe is  " + recipe);
                            recipeDB.push().setValue(recipe);
                        } else{
                            String id = recipeDB.getKey();
                            ArrayList ing11 = new ArrayList(ing1);
                            Recipe recipe = new Recipe(id, name, imgUrl, ing11, ingShowUp, step);
                            Log.d("Logger", "run: Recipe is  " + recipe);
                            recipeDB.push().setValue(recipe);
                        }


                        ingShowUp.clear();
                        step.clear();
                        ing1.clear();
                        Log.d("Logger", "run: The url is checked " + links.get(i) + " " + i);

                    }




                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("Logger", "run: Unable to scrape the url by the number " + (debug+1));
                }

            }
        });
        thread.start();

        fillList();
        listMethod();
        listenerMethod();
    }



    private void listMethod(){ //Устанавливает адаптер и показывает лист в активити
         lv = findViewById(R.id.CatList);
        IngredientsCategoryAdapter ica = new IngredientsCategoryAdapter(0, getApplicationContext(), ar);
        lv.setAdapter(ica);

    }


    private void fillList(){ //Метод заполняет List элементами. Можно было реализовать и в onCreate, но для красоты кода он находится здесь.

        IngredientCategory ic1 = new IngredientCategory(0, "Макароны");
        IngredientCategory ic2 = new IngredientCategory(1, "Хлебобулочные изделия");
        IngredientCategory ic3 = new IngredientCategory(2, "Овощи");
        IngredientCategory ic4 = new IngredientCategory(3, "Фрукты");
        IngredientCategory ic5 = new IngredientCategory(4, "Ягоды");
        IngredientCategory ic6 = new IngredientCategory(5, "Мясо");
        IngredientCategory ic7 = new IngredientCategory(6, "Рыба");
        IngredientCategory ic8 = new IngredientCategory(7, "Грибы");

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
                IngredientCategory item = (IngredientCategory) lv.getItemAtPosition(i); //элемент, на который нажал пользователь
                Intent detailedCat = new Intent(getApplicationContext(), activity_ingredients.class); //intent для перехода в другую активность

                //передача обьекта в activity_ingredients с ключом id и значением id выбранного элемента
                detailedCat.putExtra("id", item.getId());
                startActivity(detailedCat);
            }
        });
    }

}