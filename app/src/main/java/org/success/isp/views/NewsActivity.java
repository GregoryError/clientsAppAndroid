package org.success.isp.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import org.success.isp.R;
import org.success.isp.adapters.NewsMainAdapter;
import org.success.isp.pojos.EntityNews;
import org.success.isp.presenters.NewsMainPresenter;

import java.util.ArrayList;
import java.util.Collections;

public class NewsActivity extends AppCompatActivity implements Viewable{

    private ViewPager viewPagerNews;

    private NewsMainAdapter adapter;

    private NewsMainPresenter presenter;

    private Integer userId;

    private String userPass;

    private int newsId;

    private ProgressBar progressBarNews;

    private boolean darkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.buttonBack).setOnClickListener(view -> {
            finish();
        });

        progressBarNews = findViewById(R.id.progressBarNews);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getInt("userId");
            userPass = extras.getString("userPass");

            newsId = extras.getInt("newsId");
            darkMode = extras.getBoolean("darkMode");
        }

        viewPagerNews = findViewById(R.id.viewPagerNews);
        presenter = new NewsMainPresenter(this);
        progressBarNews.setVisibility(View.VISIBLE);
        presenter.loadData();

    }

    @Override
    public void showData(Object obj) {
        ArrayList<EntityNews> news = (ArrayList<EntityNews>) obj;
        Collections.reverse(news);

        // find clicked item
        int clickedItem = 0;
        for (int i = 0; i < news.size(); ++i) {
            if (news.get(i).getId() == newsId) {
                clickedItem = i;
            }
        }

        adapter = new NewsMainAdapter(this, news);
        adapter.setCurrentNews(newsId);
        adapter.setUserId(userId);
        adapter.setUserPass(userPass);
        adapter.setPager(viewPagerNews);
        viewPagerNews.setAdapter(adapter);
        viewPagerNews.setCurrentItem(clickedItem);
        progressBarNews.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}












