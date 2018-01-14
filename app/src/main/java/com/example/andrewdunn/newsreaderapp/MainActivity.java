package com.example.andrewdunn.newsreaderapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.HttpURLConnection;

public class MainActivity extends AppCompatActivity {

    ArrayList <String> titles = new ArrayList<>();
    ArrayList <String> body = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("JSONDataParsing");
        ListView listView = (ListView) findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titles);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.i("Id Tag",Integer.toString(i+1));

                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                intent.putExtra("Id",i+1);

                startActivity(intent);
            }
        });

        DownloadTask task = new DownloadTask();

        try {
            task.execute("https://jsonplaceholder.typicode.com/posts");
        } catch (Exception e) {
            e.printStackTrace();
        }

        arrayAdapter.notifyDataSetChanged();
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings){

            String result = "";

            URL url;

            HttpURLConnection urlConnection = null;

            try {

                url = new URL(strings[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();
                }

                Log.i("URLContent", result);

                JSONArray jsonArray = new JSONArray(result);

                int numberOfItems = 150;

                if (jsonArray.length() < 150) {

                    numberOfItems = jsonArray.length();

                }

                for (int i = 0; i < numberOfItems; i++) {

                    String articleId = jsonArray.getString(i);

                    Log.i("Id", articleId);

                    JSONObject jsonObject = new JSONObject(articleId);

                    if (!jsonObject.isNull("title") && !jsonObject.isNull("body")) {

                        String articleTitle = jsonObject.getString("title");

                        String articlebody = jsonObject.getString("body");

                        titles.add(articleTitle);
                        body.add(articlebody);

                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            arrayAdapter.notifyDataSetChanged();
        }
    }

}
