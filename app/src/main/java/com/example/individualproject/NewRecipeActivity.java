package com.example.individualproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class NewRecipeActivity extends AppCompatActivity {

    private TextView recipeName;
    private TextView recipeImage;
    private TextView recipeDesc;
    private TextView recipeIngs;
    private DatabaseReference db;
    private String recipeName1;
    private String recipeImage1;
    Toolbar toolbar;
    private String recipeDesc1;
    private String recipeIngsShowUpString;
    private ArrayList ingsShowUp = new ArrayList();
    private ArrayList ingsSearch = new ArrayList();
    private String[] arrayIngs;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        recipeDesc = findViewById(R.id.newRecDesc);
        recipeImage = findViewById(R.id.newRecImage);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        recipeName = findViewById(R.id.newRecName);
        recipeIngs = findViewById(R.id.newRecIngs);
        db = FirebaseDatabase.getInstance().getReference("Recipes");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.newRecipeItem){
            Toast.makeText(this, "Вы уже на странице добавления нового рецепта!", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.redactRange){
            Intent intent = new Intent(NewRecipeActivity.this, AccountActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.redactIngs){
            Intent intent = new Intent(NewRecipeActivity.this, VerifyIngredientsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.logOut){
            Intent intent = new Intent(NewRecipeActivity.this, LoginActivity.class);
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

    public void toCreateRecipe(View view){
        recipeDesc1 = recipeDesc.getText().toString(); //Описание ингредиента (пока только текстовое, без шагов)
        recipeImage1 = recipeImage.getText().toString(); //Картинка ингредиента
        recipeName1 = recipeName.getText().toString(); // Имя ингредиента
        recipeIngsShowUpString = recipeIngs.getText().toString();

        arrayIngs = recipeIngsShowUpString.split(", ");
        Log.d("Logger", "toCreateRecipe: " + arrayIngs.toString());
        for (int i = 0; i<arrayIngs.length; i++){//ingsShowUp - массив с ингредиентами для показа
            ingsShowUp.add(arrayIngs[i]);
        }


        for (int i = 0; i<ingsShowUp.size(); i++){
            String ingName = ingsShowUp.get(i).toString();
            String[] ingNameArray = new String[2];
            if (ingName.contains("-")){
                 ingNameArray = ingName.split("-");
            } else if (ingName.contains(" -")){
                 ingNameArray = ingName.split(" -");
            } else if (ingName.contains(" - ")){
                 ingNameArray = ingName.split(" - ");
            } else if (ingName.contains("- ")){
                 ingNameArray = ingName.split("- ");
            }
            String ingNameDone = ingNameArray[0];
            ingsSearch.add(ingNameDone); //ingsSearch - массив с ингредиентами для поиска
        }

        if (recipeDesc1.equals("") || recipeImage1.equals("")|| recipeName1.equals("") || ingsShowUp.isEmpty() || ingsSearch.isEmpty()){
            Toast.makeText(this, "Одно или несколько текстовых полей остались пустыми. Пожалуйста, убедитесь, что все поля заполнены и повторите попытку.", Toast.LENGTH_LONG).show();
        } else{
            id = db.getKey();
            Recipe recipe = new Recipe(id, recipeName1, recipeImage1, ingsSearch, ingsShowUp, recipeDesc1);
            Log.d("Logger", "toCreateRecipe: Recipe is " + recipe);
            db.push().setValue(recipe);
            Toast.makeText(this, "Ваш рецепт был добавлен успешно!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(NewRecipeActivity.this, MainActivity.class);
            startActivity(intent);
        }

    }
}