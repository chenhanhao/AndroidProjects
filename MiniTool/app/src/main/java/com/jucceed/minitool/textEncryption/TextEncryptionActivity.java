package com.jucceed.minitool.textEncryption;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.jucceed.minitool.R;

import java.io.LineNumberInputStream;
import java.util.ArrayList;
import java.util.List;

public class TextEncryptionActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_encryption);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Context context = TextEncryptionActivity.this;
        List<String> stringList = new ArrayList<>();
        stringList.add("BASE64");
        stringList.add("MD5");
        stringList.add("RC4");
        stringList.add("BASE64");
        stringList.add("MD5");
        stringList.add("RC4");
        stringList.add("BASE64");
        stringList.add("MD5");
        stringList.add("RC4");
        stringList.add("BASE64");
        stringList.add("MD5");
        stringList.add("RC4");
        stringList.add("BASE64");
        stringList.add("MD5");
        stringList.add("RC4");
        stringList.add("BASE64");
        stringList.add("MD5");
        stringList.add("RC4");

        ItemAdapter adapter = new ItemAdapter(stringList);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

    }


}
