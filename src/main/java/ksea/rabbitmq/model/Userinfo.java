package ksea.rabbitmq.model;

import java.io.Serializable;


public class Userinfo implements Serializable {

    private String uid;

    private String username;

    private String pwd;

    public Userinfo() {
    }

    public Userinfo(String uid, String username, String pwd) {
        this.uid = uid;
        this.username = username;
        this.pwd = pwd;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "Userinfo{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}
