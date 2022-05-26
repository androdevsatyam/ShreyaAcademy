package com.common.validator;

import android.util.Patterns;

/**
 * @Authoer Dharmesh
 * @Date 24-02-2022
 * <p>
 * Information
 **/
public class PhoneValidator {

    private final String phone;
    private int minLength = 10;
    private int maxLength = 10;

    public PhoneValidator(String phone) {
        this.phone = phone;
    }

    public PhoneValidator(String phone, int minLength, int maxLength) {
        this.phone = phone;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public boolean isPhoneNumberEmpty() {
        return (phone == null || phone.isEmpty());
    }

    public boolean isPhoneNumberInValid() {
        int length = phone.length();
        if (length >= minLength && length <= maxLength) {
            return !Patterns.PHONE.matcher(phone).matches();
        } else {
            return true;
        }
    }
}
