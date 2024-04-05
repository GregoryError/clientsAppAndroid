package org.success.isp.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.success.isp.R;
import org.success.isp.pojos.IssueQuestion;
import org.success.isp.workers.QuestionManager;


public class StepMenuFragment extends Fragment {
    private int pageNumber;

    private IssueQuestion question;

    public void setQuestion(IssueQuestion question) {
        this.question = question;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_menu, container, false);


        // Получение активности, к которой привязан фрагмент
        ViewPager viewPager;
        Activity activity = getActivity();
        if (activity instanceof IssueActivity) {
            IssueActivity issueActivity = (IssueActivity) activity;

            // Получение ViewPager из вашей активности
            viewPager = issueActivity.findViewById(R.id.viewPagerIssues);

            // Перелистывание к следующему вопросу
        } else {
            viewPager = null;
        }

        // Настройка вопроса и вариантов ответов на странице
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        TextView textViewQuestion = view.findViewById(R.id.textViewQuestion);
        textViewQuestion.setText(question.getQuestion());
        RadioGroup radioGroup = view.findViewById(R.id.radioGroupAnswers);
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 30, 10, 30);

        if (question.getOptions() != null) {
            for (int i = 0; i < question.getOptions().size(); ++i) {
                RadioButton buttonAnswer = new RadioButton(getContext());
                buttonAnswer.setText(question.getOptions().get(i));
                buttonAnswer.setTextSize(18f);

                int finalI = i;
                buttonAnswer.setOnClickListener(view1 -> {
                    QuestionManager.getInstance().answer(question.getQid(), finalI);
                    viewPager.setCurrentItem(QuestionManager.getInstance().getNextQuestion(), true);
                });

                radioGroup.addView(buttonAnswer, i, params);
            }
        }

        return view;
    }
}
