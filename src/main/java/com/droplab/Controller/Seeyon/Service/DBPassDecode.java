package com.droplab.Controller.Seeyon.Service;

import com.droplab.Utils.ResponseUtils;
import com.droplab.service.BugService;
import com.seeyon.v3x.dbpool.util.TextEncoder;

import java.util.Iterator;

/**
 * 致远oa 数据库密码解密
 */
public class DBPassDecode extends BugService {
    @Override
    public Object run(String type) {
        try {
            if (params.size() > 0) {
                String password = null;  //数据库密码
                Iterator<String> iterator = params.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (key.equals("password")) {
                        password = params.get(key);
                    }
                }
                if(!password.equals("") && password !=null){
                    String decode = TextEncoder.decode(password);
                    ResponseUtils responseUtils = new ResponseUtils();
                    responseUtils.setMessage(decode);
                    return responseUtils;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
