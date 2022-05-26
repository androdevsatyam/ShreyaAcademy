package com.shreya_scademy.app.ui.syllabus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.shreya_scademy.app.R;
import com.shreya_scademy.app.model.modellogin.ModelLogin;
import com.shreya_scademy.app.ui.base.BaseActivity;
import com.shreya_scademy.app.utils.AppConsts;
import com.shreya_scademy.app.utils.sharedpref.SharedPref;
import com.shreya_scademy.app.utils.widgets.CustomTextExtraBold;

import java.util.ArrayList;

public class ActivitySyllabus extends BaseActivity {
    ImageView ivBack,imgNoSyl;
    CustomTextExtraBold tvHeader;
    Context mContext;
    RecyclerView rvListSyllabus;
    SharedPref sharedPref;
    ModelLogin modelLogin;
    ArrayList<ModelSyllabus.Data.SubjectData> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);
        mContext = ActivitySyllabus.this;
        sharedPref = SharedPref.getInstance(mContext);
        modelLogin = sharedPref.getUser(AppConsts.STUDENT_DATA);

        initial();
    }

    void initial() {
        ivBack = findViewById(R.id.ivBack);
        imgNoSyl = findViewById(R.id.imgNoSyl);
        rvListSyllabus = findViewById(R.id.rvListSyllabus);
        tvHeader = findViewById(R.id.tvHeader);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvHeader.setText(getResources().getString(R.string.Syllabus));
        apiSyllabus();


    }

    void apiSyllabus() {


        AndroidNetworking.post(AppConsts.BASE_URL + AppConsts.API_SYLLABUS_DATA)
                .addBodyParameter(AppConsts.BATCH_ID,modelLogin.getStudentData().getBatchId())
                .build().getAsObject(ModelSyllabus.class, new ParsedRequestListener<ModelSyllabus>() {
            @Override
            public void onResponse(ModelSyllabus response) {
                Toast.makeText(mContext,""+response.getMsg(), Toast.LENGTH_SHORT).show();
               if(response.status.equalsIgnoreCase("true")){
                   listData = new ArrayList<>();
                   listData=response.getData().getBatchSubject();
                   rvListSyllabus.setLayoutManager(new LinearLayoutManager(mContext));
                   AdapterSyllabus homeAdapter = new AdapterSyllabus(mContext, listData);
                   rvListSyllabus.setAdapter(homeAdapter);
               }else{
                   imgNoSyl.setVisibility(View.VISIBLE);
               }
            }

            @SuppressLint("ResourceType")
            @Override
            public void onError(ANError anError) {
                Toast.makeText(mContext,mContext.getResources().getString(R.id.tryAgainWhenServerError),
                        Toast.LENGTH_SHORT).show();

            }
        });

    }
}