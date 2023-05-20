package com.example.individualproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.commons.validator.routines.UrlValidator;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {
    RecyclerViewOnClickInterface recyclerViewOnClickInterface;
    Context context;
    ArrayList ingsList2;
    UrlValidator urlValidator = new UrlValidator();
    String urlPlaceholder = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png";
    List<Recipe> rec;

    public void setSearchList(List<Recipe> filtered){
        this.rec = filtered;
        notifyDataSetChanged();
    }


    public RecipesAdapter(Context context, List<Recipe> list, RecyclerViewOnClickInterface recyclerViewOnClickInterface){
        this.context = context;
        this.rec = list;
        this.recyclerViewOnClickInterface = recyclerViewOnClickInterface;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.recipe_part,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.name.setText(rec.get(position).getName());
        Recipe recipe = new Recipe(rec.get(position));
        String ings = "";
        ingsList2 = recipe.getIng();
        for (int i = 0; i<recipe.getIng().size(); i++){
            if (i == recipe.getIng().size() -1){
                ings = ings + ingsList2.get(i);
            } else{
                ings = ings + ingsList2.get(i) + ", ";
            }
            }
        holder.ings.setText(ings);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context.getApplicationContext()));
        if (!urlValidator.isValid(recipe.getImgUrl())){
            imageLoader.displayImage(urlPlaceholder, holder.image);
        } else{
            imageLoader.displayImage(recipe.getImgUrl(), holder.image);
        }


    }

    @Override
    public int getItemCount() {
        return rec.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView ings;
        ShapeableImageView image;
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.recipeName);
            ings = itemView.findViewById(R.id.recipeIngs);
            image = itemView.findViewById(R.id.recipeImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewOnClickInterface != null){
                        int position = getAdapterPosition();
                        if (position !=RecyclerView.NO_POSITION){
                            recyclerViewOnClickInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}

