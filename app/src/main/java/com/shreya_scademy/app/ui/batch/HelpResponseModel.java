package com.shreya_scademy.app.ui.batch;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @Authoer Dharmesh
 * @Date 02-04-2022
 * <p>
 * Information
 **/


public class HelpResponseModel {
    private String status;
    private String msg;
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private List<HelpModel> data = new ArrayList<HelpModel>();

        public List<HelpModel> getData() {
            return data;
        }

        public void setData(List<HelpModel> data) {
            this.data = data;
        }
    }

    public static class HelpModel implements Parcelable {
        private String id;
        private String title;
        private String description;
        private String type;
        private String upload;
        private String image;
        private String status;

        protected HelpModel(Parcel in) {
            id = in.readString();
            title = in.readString();
            description = in.readString();
            type = in.readString();
            upload = in.readString();
            image = in.readString();
            status = in.readString();
        }

        public static final Creator<HelpModel> CREATOR = new Creator<HelpModel>() {
            @Override
            public HelpModel createFromParcel(Parcel in) {
                return new HelpModel(in);
            }

            @Override
            public HelpModel[] newArray(int size) {
                return new HelpModel[size];
            }
        };

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUpload() {
            return upload;
        }

        public void setUpload(String upload) {
            this.upload = upload;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(id);
            parcel.writeString(title);
            parcel.writeString(description);
            parcel.writeString(type);
            parcel.writeString(upload);
            parcel.writeString(image);
            parcel.writeString(status);
        }
    }

}