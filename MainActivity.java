package com.nursiam.amarbanglaschool;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NetworkChangeReceiver.NetworkChangeListener  {

    private AppUpdateManager appUpdateManager;
    private final int REQUEST_CODE = 1;
    private AlertDialog dialog;

    DrawerLayout drawerlayout;
    MaterialToolbar materialtoolbar;
    ChipNavigationBar bottom_nav;
    NavigationView navigationview;
    TextToSpeechHelper_Bangla textToSpeechHelper;
    FragmentManager fragmentManager;
    NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        GDPRmassage.requestConsentForm(MainActivity.this);



        // Initialize TTS
        textToSpeechHelper = new TextToSpeechHelper_Bangla(this);

        // Find views
        drawerlayout = findViewById(R.id.drawerlayout);
        materialtoolbar = findViewById(R.id.materialtoolbar);
        navigationview = findViewById(R.id.navigationview);
        bottom_nav = findViewById(R.id.bottom_nav);


        materialtoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.shear) {
                    final String appPakageName = getPackageName();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                    intent.putExtra(Intent.EXTRA_TEXT, "Download Now : https://play.google.com/store/apps/details?id=" + appPakageName);
                    startActivity(Intent.createChooser(intent, "Share Via"));

                }


                return false;
            }
        });


        // Drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        MainActivity.this, drawerlayout, materialtoolbar, R.string.drawer_close, R.string.drawer_open);
        drawerlayout.addDrawerListener(toggle);


        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                if (menuItem.getItemId() == R.id.home) {

                    Custom_Tost.show(MainActivity.this,"This is Home");

                    loadHomeFragment(savedInstanceState);
                    drawerlayout.closeDrawer(GravityCompat.START);
                }


                else if (menuItem.getItemId() == R.id.review) {
                    final String appPackageName = getPackageName();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (Exception e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }

                    drawerlayout.closeDrawer(GravityCompat.START);
                }


                else if (menuItem.getItemId() == R.id.deviloper) {
                    // ডায়ালগ বিল্ডার তৈরি
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

// কাস্টম লেআউট
                    LinearLayout layout = new LinearLayout(MainActivity.this);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setPadding(50, 40, 50, 40);

// ইমেজ
                    // ইমেজ ভিউ তৈরি করা
                    ImageView imageView = new ImageView(MainActivity.this);
                    imageView.setImageResource(R.drawable.devloper_image); // আপনার ইমেজ রিসোর্স এখানে ব্যবহার করুন

                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            250 // ফিক্সড হাইট
                    );
                    imageParams.setMargins(0, 0, 0, 20); // নিচে মার্জিন
                    imageView.setLayoutParams(imageParams);

// লেআউটে যোগ করা
                    layout.addView(imageView);


// টাইটেল
                    TextView titleText = new TextView(MainActivity.this);
                    titleText.setText("SN NUR HOSSAIN");
                    titleText.setTextSize(24);
                    titleText.setGravity(Gravity.CENTER);
                    titleText.setTypeface(null, Typeface.BOLD);
                    titleText.setTextColor(Color.parseColor("#FF5722"));
                    layout.addView(titleText);

// মেসেজ
                    TextView messageText = new TextView(MainActivity.this);
                    messageText.setText("Android apps developer.\nExperienced Java, PHP, MySQL, JSON");
                    messageText.setTextSize(16);
                    messageText.setGravity(Gravity.CENTER);
                    messageText.setTextColor(Color.parseColor("#4CAF50"));
                    layout.addView(messageText);

// লেআউট সেট করা
                    builder.setView(layout);

// OK বাটন
                    builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

// ডায়ালগ দেখানো
                    builder.create().show();

