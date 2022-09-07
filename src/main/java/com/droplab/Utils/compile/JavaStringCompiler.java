package com.droplab.Utils.compile;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * In-memory compile Java source code as String.
 *
 * @author michael
 */
public class JavaStringCompiler {

    JavaCompiler compiler;
    StandardJavaFileManager stdManager;

    public JavaStringCompiler() {
        compiler = ToolProvider.getSystemJavaCompiler();
        System.out.println(compiler);
        this.stdManager = compiler.getStandardFileManager(null, null, null);
    }

    /**
     * Compile a Java source file in memory.
     *
     * @param fileName
     *            Java file name, e.g. "Test.java"
     * @param source
     *            The source code as String.
     * @return The compiled results as Map that contains class name as key,
     *         class binary as value.
     * @throws IOException
     *             If compile error.
     */
    public Map<String, byte[]> compile(String fileName, String source) throws IOException {

        List<String> options = new ArrayList<>();
        String property = System.getProperty("user.dir");
        options.add("-encoding");
        options.add("UTF-8");
        options.add("-classpath");
        options.add(property+"/tomcat/tomcat-embed-core-8.5.40.jar");  //指定动态编译时tomcat所需要的依赖。
        options.add("-Xlint:deprecation");
        options.add("-Xlint:unchecked");
        try (MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager)) {
            JavaFileObject javaFileObject = manager.makeStringSource(fileName, source);
            CompilationTask task = compiler.getTask(null, manager, null, options, null, Arrays.asList(javaFileObject));
            Boolean result = task.call();
            if (result == null || !result.booleanValue()) {
                throw new RuntimeException("Compilation failed.");
            }
            return manager.getClassBytes();
        }
    }
}
