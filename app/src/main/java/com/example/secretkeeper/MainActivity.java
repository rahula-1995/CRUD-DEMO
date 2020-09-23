package com.example.secretkeeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends Activity {


    LayoutInflater inflater1;

    int count =0;

    String url;
    String secret;

    EditText txturl, txtsecret,txtsearch;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;

    Data data;


    ListView listView;
    RecyclerView recyclerview;

    ArrayList<Data> dataArrayList;

    CustomAdapter customAdapter;


    String key;

    int temp;
    RecyclerAdapter recyclerAdapter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("users");

        key=databaseReference.push().getKey();

        txturl = (EditText) findViewById(R.id.writeurl);
        txtsecret = (EditText)findViewById(R.id.writesecret);
        listView = (ListView) findViewById(R.id.readlist);
        recyclerview=(RecyclerView) findViewById(R.id.readrecycler);
        findViewById(R.id.gotorecycle).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent hintent=new Intent(MainActivity.this, RecyclerActivity.class);
                hintent.putExtra("users", (Parcelable) dataArrayList);
                startActivity(hintent);
            }
        });

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {

                    url = txturl.getText().toString().trim();


                    if (TextUtils.isEmpty(url) ) {

                        Toast.makeText(getApplicationContext(), "Please Enter URL", Toast.LENGTH_SHORT).show();

                    } else {

                        secret =(txtsecret.getText().toString().trim());

                        data = new Data(databaseReference.push().getKey(), url, secret);

                        databaseReference.child(data.getKey()).setValue(data);

                        Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();

                        txturl.setText("");
                        txtsecret.setText("");

                    }
                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_SHORT).show();

                }


            }
        });

        dataArrayList = new ArrayList<>();

        customAdapter = new CustomAdapter(MainActivity.this, dataArrayList);
        recyclerAdapter=new RecyclerAdapter(dataArrayList,MainActivity.this);
        recyclerview.setAdapter(recyclerAdapter);


        listView.setAdapter(customAdapter);


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Data datam = dataSnapshot.getValue(Data.class);

                dataArrayList.add(datam);

                customAdapter.notifyDataSetChanged();
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                final View v = inflater1.from(getApplicationContext()).inflate(R.layout.custom_alert, null);
                temp = i;

                final EditText updturl, updtsecret;

                updturl = (EditText) v.findViewById(R.id.updtURL);
                updtsecret = (EditText) v.findViewById(R.id.updtSecretkey);


                final AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this).setView(v);
                final AlertDialog alert = builder.create();

                v.findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Data tempData = new Data(dataArrayList.get(temp).getKey(), updturl.getText().toString().trim(),
                                (updtsecret.getText().toString().trim()));

                        databaseReference.child(dataArrayList.get(temp).getKey()).setValue(tempData);

                        dataArrayList.remove(temp);

                        dataArrayList.add(temp,tempData);

                        customAdapter.notifyDataSetChanged();
                        recyclerAdapter.notifyDataSetChanged();
                    }
                });

                v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(temp == -1){

                            Toast.makeText(getApplicationContext(), "There is no data to delete", Toast.LENGTH_SHORT).show();

                        }else {

                            databaseReference.child(dataArrayList.get(temp).getKey()).removeValue();

                            dataArrayList.remove(temp);

                            customAdapter.notifyDataSetChanged();
                            recyclerAdapter.notifyDataSetChanged();

                            alert.cancel();

                            temp = -1;
                        }
                    }
                });



                updturl.setText(dataArrayList.get(temp).getUrl());

                updtsecret.setText("" + dataArrayList.get(temp).getSecret());



                try {


                    alert.show();

                } catch (Exception e) {

                    Log.d("show", "onItemClick: " + e);

                }




                return;


            }
        });

        txtsearch = (EditText) findViewById(R.id.search);

        findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                url = txtsearch.getText().toString().trim();

                databaseReference.orderByChild("name").equalTo(url).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        ++count;



                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                            data = dataSnapshot1.getValue(Data.class);
                            dataArrayList.clear();
                            dataArrayList.add(data);
                            Log.d("log", "onDataChange: "+dataSnapshot1.child("url").getValue());

                        }


                        func();






                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });








            }
        });

        realtimeUpdate();
    }

    public void realtimeUpdate(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    data = dataSnapshot1.getValue(Data.class);

                    dataArrayList.add(data);

                }


                customAdapter.notifyDataSetChanged();
                recyclerAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void func(){

        if(count!=0){

            customAdapter = new CustomAdapter(getApplicationContext(),dataArrayList);
            recyclerAdapter=new RecyclerAdapter(dataArrayList,getApplicationContext());

            listView.setAdapter(customAdapter);
            recyclerview.setAdapter(recyclerAdapter);

        }else {

            Toast.makeText(getApplicationContext(), "There is no data to show", Toast.LENGTH_SHORT).show();
            listView.setVisibility(View.GONE);
            recyclerview.setVisibility(recyclerview.GONE);
        }


    }
}