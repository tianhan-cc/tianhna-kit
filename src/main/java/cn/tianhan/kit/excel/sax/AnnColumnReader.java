package cn.tianhan.kit.excel.sax;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @Author NieAnTai
 * @Date 2021/2/28 12:42 上午
 * @Version 1.0
 * @Email nieat@foxmail.com
 * @Description
 **/
public class AnnColumnReader<T> extends GeneralAnnReader<T> {
    public AnnColumnReader(Class<T> zClass) {
        super(zClass);
    }

    @Override
    public void dealRows(WorksheetInfo info) {
        List<Map<String, Object>> rowsT = info.getRows();
        List<Field> fields = annotation.getAnnField();
        List<String> keys = annotation.getColumn();
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
                    String key = keys.get(i2) + (i + 1);
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
