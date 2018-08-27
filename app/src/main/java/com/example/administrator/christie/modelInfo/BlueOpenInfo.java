package com.example.administrator.christie.modelInfo;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/8/23 15:55
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class BlueOpenInfo {
    private List<ArrBean> arr;

    public List<ArrBean> getArr() {
        return arr;
    }

    public void setArr(List<ArrBean> arr) {
        this.arr = arr;
    }

    public static class ArrBean {
        /**
         * fname : A小区
         * xinxi : 000000004D928CFBCEAA6C01A48911B2
         * projectdetail_id : 8a8080866517061501651d5fc139004b
         * cardno : AE4E714800000000
         */

        private String fname;
        private String xinxi;
        private String projectdetail_id;
        private String cardno;

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getXinxi() {
            return xinxi;
        }

        public void setXinxi(String xinxi) {
            this.xinxi = xinxi;
        }

        public String getProjectdetail_id() {
            return projectdetail_id;
        }

        public void setProjectdetail_id(String projectdetail_id) {
            this.projectdetail_id = projectdetail_id;
        }

        public String getCardno() {
            return cardno;
        }

        public void setCardno(String cardno) {
            this.cardno = cardno;
        }
    }
}
