package com.droplab.Controller.Seeyon.Common;

import com.droplab.Utils.HttpUtils;
import org.jsoup.Connection;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class PortalDesignerManager {
    private final String contentUrl = "/ajax.do";
    private final String arguments ="[0,\"%s\",\"%s\"]";

    private static PortalDesignerManager portalDesignerManager = null;

    public static PortalDesignerManager instance() {
        if (portalDesignerManager == null) {
            portalDesignerManager = new PortalDesignerManager();
        }
        return portalDesignerManager;
    }

    public boolean portalDesignerManager(String url, String fileid, Map headers){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String createDate = simpleDateFormat.format(new Date());
            HashMap paramMap = new HashMap();
            paramMap.put("method","ajaxAction");
            paramMap.put("managerName","portalDesignerManager");
            paramMap.put("managerMethod","uploadPageLayoutAttachment");
            paramMap.put("arguments",String.format(arguments,createDate,fileid));

            Connection.Response post = HttpUtils.post(headers, url + contentUrl, paramMap);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private Date clearTime(Date date) {
        GregorianCalendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(date == null ? new Date() : date);
        startCalendar.set(11, 0);
        startCalendar.set(12, 0);
        startCalendar.set(13, 0);
        startCalendar.set(14, 0);
        return startCalendar.getTime();
    }
}
