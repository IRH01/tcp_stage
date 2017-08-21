/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  Aug 18, 2014  7:52:20 PM
 */
package com.irh.core.util;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 文件工具类
 *
 * @author iritchie.ren
 */
public final class FileUtil {
    /**
     * 打印日志
     */
    private final static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     *
     */
    private FileUtil() {

    }

    /**
     * 根据过滤器返回文件.
     *
     * @param dir
     * @param recursive
     * @return
     */
    public static Collection<File> listFiles(File dir, boolean recursive) {
        return FileUtils.listFiles(dir, new AbstractFileFilter() {
            @Override
            public boolean accept(File file) {
                return true;
            }
        }, recursive ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE);
    }

    /**
     * 根据过滤器返回文件.
     *
     * @param dir
     * @param filter
     * @param recursive
     * @return
     */
    public static Collection<File> listFiles(File dir, AbstractFileFilter filter, boolean recursive) {
        return FileUtils.listFiles(dir, filter, recursive ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE);
    }

    /**
     * @param dir
     * @param extensions 不需要带点
     * @param recursive
     * @return
     */
    public static Collection<File> listFiles(File dir, String[] extensions, boolean recursive) {
        return FileUtils.listFiles(dir, extensions, recursive);
    }

    /**
     * 返回指定名字目录.
     *
     * @return
     */
    public static Collection<File> listDirs(final File dir, final String dirName, final boolean recursive) {
        List<File> result = new ArrayList<>();
        subListDirs(result, dir, dirName, recursive);
        return result;
    }

    /**
     * 把文件读取到字符串中，用系统默认编码。
     */
    public static String readFileToString(File file) throws IOException {
        return FileUtils.readFileToString(file, Charset.defaultCharset());
    }

    /**
     * 读取文件行
     *
     * @param file
     * @param encoding
     * @return
     * @throws IOException
     */
    public static List<String> readLines(File file, String encoding) throws IOException {
        return FileUtils.readLines(file, Charsets.toCharset(encoding));
    }

    /**
     * 读取文件到二进制数组中。
     */
    public static byte[] readFileToByteArray(File file) throws IOException {
        return FileUtils.readFileToByteArray(file);
    }

    /**
     * 清空文件夹，但是不删除文件夹。
     */
    public static void cleanDirectory(File directory) throws IOException {
        FileUtils.cleanDirectory(directory);
    }

    /**
     * 删除空的文件夹
     */
    public static void delEmptyDirectory(File directory) throws IOException {
        File[] files = directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                String[] filePaths = file.list();
                if (filePaths != null) {
                    if (file.isDirectory() && filePaths.length == 0) {
                        return true;
                    }
                    return false;
                } else {
                    return false;
                }
            }
        });
        if (files != null) {
            for (File delFile : files) {
                delFile.delete();
            }
        }
    }

    /**
     * @param dir
     * @param dirName
     * @return
     */
    private static void subListDirs(final List<File> files, final File dir, final String dirName, final boolean recursive) {
        dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    if (file.getName().equalsIgnoreCase(dirName)) {
                        files.add(file);
                    }
                    if (recursive) {
                        subListDirs(files, file, dirName, recursive);
                    }
                }
                return false;
            }
        });
    }

    /**
     * 将文件生成md5
     *
     * @param fileStream
     * @return
     * @throws FileNotFoundException
     */

    public static String getMd5ByFile(byte[] fileStream) throws FileNotFoundException {
        String value = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(fileStream);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    /**
     * 获取文件的byte数组
     *
     * @param file 文件
     * @return
     * @throws IOException
     */
    public static byte[] getBytes(File file) throws IOException {
        byte[] buffer = null;
        ByteArrayOutputStream bos = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream(8192);
            byte[] b = new byte[8192];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            logger.debug(e.getMessage());
        } catch (IOException e) {
            logger.debug(e.getMessage());
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (bos != null) {
                bos.close();
            }

        }
        return buffer;
    }

    public static InputStream getInputStream(String urlpath) throws IOException {
        URL url = new URL(urlpath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //得到输入流
        return conn.getInputStream();
    }

    /** */
    /**
     * 把字节数组保存为一个文件
     *
     * @Author ccq
     */
    public static File getFileFromBytes(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        FileOutputStream fstream = null;
        try {
            file = new File(outputFile);
            fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fstream != null) {
                try {
                    fstream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }


}
