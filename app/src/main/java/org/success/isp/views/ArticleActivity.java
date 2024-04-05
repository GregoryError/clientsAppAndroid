package org.success.isp.views;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import org.success.isp.R;
import org.success.isp.adapters.ArticleMainAdapter;
import org.success.isp.pojos.EntityArticle;
import org.success.isp.pojos.EntityNews;
import org.success.isp.presenters.ArticlesMainPresenter;

import java.util.ArrayList;
import java.util.Collections;

public class ArticleActivity extends AppCompatActivity implements Viewable {

    private Toolbar toolbar;

    private ViewPager viewPagerArticles;

    private ArticleMainAdapter adapter;

    private ArticlesMainPresenter presenter;

    private int articleId;

    private ProgressBar progressBarArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_article);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        findViewById(R.id.buttonBack).setOnClickListener(view -> {
            finish();
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            articleId = extras.getInt("articleId");
        }

        progressBarArticle = findViewById(R.id.progressBarArticle);
        viewPagerArticles = findViewById(R.id.viewPagerArticles);

        progressBarArticle.setVisibility(View.VISIBLE);
        presenter = new ArticlesMainPresenter(this);
        presenter.loadData();
    }

    @Override
    public void showData(Object obj) {
        ArrayList<EntityArticle> articles = (ArrayList<EntityArticle>) obj;


        // find clicked item
        int clickedItem = 0;
        for (int i = 0; i < articles.size(); ++i) {
            if (articles.get(i).getId() == articleId) {
                clickedItem = i;
            }
        }
        adapter = new ArticleMainAdapter(this, articles);
        adapter.setCurrentArticle(articleId);
        adapter.setPager(viewPagerArticles);
        viewPagerArticles.setAdapter(adapter);
        viewPagerArticles.setCurrentItem(clickedItem);
        progressBarArticle.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}



















