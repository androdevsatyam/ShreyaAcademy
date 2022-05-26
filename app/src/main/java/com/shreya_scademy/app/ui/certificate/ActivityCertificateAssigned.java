package com.shreya_scademy.app.ui.certificate;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.shreya_scademy.app.R;
import com.shreya_scademy.app.model.modelCertificate.ModelCertificateAssign;
import com.shreya_scademy.app.model.modellogin.ModelLogin;
import com.shreya_scademy.app.ui.base.BaseActivity;
import com.shreya_scademy.app.ui.home.ActivityHome;
import com.shreya_scademy.app.utils.AppConsts;
import com.shreya_scademy.app.utils.ProjectUtils;
import com.shreya_scademy.app.utils.sharedpref.SharedPref;
import com.shreya_scademy.app.utils.widgets.CustomSmallText;
import com.shreya_scademy.app.utils.widgets.CustomTextExtraBold;

import java.util.ArrayList;
import java.util.List;

public class ActivityCertificateAssigned extends BaseActivity {
    Context mContext;

    ImageView ivBack;
    ImageView noResultTv;
    CustomTextExtraBold tvHeader;
    SharedPref sharedPref;
    ModelLogin modelLogin;
    RecyclerView rvBatchList;
    ArrayList<ModelCertificateAssign.Data> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate_assign);
        mContext = ActivityCertificateAssigned.this;
        sharedPref = SharedPref.getInstance(mContext);
        modelLogin = sharedPref.getUser(AppConsts.STUDENT_DATA);
        ivBack = findViewById(R.id.ivBack);
        tvHeader = (CustomTextExtraBold) findViewById(R.id.tvHeader);
        tvHeader.setText(getResources().getString(R.string.Certificate));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            initial();
        }
    }

    private void requestPermission() {

        Dexter.withActivity(ActivityCertificateAssigned.this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {


                            initial();

                        } else {
                            Toast.makeText(mContext, getResources().getString(R.string.Please_allow_permissions), Toast.LENGTH_SHORT).show();
                        }


                        if (report.isAnyPermissionPermanentlyDenied()) {


                            openSettingsDialog();
                        }
                    }


                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(mContext, getResources().getString(R.string.ErrorOccurred), Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();

    }

    private void openSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(getResources().getString(R.string.Please_allow_permissions));
        builder.setMessage(getResources().getString(R.string.This_app_needs_permission));
        builder.setPositiveButton(getResources().getString(R.string.GOTO_SETTINGS), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    void initial() {

        noResultTv = findViewById(R.id.noResultTv);
        rvBatchList = findViewById(R.id.rvBatchList);
        rvBatchList.setLayoutManager(new LinearLayoutManager(mContext));


        apiCertificate();


    }

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra(AppConsts.IS_PUSH)) {
            startActivity(new Intent(getApplicationContext(), ActivityHome.class));
            finish();

        } else {
            super.onBackPressed();
        }
    }


    void apiCertificate() {

        ProjectUtils.showProgressDialog(mContext, false, getResources().getString(R.string.Loading___));

        AndroidNetworking.post(AppConsts.BASE_URL + AppConsts.API_CERTIFICATE)
                .addBodyParameter(AppConsts.STUDENT_ID, modelLogin.getStudentData().getStudentId())
                .build()
                .getAsObject(ModelCertificateAssign.class, new ParsedRequestListener<ModelCertificateAssign>() {
                    @Override
                    public void onResponse(ModelCertificateAssign response) {
                        arrayList = new ArrayList<>();
                        if (response.getStatus().equalsIgnoreCase("true")) {
                            arrayList = response.getData();

                            ProjectUtils.pauseProgressDialog();
                            noResultTv.setVisibility(View.GONE);
                            AdapterCustom adapterCustom = new AdapterCustom(arrayList);
                            rvBatchList.setAdapter(adapterCustom);

                        } else {
                            ProjectUtils.pauseProgressDialog();
                            noResultTv.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                    }
                });
    }

    class AdapterCustom extends RecyclerView.Adapter<AdapterCustom.Myholder> {
        ArrayList<ModelCertificateAssign.Data> listBatch;
        String tag = "";
        View view;

        AdapterCustom(ArrayList<ModelCertificateAssign.Data> list) {
            this.listBatch = list;
            this.tag = tag;
        }

        @NonNull
        @Override
        public AdapterCustom.Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            view = LayoutInflater.from(mContext).inflate(R.layout.multibatch_list, parent, false);
            return new AdapterCustom.Myholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterCustom.Myholder holder, int position) {


            holder.layoutBatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listBatch.get(position).isAssign()) {
                        mContext.startActivity(new Intent(mContext, ActivityCertificate.class)
                                .putExtra("FileName", "" + listBatch.get(position).getFilename())
                                .putExtra("FileUrl", "" + listBatch.get(position).getFilesUrl()));
                    }
                }
            });
            holder.ivArrow.setVisibility(View.GONE);
            if (listBatch.get(position).isAssign()) {
                holder.inactive.setText("Show");
            } else {
                holder.inactive.setText("Not Assigned");
            }
            holder.batchName.setText("" + listBatch.get(position).getBatch_name());


        }

        @Override
        public int getItemCount() {
            return listBatch.size();
        }

        class Myholder extends RecyclerView.ViewHolder {
            CustomSmallText batchName;
            RelativeLayout layoutBatch;
            TextView inactive;
            ImageView ivArrow;

            public Myholder(@NonNull View itemView) {
                super(itemView);

                batchName = itemView.findViewById(R.id.batchName);
                layoutBatch = itemView.findViewById(R.id.layoutBatch);
                inactive = itemView.findViewById(R.id.inactive);
                ivArrow = itemView.findViewById(R.id.ivArrow);

            }
        }
    }
}