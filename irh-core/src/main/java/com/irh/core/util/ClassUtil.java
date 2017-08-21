/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-21  下午3:47:16
 */
package com.irh.core.util;

import org.apache.commons.lang.ClassUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类加载工具
 *
 * @author iritchie.ren
 */
@SuppressWarnings("unchecked")
public final class ClassUtil {

    /**
     *
     */
    private ClassUtil() {

    }

    /**
     * 取得某一类所在包的所有类名 不含迭代
     *
     * @param classLocation
     * @param packageName
     * @return
     */
    public static String[] getPackageAllClassName(final String classLocation,
                                                  final String packageName) {
        // 将packageName分解
        String[] packagePathSplit = packageName.split("[.]");
        String realClassLocation = classLocation;
        int packageLength = packagePathSplit.length;
        for (int i = 0; i < packageLength; i++) {
            realClassLocation = realClassLocation + File.separator
                    + packagePathSplit[i];
        }
        File packeageDir = new File(realClassLocation);
        if (packeageDir.isDirectory()) {
            String[] allClassName = packeageDir.list();
            return allClassName;
        }
        return null;
    }

    /**
     * 从包package中获取所有的Class,包含annotation
     *
     * @return
     */
    public static <T> List<Class<T>> getClassesByInterface(String packageName,
                                                           Class<T> interfaceClass) {
        //过滤annotation
        List<Class<T>> resultClass = new ArrayList<>();
        List<Class<T>> tmpCLasses = getClasses(packageName, true);
        if (interfaceClass != null) {
            for (Class<?> clazz : tmpCLasses) {
                for (Object interfaceCls : ClassUtils.getAllInterfaces(clazz)) {
                    if (interfaceCls == interfaceClass) {
                        resultClass.add((Class<T>) clazz);
                        break;
                    }
                }
            }
        }
        return resultClass;
    }

    /**
     * 从包package中获取所有的Class,包含annotation
     *
     * @return
     */
    public static <T> List<Class<T>> getClassesByAnnotation(String packageName,
                                                            Class<? extends Annotation> annotationCls) {
        //过滤annotation
        List<Class<T>> resultClass = new ArrayList<>();
        List<Class<T>> tmpCLasses = getClasses(packageName, true);
        if (annotationCls != null) {
            for (Class<?> clazz : tmpCLasses) {
                if (clazz.isAnnotationPresent(annotationCls)) {
                    resultClass.add((Class<T>) clazz);
                }
            }
        }
        return resultClass;
    }

    /**
     * 从包package中获取所有的Class,包含annotation
     *
     * @return
     */
    public static <T> List<Class<T>> getClasses(String packageName,
                                                boolean recursive) {
        // 第一个class类的集合
        List<Class<T>> resultClasses = new ArrayList<>();
        // 是否循环迭代
        // 获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader()
                    .getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    ClassUtil.findAndAddClassesInPackageByFile(packageName,
                            filePath, recursive, resultClasses);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 定义一个JarFile
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection())
                                .getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx)
                                            .replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    // 如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class")
                                            && !entry.isDirectory()) {
                                        // 去掉后面的".class" 获取真正的类名
                                        String className = name.substring(
                                                packageName.length()
                                                        + 1,
                                                name.length() - 6);
                                        try {
                                            // 添加到classes
                                            resultClasses.add((Class<T>) Class
                                                    .forName(packageName
                                                            + '.'
                                                            + className));
                                        } catch (ClassNotFoundException e) {
                                            LogUtil.error("get classes error.",
                                                    e);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        LogUtil.error("get classes error.", e);
                    }
                }
            }
        } catch (IOException e) {
            LogUtil.error("get classes error.", e);
        }
        return resultClasses;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     */
    public static <T> void findAndAddClassesInPackageByFile(
            final String packageName, final String packagePath,
            final boolean recursive, final List<Class<T>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            @Override
            public boolean accept(final File file) {
                return (recursive && file.isDirectory())
                        || (file.getName().endsWith(".class"));
            }
        });

        // 循环所有文件
        if (dirfiles != null) {
            for (File file : dirfiles) {
                // 如果是目录 则继续扫描
                if (file.isDirectory()) {
                    ClassUtil.findAndAddClassesInPackageByFile(
                            packageName + "." + file.getName(),
                            file.getAbsolutePath(), recursive, classes);
                } else {
                    // 如果是java类文件 去掉后面的.class 只留下类名
                    String className = file.getName().substring(0,
                            file.getName().length() - 6);

                    try {
                        // 添加到集合中去
                        classes.add((Class<T>) Class.forName(
                                packageName + '.' + className));

                    } catch (ClassNotFoundException e) {
                        LogUtil.error("get classes error.", e);
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("dirfiles is null");
        }
    }

    /**
     * 构造一个不定参数的类
     *
     * @param args
     * @return
     * @throws ClassNotFoundException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Object newInstance(final Class<?> newoneClass,
                                     final String args) throws NoSuchMethodException,
            InstantiationException,
            IllegalAccessException,
            InvocationTargetException {
        Constructor<?> con = newoneClass.getConstructor(String.class);
        return con.newInstance(args);
    }

    /**
     * 查找最适合的父类classloader
     *
     * @return
     */
    public static ClassLoader findParentClassLoader() {
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
