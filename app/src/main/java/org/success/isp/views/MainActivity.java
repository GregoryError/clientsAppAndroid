package org.success.isp.views;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;

import org.success.isp.R;
import org.success.isp.adapters.ArticlesAdapter;
import org.success.isp.adapters.NewsAdapter;
import org.success.isp.adapters.PromoSliderAdapter;
import org.success.isp.pojos.AppInitData;
import org.success.isp.pojos.ExtraEntityPreview;
import org.success.isp.presenters.MainPresenter;
import org.success.isp.utils.Session;
import org.success.isp.utils.firebase.AppFirebaseMessaging;
import org.success.isp.workers.CommonDataSender;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements Viewable {
    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private LinearLayout linearLayoutAuth;
    private Toolbar toolBar;
    private ProgressBar progressBar;
    private ScrollView scrollViewMain;
    private TextView textViewNewsLabel;
    private RecyclerView recyclerViewArticles;
    private Snackbar snack;
    private Timer timer;
    private PromoSliderAdapter promoSliderAdapter;
    private ArticlesAdapter articlesAdapter;
    private NewsAdapter newsAdapter;
    private MainPresenter mainPresenter;
    private TextView textViewToolBar;
    private RecyclerView recyclerViewNews;
    private TextView textViewBalance;
    private Button buttonPay;
    private TextView textViewBalanceCurrent;
    private TextView textViewNextDate;
    private TextView textViewPlanName;
    private ViewPager viewPagerPromo;
    private CardView cardViewPlanPanel;
    private CardView cardViewDetailsPanel;
    private String uName = null;
    private String uPass = null;
    private String userId = null;
    private SharedPreferences preferences;
    private Menu menu;
    private Boolean darkMode;
    private Boolean emulate;
    private AppInitData appInitData;
    private boolean needGreat;
    private boolean needRefresh;
    private boolean needLoadElements;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CommonDataSender commonDataSender;
    private String planNameStr;
    private Float currentBalance;

    private int loads;

    private ConstraintLayout constraintHelpLogin;

    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        preferences = getSharedPreferences("success_prefs", Context.MODE_PRIVATE);
        emulate = false;
        needGreat = true;
        needRefresh = true;
        needLoadElements = true;
        currentBalance = 0f;
        loads = 0;


        // Out
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("ap_was_out")) {
            preferences.edit().remove("userName").apply();
            preferences.edit().remove("userPass").apply();
            preferences.edit().remove("userId").apply();
        }

        if (extras != null && extras.containsKey("great")) {
            needGreat = extras.getBoolean("great");
        }

        if (extras != null && extras.containsKey("refresh")) {
            needRefresh = extras.getBoolean("refresh");
        }

        askNotificationPermission();

        //        Window window = getWindow();
        //        window.setStatusBarColor(getColor(R.color.main_black));
        //        SystemWorker.getInstance().changeStatusBarContrastStyle(window, false);

        mainPresenter = new MainPresenter(this);
        commonDataSender = new CommonDataSender(this);


        linearLayoutAuth = findViewById(R.id.linearLayoutAuth);
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressBar);
        scrollViewMain = findViewById(R.id.scrollViewMain);
        recyclerViewNews = findViewById(R.id.recyclerViewNews);
        cardViewPlanPanel = findViewById(R.id.cardViewPlanPanel);
        cardViewDetailsPanel = findViewById(R.id.cardViewDetailsPanel);
        recyclerViewArticles = findViewById(R.id.recyclerViewArticles);
        textViewNewsLabel = findViewById(R.id.textViewNewsLabel);
        textViewToolBar = findViewById(R.id.textViewToolBar);
        textViewBalance = findViewById(R.id.textViewBalance);
        buttonPay = findViewById(R.id.buttonPay);
        textViewBalanceCurrent = findViewById(R.id.textViewBalanceCurrent);
        textViewNextDate = findViewById(R.id.textViewNextDate);
        viewPagerPromo = findViewById(R.id.viewPagerPromo);
        textViewPlanName = findViewById(R.id.textViewPlanName);
        constraintHelpLogin = findViewById(R.id.constraintHelpLogin);

        constraintHelpLogin.setOnClickListener(view -> {
            Intent intent = new Intent(this, HelpLoginActivity.class);
            startActivity(intent);
        });

        textViewToolBar.setOnClickListener(view -> {

            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(userId.toString());
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("", userId.toString());
                clipboard.setPrimaryClip(clip);
            }

            Toast.makeText(this, R.string.copyed, Toast.LENGTH_SHORT).show();
        });

        cardViewDetailsPanel.setOnClickListener(view -> {
            if (currentBalance >= 0f) {
                Toast.makeText(this, R.string.accessGranted, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.requiredSummLabel) + currentBalance.toString(), Toast.LENGTH_SHORT).show();
            }

        });

        cardViewPlanPanel.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle(planNameStr)
                    .setMessage("Желаете сменить тариф?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        Bundle bundle = new Bundle();
                        bundle.putString("extraInfo", "Подскажите, какие тарифы сейчас доступны для меня?");
                        bundle.putString("userPass", uPass);
                        bundle.putString("userId", userId);
                        Intent intent = new Intent(this, ChatActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(R.drawable.rocket_24)
                    .show();
        });

        if (preferences.getBoolean("darkMode", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            scrollViewMain.setBackgroundColor(getColor(R.color.main_black));
            darkMode = true;
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            scrollViewMain.setBackgroundColor(getColor(R.color.main_back_light));
            darkMode = false;
        }

        buttonPay.setOnClickListener(v -> {
            Intent intent = new Intent(this, PayActivity.class);
            startActivity(intent);
        });

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        setSupportActionBar(toolBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Initialize auth elements
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        snack = Snackbar.make(linearLayoutAuth, "", Snackbar.LENGTH_SHORT);
        View snack_view = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snack_view.getLayoutParams();
        params.gravity = Gravity.TOP;
        snack_view.setLayoutParams(params);


        // Set a click listener for the login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve entered username and password
                uName = editTextUsername.getText().toString().trim();
                uPass = editTextPassword.getText().toString().trim();

                // Implement authentication logic here
                if (uName.length() > 0 && uPass.length() > 0) {
                    progressBar.setVisibility(View.VISIBLE);
                    mainPresenter.setUserName(uName);
                    mainPresenter.setUserPass(uPass);
                    mainPresenter.loadData();
                } else {
                    snack.setText("Введите имя и пароль");
                    snack.setDuration(Snackbar.LENGTH_SHORT);
                    snack.show();
                }
            }
        });


        if (preferences != null) {
            if (preferences.contains("userName") && preferences.contains("userPass")) {
                uName = preferences.getString("userName", null);
                uPass = preferences.getString("userPass", null);
                Session.getInstance().setUserName(uName);
                Session.getInstance().setUserPass(uPass);
                swipeRefreshLayout.setEnabled(true);
            }
        }

        swipeRefreshLayout.setOnRefreshListener(() -> {
            needGreat = false;
            needLoadElements = false;
            if (timer != null)
                timer.cancel();
            if (uPass != null && uName != null) {
                // Was authorized
                if (!emulate) {
                    mainPresenter.setUserName(uName);
                    mainPresenter.setUserPass(uPass);
                    mainPresenter.loadData();
                } else {
                    showData(null);
                }
            }
            swipeRefreshLayout.setRefreshing(false);
        });

        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setDistanceToTriggerSync(250);

        scrollViewMain.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (!v.canScrollVertically(1) && !swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setEnabled(true);
            } else {
                swipeRefreshLayout.setEnabled(false);
            }
        });

        scrollViewMain.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == 0 && !swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setEnabled(true);
            } else {
                swipeRefreshLayout.setEnabled(false);
            }
        });


        if (uPass != null && uName != null) {
            // Was authorized
            progressBar.setVisibility(View.VISIBLE);
            if (!emulate) {
                mainPresenter.setUserName(uName);
                mainPresenter.setUserPass(uPass);
                mainPresenter.loadData();
            } else {
                showData(null);
            }
        } else {
            // Was`t autorized before
            linearLayoutAuth.setVisibility(View.VISIBLE);
        }

        if (!preferences.contains("newToken"))
            preferences.edit().putBoolean("newToken", true).apply();
        setupButtons();
    }


    @Override
    public void showData(Object obj) {
        hideKeyboard(this);
        if (!emulate) {
            appInitData = ((AppInitData) obj);
        }

        // Auth success => Save auth data
        if (preferences != null && !preferences.contains("userName") && !preferences.contains("userPass")) {
            preferences.edit().putString("userName", uName).apply();
            preferences.edit().putString("userPass", uPass).apply();
            Session.getInstance().setUserName(uName);
            Session.getInstance().setUserPass(uPass);
        }

        if (needGreat) {
            snack.setText("Добро пожаловать!");
            snack.setDuration(Snackbar.LENGTH_SHORT);
            snack.show();
        }

        // enable if auth
        linearLayoutAuth.setVisibility(View.INVISIBLE);
        toolBar.setVisibility(View.VISIBLE);
        scrollViewMain.setVisibility(View.VISIBLE);
        textViewNewsLabel.setVisibility(View.VISIBLE);
        recyclerViewArticles.setVisibility(View.VISIBLE);

        // set data
        textViewToolBar.setText(String.format(getString(R.string.uid_str), appInitData.getUid()));

        if (!preferences.contains("userId")) {
            preferences.edit().putString("userId", String.valueOf(appInitData.getUid())).apply();
        }

        userId = String.valueOf(appInitData.getUid());
        Session.getInstance().setUserId(Integer.valueOf(userId));

        textViewBalance.setText(String.valueOf(appInitData.getBalance()));
        textViewPlanName.setText(appInitData.getPlanName());
        planNameStr = appInitData.getPlanName();
        textViewBalanceCurrent.setText(String.valueOf(appInitData.getCurrentBalance()));
        currentBalance = appInitData.getCurrentBalance();


        Calendar currentDate = Calendar.getInstance();
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
        int currentMonth = currentDate.get(Calendar.MONTH);
        int currentYear = currentDate.get(Calendar.YEAR);

        int payDay = Integer.parseInt(appInitData.getPayDay());

        // Проверка условия для определения следующего месяца и года
        if (payDay < currentDay) {
            currentMonth++;  // Увеличиваем месяц
            if (currentMonth > 11) {  // Проверяем, является ли текущий месяц последним в году
                currentMonth = 0;  // Если последний месяц в году, то устанавливаем январь (0)
                currentYear++;  // Увеличиваем год
            }
        }

        // Форматирование дня платежа в виде "dd.MM.yy"
        @SuppressLint("DefaultLocale") String formattedPayDay = String.format("%02d.%02d.%02d", payDay, currentMonth + 1, currentYear % 100);

        // Установка отформатированной даты в textView
        textViewNextDate.setText(formattedPayDay);

        newsAdapter = new NewsAdapter(this);
        newsAdapter.setUserId(Integer.valueOf(userId));

        Session.getInstance().setDarkMode(darkMode);
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewNews.setAdapter(newsAdapter);
        ArrayList<ExtraEntityPreview> newsPreview = (ArrayList<ExtraEntityPreview>) appInitData.getNews();
        Collections.reverse(newsPreview);
        newsAdapter.setPreviews(newsPreview);

        articlesAdapter = new ArticlesAdapter(this);
        recyclerViewArticles.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewArticles.setAdapter(articlesAdapter);
        ArrayList<ExtraEntityPreview> articlesPreviews = (ArrayList<ExtraEntityPreview>) appInitData.getArticles();
        Collections.reverse(articlesPreviews);
        articlesAdapter.setPreviews(articlesPreviews);

        // promo carousel
        promoSliderAdapter = new PromoSliderAdapter(this, appInitData.getPromos());
        viewPagerPromo.setAdapter(promoSliderAdapter);

        setupAutoPager();
        if (needLoadElements) {
            setupToolbarMenu();
        }
        progressBar.setVisibility(View.INVISIBLE);

        swipeRefreshLayout.setEnabled(true);

        // getSharedPreferences("success_prefs", Context.MODE_PRIVATE).edit().putBoolean("newToken", true).apply();
        if (preferences.contains("newToken") && preferences.getBoolean("newToken", false)) {
            preferences.edit().putBoolean("newToken", false).apply();
            commonDataSender.setUserId(userId);
            commonDataSender.setUserPass(uPass);
            commonDataSender.sendToken(AppFirebaseMessaging.getToken(this));
        }


        // feedback
        if (!(preferences.contains("noFeedback") && preferences.getBoolean("noFeedback", true))) {
            if (preferences.contains("launches")) {
                int l = preferences.getInt("launches", 0);
                if (l % 12 == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage(R.string.feedbackTitle)
                            .setCancelable(true)
                            .setPositiveButton(R.string.leterLabel,
                                    (dialog, id) -> dialog.cancel())
                            .setNeutralButton(R.string.noLabel,
                                    (dialog, id) -> {
                                        preferences.edit().putBoolean("noFeedback", true).apply();
                                        dialog.cancel();
                                    })
                            .setNegativeButton(R.string.makeFeedbackLabel,
                                    (dialog, id) -> {
                                        makeFeedback();
                                        preferences.edit().putBoolean("noFeedback", true).apply();
                                        dialog.cancel();
                                    });
                    builder.create().show();
                }

            }

            loads++;
            preferences.edit().putInt("launches", loads).apply();
        }

    }

    private int currentPage = 0;

    private void setupAutoPager() {
        final Handler handler = new Handler();
        final Runnable update = () -> {
            viewPagerPromo.setCurrentItem(currentPage, true);
            if (currentPage == promoSliderAdapter.getCount() - 1) {
                currentPage = 0;
            } else {
                ++currentPage;
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 700, 2500);
    }

    @Override
    public void showError(String str) {
        System.out.println("ERROR: " + str);
        progressBar.setVisibility(View.INVISIBLE);
        snack.setText(str);
        snack.setDuration(Snackbar.LENGTH_SHORT);
        snack.show();
    }

    private static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setupToolbarMenu() {
        toolBar.inflateMenu(R.menu.navigation_menu);
        toolBar.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.nav_share) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Устанавливайте наше мобильное приложение: https://play.google.com/store/apps/details?id=" + getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }

//            if (item.getItemId() == R.id.nav_map) {
//
//            }

            if (item.getItemId() == R.id.nav_multi) {
                Toast.makeText(this, R.string.devOptionsLabel, Toast.LENGTH_LONG).show();
            }

            if (item.getItemId() == R.id.nav_feedback) {
                makeFeedback();
            }

            if (item.getItemId() == R.id.nav_color_mode) {
                if (preferences != null) {
                    if (preferences.getBoolean("darkMode", false)) {
                        preferences.edit().putBoolean("darkMode", false).apply();
                    } else {
                        preferences.edit().putBoolean("darkMode", true).apply();
                    }
                }
                emulate = true; // Do not need to load from network, just give us already stored data
                recreate();
            }

            if (item.getItemId() == R.id.nav_our_site) {
                String url = "http://www.comfort-tv.ru/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }

            if (item.getItemId() == R.id.nav_developer) {
//                String url = "https://webkolesnikov.com";
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(url));
//                startActivity(intent);

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "errorgrisha@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.emailFeedbackLabel));
                startActivity(Intent.createChooser(emailIntent, getString(R.string.writeToDevLabel)));

            }

            if (item.getItemId() == R.id.nav_policy) {
                Intent intent = new Intent(getApplicationContext(), SecureAndPolicyActivity.class);
                startActivity(intent);
            }

            if (item.getItemId() == R.id.nav_logout) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("ap_was_out", true);
                startActivity(intent);
            }

            return false;
        });

        if (menu != null) {
            if (darkMode) menu.findItem(R.id.nav_color_mode).setTitle(R.string.title_go_light);
            else menu.findItem(R.id.nav_color_mode).setTitle(R.string.title_go_dark);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.menu = menu;
        return true;
    }

    @Override
    protected void onDestroy() {
        if (timer != null) timer.cancel();
        super.onDestroy();
    }

    private void changeIconsColor(ArrayList<ImageView> icons, int color) {
        for (ImageView i : icons) {
            i.setColorFilter(ContextCompat.getColor(this, color), PorterDuff.Mode.SRC_IN);
        }
    }

    private void setupButtons() {
        // Adjust buttons
        View button_pay = findViewById(R.id.button_pay);
        ((TextView) button_pay.findViewById(R.id.textViewButton_0)).setText("Платежи");
        ((ImageView) button_pay.findViewById(R.id.imageViewButton_0_Icon)).setImageResource(R.drawable.pay_icon);
        if (darkMode) {
            ((ImageView) button_pay.findViewById(R.id.imageViewButton_0_Icon)).setColorFilter(ContextCompat.getColor(this, R.color.main_light), PorterDuff.Mode.SRC_IN);
            ((ImageView) button_pay.findViewById(R.id.imageViewRightArrow)).setColorFilter(ContextCompat.getColor(this, R.color.main_light), PorterDuff.Mode.SRC_IN);
        }
        button_pay.setOnClickListener(view -> {
            Intent intent = new Intent(this, PaymentsActivity.class);
            intent.putExtra("userName", uName);
            intent.putExtra("userPass", uPass);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        View button_pay_tmp = findViewById(R.id.button_pay_tmp);
        ((TextView) button_pay_tmp.findViewById(R.id.textViewButton_0)).setText("Временный платеж");
        ((ImageView) button_pay_tmp.findViewById(R.id.imageViewButton_0_Icon)).setImageResource(R.drawable.tmp_pay);
        if (darkMode) {
            ((ImageView) button_pay_tmp.findViewById(R.id.imageViewButton_0_Icon)).setColorFilter(ContextCompat.getColor(this, R.color.main_light), PorterDuff.Mode.SRC_IN);
            ((ImageView) button_pay_tmp.findViewById(R.id.imageViewRightArrow)).setColorFilter(ContextCompat.getColor(this, R.color.main_light), PorterDuff.Mode.SRC_IN);
        }
        button_pay_tmp.setOnClickListener(view -> {
            Intent intent = new Intent(this, TempPayActivity.class);
            intent.putExtra("userName", uName);
            intent.putExtra("userPass", uPass);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        View button_chat = findViewById(R.id.button_chat);
        ((TextView) button_chat.findViewById(R.id.textViewButton_0)).setText("Чат с поддержкой");
        ((ImageView) button_chat.findViewById(R.id.imageViewButton_0_Icon)).setImageResource(R.drawable.chat_icon);
        if (darkMode) {
            ((ImageView) button_chat.findViewById(R.id.imageViewButton_0_Icon)).setColorFilter(ContextCompat.getColor(this, R.color.main_light), PorterDuff.Mode.SRC_IN);
            ((ImageView) button_chat.findViewById(R.id.imageViewRightArrow)).setColorFilter(ContextCompat.getColor(this, R.color.main_light), PorterDuff.Mode.SRC_IN);
        }
        button_chat.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("userName", uName);
            intent.putExtra("userPass", uPass);
            intent.putExtra("userId", userId);
            intent.putExtra("darkMode", darkMode);
            startActivity(intent);
        });

        View button_issue = findViewById(R.id.button_issue);
        ((TextView) button_issue.findViewById(R.id.textViewButton_0)).setText("Решить проблему");
        ((ImageView) button_issue.findViewById(R.id.imageViewButton_0_Icon)).setImageResource(R.drawable.issue_icon);
        if (darkMode) {
            ((ImageView) button_issue.findViewById(R.id.imageViewButton_0_Icon)).setColorFilter(ContextCompat.getColor(this, R.color.main_light), PorterDuff.Mode.SRC_IN);
            ((ImageView) button_issue.findViewById(R.id.imageViewRightArrow)).setColorFilter(ContextCompat.getColor(this, R.color.main_light), PorterDuff.Mode.SRC_IN);
        }
        button_issue.setOnClickListener(view -> {
            Intent intent = new Intent(this, IssueActivity.class);
            intent.putExtra("userName", uName);
            intent.putExtra("userPass", uPass);
            intent.putExtra("userId", userId);
            intent.putExtra("darkMode", darkMode);
            startActivity(intent);
        });


//        View button_points = findViewById(R.id.button_pay_points);
//        ((TextView) button_points.findViewById(R.id.textViewButton_0)).setText("Точки оплаты");
//        ((ImageView) button_points.findViewById(R.id.imageViewButton_0_Icon)).setImageResource(R.drawable.location_icon);
//        if (darkMode) {
//            ((ImageView) button_points.findViewById(R.id.imageViewButton_0_Icon)).setColorFilter(ContextCompat.getColor(this, R.color.main_light), PorterDuff.Mode.SRC_IN);
//            ((ImageView) button_points.findViewById(R.id.imageViewRightArrow)).setColorFilter(ContextCompat.getColor(this, R.color.main_light), PorterDuff.Mode.SRC_IN);
//        }
//        button_points.setOnClickListener(view -> {
//
//        });


        View button_call = findViewById(R.id.button_call);
        ((TextView) button_call.findViewById(R.id.textViewButton_0)).setText("Позвонить нам");
        ((ImageView) button_call.findViewById(R.id.imageViewButton_0_Icon)).setImageResource(R.drawable.call_svg);
        if (darkMode) {
            ((ImageView) button_call.findViewById(R.id.imageViewButton_0_Icon)).setColorFilter(ContextCompat.getColor(this, R.color.main_light), PorterDuff.Mode.SRC_IN);
            ((ImageView) button_call.findViewById(R.id.imageViewRightArrow)).setColorFilter(ContextCompat.getColor(this, R.color.main_light), PorterDuff.Mode.SRC_IN);
        }
        button_call.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+78137898098"));
            startActivity(intent);
        });
    }

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void makeFeedback() {
        String appPackageName = getPackageName();
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
        marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(marketIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null)
            timer.cancel();
    }
}
















