package ksea.rabbitmq.model;

import java.io.Serializable;


public class Deptinfo implements Serializable {

    private String deptid;
    private String deptname;


    public Deptinfo() {
    }

    public Deptinfo(String deptid, String deptname) {
        this.deptid = deptid;
        this.deptname = deptname;
    }


    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    @Override
    public String toString() {
        return "Deptinfo{" +
                "deptid='" + deptid + '\'' +
                ", deptname='" + deptname + '\'' +
                '}';
    }
}
