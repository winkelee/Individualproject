package com.example.individualproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
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

public class MainActivity extends AppCompatActivity implements RecyclerViewOnClickInterface{
    public static ArrayList<Ingredient> ar = new ArrayList<>();
    private RecyclerView lv;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DatabaseReference ingdsDB;
    Toolbar toolbar;
    private IngredientsAdapter ia;
    private SearchView searchBar;
    final static String url= "https://www.povarenok.ru/recipes/~";
    private ArrayList<String> links = new ArrayList<String>(); // лист с ссылками на рецепты
    private String name = null; //имя рецепта
    int l =0;
    public ArrayList<Ingredient> ingList = new ArrayList<>();
    public static ArrayList<Ingredient> userPreferences = new ArrayList<>();
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
        Log.e("Logger", "onCreate: " + ingdsDB);
        lv = findViewById(R.id.CatList);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        searchBar = findViewById(R.id.searchBar); //Работа с поиском ингредиентов
        lv.setLayoutManager(new LinearLayoutManager(this));
        ia = new IngredientsAdapter(getApplicationContext(), ar, this);
        lv.setAdapter(ia);
        fillList();
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) { //Вызывается при применении поиска пользователем
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) { //Вызывается каждый раз когда пользователь меняет текст
                ingList.clear();
                for (Ingredient ing : ar){
                    if (ing.getName().toLowerCase().contains(s.toLowerCase())){
                        ingList.add(ing);
                    }
                }
                ia.setSearchList(ingList);
                return false;
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.newRecipeItem){
            Intent intent = new Intent(MainActivity.this, NewRecipeActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.redactRange){
            Intent intent = new Intent(MainActivity.this, AccountActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.redactIngs){
            Intent intent = new Intent(MainActivity.this, VerifyIngredientsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.logOut){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            FirebaseAuth.getInstance().signOut();
            startActivity(intent);
            return true;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
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



    public void toVerify(View view){
        Intent intent = new Intent(getApplicationContext(), VerifyIngredientsActivity.class);
        startActivity(intent);
    }


    @Override
    public void onItemClick(int i) {

        if (ingList.isEmpty()){
            Ingredient item = new Ingredient(ar.get(i));
            if (userPreferences.contains(item)){
                Toast.makeText(MainActivity.this, "Вы убрали: " + item.getName(), Toast.LENGTH_SHORT).show();
                Log.d("Logger", "onItemClick: " + userPreferences);
                userPreferences.remove(item);
            }
            else{
                userPreferences.add(item);
                Log.d("Logger", "onItemClick: " + userPreferences);
                Toast.makeText(MainActivity.this, "Вы выбрали: " + item.getName(), Toast.LENGTH_SHORT).show();
            }
        } else{
            Ingredient item = new Ingredient(ingList.get(i));
            if (userPreferences.contains(item)){
                Toast.makeText(MainActivity.this, "Вы убрали: " + item.getName(), Toast.LENGTH_SHORT).show();
                Log.d("Logger", "onItemClick: " + userPreferences);
                userPreferences.remove(item);
            }
            else{
                userPreferences.add(item);
                Log.d("Logger", "onItemClick: " + userPreferences);
                Toast.makeText(MainActivity.this, "Вы выбрали: " + item.getName(), Toast.LENGTH_SHORT).show();
            }
        }


    }
}