// Drawer বন্ধ করা
                    drawerlayout.closeDrawer(GravityCompat.START);

                }

                else if (menuItem.getItemId() == R.id.privicypolicy) {

                    drawerlayout.closeDrawer(GravityCompat.START);

                    // প্রাইভেসি পলিসি লিংক
                    String url = "https://sites.google.com/view/amar-bangla-school/home-page";
                    showParentalGate(url);
                }



                else if (menuItem.getItemId() == R.id.support_team) {
                    drawerlayout.closeDrawer(GravityCompat.START);

                    String phoneNumber = "+393293897859";
                    String message = "Hello, I need some support for Amar Bangla School App.";

                    try {
                        String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + URLEncoder.encode(message, "UTF-8");
                        showParentalGate(url);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                else if (menuItem.getItemId() == R.id.share_app){
                    final String appPakageName = getPackageName();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                    intent.putExtra(Intent.EXTRA_TEXT, "Download Now : https://play.google.com/store/apps/details?id=" + appPakageName);
                    startActivity(Intent.createChooser(intent, "Share Via"));

                    drawerlayout.closeDrawer(GravityCompat.START);
                }

                else if(menuItem.getItemId() == R.id.website){
                    //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://snnurhossain.com")));
                    drawerlayout.closeDrawer(GravityCompat.START);
                    showParentalGate("https://snnurhossain.com");
                }

                else if (menuItem.getItemId() == R.id.fb_page) {
                    /*String url = "https://www.facebook.com/profile.php?id=61575433142821";
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);*/
                    drawerlayout.closeDrawer(GravityCompat.START);
                    showParentalGate("https://www.facebook.com/profile.php?id=61575433142821");
                }


                return true;
            }
        });



//..................................................................................................


        // Keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // App update check
        appupdete();

        // Load default fragment
        loadHomeFragment(savedInstanceState);

        // Bottom navigation click listener
        bottom_nav.setOnItemSelectedListener(id -> {
            Fragment fragment = null;

            if (id == R.id.home) {
                fragment = new Fragment_Main();
            } else if (id == R.id.another) {
                fragment = new Freagment_Another();
            }else if (id == R.id.quiz) {
                fragment = new Fragment_Quiz();
            }

            if (fragment != null) {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_frame_layout, fragment)
                        .commit();
            } else {
                Log.e(TAG, "Something is wrong: Fragment is null");
            }
        });
    }

    private void showParentalGate(String redirectUrl) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Parent Verification");

        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        TextView questionText = new TextView(MainActivity.this);
        questionText.setTextSize(18);
        questionText.setTextColor(Color.BLACK);

        EditText answerInput = new EditText(MainActivity.this);
        answerInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        answerInput.setHint("Enter Answer");

        layout.addView(questionText);
        layout.addView(answerInput);

        builder.setView(layout);

        // Random math equation
        Random random = new Random();
        int num1 = random.nextInt(10) + 5;   // 5–14
        int num2 = random.nextInt(10) + 5;
        int correctAnswer = num1 + num2;

        questionText.setText("What is " + num1 + " + " + num2 + " ?");

        builder.setPositiveButton("Verify", null);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {

            String userAnswer = answerInput.getText().toString().trim();

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {

                if (!userAnswer.isEmpty() && Integer.parseInt(userAnswer) == correctAnswer) {

                    dialog.dismiss();

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl));
                    startActivity(intent);

                } else {
                    Custom_Tost.show(MainActivity.this,"Verification Failed!");
                }
            });
        });
    }


    private void loadHomeFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            bottom_nav.setItemSelected(R.id.home, true);
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame_layout, new Fragment_Main())
                    .commit();
        }
    }

    private void appupdete() {
        appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.IMMEDIATE, this, REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public void onNetworkChanged(boolean isConnected) {
        if (isConnected) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        } else {
            if (dialog == null || !dialog.isShowing()) {
                ShowDialog();
            }
        }
    }

    private void ShowDialog() {
        dialog = new AlertDialog.Builder(MainActivity.this)
                .setView(R.layout.no_internet_dialog)
                .setCancelable(false)
                .create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView playButton = dialog.findViewById(R.id.playButton);
        playButton.setOnClickListener(view -> finishAffinity());
    }





    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

        // ১. নতুন কাস্টম Dialog তৈরি করুন
        final Dialog exitDialog = new Dialog(MainActivity.this);

        // ২. উইন্ডো কনফিগারেশন: requestFeature() অবশ্যই setContentView() এর আগে থাকতে হবে
        if (exitDialog.getWindow() != null) {
            // শিরোনাম সরিয়ে দিন
            exitDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            // ব্যাকগ্রাউন্ড স্বচ্ছ করুন
            exitDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }


        exitDialog.setContentView(R.layout.custom_exit_design);


        Button btnYes = exitDialog.findViewById(R.id.btn_yes);
        Button btnNo = exitDialog.findViewById(R.id.btn_no);


        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
                MainActivity.super.onBackPressed();
                finish();
            }
        });


        exitDialog.show();
    }



}
