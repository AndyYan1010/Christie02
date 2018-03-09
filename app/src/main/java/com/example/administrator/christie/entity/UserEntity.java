package com.example.administrator.christie.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/9 0009.
 */

public class UserEntity {
    private String id;
    private String fmobile;
    private String fname;
    private String fpassword;
    private String fcheck;
    private List<String> functionlist;
    private List<String> applylist;

    public String getFmobile() {
        return fmobile;
    }

    public void setFmobile(String fmobile) {
        this.fmobile = fmobile;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFpassword() {
        return fpassword;
    }

    public void setFpassword(String fpassword) {
        this.fpassword = fpassword;
    }

    public List<String> getFunctionlist() {
        return functionlist;
    }

    public void setFunctionlist(List<String> functionlist) {
        this.functionlist = functionlist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFcheck() {
        return fcheck;
    }

    public void setFcheck(String fcheck) {
        this.fcheck = fcheck;
    }

    public List<String> getApplylist() {
        return applylist;
    }

    public void setApplylist(List<String> applylist) {
        this.applylist = applylist;
    }
}
