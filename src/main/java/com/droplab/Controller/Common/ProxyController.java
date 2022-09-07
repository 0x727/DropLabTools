package com.droplab.Controller.Common;

import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.InfoUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;


@Controller
@RequestMapping("/common")
public class ProxyController {

    @RequestMapping(value = "/proxyset", method = RequestMethod.POST)
    public String proxySet(Model model, @RequestParam boolean flag,@RequestParam String type, @RequestParam String host,
                           @RequestParam String port, @RequestParam(required = false) String username,
                           @RequestParam(required = false) String password){
        if (flag){
            if (!host.equals("") && !port.equals("") && !type.equals("")){
                InfoUtils.openProxy=true;
                InfoUtils.ProxyHost=host;
                InfoUtils.ProxyPort= Integer.parseInt(port);
                InfoUtils.ProxyType=type;
                if (!username.equals("") && !password.equals("")){
                    InfoUtils.ProxyUsername=username;
                    InfoUtils.ProxyPassword=password;
                }
                model= CommonUtils.modelSet(new HashMap<>(),model);
                return "index";
            }else {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("proxyset","Host or port is not null or set error");
                model=CommonUtils.modelSet(hashMap,model);
                return "proxy";
            }
        }else {
            if (InfoUtils.openProxy){ //关闭代理
                InfoUtils.openProxy=false;
            }
        }
        return "index";
    }

    @RequestMapping(value = "/proxy",method = RequestMethod.GET)
    public String proxy(Model model){
        model= CommonUtils.modelSet(new HashMap<>(),model);
        return "proxy";
    }
}
