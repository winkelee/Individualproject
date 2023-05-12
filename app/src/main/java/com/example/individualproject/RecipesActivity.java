package com.example.individualproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.fasterxml.jackson.core.JsonParser;
import java.util.ArrayList;
import java.util.HashMap;


public class RecipesActivity extends AppCompatActivity {

    private static ArrayList userPreferences = new ArrayList(VerifyIngredientsActivity.userPreferences);
    private ListView listView;
    private ProgressBar loadingView;
    private RecipesAdapter recAdapter;
    private SearchView searchBar;
    public static String recName;
    public static String recSteps;
    public static String recImage;
    public static String recIngs;
    private String TAG = "Logger";
    private ArrayList searchList = new ArrayList();
    private ArrayList<Recipe> showUpRecipe= new ArrayList<>();
    int equalCount = 0;
    int l = 0;
    private DatabaseReference recipeDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        getSupportActionBar().hide();
        recipeDB = FirebaseDatabase.getInstance().getReference("Recipes");
        listMethod();
        fillList();
        listenerMethod();
        loadingView = findViewById(R.id.loadingView);
        searchBar = findViewById(R.id.recSearch); //Работа с поиском ингредиентов
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) { //Вызывается при применении поиска пользователем
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) { //Вызывается каждый раз когда пользователь меняет текст
                ArrayList<Recipe> recList = new ArrayList<>();
                for (Recipe rec : showUpRecipe){
                    if (rec.getName().toLowerCase().contains(s.toLowerCase())){
                        recList.add(rec);
                    }
                }
                RecipesAdapter ia = new RecipesAdapter(0, getApplicationContext(), recList);
                listView.setAdapter(ia);
                return false;
            }
        });
    }

    private void listMethod(){
        listView = findViewById(R.id.recList);
        recAdapter = new RecipesAdapter(1, getApplicationContext(), showUpRecipe);
        listView.setAdapter(recAdapter);

    }

    private void fillList(){
        Log.d(TAG, "fillList: " + userPreferences);
    ValueEventListener vel = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (!showUpRecipe.isEmpty()){
                showUpRecipe.clear();
            }

            if (userPreferences.isEmpty()){
                for(DataSnapshot ds : snapshot.getChildren()){
                    HashMap<String, String> recipe1 = (HashMap<String, String>) ds.getValue();
                    Log.d("Logger", "onDataChange: " + recipe1.values() + l);

                    l++;
                    ObjectMapper objectMapper = new ObjectMapper();
                    Recipe recipe = objectMapper.convertValue(recipe1, Recipe.class);
                    Log.d("Logger", "onDataChange: " + recipe);
                    showUpRecipe.add(recipe);
                }
                recAdapter.notifyDataSetChanged();
            } else{
                for(DataSnapshot ds : snapshot.getChildren()){
                    HashMap<String, String> recipe1 = (HashMap<String, String>) ds.getValue();
                    Log.d("Logger", "onDataChange: " + recipe1.values() );
                    l++;
                    ObjectMapper objectMapper = new ObjectMapper();
                    Recipe recipe = objectMapper.convertValue(recipe1, Recipe.class);
                    searchList = recipe.getIng();
                    for (int count = 0; count<userPreferences.size(); count++){
                        Ingredient ing = (Ingredient) userPreferences.get(count);
                            if (searchList.contains(ing.getName())){
                                equalCount++;
                            }
                    }
                    if (equalCount > searchList.size()/3){
                        showUpRecipe.add(recipe);
                    }
                    Log.d(TAG, "onDataChange: " + showUpRecipe);
                    equalCount = 0;
                }
                recAdapter.notifyDataSetChanged();
            }

            loadingView.setVisibility(View.GONE);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
recipeDB.addValueEventListener(vel);
    }

    private void listenerMethod(){ //Простой метод для распознавания нажатия на элемент
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Recipe item = (Recipe) listView.getItemAtPosition(i); //элемент, на который нажал пользователь
                Intent intent = new Intent(getApplicationContext(), DetailedRecipeActivity.class);
                ArrayList showUpIngs = new ArrayList();
                showUpIngs = item.getIngShowUp();
                ArrayList recStepsCopy = new ArrayList();
                recStepsCopy= item.getStep();
                recIngs = "";
                recSteps = "";
                recName = item.getName();
                recImage = item.getImgUrl();
                for (int count = 0; count< showUpIngs.size(); count++){
                    recIngs = recIngs + " \n " + " \n " + (count+1) + ". " + showUpIngs.get(count);
                }
                if (recStepsCopy.isEmpty()){
                    recSteps = item.getDescAlt();
                }if(item.getDescAlt().contains("Нравятся наши рецепты?")){
                    recSteps = "Рецепт незакончен пользователем";
                }
                else{
                    for (int count = 0; count< recStepsCopy.size(); count++){
                        recSteps = recSteps + " \n " + " \n " + (count+1) + ". " + recStepsCopy.get(count);
                    }
                }


                startActivity(intent);

            }
        });
    }

}