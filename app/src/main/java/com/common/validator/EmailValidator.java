package com.common.validator;

import android.util.Patterns;

/**
 * @Authoer Dharmesh
 * @Date 24-02-2022
 * <p>
 * Information
 **/
public class EmailValidator {

    private final String email;

    public EmailValidator(String email) {
        this.email = email;
    }

    public boolean isEmailEmpty() {
        return (email == null || email.isEmpty());
    }

    public boolean isEmailInValid(){
        return !Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
