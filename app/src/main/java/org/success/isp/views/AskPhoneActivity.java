package org.success.isp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import org.success.isp.R;
import org.success.isp.pojos.IssueQuestion;
import org.success.isp.utils.MaskWatcher;
import org.success.isp.workers.QuestionManager;

import java.io.Serializable;
import java.util.ArrayList;

public class AskPhoneActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private String userId;

    private String userPass;

    private String phoneStr;

    private TextInputEditText editTextPhone;

    private ArrayList<IssueQuestion> questionsList;

    private String extraInfoStr;

    private String extraString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_phone);

        extraInfoStr = "";
        extraString = "";

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        findViewById(R.id.buttonBack).setOnClickListener(view -> {
            finish();
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getString("userId");
            userPass = extras.getString("userPass");
            questionsList = ((ArrayList<IssueQuestion>) extras.getSerializable("questionSet"));
            if (extras.containsKey("extraInfo"))
                extraInfoStr = extras.getString("extraInfo");
            if (extras.containsKey("extraString"))
                extraString = extras.getString("extraString");
        }

        Button buttonConfirm = findViewById(R.id.buttonConfirm);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPhone.addTextChangedListener(new MaskWatcher("## (###) ###-##-##", 11));
        editTextPhone.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(18)
        });

        editTextPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                editTextPhone.setText("+7 ");
                if (editTextPhone.length() > 3)
                    editTextPhone.setSelection(4);
            }
        });

        editTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editTextPhone.getText().toString().trim().length() > 17) {
                    buttonConfirm.setVisibility(Button.VISIBLE);
                } else {
                    buttonConfirm.setVisibility(Button.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        buttonConfirm.setOnClickListener(view -> {
            phoneStr = editTextPhone.getText().toString().trim();
            sendReport();
        });

        editTextPhone.requestFocus();

    }

    private void sendReport() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("userAnswersMap", (Serializable) QuestionManager.getInstance().getUserAnswers());
        bundle.putSerializable("questionSet", (Serializable) questionsList);
        bundle.putString("extraString", extraInfoStr + extraString +
                "\nМой номер: " + phoneStr + getString(R.string.callToSolve));
        bundle.putString("userPass", userPass);
        bundle.putString("userId", userId);
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}



















