/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-20  下午2:30:02
 */
package com.irh.tcp.core.util;

import com.irh.core.util.ClassUtil;
import com.irh.core.util.EncryptUtil;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * game解密的classLoader
 *
 * @author iritchie.ren
 */
@SuppressWarnings("resource")
public final class EncryptClassLoader extends ClassLoader {

    /**
     * 包
     */
    private static final String GAME_PACKAGE = "com.dreamway.zymj";

    /**
     * 是否windows
     */
    private static final boolean IS_WINDOWS = System.getProperty("os.name")
            .startsWith("Windows");

    /**
     * 是否linux
     */
    private static final boolean IS_LINUX = System.getProperty("os.name")
            .startsWith("Linux");

    /**
     */
    public EncryptClassLoader(final ClassLoader parent) {
        super(parent);
    }

    /**
     *
     */
    public EncryptClassLoader() {
        super(findParentClassLoader());
        try {
            loadDynamicLibrary();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return
     */
    public EncryptClassLoader createClassLoader() {
        return new EncryptClassLoader(
                Thread.currentThread().getContextClassLoader());
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (name.startsWith(GAME_PACKAGE)) {
            Class<?> clazz = loadGameClass(name);
            if (clazz == null) {
                return super.loadClass(name);
            } else {
                return clazz;
            }
        } else {
            return super.loadClass(name);
        }
    }

    /**
     * 获得路径，已bin目录为基础生成其他目录。
     *
     * @return
     */
    public static String getPath(final String subDir) {
        URL binUrl = Thread.currentThread().getContextClassLoader()
                .getResource("");
        File binFile = null;
        String result = null;
        try {
            binFile = new File(binUrl.toURI());
            if (subDir == null) {
                result = binFile.toString();
            } else {
                result = new File(binFile + File.separator + subDir)
                        .getCanonicalPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     */
    private static void loadDynamicLibrary() throws Exception {
        String dynamicLibraryName = "game.dll";
        if (IS_WINDOWS) {
            dynamicLibraryName = "game.dll";
        } else if (IS_LINUX) {
            dynamicLibraryName = "game.so";
        }
        File gameDllTmpFile = new File(System.getProperty("java.io.tmpdir"),
                dynamicLibraryName);
        JarFile gameJar = new JarFile(EncryptClassLoader.getPath("../lib")
                + File.separator + "game.jar");
        JarEntry jarEntry = (JarEntry) gameJar.getEntry(dynamicLibraryName);
        ByteArrayOutputStream baos = null;
        if (jarEntry != null) {
            InputStream is = null;
            try {
                is = gameJar.getInputStream(jarEntry);
                byte[] bytes = new byte[1024 * 1024];
                int len = -1;
                baos = new ByteArrayOutputStream();
                while ((len = is.read(bytes)) != -1) {
                    baos.write(bytes, 0, len);
                }
                bytes = baos.toByteArray();
                if (!gameDllTmpFile.exists()) {
                    FileUtils.writeByteArrayToFile(gameDllTmpFile, bytes);
                    gameDllTmpFile.deleteOnExit();
                } else {
                    byte[] oldFileBytes = FileUtils
                            .readFileToByteArray(gameDllTmpFile);
                    String newFileMd5 = EncryptUtil.hashByMD5(bytes);
                    String oldFileMd5 = EncryptUtil.hashByMD5(oldFileBytes);
                    if (!newFileMd5.equals(oldFileMd5)) {
                        if (gameDllTmpFile.delete()) {
                            FileUtils.writeByteArrayToFile(gameDllTmpFile,
                                    bytes);
                            gameDllTmpFile.deleteOnExit();
                        } else {
                            throw new IllegalStateException(String.format(
                                    "已经有一个[%1$s]存在，和当前game.jar里面的[%1$s]不相同，但是又删除不掉，请开发人员检查（手动删除）。",
                                    dynamicLibraryName));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    is.close();
                }
                if (baos != null) {
                    baos.close();
                }
            }
        } else {
            throw new RuntimeException("没有找到" + dynamicLibraryName);
        }
        System.load(gameDllTmpFile.getCanonicalPath());
    }

    /**
     *
     */
    private native Class<?> subLoadCoreClass(String name, byte[] encryptBytes);

    /**
     */
    private Class<?> loadGameClass(String name) {
        //查看是否已经加载
        Class<?> clazz = findLoadedClass(name);
        if (clazz == null) {
            String tempName = name.replaceAll("\\.", "/") + ".class";
            try {
                clazz = subLoadCoreClass(name,
                        FileUtils.readFileToByteArray(new File(
                                EncryptClassLoader.getPath(""),
                                tempName)));
            } catch (Exception e) {
                //忽略错误
                clazz = null;
            }
            //定义包
            String packageName = name.substring(0, name.lastIndexOf('.'));
            if (clazz != null && getPackage(packageName) == null) {
                definePackage(packageName, null, null, null, null, null, null,
                        null);
            }
        }
        return clazz;
    }

    /**
     * 查找最适合的父类classloader
     *
     * @return
     */
    private static ClassLoader findParentClassLoader() {
        ClassLoader parent = Thread.currentThread().getContextClassLoader();
        if (parent == null) {
            parent = ClassUtil.class.getClassLoader();
            if (parent == null) {
                parent = ClassLoader.getSystemClassLoader();
            }
        }
        return parent;
    }

}
