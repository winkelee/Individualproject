package com.example.individualproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountActivity extends AppCompatActivity {

    private EditText range;
    public static int rangeInt =-1;
    private int rangeIntCopy;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.account_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        range = findViewById(R.id.accDis);

    }


    public void toSubmitRange(View view){
    String rangeString = range.getText().toString();
    rangeIntCopy = Integer.parseInt(rangeString);
    if (rangeIntCopy < 0){
        Toast.makeText(this, "Число диапазона поиска не может быть меньше нуля. Пожалуйста, введите другое число или оставьте поле пустым", Toast.LENGTH_SHORT).show();
    } else {
        rangeInt = Integer.parseInt(rangeString);
        Toast.makeText(this, "Число диапазона поиска изменено на " + rangeInt, Toast.LENGTH_SHORT).show();
    }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.newRecipeItem){
            Intent intent = new Intent(AccountActivity.this, NewRecipeActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.redactRange){
            Toast.makeText(this, "Вы уже на странице изменения диапазона поиска!", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.redactIngs){
            Intent intent = new Intent(AccountActivity.this, VerifyIngredientsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.logOut){
            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
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
}