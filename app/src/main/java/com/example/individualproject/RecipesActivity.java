package com.example.individualproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class RecipesActivity extends AppCompatActivity {

    private static ArrayList userPreferences = new ArrayList(VerifyIngredientsActivity.userPreferences);
    private ListView listView;
    private RecipesAdapter recAdapter;
    private ArrayList<Recipe> showUpRecipe= new ArrayList<>();
    int l = 0;
    private DatabaseReference recipeDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        recipeDB = FirebaseDatabase.getInstance().getReference("Recipes");
        listMethod();
        fillList();
    }

    private void listMethod(){
        listView = findViewById(R.id.recList);
        recAdapter = new RecipesAdapter(1, getApplicationContext(), showUpRecipe);
        listView.setAdapter(recAdapter);

    }

    private void fillList(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!showUpRecipe.isEmpty()){
                    showUpRecipe.clear();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    l++;
                    GenericTypeIndicator<Recipe> t = new GenericTypeIndicator<Recipe>() {};
                    Recipe recipe = dataSnapshot.getValue(t);
                    if (recipe != null){
                        recipe.setId2(l);
                        showUpRecipe.add(recipe);
                    }

                }
                recAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        recipeDB.addValueEventListener(valueEventListener);

    }

}