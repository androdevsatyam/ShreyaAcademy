package com.shreya_scademy.app.ui.mobilephone;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Authoer Dharmesh
 * @Date 02-04-2022
 * <p>
 * Information
 **/
public class PhoneNumberLoginResponseModel implements Parcelable {
    private Integer code;
    private String status;
    private String msg;

    protected PhoneNumberLoginResponseModel(Parcel in) {
        if (in.readByte() == 0) {
            code = null;
        } else {
            code = in.readInt();
        }
        status = in.readString();
        msg = in.readString();
    }

    public static final Creator<PhoneNumberLoginResponseModel> CREATOR = new Creator<PhoneNumberLoginResponseModel>() {
        @Override
        public PhoneNumberLoginResponseModel createFromParcel(Parcel in) {
            return new PhoneNumberLoginResponseModel(in);
        }

        @Override
        public PhoneNumberLoginResponseModel[] newArray(int size) {
            return new PhoneNumberLoginResponseModel[size];
        }
    };

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (code == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(code);
        }
        parcel.writeString(status);
        parcel.writeString(msg);
    }
}
