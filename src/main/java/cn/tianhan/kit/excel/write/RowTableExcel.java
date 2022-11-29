package cn.tianhan.kit.excel.write;

import cn.tianhan.kit.excel.ExcelAnnotation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @Author: NieAnTai
 * @Description: 表头行Excel表格
 * @Date: 10:41 2019/4/25
 */
public class RowTableExcel extends AbstractWriteExcel {
    /**
     * 存储XSSFRow对象
     */
    private SXSSFRow[] rows;
    private List<?> data;
    private ExcelAnnotation annotationUtils;

    public RowTableExcel(List<?> data, Class zClass) {
        super();
        this.data = data;
        this.annotationUtils = new ExcelAnnotation(zClass);
    }

    @Override
    public void finish() {
        if (!annotationUtils.getFlag()) {
            throw new NullPointerException("没有设置ExcelTarget注解类");
        }
        sheet.setDefaultRowHeight((short) 500);
        thead();
        context();
    }


    /**
     * 填充表格内容
     */
    private void context() {
        List<Field> fields = annotationUtils.getAnnField();

        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 10);
        font.setBold(false);

        CellStyle style2 = workbook.createCellStyle();
        style2.setBorderTop(BorderStyle.THIN);
        style2.setBorderRight(BorderStyle.THIN);
        style2.setBorderLeft(BorderStyle.THIN);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setAlignment(HorizontalAlignment.CENTER);
        style2.setVerticalAlignment(VerticalAlignment.CENTER);
        // style_2.setWrapText(true);
        style2.setFont(font);

        try {
            for (int c = 0; c < this.data.size(); c++) {
                Object tObj = this.data.get(c);
                for (int r = 0; r < fields.size(); r++) {
                    SXSSFRow row;
                    if (r > this.rows.length) {
                        break;
                    }
                    row = this.rows[r];
                    sheet.setColumnWidth(c + 1, 8000);
                    SXSSFCell cell = row.createCell(c + 1);
                    Field f = fields.get(r);
                    String value = annotationUtils.getFieldPojo(f, tObj);
                    cell.setCellValue(value);
                    cell.setCellStyle(style2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("读取数据失败");
        }
    }

    /**
     * 设置表头
     */
    private void thead() {
        List<String> theads = annotationUtils.getAnnValue();

        Font font = workbook.createFont();
        // 设置字体风格
        font.setFontName("宋体");
        // 设置字体宽度
        font.setFontHeightInPoints((short) 12);
        // 是否加粗
        font.setBold(true);

        CellStyle style = workbook.createCellStyle();
        // 设置边框
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        // 设置居中
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置单元格颜色
        style.setFillForegroundColor((short) 52);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 是否换行
        style.setWrapText(true);
        // 设置字体
        style.setFont(font);

        this.rows = new SXSSFRow[theads.size()];
        sheet.setColumnWidth(0, 5000);
        for (int c = 0; c < theads.size(); c++) {
            SXSSFRow row = sheet.createRow(c);
            rows[c] = row;
            SXSSFCell cell = row.createCell(0);
            cell.setCellValue(theads.get(c));
            cell.setCellStyle(style);
        }
    }
}
