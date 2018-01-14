package com.example.andrewdunn.newsreaderapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class ArticleActivity extends AppCompatActivity {

    ArrayList <String> names = new ArrayList<>();
    ArrayList <String> body = new ArrayList<>();
    ArrayList <String> email = new ArrayList<>();
    ArrayAdapter arrayAdap;

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        setTitle("JSONDataParsing");
        Intent intent = getIntent();

        id = intent.getIntExtra("Id",0);

        ListView postList = (ListView) findViewById(R.id.postListView);

        arrayAdap = new ArrayAdapter(this, android.R.layout.simple_list_item_1, names);

        postList.setAdapter(arrayAdap);

        postList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.i("Id Tag",Integer.toString(i+1));
                Log.i("Name",names.get(i));
                Log.i("Body",body.get(i));
                Log.i("Email",email.get(i));

                Intent intent = new Intent(getApplicationContext(), Comment.class);
                intent.putExtra("Id",i+1);
                intent.putExtra("Name",names.get(i));
                intent.putExtra("Email",email.get(i));
                intent.putExtra("Body",body.get(i));

                startActivity(intent);
            }
        });

        DownloadTask task = new DownloadTask();

        try {
            task.execute("https://jsonplaceholder.typicode.com/comments");
        } catch (Exception e) {
            e.printStackTrace();
        }

        arrayAdap.notifyDataSetChanged();


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

                Log.i("Comments", result);

                JSONArray jsonArray = new JSONArray(result);

                int numberOfItems = 500;

                if (jsonArray.length() < 500) {

                    numberOfItems = jsonArray.length();

                }

                for (int i = 0; i < numberOfItems; i++) {

                    String articleId = jsonArray.getString(i);

                    Log.i("PostId", articleId);

                    JSONObject jsonObject = new JSONObject(articleId);

                    if (!jsonObject.isNull("name") && !jsonObject.isNull("body")) {

                        if(id == jsonObject.getInt("postId") ) {
                            String articleName = jsonObject.getString("name");
                            String articleEmail = jsonObject.getString("email");
                            String articleBody = jsonObject.getString("body");

                            names.add(articleName);
                            email.add(articleEmail);
                            body.add(articleBody);
                        }
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

            arrayAdap.notifyDataSetChanged();
        }
    }
}
