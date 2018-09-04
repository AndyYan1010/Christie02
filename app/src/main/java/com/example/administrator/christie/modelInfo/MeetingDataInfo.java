package com.example.administrator.christie.modelInfo;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/7 8:56
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class MeetingDataInfo {

    /**
     * jsonObject : [{"meeting_status":"0","sys_org_code":"A03A01","fread":0,"create_by":"55","sys_company_code":"A03","ftype":"1","project_id":"4028f39d62bd7fc60162bd82d7a1005a","bpm_status":"1","id":"4d2881e86329ac30016329b005f50005","meeting_name":"1","create_date":{"date":4,"day":5,"hours":13,"minutes":46,"month":4,"nanos":797000000,"seconds":31,"time":1525412791797,"timezoneOffset":-480,"year":118},"meeting_content":"1","create_name":"55"}]
     * message : 查找成功
     * code : 0
     */

    private String               message;
    private int                  code;
    private List<ArrBean> arr;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<ArrBean> getArr() {
        return arr;
    }

    public void setArr(List<ArrBean> arr) {
        this.arr = arr;
    }

    public static class ArrBean {
        /**
         * ftype : 2
         * id : 40288afb658ee9ba01658ef6daa4000c
         * meeting_name : 会议
         * create_date : 2018-08-31 15:51:07
         * meeting_content : <p>1.公告是行政公文的主要文种之一，它和<a target="_blank" href="https://baike.baidu.com/item/%E9%80%9A%E5%91%8A" style="color: rgb(19, 110, 194); text-decoration-line: none;">通告</a>都属于发布范围广泛的晓谕性文种。公告是向国内外宣布重要事项或者法定事项时使用的公文。适用于向国内外宣布重要事项或者法定事项。</p><p>2.公告是用于向国内外宣布重要事项或者法定事项的公文。公告主要有两种，一是宣布重要事项，如最近中国将在东海进行地对地导弹发射训练；二是宣布法定事项，如宣布某项法规或规章，宣布国家领导人<a target="_blank" href="https://baike.baidu.com/item/%E9%80%89%E4%B8%BE" style="color: rgb(19, 110, 194); text-decoration-line: none;">选举</a>结果。</p><p>&nbsp; &nbsp; &nbsp;然而，公告在实际使用中，往往偏离了《<a target="_blank" href="https://baike.baidu.com/item/%E5%9B%BD%E5%AE%B6%E8%A1%8C%E6%94%BF%E6%9C%BA%E5%85%B3%E5%85%AC%E6%96%87%E5%A4%84%E7%90%86%E5%8A%9E%E6%B3%95" style="color: rgb(19, 110, 194); text-decoration-line: none;">国家行政机关公文处理办法</a>》中的规定，各机关、单位、团体事无巨细经常使用公告。公告的庄重性特点被忽视，只注意到广泛性和周知性，以致使公告逐渐演变为“公而告之”。</p><p><br/></p>
         * fread : 0
         */

        private String ftype;
        private String id;
        private String meeting_name;
        private String create_date;
        private String meeting_content;
        private int    fread;

        public String getFtype() {
            return ftype;
        }

        public void setFtype(String ftype) {
            this.ftype = ftype;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMeeting_name() {
            return meeting_name;
        }

        public void setMeeting_name(String meeting_name) {
            this.meeting_name = meeting_name;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }

        public String getMeeting_content() {
            return meeting_content;
        }

        public void setMeeting_content(String meeting_content) {
            this.meeting_content = meeting_content;
        }

        public int getFread() {
            return fread;
        }

        public void setFread(int fread) {
            this.fread = fread;
        }
    }
}
