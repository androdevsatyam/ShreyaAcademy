package com.shreya_scademy.app.ui.help;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shreya_scademy.app.R;
import com.shreya_scademy.app.databinding.ActivityHelpDetailBinding;
import com.shreya_scademy.app.ui.base.BaseActivity;
import com.shreya_scademy.app.ui.batch.HelpResponseModel;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelpDetailActivity extends BaseActivity {

    private ActivityHelpDetailBinding binding;
    private HelpResponseModel.HelpModel helpModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHelpDetailBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getIntentData();
        init();
        bindData();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void bindData() {
        binding.tvTitle.setText(helpModel.getTitle());

        binding.webViewData.setWebChromeClient(new WebChromeClient());
        binding.webViewData.getSettings().setLoadsImagesAutomatically(true);
        binding.webViewData.getSettings().setJavaScriptEnabled(true);
        binding.webViewData.getSettings().setAllowFileAccess(true);
        binding.webViewData.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        binding.webViewData.getSettings().setMediaPlaybackRequiresUserGesture(true);
        binding.webViewData.getSettings().setDomStorageEnabled(true);
        binding.webViewData.getSettings().setAppCacheEnabled(true);
//        String data_html = "<!DOCTYPE html><html> <head> <meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"target-densitydpi=high-dpi\" /> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> <link rel=\"stylesheet\" media=\"screen and (-webkit-device-pixel-ratio:1.5)\" href=\"hdpi.css\" /></head> <body style=\"background:#E7EFF2;margin:0 0 0 0; padding:0 0 0 0;\"> "+ helpModel.getDescription() + " </body> </html> ";
        binding.webViewData.loadData(helpModel.getDescription(), "text/html", null);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null && intent.getExtras().containsKey("bundle")) {
            Bundle bundle = intent.getExtras().getBundle("bundle");
            if (bundle != null && bundle.containsKey("data")) {
                helpModel = bundle.getParcelable("data");
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    void init() {
        binding.header.tvHeader.setText(R.string.help);
        binding.header.ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}
