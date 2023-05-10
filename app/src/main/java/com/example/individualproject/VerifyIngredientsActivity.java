package com.example.individualproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


public class VerifyIngredientsActivity extends AppCompatActivity {

    private ListView listView;
    private IngredientsAdapter ia;
    public static ArrayList userPreferences = new ArrayList(MainActivity.userPreferences);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_ingredients);

        listMethod();
        listenerMethod();

    }

    private void listMethod(){
        listView = findViewById(R.id.ingList);
        ia = new IngredientsAdapter(0, getApplicationContext(), userPreferences);
        listView.setAdapter(ia);

    }

    private void listenerMethod(){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Ingredient item = (Ingredient) listView.getItemAtPosition(i);
                userPreferences.remove(item);
                ia.remove(item);
                ia.notifyDataSetChanged();
                Toast.makeText(VerifyIngredientsActivity.this, "Вы убрали: " + item.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void toRecipes(View view){
        Intent intent = new Intent(VerifyIngredientsActivity.this, RecipesActivity.class);
        startActivity(intent);
    }


}