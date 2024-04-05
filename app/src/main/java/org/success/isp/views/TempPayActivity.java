package org.success.isp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.success.isp.R;
import org.success.isp.api.ApiFactory;
import org.success.isp.api.ApiService;
import org.success.isp.pojos.EntityMisc;
import org.success.isp.presenters.TempPayPresenter;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TempPayActivity extends AppCompatActivity implements Viewable {

    private Toolbar toolbar;

    private TempPayPresenter tempPayPresenter;

    private TextView textViewTempPayDescription;

    private Button buttonTempPayActivate;

    private CheckBox checkboxAgree;

    private Context context;

    private ConstraintLayout constraintTempPayRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_pay);
        context = this;
        constraintTempPayRoot = findViewById(R.id.constraintTempPayRoot);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        findViewById(R.id.buttonBack).setOnClickListener(view -> {
            finish();
        });

        textViewTempPayDescription = findViewById(R.id.textViewTempPayDescription);
        buttonTempPayActivate = findViewById(R.id.buttonTempPayActivate);
        checkboxAgree = findViewById(R.id.checkboxAgree);

        checkboxAgree.setOnClickListener(view -> {
            if (checkboxAgree.isChecked())
                buttonTempPayActivate.setVisibility(View.VISIBLE);
            else
                buttonTempPayActivate.setVisibility(View.INVISIBLE);
        });


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            buttonTempPayActivate.setOnClickListener(view -> {
                ApiFactory.getInstance().getApiService().getTrustedPay(bundle.getString("userId"),
                        bundle.getString("userPass")).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Snackbar.make(constraintTempPayRoot, response.body(), Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Snackbar.make(constraintTempPayRoot, t.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
            });
        }

        tempPayPresenter = new TempPayPresenter(this);
        tempPayPresenter.loadData();

    }

    @Override
    public void showData(Object obj) {
        EntityMisc entityMisc = ((EntityMisc) obj);
        textViewTempPayDescription.setText(entityMisc.getContent().replaceAll("\\\\n", "\\\n"));
    }

    @Override
    public void showError(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}