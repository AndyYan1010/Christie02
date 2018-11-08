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
     * fstatus : 0
     * amount : 1
     * intime : 2018-11-07 09:20:00
     * freetime : 30
     * plateNo : 浙B123456
     * resMsg  : 正常
     * outtime : 2018-11-07 17:09:29
     * parkname : 南山小区停车场
     * resCode  : 0
     */

    private String fstatus;
    private String amount;
    private String intime;
    private String freetime;
    private String plateNo;
    private String resMsg;
    private String outtime;
    private String parkname;
    private String resCode;
    /**
     * ispay : 1
     */

    private String ispay;
    /**
     * parkid : 26
     */

    private String parkid;

    public String getFstatus() {
        return fstatus;
    }

    public void setFstatus(String fstatus) {
        this.fstatus = fstatus;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getFreetime() {
        return freetime;
    }

    public void setFreetime(String freetime) {
        this.freetime = freetime;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public String getOuttime() {
        return outtime;
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }

    public String getParkname() {
        return parkname;
    }

    public void setParkname(String parkname) {
        this.parkname = parkname;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getIspay() {
        return ispay;
    }

    public void setIspay(String ispay) {
        this.ispay = ispay;
    }

    public String getParkid() {
        return parkid;
    }

    public void setParkid(String parkid) {
        this.parkid = parkid;
    }
}
