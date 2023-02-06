package com.leopard4.contactapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.leopard4.contactapp.adapter.ContactAdapter;
import com.leopard4.contactapp.data.DatabaseHandler;
import com.leopard4.contactapp.model.Contact;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnAdd;

    // 리사이클러뷰를 사용할때!
    // RecyclerView, Adapter, ArrayList(실제데이터) 를 셋트로 적어라!
    RecyclerView recyclerView;
    ContactAdapter adapter;
    ArrayList<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);

        // 리사이클러뷰를 화면에 연결하고,
        // 쌍으로 같이 다니는 코드도 작성
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);

            }

        });

    }

    @Override
    protected void onResume() {
        // DB에서 데이터를 가져와서, 리사이클러뷰에 뿌려준다.
        DatabaseHandler db = new DatabaseHandler(MainActivity.this);
        contactList = db.getAllContacts();


        // 어댑터를 생성하고,
        adapter = new ContactAdapter(MainActivity.this, contactList);
        // 리사이클러뷰에 셋팅
        recyclerView.setAdapter(adapter);

        super.onResume();
    }
}