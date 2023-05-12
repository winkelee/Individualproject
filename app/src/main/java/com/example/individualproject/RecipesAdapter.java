package com.example.individualproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


import com.google.android.material.imageview.ShapeableImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

public class RecipesAdapter extends ArrayAdapter<Recipe> {
    TextView name;
    ShapeableImageView image;
    TextView ings;
    static Handler h;
    Thread thread;
    public Bitmap is;
    String placeholderUrl;
    ImageLoader mLoader = ImageLoader.getInstance();
    ArrayList<Recipe> ingredients= new ArrayList<>();

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
         placeholderUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png";


        Recipe rec = getItem(position);

        String ingsCopy = new String();
        ingredients = rec.getIng();
        for (int i = 0; i<ingredients.size(); i++){
            if (i == ingredients.size()-1){
                ingsCopy = ingsCopy + ingredients.get(i);
            }else{
                ingsCopy = ingsCopy + ingredients.get(i) + ", ";
            }
        }
        mLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
        if (rec.getImgUrl().contains("gefest")){
            mLoader.displayImage(placeholderUrl, image);
        }else{
            mLoader.displayImage(rec.getImgUrl(), image);
        }
        ings.setText(ingsCopy);
        name.setText(rec.getName());

        return convertView;
    }

}

