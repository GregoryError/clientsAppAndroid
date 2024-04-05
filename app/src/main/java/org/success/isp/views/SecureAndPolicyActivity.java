package org.success.isp.views;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.success.isp.R;
import org.success.isp.adapters.HybridEntityAdapter;
import org.success.isp.pojos.EntitiesPack;
import org.success.isp.presenters.AdvisorPresenter;

import java.util.ArrayList;

public class SecureAndPolicyActivity extends AppCompatActivity implements Viewable{

    private Toolbar toolbar;

    private RecyclerView recyclerViewSecurity;

    private ProgressBar progressBarSecurity;

    private HybridEntityAdapter adapter;

    private AdvisorPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_secure_and_policy);
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
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("great", false);
            startActivity(intent);
        });

        progressBarSecurity = findViewById(R.id.progressBarSecurity);
        recyclerViewSecurity = findViewById(R.id.recyclerViewSecurity);

        progressBarSecurity.setVisibility(View.VISIBLE);
        presenter = new AdvisorPresenter(this);
        adapter = new HybridEntityAdapter(this);
        ArrayList<String> tags = new ArrayList<>();
        tags.add("политика_конфиденциальности");
        tags.add("безопасность_данных");
        tags.add("privacy_policy");
        presenter.setTags(tags);
        presenter.loadData();

    }

    @Override
    public void showData(Object obj) {
        EntitiesPack pack = (EntitiesPack) obj;
        recyclerViewSecurity.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewSecurity.setAdapter(adapter);
        adapter.setPack(pack);
        progressBarSecurity.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}