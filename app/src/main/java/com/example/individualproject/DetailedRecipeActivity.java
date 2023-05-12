package com.example.individualproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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
}