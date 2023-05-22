package com.example.individualproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText textPass, textLog;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.greetings_layout);

        //привязываем значения
        textPass = findViewById(R.id.pass);
        textLog = findViewById(R.id.email);
        mAuth = FirebaseAuth.getInstance(); // назвал переменную как в примере в самом firebase :)

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser(); //экземпляр юзера
        if (currentUser != null){ //проверяем, а зашёл ли пользователь в систему?
            alreadySignedIn();
            Log.d("DEBUG", "onStart: user is logged");
        } else{
            Log.d("DEBUG", "onStart: user is null");
        }
    }

    public void toSignUp(View view){ // Метод для регистрации
        if(!textLog.getText().toString().equals("") && !textPass.getText().toString().equals("") && textPass.length() >= 6){
            mAuth.createUserWithEmailAndPassword(textLog.getText().toString(), textPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        verification();
                        alreadySignedIn();
                        Log.d("User", "onComplete: success!");
                    } else{
                        Log.e("User", "onComplete: failed! What? How?");
                    }

                }
            });

        } else{
            Toast.makeText(this, "Почта и пароль не могут быть пустыми. Пароль должен содержать не менее 6 символов. Пожалуйста, попробуйте ещё раз.", Toast.LENGTH_LONG).show();
        }

    }

    public void toSignIn(View view){ //Аналогичный метод для входа
        if (!textLog.getText().toString().equals("") && !textPass.getText().toString().equals("")){
            mAuth.signInWithEmailAndPassword(textLog.getText().toString(), textPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        alreadySignedIn();
                        Log.d("TAG", "onComplete: success!");
                    }else{
                        Toast.makeText(LoginActivity.this, "Неправильные почта или пароль. Пожалуйста, попробуйте ещё раз.", Toast.LENGTH_SHORT).show();
                        Log.e("User", "onComplete: failed! What? How?");
                    }

                }
            });
        } else{
            Toast.makeText(LoginActivity.this, "Неправильные почта или пароль. Пожалуйста, попробуйте ещё раз.", Toast.LENGTH_SHORT).show();
        }

    }
    public void alreadySignedIn(){ //Метод для перехода с этой активности на акк, никакой магии
        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified()){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class); //если он уже вошёл, то смысл входить ещё раз? Просто перенаправляем в АА.
            startActivity(intent);
        }

    }

    private void verification(){ //отправляет письмо с верификацией почты
        if (mAuth.getCurrentUser() != null){
            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Подтвердите адрес почты, перейдя по ссылке в письме, отправленном на " + mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                    }else{

                    }
                }
            });
        }


    }

    public void toResetPasswordActivity(View view){
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }


}