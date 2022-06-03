package com.shreya_scademy.app.ui.multibatch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.shreya_scademy.app.R;
import com.shreya_scademy.app.model.modellogin.ModelLogin;
import com.shreya_scademy.app.ui.base.BaseActivity;
import com.shreya_scademy.app.ui.batch.AdapterCatPage;
import com.shreya_scademy.app.ui.batch.ModelCatSubCat;
import com.shreya_scademy.app.ui.contact.ContactActivity;
import com.shreya_scademy.app.ui.home.ActivityHome;
import com.shreya_scademy.app.ui.settingdashboard.ActivitySettingDashboard;
import com.shreya_scademy.app.utils.AppConsts;
import com.shreya_scademy.app.utils.ProjectUtils;
import com.shreya_scademy.app.utils.sharedpref.SharedPref;

import java.util.ArrayList;
import java.util.Locale;


public class ActivityMultiBatchHome extends BaseActivity {
    RecyclerView rlBatchRecomm;
    Context context;
    ImageView noResult;
    EditText searchBarEditText;
    ModelLogin modelLogin;
    SharedPref sharedPref;
    TextView settings, myBatch;
    ArrayList<ModelCatSubCat.batchData> catSubList = new ArrayList<>();
    AdapterCatPage adapterCat;
    boolean isLoading = false;
    int pageStart = 0, pageEnd = 3;
    String searchTag = "";

    private ImageButton imgBtnContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = ActivityMultiBatchHome.this;
        sharedPref = SharedPref.getInstance(context);
        modelLogin = sharedPref.getUser(AppConsts.STUDENT_DATA);
        if (!ProjectUtils.checkConnection(context)) {
            Toast.makeText(context, getResources().getString(R.string.NoInternetConnection), Toast.LENGTH_SHORT).show();
        }
        if (modelLogin != null) {
            if (modelLogin.getStudentData() != null) {
                if (modelLogin.getStudentData().getLanguageName().equalsIgnoreCase("arabic")) {
                    //manually set Directions
                    getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                    Configuration configuration = getResources().getConfiguration();
                    configuration.setLayoutDirection(new Locale("fa"));
                    getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
                    String languageToLoad = "ar"; // your language
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config,
                            getBaseContext().getResources().getDisplayMetrics());
                }

               else if (modelLogin.getStudentData().getLanguageName().equalsIgnoreCase("french")) {
                    String languageToLoad = "fr"; // your language
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config,
                            getBaseContext().getResources().getDisplayMetrics());


                }
               else if (modelLogin.getStudentData().getLanguageName().equalsIgnoreCase("english")) {
                    String languageToLoad = "en"; // you     gbr language
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config,
                            getBaseContext().getResources().getDisplayMetrics());
                }

            }
        }
        setContentView(R.layout.activity_multi_batch);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initial();
    }

    private void exitAppDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(getResources().getString(R.string.Are_you_sure_you_want_to_close_this_app))
                .setCancelable(false)
                .setTitle(getResources().getString(R.string.app_name))
                .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


        AlertDialog alertDialog = builder.create();


        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                if (modelLogin.getStudentData().getLanguageName().equalsIgnoreCase("arabic")) {
                    alertDialog.findViewById(android.R.id.message).setTextDirection(View.TEXT_DIRECTION_RTL);
                }

                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            }
        });
        alertDialog.show();


    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        exitAppDialog();
    }

    private void initial() {
        noResult = (ImageView) findViewById(R.id.noResult);
        searchBarEditText = findViewById(R.id.searchBarEditText);

        searchBarEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    searchTag = s.toString();
                    catSubList = new ArrayList<>();

                    getBatches();
                }
                if (s.length() <= 0) {
                    pageStart = 0;
                    pageEnd = 3;
                    searchTag = "";
                    isLoading = false;
                    getBatches();

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rlBatchRecomm = findViewById(R.id.rlBatchRecomm);
        settings = findViewById(R.id.settings);
        myBatch = findViewById(R.id.myBatch);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivitySettingDashboard.class);
                startActivity(intent);
            }
        });
        myBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityHome.class);
                startActivity(intent);
            }
        });

        imgBtnContact = findViewById(R.id.imgBtnContact);
        imgBtnContact.setOnClickListener(view -> startActivity(new Intent(context, ContactActivity.class)));


        getBatches();
    }

    void getBatches() {
        if (!searchTag.isEmpty()) {
            pageStart = 0;
            pageEnd = 100;
        }
        AndroidNetworking.get(AppConsts.BASE_URL + AppConsts.API_GET_BATCH_FEE)
                .addPathParameter(AppConsts.START, "" + pageStart)
                .addPathParameter(AppConsts.LENGTH, "" + pageEnd)
                .addPathParameter(AppConsts.LIMIT, "3")
                .addPathParameter(AppConsts.SEARCH, searchTag)
                .addPathParameter(AppConsts.STUDENT_ID, "" + modelLogin.getStudentData().getStudentId())
                .build(
                ).getAsObject(ModelCatSubCat.class, new ParsedRequestListener<ModelCatSubCat>() {
            @Override
            public void onResponse(ModelCatSubCat response) {
                if (response.getStatus().equalsIgnoreCase("true")) {
                    if (pageStart == 0) {
                        catSubList = response.getBatchData();
                        initAdapter();
                        //initScrollListener();
                        if (catSubList.size() < 1) {
                            noResult.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (searchTag.isEmpty()) {
                            catSubList.addAll(response.getBatchData());
                            adapterCat.notifyDataSetChanged();
                            isLoading = false;
                        } else {
                            catSubList = response.getBatchData();
                            initAdapter();
                            // initScrollListener();
                        }
                    }


                } else {
                    noResult.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(ANError anError) {
                Toast.makeText(context, getResources().getString(R.string.Try_again), Toast.LENGTH_SHORT).show();
                ProjectUtils.pauseProgressDialog();

            }
        });
    }

    private void initAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        rlBatchRecomm.setLayoutManager(linearLayoutManager);

        adapterCat = new AdapterCatPage(catSubList, getApplicationContext(), "" + modelLogin.getStudentData().getStudentId());
        rlBatchRecomm.setAdapter(adapterCat);

    }

    private void initScrollListener() {
        rlBatchRecomm.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == catSubList.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        catSubList.add(null);
        adapterCat.notifyItemInserted(catSubList.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                catSubList.remove(catSubList.size() - 1);
                int scrollPosition = catSubList.size();
                adapterCat.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 3;
                pageStart = currentSize - 1;
                pageEnd = nextLimit;

                getBatches();
                while (currentSize - 1 < nextLimit) {

                    currentSize++;
                }


            }
        }, 2000);


    }


}

