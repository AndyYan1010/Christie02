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
         * good_price : 20
         * newpic : http://118.89.109.106:8080/upFiles/upload/files/20180821/a.jpg
         * id : 8a8080866517061501651dc3cdb6007d
         * good_name : 糖醋里脊
         * good_pic : upload/files\20180821\a.jpg,
         * good_introduce : 肉
         */

        private int good_price;
        private String newpic;
        private String id;
        private String good_name;
        private String good_pic;
        private String good_introduce;
        /**
         * type : 0
         */

        private String type;
        /**
         * like : 0
         * dislike : 0
         */

        private int like;
        private int dislike;

        public int getGood_price() {
            return good_price;
        }

        public void setGood_price(int good_price) {
            this.good_price = good_price;
        }

        public String getNewpic() {
            return newpic;
        }

        public void setNewpic(String newpic) {
            this.newpic = newpic;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getLike() {
            return like;
        }

        public void setLike(int like) {
            this.like = like;
        }

        public int getDislike() {
            return dislike;
        }

        public void setDislike(int dislike) {
            this.dislike = dislike;
        }
    }

    //    private List<ArrBean> arr;
//
//    public List<ArrBean> getArr() {
//        return arr;
//    }
//
//    public void setArr(List<ArrBean> arr) {
//        this.arr = arr;
//    }
//
//    public static class ArrBean {
//        /**
//         * good_price : 1
//         * good_name : 1
//         * good_pic : 1
//         * good_introduce : 1
//         */
//
//        private double good_price;
//        private String good_name;
//        private String good_pic;
//        private String good_introduce;
//        /**
//         * id : 4d2881e863239e9f016323a03eb80007
//         */
//
//        private String id;
//
//        public double getGood_price() {
//            return good_price;
//        }
//
//        public void setGood_price(double good_price) {
//            this.good_price = good_price;
//        }
//
//        public String getGood_name() {
//            return good_name;
//        }
//
//        public void setGood_name(String good_name) {
//            this.good_name = good_name;
//        }
//
//        public String getGood_pic() {
//            return good_pic;
//        }
//
//        public void setGood_pic(String good_pic) {
//            this.good_pic = good_pic;
//        }
//
//        public String getGood_introduce() {
//            return good_introduce;
//        }
//
//        public void setGood_introduce(String good_introduce) {
//            this.good_introduce = good_introduce;
//        }
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//    }
}
