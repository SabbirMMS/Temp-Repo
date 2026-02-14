package com.nursiam.amarbanglaschool;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.HashMap;

public class Page_Activity extends AppCompatActivity {

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    HashMap<String, String> hashMap;
    RecyclerView recyclerViews;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsing_Toolbar;
    ImageView imageView;
    AppBarLayout appBarLayout;
    Load_Data loadData;

    String PageName;
    LinearLayout adContainerView;
    RecyclerView.Adapter adapter;
    private TextToSpeechHelper_Bangla textToSpeechHelper;
    private TextToSpeechHelper_Arby textToSpeechHelperArby;
    private TextToSpeechHelper_English textToSpeechHelperEnglish;

    public static String COLLAPSING_TOOLBAR = "";
    public static Bitmap MY_BITMAP = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page);

        // Handle system insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textToSpeechHelper = new TextToSpeechHelper_Bangla(this);
        textToSpeechHelperArby = new TextToSpeechHelper_Arby(this);
        textToSpeechHelperEnglish = new TextToSpeechHelper_English(this);

        Intent intent = getIntent();
        PageName = intent.getStringExtra("pageName");

        loadData = new Load_Data();

        informationLoad();


        adContainerView = findViewById(R.id.adContainerView);
        // --- Load Ad ---
        loadBanner();

        GDPRmassage.requestConsentForm(Page_Activity.this);


        // ---------------------Tool bar & AppBarLayout------------------------
        toolbar = findViewById(R.id.toolbar);
        collapsing_Toolbar = findViewById(R.id.collapsing_Toolbar);
        imageView = findViewById(R.id.imageView);
        appBarLayout = findViewById(R.id.appbar);

        toolbar.getMenu().clear();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrows);
            getSupportActionBar().setTitle("");
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        collapsing_Toolbar.setTitle(COLLAPSING_TOOLBAR);
        if (MY_BITMAP != null) imageView.setImageBitmap(MY_BITMAP);

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            int totalScrollRange = appBarLayout.getTotalScrollRange();
            float percentage = (float) Math.abs(verticalOffset) / (float) totalScrollRange;
            imageView.setAlpha(1 - percentage);
        });

        recyclerViews = findViewById(R.id.recyclerViews);
        recyclerViews.setAdapter(new My_RecycleView());

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = recyclerViews.getAdapter().getItemViewType(position);


                if (viewType == My_RecycleView.TYPE_BANGLA_MASH) {
                    return 2;
                } else if (viewType == My_RecycleView.TYPE_BANGLA_SOPTAH) {
                    return 2;
                } else if (viewType == My_RecycleView.TYPE_ENGLISH_MONTH) {
                    return 2;
                } else if (viewType == My_RecycleView.TYPE_ENGLISH_WIKE) {
                    return 2;
                }else if (viewType == My_RecycleView.TYPE_STORY) {
                    return 2;
                }else if (viewType == My_RecycleView.TYPE_POYMES) {
                    return 2;
                }else if (viewType == My_RecycleView.TYPE_GOOD_HAVID) {
                    return 2;
                }

                // বাকিগুলো ১ span নেবে = ২ কলাম করে দেখাবে
                return 1;
            }
        });

        recyclerViews.setLayoutManager(layoutManager);

    }

    // ------------------ RecyclerView Adapter ---------------------------------------------------------

    private class My_RecycleView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        // Define item types
        private static final int TYPE_SORBORNO = 0;
        private static final int TYPE_BENJORBORNO = 1;
        private static final int TYPE_BANGLA_MASH = 2;
        private static final int TYPE_BANGLA_SOPTAH = 3;
        private static final int TYPE_ENGLISH_ALFABET = 4;
        private static final int TYPE_ENGLISH_MONTH = 5;
        private static final int TYPE_ENGLISH_WIKE = 6;
        private static final int TYPE_VAICOLE = 7;
        private static final int TYPE_BIRD = 8;
        private static final int TYPE_FLOWEW = 9;
        private static final int TYPE_FROUT = 10;
        private static final int TYPE_VAGITABLE = 11;
        private static final int TYPE_FISH = 12;
        private static final int TYPE_ANIMAL = 13;
        private static final int TYPE_ORGAN = 14;
        private static final int TYPE_STORY = 15;
        private static final int TYPE_POYMES = 16;
        private static final int TYPE_GOOD_HAVID = 17;
        private static final int TYPE_ARBY_HARAF = 18;
        private static final int TYPE_SONKHA = 19;

