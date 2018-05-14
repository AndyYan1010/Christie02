package com.example.administrator.christie.modelInfo;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/7 8:56
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class MeetingDataInfo {

    /**
     * jsonObject : [{"meeting_status":"0","sys_org_code":"A03A01","fread":0,"create_by":"55","sys_company_code":"A03","ftype":"1","project_id":"4028f39d62bd7fc60162bd82d7a1005a","bpm_status":"1","id":"4d2881e86329ac30016329b005f50005","meeting_name":"1","create_date":{"date":4,"day":5,"hours":13,"minutes":46,"month":4,"nanos":797000000,"seconds":31,"time":1525412791797,"timezoneOffset":-480,"year":118},"meeting_content":"1","create_name":"55"}]
     * message : 查找成功
     * code : 0
     */

    private String               message;
    private int                  code;
    private List<JsonObjectBean> jsonObject;

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

    public List<JsonObjectBean> getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(List<JsonObjectBean> jsonObject) {
        this.jsonObject = jsonObject;
    }

    public static class JsonObjectBean {
        /**
         * meeting_status : 0
         * sys_org_code : A03A01
         * fread : 0
         * create_by : 55
         * sys_company_code : A03
         * ftype : 1
         * project_id : 4028f39d62bd7fc60162bd82d7a1005a
         * bpm_status : 1
         * id : 4d2881e86329ac30016329b005f50005
         * meeting_name : 1
         * create_date : {"date":4,"day":5,"hours":13,"minutes":46,"month":4,"nanos":797000000,"seconds":31,"time":1525412791797,"timezoneOffset":-480,"year":118}
         * meeting_content : 1
         * create_name : 55
         */

        private String         meeting_status;
        private String         sys_org_code;
        private int            fread;
        private String         create_by;
        private String         sys_company_code;
        private String         ftype;
        private String         project_id;
        private String         bpm_status;
        private String         id;
        private String         meeting_name;
        private CreateDateBean create_date;
        private String         meeting_content;
        private String         create_name;

        public String getMeeting_status() {
            return meeting_status;
        }

        public void setMeeting_status(String meeting_status) {
            this.meeting_status = meeting_status;
        }

        public String getSys_org_code() {
            return sys_org_code;
        }

        public void setSys_org_code(String sys_org_code) {
            this.sys_org_code = sys_org_code;
        }

        public int getFread() {
            return fread;
        }

        public void setFread(int fread) {
            this.fread = fread;
        }

        public String getCreate_by() {
            return create_by;
        }

        public void setCreate_by(String create_by) {
            this.create_by = create_by;
        }

        public String getSys_company_code() {
            return sys_company_code;
        }

        public void setSys_company_code(String sys_company_code) {
            this.sys_company_code = sys_company_code;
        }

        public String getFtype() {
            return ftype;
        }

        public void setFtype(String ftype) {
            this.ftype = ftype;
        }

        public String getProject_id() {
            return project_id;
        }

        public void setProject_id(String project_id) {
            this.project_id = project_id;
        }

        public String getBpm_status() {
            return bpm_status;
        }

        public void setBpm_status(String bpm_status) {
            this.bpm_status = bpm_status;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMeeting_name() {
            return meeting_name;
        }

        public void setMeeting_name(String meeting_name) {
            this.meeting_name = meeting_name;
        }

        public CreateDateBean getCreate_date() {
            return create_date;
        }

        public void setCreate_date(CreateDateBean create_date) {
            this.create_date = create_date;
        }

        public String getMeeting_content() {
            return meeting_content;
        }

        public void setMeeting_content(String meeting_content) {
            this.meeting_content = meeting_content;
        }

        public String getCreate_name() {
            return create_name;
        }

        public void setCreate_name(String create_name) {
            this.create_name = create_name;
        }

        public static class CreateDateBean {
            /**
             * date : 4
             * day : 5
             * hours : 13
             * minutes : 46
             * month : 4
             * nanos : 797000000
             * seconds : 31
             * time : 1525412791797
             * timezoneOffset : -480
             * year : 118
             */

            private int  date;
            private int  day;
            private int  hours;
            private int  minutes;
            private int  month;
            private int  nanos;
            private int  seconds;
            private long time;
            private int  timezoneOffset;
            private int  year;

            public int getDate() {
                return date;
            }

            public void setDate(int date) {
                this.date = date;
            }

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }

            public int getHours() {
                return hours;
            }

            public void setHours(int hours) {
                this.hours = hours;
            }

            public int getMinutes() {
                return minutes;
            }

            public void setMinutes(int minutes) {
                this.minutes = minutes;
            }

            public int getMonth() {
                return month;
            }

            public void setMonth(int month) {
                this.month = month;
            }

            public int getNanos() {
                return nanos;
            }

            public void setNanos(int nanos) {
                this.nanos = nanos;
            }

            public int getSeconds() {
                return seconds;
            }

            public void setSeconds(int seconds) {
                this.seconds = seconds;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public int getTimezoneOffset() {
                return timezoneOffset;
            }

            public void setTimezoneOffset(int timezoneOffset) {
                this.timezoneOffset = timezoneOffset;
            }

            public int getYear() {
                return year;
            }

            public void setYear(int year) {
                this.year = year;
            }
        }
    }
}
