package com.example.individualproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class DetailedRecipeActivity extends AppCompatActivity {
    public String recName;
    public String recSteps;
    public String recImage;
    public String recIngs;
    private Bitmap is;
    Toolbar toolbar;
    private Handler h;
    private String placeholderUrl;
    private TextView recNameShowUp;
    private TextView recStepShowUp;
    private TextView recIngsShowUp;
    private ImageView recImageShowUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail);

        recImage = RecipesActivity.recImage;
        recSteps = RecipesActivity.recSteps;
        recName = RecipesActivity.recName;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        recIngs = RecipesActivity.recIngs;

        recIngsShowUp = findViewById(R.id.recIngsDetailed);
        recNameShowUp = findViewById(R.id.recNameDetailed);
        recStepShowUp = findViewById(R.id.recStepsDetailed);
        recImageShowUp = findViewById(R.id.recImageDetailed);

        h = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 1){
                    recImageShowUp.setImageBitmap(is);
                }
            }
        };
        placeholderUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png";
        recIngsShowUp.setText(recIngs);
        recNameShowUp.setText(recName);
        recStepShowUp.setText(recSteps);
        RecipesActivity.recIngs = "";
        RecipesActivity.recSteps = "";
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL(recImage);
                    is = BitmapFactory.decodeStream((InputStream) url.getContent());
                    h.sendEmptyMessage(1);
                } catch (MalformedURLException e) {
                    Log.e("EXCEPTION", "MALFORMEDURLEXCEPTION ");
                    recImageShowUp.setImageResource(R.drawable.placeholder);
                    e.printStackTrace();
                } catch (IOException e){
                    Log.e("EXCEPTION", "IOEXCEPTION ");

                }
            }
        });
        thread.start();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.newRecipeItem){
            Intent intent = new Intent(DetailedRecipeActivity.this, NewRecipeActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.redactRange){
            Intent intent = new Intent(DetailedRecipeActivity.this, AccountActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.redactIngs){
            Intent intent = new Intent(DetailedRecipeActivity.this, VerifyIngredientsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.logOut){
            Intent intent = new Intent(DetailedRecipeActivity.this, LoginActivity.class);
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