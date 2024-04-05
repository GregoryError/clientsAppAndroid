package org.success.isp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import org.success.isp.R;
import org.success.isp.adapters.PaymentsAdapter;
import org.success.isp.pojos.DB_dry_pay;
import org.success.isp.presenters.PaymentsPresenter;

import java.util.ArrayList;
import java.util.Collections;

public class PaymentsActivity extends AppCompatActivity implements Viewable {
    private SharedPreferences preferences;
    private Boolean darkMode;
    private PaymentsAdapter paymentsAdapter;
    private ArrayList<Integer> categories;
    private PaymentsPresenter paymentsPresenter;
    private RecyclerView recyclerViewPayments;
    private Toolbar toolbar;
    private String uName;
    private String uPass;
    private String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        findViewById(R.id.buttonBack).setOnClickListener(view -> {
            finish();
        });

        preferences = getSharedPreferences("success_prefs", Context.MODE_PRIVATE);

        if (preferences.getBoolean("darkMode", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            darkMode = true;
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            darkMode = false;
        }

        categories = new ArrayList<>();
        categories.add(95);     // 95 - qiwi transaction
        categories.add(96);     // 96 - sber incoming pay
        categories.add(600);    // 600 - cash pay
        categories.add(1000);   // 1000 - temp pay by user
        // categories.add(426);    // 426 - temp pay delete
        categories.add(423);    // 423 - blocked by pay reason
        categories.add(110);    // 110 - minus for services
        categories.add(0);      // 0 - admin minus

        recyclerViewPayments = findViewById(R.id.recyclerViewPayments);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            uName = bundle.getString("userName");
            uPass = bundle.getString("userPass");
            uId = bundle.getString("userId");

            paymentsPresenter = new PaymentsPresenter(this);
            paymentsPresenter.setUserId(uId);
            paymentsPresenter.setUserPass(uPass);
            paymentsPresenter.loadData();
        }
    }

    @Override
    public void showData(Object obj) {
        ArrayList<DB_dry_pay> payments = new ArrayList<>();

        for (DB_dry_pay p : (ArrayList<DB_dry_pay>) obj) {
            if (categories.contains(p.getCategory())) {
                payments.add(p);
            }
        }

        Collections.reverse(payments);

        paymentsAdapter = new PaymentsAdapter(this);
        paymentsAdapter.setCategories(categories);
        paymentsAdapter.setDarkMode(darkMode);
        recyclerViewPayments.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        recyclerViewPayments.setAdapter(paymentsAdapter);
        paymentsAdapter.setPaymentEvents(payments);
    }

    @Override
    public void showError(String str) {

    }
}