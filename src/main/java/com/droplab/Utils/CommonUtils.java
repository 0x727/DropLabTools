package com.droplab.Utils;

import org.springframework.ui.Model;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class CommonUtils {
    public static HashMap hashMapClone(HashMap source,HashMap destination){
        if(!source.isEmpty()){
            destination.clear();
            Iterator iterator = source.keySet().iterator();
            while (iterator.hasNext()){
                String key= (String) iterator.next();
                destination.put(key,source.get(key));
            }
            return destination;
        }
        return new HashMap();
    }

    public static Model modelSet(HashMap<String,String> hashMap,Model model){
        if(!hashMap.isEmpty()){
            Iterator<String> iterator = hashMap.keySet().iterator();
            while (iterator.hasNext()){
                String key=iterator.next();
                model.addAttribute(key,hashMap.get(key));
            }
        }
        if(InfoUtils.openProxy){  //自动加上代理设置
            model.addAttribute("proxyset",String.format("proxy->%s://%s:%s@%s:%s",
                    InfoUtils.ProxyType,InfoUtils.ProxyUsername,InfoUtils.ProxyPassword,InfoUtils.ProxyHost,InfoUtils.ProxyPort));
        }
        return model;
    }

    /**
     * 随机数生成
     * @return
     */
    public static String Random(){
        String random = String.valueOf(Math.random());
        random = random.substring(random.length() - 8, random.length());
        return random;
    }

    /**
     * 随机字符串生成
     * @param bit
     * @return
     */
    public static String RandomStr(int bit){ //随机字符串生成
        String str="abcdefhijklmnopqrstuvwxyzABCDEFHIJKLMNOPQRSTUVWXYZ";
        Random rand=new Random();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < bit; i++) {
            int anInt = rand.nextInt(str.length());
            buffer.append(str.charAt(anInt));
        }
        return buffer.toString();
    }

    /**
     * 字节数组转16进制
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {  //字节转16进制
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String s = Integer.toHexString(bytes[i] & 0xFF);
            if (s.length() < 2) {
                s = "0" + s;
            }
            stringBuffer.append(s.toLowerCase());
        }
        return stringBuffer.toString();
    }

    /**
     * unicode 转字符串
     */
    public static String unicode2String(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u00");
        for (int i = 1; i < hex.length; i++) {
            // 转换出每⼀个代码点
            int data = Integer.parseInt(hex[i], 16);
            // 追加成string
            string.append((char) data);
        }
        return string.toString();
    }

    /**
     * 字符串转换unicode
     */
    public static String string2Unicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每⼀个字符
            char c = string.charAt(i);
            // 转换为unicode
            unicode.append("\\u00" + Integer.toHexString(c));
        }
        return unicode.toString();
    }

    /**
     * 获取字符串md5值
     * @param source
     * @return
     */
    public static String getMD5(String source) {
        if (source == null) {
            return null;
        } else {
            char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

            try {
                byte[] strTemp = source.getBytes();
                MessageDigest mdTemp = MessageDigest.getInstance("MD5");
                mdTemp.update(strTemp);
                byte[] md = mdTemp.digest();
                int j = md.length;
                char[] str = new char[j * 2];
                int k = 0;

                for (int i = 0; i < j; ++i) {
                    byte byte0 = md[i];
                    str[k++] = hexDigits[byte0 >>> 4 & 15];
                    str[k++] = hexDigits[byte0 & 15];
                }

                return new String(str);
            } catch (Exception var10) {
                return null;
            }
        }
    }
}
