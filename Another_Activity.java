package com.nursiam.amarbanglaschool;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

import java.util.ArrayList;
import java.util.HashMap;

public class Another_Activity extends AppCompatActivity {

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    RecyclerView recyclerViews;
    String PageName;
    Load_Data loadData;
    Toolbar toolbar;
    TextToSpeechHelper_Bangla textToSpeechHelperBangla;
    TextToSpeechHelper_English textToSpeechHelperEnglish;
    LinearLayout adContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_another);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    /*
     new Thread(
                () -> {
                    MobileAds.initialize(this, initializationStatus -> {});

                })
                .start();
     */

        GDPRmassage.requestConsentForm(Another_Activity.this);


        recyclerViews = findViewById(R.id.recyclerViews);


        toolbar = findViewById(R.id.toolbar);
        loadData = new Load_Data();
        textToSpeechHelperBangla = new TextToSpeechHelper_Bangla(this);
        textToSpeechHelperEnglish = new TextToSpeechHelper_English(this);

        PageName = getIntent().getStringExtra("pageName");

        // --- Toolbar ---
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrows);
            getSupportActionBar().setTitle(PageName);
        }
        toolbar.setNavigationOnClickListener(v -> finish());


        adContainerView = findViewById(R.id.adContainerView);
        // --- Load Ad ---
        loadBanner();


        // --- Load Data ---
        informationLoad();


        // --- Adapter & Layout Manager ---
        My_RecycleView adapter = new My_RecycleView();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (PageName != null && (PageName.contains("স্বরবর্ণ") ||
                        PageName.contains("ব্যঞ্জনবর্ণ") ||
                        PageName.contains("ইংরেজি"))) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });

        recyclerViews.setLayoutManager(layoutManager);
        recyclerViews.setAdapter(adapter);
    }


    private void loadBanner() {

        if (adContainerView == null) return;

        AdView adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.Banner_AD_ID));

        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        //AdSize adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, 360);

        // Define dynamic width
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

        adView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {

                adContainerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
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


        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void informationLoad() {
        if (PageName == null) return;
        if (PageName.contains("স্বরবর্ণ")) {
            arrayList = loadData.SORBORNO_BAKKO();
        } else if (PageName.contains("ব্যঞ্জনবর্ণ")) {
            arrayList = loadData.BENJORBORNO_BAKKO();
        } else if (PageName.contains("ইংরেজি")) {
            arrayList = loadData.ENGLISH_ALFABET_BAKKO();
        }
    }

    private class My_RecycleView extends RecyclerView.Adapter<My_RecycleView.BAKKO_ViewHolder> {

        @NonNull
        @Override
        public BAKKO_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_wotde_bakko_design, parent, false);
            return new BAKKO_ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BAKKO_ViewHolder holder, int position) {
            HashMap<String, String> map = arrayList.get(position);

            String letter = map.get("Bangla");
            String word = map.get("Bangla_text");
            String description = map.get("Bangla_Discription");
            String image = map.get("image");
            String language = map.get("Language");

            holder.letter_text.setText(letter);
            holder.short_text.setText(letter + " - " + word);
            holder.long_text.setText(description);

            if (image != null && !image.isEmpty()) {
                holder.imageView.setImageResource(Integer.parseInt(image));
            }

            holder.card_main.setOnClickListener(v -> {
                if (description != null) {
                    if (language != null && language.equalsIgnoreCase("english")) {
                        textToSpeechHelperEnglish.speak(description);
                    } else {
                        textToSpeechHelperBangla.speak(description);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class BAKKO_ViewHolder extends RecyclerView.ViewHolder {
            CardView card_main;
            TextView letter_text, short_text, long_text;
            ImageView imageView;

            public BAKKO_ViewHolder(@NonNull View itemView) {
                super(itemView);
                letter_text = itemView.findViewById(R.id.letter_text);
                short_text = itemView.findViewById(R.id.short_text);
                long_text = itemView.findViewById(R.id.long_text);
                imageView = itemView.findViewById(R.id.imageView);
                card_main = itemView.findViewById(R.id.card_main);
            }
        }
    }
}