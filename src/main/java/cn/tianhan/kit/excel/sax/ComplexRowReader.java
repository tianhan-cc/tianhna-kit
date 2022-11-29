package cn.tianhan.kit.excel.sax;



import cn.tianhan.kit.ConvertMap;

import java.util.*;

/**
 * @Author: NieAnTai
 * @Description: 复杂表头读取
 * @Date: 18:52 2019/4/23
 */
public class ComplexRowReader implements IRowReader {
    private LinkedHashMap<String, String> firstRow;
    private LinkedList<ConvertMap> rowList;

    private Map<String, List<String>> equalRow;

    public ComplexRowReader() {
        firstRow = new LinkedHashMap<>();
        rowList = new LinkedList<>();
        equalRow = new HashMap<>();
    }

    @Override
    public List<ConvertMap> getRowList() {
        return this.rowList;
    }

    @Override
    public void dealRows(WorksheetInfo info) {
        List<Map<String, Object>> rowsT = info.getRows();
        findCellSpace(info.getMergeCells());
        int cell = info.getMergeCell() == 0 ? 1 : info.getMergeCell();
        LinkedHashMap<String, String> table = new LinkedHashMap<>();
        rowsT.stream().limit(cell).forEach((current) -> {
            current.forEach((k, v) -> {
                table.put(k, v.toString());
            });
        });
        table.forEach((k, v) -> {
            String k1 = k.replaceAll("[0-9]", "");
            if (this.firstRow.containsKey(k1)) {
                String v1 = this.firstRow.get(k1);
                v = new StringBuilder(v1).append(":").append(v).toString();
            } else {
                String parentK = findParentCell(k);
                // 递归
                while (parentK != null && table.containsKey(parentK)) {
                    String v2 = table.get(parentK);
                    v = new StringBuilder(v2).append(":").append(v).toString();
                    parentK = findParentCell(parentK);
                }
            }
            this.firstRow.put(k1, v);
        });
        rowsT.stream().skip(cell).forEach(current -> {
            ConvertMap map = new ConvertMap(true);
            current.forEach((k, v) -> {
                String t = k.replaceAll("[0-9]", "");
                if (firstRow.containsKey(t)) {
                    String[] keys = firstRow.get(t).split(":");
                    String parentKey, childKey;
                    ConvertMap tmp = map;
                    for (int i = 0; ; i++) {
                        parentKey = i < keys.length ? keys[i] : null;
                        childKey = i < keys.length - 1 ? keys[i + 1] : null;
                        if (parentKey == null) break;
                        if (tmp.containsKey(parentKey)) {
                            Object oldV = tmp.get(parentKey);
                            if (oldV instanceof ConvertMap) {
                                tmp = (ConvertMap) oldV;
                            } else {
                                ConvertMap m = new ConvertMap();
                                m.put(parentKey, oldV);
                                tmp.put(parentKey, m);
                                tmp = m;
                            }
                        } else {
                            if (childKey == null) {
                                tmp.put(parentKey, v);
                            } else {
                                ConvertMap m2 = new ConvertMap();
                                tmp.put(parentKey, m2);
                                tmp = m2;
                            }
                        }
                    }
                }
            });
            rowList.add(map);
        });
    }

    /**
     * 获取同行，异列的合并单元格数据
     */
    private void findCellSpace(List<String> cells) {
        cells.forEach(s -> {
            String v1 = s.replaceAll("[a-zA-Z]", "");
            String[] v1s = v1.split(":");
            String i;
            if ((i = v1s[0]).equals(v1s[1])) {
                List<String> ll;
                if (this.equalRow.containsKey(i)) {
                    ll = this.equalRow.get(i);
                    ll.add(s.replaceAll("[0-9]", ""));
                } else {
                    ll = new ArrayList<>();
                    ll.add(s.replaceAll("[0-9]", ""));
                    this.equalRow.put(i, ll);
                }
            }
        });
    }

    /**
     * 判断获取上级单元格列
     */
    private String findParentCell(String c) {
        int row = Integer.valueOf(c.replaceAll("[a-zA-Z]", "")) - 1;
        String cell = c.replaceAll("[0-9]", "");
        if (this.equalRow.containsKey(String.valueOf(row))) {
            List<String> between = this.equalRow.get(String.valueOf(row));
            for (String b : between) {
                String[] bs = b.split(":");
                String bs1 = bs[0];
                String bs2 = bs[1];
                if (compareLetter(cell, bs1) >= 0 &&
                        compareLetter(cell, bs2) <= 0) {
                    return bs1 + row;
                }
            }
        }
        return null;
    }

    /**
     * Excel排序字母比较
     */
    private int compareLetter(String s1, String s2) {
        // 同长度比较
        if (s1.length() == s2.length()) {
            return s1.compareTo(s2);
        } else {
            // 不同长度,长者优胜
            return s1.length() - s2.length();
        }
    }
}
