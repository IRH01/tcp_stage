package com.irh.core.util;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 读写excel工具类
 *
 * @author zk
 * @date 2015年9月29日 下午5:06:28
 */
public final class ExcelUtil {

    private ExcelUtil() {

    }

    public static List<String[]> readToList(InputStream in, String fileName) {
        if (fileName.endsWith(".xlsx")) {
            return readToList(in, true);// Excel 2007
        } else {
            return readToList(in, false);// Excel 2003
        }
    }

    @SuppressWarnings("resource")
    public static List<String[]> readToList(InputStream in, boolean isExcel2007) {
        List<String[]> list = new ArrayList<String[]>();
        try {
            Workbook workbook = null;
            if (isExcel2007) {
                workbook = new XSSFWorkbook(in);// Excel 2007
            } else {
                workbook = new HSSFWorkbook(in);// Excel 2003
            }

            Sheet sheet = workbook.getSheetAt(0);// Excel工作表对象

            int rowTotal = sheet.getPhysicalNumberOfRows();
            int maxColTotal = getMaxColTotal(sheet);
            for (int i = 0; i < rowTotal; i++) {
                Row row = sheet.getRow(i);// Excel工作表的行
                if (row == null) {
                    break;
                }
                String[] cellValues = new String[maxColTotal];
                for (int j = 0; j < maxColTotal; j++) {
                    Cell cell = row.getCell(j, Row.CREATE_NULL_AS_BLANK);// Excel工作表指定行的单元格
                    if (null == cell) {
                        cellValues[j] = "";
                    } else {
                        // 判断单元格值类型
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:// 字符串类型
                                cellValues[j] = cell.getRichStringCellValue().getString();
                                break;
                            case Cell.CELL_TYPE_NUMERIC:// 数值类型
                                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                    cellValues[j] = cell.getDateCellValue().toString();
                                } else {
                                    DecimalFormat decimalFormat = new DecimalFormat("#");
                                    cellValues[j] = decimalFormat.format(cell.getNumericCellValue());
                                }
                                break;
                            case Cell.CELL_TYPE_FORMULA:// 公式
                                cellValues[j] = cell.getCellFormula();
                                break;
                            case Cell.CELL_TYPE_ERROR | Cell.CELL_TYPE_BLANK:
                                cellValues[j] = "";
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                cellValues[j] = String.valueOf(cell.getBooleanCellValue());
                                break;
                            default:
                                cellValues[j] = "";
                        }
                    }
                    cellValues[j] = cellValues[j].trim();
                }
                list.add(cellValues);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 将excel内容转化为list
     *
     * @param excelPath excel 文件路径
     * @return
     * @throws IOException
     */
    public static List<String[]> readToList(String excelPath) {
        try {
            InputStream in = new FileInputStream(excelPath);
            if (excelPath.endsWith(".xlsx")) {
                return readToList(in, true);// Excel 2007
            } else {
                return readToList(in, false);// Excel 2003
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<String[]>();
    }

    private static int getMaxColTotal(Sheet sheet) {
        int max = 0;
        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row r = sheet.getRow(i);
            if (r == null) {
                continue;
            }
            int temp = r.getPhysicalNumberOfCells();
            if (max < temp) {
                max = temp;
            }
        }
        return max;
    }


    @SuppressWarnings({"resource", "unchecked", "rawtypes"})
    public static <T> File exportToExcelFile(List<T> datas, String excelPath) {
        try {
            // 声明一个工作薄
            XSSFWorkbook workbook = new XSSFWorkbook();

            // 生成一个表格
            XSSFSheet sheet = workbook.createSheet();
            for (int i = 0; i < datas.size(); i++) {
                T t = datas.get(i);

                Field[] fields = t.getClass().getDeclaredFields();

                // 标题
                if (i == 0) {
                    XSSFRow row = sheet.createRow(0);
                    for (int k = 0; k < fields.length; k++) {
                        Field field = fields[k];
                        XSSFCell cell = row.createCell(k);
                        cell.setCellValue(field.getName());
                    }
                }

                // 数据体
                XSSFRow row = sheet.createRow(i + 1);
                for (int j = 0; j < fields.length; j++) {
                    XSSFCell cell = row.createCell(j);
                    Field field = fields[j];
                    String fieldName = field.getName();
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Class tCls = t.getClass();
                    Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                    Object value = getMethod.invoke(t, new Object[]{});
                    if (value == null || "".equals(value)) {
                        value = "";
                        cell.setCellValue("");
                    } else if (value instanceof String) {
                        String intValue = (String) value;
                        cell.setCellValue(intValue);
                    } else if (value instanceof Integer) {
                        int intValue = (Integer) value;
                        cell.setCellValue(intValue);
                    } else if (value instanceof Float) {
                        float fValue = (Float) value;
                        cell.setCellValue(fValue + "");
                    } else if (value instanceof Double) {
                        double dValue = (Double) value;
                        cell.setCellValue(dValue + "");
                    } else if (value instanceof Long) {
                        long longValue = (Long) value;
                        cell.setCellValue(longValue);
                    } else if (value instanceof Date) {
                        String dValue = TimeUtil.dateToEngString((Date) value, "yyyy-MM-dd HH:mm:ss");
                        cell.setCellValue(dValue);
                    }
                }
            }

            File f = new File(excelPath);
            OutputStream os = new FileOutputStream(f);
            workbook.write(os);
            return f;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