//..................... ViewHolders.................................................................


        private class SORBORNO_ViewHolder extends RecyclerView.ViewHolder {
            TextView text;

            public SORBORNO_ViewHolder(@NonNull View itemView) {
                super(itemView);

                text = itemView.findViewById(R.id.text);
            }
        }


        private class BENJORBORNO_ViewHolder extends RecyclerView.ViewHolder {
            TextView text;
            public BENJORBORNO_ViewHolder(@NonNull View itemView) {
                super(itemView);

                text = itemView.findViewById(R.id.text);

            }
        }


        private class BANGLA_MASH_ViewHolder extends RecyclerView.ViewHolder {
            TextView text;
            MaterialCardView materialCardView;
            public BANGLA_MASH_ViewHolder(@NonNull View itemView) {
                super(itemView);

                text = itemView.findViewById(R.id.text);
                materialCardView = itemView.findViewById(R.id.materialCardView);
            }
        }


        private class BANGLA_SOPTAH_ViewHolder extends RecyclerView.ViewHolder{

            TextView text;
            MaterialCardView materialCardView;

            public BANGLA_SOPTAH_ViewHolder(@NonNull View itemView) {
                super(itemView);

                text = itemView.findViewById(R.id.text);
                materialCardView = itemView.findViewById(R.id.materialCardView);

            }
        }

        private class ENGLISH_ALFABET_ViewHolder extends RecyclerView.ViewHolder{
            TextView text;
            public ENGLISH_ALFABET_ViewHolder(@NonNull View itemView) {
                super(itemView);

                text = itemView.findViewById(R.id.text);

            }
        }

        private class ARBY_HARAF_ViewHolder extends RecyclerView.ViewHolder{
            TextView text;
            public ARBY_HARAF_ViewHolder(@NonNull View itemView) {
                super(itemView);

                text = itemView.findViewById(R.id.text);

            }
        }


        private class SONKHA_ViewHolder extends RecyclerView.ViewHolder{
            TextView text;
            public SONKHA_ViewHolder(@NonNull View itemView) {
                super(itemView);

                text = itemView.findViewById(R.id.text);

            }
        }



        private class ENGLISH_MONTH_ViewHolder extends RecyclerView.ViewHolder{

            TextView text;
            MaterialCardView materialCardView;

            public ENGLISH_MONTH_ViewHolder(@NonNull View itemView) {
                super(itemView);

                text = itemView.findViewById(R.id.text);
                materialCardView = itemView.findViewById(R.id.materialCardView);
            }
        }

        private class ENGLISH_WIKE_ViewHolder extends RecyclerView.ViewHolder{
            TextView text;
            MaterialCardView materialCardView;
            public ENGLISH_WIKE_ViewHolder(@NonNull View itemView) {
                super(itemView);

                text = itemView.findViewById(R.id.text);
                materialCardView = itemView.findViewById(R.id.materialCardView);

            }
        }

        private class VAICOLE_ViewHolder extends RecyclerView.ViewHolder{
            TextView text;
            MaterialCardView materialCardView;
            ImageView imageView;
            public VAICOLE_ViewHolder(@NonNull View itemView) {
                super(itemView);

                text = itemView.findViewById(R.id.text);
                materialCardView = itemView.findViewById(R.id.materialCardView);
                imageView = itemView.findViewById(R.id.imageView);


            }
        }

        private class BIRD_ViewHolder extends RecyclerView.ViewHolder{
            TextView text;
            MaterialCardView materialCardView;
            ImageView imageView;
            public BIRD_ViewHolder(@NonNull View itemView) {
                super(itemView);

                text = itemView.findViewById(R.id.text);
                materialCardView = itemView.findViewById(R.id.materialCardView);
                imageView = itemView.findViewById(R.id.imageView);

            }
        }

        private class FLOWEW_ViewHolder extends RecyclerView.ViewHolder{
            TextView text;
            MaterialCardView materialCardView;
            ImageView imageView;
            public FLOWEW_ViewHolder(@NonNull View itemView) {
                super(itemView);


                text = itemView.findViewById(R.id.text);
                materialCardView = itemView.findViewById(R.id.materialCardView);
                imageView = itemView.findViewById(R.id.imageView);

            }
        }

        private class FROUT_ViewHolder extends RecyclerView.ViewHolder{
            TextView text;
            MaterialCardView materialCardView;
            ImageView imageView;
            public FROUT_ViewHolder(@NonNull View itemView) {
                super(itemView);


                text = itemView.findViewById(R.id.text);
                materialCardView = itemView.findViewById(R.id.materialCardView);
                imageView = itemView.findViewById(R.id.imageView);

            }
        }

        private class VAGITABLE_ViewHolder extends RecyclerView.ViewHolder{
            TextView text;
            MaterialCardView materialCardView;
            ImageView imageView;
            public VAGITABLE_ViewHolder(@NonNull View itemView) {
                super(itemView);


                text = itemView.findViewById(R.id.text);
                materialCardView = itemView.findViewById(R.id.materialCardView);
                imageView = itemView.findViewById(R.id.imageView);
            }
        }

        private class FISH_ViewHolder extends RecyclerView.ViewHolder{
            TextView text;
            MaterialCardView materialCardView;
            ImageView imageView;
            public FISH_ViewHolder(@NonNull View itemView) {
                super(itemView);


                text = itemView.findViewById(R.id.text);
                materialCardView = itemView.findViewById(R.id.materialCardView);
                imageView = itemView.findViewById(R.id.imageView);

            }
        }

        private class ANIMAL_ViewHolder extends RecyclerView.ViewHolder{
            TextView text;
            MaterialCardView materialCardView;
            ImageView imageView;
            public ANIMAL_ViewHolder(@NonNull View itemView) {
                super(itemView);


                text = itemView.findViewById(R.id.text);
                materialCardView = itemView.findViewById(R.id.materialCardView);
                imageView = itemView.findViewById(R.id.imageView);

            }
        }

        private class ORGAN_ViewHolder extends RecyclerView.ViewHolder{
            TextView text;
            MaterialCardView materialCardView;
            ImageView imageView;
            public ORGAN_ViewHolder(@NonNull View itemView) {
                super(itemView);


                text = itemView.findViewById(R.id.text);
                materialCardView = itemView.findViewById(R.id.materialCardView);
                imageView = itemView.findViewById(R.id.imageView);

            }
        }

        private class STORY_ViewHolder extends RecyclerView.ViewHolder{
            TextView subect,main_text;
            public STORY_ViewHolder(@NonNull View itemView) {
                super(itemView);
                subect = itemView.findViewById(R.id.subect);
                main_text = itemView.findViewById(R.id.main_text);

            }
        }

        private class POYMES_ViewHolder extends RecyclerView.ViewHolder{

            TextView subect,main_text;
            public POYMES_ViewHolder(@NonNull View itemView) {
                super(itemView);
                subect = itemView.findViewById(R.id.subect);
                main_text = itemView.findViewById(R.id.main_text);

            }
        }

        private class GOOD_HAVID_ViewHolder extends RecyclerView.ViewHolder{
            TextView subect,main_text;
            public GOOD_HAVID_ViewHolder(@NonNull View itemView) {
                super(itemView);

                subect = itemView.findViewById(R.id.subect);
                main_text = itemView.findViewById(R.id.main_text);
            }
        }



