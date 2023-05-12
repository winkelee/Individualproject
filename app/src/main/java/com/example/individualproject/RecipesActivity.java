package com.example.individualproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

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


}