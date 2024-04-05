package org.success.isp.views;

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
import org.success.isp.adapters.PromoMainAdapter;
import org.success.isp.pojos.EntityNews;
import org.success.isp.pojos.EntityPromo;
import org.success.isp.presenters.NewsMainPresenter;
import org.success.isp.presenters.PromoMainPresenter;

import java.util.ArrayList;
import java.util.Collections;

public class PromoActivity extends AppCompatActivity implements Viewable{

    private ViewPager viewPagerPromo;

    private PromoMainAdapter adapter;

    private PromoMainPresenter presenter;

    private Integer userId;

    private String userPass;

    private int promoId;

    private ProgressBar progressBarPromo;

    private boolean darkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_promo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.buttonBack).setOnClickListener(view -> {
            finish();
        });

        progressBarPromo = findViewById(R.id.progressBarPromo);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getInt("userId");
            userPass = extras.getString("userPass");

            promoId = extras.getInt("promoId");
            darkMode = extras.getBoolean("darkMode");
        }

        viewPagerPromo = findViewById(R.id.viewPagerPromo);
        presenter = new PromoMainPresenter(this);
        progressBarPromo.setVisibility(View.VISIBLE);
        presenter.loadData();
    }

    @Override
    public void showData(Object obj) {
        ArrayList<EntityPromo> promo = (ArrayList<EntityPromo>) obj;
        Collections.reverse(promo);

        // find clicked item
        int clickedItem = 0;
        for (int i = 0; i < promo.size(); ++i) {
            if (promo.get(i).getId() == promoId) {
                clickedItem = i;
            }
        }

        adapter = new PromoMainAdapter(this, promo);
        adapter.setCurrentNews(promoId);
        adapter.setPager(viewPagerPromo);
        viewPagerPromo.setAdapter(adapter);
        viewPagerPromo.setCurrentItem(clickedItem);
        progressBarPromo.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}