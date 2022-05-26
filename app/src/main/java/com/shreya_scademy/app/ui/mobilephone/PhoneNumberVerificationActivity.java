package com.shreya_scademy.app.ui.mobilephone;

import static com.shreya_scademy.app.utils.AppConsts.IS_REGISTER;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.common.utils.UIUtils;
import com.google.gson.Gson;
import com.shreya_scademy.app.R;
import com.shreya_scademy.app.databinding.PhoneNumberVerificationActivityBinding;
import com.shreya_scademy.app.model.modellogin.ModelLogin;
import com.shreya_scademy.app.ui.multibatch.ActivityMultiBatchHome;
import com.shreya_scademy.app.utils.AppConsts;
import com.shreya_scademy.app.utils.ProjectUtils;
import com.shreya_scademy.app.utils.sharedpref.SharedPref;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PhoneNumberVerificationActivity extends AppCompatActivity {

    private PhoneNumberVerificationActivityBinding binding;
    private PhoneNumberLoginResponseModel phoneNumberLoginResponseModel;
    private String mobile;
    private SharedPref sharedPref;
    private String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PhoneNumberVerificationActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getIntentData();
        init();
        setOTPNumber();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null && intent.getExtras().containsKey("bundle")) {
            Bundle bundle = intent.getExtras().getBundle("bundle");
            if (bundle != null) {
                if (bundle.containsKey("data")) {
                    phoneNumberLoginResponseModel = bundle.getParcelable("data");
                }
                if (bundle.containsKey("mobile")) {
                    mobile = bundle.getString("mobile");
                }
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    private void setOTPNumber() {
        binding.tvResendOTP.setText(getString(R.string.resend_otp) + "\n OTP just for demo " + phoneNumberLoginResponseModel.getCode());
    }

    void init() {

        sharedPref = SharedPref.getInstance(this);

        binding.header.ivBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.tvResendOTP.setOnClickListener(v -> {
            UIUtils.hideKeyboard(getWindow());
            invokeMobileLoginAPI(mobile);
        });
        binding.btnSubmit.setOnClickListener(v -> {
            if (otp != null && otp.length() == 6) {
                invokeOTPVerifyAPI(otp);
            }
        });

        binding.txtPinEntry2.setAnimateText(true);
        binding.txtPinEntry2.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {
                if (str.length() == 6) {
                    otp = str.toString();
                }
//                if (str.toString().equals(phoneNumberLoginResponseModel.getCode())) {
//                    Toast.makeText(PhoneNumberVerificationActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
//
//                } else {
//                    binding.txtPinEntry2.setError(true);
//                    Toast.makeText(PhoneNumberVerificationActivity.this, "FAIL", Toast.LENGTH_SHORT).show();
//                    binding.txtPinEntry2.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            binding.txtPinEntry2.setText(null);
//                        }
//                    }, 1000);
//                }
            }
        });
        binding.txtPinEntry2.requestFocus();
        binding.txtPinEntry2.setFocusable(true);
        UIUtils.showKeyboard(getWindow());
    }

    private void invokeMobileLoginAPI(String params) {
        if (ProjectUtils.checkConnection(this)) {
            ProjectUtils.showProgressDialog(this, false, getResources().getString(R.string.Loading___));
            AndroidNetworking.post(AppConsts.BASE_URL + AppConsts.MOBILE_LOGIN)
                    .addBodyParameter(AppConsts.MOBILE_NUMBER, params)
                    .setTag(AppConsts.MOBILE_LOGIN)
                    .build()
                    .getAsObject(PhoneNumberLoginResponseModel.class, new ParsedRequestListener<PhoneNumberLoginResponseModel>() {
                        @Override
                        public void onResponse(PhoneNumberLoginResponseModel response) {
                            ProjectUtils.pauseProgressDialog();
                            if (AppConsts.TRUE.equals(response.getStatus()) && response.getCode() != null &&
                                    String.valueOf(response.getCode()).length() == 6) {
                                phoneNumberLoginResponseModel = response;
                                setOTPNumber();
                            } else {
                                showToast(response.getMsg());
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            ProjectUtils.pauseProgressDialog();
                            String string = "" + getResources().getString(R.string.Try_again_server_issue);
                            showToast(string);

                        }
                    });
        } else {
            showToast(R.string.NoInternetConnection);
        }
    }

    private void showToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }

    private void showToast(String resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    private void invokeOTPVerifyAPI(String params) {
        if (ProjectUtils.checkConnection(this)) {
            ProjectUtils.showProgressDialog(this, false, getResources().getString(R.string.Loading___));
            AndroidNetworking.post(AppConsts.BASE_URL + AppConsts.VERIFY_OTP)
                    .addBodyParameter(AppConsts.MOBILE_NUMBER, mobile)
                    .addBodyParameter(AppConsts.OTP, params)
                    .setTag(AppConsts.MOBILE_LOGIN)
                    .build()
                    .getAsObject(ModelLogin.class, new ParsedRequestListener<ModelLogin>() {
                        @Override
                        public void onResponse(ModelLogin response) {

                            ProjectUtils.pauseProgressDialog();
                            if (AppConsts.TRUE.equals(response.getStatus())) {
                                showToast(response.getMsg());

                                sharedPref.setUser(AppConsts.STUDENT_DATA, response);
                                sharedPref.setBooleanValue(IS_REGISTER, true);
                                Calendar cal = Calendar.getInstance();
                                String format = new SimpleDateFormat("E, MMM d, yyyy").format(cal.getTime());
                                sharedPref.setDate("date", format);
                                startActivity(new Intent(PhoneNumberVerificationActivity.this, ActivityMultiBatchHome.class).putExtra(AppConsts.IS_SPLASH, "true"));
                            } else {
                                String string = response.getMsg();
                                showToast(string);
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            ProjectUtils.pauseProgressDialog();
                            String string = "" + getResources().getString(R.string.Try_again_server_issue);
                            showToast(string);

                        }
                    });
        } else {
            showToast(R.string.NoInternetConnection);
        }
    }
}
