package cn.tianhan.kit.excel.sax;


import com.tianhan.cloud.common.core.utils.ConvertMap;
import com.tianhan.cloud.common.core.utils.TypeUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: NieAnTai
 * @Description:
 * @Date: 17:57 2019/1/3
 */
public class GeneralRowReader implements IRowReader {

    private LinkedHashMap<String, String> firstRow;
    private List<ConvertMap> rowList;

    public GeneralRowReader() {
        firstRow = new LinkedHashMap<>();
        rowList = new ArrayList<ConvertMap>();
    }

    @Override
    public List<ConvertMap> getRowList() {
        return this.rowList;
    }

    @Override
    public void dealRows(WorksheetInfo info) {
        List<Map<String, Object>> rowsT = info.getRows();
        int cell = info.getMergeCell() == 0 ? 1 : info.getMergeCell();
        rowsT.stream().limit(cell).forEach(current -> {
            current.forEach((k, v) -> {
                firstRow.put(k.replaceAll("[0-9]", ""), v.toString());
            });
        });
        for (int i = cell; i < rowsT.size(); i++) {
            Map<String, Object> current = rowsT.get(i);
            ConvertMap map = new ConvertMap(firstRow.size(), true);
            for (String k : firstRow.keySet()) {
                String v = firstRow.get(k);
                Object oVal = map.get(v);
                Object currentVal = current.get(k + (i + 1));
                // 重复项结果相加
                if (oVal != null && currentVal != null &&
                        TypeUtils.isNumber(oVal.toString()) &&
                        TypeUtils.isNumber(currentVal.toString())) {
                    map.put(v, new BigDecimal(oVal.toString()).add(new BigDecimal(currentVal.toString())));
                } else {
                    map.put(v, currentVal);
                }
            }
            rowList.add(map);
        }
    }

    public void addRowList(ConvertMap m) {
        rowList.add(m);
    }

    public void setRowList(List<ConvertMap> rowList) {
        this.rowList = rowList;
    }
}
