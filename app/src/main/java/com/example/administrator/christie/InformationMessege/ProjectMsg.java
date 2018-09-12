package com.example.administrator.christie.InformationMessege;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/27 13:27
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class ProjectMsg {
    /**
     * id : 4028f39d62bd7fc60162bd82a7660058
     * project_name : A公司
     */

    private String id;
    private String project_name;
    private String detail_name;
    /**
     * toNext : 0
     */

    private String toNext;
    /**
     * type : 1
     */

    private String type;
    /**
     * rssi : -70
     */

    private int    rssi;//蓝牙信号强度

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getDetail_name() {
        return detail_name;
    }

    public void setDetail_name(String detail_name) {
        this.detail_name = detail_name;
    }

    public String getToNext() {
        return toNext;
    }

    public void setToNext(String toNext) {
        this.toNext = toNext;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
}
