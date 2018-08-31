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
         * xinxi : 000000004D928CFBCEAA6C01A48911B2
         * projectname : 克立司帝上海总部
         * projectdetail_id : 8a8080866361bafe016362143f1f0014
         * cardno : AE4E714800000000
         * lanya : [{"fname":"财务室","address2":"3","fangxiang":"1","address1":"1","id":"8a8080866589d03801658d72cb740002","name2":"4","name1":"2"},{"fname":"会议室","address2":"","fangxiang":"0","address1":"aaa","id":"40288afb657fb57d01657fb65aab0001","name2":"","name1":"会议室门"}]
         */

        private String xinxi;
        private String          projectname;
        private String          projectdetail_id;
        private String          cardno;
        private List<LanyaBean> lanya;

        public String getXinxi() {
            return xinxi;
        }

        public void setXinxi(String xinxi) {
            this.xinxi = xinxi;
        }

        public String getProjectname() {
            return projectname;
        }

        public void setProjectname(String projectname) {
            this.projectname = projectname;
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

        public List<LanyaBean> getLanya() {
            return lanya;
        }

        public void setLanya(List<LanyaBean> lanya) {
            this.lanya = lanya;
        }

        public static class LanyaBean {
            /**
             * fname : 财务室
             * address2 : 3
             * fangxiang : 1
             * address1 : 1
             * id : 8a8080866589d03801658d72cb740002
             * name2 : 4
             * name1 : 2
             */

            private String fname;
            private String address2;
            private String fangxiang;
            private String address1;
            private String id;
            private String name2;
            private String name1;

            public String getFname() {
                return fname;
            }

            public void setFname(String fname) {
                this.fname = fname;
            }

            public String getAddress2() {
                return address2;
            }

            public void setAddress2(String address2) {
                this.address2 = address2;
            }

            public String getFangxiang() {
                return fangxiang;
            }

            public void setFangxiang(String fangxiang) {
                this.fangxiang = fangxiang;
            }

            public String getAddress1() {
                return address1;
            }

            public void setAddress1(String address1) {
                this.address1 = address1;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName2() {
                return name2;
            }

            public void setName2(String name2) {
                this.name2 = name2;
            }

            public String getName1() {
                return name1;
            }

            public void setName1(String name1) {
                this.name1 = name1;
            }
        }
    }
}
