package com.example.administrator.christie.modelInfo;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/8/21 14:13
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class BannerListInfo {

    private List<ArrBean> arr;

    public List<ArrBean> getArr() {
        return arr;
    }

    public void setArr(List<ArrBean> arr) {
        this.arr = arr;
    }

    public static class ArrBean {
        /**
         * newpic : http://118.89.109.106:8080/upFiles/upload/files/20180821/cc.PNG
         */

        private String newpic;

        public String getNewpic() {
            return newpic;
        }

        public void setNewpic(String newpic) {
            this.newpic = newpic;
        }
    }
}
