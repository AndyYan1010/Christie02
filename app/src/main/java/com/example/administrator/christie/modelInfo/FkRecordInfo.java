package com.example.administrator.christie.modelInfo;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/10 10:25
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class FkRecordInfo {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * fname : 张三
         * flength : 0.5小时
         * fdate : 2018-08-05 00:00:00
         * fname1 : A小区
         * freason : 来访事由
         * project_name : C公司
         * fmobile : 18036215618
         */

        private String fname;
        private String flength;
        private String fdate;
        private String fname1;
        private String freason;
        private String project_name;
        private String fmobile;

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getFlength() {
            return flength;
        }

        public void setFlength(String flength) {
            this.flength = flength;
        }

        public String getFdate() {
            return fdate;
        }

        public void setFdate(String fdate) {
            this.fdate = fdate;
        }

        public String getFname1() {
            return fname1;
        }

        public void setFname1(String fname1) {
            this.fname1 = fname1;
        }

        public String getFreason() {
            return freason;
        }

        public void setFreason(String freason) {
            this.freason = freason;
        }

        public String getProject_name() {
            return project_name;
        }

        public void setProject_name(String project_name) {
            this.project_name = project_name;
        }

        public String getFmobile() {
            return fmobile;
        }

        public void setFmobile(String fmobile) {
            this.fmobile = fmobile;
        }
    }
}
