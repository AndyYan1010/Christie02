package com.example.administrator.christie.modelInfo;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/3 14:26
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class PersonalDataInfo {

    /**
     * arr : {"fstatus":"0","faddress":"三栋1号楼","listProject":[{"fname":"a","project_id":"4028f39d62bd7fc60162bd82a7660058","project_name":"A公司"},{"fname":"b","project_id":"4028f39d62bd7fc60162bd82a7660058","project_name":"A公司"}],"project_id":"4028f39d62bd7fc60162bd82a7660058","projectdetail_id":"4d2881e962d72b4d0162d72d6eca0001","bpm_status":"1","telephone":"18036215618","id":"f62f9ba1edb14dfbb12f1ab13adfe00d","id_pic":"22a.pig","relation":"本人"}
     */

    private ArrBean arr;

    public ArrBean getArr() {
        return arr;
    }

    public void setArr(ArrBean arr) {
        this.arr = arr;
    }

    public static class ArrBean {
        /**
         * fstatus : 0
         * faddress : 三栋1号楼
         * listProject : [{"fname":"a","project_id":"4028f39d62bd7fc60162bd82a7660058","project_name":"A公司"},{"fname":"b","project_id":"4028f39d62bd7fc60162bd82a7660058","project_name":"A公司"}]
         * project_id : 4028f39d62bd7fc60162bd82a7660058
         * projectdetail_id : 4d2881e962d72b4d0162d72d6eca0001
         * bpm_status : 1
         * telephone : 18036215618
         * id : f62f9ba1edb14dfbb12f1ab13adfe00d
         * id_pic : 22a.pig
         * relation : 本人
         */

        private String fstatus;
        private String                faddress;
        private String                project_id;
        private String                projectdetail_id;
        private String                bpm_status;
        private String                telephone;
        private String                id;
        private String                id_pic;
        private String                relation;
        private List<ListProjectBean> listProject;
        /**
         * user_name : Andy
         */

        private String user_name;

        public String getFstatus() {
            return fstatus;
        }

        public void setFstatus(String fstatus) {
            this.fstatus = fstatus;
        }

        public String getFaddress() {
            return faddress;
        }

        public void setFaddress(String faddress) {
            this.faddress = faddress;
        }

        public String getProject_id() {
            return project_id;
        }

        public void setProject_id(String project_id) {
            this.project_id = project_id;
        }

        public String getProjectdetail_id() {
            return projectdetail_id;
        }

        public void setProjectdetail_id(String projectdetail_id) {
            this.projectdetail_id = projectdetail_id;
        }

        public String getBpm_status() {
            return bpm_status;
        }

        public void setBpm_status(String bpm_status) {
            this.bpm_status = bpm_status;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getId_pic() {
            return id_pic;
        }

        public void setId_pic(String id_pic) {
            this.id_pic = id_pic;
        }

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }

        public List<ListProjectBean> getListProject() {
            return listProject;
        }

        public void setListProject(List<ListProjectBean> listProject) {
            this.listProject = listProject;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public static class ListProjectBean {
            /**
             * fname : a
             * project_id : 4028f39d62bd7fc60162bd82a7660058
             * project_name : A公司
             */

            private String fname;
            private String project_id;
            private String project_name;
            /**
             * faddress : sdadas
             * user_id : 3
             * projectdetail_id : 4d2881e962d72b4d0162d72d6eca0001
             * id : 6
             * id_pic : asdas
             * relation : asdasd
             */

            private String faddress;
            private int    user_id;
            private String projectdetail_id;
            private int    id;
            private String id_pic;
            private String relation;

            public String getFname() {
                return fname;
            }

            public void setFname(String fname) {
                this.fname = fname;
            }

            public String getProject_id() {
                return project_id;
            }

            public void setProject_id(String project_id) {
                this.project_id = project_id;
            }

            public String getProject_name() {
                return project_name;
            }

            public void setProject_name(String project_name) {
                this.project_name = project_name;
            }

            public String getFaddress() {
                return faddress;
            }

            public void setFaddress(String faddress) {
                this.faddress = faddress;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getProjectdetail_id() {
                return projectdetail_id;
            }

            public void setProjectdetail_id(String projectdetail_id) {
                this.projectdetail_id = projectdetail_id;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getId_pic() {
                return id_pic;
            }

            public void setId_pic(String id_pic) {
                this.id_pic = id_pic;
            }

            public String getRelation() {
                return relation;
            }

            public void setRelation(String relation) {
                this.relation = relation;
            }
        }
    }
}
