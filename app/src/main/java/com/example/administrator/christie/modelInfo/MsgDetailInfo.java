package com.example.administrator.christie.modelInfo;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/15 10:53
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class MsgDetailInfo {

    /**
     * detail : {"meeting_status":"0","ftype":"1","meeting_user":"1","meeting_name":"1","create_date":"2018-05-14 10:36:20","meeting_content":"1"}
     * message : 消息查阅成功
     * code : 1
     */

    private DetailBean detail;
    private String message;
    private int    code;

    public DetailBean getDetail() {
        return detail;
    }

    public void setDetail(DetailBean detail) {
        this.detail = detail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class DetailBean {
        /**
         * meeting_status : 0
         * ftype : 1
         * meeting_user : 1
         * meeting_name : 1
         * create_date : 2018-05-14 10:36:20
         * meeting_content : 1
         */

        private String meeting_status;
        private String ftype;
        private String meeting_user;
        private String meeting_name;
        private String create_date;
        private String meeting_content;

        public String getMeeting_status() {
            return meeting_status;
        }

        public void setMeeting_status(String meeting_status) {
            this.meeting_status = meeting_status;
        }

        public String getFtype() {
            return ftype;
        }

        public void setFtype(String ftype) {
            this.ftype = ftype;
        }

        public String getMeeting_user() {
            return meeting_user;
        }

        public void setMeeting_user(String meeting_user) {
            this.meeting_user = meeting_user;
        }

        public String getMeeting_name() {
            return meeting_name;
        }

        public void setMeeting_name(String meeting_name) {
            this.meeting_name = meeting_name;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }

        public String getMeeting_content() {
            return meeting_content;
        }

        public void setMeeting_content(String meeting_content) {
            this.meeting_content = meeting_content;
        }
    }
}
