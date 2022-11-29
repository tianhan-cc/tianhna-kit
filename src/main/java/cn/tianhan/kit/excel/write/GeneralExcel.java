package cn.tianhan.kit.excel.write;

import cn.tianhan.kit.excel.ExcelAnnotation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @Author: NieAnTai
 * @Description: 导出Excel表格
 * @Date: 16:51 2018/8/20
 */
public class GeneralExcel extends AbstractWriteExcel {
    private List<?> data;
    private ExcelAnnotation annotationUtils;

    public GeneralExcel(List<?> data, Class zClass) {
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

        CellStyle style_2 = workbook.createCellStyle();
        style_2.setBorderTop(BorderStyle.THIN);
        style_2.setBorderRight(BorderStyle.THIN);
        style_2.setBorderLeft(BorderStyle.THIN);
        style_2.setBorderBottom(BorderStyle.THIN);
        style_2.setAlignment(HorizontalAlignment.CENTER);
        style_2.setVerticalAlignment(VerticalAlignment.CENTER);
        // style_2.setWrapText(true);
        style_2.setFont(font);

        try {
            for (int r = 1; r <= this.data.size(); r++) {
                Object tObje = this.data.get(r - 1);
                SXSSFRow row = sheet.createRow(r);
                for (int _c = 0; _c < fields.size(); _c++) {
                    Field f = fields.get(_c);
                    String value = annotationUtils.getFieldPojo(f, tObje);
                    SXSSFCell _cell = row.createCell(_c);
                    _cell.setCellValue(value);
                    _cell.setCellStyle(style_2);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("读取数据失败");
        }
    }

    /**
     * 设置表头
     */
    private void thead() {
        List<String> theads = annotationUtils.getAnnValue();
        SXSSFRow row = sheet.createRow(0);

        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);

        CellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor((short) 52);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setWrapText(true);
        style.setFont(font);

        for (int c = 0; c < theads.size(); c++) {
            SXSSFCell cell = row.createCell(c);
            sheet.setColumnWidth(c, 8000);
            cell.setCellValue(theads.get(c));
            cell.setCellStyle(style);
        }
    }
}