//..................... ViewHolders.................................................................

        @Override
        public int getItemViewType(int position) {


            if (PageName.equals("স্বরবর্ণ")) return TYPE_SORBORNO;
            else if (PageName.equals("ব্যঞ্জনবর্ণ")) return TYPE_BENJORBORNO;
            else if (PageName.equals("বাংলা মাস")) return TYPE_BANGLA_MASH;
            else if (PageName.equals("বাংলা সপ্তাহ")) return TYPE_BANGLA_SOPTAH;
            else if (PageName.equals("ইংরেজী বর্ণমালা")) return TYPE_ENGLISH_ALFABET;
            else if (PageName.equals("আরবি হরফ")) return TYPE_ARBY_HARAF;
            else if (PageName.equals("সংখ্যা")) return TYPE_SONKHA;
            else if (PageName.equals("ইংরেজি মাস")) return TYPE_ENGLISH_MONTH;
            else if (PageName.equals("ইংরেজি সপ্তাহ")) return TYPE_ENGLISH_WIKE;
            else if (PageName.equals("যানবাহন")) return TYPE_VAICOLE;
            else if (PageName.equals("পাখি")) return TYPE_BIRD;
            else if (PageName.equals("ফুল")) return TYPE_FLOWEW;
            else if (PageName.equals("ফল")) return TYPE_FROUT;
            else if (PageName.equals("সবজি")) return TYPE_VAGITABLE;
            else if (PageName.equals("মাছ")) return TYPE_FISH;
            else if (PageName.equals("জীবজন্তু")) return TYPE_ANIMAL;
            else if (PageName.equals("অঙ্গ-প্রত্যঙ্গ")) return TYPE_ORGAN;
            else if (PageName.equals("গল্প")) return TYPE_STORY;
            else if (PageName.equals("কবিতা")) return TYPE_POYMES;
            else if (PageName.equals("ভালো অভ্যাস")) return TYPE_GOOD_HAVID;


            else return TYPE_GOOD_HAVID;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();

            if (viewType == TYPE_SORBORNO) {
                View view = inflater.inflate(R.layout.custom_wotde_design, parent, false);
                return new SORBORNO_ViewHolder(view);

            } else if (viewType == TYPE_BENJORBORNO) {
                View view = inflater.inflate(R.layout.custom_wotde_design, parent, false);
                return new BENJORBORNO_ViewHolder(view);

            } else if (viewType == TYPE_BANGLA_MASH) {
                View view = inflater.inflate(R.layout.custom_text_sound_design, parent, false);
                return new BANGLA_MASH_ViewHolder(view);

            }else if (viewType == TYPE_BANGLA_SOPTAH) {
                View view = inflater.inflate(R.layout.custom_text_sound_design, parent, false);
                return new BANGLA_SOPTAH_ViewHolder(view);



            }else if (viewType == TYPE_ENGLISH_ALFABET) {
                View view = inflater.inflate(R.layout.custom_wotde_design, parent, false);
                return new ENGLISH_ALFABET_ViewHolder(view);


            }else if (viewType == TYPE_ARBY_HARAF) {
                View view = inflater.inflate(R.layout.custom_wotde_design, parent, false);
                return new ARBY_HARAF_ViewHolder(view);


            }else if (viewType == TYPE_SONKHA) {
                View view = inflater.inflate(R.layout.custom_wotde_design, parent, false);
                return new SONKHA_ViewHolder(view);



            }else if (viewType == TYPE_ENGLISH_MONTH) {
                View view = inflater.inflate(R.layout.custom_text_sound_design, parent, false);
                return new ENGLISH_MONTH_ViewHolder(view);

            }else if (viewType == TYPE_ENGLISH_WIKE) {
                View view = inflater.inflate(R.layout.custom_text_sound_design, parent, false);
                return new ENGLISH_WIKE_ViewHolder(view);

            }else if (viewType == TYPE_VAICOLE) {
                View view = inflater.inflate(R.layout.custom_image_text_design, parent, false);
                return new VAICOLE_ViewHolder(view);

            }else if (viewType == TYPE_BIRD) {
                View view = inflater.inflate(R.layout.custom_image_text_design, parent, false);
                return new BIRD_ViewHolder(view);

            }else if (viewType == TYPE_FLOWEW) {
                View view = inflater.inflate(R.layout.custom_image_text_design, parent, false);
                return new FLOWEW_ViewHolder(view);

            }else if (viewType == TYPE_FROUT) {
                View view = inflater.inflate(R.layout.custom_image_text_design, parent, false);
                return new FROUT_ViewHolder(view);

            }else if (viewType == TYPE_VAGITABLE) {
                View view = inflater.inflate(R.layout.custom_image_text_design, parent, false);
                return new VAGITABLE_ViewHolder(view);

            }else if (viewType == TYPE_FISH) {
                View view = inflater.inflate(R.layout.custom_image_text_design, parent, false);
                return new FISH_ViewHolder(view);

            }else if (viewType == TYPE_ANIMAL) {
                View view = inflater.inflate(R.layout.custom_image_text_design, parent, false);
                return new ANIMAL_ViewHolder(view);

            }else if (viewType == TYPE_ORGAN) {
                View view = inflater.inflate(R.layout.custom_image_text_design, parent, false);
                return new ORGAN_ViewHolder(view);

            }else if (viewType == TYPE_STORY) {
                View view = inflater.inflate(R.layout.layout_poit_design, parent, false);
                return new STORY_ViewHolder(view);

            }else if (viewType == TYPE_POYMES) {
                View view = inflater.inflate(R.layout.layout_poimes_design, parent, false);
                return new POYMES_ViewHolder(view);

            }else if (viewType == TYPE_GOOD_HAVID) {
                View view = inflater.inflate(R.layout.layout_poit_design, parent, false);
                return new GOOD_HAVID_ViewHolder(view);

           }

            else {
                View view = inflater.inflate(R.layout.layout_poit_design, parent, false);
                return new GOOD_HAVID_ViewHolder(view);
            }


        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            HashMap<String, String> map = arrayList.get(position);

            int color = new int[]{

                    Color.parseColor("#FFFFFF"), Color.parseColor("#000000"), Color.parseColor("#FFEB3B"), Color.parseColor("#FFD700"),
                    Color.parseColor("#FF9800"), Color.parseColor("#FF5722"), Color.parseColor("#F44336"), Color.parseColor("#DC143C"),
                    Color.parseColor("#E91E63"), Color.parseColor("#C2185B"), Color.parseColor("#2196F3"), Color.parseColor("#1565C0"),
                    Color.parseColor("#00BCD4"), Color.parseColor("#00838F"), Color.parseColor("#9C27B0"), Color.parseColor("#6A1B9A"),
                    Color.parseColor("#673AB7"), Color.parseColor("#3F51B5"), Color.parseColor("#795548"), Color.parseColor("#607D8B")

            }[new java.util.Random().nextInt(20)];



            if (holder instanceof SORBORNO_ViewHolder) {
                SORBORNO_ViewHolder vh = (SORBORNO_ViewHolder) holder;
                vh.text.setText(map.get("Bangla"));
                vh.text.setTextColor(color);
                vh.text.setOnClickListener(v -> {
                    String speakText = map.get("Bangla");
                    if (speakText != null && !speakText.trim().isEmpty()) {
                        boolean spoken = textToSpeechHelper.speak(speakText);
                        if (!spoken) {
                            Custom_Tost.show(Page_Activity.this, "language not supported");
                        }
                    }
                });

            } else if (holder instanceof BENJORBORNO_ViewHolder) {
                BENJORBORNO_ViewHolder vh = (BENJORBORNO_ViewHolder) holder;
                vh.text.setText(map.get("Bangla"));
                vh.text.setTextColor(color);
                vh.text.setOnClickListener(v -> {
                    String speakText = map.get("Bangla");
                    if (speakText != null && !speakText.trim().isEmpty()) {
                        boolean spoken = textToSpeechHelper.speak(speakText);
                        if (!spoken) {
                            Custom_Tost.show(Page_Activity.this, "language not supported");
                        }
                    }
                });
            }

            else if (holder instanceof ARBY_HARAF_ViewHolder) {
                ARBY_HARAF_ViewHolder vh = (ARBY_HARAF_ViewHolder) holder;
                vh.text.setText(map.get("Bangla"));
                vh.text.setTextColor(color);
                vh.text.setOnClickListener(v -> {
                    String speakText = map.get("Bangla");
                    if (speakText != null && !speakText.trim().isEmpty()) {
                        boolean spoken = textToSpeechHelperArby.speak(speakText);
                        if (!spoken) {
                            Custom_Tost.show(Page_Activity.this, "language not supported");
                        }
                    }
                });

            } else if (holder instanceof SONKHA_ViewHolder) {
                SONKHA_ViewHolder vh = (SONKHA_ViewHolder) holder;
                vh.text.setText(map.get("Bangla"));
                vh.text.setTextColor(color);
                vh.text.setOnClickListener(v -> {
                    String speakText = map.get("Bangla");
                    if (speakText != null && !speakText.trim().isEmpty()) {
                        boolean spoken = textToSpeechHelper.speak(speakText);
                        if (!spoken) {
                            Custom_Tost.show(Page_Activity.this, "language not supported");
                        }
                    }
                });
            }
            else if (holder instanceof BANGLA_MASH_ViewHolder) {
                BANGLA_MASH_ViewHolder vh = (BANGLA_MASH_ViewHolder) holder;
                vh.text.setText(map.get("Bangla"));
                vh.text.setTextColor(color);

                vh.materialCardView.setOnClickListener(view1 -> {
                    String speakText = vh.text.getText().toString().trim();
                    if (!speakText.isEmpty()) {
                        boolean spoken = textToSpeechHelper.speak(speakText);
                        if (!spoken) {

                            Custom_Tost.show(Page_Activity.this,"language not supported");

                             }
                    }
                });
            }

            else if (holder instanceof BANGLA_SOPTAH_ViewHolder) {
                BANGLA_SOPTAH_ViewHolder vh = (BANGLA_SOPTAH_ViewHolder) holder;
                vh.text.setText(map.get("Bangla"));
                  vh.text.setTextColor(color);

                vh.materialCardView.setOnClickListener(view1 -> {
                    String speakText = vh.text.getText().toString().trim();
                    if (!speakText.isEmpty()) {
                        boolean spoken = textToSpeechHelper.speak(speakText);
                        if (!spoken) {
                            Custom_Tost.show(Page_Activity.this,"language not supported");
                        }
                    }
                });
            }


          else if (holder instanceof ENGLISH_ALFABET_ViewHolder) {
                ENGLISH_ALFABET_ViewHolder vh = (ENGLISH_ALFABET_ViewHolder) holder;
                vh.text.setText(map.get("Bangla"));
                vh.text.setTextColor(color);
                vh.text.setOnClickListener(v -> {
                    String speakText = map.get("Bangla");
                    if (speakText != null && !speakText.trim().isEmpty()) {
                        boolean spoken = textToSpeechHelperEnglish.speak(speakText);
                        if (!spoken) {
                            Custom_Tost.show(Page_Activity.this, "language not supported");
                        }
                    }
                });
            }


            else if (holder instanceof ENGLISH_MONTH_ViewHolder) {
                ENGLISH_MONTH_ViewHolder vh = (ENGLISH_MONTH_ViewHolder) holder;
                vh.text.setText(map.get("Bangla"));
                  vh.text.setTextColor(color);

                vh.materialCardView.setOnClickListener(view1 -> {
                    String speakText = vh.text.getText().toString().trim();
                    if (!speakText.isEmpty()) {
                        boolean spoken = textToSpeechHelper.speak(speakText);
                        if (!spoken) {
                            Custom_Tost.show(Page_Activity.this,"language not supported");
                        }
                    }
                });
            }


            else if (holder instanceof ENGLISH_WIKE_ViewHolder) {
                ENGLISH_WIKE_ViewHolder vh = (ENGLISH_WIKE_ViewHolder) holder;
                vh.text.setText(map.get("Bangla"));
                vh.text.setTextColor(color);

                vh.materialCardView.setOnClickListener(view1 -> {
                    String speakText = vh.text.getText().toString().trim();
                    if (!speakText.isEmpty()) {
                        boolean spoken = textToSpeechHelper.speak(speakText);
                        if (!spoken) {  Custom_Tost.show(Page_Activity.this,"language not supported");
                        }
                    }
                });
            }

            else if (holder instanceof VAICOLE_ViewHolder) {
                VAICOLE_ViewHolder variable = (VAICOLE_ViewHolder) holder;
                variable.text.setText(map.get("Bangla"));

                // ইমেজ সেট করা
                String imageName = map.get("image");
                if (imageName != null && !imageName.isEmpty()) {
                    int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                    if (resId != 0) {
                        variable.imageView.setImageResource(resId);
                    } else {
                        variable.imageView.setImageResource(R.drawable.image_bangal); // fallback
                    }
                }

                // কার্ড ক্লিক করলে Text-to-Speech চালানো
                variable.materialCardView.setOnClickListener(view1 -> {
                    String speakText = variable.text.getText().toString().trim();
                    if (!speakText.isEmpty()) {
                        boolean spoken = textToSpeechHelper.speak(speakText);
                        if (!spoken) {  Custom_Tost.show(Page_Activity.this,"language not supported");
                        }
                    }
                });
            }



            else if (holder instanceof BIRD_ViewHolder) {
                BIRD_ViewHolder variable = (BIRD_ViewHolder) holder;
                variable.text.setText(map.get("Bangla"));

                // ইমেজ সেট করা
                String imageName = map.get("image");
                if (imageName != null && !imageName.isEmpty()) {
                    int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                    if (resId != 0) {
                        variable.imageView.setImageResource(resId);
                    } else {
                        variable.imageView.setImageResource(R.drawable.image_bangal); // fallback
                    }
                }

                // কার্ড ক্লিক করলে Text-to-Speech চালানো
                variable.materialCardView.setOnClickListener(view1 -> {
                    String speakText = variable.text.getText().toString().trim();
                    if (!speakText.isEmpty()) {
                        boolean spoken = textToSpeechHelper.speak(speakText);
                        if (!spoken) {  Custom_Tost.show(Page_Activity.this,"language not supported");
                        }
                    }
                });
            }


            else if (holder instanceof FLOWEW_ViewHolder) {
                FLOWEW_ViewHolder variable = (FLOWEW_ViewHolder) holder;
                variable.text.setText(map.get("Bangla"));

                // ইমেজ সেট করা
                String imageName = map.get("image");
                if (imageName != null && !imageName.isEmpty()) {
                    int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                    if (resId != 0) {
                        variable.imageView.setImageResource(resId);
                    } else {
                        variable.imageView.setImageResource(R.drawable.image_bangal); // fallback
                    }
                }

                // কার্ড ক্লিক করলে Text-to-Speech চালানো
                variable.materialCardView.setOnClickListener(view1 -> {
                    String speakText = variable.text.getText().toString().trim();
                    if (!speakText.isEmpty()) {
                        boolean spoken = textToSpeechHelper.speak(speakText);
                        if (!spoken) {  Custom_Tost.show(Page_Activity.this,"language not supported");
                        }
                    }
                });
            }


            else if (holder instanceof FROUT_ViewHolder) {
                FROUT_ViewHolder variable = (FROUT_ViewHolder) holder;
                variable.text.setText(map.get("Bangla"));

                // ইমেজ সেট করা
                String imageName = map.get("image");
                if (imageName != null && !imageName.isEmpty()) {
                    int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                    if (resId != 0) {
                        variable.imageView.setImageResource(resId);
                    } else {
                        variable.imageView.setImageResource(R.drawable.image_bangal); // fallback
                    }
                }

                // কার্ড ক্লিক করলে Text-to-Speech চালানো
                variable.materialCardView.setOnClickListener(view1 -> {
                    String speakText = variable.text.getText().toString().trim();
                    if (!speakText.isEmpty()) {
                        boolean spoken = textToSpeechHelper.speak(speakText);
                        if (!spoken) {  Custom_Tost.show(Page_Activity.this,"language not supported");
                        }
                    }
                });
            }



            else if (holder instanceof VAGITABLE_ViewHolder) {
                VAGITABLE_ViewHolder variable = (VAGITABLE_ViewHolder) holder;
                variable.text.setText(map.get("Bangla"));

                // ইমেজ সেট করা
                String imageName = map.get("image");
                if (imageName != null && !imageName.isEmpty()) {
                    int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                    if (resId != 0) {
                        variable.imageView.setImageResource(resId);
                    } else {
                        variable.imageView.setImageResource(R.drawable.image_bangal); // fallback
                    }
                }

                // কার্ড ক্লিক করলে Text-to-Speech চালানো
                variable.materialCardView.setOnClickListener(view1 -> {
                    String speakText = variable.text.getText().toString().trim();
                    if (!speakText.isEmpty()) {
                        boolean spoken = textToSpeechHelper.speak(speakText);
                        if (!spoken) {  Custom_Tost.show(Page_Activity.this,"language not supported");
                        }
                    }
                });
            }



            else if (holder instanceof FISH_ViewHolder) {
                FISH_ViewHolder variable = (FISH_ViewHolder) holder;
                variable.text.setText(map.get("Bangla"));

                // ইমেজ সেট করা
                String imageName = map.get("image");
                if (imageName != null && !imageName.isEmpty()) {
                    int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                    if (resId != 0) {
                        variable.imageView.setImageResource(resId);
                    } else {
                        variable.imageView.setImageResource(R.drawable.image_bangal); // fallback
                    }
                }

                // কার্ড ক্লিক করলে Text-to-Speech চালানো
                variable.materialCardView.setOnClickListener(view1 -> {
                    String speakText = variable.text.getText().toString().trim();
                    if (!speakText.isEmpty()) {
                        boolean spoken = textToSpeechHelper.speak(speakText);
                        if (!spoken) {  Custom_Tost.show(Page_Activity.this,"language not supported");
                        }
                    }
                });
            }




            else if (holder instanceof ANIMAL_ViewHolder) {
                ANIMAL_ViewHolder variable = (ANIMAL_ViewHolder) holder;
                variable.text.setText(map.get("Bangla"));

                // ইমেজ সেট করা
                String imageName = map.get("image");
                if (imageName != null && !imageName.isEmpty()) {
                    int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                    if (resId != 0) {
                        variable.imageView.setImageResource(resId);
                    } else {
                        variable.imageView.setImageResource(R.drawable.image_bangal); // fallback
                    }
                }

                // কার্ড ক্লিক করলে Text-to-Speech চালানো
                variable.materialCardView.setOnClickListener(view1 -> {
                    String speakText = variable.text.getText().toString().trim();
                    if (!speakText.isEmpty()) {
                        boolean spoken = textToSpeechHelper.speak(speakText);
                        if (!spoken) {  Custom_Tost.show(Page_Activity.this,"language not supported");
                        }
                    }
                });
            }




            else if (holder instanceof ORGAN_ViewHolder) {
                ORGAN_ViewHolder variable = (ORGAN_ViewHolder) holder;
                variable.text.setText(map.get("Bangla"));

                // ইমেজ সেট করা
                String imageName = map.get("image");
                if (imageName != null && !imageName.isEmpty()) {
                    int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                    if (resId != 0) {
                        variable.imageView.setImageResource(resId);
                    } else {
                        variable.imageView.setImageResource(R.drawable.image_bangal); // fallback
                    }
                }

                // কার্ড ক্লিক করলে Text-to-Speech চালানো
                variable.materialCardView.setOnClickListener(view1 -> {
                    String speakText = variable.text.getText().toString().trim();
                    if (!speakText.isEmpty()) {
                        boolean spoken = textToSpeechHelper.speak(speakText);
                        if (!spoken) {  Custom_Tost.show(Page_Activity.this,"language not supported");
                        }
                    }
                });
            }



            else if (holder instanceof STORY_ViewHolder) {
                STORY_ViewHolder variable = (STORY_ViewHolder) holder;
                variable.subect.setText(map.get("subject"));
                variable.main_text.setText(map.get("গল্প"));
            }


            else if (holder instanceof POYMES_ViewHolder) {
                POYMES_ViewHolder variable = (POYMES_ViewHolder) holder;
                variable.subect.setText(map.get("subject"));
                variable.main_text.setText(map.get("কবিতা"));
            }


            else if (holder instanceof GOOD_HAVID_ViewHolder) {
                GOOD_HAVID_ViewHolder variable = (GOOD_HAVID_ViewHolder) holder;
                variable.subect.setText(map.get("subject"));
                variable.main_text.setText(map.get("ভালো অভ্যাস"));
            }



        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }


    private void loadBanner() {
        if (adContainerView == null) return;

        AdView adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.Banner_AD_ID));

        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        // Adaptive Banner Size (পর্দার মাপ অনুযায়ী)
