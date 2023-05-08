package com.example.individualproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class IngredientsAdapter extends ArrayAdapter<Ingredient> {

    public IngredientsAdapter(int resource, Context context, List<Ingredient> list){
        super(context, resource, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //обычная инициализация элементов.


        //Даёт адаптеру View для работы
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_part, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.CatName);
        Ingredient ig = getItem(position);
        name.setText(ig.getName());

        return convertView; //Возвращает новоиспечённый View, наполненный элементами
    }
}

