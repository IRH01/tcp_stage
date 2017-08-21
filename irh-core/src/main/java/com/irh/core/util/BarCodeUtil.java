package com.irh.core.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * 生成二维码
 *
 * @author zk
 * @date 2015年10月13日 下午5:48:16
 */
public final class BarCodeUtil {

    private BarCodeUtil() {

    }

    /**
     * 将内容转化为二维码图片（png格式）
     *
     * @param filePath 文件路径+文件名
     * @param content  要转化的内容
     * @return
     */
    @SuppressWarnings("deprecation")
    public static boolean createBarCodeToFile(String content, String filePath) {
        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bm = null;
            bm = new MultiFormatWriter().encode(content.toString(), BarcodeFormat.QR_CODE, 300, 300);
            File file = new File(filePath);
            file.mkdirs();
            MatrixToImageWriter.writeToFile(bm, "png", file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将内容转化为二维码图片输出流等式
     *
     * @param content
     * @param outstream
     * @return
     */
    public static boolean createBarCodeToOutputStream(String content, OutputStream outstream) {
        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bm = null;
            bm = new MultiFormatWriter().encode(content.toString(), BarcodeFormat.QR_CODE, 300, 300);
            MatrixToImageWriter.writeToStream(bm, "png", outstream);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
