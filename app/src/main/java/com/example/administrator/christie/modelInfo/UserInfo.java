package com.example.administrator.christie.modelInfo;

import java.io.Serializable;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/28 9:52
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class UserInfo implements Serializable {
    private String phone;
    private String psw;
    private String id;
    /**
     * fstatus : 0
     */

    private boolean fstatus;

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getPsw() {
        return psw;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean getFstatus() {
        return fstatus;
    }

    public void setFstatus(boolean fstatus) {
        this.fstatus = fstatus;
    }
}
