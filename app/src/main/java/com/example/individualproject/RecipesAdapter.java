package com.example.individualproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.imageview.ShapeableImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

public class RecipesAdapter extends ArrayAdapter<Recipe> {
    TextView name;
    ShapeableImageView image;
    TextView ings;
    Handler h;
    Thread thread;
    public Bitmap is;
    ArrayList<Recipe> ingredients= new ArrayList<Recipe>();

    public RecipesAdapter(int resource, Context context, List<Recipe> list){
        super(context, resource, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recipe_part, parent, false);
        }
         name =  convertView.findViewById(R.id.recipeName);
         image = convertView.findViewById(R.id.recipeImage);
         ings = convertView.findViewById(R.id.recipeIngs);
        h = new Handler(){
            public void handleMessage(android.os.Message msg){
                if (msg.what == 1){
                    image.setImageBitmap(is);
                }
            }
        };

        Recipe rec = getItem(position);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String url1 = rec.getImgUrl();
                    URL url = new URL(url1);
                    is = BitmapFactory.decodeStream((InputStream) url.getContent());
                    h.sendEmptyMessage(1);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        //String ingsCopy = ingredients.toString();
        //ings.setText(ingsCopy.substring(1, ingsCopy.length()-1));
        String ingsCopy = new String();
        ingredients = rec.getIng();
        for (int i = 0; i<ingredients.size(); i++){
            if (i == ingredients.size()-1){
                ingsCopy = ingsCopy + ingredients.get(i);
            }else{
                ingsCopy = ingsCopy + ingredients.get(i) + ", ";
            }
        }
        ings.setText(ingsCopy);
        name.setText(rec.getName());

        return convertView;
    }
}

