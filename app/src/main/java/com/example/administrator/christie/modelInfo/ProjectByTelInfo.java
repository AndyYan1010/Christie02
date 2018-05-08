package com.example.administrator.christie.modelInfo;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/7 10:24
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class ProjectByTelInfo {

    private List<ProjectlistBean> projectlist;

    public List<ProjectlistBean> getProjectlist() {
        return projectlist;
    }

    public void setProjectlist(List<ProjectlistBean> projectlist) {
        this.projectlist = projectlist;
    }

    public static class ProjectlistBean {
        /**
         * project_id : 4028f39d62bd7fc60162bd82a7660058
         * project_name : A公司
         */

        private String id;
        private String project_name;

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
    }
}
