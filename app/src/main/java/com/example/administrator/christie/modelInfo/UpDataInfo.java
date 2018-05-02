package com.example.administrator.christie.modelInfo;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/2 13:54
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class UpDataInfo {

    /**
     * result : 1
     * fileName : 1525240375156.jpeg
     */

    private int result;
    private String fileName;
    /**
     * validateCode : 269240
     * valid : true
     */

    private String validateCode;
    private boolean valid;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
