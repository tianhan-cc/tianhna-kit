package cn.tianhan.kit.excel.write;

import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @Author: NieAnTai
 * @Description: 写入Excel抽象类
 * @Date: 10:10 2019/4/28
 */
public abstract class AbstractWriteExcel implements IWrite {
    protected SXSSFWorkbook workbook;
    protected SXSSFSheet sheet;

    public AbstractWriteExcel() {
        workbook = new SXSSFWorkbook();
        sheet = workbook.createSheet("Sheet1");
    }

    /**
     * 执行
     */
    public abstract void finish();

    @Override
    public void write(OutputStream out) throws IOException, NullPointerException {
        finish();
        workbook.write(out);
    }


    public void setWorkbook(SXSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public void setSheet(SXSSFSheet sheet) {
        this.sheet = sheet;
    }
}
