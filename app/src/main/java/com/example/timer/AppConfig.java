package com.example.timer;

public class AppConfig {

    private String serverurl = null;

    public AppConfig(){
        this.serverurl = "https://test.docloud.site/";
    }

    public String getServerurl(){
        return this.serverurl;
    }
}
