package com.common.social;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.shreya_scademy.app.R;

/**
 * @Author Dharmesh
 * @Date 18-03-2022
 * <p>
 * Information
 **/
public class GoogleSignInTask {

    // Facebook Hash key code
    private final GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInCallBack googleSignInCallBack;

    public GoogleSignInTask(Activity context) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public Intent signIn(GoogleSignInCallBack googleSignInCallBack) {
        this.googleSignInCallBack = googleSignInCallBack;
        return mGoogleSignInClient.getSignInIntent();
    }

    public void signInIntent(Intent data){
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        handleSignInResult(task);
    }

    public void signOut(){
        mGoogleSignInClient.signOut();
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
//            updateUI(account);
//            Log.d("DDD", account.getEmail());
//            Toast.makeText(context, "Google signed successfull", Toast.LENGTH_SHORT).show();
            googleSignInCallBack.onSigned(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
           Log.w(String.valueOf(this), "signInResult:failed code=" + e.getStatusCode());
            googleSignInCallBack.onSigned(null);
        }
    }


    public interface GoogleSignInCallBack {
        void onSigned(GoogleSignInAccount account);
    }

}
