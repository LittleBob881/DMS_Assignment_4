package com.example.kpopprofileandroidapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BandRecyclerViewAdapter extends RecyclerView.Adapter<BandRecyclerViewAdapter.RecyclerViewHolder>{

    Context context;
    List<Band> bandList;
    Class res = R.drawable.class;

    public BandRecyclerViewAdapter(Context context, List bandList)
    {
        this.context = context;
        this.bandList = bandList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.band_recycler_row, viewGroup, false);

        return new RecyclerViewHolder(view);
    }

    //communicates with the RecyclerViewHolder
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(@NonNull BandRecyclerViewAdapter.RecyclerViewHolder holder, int position) {
        holder.bandNameText.setText(bandList.get(position).getName());
        holder.yearText.setText(Integer.toString(bandList.get(position).getYear()));
        holder.generationText.setText("Generation "+ Integer.toString(bandList.get(position).getGeneration()));
        holder.fandomText.setText(bandList.get(position).getFandomName());
        try {
            Field field = res.getField(bandList.get(position).getName().replaceAll("\\s+", "").toLowerCase());
            holder.imageView.setImageResource(field.getInt(null));

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return bandList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView bandNameText, yearText, generationText, fandomText;
        ImageView imageView;

        //receives the view from "onCreateViewHolder" of parent adapter class
        public RecyclerViewHolder(@NonNull View itemView) {
             super(itemView);

             bandNameText = itemView.findViewById(R.id.bandNameText);
             yearText = itemView.findViewById(R.id.yearText);
             generationText = itemView.findViewById(R.id.generationText);
             fandomText = itemView.findViewById(R.id.fandomText);
             imageView = itemView.findViewById(R.id.bandImage);
        }
    }
}
