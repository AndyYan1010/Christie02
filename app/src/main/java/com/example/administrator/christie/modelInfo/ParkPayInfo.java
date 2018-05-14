package com.example.administrator.christie.modelInfo;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/14 13:07
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class ParkPayInfo {

    /**
     * amount : 30
     * payType : null
     * payTime : null
     * parklist : {"inTime":"2018-05-03 09:57:16","fstatus":0,"amount":30,"plateNo":"苏fhahha","id":1,"outTime":"2018-05-03 09:57:32","parkId":1,"unlock_id":3}
     * resCode : 0
     * discount : null
     * resMsg :
     */

    private double       amount;
    private Object       payType;
    private Object       payTime;
    private ParklistBean parklist;
    private int          resCode;
    private Object       discount;
    private String       resMsg;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Object getPayType() {
        return payType;
    }

    public void setPayType(Object payType) {
        this.payType = payType;
    }

    public Object getPayTime() {
        return payTime;
    }

    public void setPayTime(Object payTime) {
        this.payTime = payTime;
    }

    public ParklistBean getParklist() {
        return parklist;
    }

    public void setParklist(ParklistBean parklist) {
        this.parklist = parklist;
    }

    public int getResCode() {
        return resCode;
    }

    public void setResCode(int resCode) {
        this.resCode = resCode;
    }

    public Object getDiscount() {
        return discount;
    }

    public void setDiscount(Object discount) {
        this.discount = discount;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public static class ParklistBean {
        /**
         * inTime : 2018-05-03 09:57:16
         * fstatus : 0
         * amount : 30
         * plateNo : 苏fhahha
         * id : 1
         * outTime : 2018-05-03 09:57:32
         * parkId : 1
         * unlock_id : 3
         */

        private String inTime;
        private int    fstatus;
        private double amount;
        private String plateNo;
        private int    id;
        private String outTime;
        private int    parkId;
        private int    unlock_id;

        public String getInTime() {
            return inTime;
        }

        public void setInTime(String inTime) {
            this.inTime = inTime;
        }

        public int getFstatus() {
            return fstatus;
        }

        public void setFstatus(int fstatus) {
            this.fstatus = fstatus;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getPlateNo() {
            return plateNo;
        }

        public void setPlateNo(String plateNo) {
            this.plateNo = plateNo;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOutTime() {
            return outTime;
        }

        public void setOutTime(String outTime) {
            this.outTime = outTime;
        }

        public int getParkId() {
            return parkId;
        }

        public void setParkId(int parkId) {
            this.parkId = parkId;
        }

        public int getUnlock_id() {
            return unlock_id;
        }

        public void setUnlock_id(int unlock_id) {
            this.unlock_id = unlock_id;
        }
    }
}
