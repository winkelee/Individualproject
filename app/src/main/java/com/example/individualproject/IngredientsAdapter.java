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

import org.w3c.dom.Text;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {
    RecyclerViewOnClickInterface recyclerViewOnClickInterface;
    Context context;
    List<Ingredient> ings;

    public void setSearchList(List<Ingredient> filtered){
        this.ings = filtered;
        notifyDataSetChanged();
    }


    public IngredientsAdapter(Context context, List<Ingredient> list, RecyclerViewOnClickInterface recyclerViewOnClickInterface){
    this.context = context;
    this.ings = list;
    this.recyclerViewOnClickInterface = recyclerViewOnClickInterface;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IngredientViewHolder(LayoutInflater.from(context).inflate(R.layout.ing_part,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        holder.name.setText(ings.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return ings.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.CatName);

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

