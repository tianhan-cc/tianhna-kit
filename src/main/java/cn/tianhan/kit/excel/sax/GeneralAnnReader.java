package cn.tianhan.kit.excel.sax;

import com.tianhan.cloud.common.core.utils.excel.ExcelAnnotation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: NieAnTai
 * @Description: ExcelTarget注解映射注入
 * @Date: 15:43 2019/4/26
 */
public class GeneralAnnReader<T> implements IRowReader {
    protected Class<T> zClass;
    protected ExcelAnnotation annotation;
    protected List<T> rowList;
    protected LinkedHashMap<String, String> firstRow;

    public GeneralAnnReader(Class<T> zClass) {
        this.zClass = zClass;
        firstRow = new LinkedHashMap<>();
        rowList = new ArrayList<>();
        annotation = new ExcelAnnotation(zClass);
    }

    @Override
    public List<T> getRowList() {
        return rowList;
    }

    @Override
    public void dealRows(WorksheetInfo info) {
        List<Map<String, Object>> rowsT = info.getRows();
        List<Field> fields = annotation.getAnnField();
        List<String> keys = annotation.getAnnValue();
        int cell = info.getMergeCell() == 0 ? 1 : info.getMergeCell();
        rowsT.stream().limit(cell).forEach(current -> {
            current.forEach((k, v) -> {
                firstRow.put(v.toString(), k.replaceAll("[0-9]", ""));
            });
        });
        for (int i = cell; i < rowsT.size(); i++) {
            Map<String, Object> current = rowsT.get(i);
            T target = null;
            try {
                target = zClass.newInstance();
                for (int i2 = 0; i2 < fields.size(); i2++) {
                    String key = firstRow.get(keys.get(i2)) + (i + 1);
                    Object value = current.get(key);
                    annotation.setFieldPojo(fields.get(i2), value, target);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            rowList.add(target);
        }
    }
}
