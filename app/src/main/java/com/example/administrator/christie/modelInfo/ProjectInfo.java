package com.example.administrator.christie.modelInfo;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/2 9:51
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class ProjectInfo {

    private List<ProjectlistBean> projectlist;
    private List<ProjectdetalilistBean> projectdetalilist;

    public List<ProjectlistBean> getProjectlist() {
        return projectlist;
    }

    public void setProjectlist(List<ProjectlistBean> projectlist) {
        this.projectlist = projectlist;
    }

    public List<ProjectdetalilistBean> getProjectdetalilist() {
        return projectdetalilist;
    }

    public void setProjectdetalilist(List<ProjectdetalilistBean> projectdetalilist) {
        this.projectdetalilist = projectdetalilist;
    }

    public static class ProjectlistBean {
        /**
         * id : 4028f39d62bd7fc60162bd82a7660058
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


    public static class ProjectdetalilistBean {
        /**
         * fname : a
         * id : 4d2881e962d72b4d0162d72d6eca0001
         */

        private String fname;
        private String id;

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
