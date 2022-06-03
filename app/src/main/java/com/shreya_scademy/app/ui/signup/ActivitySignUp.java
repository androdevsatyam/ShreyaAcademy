package com.shreya_scademy.app.ui.signup;

import static com.shreya_scademy.app.utils.AppConsts.IS_REGISTER;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.common.social.FacebookSignInTask;
import com.common.social.GoogleSignInTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.shreya_scademy.app.R;
import com.shreya_scademy.app.databinding.ActivitySignBinding;
import com.shreya_scademy.app.model.modellogin.ModelLogin;
import com.shreya_scademy.app.ui.base.BaseActivity;
import com.shreya_scademy.app.ui.batch.ActivityBatch;
import com.shreya_scademy.app.ui.batch.ModelCatSubCat;
import com.shreya_scademy.app.ui.login.ActivityLogin;
import com.shreya_scademy.app.ui.mobilephone.PhoneNumberLoginActivity;
import com.shreya_scademy.app.ui.paymentGateway.ActivityPaymentGateway;
import com.shreya_scademy.app.utils.AppConsts;
import com.shreya_scademy.app.utils.ProjectUtils;
import com.shreya_scademy.app.utils.sharedpref.SharedPref;
import com.shreya_scademy.app.utils.widgets.CustomEditText;
import com.shreya_scademy.app.utils.widgets.CustomTextSemiBold;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class ActivitySignUp extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ActivitySignUp";
    Context context;
    CustomEditText etUserName, etUserMobile, etUserEmail;
    RelativeLayout btnSignup;
    ModelLogin modelLogin;
    SharedPref sharedPref;
    String versionCode;
    CustomTextSemiBold tvMessage;
    String amount, batchId, paymentType;
    static ModelCatSubCat.batchData.SubCategory.BatchData batchData;
    LinearLayout loginTv, tvMove;
    private ActivitySignBinding binding;
    private GoogleSignInTask googleSignInTask;
    private FacebookSignInTask facebookSignInTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        context = ActivitySignUp.this;
        sharedPref = SharedPref.getInstance(context);
        modelLogin = sharedPref.getUser(AppConsts.STUDENT_DATA);
        googleSignInTask = new GoogleSignInTask(this);
        facebookSignInTask = new FacebookSignInTask();
        init();
    }

    void init() {
        etUserName = findViewById(R.id.etUserName);
        loginTv = findViewById(R.id.loginTv);
        loginTv.setOnClickListener(this);
        tvMove = findViewById(R.id.tvMove);
        tvMove.setOnClickListener(this);
        tvMessage = findViewById(R.id.tvMessage);
        etUserMobile = findViewById(R.id.etUserMobile);
        etUserEmail = findViewById(R.id.etUserEmail);
        btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(this);

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = String.valueOf(pInfo.versionCode);
        }catch (Exception e){
            Log.d("ACtivity",""+e.getMessage());
        }

        if (getIntent().hasExtra("amount")) {
            amount = getIntent().getStringExtra("amount");
            batchId = getIntent().getStringExtra("BatchId");
            paymentType = getIntent().getStringExtra("paymentType");
        }
        if (getIntent().hasExtra("data")) {
            batchData = (ModelCatSubCat.batchData.SubCategory.BatchData) getIntent().getSerializableExtra("data");
        }

        binding.btnFacebookSignIn.setOnClickListener(v -> {
            facebookSignInTask.signIn(ActivitySignUp.this, new FacebookSignInTask.FacebookSignInCallBack() {
                @Override
                public void onSigned(String userId) {
                    if (userId == null) {
                        Toast.makeText(ActivitySignUp.this, "Facebook login error", Toast.LENGTH_SHORT).show();
                    } else {
                      /*  FirebaseInstanceId.getInstance().getInstanceId()
                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                    @Override
                                    public void onComplete(Task<InstanceIdResult> task) {
                                        if (!task.isSuccessful()) {
                                            return;
                                        }
*/
                                        if (getIntent().hasExtra("login")) {
                                            if (getIntent().getStringExtra("login").equalsIgnoreCase("Withoutbatch")) {


                                                ProjectUtils.showProgressDialog(context, true, getResources().getString(R.string.Loading___));
                                                AndroidNetworking.post(AppConsts.BASE_URL + AppConsts.API_STUDENT_REGISTRATION)
                                                        .addBodyParameter(AppConsts.NAME, "" + etUserName.getText().toString())
                                                        .addBodyParameter(AppConsts.EMAIL, "" + etUserEmail.getText().toString())
                                                        .addBodyParameter(AppConsts.MOBILE, "" + etUserMobile.getText().toString())
                                                        .addBodyParameter(AppConsts.TOKEN, "" + userId)
                                                        .addBodyParameter(AppConsts.VERSION_CODE, "" + versionCode)
                                                        .build()
                                                        .getAsObject(ModelLogin.class, new ParsedRequestListener<ModelLogin>() {
                                                            @Override
                                                            public void onResponse(ModelLogin response) {
                                                                if (response.getStatus().equalsIgnoreCase("true")) {

                                                                    sharedPref.setUser(AppConsts.STUDENT_DATA, response);
                                                                    sharedPref.setBooleanValue(IS_REGISTER, true);
                                                                    modelLogin = sharedPref.getUser(AppConsts.STUDENT_DATA);
                                                                    Intent intent = new Intent(context, ActivityPaymentGateway.class).putExtra("login", "Withoutbatch");
                                                                    intent.putExtra("name", "" + etUserName.getText().toString());
                                                                    intent.putExtra("email", "" + etUserEmail.getText().toString());
                                                                    intent.putExtra("mobile", "" + etUserMobile.getText().toString());
                                                                    intent.putExtra("token", "" + userId);
                                                                    intent.putExtra("versionCode", "" + versionCode);
                                                                    startActivity(intent);
                                                                    ProjectUtils.pauseProgressDialog();

                                                                } else {
                                                                    ProjectUtils.pauseProgressDialog();
                                                                    Toast.makeText(context, "" + response.getMsg(), Toast.LENGTH_SHORT).show();
                                                                }

                                                            }

                                                            @Override
                                                            public void onError(ANError anError) {
                                                                ProjectUtils.pauseProgressDialog();
                                                                Log.v("saloni123", "saloni  " + anError);
                                                            }
                                                        });
                                            }
                                        } else {
                                            Log.v("saloni1234", "==================goes right");
                                            apiSignUp(userId);
                                        }
//                                    }
//                                });
                        Toast.makeText(ActivitySignUp.this, "User id " + userId, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        binding.btnGoogleSignIn.setOnClickListener(v -> {
            Intent intent = googleSignInTask.signIn(account -> {
                if (account != null) {
                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(Task<InstanceIdResult> task) {
                                    if (!task.isSuccessful()) {
                                        return;
                                    }
                                    if (getIntent().hasExtra("login")) {
                                        if (getIntent().getStringExtra("login").equalsIgnoreCase("Withoutbatch")) {
                                            ProjectUtils.showProgressDialog(context, true, getResources().getString(R.string.Loading___));

                                            AndroidNetworking.post(AppConsts.BASE_URL + AppConsts.API_GOOGLE_LOGIN)
                                                    .addBodyParameter(AppConsts.EMAIL, "" + account.getEmail())
                                                    .addBodyParameter(AppConsts.FIRSTNAME, "" +account.getGivenName())
                                                    .addBodyParameter(AppConsts.LASTNAME, "")
                                                    .addBodyParameter(AppConsts.NAME, "" +account.getDisplayName())
                                                    .addBodyParameter(AppConsts.GENDER, "" )
                                                    .addBodyParameter(AppConsts.PICTURE, "" + account.getPhotoUrl())
                                                    .addBodyParameter(AppConsts.LOCATION, "")
                                                    .build()
                                                    .getAsObject(ModelLogin.class, new ParsedRequestListener<ModelLogin>() {
                                                        @Override
                                                        public void onResponse(ModelLogin response) {
                                                            if (response.getStatus().equalsIgnoreCase("true")) {
                                                                sharedPref.setUser(AppConsts.STUDENT_DATA, response);
                                                                sharedPref.setBooleanValue(IS_REGISTER, true);
                                                                modelLogin = sharedPref.getUser(AppConsts.STUDENT_DATA);
                                                                Intent intent = new Intent(context, ActivityPaymentGateway.class).putExtra("login", "Withoutbatch");
                                                                intent.putExtra("name", "" + response.getStudentData().getFullName().isEmpty());
                                                                intent.putExtra("email", "" + response.getStudentData().getUserEmail());
                                                                intent.putExtra("mobile", "" + response.getStudentData().getMobile());
                                                                intent.putExtra("token", "" + task.getResult().getToken());
                                                                intent.putExtra("versionCode", "" + versionCode);
                                                                startActivity(intent);
                                                                ProjectUtils.pauseProgressDialog();
                                                                Toast.makeText(context, "" + response.getMsg(), Toast.LENGTH_LONG).show();


                                                            } else {
                                                                googleSignInTask.signOut();
                                                                ProjectUtils.pauseProgressDialog();
                                                                Toast.makeText(context, "" + response.getMsg()+"\nUse Another Email or Login", Toast.LENGTH_LONG).show();
                                                            }

                                                        }

                                                        @Override
                                                        public void onError(ANError anError) {
                                                            ProjectUtils.pauseProgressDialog();
                                                            Log.v("saloni123", "saloni  " + anError);
                                                        }
                                                    });


                                          /*  AndroidNetworking.post(AppConsts.BASE_URL + AppConsts.API_STUDENT_REGISTRATION)
                                                    .addBodyParameter(AppConsts.NAME, "" +account.getDisplayName())
                                                    .addBodyParameter(AppConsts.EMAIL, "" + account.getEmail())
                                                    .addBodyParameter(AppConsts.MOBILE, "" + "0000000000")
                                                    .addBodyParameter(AppConsts.TOKEN, "" + task.getResult().getToken())
                                                    .addBodyParameter(AppConsts.VERSION_CODE, "" + versionCode)
                                                    .build()
                                                    .getAsObject(ModelLogin.class, new ParsedRequestListener<ModelLogin>() {
                                                        @Override
                                                        public void onResponse(ModelLogin response) {
                                                            if (response.getStatus().equalsIgnoreCase("true")) {
                                                                sharedPref.setUser(AppConsts.STUDENT_DATA, response);
                                                                sharedPref.setBooleanValue(IS_REGISTER, true);
                                                                modelLogin = sharedPref.getUser(AppConsts.STUDENT_DATA);
                                                                Intent intent = new Intent(context, ActivityPaymentGateway.class).putExtra("login", "Withoutbatch");
                                                                intent.putExtra("name", "" + account.getDisplayName());
                                                                intent.putExtra("email", "" + account.getEmail());
                                                                intent.putExtra("mobile", "" + "0000000000");
                                                                intent.putExtra("token", "" + task.getResult().getToken());
                                                                intent.putExtra("versionCode", "" + versionCode);
                                                                startActivity(intent);
                                                                ProjectUtils.pauseProgressDialog();

                                                            } else {
                                                                googleSignInTask.signOut();
                                                                ProjectUtils.pauseProgressDialog();
                                                                Toast.makeText(context, "" + response.getMsg()+"\nUse Another Email or Login", Toast.LENGTH_LONG).show();
                                                            }

                                                        }

                                                        @Override
                                                        public void onError(ANError anError) {
                                                            ProjectUtils.pauseProgressDialog();
                                                            Log.v("saloni123", "saloni  " + anError);
                                                        }
                                                    });
                                        */}
                                    } else {
                                        Log.v("saloni1234", "==================goes right");
                                        apiSignUp(task.getResult().getToken());
                                    }
                                }
                            });
                } else {
                    Toast.makeText(ActivitySignUp.this, "Unable to use Google", Toast.LENGTH_SHORT).show();
                }
            });
            mGetContent.launch(intent);
        });
        binding.btnPhoneSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(ActivitySignUp.this, PhoneNumberLoginActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Activity callback API.
     */
    private final ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    switch (result.getResultCode()) {
                        case Activity.RESULT_OK:
                            Intent intent = result.getData();
                            googleSignInTask.signInIntent(intent);

                            break;
                        case Activity.RESULT_CANCELED:
                            break;
                    }
                }
            });

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    void apiSignUp(String token) {
        ProjectUtils.showProgressDialog(context, false, getResources().getString(R.string.Loading___));

        AndroidNetworking.post(AppConsts.BASE_URL + AppConsts.API_CHECK_BATCH)
                .addBodyParameter(AppConsts.EMAIL, "" + etUserEmail.getText().toString()).build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ProjectUtils.pauseProgressDialog();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getString(AppConsts.ISEMAILEXIST).equalsIgnoreCase(AppConsts.TRUE)) {
                                Intent intent = new Intent(context, ActivityPaymentGateway.class);
                                intent.putExtra("versionCode", "" + versionCode);
                                intent.putExtra("paymentType", "" + paymentType);
                                intent.putExtra("token", "" + token);
                                intent.putExtra("amount", "" + amount);
                                intent.putExtra("BatchId", "" + batchId);
                                intent.putExtra("name", "" + etUserName.getText().toString());
                                intent.putExtra("email", "" + etUserEmail.getText().toString());
                                intent.putExtra("mobile", "" + etUserMobile.getText().toString());
                                intent.putExtra("data", batchData);

                                startActivity(intent);
                            } else {
                                Toast.makeText(context, getResources().getString(R.string.EmailExist), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        Toast.makeText(context, getResources().getString(R.string.Try_again_server_issue), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvMove:
                Intent intent = new Intent(context, ActivityBatch.class);
                startActivity(intent);
                break;
            case R.id.loginTv:
                startActivity(new Intent(context, ActivityLogin.class));
                break;
            case R.id.btnSignup:
                if (ProjectUtils.checkConnection(context)) {
                    if (etUserName.getText().toString().isEmpty()) {
                        etUserName.setError(getResources().getString(R.string.Please_Enter_name));
                        etUserName.requestFocus();
                    } else {
                        if (etUserEmail.getText().toString().isEmpty()) {
                            etUserEmail.setError("" + getResources().getString(R.string.EnterEmail));
                            etUserEmail.requestFocus();

                        } else {
                            if (etUserMobile.getText().toString().isEmpty()) {
                                etUserMobile.setError("" + getResources().getString(R.string.EnterMobile));
                                etUserMobile.requestFocus();
                            } else {
                                if (isValidEmail(etUserEmail.getText().toString())) {
                                    if (etUserMobile.getText().toString().length() > 6) {
                                        FirebaseInstanceId.getInstance().getInstanceId()
                                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                    @Override
                                                    public void onComplete(Task<InstanceIdResult> task) {
                                                        if (!task.isSuccessful()) {
                                                            return;
                                                        }

                                                        if (getIntent().hasExtra("login")) {
                                                            if (getIntent().getStringExtra("login").equalsIgnoreCase("Withoutbatch")) {


                                                                ProjectUtils.showProgressDialog(context, true, getResources().getString(R.string.Loading___));
                                                                AndroidNetworking.post(AppConsts.BASE_URL + AppConsts.API_STUDENT_REGISTRATION)
                                                                        .addBodyParameter(AppConsts.NAME, "" + etUserName.getText().toString())
                                                                        .addBodyParameter(AppConsts.EMAIL, "" + etUserEmail.getText().toString())
                                                                        .addBodyParameter(AppConsts.MOBILE, "" + etUserMobile.getText().toString())
                                                                        .addBodyParameter(AppConsts.TOKEN, "" + task.getResult().getToken())
                                                                        .addBodyParameter(AppConsts.VERSION_CODE, "" + versionCode)
                                                                        .build()
                                                                        .getAsObject(ModelLogin.class, new ParsedRequestListener<ModelLogin>() {
                                                                            @Override
                                                                            public void onResponse(ModelLogin response) {
                                                                                if (response.getStatus().equalsIgnoreCase("true")) {

                                                                                    sharedPref.setUser(AppConsts.STUDENT_DATA, response);
                                                                                    sharedPref.setBooleanValue(IS_REGISTER, true);
                                                                                    modelLogin = sharedPref.getUser(AppConsts.STUDENT_DATA);
                                                                                    Intent intent = new Intent(context, ActivityPaymentGateway.class).putExtra("login", "Withoutbatch");
                                                                                    intent.putExtra("name", "" + etUserName.getText().toString());
                                                                                    intent.putExtra("email", "" + etUserEmail.getText().toString());
                                                                                    intent.putExtra("mobile", "" + etUserMobile.getText().toString());
                                                                                    intent.putExtra("token", "" + task.getResult().getToken());
                                                                                    intent.putExtra("versionCode", "" + versionCode);
                                                                                    startActivity(intent);
                                                                                    ProjectUtils.pauseProgressDialog();

                                                                                } else {
                                                                                    ProjectUtils.pauseProgressDialog();
                                                                                    Toast.makeText(context, "" + response.getMsg(), Toast.LENGTH_SHORT).show();
                                                                                }

                                                                            }

                                                                            @Override
                                                                            public void onError(ANError anError) {
                                                                                ProjectUtils.pauseProgressDialog();
                                                                                Log.v("saloni123", "saloni  " + anError);
                                                                            }
                                                                        });


                                                            }
                                                        } else {
                                                            Log.v("saloni1234", "==================goes right");
                                                            apiSignUp(task.getResult().getToken());
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(context, "" + getResources().getString(R.string.Entervalidnumber), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    etUserEmail.setError("" + getResources().getString(R.string.InvalidEmail));
                                    etUserEmail.requestFocus();
                                }


                            }
                        }
                    }

                } else {

                    Toast.makeText(context, getResources().getString(R.string.NoInternetConnection), Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (facebookSignInTask.isFacebookLogin()) {
            facebookSignInTask.getCallbackManager().onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }
}