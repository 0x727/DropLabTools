package com.droplab.service;

import org.jsoup.Connection;

import java.util.HashMap;

public class BugService implements BugsInfterface{
    protected HashMap<String,String> params;
    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }
    @Override
    public boolean check() {
        Object check = run("check");
        if(check != null){
            return (boolean) check;
        }else {
            return false;
        }
    }

    @Override
    public Connection.Response exploit() {
        Object exploit = run("exploit");
        if(exploit != null)
            return (Connection.Response) exploit;
        else
            return null;
    }

    @Override
    public Object run(String type) {
        return null;
    }

}
