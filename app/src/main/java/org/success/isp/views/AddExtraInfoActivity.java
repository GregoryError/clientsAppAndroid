package org.success.isp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import org.success.isp.R;
import org.success.isp.workers.QuestionManager;

import java.io.Serializable;
import java.util.ArrayList;

public class AddExtraInfoActivity extends AppCompatActivity {

    private ArrayList<IssueActivity> questionsList;

    private String userPass;

    private String userId;

    private boolean toTheChat;

    private String extraString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_extra_info);

        toTheChat = false;
        extraString = "";
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        EditText editTextExtraInfo = findViewById(R.id.editTextExtraInfo);

        findViewById(R.id.buttonBack).setOnClickListener(view -> {
            finish();
        });

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            userPass = extra.getString("userPass");
            userId = extra.getString("userId");
            questionsList = (ArrayList<IssueActivity>) extra.getSerializable("questionSet");
            toTheChat = extra.getBoolean("toTheChat");
            if (extra.containsKey("extraString"))
                extraString = extra.getString("extraString");
        }


        findViewById(R.id.buttonConfirm).setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("userAnswersMap", (Serializable) QuestionManager.getInstance().getUserAnswers());
            bundle.putSerializable("questionSet", (Serializable) questionsList);
            bundle.putString("userPass", userPass);
            bundle.putString("userId", userId);
            if (editTextExtraInfo.getText().toString().length() > 0)
                bundle.putString("extraInfo", "\nДоп. инфо. о проблеме: " + editTextExtraInfo.getText().toString().trim() + '\n');

            if (extraString.length() > 0)
                bundle.putString("extraString", extraString);

            if (toTheChat) {
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra("toTheChat", true);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, AskPhoneActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}