package com.common.social;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;
import java.util.Collections;


public class FacebookSignInTask {

    private final CallbackManager callbackManager;
    private boolean isFacebookLogin = false;

    public FacebookSignInTask() {
        callbackManager = CallbackManager.Factory.create();
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public boolean isFacebookLogin() {
        return isFacebookLogin;
    }

    public void signIn(Activity activity, FacebookSignInCallBack facebookSignInCallBack) {
        isFacebookLogin = true;
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile","email")/*Collections.singletonList("public_profile","email")*/);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn) {
            LoginManager.getInstance().logOut();
        }
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        isFacebookLogin = false;
                        // App code
                        if (loginResult != null) {
                            String tokenUserId = loginResult.getAccessToken().getUserId();
//                            Log.d("DDD, "Token User Id " + tokenUserId);

                            new ProfileTracker() {
                                @Override
                                protected void onCurrentProfileChanged(Profile oldProfile, Profile profile) {
                                    stopTracking();
                                }
                            }.startTracking();
                            facebookSignInCallBack.onSigned(tokenUserId);
                        }
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        isFacebookLogin = false;
                        facebookSignInCallBack.onSigned(null);
                    }

                    @Override
                    public void onError(@NonNull FacebookException exception) {
                        isFacebookLogin = false;
                        // App code
                        facebookSignInCallBack.onSigned(null);
                    }
                });
    }


    public interface FacebookSignInCallBack {

        void onSigned(String userId);
    }
}
