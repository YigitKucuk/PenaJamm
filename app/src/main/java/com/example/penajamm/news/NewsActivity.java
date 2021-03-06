package com.example.penajamm.news;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.penajamm.MainScreenActivity;
import com.example.penajamm.Navigation;
import com.example.penajamm.NewPostActivity;
import com.example.penajamm.ProfilePageActivity;
import com.example.penajamm.R;
import com.example.penajamm.SettingsActivity;
import com.example.penajamm.Userlist;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity implements Navigation {
    public static final String API_KEY = "5af2790237da42859874114319baa54c";
    public static final String SOURCE = "loudwire.com";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private RecyclerView recyclerView;
    private ArrayList<Article> articles = new ArrayList<>();
    private Adapter adapter;
    private final String TAG = NewsActivity.class.getSimpleName();
    private ImageButton btnList, btnAssig, btnSettings, btnMainScreen, btnProfile, btnChat, backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(NewsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        backbtn = findViewById(R.id.backbtn);
        btnAssig = findViewById(R.id.btn_Assig);
        btnSettings = findViewById(R.id.btn_Settings);
        btnMainScreen = findViewById(R.id.btn_MainScreen);
        btnProfile = findViewById(R.id.btn_Profile);
        btnList = findViewById(R.id.btnList);
        btnChat = findViewById(R.id.btn_News);

        backbtn.setOnClickListener(view -> goToMainPage());

        btnProfile.setOnClickListener(view -> goToProfilePage());

        btnList.setOnClickListener(view -> goToUsers());

        btnMainScreen.setOnClickListener(view -> goToMainPage());

        btnSettings.setOnClickListener(view -> goToSettings());

        btnChat.setOnClickListener(view -> goToChat());

        btnAssig.setOnClickListener(view -> goToNewPosts());

        adapter = new Adapter(articles, NewsActivity.this);
        recyclerView.setAdapter(adapter);
        LoadJson();
    }

    public void LoadJson(){
        NewsApiInterface apiInterface = NewsApi.getApiClient().create(NewsApiInterface.class);

        Call<News> call;
        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat(DATE_FORMAT).format(new Date());

        call = apiInterface.getNews(date, SOURCE, API_KEY);

        call.enqueue(new Callback<News>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<News> call, @NonNull Response<News> response) {
                if (response.isSuccessful() && Objects.requireNonNull(response.body()).getArticles() != null){

                    if (!articles.isEmpty()){
                        articles.clear();
                    }

                    articles = response.body().getArticles();
                    adapter = new Adapter(articles, NewsActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(NewsActivity.this, "No Result", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<News> call, @NonNull Throwable t) {
            }
        });

    }

    public void goToProfilePage() {
        startActivity(new Intent(NewsActivity.this, ProfilePageActivity.class));
    }

    public void goToSettings() {
        startActivity(new Intent(NewsActivity.this, SettingsActivity.class));
    }

    public void goToNewPosts() {
        startActivity(new Intent(NewsActivity.this, NewPostActivity.class));
    }

    public void goToMainPage() {
        startActivity(new Intent(NewsActivity.this, MainScreenActivity.class));
    }

    public void goToChat() {
        startActivity(new Intent(NewsActivity.this, NewsActivity.class));
    }

    public void goToUsers() {
        startActivity(new Intent(NewsActivity.this, Userlist.class));
    }
}