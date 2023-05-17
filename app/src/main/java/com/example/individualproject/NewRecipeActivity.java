package com.example.individualproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
        recipeName = findViewById(R.id.newRecName);
        recipeIngs = findViewById(R.id.newRecIngs);
        db = FirebaseDatabase.getInstance().getReference("Recipes");
    }

    public void toCreateRecipe(View view){
        recipeDesc1 = recipeDesc.getText().toString(); //Описание ингредиента (пока только текстовое, без шагов)
        recipeImage1 = recipeImage.getText().toString(); //Картинка ингредиента TODO: проверка ссылки на валидность
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
        id = db.getKey();
        Recipe recipe = new Recipe(id, recipeName1, recipeImage1, ingsSearch, ingsShowUp, recipeDesc1);
        Log.d("Logger", "toCreateRecipe: Recipe is " + recipe);
    }
}