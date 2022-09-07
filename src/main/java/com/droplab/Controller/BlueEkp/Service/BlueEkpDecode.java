package com.droplab.Controller.BlueEkp.Service;

import com.landray.kmss.util.DESEncrypt;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;

/**
 * 蓝凌OA的加解密功能
 */
public class BlueEkpDecode {
    public BlueEkpDecode() {
    }

    public String DecodePassword(String password) {
        try {
            password = password.replace("\\", "");
            DESEncrypt desEncrypt = new DESEncrypt("kmssAdminKey");
            byte[] bytes = desEncrypt.decrypt(java.util.Base64.getDecoder().decode(password));
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "解密出错";
        }
    }

    public String DecodePropertiesFile(File filepath) {
        try {
            DESEncrypt desEncrypt = new DESEncrypt("kmssPropertiesKey");
            byte[] bytes = Files.readAllBytes(filepath.toPath());
            byte[] decrypt = desEncrypt.decrypt(bytes);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(decrypt);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int ch = 0;
            byte[] bytes1 = new byte[1024];
            while ((ch = inputStream.read(bytes1)) != -1) {
                outputStream.write(bytes1, 0, ch);
            }
            return new String(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
