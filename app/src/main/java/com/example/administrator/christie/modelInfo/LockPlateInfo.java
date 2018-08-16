package com.example.administrator.christie.modelInfo;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/11 15:08
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class LockPlateInfo {

    private List<ListBean> list;
    /**
     * result : fail
     */

    private String         result;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public static class ListBean {
        /**
         * fstatus : 0
         * plateno : 苏fhahha
         */

        private int fstatus;
        private String plateno;

        public int getFstatus() {
            return fstatus;
        }

        public void setFstatus(int fstatus) {
            this.fstatus = fstatus;
        }

        public String getPlateno() {
            return plateno;
        }

        public void setPlateno(String plateno) {
            this.plateno = plateno;
        }
    }
}
