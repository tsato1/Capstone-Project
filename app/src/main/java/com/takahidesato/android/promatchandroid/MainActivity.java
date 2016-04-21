package com.takahidesato.android.promatchandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    public static final String URL_BASE_SUCCESS = "";
    public static final String URL_BASE_TWEETS = "https://api.twitter.com/1.1/";
    public static final String URL_TWEETS_TOKEN = "statuses/user_timeline.json?screen_name=twitterapi&count=2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
