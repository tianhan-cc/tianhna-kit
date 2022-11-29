package cn.tianhan.kit.excel.write;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

/**
 * @Author: NieAnTai
 * @Description: 多页Excel创建
 * @Date: 10:59 2019/4/28
 */
public class MultipleSheetExcel implements IWrite {
    private LinkedList<AbstractWriteExcel> objs = new LinkedList<>();
    private LinkedList<String> names = new LinkedList<>();
    private SXSSFWorkbook workbook;

    public MultipleSheetExcel() {
        workbook = new SXSSFWorkbook();
    }

    public MultipleSheetExcel addSheet(@NotNull String name, @NotNull AbstractWriteExcel obj) {
        this.objs.add(obj);
        this.names.add(name);
        return this;
    }

    @Override
    public void write(OutputStream out) throws IOException {
        int size = objs.size();
        String sheetName;
        AbstractWriteExcel writeExcel = null;
        for (int i = 0; i < size; i++) {
            writeExcel = objs.get(i);
            sheetName = names.get(i);
            writeExcel.setWorkbook(workbook);
            writeExcel.setSheet(workbook.createSheet(sheetName));
            if (i < (size - 1)) {
                writeExcel.finish();
            } else {
                writeExcel.write(out);
            }
        }
    }
}
