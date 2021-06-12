package com.example.kpopprofileandroidapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BandRecyclerViewAdapter extends RecyclerView.Adapter<BandRecyclerViewAdapter.RecyclerViewHolder>{

    Context context;
    List<Band> bandList;

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
    @Override
    public void onBindViewHolder(@NonNull BandRecyclerViewAdapter.RecyclerViewHolder holder, int position) {
        holder.bandNameText.setText(bandList.get(position).getName());
        holder.yearText.setText(Integer.toString(bandList.get(position).getYear()));
    }

    @Override
    public int getItemCount() {
        return bandList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView bandNameText, yearText, generationText, fandomText;

        //receives the view from "onCreateViewHolder" of parent adapter class
        public RecyclerViewHolder(@NonNull View itemView) {
             super(itemView);

             bandNameText = itemView.findViewById(R.id.bandNameText);
             yearText = itemView.findViewById(R.id.yearText);
        }
    }
}
