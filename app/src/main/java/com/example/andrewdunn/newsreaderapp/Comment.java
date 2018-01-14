package com.example.andrewdunn.newsreaderapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Comment extends AppCompatActivity {

    TextView editName,editEmail,editComment;

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        setTitle("JSONDataParsing");
        Intent intent = getIntent();
        id = intent.getIntExtra("Id",0);

        String name = intent.getStringExtra("Name");
        String email = intent.getStringExtra("Email");
        String body = intent.getStringExtra("Body");

        editName = (TextView) findViewById(R.id.editName);
        editEmail = (TextView) findViewById(R.id.editEmail);
        editComment = (TextView) findViewById(R.id.editComment);

        editName.setText(name);
        editEmail.setText(email);
        editComment.setText(body);

    }

}
