package com.example.secretkeeper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Recyclerviewholder> {
    TextView url, secret;

    Context context;

    ArrayList<Data> data;

    LayoutInflater minflater;



    public RecyclerAdapter(ArrayList<Data> data, Context context) {
        // mCountries = countries;//8a
        this.context = context;
        this.data = data;
        minflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerAdapter.Recyclerviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mItemView = minflater.inflate(R.layout.custom_row, parent, false);//12
        return new Recyclerviewholder(mItemView);//13
        

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.Recyclerviewholder holder, int i) {
        holder.url.setText(url.getText()+data.get(i).getUrl());
        holder.secret.setText(secret.getText()+""+ data.get(i).getSecret());

    }

    @Override
    public int getItemCount() {
        return data.size();

    }
    public class Recyclerviewholder extends RecyclerView.ViewHolder {//5
        public TextView url;//14
        public TextView secret;
        public Recyclerviewholder(@NonNull View itemView) {
            super(itemView);
            url = (TextView) itemView.findViewById(R.id.readURL);
            secret = (TextView) itemView.findViewById(R.id.readsecretkey);
        }


    }


}
