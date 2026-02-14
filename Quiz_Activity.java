package com.nursiam.amarbanglaschool;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Quiz_Activity extends AppCompatActivity {

    TextView italianText, timerText,option1, option2, option3, option4,
            correttaAns, sbagliataAns, senzaRispostaAns,countNumber, countTotal;

    ImageView imageView, quiz_sound;
    List<Question> questionList = new ArrayList<>();
    int currentQuestion = 0;
    int score = 0;
    int wrong = 0;
    int noAnswer = 0;
    String selectedLevel = "";
    CountDownTimer countDownTimer;
    MediaPlayer mediaPlayer,failSoundPlayer,winSoundPlayer;
    boolean isPlaying = true;
    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    private static final String TAG = "fullScreen";
    AlertDialog dialog;

    LinearLayout adContainerView;
    // TTS Helper এর ইনস্ট্যান্স যোগ করা হয়েছে
    private TextToSpeechHelper_Bangla ttsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        adContainerView = findViewById(R.id.adContainerView);
        // --- Load Ad ---
        loadBanner();



        GDPRmassage.requestConsentForm(Quiz_Activity.this);



      // this is for app not screenshort----------------------------------
      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#A7BFA7"));
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // 🔹 Intent
        selectedLevel = getIntent().getStringExtra("level");
        if (selectedLevel == null) selectedLevel = "বাংলা কুইজ";

        // 🔹 Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrows);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(selectedLevel);
        }
        toolbar.setNavigationOnClickListener(v -> finish());



        imageView = findViewById(R.id.imageView);
        quiz_sound = findViewById(R.id.quiz_sound);
        italianText = findViewById(R.id.italianText);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        timerText = findViewById(R.id.timerText);

        option1.setClickable(true);
        option1.setFocusable(true);
        option2.setClickable(true);
        option2.setFocusable(true);
        option3.setClickable(true);
        option3.setFocusable(true);
        option4.setClickable(true);
        option4.setFocusable(true);

        countNumber = findViewById(R.id.count_number);
        countTotal = findViewById(R.id.count_total);

        sharedPreferences = getSharedPreferences("QuizPrefs", MODE_PRIVATE);
        isPlaying = sharedPreferences.getBoolean("isPlaying", true);

        mediaPlayer = MediaPlayer.create(this, R.raw.quiz_song);
        mediaPlayer.setLooping(true);
        if (isPlaying) {
            mediaPlayer.start();
            quiz_sound.setImageResource(R.drawable.sound);
        } else {
            quiz_sound.setImageResource(R.drawable.sound_of);
        }

        quiz_sound.setOnClickListener(v -> toggleSound());

        // TTS Helper ইনিশিয়ালাইজ করা হয়েছে
        ttsHelper = new TextToSpeechHelper_Bangla(this);

        // sounds CardView এর জন্য অনক্লিক লিসেনার যুক্ত করা হয়েছে
        CardView soundsCardView = findViewById(R.id.sounds);
        if (soundsCardView != null) {
            soundsCardView.setOnClickListener(v -> {
                if (currentQuestion < questionList.size()) {
                    String questionText = questionList.get(currentQuestion).getQuestion();
                    ttsHelper.speak(questionText);
                }
            });
        }

        if (getIntent().getStringExtra("level") != null) {
            selectedLevel = getIntent().getStringExtra("level");
        }

        loadQuestions();
        showQuestion();

        View.OnClickListener listener = view -> {
            TextView clicked = (TextView) view;
            checkAnswer(clicked.getText().toString());
        };

        option1.setOnClickListener(listener);
        option2.setOnClickListener(listener);
        option3.setOnClickListener(listener);
        option4.setOnClickListener(listener);



    }



    private void toggleSound() {
        isPlaying = !isPlaying;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isPlaying", isPlaying);
        editor.apply();

        if (isPlaying) {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.quiz_song);
                mediaPlayer.setLooping(true);
            }
            mediaPlayer.start();
            quiz_sound.setImageResource(R.drawable.sound);
        } else {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
            quiz_sound.setImageResource(R.drawable.sound_of);
        }
    }



    private void showQuestion() {
        if (currentQuestion < questionList.size()) {
            countNumber.setText(String.valueOf(currentQuestion + 1));

            Question model = questionList.get(currentQuestion);
            italianText.setText(model.getQuestion());

            List<String> options = new ArrayList<>();
            options.add(model.getOption1());
            options.add(model.getOption2());
            options.add(model.getOption3());
            options.add(model.getOption4());
            Collections.shuffle(options);

            option1.setText(options.get(0));
            option2.setText(options.get(1));
            option3.setText(options.get(2));
            option4.setText(options.get(3));

            imageView.setImageResource(model.getImageResId());

            startTimer();
        } else {
            showResult();
        }
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerText.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                noAnswer++;
                currentQuestion++;
                showQuestion();
            }
        }.start();
    }

    private void checkAnswer(String selected) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        if (currentQuestion < questionList.size()) {
            String correct = questionList.get(currentQuestion).getAnswer();
            if (selected.equals(correct)) {
                score++;
            } else {
                wrong++;
            }
            currentQuestion++;
            showQuestion();
        }
    }

    private void showResult() {
        if (isFinishing() || isDestroyed()) {
            return;
        }

        stopAllSounds();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_result_design, null);
        builder.setView(view);

        LottieAnimationView lottieAnimationView = view.findViewById(R.id.lotty_animation);
        TextView scoreText = view.findViewById(R.id.schor);
        correttaAns = view.findViewById(R.id.corretta_ans);
        sbagliataAns = view.findViewById(R.id.sbagliata_ans);
        senzaRispostaAns = view.findViewById(R.id.senza_risposta_ans);

        LinearLayout home = view.findViewById(R.id.home);
        LinearLayout playAgain = view.findViewById(R.id.play_again);

        if (score >= 16) {
            lottieAnimationView.setAnimation(R.raw.win);
            if (isPlaying) {
                winSoundPlayer = MediaPlayer.create(this, R.raw.quiz_win_sound);
                winSoundPlayer.start();
            }
        } else {
            lottieAnimationView.setAnimation(R.raw.fail);
            if (isPlaying) {
                failSoundPlayer = MediaPlayer.create(this, R.raw.quiz_win_sound);
                failSoundPlayer.start();
            }
        }

        lottieAnimationView.playAnimation();
        scoreText.setText(String.valueOf(score));
        correttaAns.setText(String.valueOf(score));
        sbagliataAns.setText(String.valueOf(wrong));
        senzaRispostaAns.setText(String.valueOf(noAnswer));

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.show();

        home.setOnClickListener(v -> {

/*
            if ( interstitialAd != null && interstitialAd.isAdLoaded()){
                Quiz_Activity.clickCount++;
            } if (interstitialAd != null && interstitialAd.isAdLoaded() && MainActivity.clickCount == 1) {
                dialog.dismiss();
                interstitialAd.show();
                Quiz_Activity.clickCount = 0;
            }else {

                dialog.dismiss();
                finish();
            }
*/

            dialog.dismiss();
            finish();

        });

        playAgain.setOnClickListener(v -> {
            dialog.dismiss();
            restartQuiz();
        });
    }


    private void stopAllSounds() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        if (winSoundPlayer != null && winSoundPlayer.isPlaying()) {
            winSoundPlayer.stop();
        }
        if (failSoundPlayer != null && failSoundPlayer.isPlaying()) {
            failSoundPlayer.stop();
        }
    }

    private void restartQuiz() {
        stopAllSounds();

        if (isPlaying) {
            mediaPlayer = MediaPlayer.create(this, R.raw.quiz_song);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
        quiz_sound.setImageResource(isPlaying ? R.drawable.sound : R.drawable.sound_of);

        currentQuestion = 0;
        score = 0;
        wrong = 0;
        noAnswer = 0;
        questionList.clear();
        loadQuestions();
        showQuestion();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null && !mediaPlayer.isPlaying() && isPlaying) {
            mediaPlayer.start();
        }
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAllSounds();

        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        if (winSoundPlayer != null) {
            winSoundPlayer.release();
        }
        if (failSoundPlayer != null) {
            failSoundPlayer.release();
        }
        if (ttsHelper != null) {
            ttsHelper.shutdown();
        }

    }



    private void loadBanner() {
        if (adContainerView == null) return;

        AdView adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.Banner_AD_ID));

        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        // Adaptive Banner Size (পর্দার মাপ অনুযায়ী)
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        float density = displayMetrics.density;
        int adWidthPixels = adContainerView.getWidth();

        if (adWidthPixels == 0) {
            adWidthPixels = displayMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        AdSize adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
        adView.setAdSize(adSize);

        // --- AdListener এখানে যোগ করা হয়েছে ---
        adView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                // অ্যাড সফলভাবে লোড হলে কন্টেইনারটি দৃশ্যমান (Visible) হবে
                adContainerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                // অ্যাড লোড হতে ব্যর্থ হলে কন্টেইনারটি অদৃশ্য (Gone) থাকবে
                adContainerView.setVisibility(View.GONE);
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        // অ্যাড লোড করার রিকোয়েস্ট পাঠানো
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }



    private void loadQuestions() {
        List<Question> tempList = new ArrayList<>();
        switch (selectedLevel) {
            case "বাংলা কুইজ":

                tempList.add(new Question("এই ফল টির নাম কি?", "আম", "কাঠাল", "কলা", "আপেল", "আম", R.drawable.mangos));
                tempList.add(new Question("এই ফল টির নাম কি?", "আম", "কাঠাল", "কলা", "আপেল", "কাঠাল", R.drawable.jackfruit));
                tempList.add(new Question("এই ফল টির নাম কি?", "আম", "কাঠাল", "কলা", "আপেল", "কলা", R.drawable.banana));
                tempList.add(new Question("এই ফল টির নাম কি?", "আম", "কাঠাল", "কলা", "আপেল", "আপেল", R.drawable.mela));
                tempList.add(new Question("এই ফল টির নাম কি?", "আনারস", "লিচু", "পেয়ারা", "ডালিম", "পেয়ারা", R.drawable.guava));
                tempList.add(new Question("এই ফল টির নাম কি?", "আনারস", "পেয়ারা", "জাম", "ডালিম", "জাম", R.drawable.jam));
                tempList.add(new Question("এই ফল টির নাম কি?", "আনারস", "ডালিম", "তরমুজ", "পেয়ারা", "তরমুজ", R.drawable.cocomero));
                tempList.add(new Question("এই ফল টির নাম কি?", "আনারস", "ডালিম", "পেয়ারা", "জাম", "ডালিম", R.drawable.pomegranate));
                tempList.add(new Question("এই ফল টির নাম কি?", "আনারস", "পেয়ারা", "বড়ই", "স্ট্রবেরি", "বড়ই", R.drawable.boroi));
                tempList.add(new Question("এই ফল টির নাম কি?", "আনারস", "পেয়ারা", "বড়ই", "স্ট্রবেরি", "স্ট্রবেরি", R.drawable.fragole));
                tempList.add(new Question("নিচের কোনটি স্বরবর্ণ?", "ক", "খ", "অ", "গ", "অ", R.drawable.sorborno));
                tempList.add(new Question("নিচের কোনটি ব্যঞ্জনবর্ণ?", "ই", "উ", "চ", "ঈ", "চ", R.drawable.sorborno));
                tempList.add(new Question("নিচের কোনটি স্বরবর্ণ?", "ঈ", "ক", "ঘ", "চ", "ঈ", R.drawable.sorborno));
                tempList.add(new Question("নিচের কোনটি ব্যঞ্জনবর্ণ?", "ঊ", "ঘ", "অ", "ঐ", "ঘ", R.drawable.sorborno));
                tempList.add(new Question("নিচের কোনটি স্বরবর্ণ?", "উ", "ক", "খ", "গ", "উ", R.drawable.sorborno));
                tempList.add(new Question("নিচের কোনটি ব্যঞ্জনবর্ণ?", "ঈ", "জ", "অ", "ঋ", "জ", R.drawable.sorborno));
                tempList.add(new Question("‘ঋ’ কোন শ্রেণীর বর্ণ?", "স্বরবর্ণ", "ব্যঞ্জনবর্ণ", "যুক্তবর্ণ", "অর্ধবর্ণ", "স্বরবর্ণ", R.drawable.sorborno));
                // ক
                tempList.add(new Question("‘ক’ দিয়ে নিচের কোনটি সঠিক শব্দ?", "কলম", "খাতা", "গাড়ি", "ঘড়ি", "কলম", R.drawable.kolom));
// খ
                tempList.add(new Question("‘খরগোশ’ কোন বর্ণ দিয়ে শুরু হয়?", "ক", "খ", "গ", "ঘ", "খ", R.drawable.khorgos));
// গ
                tempList.add(new Question("গরুর দুধ খেতে কেমন?", "তিতা", "ঝাল", "মজা", "টক", "মজা", R.drawable.gotu));
// ঘ
                tempList.add(new Question("সময় দেখতে আমরা কী ব্যবহার করি?", "বই", "ঘড়ি", "কলম", "ছাতা", "ঘড়ি", R.drawable.gori));
// চ
                tempList.add(new Question("দাদুর চোখে কী লাগে?", "চশমা", "টুপি", "ঘড়ি", "মালা", "চশমা", R.drawable.choshma));
// ছ
                tempList.add(new Question("বৃষ্টি হলে কী লাগে?", "বই", "ফুল", "ছাতা", "ডিম", "ছাতা", R.drawable.sata));
// জ
                tempList.add(new Question("সাগর জলে কী ভাসে?", "গাড়ি", "জাহাজ", "রিকশা", "ঠেলাগাড়ি", "জাহাজ", R.drawable.jahas));
// ট
                tempList.add(new Question("কোন পাখির ঠোঁটটি লাল?", "কাউয়া", "চড়ুই", "টিয়া", "কবুতর", "টিয়া", R.drawable.tiya_pakhi));
// ড
                tempList.add(new Question("নিচের কোনটিতে অনেক পুষ্টি আছে?", "রঙ", "ডিম", "লাঠি", "পাথর", "ডিম", R.drawable.dim));
// ত
                tempList.add(new Question("মজা করে কোন ফলটি খাওয়া যায়?", "লবণ", "তরমুজ", "মরিচ", "তেঁতুল", "তরমুজ", R.drawable.tormus));
// দ
                tempList.add(new Question("আমাদের জাতীয় পাখির নাম কী?", "টিয়া", "ময়ূর", "দোয়েল", "শালিক", "দোয়েল", R.drawable.doeal));
// ন
                tempList.add(new Question("নৌকা কে চালায়?", "নয়ন মাঝি", "রহিম ভাই", "জেলে", "কৃষক", "নয়ন মাঝি", R.drawable.nouka));
// প
                tempList.add(new Question("পাখি কোথায় গান গায়?", "জলে", "গাছে গাছে", "আকাশে", "ঘরে", "গাছে গাছে", R.drawable.pakhi));
// ফ
                tempList.add(new Question("ফুলে কী থাকে?", "জল", "মধু", "বিষ", "বালি", "মধু", R.drawable.fule));
// ব
                tempList.add(new Question("কী পড়লে জ্ঞান বাড়ে?", "বই", "খাতা", "কাগজ", "দেয়াল", "বই", R.drawable.boe));
// ম
                tempList.add(new Question("পেখম মেলে কে নাচে?", "হাতি", "ময়ুর", "সিংহ", "হরিণ", "ময়ুর", R.drawable.moure));
// শ
                tempList.add(new Question("শাপলা কোথায় ফোটে?", "গাছে", "বিলের জলে", "পাহাড়ে", "মরুভূমিতে", "বিলের জলে", R.drawable.saplas));
// স
                tempList.add(new Question("বনের রাজা কে?", "ভাল্লুক", "বাঘ", "সিংহ", "হাতি", "সিংহ", R.drawable.singho));
// হ
                tempList.add(new Question("কার পিঠে চড়তে মজা?", "হাতি", "পিঁপড়া", "কুকুর", "বিড়াল", "হাতি", R.drawable.haties));
// ঁ
                tempList.add(new Question("আকাশ পানে কী উঠেছে?", "তারা", "চাঁদ", "মেঘ", "সূর্য", "চাঁদ", R.drawable.chad));

                // প্রাণী চিনো 🐘
                tempList.add(new Question("এই প্রাণীটির নাম কি?", "হাতি", "ঘোড়া", "বাঘ", "বানর", "হাতি", R.drawable.hati));
                tempList.add(new Question("এই প্রাণীটির নাম কি?", "বিড়াল", "কুকুর", "গরু", "ছাগল", "বিড়াল", R.drawable.biral));

                // ফুল চিনো 🌸
                tempList.add(new Question("এই ফুলটির নাম কি?", "জবা", "গোলাপ", "সূর্যমুখী", "টিউলিপ", "গোলাপ", R.drawable.rose));
                tempList.add(new Question("এই ফুলটির নাম কি?", "চন্দ্রমল্লিকা", "সূর্যমুখী", "লিলি", "গোলাপ", "সূর্যমুখী", R.drawable.surjo_mukhi));

                // সাধারণ জ্ঞান 🧠
                tempList.add(new Question("বাংলা বর্ণমালার প্রথম অক্ষর কোনটি?", "অ", "আ", "ই", "ঈ", "অ", R.drawable.sorborno));
                tempList.add(new Question("মানুষ কয় পায়ে হাঁটে?", "দুই", "চার", "তিন", "এক", "দুই", R.drawable.matha));
                break;





            //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            case "ইংরেজি কুইজ":
                // 🐻 Animals
                tempList.add(new Question("What animal is this?", "Cat", "Dog", "Cow", "Goat", "Cat", R.drawable.biral));
                tempList.add(new Question("What animal is this?", "Lion", "Tiger", "Elephant", "Horse", "Elephant", R.drawable.hati));
                tempList.add(new Question("Which animal gives us milk?", "Dog", "Cat", "Cow", "Lion", "Cow", R.drawable.shar));
                tempList.add(new Question("Which animal is called the King of the Jungle?", "Tiger", "Lion", "Bear", "Fox", "Tiger", R.drawable.bag));
                tempList.add(new Question("Which animal lives in water?", "Cat", "Dog", "Fish", "Elephant", "Fish", R.drawable.fish));
                tempList.add(new Question("Which animal barks?", "Cat", "Cow", "Dog", "Horse", "Dog", R.drawable.kukur));

// 🍎 Fruits
                tempList.add(new Question("What fruit is this?", "Apple", "Banana", "Mango", "Orange", "Apple", R.drawable.mela));
                tempList.add(new Question("What fruit is yellow?", "Banana", "Apple", "Grapes", "Guava", "Banana", R.drawable.banana));
                tempList.add(new Question("Which fruit is green outside and red inside?", "Watermelon", "Mango", "Orange", "Apple", "Watermelon", R.drawable.cocomero));
                tempList.add(new Question("Which fruit has spikes?", "Mango", "Pineapple", "Banana", "Grapes", "Pineapple", R.drawable.ananas));
                tempList.add(new Question("Which fruit is small and red?", "Lemon", "Apple", "Strawberry", "Cherry", "Strawberry", R.drawable.fragole));
                tempList.add(new Question("Which fruit grows on trees?", "Fish", "Apple", "Carrot", "Potato", "Apple", R.drawable.mela));
                tempList.add(new Question("Which fruit is sour?", "Lemon", "Banana", "Mango", "Apple", "Lemon", R.drawable.lemone));
                tempList.add(new Question("Which fruit monkeys like most?", "Apple", "Banana", "Mango", "Orange", "Banana", R.drawable.banana));
                tempList.add(new Question("Which fruit has seeds outside?", "Apple", "Grapes", "Strawberry", "Mango", "Strawberry", R.drawable.fragole));
                tempList.add(new Question("Which fruit is orange in color?", "Apple", "Banana", "Orange", "Mango", "Orange", R.drawable.arancio));

// 🌸 Flowers
                tempList.add(new Question("What flower is this?", "Rose", "Lily", "Sunflower", "Jasmine", "Rose", R.drawable.rose));
                tempList.add(new Question("Which flower follows the sun?", "Lily", "Tulip", "Sunflower", "Rose", "Sunflower", R.drawable.surjo_mukhi));
                tempList.add(new Question("Which flower is red?", "Lily", "Rose", "Lotus", "Tulip", "Rose", R.drawable.rose));
                tempList.add(new Question("Which flower smells sweet?", "Cactus", "Rose", "Grass", "Leaf", "Rose", R.drawable.rose));
                tempList.add(new Question("What color is a sunflower?", "Red", "Blue", "Yellow", "Green", "Yellow", R.drawable.surjo_mukhi));
                tempList.add(new Question("Which flower has many petals?", "Rose", "Tulip", "Sunflower", "Lily", "Rose", R.drawable.rose));

// 🎨 Colors
                tempList.add(new Question("What color is the sky?", "Blue", "Green", "Red", "Yellow", "Blue", R.drawable.blu));
                tempList.add(new Question("What color is the sun?", "Red", "Yellow", "Blue", "Green", "Yellow", R.drawable.giallo));
                tempList.add(new Question("What color is grass?", "Blue", "Green", "Red", "Pink", "Green", R.drawable.verde));
                tempList.add(new Question("What color are apples?", "Red", "Blue", "Black", "White", "Red", R.drawable.rosso));
                tempList.add(new Question("What color are bananas?", "Green", "Yellow", "Red", "Blue", "Yellow", R.drawable.giallo));
                tempList.add(new Question("What color is milk?", "White", "Black", "Brown", "Pink", "White", R.drawable.bianco));
                tempList.add(new Question("What color are grapes?", "Purple", "Yellow", "Red", "Blue", "Purple", R.drawable.viola));
                tempList.add(new Question("What color is a lemon?", "Blue", "Yellow", "Red", "Green", "Green", R.drawable.verde));
                tempList.add(new Question("What color is an orange?", "Red", "Green", "Orange", "Blue", "Orange", R.drawable.arancione));
                tempList.add(new Question("What color are leaves?", "Yellow", "Green", "Red", "Pink", "Green", R.drawable.verde));

// 🔢 Numbers & Shapes
                tempList.add(new Question("How many sides does a triangle have?", "2", "3", "4", "5", "3", R.drawable.math_icon));
                tempList.add(new Question("How many sides does a square have?", "2", "3", "4", "5", "4", R.drawable.math_icon));
                tempList.add(new Question("How many days are there in a week?", "5", "6", "7", "8", "7", R.drawable.math_icon));
                tempList.add(new Question("How many months are in a year?", "10", "11", "12", "13", "12", R.drawable.math_icon));
                tempList.add(new Question("How many letters are there in English?", "24", "25", "26", "27", "26", R.drawable.math_icon));
                tempList.add(new Question("What number comes after 9?", "8", "9", "10", "11", "10", R.drawable.math_icon));
                tempList.add(new Question("What number comes before 5?", "3", "4", "5", "6", "4", R.drawable.math_icon));
                tempList.add(new Question("What number is bigger: 7 or 4?", "4", "7", "Both", "None", "7", R.drawable.math_icon));


                // A
                tempList.add(new Question("Which word starts with the letter ‘A’?", "Apple", "Ball", "Cat", "Dog", "Apple", R.drawable.apple));

// B
                tempList.add(new Question("‘Ball’ starts with which letter?", "A", "B", "C", "D", "B", R.drawable.ball));

// E
                tempList.add(new Question("Identify the animal starting with ‘E’:", "Fox", "Lion", "Elephant", "Tiger", "Elephant", R.drawable.haties));

// H
                tempList.add(new Question("What is ‘H’ for in your list?", "Horse", "House", "Hand", "Hen", "Horse", R.drawable.hors));

// L
                tempList.add(new Question("Which animal is known as the King of Jungle (starts with ‘L’)?", "Lion", "Zebra", "Tiger", "Dog", "Lion", R.drawable.singho));

// M
                tempList.add(new Question("What do we see in the sky at night (starts with ‘M’)?", "Sun", "Moon", "Star", "Cloud", "Moon", R.drawable.chad));

// P
                tempList.add(new Question("Which bird starts with the letter ‘P’?", "Parrot", "Crow", "Duck", "Eagle", "Parrot", R.drawable.tiya_pakhi));

// R
                tempList.add(new Question("‘Rabit’ starts with which letter?", "P", "Q", "R", "S", "R", R.drawable.khorgos));

// S
                tempList.add(new Question("What gives us light during the day (starts with ‘S’)?", "Moon", "Sun", "Lamp", "Bulb", "Sun", R.drawable.sun));

// U
                tempList.add(new Question("What do we use in the rain (starts with ‘U’)?", "Umbrella", "Watch", "Van", "Kite", "Umbrella", R.drawable.sata));

// W
                tempList.add(new Question("What do we use to see the time (starts with ‘W’)?", "Gun", "Watch", "Jug", "Nest", "Watch", R.drawable.gori));

// Z
                tempList.add(new Question("Which animal has black and white stripes?", "Tiger", "Lion", "Zebra", "Fox", "Zebra", R.drawable.zebra));

                break;

            //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            case "গনিত কুইজ":
                // ১ থেকে ১০ পর্যন্ত যোগ
                tempList.add(new Question("১ + ১ = ?", "১", "২", "৩", "৪", "২", R.drawable.math_icon));
                tempList.add(new Question("২ + ১ = ?", "২", "৩", "৪", "৫", "৩", R.drawable.math_icon));
                tempList.add(new Question("২ + ২ = ?", "২", "৩", "৪", "৫", "৪", R.drawable.math_icon));
                tempList.add(new Question("৩ + ২ = ?", "৪", "৫", "৬", "৭", "৫", R.drawable.math_icon));
                tempList.add(new Question("৪ + ২ = ?", "৫", "৬", "৭", "৮", "৬", R.drawable.math_icon));
                tempList.add(new Question("৫ + ২ = ?", "৬", "৭", "৮", "৯", "৭", R.drawable.math_icon));
                tempList.add(new Question("৬ + ৩ = ?", "৭", "৮", "৯", "১০", "৯", R.drawable.math_icon));
                tempList.add(new Question("৭ + ২ = ?", "৮", "৯", "১০", "১১", "৯", R.drawable.math_icon));
                tempList.add(new Question("৯ + ১ = ?", "৯", "১০", "১১", "১২", "১০", R.drawable.math_icon));

// বিয়োগ
                tempList.add(new Question("৫ - ২ = ?", "১", "২", "৩", "৪", "৩", R.drawable.math_icon));
                tempList.add(new Question("৬ - ৩ = ?", "২", "৩", "৪", "৫", "৩", R.drawable.math_icon));
                tempList.add(new Question("৮ - ৫ = ?", "২", "৩", "৪", "৫", "৩", R.drawable.math_icon));
                tempList.add(new Question("৯ - ৪ = ?", "৪", "৫", "৬", "৭", "৫", R.drawable.math_icon));
                tempList.add(new Question("১০ - ৬ = ?", "৩", "৪", "৫", "৬", "৪", R.drawable.math_icon));
                tempList.add(new Question("৭ - ৩ = ?", "৩", "৪", "৫", "৬", "৪", R.drawable.math_icon));
                tempList.add(new Question("৮ - ২ = ?", "৫", "৬", "৭", "৮", "৬", R.drawable.math_icon));
                tempList.add(new Question("৬ - ১ = ?", "৪", "৫", "৬", "৭", "৫", R.drawable.math_icon));
                tempList.add(new Question("৯ - ৭ = ?", "১", "২", "৩", "৪", "২", R.drawable.math_icon));
                tempList.add(new Question("১০ - ৮ = ?", "১", "২", "৩", "৪", "২", R.drawable.math_icon));

// গুণ
                tempList.add(new Question("২ × ২ = ?", "২", "৩", "৪", "৫", "৪", R.drawable.math_icon));
                tempList.add(new Question("৩ × ২ = ?", "৪", "৫", "৬", "৭", "৬", R.drawable.math_icon));
                tempList.add(new Question("৪ × ২ = ?", "৬", "৭", "৮", "৯", "৮", R.drawable.math_icon));
                tempList.add(new Question("৫ × ২ = ?", "৮", "৯", "১০", "১১", "১০", R.drawable.math_icon));
                tempList.add(new Question("৬ × ২ = ?", "১০", "১১", "১২", "১৩", "১২", R.drawable.math_icon));
                tempList.add(new Question("৭ × ২ = ?", "১২", "১৩", "১৪", "১৫", "১৪", R.drawable.math_icon));
                tempList.add(new Question("৮ × ২ = ?", "১৪", "১৫", "১৬", "১৭", "১৬", R.drawable.math_icon));
                tempList.add(new Question("৯ × ২ = ?", "১৬", "১৭", "১৮", "১৯", "১৮", R.drawable.math_icon));
                tempList.add(new Question("১০ × ২ = ?", "১৮", "১৯", "২০", "২১", "২০", R.drawable.math_icon));
                tempList.add(new Question("৩ × ৩ = ?", "৬", "৮", "৯", "১২", "৯", R.drawable.math_icon));

// ভাগ
                tempList.add(new Question("৬ ÷ ২ = ?", "২", "৩", "৪", "৫", "৩", R.drawable.math_icon));
                tempList.add(new Question("৮ ÷ ২ = ?", "২", "৩", "৪", "৫", "৪", R.drawable.math_icon));
                tempList.add(new Question("৯ ÷ ৩ = ?", "২", "৩", "৪", "৫", "৩", R.drawable.math_icon));
                tempList.add(new Question("১২ ÷ ৩ = ?", "৩", "৪", "৫", "৬", "৪", R.drawable.math_icon));
                tempList.add(new Question("১৫ ÷ ৫ = ?", "২", "৩", "৪", "৫", "৩", R.drawable.math_icon));
                tempList.add(new Question("২০ ÷ ৫ = ?", "২", "৩", "৪", "৫", "৪", R.drawable.math_icon));
                tempList.add(new Question("১৬ ÷ ৪ = ?", "২", "৩", "৪", "৫", "৪", R.drawable.math_icon));
                tempList.add(new Question("১৮ ÷ ৬ = ?", "২", "৩", "৪", "৫", "৩", R.drawable.math_icon));
                tempList.add(new Question("২১ ÷ ৭ = ?", "২", "৩", "৪", "৫", "৩", R.drawable.math_icon));
                tempList.add(new Question("২৪ ÷ ৮ = ?", "২", "৩", "৪", "৫", "৩", R.drawable.math_icon));

// তুলনা চিহ্ন
                tempList.add(new Question("৫ ___ ৩ (বড় না ছোট?)", "বড়", "ছোট", "সমান", "জানি না", "বড়", R.drawable.math_icon));
                tempList.add(new Question("২ ___ ৪ (বড় না ছোট?)", "বড়", "ছোট", "সমান", "জানি না", "ছোট", R.drawable.math_icon));
                tempList.add(new Question("৬ ___ ৬", "বড়", "ছোট", "সমান", "জানি না", "সমান", R.drawable.math_icon));
                tempList.add(new Question("১০ ___ ৮", "বড়", "ছোট", "সমান", "জানি না", "বড়", R.drawable.math_icon));
                tempList.add(new Question("৩ ___ ৫", "বড়", "ছোট", "সমান", "জানি না", "ছোট", R.drawable.math_icon));

// সংখ্যা চিনো
                tempList.add(new Question("৫ এর পর কোন সংখ্যা আসে?", "৬", "৭", "৮", "৯", "৬", R.drawable.math_icon));
                tempList.add(new Question("৯ এর আগে কোন সংখ্যা আসে?", "৬", "৭", "৮", "১০", "৮", R.drawable.math_icon));
                tempList.add(new Question("১ এর পর কোন সংখ্যা?", "০", "২", "৩", "৪", "২", R.drawable.math_icon));
                tempList.add(new Question("১০ এর পরে কোন সংখ্যা?", "১১", "১২", "১৩", "১৪", "১১", R.drawable.math_icon));
                tempList.add(new Question("৭ এর পরে কোন সংখ্যা?", "৬", "৭", "৮", "৯", "৮", R.drawable.math_icon));

// সহজ যোগ-বিয়োগ মিশ্র
                tempList.add(new Question("৩ + ৫ = ?", "৭", "৮", "৯", "১০", "৮", R.drawable.math_icon));
                tempList.add(new Question("৯ - ৩ = ?", "৫", "৬", "৭", "৮", "৬", R.drawable.math_icon));
                tempList.add(new Question("৪ + ৪ = ?", "৬", "৭", "৮", "৯", "৮", R.drawable.math_icon));
                tempList.add(new Question("৭ - ২ = ?", "৪", "৫", "৬", "৭", "৫", R.drawable.math_icon));
                tempList.add(new Question("৮ + ২ = ?", "৯", "১০", "১১", "১২", "১০", R.drawable.math_icon));
                tempList.add(new Question("৬ + ৪ = ?", "৮", "৯", "১০", "১১", "১০", R.drawable.math_icon));
                tempList.add(new Question("১০ - ৫ = ?", "৪", "৫", "৬", "৭", "৫", R.drawable.math_icon));
                tempList.add(new Question("৯ - ১ = ?", "৭", "৮", "৯", "১০", "৮", R.drawable.math_icon));

// আরও কিছু র‍্যান্ডম
                tempList.add(new Question("২ × ৫ = ?", "৮", "৯", "১০", "১১", "১০", R.drawable.math_icon));
                tempList.add(new Question("৩ × ৪ = ?", "১০", "১১", "১২", "১৩", "১২", R.drawable.math_icon));
                tempList.add(new Question("৪ × ৫ = ?", "১৫", "১৬", "১৭", "১৮", "২০", R.drawable.math_icon));
                tempList.add(new Question("৫ × ৫ = ?", "২০", "২৫", "৩০", "৩৫", "২৫", R.drawable.math_icon));
                tempList.add(new Question("৬ × ৫ = ?", "২৮", "৩০", "৩২", "৩৪", "৩০", R.drawable.math_icon));
                tempList.add(new Question("৭ × ৩ = ?", "১৮", "১৯", "২০", "২১", "২১", R.drawable.math_icon));
                tempList.add(new Question("৮ × ৪ = ?", "৩০", "৩২", "৩৪", "৩৬", "৩২", R.drawable.math_icon));
                tempList.add(new Question("৯ × ৩ = ?", "২৪", "২৫", "২৬", "২৭", "২৭", R.drawable.math_icon));
                tempList.add(new Question("১০ × ১০ = ?", "৯০", "৯৫", "১০০", "১০৫", "১০০", R.drawable.math_icon));
                tempList.add(new Question("৭ + ২ = ?", "৮", "৯", "১০", "১১", "৯", R.drawable.math_icon));
                tempList.add(new Question("৫ - ২ = ?", "১", "২", "৩", "৪", "৩", R.drawable.math_icon));
                tempList.add(new Question("২ × ২ = ?", "২", "৩", "৪", "৫", "৪", R.drawable.math_icon));
                tempList.add(new Question("৬ ÷ ২ = ?", "২", "৩", "৪", "৫", "৩", R.drawable.math_icon));
                tempList.add(new Question("৫ এর পর কোন সংখ্যা আসে?", "৬", "৭", "৮", "৯", "৬", R.drawable.math_icon));
                tempList.add(new Question("৩ × ৪ = ?", "১০", "১১", "১২", "১৩", "১২", R.drawable.math_icon));
                tempList.add(new Question("১০ - ৩ = ?", "৬", "৭", "৮", "৯", "৭", R.drawable.math_icon));
                tempList.add(new Question("৮ + ৫ = ?", "১২", "১৩", "১৪", "১৫", "১৩", R.drawable.math_icon));
                tempList.add(new Question("৯ ÷ ৩ = ?", "২", "৩", "৪", "৫", "৩", R.drawable.math_icon));
                tempList.add(new Question("৪ × ৫ = ?", "১৫", "১৮", "২০", "২৫", "২০", R.drawable.math_icon));
                tempList.add(new Question("১২ - ৭ = ?", "৪", "৫", "৬", "৭", "৫", R.drawable.math_icon));
                tempList.add(new Question("২ + ৯ = ?", "১০", "১১", "১২", "১৩", "১১", R.drawable.math_icon));
                tempList.add(new Question("৬ × ৩ = ?", "১৬", "১৭", "১৮", "১৯", "১৮", R.drawable.math_icon));
                tempList.add(new Question("৮ ÷ ৪ = ?", "১", "২", "৩", "৪", "২", R.drawable.math_icon));
                tempList.add(new Question("৯ + ৬ = ?", "১৪", "১৫", "১৬", "১৭", "১৫", R.drawable.math_icon));
                tempList.add(new Question("১৫ - ৯ = ?", "৫", "৬", "৭", "৮", "৬", R.drawable.math_icon));
                tempList.add(new Question("৭ × ২ = ?", "১২", "১৩", "১৪", "১৫", "১৪", R.drawable.math_icon));
                tempList.add(new Question("১৬ ÷ ৪ = ?", "২", "৩", "৪", "৫", "৪", R.drawable.math_icon));
                tempList.add(new Question("১০ + ৫ = ?", "১৪", "১৫", "১৬", "১৭", "১৫", R.drawable.math_icon));
                tempList.add(new Question("১১ - ৮ = ?", "২", "৩", "৪", "৫", "৩", R.drawable.math_icon));
                tempList.add(new Question("৯ × ৩ = ?", "২৪", "২৬", "২৭", "২৯", "২৭", R.drawable.math_icon));
                tempList.add(new Question("১৮ ÷ ৩ = ?", "৫", "৬", "৭", "৮", "৬", R.drawable.math_icon));
                tempList.add(new Question("১৩ + ৭ = ?", "১৯", "২০", "২১", "২২", "২০", R.drawable.math_icon));
                tempList.add(new Question("২০ - ৯ = ?", "১০", "১১", "১২", "১৩", "১১", R.drawable.math_icon));
                tempList.add(new Question("৫ × ৬ = ?", "৩০", "৩২", "৩৪", "৩৬", "৩০", R.drawable.math_icon));
                tempList.add(new Question("২৫ ÷ ৫ = ?", "৪", "৫", "৬", "৭", "৫", R.drawable.math_icon));
                tempList.add(new Question("১৪ + ৯ = ?", "২২", "২৩", "২৪", "২৫", "২৩", R.drawable.math_icon));
                tempList.add(new Question("১৯ - ৭ = ?", "১১", "১২", "১৩", "১৪", "১২", R.drawable.math_icon));
                tempList.add(new Question("৪ × ৭ = ?", "২৫", "২৬", "২৮", "২৯", "২৮", R.drawable.math_icon));
                tempList.add(new Question("২১ ÷ ৩ = ?", "৬", "৭", "৮", "৯", "৭", R.drawable.math_icon));
                tempList.add(new Question("৮ + ৯ = ?", "১৬", "১৭", "১৮", "১৯", "১৭", R.drawable.math_icon));
                tempList.add(new Question("১৭ - ৮ = ?", "৮", "৯", "১০", "১১", "৯", R.drawable.math_icon));
                tempList.add(new Question("৩ × ৯ = ?", "২৪", "২৬", "২৭", "২৮", "২৭", R.drawable.math_icon));
                tempList.add(new Question("২৪ ÷ ৬ = ?", "৩", "৪", "৫", "৬", "৪", R.drawable.math_icon));
                tempList.add(new Question("১২ + ১১ = ?", "২১", "২২", "২৩", "২৪", "২৩", R.drawable.math_icon));
                tempList.add(new Question("২২ - ১৩ = ?", "৭", "৮", "৯", "১০", "৯", R.drawable.math_icon));
                tempList.add(new Question("৮ × ৪ = ?", "৩০", "৩১", "৩২", "৩৩", "৩২", R.drawable.math_icon));
                tempList.add(new Question("২৭ ÷ ৯ = ?", "২", "৩", "৪", "৫", "৩", R.drawable.math_icon));
                tempList.add(new Question("৬ + ১৩ = ?", "১৮", "১৯", "২০", "২১", "১৯", R.drawable.math_icon));
                tempList.add(new Question("১৮ - ৯ = ?", "৭", "৮", "৯", "১০", "৯", R.drawable.math_icon));
                tempList.add(new Question("৯ × ৪ = ?", "৩৫", "৩৬", "৩৭", "৩৮", "৩৬", R.drawable.math_icon));
                tempList.add(new Question("৩২ ÷ ৮ = ?", "৩", "৪", "৫", "৬", "৪", R.drawable.math_icon));
                tempList.add(new Question("১০ + ৯ = ?", "১৮", "১৯", "২০", "২১", "১৯", R.drawable.math_icon));
                tempList.add(new Question("১৫ - ৫ = ?", "৯", "১০", "১১", "১২", "১০", R.drawable.math_icon));
                tempList.add(new Question("৭ × ৩ = ?", "২০", "২১", "২২", "২৩", "২১", R.drawable.math_icon));
                tempList.add(new Question("২৮ ÷ ৭ = ?", "৩", "৪", "৫", "৬", "৪", R.drawable.math_icon));
                tempList.add(new Question("১৪ + ৫ = ?", "১৮", "১৯", "২০", "২১", "১৯", R.drawable.math_icon));
                tempList.add(new Question("১৬ - ৬ = ?", "৮", "৯", "১০", "১১", "১০", R.drawable.math_icon));
                tempList.add(new Question("৫ × ৮ = ?", "৩৫", "৪০", "৪৫", "৫০", "৪০", R.drawable.math_icon));
                tempList.add(new Question("৩৬ ÷ ৬ = ?", "৪", "৫", "৬", "৭", "৬", R.drawable.math_icon));
                tempList.add(new Question("৯ + ৭ = ?", "১৫", "১৬", "১৭", "১৮", "১৬", R.drawable.math_icon));
                tempList.add(new Question("২৩ - ১৪ = ?", "৮", "৯", "১০", "১১", "৯", R.drawable.math_icon));
                tempList.add(new Question("৪ × ৯ = ?", "৩৪", "৩৫", "৩৬", "৩৭", "৩৬", R.drawable.math_icon));
                tempList.add(new Question("৪৫ ÷ ৫ = ?", "৭", "৮", "৯", "১০", "৯", R.drawable.math_icon));
                tempList.add(new Question("১৩ + ৮ = ?", "২০", "২১", "২২", "২৩", "২১", R.drawable.math_icon));
                tempList.add(new Question("২৫ - ১৭ = ?", "৭", "৮", "৯", "১০", "৮", R.drawable.math_icon));
                tempList.add(new Question("৬ × ৭ = ?", "৪০", "৪১", "৪২", "৪৩", "৪২", R.drawable.math_icon));
                tempList.add(new Question("৪৯ ÷ ৭ = ?", "৫", "৬", "৭", "৮", "৭", R.drawable.math_icon));
                tempList.add(new Question("১১ + ৯ = ?", "১৮", "১৯", "২০", "২১", "২০", R.drawable.math_icon));
                tempList.add(new Question("৩০ - ১২ = ?", "১৬", "১৭", "১৮", "১৯", "১৮", R.drawable.math_icon));
                tempList.add(new Question("৮ × ৫ = ?", "৩৫", "৩৮", "৪০", "৪৫", "৪০", R.drawable.math_icon));
                tempList.add(new Question("৬৩ ÷ ৯ = ?", "৬", "৭", "৮", "৯", "৭", R.drawable.math_icon));
                tempList.add(new Question("৯ + ৯ = ?", "১৭", "১৮", "১৯", "২০", "১৮", R.drawable.math_icon));
                tempList.add(new Question("১৭ - ৯ = ?", "৭", "৮", "৯", "১০", "৮", R.drawable.math_icon));
                tempList.add(new Question("৭ × ৪ = ?", "২৬", "২৭", "২৮", "২৯", "২৮", R.drawable.math_icon));
                tempList.add(new Question("৫৬ ÷ ৮ = ?", "৬", "৭", "৮", "৯", "৭", R.drawable.math_icon));
                tempList.add(new Question("১০ + ১২ = ?", "২১", "২২", "২৩", "২৪", "২২", R.drawable.math_icon));
                tempList.add(new Question("২৭ - ১১ = ?", "১৫", "১৬", "১৭", "১৮", "১৬", R.drawable.math_icon));
                tempList.add(new Question("৯ × ৫ = ?", "৪৫", "৪৬", "৪৭", "৪৮", "৪৫", R.drawable.math_icon));
                tempList.add(new Question("৩৬ ÷ ৪ = ?", "৮", "৯", "১০", "১১", "৯", R.drawable.math_icon));
                tempList.add(new Question("১২ + ১৫ = ?", "২৫", "২৬", "২৭", "২৮", "২৭", R.drawable.math_icon));
                tempList.add(new Question("২৮ - ১৩ = ?", "১৪", "১৫", "১৬", "১৭", "১৫", R.drawable.math_icon));
                tempList.add(new Question("৮ × ৬ = ?", "৪৬", "৪৭", "৪৮", "৪৯", "৪৮", R.drawable.math_icon));
                tempList.add(new Question("৭২ ÷ ৮ = ?", "৮", "৯", "১০", "১১", "৯", R.drawable.math_icon));
                tempList.add(new Question("১১ + ১৩ = ?", "২৩", "২৪", "২৫", "২৬", "২৪", R.drawable.math_icon));
                tempList.add(new Question("৩৫ - ১৮ = ?", "১৫", "১৬", "১৭", "১৮", "১৭", R.drawable.math_icon));
                tempList.add(new Question("৭ × ৬ = ?", "৪০", "৪১", "৪২", "৪৩", "৪২", R.drawable.math_icon));
                tempList.add(new Question("৬৪ ÷ ৮ = ?", "৬", "৭", "৮", "৯", "৮", R.drawable.math_icon));
                tempList.add(new Question("১৪ + ১২ = ?", "২৫", "২৬", "২৭", "২৮", "২৬", R.drawable.math_icon));
                tempList.add(new Question("১৯ - ৯ = ?", "৯", "১০", "১১", "১২", "১০", R.drawable.math_icon));
                tempList.add(new Question("৯ × ৯ = ?", "৮০", "৮১", "৮২", "৮৩", "৮১", R.drawable.math_icon));
                tempList.add(new Question("৮১ ÷ ৯ = ?", "৮", "৯", "১০", "১১", "৯", R.drawable.math_icon));
                tempList.add(new Question("১৩ + ১৪ = ?", "২৬", "২৭", "২৮", "২৯", "২৭", R.drawable.math_icon));
                tempList.add(new Question("২০ - ১০ = ?", "৮", "৯", "১০", "১১", "১০", R.drawable.math_icon));
                tempList.add(new Question("৫ × ৯ = ?", "৪৪", "৪৫", "৪৬", "৪৭", "৪৫", R.drawable.math_icon));
                tempList.add(new Question("৯০ ÷ ৯ = ?", "৮", "৯", "১০", "১১", "১০", R.drawable.math_icon));
                tempList.add(new Question("১৫ + ১৫ = ?", "২৯", "৩০", "৩১", "৩২", "৩০", R.drawable.math_icon));
                tempList.add(new Question("৩০ - ২০ = ?", "৯", "১০", "১১", "১২", "১০", R.drawable.math_icon));
                tempList.add(new Question("৮ × ৮ = ?", "৬২", "৬৩", "৬৪", "৬৫", "৬৪", R.drawable.math_icon));
                tempList.add(new Question("৪৯ ÷ ৭ = ?", "৬", "৭", "৮", "৯", "৭", R.drawable.math_icon));
                tempList.add(new Question("১৭ + ৮ = ?", "২৪", "২৫", "২৬", "২৭", "২৫", R.drawable.math_icon));
                tempList.add(new Question("২৫ - ১৫ = ?", "৯", "১০", "১১", "১২", "১০", R.drawable.math_icon));
                tempList.add(new Question("৬ × ৬ = ?", "৩৪", "৩৫", "৩৬", "৩৭", "৩৬", R.drawable.math_icon));
                tempList.add(new Question("৭২ ÷ ৯ = ?", "৭", "৮", "৯", "১০", "৮", R.drawable.math_icon));
                tempList.add(new Question("১৮ + ৬ = ?", "২৩", "২৪", "২৫", "২৬", "২৪", R.drawable.math_icon));
                tempList.add(new Question("৩২ - ১৪ = ?", "১৭", "১৮", "১৯", "২০", "১৮", R.drawable.math_icon));
                tempList.add(new Question("৯ × ৬ = ?", "৫২", "৫৩", "৫৪", "৫৫", "৫৪", R.drawable.math_icon));
                tempList.add(new Question("১০০ ÷ ১০ = ?", "৮", "৯", "১০", "১১", "১০", R.drawable.math_icon));


                break;
        }

        Collections.shuffle(tempList);

        int limit = Math.min(20, tempList.size());
        for (int i = 0; i < limit; i++) {
            questionList.add(tempList.get(i));
        }

        if (countTotal != null) {
            countTotal.setText("/ " + questionList.size());
        }
    }

}

