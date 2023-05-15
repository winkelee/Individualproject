package com.example.individualproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


public class VerifyIngredientsActivity extends AppCompatActivity implements RecyclerViewOnClickInterface{

    private RecyclerView recyclerView;
    private IngredientsAdapter ia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_ingredients);
        getSupportActionBar().hide();

        listMethod();


    }

    private void listMethod(){
        recyclerView = findViewById(R.id.ingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ia = new IngredientsAdapter(getApplicationContext(), MainActivity.userPreferences, this);
        recyclerView.setAdapter(ia);

    }

   //private void listenerMethod(){

   //    recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
   //        @Override
   //        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

   //            Ingredient item = (Ingredient) listView.getItemAtPosition(i);
   //            userPreferences.remove(item);
   //            ia.remove(item);
   //            ia.notifyDataSetChanged();
   //            Toast.makeText(VerifyIngredientsActivity.this, "Вы убрали: " + item.getName(), Toast.LENGTH_SHORT).show();
   //        }
   //    });
   //}

    public void toRecipes(View view){
        Intent intent = new Intent(VerifyIngredientsActivity.this, RecipesActivity.class);
        startActivity(intent);
    }


    @Override
    public void onItemClick(int i) {
        Ingredient item = MainActivity.userPreferences.get(i);
        MainActivity.userPreferences.remove(item);
        Toast.makeText(this, "Вы убрали: " + item.getName(), Toast.LENGTH_SHORT).show();
        ia.notifyDataSetChanged();
    }
}