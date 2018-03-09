package com.example.administrator.christie.entity;

import java.io.Serializable;

public class Plate implements Serializable{
    private String id;
    private String user_id;
    private String fisdefault;
    private String fmodel;
    private String fcolor;
    private String fplateno;
    private String fmobile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFisdefault() {
        return fisdefault;
    }

    public void setFisdefault(String fisdefault) {
        this.fisdefault = fisdefault;
    }

    public String getFmodel() {
        return fmodel;
    }

    public void setFmodel(String fmodel) {
        this.fmodel = fmodel;
    }

    public String getFcolor() {
        return fcolor;
    }

    public void setFcolor(String fcolor) {
        this.fcolor = fcolor;
    }

    public String getFplateno() {
        return fplateno;
    }

    public void setFplateno(String fplateno) {
        this.fplateno = fplateno;
    }

    public String getFmobile() {
        return fmobile;
    }

    public void setFmobile(String fmobile) {
        this.fmobile = fmobile;
    }
}
