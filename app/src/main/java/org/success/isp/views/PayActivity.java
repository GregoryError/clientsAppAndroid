package org.success.isp.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.success.isp.R;
import org.success.isp.adapters.HybridEntityAdapter;
import org.success.isp.pojos.EntitiesPack;
import org.success.isp.presenters.AdvisorPresenter;

import java.util.ArrayList;

public class PayActivity extends AppCompatActivity implements Viewable{

    private RecyclerView recyclerViewPay;

    private HybridEntityAdapter adapter;

    private AdvisorPresenter presenter;

    private ProgressBar progressBarPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pay);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.buttonBack).setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("great", false);
            startActivity(intent);
        });

        recyclerViewPay = findViewById(R.id.recyclerViewPay);
        progressBarPay = findViewById(R.id.progressBarPay);

        progressBarPay.setVisibility(View.VISIBLE);

        presenter = new AdvisorPresenter(this);
        ArrayList<String> tags = new ArrayList<>();
        tags.add("оплата");
        tags.add("платежи");
        tags.add("перевод");
        tags.add("оплата услуг");
        presenter.setTags(tags);
        presenter.loadData();

    }

    @Override
    public void showData(Object obj) {
        adapter = new HybridEntityAdapter(this);
        recyclerViewPay.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewPay.setAdapter(adapter);
        adapter.setPack((EntitiesPack) obj);
        progressBarPay.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String str) {

    }
}