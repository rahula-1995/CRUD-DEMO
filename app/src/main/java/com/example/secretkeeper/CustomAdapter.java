package com.example.secretkeeper;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    TextView url, secret;

    Context context;

    ArrayList<Data> data;

    LayoutInflater inflater;

    public CustomAdapter(Context context, ArrayList<Data> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflater.from(context).inflate(R.layout.custom_list,viewGroup,false);

        url = (TextView) view.findViewById(R.id.readURL);
        secret = (TextView) view.findViewById(R.id.readsecretkey);


        url.setText(url.getText()+data.get(i).getUrl());
        secret.setText(secret.getText()+""+ data.get(i).getSecret());



        return view;
    }
}