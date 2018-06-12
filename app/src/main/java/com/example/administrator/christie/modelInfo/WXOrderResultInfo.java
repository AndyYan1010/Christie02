package com.example.administrator.christie.modelInfo;

/**
 * @创建者 AndyYan
 * @创建时间 2018/6/12 10:49
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class WXOrderResultInfo {

    /**
     * return_code : SUCCESS
     * return_msg : OK
     * result_code : SUCCESS
     * appId : wx797b4ac4b3e272a7
     * partnerId : 1507326071
     * nonceStr : w95ewonxKHFMMMz2
     * timeStamp : 1528771655
     * sign : A880948BDA4E98581C0291404AB55210
     * trade_type : APP
     * prepayId : wx121047478958291f53d0c3bc1765704385
     */

    private String return_code;
    private String return_msg;
    private String result_code;
    private String appId;
    private String partnerId;
    private String nonceStr;
    private int   timeStamp;
    private String sign;
    private String trade_type;
    private String prepayId;

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }
}
