package com.shreya_scademy.app.ui.galary.galleryvideos;

import java.util.ArrayList;
import java.util.List;

/**
 * @Authoer Dharmesh
 * @Date 03-04-2022
 * <p>
 * Information
 **/
public class ContentViewCountModel {
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
        private int viewLimit;
        private List<Datum> data = new ArrayList<Datum>();

        public int getViewLimit() {
            return viewLimit;
        }

        public void setViewLimit(int viewLimit) {
            this.viewLimit = viewLimit;
        }

        public List<Datum> getData() {
            return data;
        }

        public void setData(List<Datum> data) {
            this.data = data;
        }
    }

    public class Datum {
        private String id;
        private String videoId;
        private String userId;
        private String count;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCount() {
            if(count == null || count.isEmpty()){
                return "0";
            }
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }


    }
}
