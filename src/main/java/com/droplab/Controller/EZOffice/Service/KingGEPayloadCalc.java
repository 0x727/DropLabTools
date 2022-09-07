package com.droplab.Controller.EZOffice.Service;


import java.nio.charset.StandardCharsets;

import static com.droplab.Controller.EZOffice.Common.Utils.getSpace;

/**
 * 金格组件自定义payload计算长度
 */
public class KingGEPayloadCalc {
    public KingGEPayloadCalc() {
    }

    public String getPayload(String body,String file){
        if (!body.equals("") && !file.equals("")) {
            byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);
            int bodySize = bodyBytes.length;
            int fileSize = file.length();

            StringBuilder headString = new StringBuilder();
            headString.append("DBSTEP V3.0    "); //0，14
            headString.append(" "); //15
            String strBodySize = String.valueOf(bodySize+2);
            String strFileSize = String.valueOf(fileSize);
            headString.append(String.format("%s%s", strBodySize, getSpace(15 - strBodySize.length()))); //16-30
            headString.append(" "); //31
            headString.append(String.format("%s%s", "0", getSpace(14))); //32-46
            headString.append(" "); //47
            headString.append(String.format("%s%s", strFileSize, getSpace(15 - strFileSize.length()))); //48-62
            headString.append(" "); //63

            byte[] HeadBytes = headString.toString().getBytes(StandardCharsets.UTF_8);
            byte[] payloadBytes = new byte[bodySize+HeadBytes.length];
            for (int i = 0,j=0; i < payloadBytes.length; i++) {
                if(i < HeadBytes.length){
                    payloadBytes[i]=HeadBytes[i];
                }else {
                    payloadBytes[i]=bodyBytes[j];
                    j++;
                }
            }
            StringBuilder payload = new StringBuilder();
            payload.append(new String(payloadBytes));
            payload.append("\r\n");
            payload.append(file);
            return payload.toString();
        }
        return "";
    }
}
