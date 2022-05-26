package com.shreya_scademy.app.ui.help;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.shreya_scademy.app.R;
import com.shreya_scademy.app.databinding.ActivityHelpsBinding;
import com.shreya_scademy.app.ui.base.BaseActivity;
import com.shreya_scademy.app.ui.batch.HelpResponseModel;
import com.shreya_scademy.app.utils.AppConsts;
import com.shreya_scademy.app.utils.ItemClickListener;
import com.shreya_scademy.app.utils.ProjectUtils;

public class HelpActivity extends BaseActivity {

    private ActivityHelpsBinding binding;
    private HelpListAdapter helpListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHelpsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        init();
        initRecyclerView();
        hidePlaceholder();
        getHelps();
    }

    private void initRecyclerView() {
        helpListAdapter = new HelpListAdapter();
        helpListAdapter.setItemClickListener((o, index) -> {
            HelpResponseModel.HelpModel helpModel = (HelpResponseModel.HelpModel) o;
            Intent intent = new Intent(HelpActivity.this, HelpDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("data", helpModel);
            intent.putExtra("bundle", bundle);
            startActivity(intent);
        });
        binding.rvVideos.setLayoutManager(new LinearLayoutManager(this));
        binding.rvVideos.setAdapter(helpListAdapter);
    }

    void init() {
        binding.header.tvHeader.setText(R.string.help);
        binding.header.ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void showLoader() {
        ProjectUtils.showProgressDialog(this, false, getResources().getString(R.string.Loading___));
    }

    private void hideLoader() {
        ProjectUtils.pauseProgressDialog();
    }

    private void showPlaceholder() {
        binding.noRecordFound.setVisibility(View.VISIBLE);
    }

    private void hidePlaceholder() {
        binding.noRecordFound.setVisibility(View.GONE);
    }

    private void getHelps() {
        showLoader();
        AndroidNetworking.get(AppConsts.BASE_URL + AppConsts.GET_HELPS)
                .build().getAsObject(HelpResponseModel.class, new ParsedRequestListener<HelpResponseModel>() {
            @Override
            public void onResponse(HelpResponseModel response) {
                hideLoader();
                try {
                    if (response.getStatus() != null
                            && response.getStatus().equalsIgnoreCase("true")
                            && response.getData() != null
                            && response.getData().getData() != null
                            && !response.getData().getData().isEmpty()) {
                        hidePlaceholder();
                        helpListAdapter.setItems(response.getData().getData());
                    } else {
                        showPlaceholder();
                    }
                } catch (Exception e) {
                    showPlaceholder();
                    Toast.makeText(HelpActivity.this, getResources().getString(R.string.Try_again), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(ANError anError) {
                hideLoader();
                showPlaceholder();
                Toast.makeText(HelpActivity.this, getResources().getString(R.string.Try_again), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
