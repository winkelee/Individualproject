package com.example.individualproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountActivity extends AppCompatActivity {

    private TextView textView;
    private EditText range;
    public static int rangeInt =-1;
    private int rangeIntCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.account_layout);
        textView = findViewById(R.id.accView);
        range = findViewById(R.id.accDis);
        textView.setText("Добро пожаловать! Вы вошли в аккаунт.");

    }

    public void toQuit(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void toCont(View view){
        Intent intent = new Intent(AccountActivity.this, MainActivity.class);
        startActivity(intent);
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

    public void toCreateRecipeActivity(View view){
        Intent intent = new Intent(AccountActivity.this, NewRecipeActivity.class);
        startActivity(intent);
    }
}