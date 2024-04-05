package org.success.isp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.success.isp.R;
import org.success.isp.adapters.StepMenuPagerAdapter;
import org.success.isp.pojos.IssueQuestion;
import org.success.isp.workers.QuestionManager;

import java.io.Serializable;
import java.util.ArrayList;

public class IssueActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private ViewPager viewPagerIssues;

    private ImageView resetButton;

    private ArrayList<IssueQuestion> questionsList;

    private StepMenuPagerAdapter adapter;

    private Boolean darkMode;

    private String userPass;

    private String userId;

    enum NextAction {
        SendToChat,
        AskForPhone,
        ShowResult
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        findViewById(R.id.buttonBack).setOnClickListener(view -> {
            finish();
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            darkMode = bundle.getBoolean("darkMode");
            userId = bundle.getString("userId");
            userPass = bundle.getString("userPass");
        }

        viewPagerIssues = findViewById(R.id.viewPagerIssues);
        resetButton = findViewById(R.id.resetButton);
        resetButton.setImageResource(R.drawable.restart_24);
        if (darkMode) {
            ((ImageView) resetButton).setColorFilter(ContextCompat.getColor(this, R.color.main_light), PorterDuff.Mode.SRC_IN);
        }

        setupQuestionsTree();
        adapter = new StepMenuPagerAdapter(getSupportFragmentManager());
        adapter.setQuestions(questionsList);
        viewPagerIssues.setAdapter(adapter);


        resetButton.setOnClickListener(v -> {
            QuestionManager.getInstance().reset();
            adapter = new StepMenuPagerAdapter(getSupportFragmentManager());
            adapter.setQuestions(questionsList);
            viewPagerIssues.setAdapter(adapter);
        });


//        if (QuestionManager.getInstance().getNextQuestion() == 0) {
//            findViewById(R.id.textViewChoseLabel).setVisibility(View.INVISIBLE);
//            resetButton.setVisibility(View.INVISIBLE);
//        }
    }

    private void setupQuestionsTree() {
        // TODO: собираюсь вынести все эти данные в базу и как-то реализовать возможность удаленного создание этого дерева вопросов

        // Создание объекта QuestionManager
        QuestionManager qManager = QuestionManager.getInstance();
        qManager.reset();

        questionsList = new ArrayList<>();

        ArrayList<String> options;

        // Questions

        IssueQuestion question;

        options = new ArrayList<>();
        options.add("Интернет.");
        options.add("Кабельное ТВ.");
        options.add("Что-то другое.");
        questionsList.add(new IssueQuestion(0, "С какой услугой у вас проблемы?", options));

        options = new ArrayList<>();
        options.add("Низкая скорость.");
        options.add("Нужно настроить соединение.");
        options.add("Пропало соединение.");
        options.add("Не работает что-то конкретное (игра, сайт, приложение).");
        options.add("Приложение/игра сообщает, что есть потери даных.");
        options.add("Нужен мастер на дом.");
        questionsList.add(new IssueQuestion(1, "Что именно не так?", options));

        options = new ArrayList<>();
        options.add("Да, я использую беспроводное соединение WiFi.");
        options.add("Нет, кабель идет напрямую в устройство.");
        questionsList.add(new IssueQuestion(2, "Вы используете Wi-Fi?", options));

        options = new ArrayList<>();
        options.add("Роутер.");
        options.add("Компьютер или ноутбук.");
        options.add("Телевизор (напрямую).");
        options.add("Что-то другое.");
        questionsList.add(new IssueQuestion(3, "В какое устройство включен кабель?", options));

        options = new ArrayList<>();
        options.add("Нет ни одного канала.");
        options.add("Нет какого-то конкретного канала/каналов.");
        options.add("Нужно настроить телевизор.");
        options.add("Нужен мастер на дом.");
        options.add("Помехи или сыпятся каналы.");
        questionsList.add(new IssueQuestion(4, "Что именно не так?", options));

        options = new ArrayList<>();
        options.add("Работы с кабелем в квартире (удлинить, укоротить).");
        options.add("Заменить/установить наконечник.");
        options.add("Настроить оборудование (роутер, компьютер, ТВ).");
        options.add("Установить интернет-розетку.");
        options.add("Установить розетку ТВ.");
        options.add("Другое.");
        questionsList.add(new IssueQuestion(5, "Для чего нужен мастер?", options));

        options = new ArrayList<>();
        options.add("Размещение кабелей в доме.");
        options.add("Замечание о работе сотрудника.");
        options.add("Узнать подключен ли дом.");
        options.add("Другое.");
        questionsList.add(new IssueQuestion(6, "Уточнение:", options));

        options = new ArrayList<>();
        options.add("Через сообщения.");
        options.add("Пусть мне перезвонят.");
        questionsList.add(new IssueQuestion(7, "Как удобнее с вами связаться?", options));

        options = new ArrayList<>();
        options.add("Нет, только на некоторых.");
        options.add("Абсолютно на всех.");
        questionsList.add(new IssueQuestion(8, "На всех каналах?", options));

        options = new ArrayList<>();
        options.add("Один.");
        options.add("Больше одного");
        questionsList.add(new IssueQuestion(9, "Сколько телевизоров у вас подключено?", options));

        options = new ArrayList<>();
        options.add("На всех.");
        options.add("Только на одном");
        questionsList.add(new IssueQuestion(10, "Проблема наблюдается на всех телевизорах?", options));


        // Задание связей между вопросами (дерево вопросов)

        qManager.setConnection(0, 0, 1);  // интернет               -> Что именно не так?
        qManager.setConnection(1, 0, 2);  // Что именно не так?     -> Низкая скорость - > Вы используете Wi-Fi?
        // Вы используете Wi-Fi? - статьи в зависимости от ответа
        qManager.setConnection(1, 1, 3);  // Что именно не так?     -> Нужно настроить соединение -> В какое устройство включен кабель?
        qManager.setConnection(1, 5, 5); // то именно не так? (4)   -> Для чего нужен мастер?
        // В какое устройство включен кабель? - статьи в зависимости от ответа
        qManager.setConnection(0, 1, 4); // Кабельное ТВ            -> Что именно не так?
        // Что именно не так? - статьи в зависимости от ответа, блок советов
        qManager.setConnection(4, 3, 5); // Что именно не так? (3)  -> Для чего нужен мастер?

        qManager.setConnection(0, 2, 6); // другое (2)              -> Выберите подходящий вариант

        qManager.setConnection(4, 4, 8);
        qManager.setConnection(4, 0, 9);

        qManager.setConnection(8, 1, 9);

        qManager.setConnection(9, 1, 10);


        // Как удобнее решить проблему?
        qManager.setConnection(8, 0, 7);
        qManager.setConnection(6, 0, 7);
        qManager.setConnection(6, 1, 7);
        qManager.setConnection(6, 2, 7);
        qManager.setConnection(6, 3, 7);
        qManager.setConnection(5, 0, 7);
        qManager.setConnection(5, 1, 7);
        qManager.setConnection(5, 2, 7);
        qManager.setConnection(5, 3, 7);
        qManager.setConnection(5, 4, 7);
        qManager.setConnection(5, 5, 7);
//        qManager.setConnection(4, 0, 7);
        qManager.setConnection(4, 1, 7);

        // qManager.setConnection(11, -1, 7);

        // Привязка запуска активности к определенным (конечным) ответам

        setActionToAnswer(1, 2, NextAction.ShowResult);
        setActionToAnswer(1, 3, NextAction.ShowResult);
        setActionToAnswer(1, 4, NextAction.ShowResult);

        setActionToAnswer(2, 0, NextAction.ShowResult);
        setActionToAnswer(2, 1, NextAction.ShowResult);

        setActionToAnswer(3, 0, NextAction.ShowResult);
        setActionToAnswer(3, 1, NextAction.ShowResult);
        setActionToAnswer(3, 2, NextAction.ShowResult);
        setActionToAnswer(3, 3, NextAction.ShowResult);

        setActionToAnswer(4, 2, NextAction.ShowResult);

        setActionToAnswer(9, 0, NextAction.ShowResult);

        setActionToAnswer(10, 0, NextAction.ShowResult);
        setActionToAnswer(10, 1, NextAction.ShowResult);

        setActionToAnswer(7, 0, NextAction.SendToChat);
        setActionToAnswer(7, 1, NextAction.AskForPhone);

    }


    void setActionToAnswer(int question, int answer, NextAction action) {
        if (action == NextAction.ShowResult) {
            QuestionManager.getInstance().setSpecialAction(question, answer, () -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable("userAnswersMap", (Serializable) QuestionManager.getInstance().getUserAnswers());
                bundle.putSerializable("questionSet", (Serializable) questionsList);
                bundle.putString("userPass", userPass);
                bundle.putString("userId", userId);
                bundle.putBoolean ("darkMode", darkMode);
                Intent intent = new Intent(this, IssueResultActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            });
        }

        if (action == NextAction.SendToChat) {
            QuestionManager.getInstance().setSpecialAction(question, answer, () -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable("userAnswersMap", (Serializable) QuestionManager.getInstance().getUserAnswers());
                bundle.putSerializable("questionSet", (Serializable) questionsList);
                bundle.putString("extraString", getString(R.string.solveAndReply));
                bundle.putString("userPass", userPass);
                bundle.putString("userId", userId);
                bundle.putBoolean("toTheChat", true);
                Intent intent = new Intent(this, AddExtraInfoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            });
        }

        if (action == NextAction.AskForPhone) {
            QuestionManager.getInstance().setSpecialAction(question, answer, () -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable("userAnswersMap", (Serializable) QuestionManager.getInstance().getUserAnswers());
                bundle.putSerializable("questionSet", (Serializable) questionsList);
                bundle.putString("userPass", userPass);
                bundle.putString("userId", userId);
                bundle.putBoolean("toTheChat", false);
                Intent intent = new Intent(this, AddExtraInfoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        QuestionManager.getInstance().reset();
    }
}





































