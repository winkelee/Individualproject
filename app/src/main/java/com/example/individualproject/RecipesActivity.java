package com.example.individualproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.fasterxml.jackson.core.JsonParser;
import java.util.ArrayList;
import java.util.HashMap;


public class RecipesActivity extends AppCompatActivity implements RecyclerViewOnClickInterface{

    private RecyclerView recyclerView;
    private ProgressBar loadingView;
    private RecipesAdapter recAdapter;
    private SearchView searchBar;
    public static String recName;
    Toolbar toolbar;
    ArrayList<Recipe> recList = new ArrayList<>();
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
        recipeDB = FirebaseDatabase.getInstance().getReference("Recipes");
        listMethod();
        fillList();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        loadingView = findViewById(R.id.loadingView);
        searchBar = findViewById(R.id.recSearch); //Работа с поиском ингредиентов
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) { //Вызывается при применении поиска пользователем
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) { //Вызывается каждый раз когда пользователь меняет текст
                recList.clear();
                for (Recipe rec : showUpRecipe){
                    if (rec.getName().toLowerCase().contains(s.toLowerCase())){
                        recList.add(rec);
                    }
                }
                recAdapter.setSearchList(recList);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.newRecipeItem){
            Intent intent = new Intent(RecipesActivity.this, NewRecipeActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.redactRange){
            Intent intent = new Intent(RecipesActivity.this, AccountActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.redactIngs){
            Intent intent = new Intent(RecipesActivity.this, VerifyIngredientsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.logOut){
            Intent intent = new Intent(RecipesActivity.this, LoginActivity.class);
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

    private void listMethod(){
        recyclerView = findViewById(R.id.recList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recAdapter = new RecipesAdapter(getApplicationContext(), showUpRecipe, this);
        recyclerView.setAdapter(recAdapter);

    }

    private void fillList(){
        Log.d(TAG, "fillList: " + MainActivity.userPreferences);
    ValueEventListener vel = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (!showUpRecipe.isEmpty()){
                showUpRecipe.clear();
            }


            if (MainActivity.userPreferences.isEmpty()){
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
                    for (int count = 0; count<MainActivity.userPreferences.size(); count++){
                        Ingredient ing = (Ingredient) MainActivity.userPreferences.get(count);
                            if (searchList.contains(ing.getName())){
                                equalCount++;
                            }
                    }
                    if (AccountActivity.rangeInt != -1 && equalCount >= 1){
                        if (equalCount >= searchList.size() - AccountActivity.rangeInt){
                            showUpRecipe.add(recipe);
                            Log.d(TAG, "onDataChange: " + recipe);
                        }
                    }
                    else{
                        if (searchList.size()/3 !=0 && equalCount >= searchList.size()/3){
                            showUpRecipe.add(recipe);
                            Log.d(TAG, "onDataChange: " + recipe);
                        } else if (searchList.size()/4 !=0 && equalCount >= searchList.size()/4){
                            showUpRecipe.add(recipe);
                            Log.d(TAG, "onDataChange: " + recipe);
                        } else if (searchList.size()/5 !=0 && equalCount >= searchList.size()/5){
                            showUpRecipe.add(recipe);
                            Log.d(TAG, "onDataChange: " + recipe);
                        }
                    }


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

    @Override
    public void onItemClick(int i) {
        if(recList.isEmpty()){
            Recipe item = new Recipe(showUpRecipe.get(i));
            Intent intent = new Intent(getApplicationContext(), DetailedRecipeActivity.class);
            ArrayList showUpIngs;
            showUpIngs = item.getIngShowUp();
            ArrayList recStepsCopy;
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
        } else{
            Recipe item = new Recipe(recList.get(i));
            Intent intent = new Intent(getApplicationContext(), DetailedRecipeActivity.class);
            ArrayList showUpIngs;
            showUpIngs = item.getIngShowUp();
            ArrayList recStepsCopy;
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
    }
}