package com.example.administrator.christie.modelInfo;

/**
 * @创建者 AndyYan
 * @创建时间 2019/7/12 15:18
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class GrzxInfo {

    /**
     * grzx : {"fcontent":"<p>eqweqesadczxascdw<\/p>"}
     */

    private GrzxBean grzx;

    public GrzxBean getGrzx() {
        return grzx;
    }

    public void setGrzx(GrzxBean grzx) {
        this.grzx = grzx;
    }

    public static class GrzxBean {
        /**
         * fcontent : <p>eqweqesadczxascdw</p>
         */

        private String fcontent;

        public String getFcontent() {
            return fcontent;
        }

        public void setFcontent(String fcontent) {
            this.fcontent = fcontent;
        }
    }
}