//        AdSize adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, 360);

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
    // ------------------ Data Loader -------------------
    private void informationLoad() {
        if (PageName == null) {
            Toast.makeText(this, "PageName not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (PageName.contains("স্বরবর্ণ")) {
            arrayList = loadData.SORBORNO();

        } else if (PageName.contains("ব্যঞ্জনবর্ণ")) {
            arrayList = loadData.BENJORBORNO();

        } else if (PageName.contains("ইংরেজী বর্ণমালা")) {
            arrayList = loadData.ENGLISH_ALFABET();}

        else if (PageName.contains("আরবি হরফ")) {
            arrayList = loadData.ARBY_HARAF();}

        else if (PageName.contains("সংখ্যা")) {
            arrayList = loadData.SONKHA();

        } else if (PageName.contains("বাংলা মাস")) {
            arrayList = loadData.BANGLA_MASH();

        } else if (PageName.contains("বাংলা সপ্তাহ")) {
            arrayList = loadData.BANGLA_SOPTAH();

        } else if (PageName.contains("ইংরেজি মাস")) {
            arrayList = loadData.ENGLISH_MONTH();

        } else if (PageName.contains("ইংরেজি সপ্তাহ")) {
            arrayList = loadData.ENGLISH_WIKE();

        } else if (PageName.contains("যানবাহন")) {
            arrayList = loadData.VAICOLE();

        } else if (PageName.contains("পাখি")) {
            arrayList = loadData.BIRD();

        } else if (PageName.contains("ফুল")) {
            arrayList = loadData.FLOWEW();

        } else if (PageName.contains("ফল")) {
            arrayList = loadData.FROUT();

        } else if (PageName.contains("সবজি")) {
            arrayList = loadData.VAGITABLE();

        } else if (PageName.contains("মাছ")) {
            arrayList = loadData.FISH();

        } else if (PageName.contains("জীবজন্তু")) {
            arrayList = loadData.ANIMAL();

        } else if (PageName.contains("অঙ্গ-প্রত্যঙ্গ")) {
            arrayList = loadData.ORGAN();

        } else if (PageName.contains("গল্প")) {
            arrayList = loadData.STORY();

        } else if (PageName.contains("কবিতা")) {
            arrayList = loadData.POYMES();

        } else if (PageName.contains("ভালো অভ্যাস")) {
            arrayList = loadData.GOOD_HAVID();
        }
    }
}
