package com.shreya_scademy.app.ui.contact;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.common.utils.UIUtils;
import com.common.validator.EmailValidator;
import com.common.validator.NameValidator;
import com.common.validator.PhoneValidator;
import com.shreya_scademy.app.R;
import com.shreya_scademy.app.databinding.ActivityContactBinding;
import com.shreya_scademy.app.model.modellogin.ModelLogin;
import com.shreya_scademy.app.ui.base.BaseActivity;
import com.shreya_scademy.app.utils.AppConsts;
import com.shreya_scademy.app.utils.ProjectUtils;

public class ContactActivity extends AppCompatActivity {

    private ActivityContactBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        init();
    }

    void init() {
        binding.header.tvHeader.setText(R.string.contact);
        binding.btnSubmit.setOnClickListener(v -> {
            performValidation();

        });
        binding.header.ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void setEditTextFocus(EditText editText){
        editText.requestFocus();
    }
    
    private void performValidation() {
        String name = binding.edtName.getText().toString();
        String email = binding.edtEmail.getText().toString();
        String subject = binding.edtSubject.getText().toString();
        String phoneNumber = binding.edtPhoneNumber.getText().toString();
        String message = binding.edtMessage.getText().toString();
        NameValidator nameValidator = new NameValidator(name);
        if (nameValidator.isNameEmpty()) {
            showToast(R.string.required_fields_are_missing);
            setEditTextFocus(binding.edtName);
        } else {
            EmailValidator emailValidator = new EmailValidator(email);
            if (emailValidator.isEmailEmpty()) {
                showToast(R.string.required_fields_are_missing);
                setEditTextFocus(binding.edtEmail);
            } else if (emailValidator.isEmailInValid()) {
                showToast(R.string.please_enter_a_valid_email);
                setEditTextFocus(binding.edtEmail);
            } else {
//                nameValidator = new NameValidator(subject);
                nameValidator.setName(subject);
                if (nameValidator.isSubjectEmpty()) {
                    showToast(R.string.required_fields_are_missing);
                    setEditTextFocus(binding.edtSubject);
                } else {
                    PhoneValidator phoneValidator = new PhoneValidator(phoneNumber,7,12);
                    if (phoneValidator.isPhoneNumberEmpty()) {
                        showToast(R.string.required_fields_are_missing);
                        setEditTextFocus(binding.edtPhoneNumber);
                    } else if (phoneValidator.isPhoneNumberInValid()) {
                        showToast(R.string.please_enter_a_valid_phone_number);
                        setEditTextFocus(binding.edtPhoneNumber);
                    } else {
//                        nameValidator = new NameValidator(message);
                        nameValidator.setName(message);
                        if (nameValidator.isMessageEmpty()) {
                            showToast(R.string.required_fields_are_missing);
                            setEditTextFocus(binding.edtMessage);
                        } else {
                            // TODO: 12-03-2022 Call contact us api
                            UIUtils.hideKeyboard(getWindow());
                            invokeInquireAPI(name, email, subject, phoneNumber, message);
                        }
                    }
                }
            }
        }
    }

    private void resetInquiryForm() {
        binding.edtName.setText("");
        binding.edtEmail.setText("");
        binding.edtSubject.setText("");
        binding.edtPhoneNumber.setText("");
        binding.edtMessage.setText("");
        binding.edtName.requestFocus();
    }

    private void invokeInquireAPI(String... params) {
        if (ProjectUtils.checkConnection(this)) {
            ProjectUtils.showProgressDialog(this, false, getResources().getString(R.string.Loading___));
            AndroidNetworking.post(AppConsts.BASE_URL + AppConsts.ADD_INQUIRY)
                    .addBodyParameter(AppConsts.NAME, params[0])
                    .addBodyParameter(AppConsts.EMAIL, params[1])
                    .addBodyParameter(AppConsts.SUBJECT, params[2])
                    .addBodyParameter(AppConsts.MOBILE, params[3])
                    .addBodyParameter(AppConsts.MESSAGE, params[4])
                    .setTag(AppConsts.ADD_INQUIRY)
                    .build()
                    .getAsObject(ModelLogin.class, new ParsedRequestListener<ModelLogin>() {
                        @Override
                        public void onResponse(ModelLogin response) {
                            ProjectUtils.pauseProgressDialog();
                            if (AppConsts.TRUE.equals(response.getStatus())) {
                                showToast(response.getMsg());
                                resetInquiryForm();
                            } else {
                                String string = " " + response.getMsg();
                                showToast(string);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void showToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }

    private void showToast(String resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }
}


