package com.example.secretkeeper;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerActivity extends AppCompatActivity {
    RecyclerView recyclerview1;
    RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        recyclerview1=(RecyclerView) findViewById(R.id.readrecycler1);

        ArrayList<? extends Data> dataArrayList = this.getIntent().getExtras().getParcelableArrayList("Users");
        recyclerAdapter=new RecyclerAdapter((ArrayList<Data>) dataArrayList,RecyclerActivity.this);
        recyclerview1.setAdapter(recyclerAdapter);
    }

}