package com.example.individualproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


public class VerifyIngredientsActivity extends AppCompatActivity implements RecyclerViewOnClickInterface{

    private RecyclerView recyclerView;
    private IngredientsAdapter ia;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_ingredients);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        listMethod();


    }

    private void listMethod(){
        recyclerView = findViewById(R.id.ingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ia = new IngredientsAdapter(getApplicationContext(), MainActivity.userPreferences, this);
        recyclerView.setAdapter(ia);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.newRecipeItem){
            Intent intent = new Intent(VerifyIngredientsActivity.this, NewRecipeActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.redactRange){
            Intent intent = new Intent(VerifyIngredientsActivity.this, AccountActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.redactIngs){
            Toast.makeText(this, "Вы уже на странице изменения ингредиентов!", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.logOut){
            Intent intent = new Intent(VerifyIngredientsActivity.this, LoginActivity.class);
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