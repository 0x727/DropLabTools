package com.droplab.Utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 用于创建特殊压缩包
 */
public class FileZipUtils {
    private int depth = 0; //目录穿越深度;
    private String platform = "win"; //windows或者linux
    private String dir;

    public FileZipUtils(int depth, String platform) {
        this.depth = depth;
        this.platform = platform;
    }

    private void handlerFile(ZipOutputStream zip, File file, String path, boolean flag) throws Exception {
        if (platform.equals("windows")) {
            dir = "..\\";
            if (!path.equals("") && path != null && !path.endsWith("\\")) {
                path += "\\";
            }
        } else if (platform.equals("linux")) {
            dir = "../";
            if (!path.equals("") && path != null && !path.endsWith("/")) {
                path += "/";
            }
        }
        String tmpPath = "";
        if (flag) {
            for (int i = 0; i < depth; i++) {
                tmpPath += dir;
            }
        }
        String name = file.getName();
        String filePath = tmpPath + path + name;
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        ZipEntry entry = new ZipEntry(filePath);
        zip.putNextEntry(entry);
        zip.write(FileUtils.readFileToByteArray(file));
        IOUtils.closeQuietly(bis);
        zip.flush();
        zip.closeEntry();
    }

    public File createZip(HashMap<String, String> fileMap, String path) throws Exception {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ZipOutputStream zip = new ZipOutputStream(outputStream);
            //将目标文件打包成zip导出
            Iterator<String> iterator = fileMap.keySet().iterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                String s = fileMap.get(next);
                boolean flag = true;  //用于判断当前这个文件是否需要目录穿越
                String tPath = s;  //
                if (!s.equals("") && s.contains("|")) {
                    String[] split = s.split("\\|");
                    tPath=split[0];
                    if (split[1].equals("false")) {
                        flag = false;
                    }
                }
                handlerFile(zip, new File(next), tPath, flag);
            }
            IOUtils.closeQuietly(zip);
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            FileUtils.writeByteArrayToFile(file, outputStream.toByteArray());
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void unzipFile(InputStream var1, String var2) throws IOException {
        byte[] var3 = new byte[1024];
        File var4 = new File(var2);
        if (!var4.exists()) {
            var4.mkdir();
        }

        ZipInputStream var5 = new ZipInputStream(var1);

        for (ZipEntry var6 = var5.getNextEntry(); var6 != null; var6 = var5.getNextEntry()) {
            String var7 = var6.getName();
            File var8 = new File(pathJoin(new String[]{var2, var7}));
            (new File(var8.getParent())).mkdirs();
            FileOutputStream var9 = new FileOutputStream(var8);

            int var10;
            while ((var10 = var5.read(var3)) > 0) {
                var9.write(var3, 0, var10);
            }

            var9.close();
        }

        var5.closeEntry();
        var5.close();
    }

    public static String pathJoin(String... var0) {
        StringBuffer var1 = new StringBuffer();
        int var2 = 0;

        for (int var3 = var0 == null ? 0 : var0.length; var2 < var3; ++var2) {
            String var4 = var0[var2];
            if (var4 == null) {
                var4 = "";
            }

            if (var2 > 0 && (var4.startsWith("/") || var4.startsWith("\\"))) {
                var4 = var4.substring(1);
            }

            var1.append(var4);
            if (var2 + 1 < var3 && !var4.endsWith("/") && !var4.endsWith("\\")) {
                var1.append("/");
            }
        }

        return var1.toString();
    }

    public static String perfectStart(String var0, String var1) {
        if (var0 == null) {
            return var1;
        } else {
            return var0.startsWith(var1) ? var0 : var1 + var0;
        }
    }
}
