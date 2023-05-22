package com.example.individualproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText email;
        FirebaseAuth mAuth;
        String emailString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email_reset);
    }

    public void toResetPassword(View view){
        emailString = email.getText().toString().trim();

        if (emailString.equals("")){
            Toast.makeText(this, "Почта не может быть пустой. Пожалуйста, повторите попытку.", Toast.LENGTH_SHORT).show();
        }else if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()){
            Toast.makeText(this, "Почта не является корректной. Пожалуйста, повторите попытку", Toast.LENGTH_SHORT).show();
        } else{
            mAuth.sendPasswordResetEmail(emailString).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ForgotPasswordActivity.this, "Письмо со сменой пароля отправлено на " + emailString, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(ForgotPasswordActivity.this, "Что-то пошло не так. Пожалуйста, проверьте корректность почты и повторите попытку.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }




    }
}