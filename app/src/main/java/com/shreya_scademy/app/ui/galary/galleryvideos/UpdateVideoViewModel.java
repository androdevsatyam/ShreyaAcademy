package com.shreya_scademy.app.ui.galary.galleryvideos;

/**
 * @Authoer Dharmesh
 * @Date 03-04-2022
 * <p>
 * Information
 **/
public class UpdateVideoViewModel {
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
        private String count;

        public int getViewLimit() {
            return viewLimit;
        }

        public void setViewLimit(int viewLimit) {
            this.viewLimit = viewLimit;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }
}