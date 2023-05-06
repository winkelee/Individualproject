package com.example.individualproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
    String url= "https://www.povarenok.ru/recipes/~";
    public ArrayList<String> links = new ArrayList<String>(); // лист
    public String name = null;
    public String imgUrl = null;
    public TreeSet ingDB = new TreeSet();
    public ArrayList step = new ArrayList();
    public ArrayList ingShowUp = new ArrayList();
    public String descAlt = null;
    public int size = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

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
                    }Log.i("Logger", "run: links is completed");



                    for (int i = 0; i< links.size(); i++){
                        Document parse = Jsoup.connect(links.get(i)).get();
                        Log.d("Logger", "run: The url is " + links.get(i));
                        for (Element row : parse.select("h1")){
                            name = row.getAllElements().text();
                            Log.d("Logger", "run: Name is " + name);
                            debug = i;
                    }
                        for (Element row : parse.select("div.m-img>img").subList(0, Math.min(1, parse.select("div.m-img>img").size()))){
                            String imgUrl = row.getAllElements().attr("src");
                            Log.d("Logger", "run: Image url is " + imgUrl);
                            debug = i;
                        }

                        for (Element row : parse.select("div.ingredients-bl>ul>li>a:first-of-type>span")){
                            String ing = row.getAllElements().text();
                            ingDB.add(ing);


                        }

                        for (Element row : parse.select("div.ingredients-bl>ul>li")){
                            Elements val = row.select("span");
                            String ing;
                            ing = val.text();


                            ingShowUp.add(ing);



                        }






                        Log.d("Logger", "run: Ingredients to show up are  " + ingShowUp.toString());
                        ingShowUp.clear();
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