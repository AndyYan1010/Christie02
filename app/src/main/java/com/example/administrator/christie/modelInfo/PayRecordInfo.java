package com.example.administrator.christie.modelInfo;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/10 14:27
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class PayRecordInfo {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * paycode : 1
         * amount : 20
         * device_name : 设备1
         * device_address : 江苏省
         * time : 2018-05-10 14:08:33
         */

        private int    paycode;
        private double amount;
        private String device_name;
        private String device_address;
        private String time;

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
