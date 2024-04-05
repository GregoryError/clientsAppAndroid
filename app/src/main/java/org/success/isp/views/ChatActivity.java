package org.success.isp.views;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import com.google.android.material.snackbar.Snackbar;

import org.success.isp.R;
import org.success.isp.adapters.ChatAdapter;
import org.success.isp.pojos.EntityMessage;
import org.success.isp.pojos.IssueQuestion;
import org.success.isp.presenters.ChatPresenter;
import org.success.isp.utils.SystemWorker;
import org.success.isp.workers.ChatSender;
import org.success.isp.workers.pojos.QuestionAnswer;

import java.util.ArrayList;
import java.util.Map;

public class ChatActivity extends AppCompatActivity implements Viewable {

    private Toolbar toolbar;

    private ImageButton buttonChatSend;

    private AppCompatMultiAutoCompleteTextView editChatMessage;

    private Snackbar currentSnackbar;

    private ConstraintLayout constraintChatRoot;

    private RecyclerView recyclerViewChat;

    private ChatPresenter chatPresenter;

    private ChatAdapter chatAdapter;

    private ChatSender chatSender;

    private Activity activity;

    private Boolean darkMode;

    private Handler handler;

    private ProgressBar progressBarChat;

    private String extraInfoString;

    private Runnable runnableCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        activity = this;
        extraInfoString = "";

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        findViewById(R.id.buttonBack).setOnClickListener(view -> {
            Intent intent = new Intent(activity, MainActivity.class);
            intent.putExtra("great", false);
            startActivity(intent);
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

//        Window window = getWindow();
//        window.setStatusBarColor(getColor(R.color.main_red));
//        SystemWorker.getInstance().changeStatusBarContrastStyle(window, false);

        buttonChatSend = findViewById(R.id.buttonChatSend);
        editChatMessage = findViewById(R.id.editChatMessage);
        progressBarChat = findViewById(R.id.progressBarChat);
        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        constraintChatRoot = findViewById(R.id.constraintChatRoot);

        // progressBarChat.setVisibility(View.VISIBLE);
        progressBarChat.setActivated(true);

        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        chatAdapter = null;
        editChatMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    buttonChatSend.setVisibility(View.VISIBLE);
                } else {
                    buttonChatSend.setVisibility(View.INVISIBLE);
                }
            }
        });

        StringBuilder sbOutsideMess = null;
        String incomeMessageStr = "";
        boolean isJustInfo = false;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            darkMode = bundle.getBoolean("darkMode");
            if (darkMode) {
                buttonChatSend.setColorFilter(getColor(R.color.main_light));
            }

            // If it goes from issue-solve activity

            if (bundle.containsKey("userAnswersMap")) {
                ArrayList<QuestionAnswer> userAnswersMap = (ArrayList<QuestionAnswer>) bundle.getSerializable("userAnswersMap");
                ArrayList<IssueQuestion> issueQuestions = (ArrayList<IssueQuestion>) bundle.getSerializable("questionSet");

                sbOutsideMess = new StringBuilder();
                sbOutsideMess.append("Клиент отправил заявку через средство диагностики.\nЛог диагностики:\n\n");

                for (int i = 0; i < userAnswersMap.size(); ++i) {
                    sbOutsideMess.append(issueQuestions.get(userAnswersMap.get(i).getQuestion()).getQuestion() + " \n" +
                            issueQuestions.get(userAnswersMap.get(i).getQuestion()).getOptions().get(userAnswersMap.get(i).getAnswer())).append("\n");
                }
            }

            if (bundle.containsKey("extraString")) {
                incomeMessageStr = bundle.getString("extraString");
            }

            if (bundle.containsKey("isJustInfo")) {
                isJustInfo = bundle.getBoolean("isJustInfo");
            }

            if (bundle.containsKey("extraInfo")) {
                extraInfoString = bundle.getString("extraInfo");
            }

            chatPresenter = new ChatPresenter((Viewable) activity);
            chatPresenter.setUserId(bundle.getString("userId"));
            chatPresenter.setUserPass(bundle.getString("userPass"));
            chatSender = new ChatSender(chatPresenter);
            chatSender.setUserId(bundle.getString("userId"));
            chatSender.setUserPass(bundle.getString("userPass"));

            handler = new Handler();

            runnableCode = new Runnable() {
                @Override
                public void run() {
                    // Выполнение в фоновом потоке

                    new Thread(() -> {
                        int pos = 0;
                        if (((LinearLayoutManager) recyclerViewChat.getLayoutManager()) != null) {
                            pos = ((LinearLayoutManager) recyclerViewChat.getLayoutManager()).findLastVisibleItemPosition();
                            // Выполнение ваших функций в фоновом потоке
//                            if (pos > 0)
//                                chatPresenter.pause();
//                            System.out.println("POS: " + pos);
                            // Обновление UI в основном потоке через Handler
                            handler.post(() -> {
                                // Обновление UI, если необходимо
                            });
                        }
                        chatPresenter.loadData();
                    }).start();
                    // Повторное выполнение через 3 секунды
                    handler.postDelayed(this, 3000);
                }
            };
            // Запуск первого выполнения в основном потоке
            handler.post(runnableCode);
        }

        buttonChatSend.setOnClickListener(v -> {
            chatSender.sendMessage(editChatMessage.getText().toString().trim());
            editChatMessage.setText("");
        });

        recyclerViewChat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int displayCapacity = 0;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                    if (displayCapacity == 0)
                        displayCapacity = lastVisibleItemPosition;

                    if (lastVisibleItemPosition > displayCapacity + 2)
                        chatPresenter.pause();
                    else
                        chatPresenter.start();
                }
            }
        });

        if (extraInfoString != null && !extraInfoString.isEmpty()) {
            chatSender.sendMessage(extraInfoString);
            Toast.makeText(activity, "Вам ответит первый свободный специалист!", Toast.LENGTH_LONG).show();
        }

        if (sbOutsideMess != null) {
            chatSender.sendMessage(sbOutsideMess.toString() + extraInfoString + incomeMessageStr);
            if (isJustInfo)
                Toast.makeText(activity, "Информация отправлена!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(activity, "Заявка отправлена!", Toast.LENGTH_SHORT).show();
        }


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(activity, MainActivity.class);
                intent.putExtra("great", false);
                startActivity(intent);
            }
        });

    }

    @Override
    public void showData(Object obj) {
        ArrayList<EntityMessage> messages = (ArrayList<EntityMessage>) obj;

        if (chatAdapter == null) {
            chatAdapter = new ChatAdapter(this);
            recyclerViewChat.setAdapter(chatAdapter);
        }
        chatAdapter.setMessages(messages);
        if (messages != null && messages.size() > 0)
            recyclerViewChat.scrollToPosition(0);

        if (progressBarChat.isActivated()) {
            progressBarChat.setVisibility(View.INVISIBLE);
            progressBarChat.setActivated(false);
        }

    }

    @Override
    public void showError(String str) {
        showSnack(str);
    }

    private void showSnack(String str) {
        currentSnackbar = Snackbar.make(constraintChatRoot, str, Snackbar.LENGTH_LONG);
        View view = currentSnackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        currentSnackbar.show();
    }

    private static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        // Остановка задачи при завершении активности
        if (handler != null)
            handler.removeCallbacks(runnableCode);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
















