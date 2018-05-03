package com.example.administrator.christie.modelInfo;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/3 15:54
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class GoodsListInfo {

    private List<ArrBean> arr;

    public List<ArrBean> getArr() {
        return arr;
    }

    public void setArr(List<ArrBean> arr) {
        this.arr = arr;
    }

    public static class ArrBean {
        /**
         * good_price : 1
         * good_name : 1
         * good_pic : 1
         * good_introduce : 1
         */

        private int good_price;
        private String good_name;
        private String good_pic;
        private String good_introduce;

        public int getGood_price() {
            return good_price;
        }

        public void setGood_price(int good_price) {
            this.good_price = good_price;
        }

        public String getGood_name() {
            return good_name;
        }

        public void setGood_name(String good_name) {
            this.good_name = good_name;
        }

        public String getGood_pic() {
            return good_pic;
        }

        public void setGood_pic(String good_pic) {
            this.good_pic = good_pic;
        }

        public String getGood_introduce() {
            return good_introduce;
        }

        public void setGood_introduce(String good_introduce) {
            this.good_introduce = good_introduce;
        }
    }
}
