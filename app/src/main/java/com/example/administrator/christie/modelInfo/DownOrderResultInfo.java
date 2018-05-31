package com.example.administrator.christie.modelInfo;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/31 11:23
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class DownOrderResultInfo {

    /**
     * message : 用户下单成功
     * id : 5
     * orderinfo : charset=utf-8&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%220.01%22%2C%22subject%22%3A%22%22%2C%22body%22%3A%22%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95%E6%95%B0%E6%8D%AE%22%2C%22out_trade_no%22%3A%22053111221621383%22%7D&method=alipay.trade.app.pay&app_id=2015061000118245&sign_type=RSA2&version=1.0&timestamp=2016-07-29+16%3A55%3A53&sign=HboMSXY7Raz3gDsKN%2Fa%2Fqv94iKhMGxNd0rGHaXzAS2YirsL10xf7WlnHTW%2BAsy%2BFZ5iPmJbO58Hto1%2BtJMDtcsZFTX9Z1GUXNIK78EvZuadVqSPn94zqG8Ra94PHmxVUjXwdfrAWn1FAdNZx%2BiYpQ6TOJsfPaIAnqcbvLK2kVPs%3D
     * result : 2
     */

    private String message;
    private int    id;
    private String orderinfo;
    private int    result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderinfo() {
        return orderinfo;
    }

    public void setOrderinfo(String orderinfo) {
        this.orderinfo = orderinfo;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
