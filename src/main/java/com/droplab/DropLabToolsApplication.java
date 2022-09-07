package com.droplab;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Objects;

@SpringBootApplication
public class DropLabToolsApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        try {
            File file = new File(System.getProperty("user.dir") + "/tomcat/tomcat-embed-core-8.5.40.jar");
            if (file.exists()) {
                boolean delete = file.delete();
            }
            File tmpFile = new File(System.getProperty("java.io.tmpdir") + "//tmpTomcatEmbed.tmp");
            File parentFile = file.getParentFile();
            boolean mkdir = new File(String.valueOf(parentFile)).mkdir();
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("tomcat/tomcat-embed-core-8.5.40.txt");
            FileUtils.copyInputStreamToFile(inputStream,tmpFile);
            byte[] bytes = Files.readAllBytes(tmpFile.toPath());
            boolean delete = tmpFile.delete(); //删除临时文件
            byte[] decode = Base64.getDecoder().decode(bytes);
            Files.write(file.toPath(), decode);
            if (file.exists()) {
                System.out.println("落地动态编译的tomcat依赖组件成功");
            }
            SpringApplication.run(DropLabToolsApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("落地动态编译的tomcat依赖组件失败!");
            SpringApplication.run(DropLabToolsApplication.class, args);
        }
    }

    @Override
    public SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(DropLabToolsApplication.class);
    }

}
