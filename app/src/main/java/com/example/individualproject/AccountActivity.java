package com.example.individualproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountActivity extends AppCompatActivity {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.account_layout);
        textView = findViewById(R.id.accView);
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
}