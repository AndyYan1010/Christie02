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
     * inTime : 2018-08-27 10:00:00
     * fstatus :
     * amount : 4
     * resCode : 0
     * resMsg : 正常
     * outTime : 2018-08-27 11:00:00
     */

    private String inTime;
    private String fstatus;
    private double amount;
    private int    resCode;
    private String resMsg;
    private String outTime;
    /**
     * parkname : 杭州湾小区停车场
     * fresstime : 60
     */

    private String parkname;
    private String fresstime;

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getFstatus() {
        return fstatus;
    }

    public void setFstatus(String fstatus) {
        this.fstatus = fstatus;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getResCode() {
        return resCode;
    }

    public void setResCode(int resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getParkname() {
        return parkname;
    }

    public void setParkname(String parkname) {
        this.parkname = parkname;
    }

    public String getFresstime() {
        return fresstime;
    }

    public void setFresstime(String fresstime) {
        this.fresstime = fresstime;
    }
}
