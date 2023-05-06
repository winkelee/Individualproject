package com.example.individualproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class activity_ingredients extends AppCompatActivity {

    IngredientCategory ic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients); //TODO: понять как же использовать это ваш парсинг сайта
        

        //Intent intent = getIntent();
       //String id = intent.getStringExtra("id");
        //ic = (IngredientCategory) MainActivity.ar.get(Integer.valueOf(id));

        //TextView name = (TextView) findViewById(R.id.CatName);
        //name.setText(ig.getName());

    }
}