package org.success.isp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.success.isp.R;
import org.success.isp.adapters.HybridEntityAdapter;
import org.success.isp.pojos.EntitiesPack;
import org.success.isp.pojos.EntityArticle;
import org.success.isp.pojos.EntityNews;
import org.success.isp.pojos.EntityPromo;
import org.success.isp.pojos.IssueQuestion;
import org.success.isp.presenters.AdvisorPresenter;
import org.success.isp.workers.QuestionManager;
import org.success.isp.workers.pojos.QuestionAnswer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IssueResultActivity extends AppCompatActivity implements Viewable {

    private Toolbar toolbar;

    private TextView textViewIssueResume;

    private RecyclerView recyclerViewIssueResult;

    private Button buttonNotSolvedCall;

    private Button buttonNotSolvedSend;

    private Button buttonSolved;

    private String userId;

    private String userPass;

    private ArrayList<IssueQuestion> issueQuestions;

    private ArrayList<QuestionAnswer> userAnswersMap;

    private AdvisorPresenter presenter;

    private boolean darkMode;

    private String solutionsTitlesStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_result);

        solutionsTitlesStr = "";

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        findViewById(R.id.buttonBack).setOnClickListener(view -> {
            finish();
        });

        textViewIssueResume = findViewById(R.id.textViewIssueResume);
        recyclerViewIssueResult = findViewById(R.id.recyclerViewIssueResult);
        buttonNotSolvedCall = findViewById(R.id.buttonNotSolvedCall);
        buttonNotSolvedSend = findViewById(R.id.buttonNotSolvedSend);
        buttonSolved = findViewById(R.id.buttonSolved);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getString("userId");
            userPass = extras.getString("userPass");
            darkMode = extras.getBoolean("darkMode");
            userAnswersMap = (ArrayList<QuestionAnswer>) extras.getSerializable("userAnswersMap");
            issueQuestions = (ArrayList<IssueQuestion>) extras.getSerializable("questionSet");
            StringBuilder sb = new StringBuilder();
            sb.append("Резюме проблемы:\n");

            for (int i = 0; i < userAnswersMap.size(); ++i) {
                sb.append(issueQuestions.get(userAnswersMap.get(i).getQuestion()).getQuestion() + " \n" +
                        issueQuestions.get(userAnswersMap.get(i).getQuestion()).getOptions().get(userAnswersMap.get(i).getAnswer())).append("\n\n");
            }

            textViewIssueResume.setText(sb);
        }

        buttonNotSolvedCall.setOnClickListener(view -> {
            // ask for number firsf
            Bundle bundle = new Bundle();
            bundle.putSerializable("userAnswersMap", (Serializable) QuestionManager.getInstance().getUserAnswers());
            bundle.putSerializable("questionSet", (Serializable) issueQuestions);
            bundle.putString("extraString", solutionsTitlesStr + '\n' + getString(R.string.callMeStr));
            bundle.putString("userPass", userPass);
            bundle.putString("userId", userId);
            bundle.putBoolean("toTheChat", false);
            Intent intent = new Intent(this, AddExtraInfoActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        buttonNotSolvedSend.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("userAnswersMap", (Serializable) QuestionManager.getInstance().getUserAnswers());
            bundle.putSerializable("questionSet", (Serializable) issueQuestions);
            bundle.putString("extraString", solutionsTitlesStr + '\n' + getString(R.string.solveAndReply));
            bundle.putString("userPass", userPass);
            bundle.putString("userId", userId);
            bundle.putBoolean("toTheChat", true);
            Intent intent = new Intent(this, AddExtraInfoActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        buttonSolved.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("userAnswersMap", (Serializable) QuestionManager.getInstance().getUserAnswers());
            bundle.putSerializable("questionSet", (Serializable) issueQuestions);
            bundle.putString("extraString", solutionsTitlesStr + '\n' + getString(R.string.solvedLabel));
            bundle.putBoolean("isJustInfo", true);
            bundle.putString("userPass", userPass);
            bundle.putString("userId", userId);
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        ArrayList<String> tags = new ArrayList<>();
        presenter = new AdvisorPresenter(this);

        String userAnswerStr = null;
        Pattern pattern = Pattern.compile("\\b[а-яА-Яa-zA-Z]{2,}\\b");
        for (int i = 0; i < userAnswersMap.size(); ++i) {
            userAnswerStr = issueQuestions.get(userAnswersMap.get(i).getQuestion())
                    .getOptions().get(userAnswersMap.get(i).getAnswer());

            if (userAnswerStr != null) {
                Matcher matcher = pattern.matcher(userAnswerStr);
                while (matcher.find()) {
                    String word = matcher.group();
                    tags.add(word);
                }
            }
        }

        presenter.setTags(tags);
        presenter.loadData();
    }

    @Override
    public void showData(Object obj) {
        EntitiesPack entitiesPack = ((EntitiesPack) obj);
        if (entitiesPack != null) {

            if (entitiesPack.getPromos() != null ||
                    entitiesPack.getNews() != null ||
                    entitiesPack.getArticles() != null) {
                solutionsTitlesStr += getString(R.string.solutionsForUserStr);

                for (EntityArticle art : entitiesPack.getArticles()) {
                    solutionsTitlesStr += '-' + art.getName() + '\n';
                }
                for (EntityNews n : entitiesPack.getNews()) {
                    solutionsTitlesStr += '-' + n.getName() + '\n';
                }
                for (EntityPromo p : entitiesPack.getPromos()) {
                    solutionsTitlesStr += '-' + p.getName() + '\n';
                }
            }

            recyclerViewIssueResult.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            HybridEntityAdapter adapter = new HybridEntityAdapter(this);
            adapter.setDarkMode(darkMode);
            recyclerViewIssueResult.setAdapter(adapter);
            adapter.setPack(entitiesPack);

        }

    }

    @Override
    public void showError(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
