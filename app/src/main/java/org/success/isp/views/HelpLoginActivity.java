package org.success.isp.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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

public class HelpLoginActivity extends AppCompatActivity implements Viewable {

    private Toolbar toolbar;

    private RecyclerView recyclerViewLoginHelp;

    private AdvisorPresenter presenter;

    private HybridEntityAdapter adapter;

    private ProgressBar progressBarLoginHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_help_login);
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

        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        recyclerViewLoginHelp = findViewById(R.id.recyclerViewLoginHelp);
        progressBarLoginHelp = findViewById(R.id.progressBarLoginHelp);
        progressBarLoginHelp.setVisibility(View.VISIBLE);
        presenter = new AdvisorPresenter(this);
        adapter = new HybridEntityAdapter(this);
        ArrayList<String> tags = new ArrayList<>();
        tags.add("восстановление_входа");
        tags.add("вход_в_систему");
        presenter.setTags(tags);
        presenter.loadData();
    }

    @Override
    public void showData(Object obj) {
        EntitiesPack pack = (EntitiesPack) obj;
        recyclerViewLoginHelp.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewLoginHelp.setAdapter(adapter);
        adapter.setPack(pack);
        progressBarLoginHelp.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}