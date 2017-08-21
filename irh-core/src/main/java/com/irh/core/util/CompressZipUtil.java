package com.irh.core.util;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by cai_changqing on 2016/12/8.
 */
public class CompressZipUtil {


    /**
     * 压缩指定文件到当前文件夹
     *
     * @param src 要压缩的指定文件
     * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败.
     */


    public static String zip(String src) {
        return zip(src, null,true,null);

    }


      public static String zip(String src, String dest, boolean isCreateDir, String passwd) {
               File srcFile = new File(src);
               dest = buildDestinationZipFilePath(srcFile, dest);
               ZipParameters parameters = new ZipParameters();
               parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);           // 压缩方式
               parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);    // 压缩级别
                 if (!StringUtil.isNullOrEmpty(passwd)) {
                        parameters.setEncryptFiles(true);
                        parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD); // 加密方式
                        parameters.setPassword(passwd.toCharArray());
                    }
                 try {
                        ZipFile zipFile = new ZipFile(dest);
                  if (srcFile.isDirectory()) {
                                // 如果不创建目录的话,将直接把给定目录下的文件压缩到压缩文件,即没有目录结构
                             if (!isCreateDir) {
                                       File [] subFiles = srcFile.listFiles();
                                      ArrayList<File> temp = new ArrayList<File>();
                                       Collections.addAll(temp, subFiles);
                                        zipFile.addFiles(temp, parameters);
                                      return dest;
                                  }
                              zipFile.addFolder(srcFile, parameters);
                           } else {
                                zipFile.addFile(srcFile, parameters);
                          }
                        return dest;
                    } catch (ZipException e) {
                        e.printStackTrace();
                    }
                 return null;
            }




    /**
     * 构建压缩文件存放路径,如果不存在将会创建
     * 传入的可能是文件名或者目录,也可能不传,此方法用以转换最终压缩文件的存放路径
     *
     * @param srcFile   源文件
     * @param destParam 压缩目标路径
     * @return 正确的压缩文件存放路径
     */


    private static String buildDestinationZipFilePath(File srcFile, String destParam) {
        if (!StringUtil.isNullOrEmpty(destParam)) {
            if (srcFile.isDirectory()) {
                destParam = srcFile.getParent() + File.separator + srcFile.getName() + ".zip";

            } else {
                String fileName = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));
                destParam = srcFile.getParent() + File.separator + fileName + ".zip";

            }

        } else {
            createDestDirectoryIfNecessary(destParam);  // 在指定路径不存在的情况下将其创建出来
            if (destParam.endsWith(File.separator)) {
                String fileName = "";
                if (srcFile.isDirectory()) {
                    fileName = srcFile.getName();

                } else {
                    fileName = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));

                }
                destParam += fileName + ".zip";

            }

        }
        return destParam;

    }


    /**
     * 在必要的情况下创建压缩文件存放目录,比如指定的存放路径并没有被创建
     *
     * @param destParam 指定的存放路径,有可能该路径并没有被创建
     */


    private static void createDestDirectoryIfNecessary(String destParam) {
        File destDir = null;
        if (destParam.endsWith(File.separator)) {
            destDir = new File(destParam);

        } else {
            destDir = new File(destParam.substring(0, destParam.lastIndexOf(File.separator)));

        }
        if (!destDir.exists()) {
            destDir.mkdirs();

        }

    }

    /**
     * 解压文件
     * @param filePath  文件目录
     * @param extractPath 解压文件目录
     * @throws ZipException
     */
    public static void extractAll(String filePath,String extractPath) throws ZipException {
        ZipFile zipFile = new ZipFile(filePath);
        zipFile.extractAll(extractPath);
    }


    public static void main(String[] args) {
   /*    zip("d:\\test\\cc", "d:\\test\\cc.zip", "11");
             try {
             File[] files = unzip("d:\\test\\汉字.zip", "aa");
               for (int i = 0; i < files.length; i++) {
                   System.out.println(files[i]);
          }
           } catch (ZipException e) {
               e.printStackTrace();
            }
     */
    }


}
