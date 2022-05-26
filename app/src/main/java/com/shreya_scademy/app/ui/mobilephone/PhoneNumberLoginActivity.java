package com.shreya_scademy.app.ui.mobilephone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.common.utils.UIUtils;
import com.common.validator.PhoneValidator;
import com.google.gson.Gson;
import com.shreya_scademy.app.R;
import com.shreya_scademy.app.databinding.PhoneNumberActivityBinding;
import com.shreya_scademy.app.model.modellogin.ModelLogin;
import com.shreya_scademy.app.utils.AppConsts;
import com.shreya_scademy.app.utils.ProjectUtils;

public class PhoneNumberLoginActivity extends AppCompatActivity {

    private PhoneNumberActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PhoneNumberActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        init();
    }

    private void setEditTextFocus(EditText editText) {
        editText.requestFocus();
    }

    void init() {
        binding.tvEnterYourPhone.setTextSize(24);
        binding.tvCountryName.setTextSize(16);
        binding.header.ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.btnSubmit.setOnClickListener(v -> {
            String phoneNumber = binding.edtName.getText().toString();
            PhoneValidator phoneValidator = new PhoneValidator(phoneNumber, 7, 12);
            if (phoneValidator.isPhoneNumberEmpty()) {
                showToast(R.string.required_fields_are_missing);
                setEditTextFocus(binding.edtName);
            } else if (phoneValidator.isPhoneNumberInValid()) {
                showToast(R.string.please_enter_a_valid_phone_number);
                setEditTextFocus(binding.edtName);
            } else {
                UIUtils.hideKeyboard(getWindow());
                invokeMobileLoginAPI(phoneNumber);
            }
        });

//        binding.tvCountryName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CountryPicker picker = CountryPicker.newInstance("Select Country", Theme.LIGHT);  // dialog title and theme
//                picker.setListener((name, code, dialCode, flagDrawableResID) -> {
//
//                    // Implement your code here
//                    binding.imgFlag.setImageResource(flagDrawableResID);
//                    binding.tvCountryName.setText(name);
//                    Log.d("DDD", "Code " + code);
//                    Log.d("DDD", "Dial Code " + dialCode);
//                    binding.tvCode.setText(dialCode);
//                    picker.dismiss();
//                });
//                picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
//            }
//        });

        binding.imgFlag.setImageResource(R.drawable.flag_np);
        binding.tvCountryName.setText(R.string.nepal);
        binding.tvCode.setText("+977");
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
                                Intent intent = new Intent(PhoneNumberLoginActivity.this, PhoneNumberVerificationActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("data", response);
                                bundle.putString("mobile",params);
                                intent.putExtra("bundle", bundle);
                                startActivity(intent);
                            } else {
                                showToast(response.getMsg());
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            String string = "" + getResources().getString(R.string.Try_again_server_issue);
                            showToast(string);
                            ProjectUtils.pauseProgressDialog();

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
}
