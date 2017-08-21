/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-20  下午2:30:02
 */
package com.irh.core.classparse;

import java.util.*;

/**
 * 
 * @author iritchie.ren
 */
public class JavaClass {
    /**
     * 
     */
    private String className;
    /**
     * 
     */
    private String packageName;
    /**
     * 
     */
    private boolean isAbstract;
    /**
     * 
     */
    private Map<String, JavaPackage> imports;
    /**
     * 
     */
    private String sourceFile;
    /**
     * 引用的文件
     */
    private List<String> importFiles;

    /**
     * 
     */
    public JavaClass(String name) {
        className = name;
        packageName = "default";
        isAbstract = false;
        imports = new HashMap<>();
        sourceFile = "Unknown";
        importFiles = new ArrayList<String>();
    }

    /**
     * 
     */
    public void setName(String name) {
        className = name;
    }

    /**
     * 
     */
    public String getName() {
        return className;
    }

    /**
     * 
     */
    public void setPackageName(String name) {
        packageName = name;
    }

    /**
     * 
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * 
     */
    public void setSourceFile(String name) {
        sourceFile = name;
    }

    /**
     * 
     */
    public String getSourceFile() {
        return sourceFile;
    }

    /**
     * 
     */
    public Collection<JavaPackage> getImportedPackages() {
        return imports.values();
    }

    /**
     * 
     */
    public void addImportedPackage(JavaPackage jPackage) {
        if (!jPackage.getName().equals(getPackageName())) {
            imports.put(jPackage.getName(), jPackage);
        }
    }

    /**
     * 
     */
    public void addImportedFile(String jImportFile) {
        if (!className.equals(jImportFile)) {
            importFiles.add(jImportFile);
        }
    }

    /**
     * 
     * @return
     */
    public List<String> getImportedFiles() {
        return importFiles;
    }

    /**
     * 
     */
    public boolean isAbstract() {
        return isAbstract;
    }

    /**
     * 
     */
    public void isAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    /**
     * 
     */
    public boolean equals(Object other) {

        if (other instanceof JavaClass) {
            JavaClass otherClass = (JavaClass) other;
            return otherClass.getName().equals(getName());
        }

        return false;
    }

    /**
     * 
     */
    public int hashCode() {
        return getName().hashCode();
    }

    /**
     * 
     */
    public static class ClassComparator implements Comparator<JavaClass> {
        /**
         * 
         */
        public int compare(JavaClass a, JavaClass b) {
            return a.getName().compareTo(b.getName());
        }
    }
}
