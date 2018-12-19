package org.clc.utils;

import java.io.*;

public class FileUtils {


    /**
     * 保存文件
     */
    public static void saveFile(byte[] bytes, String filePath, String fileName) {
        try {
            BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(filePath + fileName));
            buffStream.write(bytes);
            buffStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得指定文件的byte数组
     */
    private byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}
