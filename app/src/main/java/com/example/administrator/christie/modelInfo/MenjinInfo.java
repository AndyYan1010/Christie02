package com.example.administrator.christie.modelInfo;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/10 10:15
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class MenjinInfo {

    /**
     * device_name : 设备1
     * device_address : 江苏省
     * time : 2018-05-09 10:41:31
     */

    private String device_name;
    private String device_address;
    private String time;
    /**
     * paycode : 1
     * amount : 20
     */

    private int    paycode;
    private double amount;

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDevice_address() {
        return device_address;
    }

    public void setDevice_address(String device_address) {
        this.device_address = device_address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPaycode() {
        return paycode;
    }

    public void setPaycode(int paycode) {
        this.paycode = paycode;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    private List<ArrBean> arr;

    public List<ArrBean> getArr() {
        return arr;
    }

    public void setArr(List<ArrBean> arr) {
        this.arr = arr;
    }

    public static class ArrBean {
        /**
         * device_name : A小区
         * device_address : 会议室后门
         * time : 2018-09-04 15:28:09
         */

        private String device_name;
        private String device_address;
        private String time;

        public String getDevice_name() {
            return device_name;
        }

        public void setDevice_name(String device_name) {
            this.device_name = device_name;
        }

        public String getDevice_address() {
            return device_address;
        }

        public void setDevice_address(String device_address) {
            this.device_address = device_address;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
